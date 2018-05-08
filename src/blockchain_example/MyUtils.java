package blockchain_example;

//this class will be a home for any utility methods I may need
public class MyUtils {
	
	public static String zeros(int len){
		
		StringBuilder builder = new StringBuilder();
		
		for(int i=0; i<len; i++){
			builder.append("0");
		}
		
		return builder.toString();
	}

}