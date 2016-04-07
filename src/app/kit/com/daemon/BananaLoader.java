package app.kit.com.daemon;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;

import app.kit.com.util.Helpers;
import app.kit.service.seedlink.GenerateMiniSeed;
import app.kit.vo.Event;
import edu.sc.seis.seisFile.mseed.Btime;
import edu.sc.seis.seisFile.mseed.DataRecord;
import edu.sc.seis.seisFile.mseed.SeedRecord;

/**
 * Mango api starter Loader Class
 * @author jman
 *
 */
public class BananaLoader implements IBananaLoader{

	private String confFile; 
	private List<Event> events;
	private GenerateMiniSeed gm;
	
	PropertiesConfiguration config = new PropertiesConfiguration();

	public BananaLoader(String confFile) throws ConfigurationException {
		this.confFile = confFile;
		this.events = new ArrayList<>();
		this.gm = new GenerateMiniSeed();
	}

	@Override
	public void startEngine() throws Exception {

		System.out.println("Start program...");
		parseConf();
		
		
		int c = 0;
		for(Event event : events) {
			
			// read file from folder
			List<File> files = (List<File>) FileUtils.listFiles(new File(event.getInFolder()), null, true);
			int tot = files.size();
			for(File file : files) {
				
				// read miniseed
				DataInput input;
				DataOutputStream dos = null;
				System.out.println(">> file loaded(" + (++c) + "/" + tot + ")." + " name:" + file.toString());
				try {
					
					input = new DataInputStream(new FileInputStream(file));	
					while(true) {
						DataRecord dr = (DataRecord) SeedRecord.read(input);

						// trim miniseed
						DataRecord dr2 = gm.trimPacket(event.getStBtime(), event.getEtBtime(), dr, true);
						if ( dr2 != null ) {
							if ( dos == null ) {
								String filename = Helpers.getFileName(dr2.getHeader().getNetworkCode().trim(), dr2.getHeader().getStationIdentifier().trim(), 
										dr2.getHeader().getLocationIdentifier().trim(), dr2.getHeader().getChannelIdentifier().trim(), dr2.getHeader().getStartBtime());
								
								String outFolder = event.getOutFolder();
								if ( !outFolder.endsWith("/") ) outFolder = outFolder + "/"; 
								String fullPathFilename = outFolder + filename;
								
								System.out.println(">>> write to file: " + fullPathFilename);
								
								FileUtils.forceMkdir(new File(outFolder));
								dos = new DataOutputStream(new FileOutputStream(fullPathFilename));
							} 
							dr2.write(dos);
						}
					}
				} catch (EOFException e) {
				} catch (Exception e) {
					System.out.println("Error occurred. " + e);
				}
				
				if ( dos != null) {
					dos.close();
				}
			}
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					stopEngine();
				} catch (Exception e) {
					System.out.println("There is error in program. " + e.toString());
				}
			}
		});
	}

	@Override
	public void stopEngine() throws Exception {
		System.out.println("Stop program.");
	}
	
	private void parseConf() throws ConfigurationException, ParseException {
		config.load(new File(confFile));
		
		
		int cnt = config.getInt("event.cnt");
		
		System.out.println("*************************************");
		System.out.println("> request event. cnt:" + cnt);
		for(int i = 0; i<cnt; i++) {
			String strEventTime = config.getString("event." + i + ".time");
			int eventTimeAfter = config.getInt("event." + i + ".after");
			int eventTimeBefore = config.getInt("event." + i + ".before");
			
			String inFolder = config.getString("event." + i + ".in");
			String outFolder = config.getString("event." + i + ".out");
			
			Btime tBtime = Helpers.getBtime(strEventTime, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
			
			Calendar tCa = tBtime.convertToCalendar();
			long tBtimeLong = tCa.getTimeInMillis();
			long stBtimeLong = tBtimeLong - eventTimeBefore*1000;
			long etBtimeLong = tBtimeLong + eventTimeAfter*1000;
			
			Calendar stCa = Calendar.getInstance(); stCa.setTimeInMillis(stBtimeLong);
			Calendar etCa = Calendar.getInstance(); etCa.setTimeInMillis(etBtimeLong);
			
			Btime stBtime = new Btime(stCa.getTime());
			Btime etBtime = new Btime(etCa.getTime());
			
			Event event = new Event();
			event.setTime(tBtime);
			event.setStBtime(stBtime);
			event.setEtBtime(etBtime);
			event.setInFolder(inFolder);
			event.setOutFolder(outFolder);
			
			events.add(event);
			
			System.out.println(">> contents:" + event.toString());
		}
		System.out.println("*************************************");
	}
}
