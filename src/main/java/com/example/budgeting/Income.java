package com.example.budgeting;

import java.text.ParseException;

public class Income extends Transaction{
    public Income(String date, double amount, Account account, String note) throws ParseException {
        super(date, amount, account, note);
    }


}
