package com.example.budgeting;

import java.text.ParseException;

public class Transfer extends Transaction{
    private Account payer;
    private Account payee;
    private String pairTransactionID;

    public Transfer(String date, double amount, Account account, Account payee, String note) throws ParseException {
        super(date, -amount, account, note);
        this.payer = account;
        this.payee = payee;

        account.addTransaction(this.getId(), this);

        //create paired Transfer transaction
        new Transfer(date,amount, account, payee, note, this.getId());

    }

    public Transfer(String date, double amount, Account account, Account payee, String note, String transactionID) throws ParseException {
        super(date, amount, payee, note);

        //only Transfer has payer, payee, pairTransactionID
        this.payer = account;
        this.payee = payee;
        this.pairTransactionID = transactionID;

        //record the pairTransactionID in the note in both Transfer transactions
        this.setNote("Transfer from " + account + "(TID: " +transactionID + ") to " + payee + "(TID: " + this.getId() +") | " + this.getNote());

        Transfer payerTransaction = (Transfer) account.getTransaction(transactionID);

        payerTransaction.setPairTransactioID(this.getId());
        payerTransaction.setNote(this.getNote());
        payee.addTransaction(this.getId(), this);


    }


    public void removeTransaction(){

        //ensure to delete a pair of Transfer transactions
        if (this.getAccount() == this.getPayee()){
            this.getPayer().removeTransaction(this.getPairTransactionID());

        }else {
            this.getPayee().removeTransaction((this).getPairTransactionID());
        }
        this.getAccount().removeTransaction(this.getId());

    }

    public String getPairTransactionID() {
        return pairTransactionID;
    }

    public void setPairTransactioID(String pairTransaction) {
        this.pairTransactionID = pairTransaction;
    }

    public Account getPayer() {
        return payer;
    }

    public Account getPayee() {
        return payee;
    }
}
