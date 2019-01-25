package com.example.anonymous.e_investment.UI.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.anonymous.e_investment.R;
import com.example.anonymous.e_investment.fragments.GroupListsFragment;
import com.example.anonymous.e_investment.models.fragmentActivityLinker;
import com.google.firebase.auth.FirebaseAuth;

public class GroupListActivity extends fragmentActivityLinker {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        addFragment(new GroupListsFragment(),R.id.fragmentContainer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_product) {
            Intent newIntent=new Intent(GroupListActivity.this, CreateNewGroupActivity.class);
            newIntent.putExtra("start","new");
            startActivity(newIntent);
        }
        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(GroupListActivity.this,LoginActivity.class));
        }
        return true;
    }
}
