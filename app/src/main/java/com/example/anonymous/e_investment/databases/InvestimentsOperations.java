package com.example.anonymous.e_investment.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class InvestimentsOperations {
    private SQLiteDatabase mDatabase;
    public InvestimentsOperations(Context context) {
        mDatabase=new EInvestimentsBaseHelper(context).getWritableDatabase();

    }
}
