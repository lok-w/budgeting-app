package com.example.budgeting;

import java.text.ParseException;

public class Expense extends Transaction{
    private String category;

    public Expense(String date, double amount, Account account, String category, String note) throws ParseException {
        super(date, amount, account, note);
        this.setAmount(-this.getAmount());

        //only Expense has category
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
