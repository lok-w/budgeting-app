package com.example.budgeting;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class Transaction {
    private String id;
    private Date date;
    private double amount;
    private Account account;
    private String note;
    private SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");

    public Transaction(String date, double amount, Account account, String note) throws ParseException {
        this.id = uuid();
        this.date = ft.parse(date);
        this.amount = amount;
        this.account = account;
        this.note = note;

        //Use the constructor of Transfer instead to ensure a pair of transactions are created
        if (!(this instanceof Transfer)){
            account.addTransaction(this.id, this);
        }

    }


    public void removeTransaction(){
        this.getAccount().removeTransaction(this.getId());
    }



    public static String uuid() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, 5);
    }


    public String getId() {
        return id;
    }

    public Date getDate() {
        return this.date;
    }

    public String getStringDate() {
        return this.ft.format(this.date);
    }

    public double getAmount() {
        return amount;
    }

    public Account getAccount() {
        return account;
    }

    public String getNote() {
        return note;
    }

    //only Expense will have Category
    public String getCategory() {
        return "";
    }

    //only Transfer will have PairTransactionID
    public String getPairTransactionID() {
        return "";
    }

    //only Transfer will have Payer
    public Account getPayer() {return null;}

    //only Transfer will have Payee
    public Account getPayee() {return null;}


    public String getType() {
        return this.getClass().getSimpleName();
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setNote(String note) {
        this.note = note;
    }


}


