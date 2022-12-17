package org.example;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Block implements Serializable {

    public String hash;
    public String previousHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<>(); //our data will be a simple message.

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
    private String timeStamp;
    public int nonce;
    public long mineStartTimeStamp;
    public long mineEndTimeStamp;
    public String dificultyString = "0000";
    public boolean mined = false;
    private int index;
    private String date;
    private String data;
    private int difficulty = 4; //сложность 4 = "0000";

    //Block Constructor.
    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = Long.toString(new Date().getTime());
        this.hash = calculateHash(); //Making sure we do this after we set the other values.
        this.index = index;// служебный указатель для удобства
        this.date = calculateDate();//дата(так как таймстамп немного не то)
        this.data =data;//при генерации получит наполнение через геттер
        this.transactions=transactions;
    }
    public String calculateHash() {
        String calculatedhash = StringUtil.applySha256(previousHash + timeStamp + Integer.toString(nonce) + merkleRoot + Long.toString(mineStartTimeStamp));
        return calculatedhash;

    }
    public String calculateDate(){
        // Initializing the first formatter
        String pattern = "MM/dd/yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String simpdate = simpleDateFormat.format(new Date());
        return simpdate;
    }
    //Increases nonce value until hash target is reached.
    public void mineBlock() {
        mineStartTimeStamp = new Date().getTime(); //added
        merkleRoot = StringUtil.getMerkleRoot(this.transactions);
        while (!hash.substring(0, difficulty).equals(dificultyString)) {
            nonce++;
            hash = calculateHash();
        }
        mineEndTimeStamp = new Date().getTime();
        mined = true;
//        System.out.println("Block Mined!!! " + (mineEndTimeStamp - mineStartTimeStamp) + " ms: " + hash); //edited
    }

    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) {
            return false;
        }
        if ((!"0".equals(previousHash)) && !transaction.asReward) {
            if ((transaction.processTransaction() != true)) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }

        this.transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }
}

