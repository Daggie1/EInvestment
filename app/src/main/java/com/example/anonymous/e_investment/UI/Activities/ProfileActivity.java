package com.example.anonymous.e_investment.UI.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.anonymous.e_investment.R;
import com.example.anonymous.e_investment.fragments.ProfileFragment;
import com.example.anonymous.e_investment.models.fragmentActivityLinker;

public class ProfileActivity extends fragmentActivityLinker {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        addFragment(new ProfileFragment(),R.id.fragmentContainer);
    }
}
