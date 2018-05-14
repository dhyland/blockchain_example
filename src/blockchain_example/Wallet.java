package blockchain_example;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.*;

//This class will hold public and private keys.
public class Wallet {
	
	//privateKey is used to "sign" a transaction. This will be used to that you can't spend other peoples coins and they can't spend yours.  
	//Should be kept a secret to other users as if other users have your privateKey it is a major security flaw. 
	public PrivateKey privateKey;
	
	//publicKey will hold the users address. Okay to share with others as we need it to receive payment etc.
	//Not a big deal if others have it since all they can do it give you stuff. Kinda like IBAN number in bank accounts.
	public PublicKey publicKey;
	
	//UTXO = Unspent Transaction Output. This will be unique for the wallet.
	public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	
	//Overwrite no-args constructor.
	public Wallet(){
		generateKeys();
	}
	
	//Generates a public and private key as key pair.
	public void generateKeys(){
		try{
			//initial setup
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			
			// Initialise the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);
			KeyPair keyPair = keyGen.generateKeyPair();
			
			//assign the actual keys to their values
			publicKey = keyPair.getPublic();
			privateKey = keyPair.getPrivate();	
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	//returns the balance as of the wallet as a float (should be pretty self explanatory)
	public float getBalance(){
		float total = 0;	
		for(HashMap.Entry<String, TransactionOutput> item: Blockchain.UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			if(UTXO.isOwnedByMe(publicKey)) { //if output belongs to me ( if coins belong to me )
				UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
				total = total + UTXO.value ; 
            }
        }  
		return total;
	}
	
	//Generates and returns a new transaction from this wallet.
	public Transaction sendFunds(PublicKey _recipient, float value){
		
		//check if we have enough balance in wallet to send funds
		if(getBalance() < value){
			System.out.println("ERROR: Not Enough funds to complete transaction. Transaction Declined.");
			return null;
		}
		
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		float total = 0;
		
		//loop through each item in UTXOs entrySet
		for(Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			total = total + UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
			if(total > value){
				break;
			}
		}
		
		Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
		newTransaction.generateSignature(privateKey);
		
		//look through each input in inputs
		for(TransactionInput input: inputs){
			UTXOs.remove(input.transactionOutputID);
		}
		
		return newTransaction;
	}
	
}
