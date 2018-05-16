package test_cases;

import blockchain_example.Block;
import blockchain_example.Blockchain;

//this test will be the same as BlockchainTest1 however we will
//also add a corrupted block and check if its still valid
//to run this test simple run the main method in this class
public class BlockchainTest2 {

	public static void main(String[] args) {
		//initialise our blockchain... (genesis block will be mined automatically)
		Blockchain blockchain = new Blockchain(3);
		
		//add blocks to chain with any some data
	    blockchain.addBlock(blockchain.newBlock("Lets create our second block"));
	    blockchain.addBlock(blockchain.newBlock("3rd block with some data"));
	    blockchain.addBlock(blockchain.newBlock("This is probably the coolest blockchain you'll ever see"));
	    blockchain.addBlock(blockchain.newBlock("I can't believe how fun this is!")); 
	    blockchain.addBlock(blockchain.newBlock("Okay lets stop now"));

	    System.out.println("Check if our blockchain is valid: " + blockchain.blockchainIsValid());
		System.out.println(blockchain);

		//Lets try add a corrupted block into the blockchain
		blockchain.addBlock(new Block(15, System.currentTimeMillis(), "aaaabbb", "Block invalid"));

		//now test if blockchain is still valid... (it shouldn't be because of our corrupted block
		System.out.println("Check if our blockchain is valid: " + blockchain.blockchainIsValid());
	  }
}
