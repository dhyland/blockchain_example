package blockchain_example;

import java.util.*;

public class Blockchain {

	private int difficulty;
	private List<Block> blockchain;
	
	//static variables so they are shared between each instance. 
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); //list of all unspent transactions. 
	public static Wallet walletX;
	public static Wallet walletY;
	//any value for the minTransaction value
	public static float minTransaction = 0.1f;
	public static Transaction genesisTransaction;
	
	//constructor
	public Blockchain(int difficulty){
		
	    this.difficulty = difficulty;
	    blockchain = new ArrayList<>();
	    
	    //we need to at least mine the first block and add it to the chain
	    Block b = new Block(0, System.currentTimeMillis(), null, "First Block");
	    b.mineBlock(difficulty);
	    blockchain.add(b);
	    System.out.println("Genesis block has been mined!: " + b.getHash());
	  }
	
	public Block mostRecentBlock(){
		return blockchain.get(blockchain.size() -1);
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (Block block:blockchain) {
			builder.append(block).append("\n");
		}

		return builder.toString();
	}
	
	public void addBlock(Block newBlock){
		if(newBlock != null){
			newBlock.mineBlock(difficulty);
			blockchain.add(newBlock);
		}
	}
	
	public Block newBlock(String data){
		Block mostRecent = mostRecentBlock();
		Block newBlock = new Block(mostRecent.getIndex() + 1, System.currentTimeMillis(), mostRecent.getHash(), data);
		
		System.out.println("New block has been mined!: " + newBlock.getHash());
		
		return newBlock;
	}
	
	public boolean firstBlockIsValid(){
		
		Block firstBlock = blockchain.get(0);

		if(firstBlock.getIndex() != 0){
			return false;
		}

		if(firstBlock.getPreviousHash() != null){
			return false;
		}

		if(firstBlock.getHash() == null || !Block.calculateHash(firstBlock).equals(firstBlock.getHash())){
			return false;
		}

		return true;
	}

	public boolean newBlockIsValid(Block newBlock, Block previousBlock){
		
		if(newBlock != null  &&  previousBlock != null){
			if(previousBlock.getIndex() + 1 != newBlock.getIndex()){
				return false;
			}

			if(newBlock.getPreviousHash() == null || !newBlock.getPreviousHash().equals(previousBlock.getHash())){
				return false;
			}

			if(newBlock.getHash() == null || !Block.calculateHash(newBlock).equals(newBlock.getHash())){
				return false;
			}

			return true;
		}

		return false;
	}

	public boolean blockchainIsValid() {
		
		if(!firstBlockIsValid()){
			return false;
		}

		for(int i=1; i<blockchain.size(); i++){
			Block currentBlock = blockchain.get(i);
			Block previousBlock = blockchain.get(i-1);

			if(!newBlockIsValid(currentBlock, previousBlock)){
				return false;
			}
		}

		return true;
	}
	
}
