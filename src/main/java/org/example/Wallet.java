package org.example;

import java.io.*;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Wallet implements Serializable {

    public PrivateKey privateKey;
    public PublicKey publicKey;
    public ArrayList<Transaction> transactions = new ArrayList<>();
    public float Balance;


    public Wallet() throws NoSuchProviderException{
        generateKeyPair();

    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(ecSpec, random); //256 
            KeyPair keyPair = keyGen.generateKeyPair();
            // Set the public and private keys from the keyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

//    public float getBalance() { //перезаписывает балансы у кошельков, к которым применена
//        float total = 0;
//        for (Map.Entry<String, TransactionOutput> item : Main.UTXOs.entrySet()) {
//            TransactionOutput UTXO = item.getValue();
//            if (UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
//                UTXOs.put(UTXO.id, UTXO); //add it to our list of unspent transactions.
//                if (total<=Math.abs(UTXO.value) && UTXO.value>0) {
//                    total += UTXO.value;
//                }
//                else{}
//                if (UTXO.value < 0 )
//                {total +=UTXO.value;}
//                else{}
//            }
//        }
//        if (total != 0) {
//            setBalance(total); //в этой точке
//        }
//        total=Balance;
//        return total;
//    }
public float getthisBalance() { //перезаписывает балансы у кошельков, к которым применена
    float total = 0;
    ArrayList <TransactionOutput> list = new ArrayList<>();
    for (int i=Main.ids.size()-Main.UTXOs.size(); i<Main.ids.size();i++){
        PublicKey tempKEY = Main.UTXOs.get(Integer.toString(i)).reciepient;
        if (tempKEY.equals(publicKey)){
            total+= Main.UTXOs.get(Integer.toString(i)).value;
            break;
        }
    }

    return total;
}
public float getBalance() { //перезаписывает балансы у кошельков, к которым применена
    float total = 0;
    for (Map.Entry<String, TransactionOutput> item : Main.UTXOs.entrySet()) {
        TransactionOutput UTXO = item.getValue();
        if (UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
            Main.UTXOs.put(UTXO.id, UTXO); //add it to our list of unspent transactions.
            if (total<=Math.abs(UTXO.value) && UTXO.value>0) {
                total += UTXO.value;
            }
            else{}
//            if (UTXO.value < 0 )
//            {total +=UTXO.value;}
//            else{}
        }
    }
    return total;
}
//    public float gettrueBalance() { //перезаписывает балансы у кошельков, к которым применена
//        float total = 0;
//        for (int i=0; i<Main.UTXOs.size(); i++){
//            HashMap <String,TransactionOutput> UTXO = Main.UTXOs;
//            if (UTXO.get(i).reciepient == publicKey){
//                total+=UTXO.get(i).value;
//            }
//
//        return total;
//        }

//            if (UTXO.value < 0 )
//            {total +=UTXO.value;}
//            else{}


//        return total;
//    }

        public Transaction sendFunds(Wallet wallet, float value) {
        PublicKey _recipient= wallet.publicKey;
        if (Math.abs(getthisBalance()) < Math.abs(value)) {
//            if (Math.abs(getthisBalance()) < Math.abs(value)) {
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        ArrayList<TransactionInput> inputs = new ArrayList<>();
        ArrayList<TransactionOutput> outputs = new ArrayList<>();
        ArrayList <TransactionOutput> list = new ArrayList<>();
        float total = 0;
        float leftover = 0;
        for (int i=Main.ids.size()-Main.UTXOs.size(); i<Main.ids.size();i++){
            PublicKey tempKEY = Main.UTXOs.get(Integer.toString(i)).reciepient;
            total+= Main.UTXOs.get(Integer.toString(i)).value;
            inputs.add(new TransactionInput(Main.UTXOs.get(Integer.toString(i)).id));
            leftover = Main.UTXOs.get(Integer.toString(i)).value - value; //get value of inputs then the left over change:


        }
        System.out.println("SENDER HAD AT THIS MOMENT "+total);
        if (total<value){
        Transaction newTransaction = new Transaction(publicKey, _recipient, total-leftover, inputs, outputs);
        newTransaction.generateSignature(privateKey);
        return newTransaction;}
        else {
            Transaction newTransaction = new Transaction(publicKey, _recipient, leftover+value*2, inputs, outputs);
            newTransaction.generateSignature(privateKey);
            return newTransaction;}
//        for (TransactionInput input : inputs) {
//            UTXOs.remove(input.transactionOutputId);
//        }


    }


}
