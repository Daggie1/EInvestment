package com.example.anonymous.e_investment.fragments;


import android.bluetooth.BluetoothHidDeviceAppQosSettings;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.example.anonymous.e_investment.UI.Activities.GroupListActivity;
import com.example.anonymous.e_investment.models.Contribution;

import com.example.anonymous.e_investment.models.Group;
import com.example.anonymous.e_investment.models.Member;
import com.example.anonymous.e_investment.mpesa.AddMembersActivity;
import com.example.anonymous.e_investment.mpesa.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupInfoFragment extends Fragment {
ImageView group_image,see_transaction;
TextView balance,username,joined_date,divided,total;

Button deposit;
String member_username,member_usernameid;
    String selected_groupId,selectedgroup_name;
    ArrayList<Member> mMemberlistitems = new ArrayList<>();

    RecyclerView mRecyclerView;
   Member mMemberModel;
    FloatingActionButton fab;

    MyAdapter myAdapter = new MyAdapter(mMemberlistitems);


    DatabaseReference dbrefcont;
    Query dbRef ;
    public GroupInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        updateRecyclerView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selected_groupId=getActivity().getIntent().getStringExtra("selectedgroup_id");
        selectedgroup_name=getActivity().getIntent().getStringExtra("selectedgroup_name");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View view= inflater.inflate(R.layout.fragment_group_info, container, false);
        getActivity().setTitle(selectedgroup_name);
     group_image=(ImageView) view.findViewById(R.id.groupimage);
        see_transaction=(ImageView) view.findViewById(R.id.see_transaction);
        balance=(TextView) view.findViewById(R.id.group_balance);
        username=(TextView) view.findViewById(R.id.username);
        joined_date=(TextView) view.findViewById(R.id.join_date);
        divided=(TextView) view.findViewById(R.id.divided);
        total=(TextView) view.findViewById(R.id.total_amount);
        deposit=(Button) view.findViewById(R.id.deposit);
        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
getcurrentUsername();
        Toast.makeText(getActivity(),""+selected_groupId,Toast.LENGTH_SHORT).show();
        loadGroupInfo(selected_groupId);
        loadContributionModule();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.members_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        if (mMemberlistitems.size() <= 0) {


            mRecyclerView.setVisibility(View.VISIBLE);

            mRecyclerView.setAdapter(myAdapter);}

        mRecyclerView.setLayoutManager(layoutManager);
     return  view;
    }

    public void loadGroupInfo(String selected_group){
        Query query= FirebaseDatabase.getInstance().getReference("Groups").orderByChild("groupId").equalTo(selected_group);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                   for(DataSnapshot groupsnap: dataSnapshot.getChildren()){
                       Group group=groupsnap.getValue(Group.class);

                       balance.setText(group.getTotalAmount());


                       Glide.with(getActivity()).load(group.getPic_url()).into(group_image);
                   }
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public String getcurrentUsername(){
        String memberid=FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query=FirebaseDatabase.getInstance().getReference("Members").orderByChild("member_id").equalTo(memberid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot membersnap:dataSnapshot.getChildren()){
                    Member membermodel=  membersnap.getValue(Member.class);
                   username.setText(membermodel.getUsername());

                }}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return member_username;
    }
public void loadContributionModule(){
    String memberid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query= FirebaseDatabase.getInstance().getReference("Contribution").orderByChild("userId").equalTo(selected_groupId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot contributionsnap:dataSnapshot.getChildren()){
                       Contribution membermodel=  contributionsnap.getValue(Contribution.class);
                      total.setText(membermodel.getAmoount_transacted());

                    }}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





    public void updateRecyclerView() {
        String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("Contribution").orderByChild("group1d").equalTo(selected_groupId);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    mMemberlistitems.clear();
                    for (DataSnapshot contributionSnapShot : dataSnapshot.getChildren()) {

                        Contribution contributionmodel = contributionSnapShot.getValue(Contribution.class);

                        Query query=FirebaseDatabase.getInstance().getReference("Members").orderByChild("member_id").equalTo(contributionmodel.getUserId());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    mMemberlistitems.clear();
                                    for (DataSnapshot memberSnapShot : dataSnapshot.getChildren()) {

                                        Member membermodel = memberSnapShot.getValue(Member.class);
                                        mMemberlistitems.add(membermodel);
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
        private ArrayList<Member> list;

        public MyAdapter(ArrayList<Member> Data) {
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
            Member member = mMemberlistitems.get(position);
            holder.bind(member);


        }

        @Override
        public int getItemCount() {
            return list.size();
        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView groupName;

        public CircularImageView groupImage;
        ImageView check;
        Member mmemberModel;

        public MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.member_item, parent, false));

            check=(ImageView) itemView.findViewById(R.id.addmembersimageview);
            groupImage = (CircularImageView) itemView.findViewById(R.id.member_image);
            groupName = (TextView) itemView.findViewById(R.id.member_name);
            itemView.setOnClickListener(this);

        }

        public void bind(Member member) {
            mmemberModel = member;

            groupName.setText(mmemberModel.getUsername());
            //Glide.with(AddMembersActivity).load(mmemberModel.get()).into(groupImage);

        }

        @Override
        public void onClick(View view) {


        }
    }
}
