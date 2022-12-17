package org.example;

import java.io.Serializable;
import java.security.PublicKey;
public class TransactionOutput implements Serializable {

    public String id;
    public PublicKey reciepient; //also known as the new owner of these coins.
    public float value; //the amount of coins they own
    public String parentTransactionId; //the id of the transaction this output was created in

    //Constructor
    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
//        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient) + Float.toString(value) + parentTransactionId);
        this.id = Integer.toString(calc());
    }
    public int calc(){
        System.out.println(Main.ids.size()+" asdasdad");
        int meme = Main.ids.size();
    Main.ids.add(Main.ids.size(), Main.ids.size());
    return meme;
    }

    //Check if coin belongs to you
    public boolean isMine(PublicKey publicKey) {
        return (publicKey == reciepient);
    }

}

