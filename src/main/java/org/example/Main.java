package org.example;

import org.jetbrains.annotations.NotNull;

import java.io.ObjectOutputStream;
import java.io.*;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main implements Serializable {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static ArrayList<Wallet> walletlist = new ArrayList<>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();
    public static float minimumTransaction = 10;
    public static ArrayList<Wallet> wallet = new ArrayList<>();
    public static Wallet walletA;
    public static Wallet walletB;
    public static Wallet coinbase;
    public static Wallet miner;
    public static ArrayList<Integer> ids = new ArrayList<>();
    public static int walletamountTEST =5;
    public static Transaction genesisTransaction;

    public static void main(String[] args) throws NotSerializableException, java.lang.ClassNotFoundException, NoSuchProviderException {
        //add our blocks to the blockchain ArrayList:
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        //Create wallets:
        for (int i=0; i<walletamountTEST; i++)
        {
            walletlist.add(i, new Wallet());
        }
        FULL_DESERIALIZE();
        System.out.println(blockchain.get(blockchain.size()-1).transactions);

//        код корневого блока, который положит сам в себя 100 валюты         //для тестов класть 100 в кошелек А
//        Block block1 = blockchain.get(blockchain.size() - 1); //думать о том, почему, если так, то прошлые хэши не эквивалентны (можно в целом вывести, но не суть важно
        checkcost();
//        System.out.println(genesisTransaction.getInputsValue());
        System.out.println(genesisTransaction.getOutputsValue());

//        System.out.println(blockchain.get(0).transactions.get(0).outputs.get(0).value);
        Block block1 = new Block(blockchain.get(blockchain.size() - 1).hash);
        block1.addTransaction(walletlist.get(1).sendFunds(walletlist.get(0), 4));
        addBlock(block1);
//        block1.addTransaction(walletlist.get(0).sendFunds(walletlist.get(2), minimumTransaction));
//        block1.addTransaction(walletlist.get(0).sendFunds(walletlist.get(3), minimumTransaction));
//        System.out.println(blockchain.get(1).transactions.get(2).outputs.get(1)); //помни эту строку, она гласит, что мы правим миром \\ПРОВЕРЬ ЧЕРЕЗ isMine> есть вопросики

//        System.out.println(blockchain.size());
//        System.out.println(walletlist.get(0).Balance);
        System.out.println(UTXOs.get(Integer.toString(0)));
        System.out.println(UTXOs.get(Integer.toString(1)));
        System.out.println(UTXOs.get(Integer.toString(2)));
        checkcost();
        isChainValid();
//        FULL_SERIALIZE();
        System.out.println(UTXOs);
        System.out.println(blockchain.get(blockchain.size()-1).transactions);
//
//        System.out.println(walletlist.get(0).getthisBalance());
//        System.out.println(walletlist.get(1).getthisBalance());

//
}



    public static void processTransaction(){


    }
    public static void SerializeBlocks(ArrayList<Block> arrayListname) {
        try {
            FileOutputStream fos = new FileOutputStream("blockchainData");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(arrayListname);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    public static void SerializeTimer(ArrayList<Integer> arrayListname) {
        try {
            FileOutputStream fos = new FileOutputStream("integerdata");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(arrayListname);
            oos.close();
            fos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    public static void checkcost(){
        for (int i=0; i<walletamountTEST; i++)
        {
        walletlist.get(i).getBalance();
        }
        for (int i=0; i<walletamountTEST; i++)
        {  System.out.println("Wallet"+ i +" TOTAL is: " + walletlist.get(i).getthisBalance());}

    }
    public static void FULL_DESERIALIZE() throws ClassNotFoundException, java.lang.ClassNotFoundException{
        DeserializeBlock();
        DeserializeWallet();
        DeserializeGenesis();
        DeserializeMap();
        deserializeinteger();

    }
    public static void FULL_SERIALIZE() throws NotSerializableException {
        SerializeBlocks(blockchain);
        SerializeWallets(walletlist);
        SerializeHashMap(UTXOs);
        SerializeGenesis();
        SerializeTimer(ids);
    }
    public static void DeserializeGenesis() throws java.lang.ClassNotFoundException{
        try
        {
            FileInputStream fis = new FileInputStream("genesisData");
            ObjectInputStream ois = new ObjectInputStream(fis);

            genesisTransaction = (Transaction) ois.readObject();

            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            ArrayList<TransactionOutput> outputs = new ArrayList<>();
            genesisTransaction = new Transaction(walletlist.get(0).publicKey, walletlist.get(0).publicKey, 1000000f, null, outputs);
            genesisTransaction.generateSignature(walletlist.get(0).privateKey);	 //manualy sign the genesis transaction
            genesisTransaction.transactionId = "0"; //manually set the transaction id
            ids.add(0,0);
            ids.trimToSize();
            genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value,null)); //manually add the Transactions Output
            UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
            System.out.println("Creating and Mining Genesis block... ");
            Block genesis = new Block("0");
            genesis.addTransaction(genesisTransaction);
            addBlock(genesis);
            return;
        }

    }
    public static void DeserializeMap() throws java.lang.ClassNotFoundException{
        try
        {
            FileInputStream fis = new FileInputStream("Mapdata");
            ObjectInputStream ois = new ObjectInputStream(fis);

            UTXOs = (HashMap<String, TransactionOutput>) ois.readObject();

            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            return;
        }
    }
    public static void DeserializeWallet() throws ClassNotFoundException{
        try
        {
            FileInputStream fis = new FileInputStream("Walletdata");
            ObjectInputStream ois = new ObjectInputStream(fis);

            walletlist = (ArrayList) ois.readObject();

            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            return;
        }
    for(int i=0; i<walletlist.size();i++)
    {
//            Transaction rewardTransaction = new Transaction(walletlist.get(0).publicKey, walletlist.get(i).publicKey, walletlist.get(i).getBalance(), null);
//            rewardTransaction.asReward = true;
//            rewardTransaction.generateSignature(walletlist.get(0).privateKey);	 //manually sign the genesis transaction
//            rewardTransaction.transactionId = rewardTransaction.calulateHash(); //manually set the transaction id
//            rewardTransaction.outputs.add(new TransactionOutput(rewardTransaction.reciepient, rewardTransaction.value, rewardTransaction.transactionId)); //manually add the Transactions Output
//            UTXOs.put(rewardTransaction.outputs.get(0).id, rewardTransaction.outputs.get(0)); //its important to store our transaction in the UTXOs list.
//            walletlist.get(i).getBalance();
        }
    }
    public static void exit(Wallet whoexits){
    }
    public void enter(){

    }

    public static void DeserializeBlock(){
        try
        {
            FileInputStream fis = new FileInputStream("blockchaindata");
            ObjectInputStream ois = new ObjectInputStream(fis);

            blockchain = (ArrayList) ois.readObject();

            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            return;
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }

        //Verify list data
        for (Block blocks : blockchain) {
            System.out.println(blocks);
        }
    }
    public static void deserializeinteger(){
        try
        {
            FileInputStream fis = new FileInputStream("integerdata");
            ObjectInputStream ois = new ObjectInputStream(fis);

            ids = (ArrayList) ois.readObject();

            ois.close();
            fis.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
            return;
        }
        catch (ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }

        //Verify list data
        for (Block blocks : blockchain) {
            System.out.println(blocks);
        }
    }

    public static void SerializeHashMap(HashMap<String, TransactionOutput> mapname)throws NotSerializableException{
        try {
            FileOutputStream fos = new FileOutputStream("Mapdata");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mapname);
            oos.flush();
            oos.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static void SerializeGenesis () {
            try {
                FileOutputStream fos = new FileOutputStream("genesisdata");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(genesisTransaction);
                oos.close();
                fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    public static void SerializeWallets (ArrayList <Wallet> wallets) {
            try {
                FileOutputStream fos = new FileOutputStream("Walletdata");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(wallets);
                oos.close();
                fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        public static @NotNull Boolean isChainValid () {
            Block currentBlock;
            Block previousBlock;
            //commented String hashTarget = new String(new char[difficulty]).replace('\0', '0');
            HashMap<String, TransactionOutput> tempUTXOs = new HashMap<>(); //a temporary working list of unspent transactions at a given block state.
            tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

            //loop through blockchain to check hashes:
            for (int i = 1; i < blockchain.size(); i++) {
                currentBlock = blockchain.get(i);
                previousBlock = blockchain.get(i - 1);

                //compare registered hash and calculated hash:
                if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                    System.out.println("#Current Hashes not equal");
                    return false;
                }
                //compare previous hash and registered previous hash
                if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                    System.out.println("#Previous Hashes not equal");
                    return false;
                }
                //check if hash is solved
                if (!currentBlock.mined) { //(!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) { //edited
                    System.out.println("#This block hasn't been mined");
                    return false;
                }

                //loop thru blockchains transactions:
                TransactionOutput tempOutput;
                for (int t = 0; t < currentBlock.transactions.size(); t++) {
                    Transaction currentTransaction = currentBlock.transactions.get(t);

                    if (!currentTransaction.verifySignature()) {
                        System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                        return false;
                    }
                    if (!currentTransaction.asReward) { //added
//                        if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
//                            System.out.println(currentTransaction.getInputsValue());
//                            System.out.println(currentTransaction.getOutputsValue());
//                            System.out.println("#Inputs are not equal to outputs on Transaction(" + t + ")");
////                            return false;
//                        }

                        for (TransactionInput input : currentTransaction.inputs) {
                            tempOutput = tempUTXOs.get(input.transactionOutputId);

                            if (tempOutput == null) {
                                System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                                return false;
                            }

                            if (input.UTXO.value != tempOutput.value) {
                                System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                                return false;
                            }

                            tempUTXOs.remove(input.transactionOutputId);
                        }
                    } //added

                    for (TransactionOutput output : currentTransaction.outputs) {
                        tempUTXOs.put(output.id, output);
                    }

                    if (currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) {
                        System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                        return false;
                    }

                    if (!currentTransaction.asReward) { //added
                        if (currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
                            System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                            return false;
                        }
                    } //added

                }

            }
            System.out.println("Blockchain is valid");
            return true;
        }

    public static void addBlock(Block newBlock) {

        newBlock.mineBlock();
        //добавление
        blockchain.add(newBlock);
    }
    public static void restore(Block newBlock){
        newBlock.mineBlock();
        //добавление
//        if(!newBlock.previousHash.equals("0")) {
//            Transaction rewardTransaction = new Transaction(walletlist.get(0).publicKey, walletlist.get(walletamountTEST-1).publicKey, 1f, null, null);
//            System.out.println("I AM GROOT I MINED 1 COIN DONT BE AFRAID TEST");
//            rewardTransaction.asReward = true;
//            rewardTransaction.generateSignature(walletlist.get(2).privateKey);	 //manually sign the genesis transaction
//            rewardTransaction.transactionId = rewardTransaction.calulateHash(); //manually set the transaction id
//            rewardTransaction.outputs.add(new TransactionOutput(rewardTransaction.reciepient, rewardTransaction.value, rewardTransaction.transactionId)); //manually add the Transactions Output
//            UTXOs.put(rewardTransaction.outputs.get(0).id, rewardTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
//            newBlock.addTransaction(rewardTransaction);
//        }
    }
}


