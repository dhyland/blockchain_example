package test_cases;

import blockchain_example.Blockchain;

//this test will be basic test for Blockchain functionality
//ensure we can mine blocks and print the information
//to run this test simple run the main method in this class.
public class BlockchainTest1 {

	public static void main(String [] args){
		//initialise our blockchain... (genesis block will be mined automatically)
		Blockchain blockchain = new Blockchain(3);
		
		//add blocks to chain with any some data
	    blockchain.addBlock(blockchain.newBlock("Lets create our second block"));
	    blockchain.addBlock(blockchain.newBlock("3rd block with some data"));
	    blockchain.addBlock(blockchain.newBlock("This is probably the coolest blockchain you'll ever see"));
	    blockchain.addBlock(blockchain.newBlock("I can't believe how fun this is!")); 
	    blockchain.addBlock(blockchain.newBlock("Okay lets stop now")); 
	  
	    System.out.println("Check if our blockchain is valid: " + blockchain.blockchainIsValid());
	    //this will simply print out blockchain block by block... handy :)
	    System.out.println(blockchain);
	}
}
