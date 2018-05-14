package blockchain_example;

import java.security.*;


public class TransactionOutput {

	public String id;
	//new owner of the coins (payee).
	public PublicKey payee;
	//amount of coins payee owns
	public float value;
	//the id of the transaction this output was created in
	public String parentTransactionID;
	
	//basic constructor
	public TransactionOutput(PublicKey payee0, float value0, String parentTransactionID0) {
		this.payee = payee0;
		this.value = value0;
		this.parentTransactionID = parentTransactionID0;
		this.id = MyUtils.applySha256(MyUtils.getStringFromKey(payee0)+Float.toString(value0)+parentTransactionID0);
	}
	
	//does this coin belong to me?
	public boolean isOwnedByMe(PublicKey publicKey) {
		return (publicKey == payee);
	}
	
}
