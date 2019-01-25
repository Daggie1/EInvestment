package com.example.anonymous.e_investment.mpesa;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.anonymous.e_investment.UI.Activities.GroupInfoActivity;
import com.example.anonymous.e_investment.UI.Activities.GroupListActivity;
import com.example.anonymous.e_investment.models.Contribution;

import com.example.anonymous.e_investment.models.Member;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class AddMembersActivity extends AppCompatActivity {
    ArrayList<Member> mMemberlistitems = new ArrayList<>();

    RecyclerView mRecyclerView;
Button add;
    FloatingActionButton fab;
    LinearLayout no_item_layout;
    MyAdapter myAdapter = new MyAdapter(mMemberlistitems);

    DatabaseReference dbRef ,dbrefcont;
    Query query;


    @Override
    public void onResume() {
        super.onResume();

        updateRecyclerView();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbRef = FirebaseDatabase.getInstance().getReference("Members");
        dbrefcont = FirebaseDatabase.getInstance().getReference("Contribution");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        add=(Button) findViewById(R.id.addmemberBtn) ;
        fab=(FloatingActionButton) findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) findViewById(R.id.group_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(AddMembersActivity.this);
        no_item_layout=(LinearLayout) findViewById(R.id.no_item_layout);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);

add.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Toast.makeText(AddMembersActivity.this,"added succesfully",Toast.LENGTH_LONG);
        startActivity(new Intent(AddMembersActivity.this, GroupListActivity.class));
    }
});
        if (mMemberlistitems.size() <= 0) {

            no_item_layout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            mRecyclerView.setAdapter(myAdapter);}

        mRecyclerView.setLayoutManager(layoutManager);
    }


    public void updateRecyclerView() {

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    mMemberlistitems.clear();
                    for (DataSnapshot memberSnap : dataSnapshot.getChildren()) {

                        Member membermodel = memberSnap.getValue(Member.class);
                        mMemberlistitems.add(membermodel);
                    }

                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Toast.makeText(getActivity(),"contributionlist=="+String.valueOf(mContributionlistitems.size()),Toast.LENGTH_LONG).show();
        /*for(int i=0;i<=mContributionlistitems.size()-1;i++){
            if (mContributionlistitems.get(i).getUserId() == FirebaseAuth.getInstance().getCurrentUser().getUid()) {
                Query query=FirebaseDatabase.getInstance().getReference("Groups").orderByChild("groupId").equalTo(mContributionlistitems.get(i).getGroup1d());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            mGrouplistitems.clear();
                            for (DataSnapshot groupSnapShot : dataSnapshot.getChildren()) {

                                Group groupmodel = groupSnapShot.getValue(Group.class);
                                mGrouplistitems.add(groupmodel);
                            }

                        }
                        Toast.makeText(getActivity(),"grouplist=="+String.valueOf(mGrouplistitems.size()),Toast.LENGTH_LONG).show();
                        myAdapter.notifyDataSetChanged();

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }*/
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<Member> list;

        public MyAdapter(ArrayList<Member> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(AddMembersActivity.this);
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
            if(check.getVisibility()==view.GONE){
check.setVisibility(View.VISIBLE);
return;
            }
            else{
                check.setVisibility(View.GONE);
            }

        }
    }
}
