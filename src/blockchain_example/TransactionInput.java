package blockchain_example;

public class TransactionInput {
	
	//Needed for referencing
	public String transactionOutputID;
	
	//this is unspent transaction output
	public TransactionOutput UTXO;
	
	//basic constructor
	public TransactionInput(String transactionOutputID0) {
		this.transactionOutputID = transactionOutputID0;
	}
}
