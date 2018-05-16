package test_cases;

import java.security.Security;
import blockchain_example.*;

//this is a test to check that all wallets, transactions and blockchain functionality is working correctly. 
//we will make 2 real transactions and get 1 transaction to purposely fail
public class WalletTest2 {

	public static void main(String [] args) {
		
		Blockchain blockchain = new Blockchain(3);
		
		//add our blocks to the blockchain ArrayList:
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup BC as a Security Provider
		
		//Create wallets:
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();		
		Wallet coinPurse = new Wallet();
		
		//create genesis transaction, which sends 100 coins to walletA: 
		Blockchain.genesisTransaction = new Transaction(coinPurse.publicKey, walletA.publicKey, 100f, null);
		Blockchain.genesisTransaction.generateSignature(coinPurse.privateKey);	 //manually sign the genesis transaction	
		Blockchain.genesisTransaction.transactionID = "0"; //manually set the transaction id
		Blockchain.genesisTransaction.tOutputs.add(new TransactionOutput(Blockchain.genesisTransaction.payee, Blockchain.genesisTransaction.value, Blockchain.genesisTransaction.transactionID)); //manually add the Transactions Output
		Blockchain.UTXOs.put(Blockchain.genesisTransaction.tOutputs.get(0).id, Blockchain.genesisTransaction.tOutputs.get(0)); //its important to store our first transaction in the UTXOs list.
		
		
		System.out.println("WalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletA is Attempting to send funds (40) to WalletB...");
		(blockchain.mostRecentBlock()).addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		System.out.println("WalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		System.out.println();
		blockchain.addBlock(blockchain.newBlock("Add 2nd block"));	
		System.out.println("WalletA Attempting to send more funds (1000) than it has...");
		(blockchain.mostRecentBlock()).addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
		System.out.println("WalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		System.out.println();
		blockchain.addBlock(blockchain.newBlock("Add 3rd block"));
		System.out.println("WalletB is Attempting to send funds (20) to WalletA...");
		(blockchain.mostRecentBlock()).addTransaction(walletB.sendFunds( walletA.publicKey, 20));
		System.out.println("WalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
		
		System.out.println("\nCheck if our blockchain is valid: " + blockchain.blockchainIsValid());
	}
}
