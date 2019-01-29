package com.example.anonymous.e_investment.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anonymous.e_investment.R;
import com.example.anonymous.e_investment.models.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class TransactionFragment extends  Fragment{

    ArrayList<Transaction> mTransactionlistitems = new ArrayList<>();
    RecyclerView mRecyclerView;


    MyAdapter myAdapter = new MyAdapter(mTransactionlistitems);

String group_name,group_id;
    Query dbRef ;
    public TransactionFragment() {
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
        group_name=getActivity().getIntent().getStringExtra("groupname");
        group_id=getActivity().getIntent().getStringExtra("groupid");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_transaction_, container, false);
        getActivity().setTitle("Your Group Transaction");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.transaction_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        if (mTransactionlistitems.size() <= 0) {

            mRecyclerView.setVisibility(View.VISIBLE);

            mRecyclerView.setAdapter(myAdapter);}

        mRecyclerView.setLayoutManager(layoutManager);
        return view;
    }

    public void updateRecyclerView() {
        String user= FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("Transactions").orderByChild("userid").equalTo(user);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    mTransactionlistitems.clear();
                    for (DataSnapshot transactioonSnapShot : dataSnapshot.getChildren()) {
                   if(String.valueOf(transactioonSnapShot.child("").getValue(String.class))==group_id){
                       Transaction tmodel=transactioonSnapShot.getValue(Transaction.class);
                       mTransactionlistitems.add(tmodel);
                   }
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
        private ArrayList<Transaction> list;

        public MyAdapter(ArrayList<Transaction> Data) {
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
            Transaction transaction = mTransactionlistitems.get(position);
            holder.bind(transaction);


        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView groupName, amount,date,transactId;


        Transaction mtransactionModel;


        public MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.group_item, parent, false));

            groupName = (TextView) itemView.findViewById(R.id.group_name);
            amount = (TextView) itemView.findViewById(R.id.transaction_amount);
            transactId = (TextView) itemView.findViewById(R.id.transaction_id);
            date = (TextView) itemView.findViewById(R.id.transaction_date);
            itemView.setOnClickListener(this);

        }

        public void bind(Transaction transaction) {
            mtransactionModel = transaction;
            groupName.setText("to "+group_name);
            amount.setText(mtransactionModel.getamount_transacted());
            transactId.setText(mtransactionModel.getTransaction_id());

        }

        @Override
        public void onClick(View view) {


        }
    }

}
