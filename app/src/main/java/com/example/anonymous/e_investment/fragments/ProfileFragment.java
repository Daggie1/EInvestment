package com.example.anonymous.e_investment.fragments;


import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.anonymous.e_investment.R;
import com.example.anonymous.e_investment.models.Contribution;
import com.example.anonymous.e_investment.models.Group;
import com.example.anonymous.e_investment.models.Member;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    private static final int PICK_PHOTO_REQUEST_CODE =89 ;
    private TextView profile_name,profile_email,profile_phone;
private EditText edit_profile_name,edit_profile_email,edit_profile_phone;
private Button edit_profile_btn;
private LinearLayout edit_profile_lLayout,profile_lLayout;
private CircularImageView edit_profile_image,proffile_image;
private  String member_id;
private Uri photopath;
private StorageReference storageRef;
private Uri downloadurl;
private  Query query;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        member_id=getActivity().getIntent().getStringExtra("member_id");
        storageRef= FirebaseStorage.getInstance().getReference("profileImages");
         query= FirebaseDatabase.getInstance().getReference("Members").orderByChild("member_id").equalTo(member_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_profile, container, false);
      profile_email=(TextView) view.findViewById(R.id.profile_email);
      profile_name=(TextView) view.findViewById(R.id.profile_name);
      profile_phone=(TextView) view.findViewById(R.id.profile_phone);
      edit_profile_email=(EditText) view.findViewById(R.id.edit_profile_email);
      edit_profile_name=(EditText) view.findViewById(R.id.edit_profile_name);
      edit_profile_phone=(EditText) view.findViewById(R.id.edit_profile_phone);
      edit_profile_btn=(Button) view.findViewById(R.id.profile_update_btn);
      edit_profile_lLayout=(LinearLayout) view.findViewById(R.id.edit_profile_linearlayout);
      profile_lLayout=(LinearLayout) view.findViewById(R.id.profile_linearlayout);
      edit_profile_image=(CircularImageView) view.findViewById(R.id.edit_profile_image);
      edit_profile_image=(CircularImageView) view.findViewById(R.id.edit_profile_image);
        edit_profile_lLayout.setVisibility(View.GONE);
        profile_lLayout.setVisibility(View.VISIBLE);
        String user=FirebaseAuth.getInstance().getCurrentUser().getUid();
    if(member_id.equalsIgnoreCase(user)){
        edit_profile_lLayout.setVisibility(View.VISIBLE);
        profile_lLayout.setVisibility(View.GONE);
    }

      loadingdataToViews();
      edit_profile_image.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              choosePhoto();
          }
      });
      edit_profile_btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              updateProfile();
          }
      });
      return view;
    }
private void loadingdataToViews(){

    query.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          if(dataSnapshot.exists()){
              for(DataSnapshot memmbersnapshot:dataSnapshot.getChildren()){
                  Member member=memmbersnapshot.getValue(Member.class);
                  edit_profile_email.setText(member.getEmail());
                  edit_profile_name.setText(member.getUsername());
                  edit_profile_phone.setText(member.getPhone());
                  profile_email.setText(member.getEmail());
                  profile_name.setText(member.getUsername());
                  profile_phone.setText(member.getPhone());
                  Glide.with(getActivity()).load(member.getPicurl()).into(edit_profile_image);
                  Glide.with(getActivity()).load(member.getPicurl()).into(edit_profile_image);
              }
          }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}


    public void choosePhoto() {
        Intent photointent=new Intent();
        photointent.setType("image/*");
        photointent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(photointent,"Choose a product Photo"),PICK_PHOTO_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PICK_PHOTO_REQUEST_CODE && resultCode==RESULT_OK && data!=null&& data.getData()!=null){
            photopath  =data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),photopath);
                edit_profile_image.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateProfile(){
        if(photopath!=null) {

            StorageReference riversRef = storageRef.child("images/" + new Date().getTime() + ".jpg");
            riversRef.putFile(photopath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         downloadurl=taskSnapshot.getDownloadUrl();
                         query.addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 if(dataSnapshot.exists()){
                                     String username=edit_profile_name.getText().toString();
                                     String phone=edit_profile_phone.getText().toString();
                                     String email=edit_profile_email.getText().toString();
                                     String url=downloadurl.toString();
                                     for(DataSnapshot membersnap:dataSnapshot.getChildren()){
                                         membersnap.getRef().child("username").setValue(username);
                                         membersnap.getRef().child("phone").setValue(phone);
                                         membersnap.getRef().child("email").setValue(email);
                                         membersnap.getRef().child("picurl").setValue(url);
                                         Toast.makeText(getActivity(),"Updated successfully!",Toast.LENGTH_LONG).show();
                                     }
                                 }
                             }

                             @Override
                             public void onCancelled(DatabaseError databaseError) {

                             }
                         });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }
        else{
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String username=edit_profile_name.getText().toString();
                        String phone=edit_profile_phone.getText().toString();
                        String email=edit_profile_email.getText().toString();

                        for(DataSnapshot membersnap:dataSnapshot.getChildren()){
                            membersnap.getRef().child("username").setValue(username);
                            membersnap.getRef().child("phone").setValue(phone);
                            membersnap.getRef().child("email").setValue(email);

                            Toast.makeText(getActivity(),"Updated successfully!",Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
