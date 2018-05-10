package blockchain_example;

import java.security.*;
import java.util.*;

//block object will represent a single block inside a blockchain
public class Block {

	private String hash;
	private String previousHash;
	private String data;
	private int index;
	private long timestamp;
	private int nonce;
	
	// all args constructor. (although technically not all args)
	public Block(int index0, long timestamp0, String previousHash0, String data0){
		index=index0;
		timestamp=timestamp0;
		previousHash=previousHash0;
		data=data0;
		nonce = 0;
		hash = Block.calculateHash(this);
	}

	public String getHash(){
		return hash;
	}
	
	public String getPreviousHash(){
		return previousHash;
	}
	
	public String getData(){
		return data;
	}
	
	public int getIndex(){
		return index;
	}
	
	public long getTimestamp(){
		return timestamp;
	}
	
	//should be obvious but simply returns all information contained in the block in a readable string.
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("Block id: ").append(index).append(" { timestamp : ").append(new Date(timestamp)).append(", ").append("hash : ").append(hash).append(", ").
		append("data : ").append(data).append(", ").append("previousHash : ").append(previousHash).append(" }");
		
		
		return builder.toString();
		
	}
	
	public String str() {
		// TODO Auto-generated method stub
		return index + timestamp + data + previousHash + nonce;
	}
	
	//mines a block, lod= level of difficulty
	// supply how difficult a block is to mine as an int
	public void mineBlock(int lod) {
		nonce = 0;

		while (!getHash().substring(0,  lod).equals(MyUtils.zeros(lod))) {
			nonce++;
			hash = Block.calculateHash(this);
		}
	}
	
	// calculate the hash of a given block.
	public static String calculateHash(Block block){
		if (block != null){
			MessageDigest digest = null;
			
			try {
				digest = MessageDigest.getInstance("SHA-256");
			}
			catch (NoSuchAlgorithmException e){
				return null;
			}
			
			final StringBuilder builder = new StringBuilder();
			
			String txt = block.str();
			final byte bytes[] = digest.digest(txt.getBytes());
			
			for (final byte b : bytes){
				String hex = Integer.toHexString(0xff & b);
				
				if (hex.length() == 1){
					builder.append('0');
				}
				
				builder.append(hex);
			}

			return builder.toString();
		}
		
		return null;
	}
	
}
