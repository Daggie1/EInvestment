package com.example.anonymous.e_investment.UI.Activities;

import android.os.Bundle;

import com.example.anonymous.e_investment.R;
import com.example.anonymous.e_investment.fragments.CreateNewGroupfragment;
import com.example.anonymous.e_investment.models.fragmentActivityLinker;

public class CreateNewGroupActivity extends fragmentActivityLinker {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);

        addFragment(new CreateNewGroupfragment(),R.id.fragmentContainer);



    }
}
