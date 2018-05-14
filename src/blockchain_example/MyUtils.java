package blockchain_example;

import java.security.*;
import java.util.*;

//this class will be a home for any utility methods I may need
public class MyUtils {
	
	public static String zeros(int len){
		
		StringBuilder builder = new StringBuilder();
		
		for(int i=0; i<len; i++){
			builder.append("0");
		}
		
		return builder.toString();
	}

	public static String getStringFromKey(Key k){
		return Base64.getEncoder().encodeToString(k.getEncoded());
	}

	//SHA = Secure Hash Algorithm
	//SHA-256 algorithm generates an almost-unique, fixed size 256-bit (32-byte) hash
	//256 bit will do for this project as its for demonstration purposes only
	public static String applySha256(String s){
		
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(s.getBytes("UTF-8"));
			StringBuffer buffer = new StringBuffer();
			
			for(int i = 0; i < hash.length; i++){
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1){
					buffer.append('0');
				}
				buffer.append(hex);
			}
			return buffer.toString();
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	//ECDSA = Elliptic Curve Digital Signature Algorithm
	public static byte[] applyECDSASig(PrivateKey privateKey, String input){
		Signature sig;
		byte[] output = new byte[0];
		
		try{
			sig = Signature.getInstance("ECDSA", "BC");
			sig.initSign(privateKey);
			byte[] strByte = input.getBytes();
			sig.update(strByte);
			byte[] realSig = sig.sign();
			output = realSig;
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
		return output;
	}
	
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature){
		try{
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
			ecdsaVerify.initVerify(publicKey);
			ecdsaVerify.update(data.getBytes());
			
			return ecdsaVerify.verify(signature);
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}