package com.example.anonymous.e_investment.UI.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.anonymous.e_investment.R;
import com.example.anonymous.e_investment.fragments.TransactionFragment;
import com.example.anonymous.e_investment.models.fragmentActivityLinker;

public class TransactionActivity extends fragmentActivityLinker {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        addFragment(new TransactionFragment(),R.id.fragmentContainer);
    }
}
