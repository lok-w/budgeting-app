package com.example.budgeting;

import java.util.ArrayList;
import java.util.HashMap;


public class Account {
    private String name;
    private String type;
    private HashMap<String, Transaction> transactions;

    Account(String type, String name){
        this.type = type;
        this.name = name;
        this.transactions = new HashMap<String, Transaction>();

    }

    //to print the name of the account
    @Override
    public String toString() {
        return name;
    }


    public void addTransaction(String transactionID, Transaction transaction) {
        this.transactions.put(transactionID,transaction);
    }

    public void removeTransaction(String transactionID) {
        this.transactions.remove(transactionID);
    }

    public Transaction getTransaction(String transactionID){
        return this.transactions.get(transactionID);
    }

    public ArrayList<Transaction> getTransactionList(){
        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
        for (Transaction transaction: this.transactions.values()) {
            transactionList.add(transaction);
        }
        return transactionList;
    }


    public String getType() {
        return type;
    }

    public String getStatistics() {
        double accountBalance = 0.0;
        int countTransaction = this.transactions.size();
        double maxExpenseAmount = 0.0;
        String maxTransactionNote = "";

        // ensure each category can have only 1 value
        HashMap<String,Double> categoryList = new HashMap<String, Double>();

        String maxCategory="";
        Double maxCategoryTotal = 0.0;


        for (Transaction transaction:this.transactions.values()) {
            accountBalance += transaction.getAmount();
            if (transaction instanceof Expense){
                if (transaction.getAmount()<maxExpenseAmount){
                    maxExpenseAmount = transaction.getAmount();
                    maxTransactionNote = transaction.getNote();
                }
                if (categoryList.containsKey(transaction.getCategory())){
                    categoryList.put(transaction.getCategory(),categoryList.get(transaction.getCategory()) + transaction.getAmount());
                }else {
                    categoryList.put(transaction.getCategory(),+ transaction.getAmount());
                }
            }
        }

        for (String category: categoryList.keySet()) {
            if (categoryList.get(category)< maxCategoryTotal){
                maxCategoryTotal = categoryList.get(category);
                maxCategory = category;
            }
        }

        String statistics = "Account Balance: $"+ accountBalance + "\n" + "# Transaction: "+ countTransaction + "\n" + "Max Expense: $" + Math.abs(maxExpenseAmount)  + "\n" + "Note of Max Expense: " + maxTransactionNote + "\n" + "Max Category: "+ maxCategory + "\n" + "Total amount of Max Category: $" + Math.abs(maxCategoryTotal) ;

        return statistics;

    }
}
