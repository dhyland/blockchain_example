package blockchain_example;

import java.security.*;
import java.util.ArrayList;

//class to deal with transactions on the blockchain
public class Transaction {

	public ArrayList<TransactionInput> tInputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> tOutputs = new ArrayList<TransactionOutput>();
	
	//address of the person paying
	public PublicKey payer;
	//address of the person receiving payment
	public PublicKey payee;
	
	//value of the transaction
	public float value;
	//id of the transaction. will be hash represented as a String.
	public String transactionID;
	//Signature to prevent others from using coins in your wallet
	public byte[] signature;
	
	//count of how many transactions have occurred.
	private static int numTransactions = 0;
	
	//all-args constructor
	public Transaction(PublicKey payer0, PublicKey payee0, float value0, ArrayList<TransactionInput> tInputs0){
		this.payer = payer0;
		this.payee = payee0;
		this.value = value0;
		this.tInputs = tInputs0;
	}
	
	//basic digital signature
	public void generateSignature(PrivateKey privateKey){
		String data = MyUtils.getStringFromKey(payer) + MyUtils.getStringFromKey(payee) + Float.toString(value)	;
		signature = MyUtils.applyECDSASig(privateKey,data);		
	}
	
	//verify the digital signature
	public boolean verifiySignature(){
		String data = MyUtils.getStringFromKey(payer) + MyUtils.getStringFromKey(payee) + Float.toString(value)	;
		return MyUtils.verifyECDSASig(payer, data, signature);
	}
	
	// calculate the hash for the transaction
	private String calculateTransatctionHash(){
		
		//helps keep count of transactions + helps ensure no two hashes are the same
		numTransactions++;
		
		return MyUtils.applySha256(
				MyUtils.getStringFromKey(payer) +
				MyUtils.getStringFromKey(payee) +
				Float.toString(value) + numTransactions
				);
	}
	
	//Method to process a transaction requested by two users
	public boolean processTransaction(){
		
		//check signature
		if(verifiySignature() == false){
			System.out.println("ERROR: Transaction Signature failed to verify");
			return false;
		}
				
		//get inputs
		for(TransactionInput i : tInputs){
			i.UTXO = Blockchain.UTXOs.get(i.transactionOutputID);
		}

		//ensure transaction is actually valid
		if(getInputsValue() < Blockchain.minTransaction){
			System.out.println("#Transaction Inputs to small: " + getInputsValue());
			return false;
		}
		
		//get value of inputs then the left over change:
		float leftOver = getInputsValue() - value;
		transactionID = calculateTransatctionHash();
		//send value to payee
		tOutputs.add(new TransactionOutput( this.payee, value,transactionID));
		//send the left over sum back to payer
		// left over sum can be considered "change"
		tOutputs.add(new TransactionOutput( this.payer, leftOver,transactionID));	
				
		//add outputs to Unspent list
		for(TransactionOutput o : tOutputs){
			Blockchain.UTXOs.put(o.id , o);
		}
		
		//remove transaction inputs from UTXO lists as spent:
		for(TransactionInput i : tInputs){
			//if we can't find a transaction then simply skip this iteration
			if(i.UTXO == null){
				continue;
			}
			Blockchain.UTXOs.remove(i.UTXO.id);
		}
		
		return true;
	}
	
	//returns the total sum of all inputs(UTXOs)
	public float getInputsValue() {
		float total = 0;
		for(TransactionInput i : tInputs) {
			if(i.UTXO == null){
				continue;
			}
			total = total + i.UTXO.value;
		}
		return total;
	}

	
	//returns the total sum of all outputs
	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutput o : tOutputs) {
			total = total + o.value;
		}
		return total;
	}
	
	
	
}
