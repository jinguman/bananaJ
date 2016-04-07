package app.kit.vo;

import edu.sc.seis.seisFile.mseed.Btime;
import lombok.Data;

@Data
public class Event {

	private Btime time;
	private Btime stBtime;
	private Btime etBtime;
	
	private String inFolder;
	private String outFolder;
}
