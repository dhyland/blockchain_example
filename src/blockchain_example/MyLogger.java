package blockchain_example;

import java.util.logging.*;

//basic class to log info
public class MyLogger {

	private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    static{
    	ConsoleHandler handler = new ConsoleHandler();
    	handler.setFormatter(new MyLogFormatter());
    	LogManager.getLogManager().reset();
    	LOGGER.addHandler(handler);
    }

    public static void sendStartupMessage(){
    	LOGGER.log(Level.INFO, "\n---------------------------------------------\n" + "Blockchain starting \n" + "---------------------------------------------");
    }

    public static void sendClientStartupMessage(){
        LOGGER.log(Level.INFO, "\n---------------------------------------------\n" + "Client is starting \n" + "---------------------------------------------");
    }

    public static void log(Level level, String message){
        LOGGER.log(level, message);
    }
}
