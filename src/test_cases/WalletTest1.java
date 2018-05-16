package test_cases;

import java.security.Security;
import blockchain_example.*;

//this class will simply test the functionality of signatures for transactions using wallets
public class WalletTest1 {
	
	public static void main(String [] args) {
		
		//Setup BC as security provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
		
		//need 2 wallets
		Wallet walletA = new Wallet();
		Wallet walletB = new Wallet();
		
		//Test public and private keys
		System.out.println("Public Key: " + MyUtils.getStringFromKey(walletA.publicKey));
		System.out.println("Private Key: " + MyUtils.getStringFromKey(walletA.privateKey));
		
		//lets make a transaction between the two wallets
		Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
		//sign it
		transaction.generateSignature(walletA.privateKey);
		
		//check the signature is working correctly. can use public key for this
		System.out.println("Is signature verified?: " + transaction.verifiySignature());

	}
}
