package com.example.anonymous.e_investment.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anonymous.e_investment.R;
import com.example.anonymous.e_investment.UI.Activities.CreateNewGroupActivity;
import com.example.anonymous.e_investment.UI.Activities.GroupInfoActivity;
import com.example.anonymous.e_investment.UI.Activities.SignupActivity;
import com.example.anonymous.e_investment.models.Contribution;
import com.example.anonymous.e_investment.models.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class GroupListsFragment extends Fragment {

    ArrayList<Group> mGrouplistitems = new ArrayList<>();
    ArrayList<Contribution> mContributionlistitems = new ArrayList<>();
    RecyclerView mRecyclerView;
    Group mGroupModel;
    FloatingActionButton fab;
LinearLayout no_item_layout;
    MyAdapter myAdapter = new MyAdapter(mGrouplistitems);

    DatabaseReference dbrefcont;
    Query dbRef ;
    public GroupListsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        updateRecyclerView();
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbrefcont = FirebaseDatabase.getInstance().getReference("Contribution");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_group_lists, container, false);
        getActivity().setTitle("Your Groups");
        fab=(FloatingActionButton) view.findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.group_recyclerview);
       LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        no_item_layout=(LinearLayout) view.findViewById(R.id.no_item_layout);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
fab.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(getActivity(), CreateNewGroupActivity.class));
    }
});
        if (mGrouplistitems.size() <= 0) {

            no_item_layout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            mRecyclerView.setAdapter(myAdapter);}

        mRecyclerView.setLayoutManager(layoutManager);
        return view;
    }

    public void updateRecyclerView() {
        String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("Contribution").orderByChild("userId").equalTo(user);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                   mGrouplistitems.clear();
                    for (DataSnapshot contributionSnapShot : dataSnapshot.getChildren()) {

                        Query query=FirebaseDatabase.getInstance().getReference("Groups").orderByChild("groupId").equalTo(contributionSnapShot.child("group1d").getValue(String.class));
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    //mTransactionlistitems.clear();
                                    for (DataSnapshot groupSnapShot : dataSnapshot.getChildren()) {

                                        Group groupmodel = groupSnapShot.getValue(Group.class);
                                        mGrouplistitems.add(groupmodel);
                                    }

                                }
                                myAdapter.notifyDataSetChanged();

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                       }

                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<Group> list;

        public MyAdapter(ArrayList<Group> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            // create a new view

            return new MyViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Group group = mGrouplistitems.get(position);
            holder.bind(group);


        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView groupName, groupamount;

        public CircularImageView groupImage;
        Group mgroupModel;

        public MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.group_item, parent, false));

           groupamount = (TextView) itemView.findViewById(R.id.balance);
            groupImage = (CircularImageView) itemView.findViewById(R.id.groupimage);
            groupName = (TextView) itemView.findViewById(R.id.group_name);
            itemView.setOnClickListener(this);

        }

        public void bind(Group group) {
            mgroupModel = group;
            groupamount.setText(mgroupModel.getTotalAmount());
            groupName.setText(mgroupModel.getGroup_name());
            Glide.with(getActivity()).load(mgroupModel.getPic_url()).into(groupImage);

        }

        @Override
        public void onClick(View view) {

            Intent newintent = new Intent(getActivity(), GroupInfoActivity.class);
            newintent.putExtra("selectedgroup_id", mgroupModel.getGroupId());
            newintent.putExtra("selectedgroup_name", mgroupModel.getGroup_name());
            startActivity(newintent);
        }
    }
}