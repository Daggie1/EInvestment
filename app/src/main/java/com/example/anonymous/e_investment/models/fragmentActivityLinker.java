package com.example.anonymous.e_investment.models;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class fragmentActivityLinker extends AppCompatActivity{
    public   void addFragment(Fragment fragment ,int fragmentContainer){
        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment1=fm.findFragmentById(fragmentContainer);
        if(fragment1==null){
            fragment1=fragment;
            fm.beginTransaction()
                    .add(fragmentContainer,fragment1)
                    .commit();
        }
    }
}
