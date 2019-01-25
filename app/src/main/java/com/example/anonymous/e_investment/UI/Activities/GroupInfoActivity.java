package com.example.anonymous.e_investment.UI.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.anonymous.e_investment.R;
import com.example.anonymous.e_investment.fragments.GroupInfoFragment;
import com.example.anonymous.e_investment.models.fragmentActivityLinker;

public class GroupInfoActivity extends fragmentActivityLinker {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
addFragment(new GroupInfoFragment(),R.id.fragmentContainer);

    }
}
