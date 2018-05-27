package blockchain_example;

import java.util.logging.*;

public class MyLogFormatter extends Formatter{
	
	@Override
	public String format(LogRecord logRecord){
		if(logRecord.getLevel() == Level.INFO){
			return logRecord.getMessage() + "\r\n";
		}
		else{
			//return level and message with new line
			return "[" + logRecord.getLevel() + "]" + logRecord.getMessage() + "\r\n";
		}
	}
}
 