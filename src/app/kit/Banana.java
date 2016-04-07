package app.kit;

import java.io.IOException;

import app.kit.com.daemon.BananaLoader;
import app.kit.com.daemon.IBananaLoader;

public class Banana {

	private void startBanana(String conf) throws Exception {
		IBananaLoader loader = new BananaLoader(conf);
		loader.startEngine();
	}
	
	public static void main( String[] args ) throws IOException {
    	
		// vm argument: -Dlogback.configurationFile="file:./home/conf/logback.xml"
    	//Logger logger = LoggerFactory.getLogger(MangoJC.class);
    	
		// check args
		if ( args.length == 0 ) {
			System.out.println("Usage:");
			System.out.println("$ banana.jar config.conf");
			return;
		} 
		
    	Banana main = new Banana();
		try {
			main.startBanana(args[0]);
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		
    }
	

}
