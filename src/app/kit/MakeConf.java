package app.kit;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import app.kit.com.util.Helpers;
import app.kit.vo.Trace;
import edu.sc.seis.seisFile.mseed.Btime;

public class MakeConf {

	private void make(String eCnt, String eT, String eB, String eA, String inF, String outF) throws Exception {
		
		Btime tBtime = Helpers.getBtime(eT, new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss"));
		
		System.out.println("event." + eCnt + ".time = " + eT);
		System.out.println("event." + eCnt + ".before = " + eB);
		System.out.println("event." + eCnt + ".after = " + eA);
		
		if ( !inF.endsWith("/") ) inF = inF + "/";
		DecimalFormat threeZero = new DecimalFormat("000");
		inF = inF + Trace.getBtimeToStringY(tBtime) + "/" + threeZero.format(tBtime.jday); 
		System.out.println("event." + eCnt + ".in = " + inF);
		
		if ( !outF.endsWith("/") ) outF = outF + "/";
		System.out.println("event." + eCnt + ".out = " + outF + Trace.getBtimeToStringYMDHMSNormal(tBtime));
	}
	
	public static void main( String[] args ) throws IOException {
    	
		/***
			java -classpath banana.jar app.kit.MakeConf 0 2015-01-01T12:34:56 60 120 d:/temp d:/temp 

			event.0.time = 2015-03-01T00:30:00
			event.0.before = 60
			event.0.after = 120
			event.0.in = d:/temp/2015/001
			event.0.out = d:/temp2
		 ***/
    	
		// check args
		if ( args.length != 6 ) {
			System.out.println("Usage:");
			System.out.println("$ java -classpath banana.jar app.kit.MakeConf 0 2015-01-01T12:34:56 60 120 d:/in d:/out");
			return;
		}
		
		String eventCnt = args[0];
		String eventTime = args[1];
		String eventBefore = args[2];
		String eventAfter = args[3];
		String inFolder = args[4];
		String outFolder = args[5];
		
    	MakeConf main = new MakeConf();
		try {
			main.make(eventCnt, eventTime, eventBefore, eventAfter, inFolder, outFolder);
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		
    }
	

}
