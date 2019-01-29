package com.example.anonymous.e_investment.fragments;

import android.app.ProgressDialog;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.anonymous.e_investment.R;
import com.example.anonymous.e_investment.models.Contribution;
import com.example.anonymous.e_investment.models.Group;
import com.example.anonymous.e_investment.mpesa.AddMembersActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class CreateNewGroupfragment extends Fragment {

    private static final int PICK_PHOTO_REQUEST_CODE =1212 ;
    Uri photopath,downloadurl;
    Button maddproductBtn,addOtherBtn;
    ImageView groupImage;
    ProgressDialog dialog;
    Boolean uploadStatus=false;
    AutoCompleteTextView  groupName;
    String mcurrentLat,mCurrentLong;
    StorageReference photoref;
    DatabaseReference dbreference;
    String group_id;
    public CreateNewGroupfragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoref= FirebaseStorage.getInstance().getReference();
        dbreference= FirebaseDatabase.getInstance().getReference("Groups");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_new_group, container, false);
        maddproductBtn=(Button) view.findViewById(R.id.addProductBtn);
        addOtherBtn=(Button) view.findViewById(R.id.addOtherBtn);
        addOtherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent addmembersintent=new Intent(new Intent(getActivity(), AddMembersActivity.class));
addmembersintent.putExtra("Groupid",group_id);
startActivity(addmembersintent);
            }
        });
        groupName =(AutoCompleteTextView) view.findViewById(R.id.addproductnametxt);

        groupImage =(ImageView) view.findViewById(R.id.addProductImageview);
        groupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });
        maddproductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Creating");
                dialog.setIndeterminate(true);
                uploadPhoto();
            }
        });
        return view;
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
                groupImage.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadPhoto(){
        if(photopath!=null) {
            StorageReference riversRef = photoref.child("images/"+ new Date().getTime() +".jpg");

            riversRef.putFile(photopath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.show();
                            // Get a URL to the uploaded content
                            downloadurl= taskSnapshot.getDownloadUrl();
                            //downloadurl = taskSnapshot.getUploadSessionUri();
                            Toast.makeText(getActivity(), "Creating...", Toast.LENGTH_LONG).show();
                            uploadStatus = true;
                            String gname= groupName.getText().toString().trim();
                            String groupid=UUID.randomUUID().toString();
                            String picurl=downloadurl.toString();

                            if(!TextUtils.isEmpty(gname)){
                                String id=dbreference.push().getKey();
                                String sellerId= FirebaseAuth.getInstance().getCurrentUser().getUid();

                                Group  group=new Group(groupid,gname,"0",picurl);
                                group_id=groupid;
                                dbreference.child(id).setValue(group);
                               DatabaseReference query=FirebaseDatabase.getInstance().getReference("Contribution");
                                String contributionid= UUID.randomUUID().toString();
                                String memberid= FirebaseAuth.getInstance().getCurrentUser().getUid();

                                Contribution contribution=new Contribution(contributionid,memberid,groupid,"0");
                                query.child(id).setValue(contribution);
                                /*TomatoesModel newproduct=new TomatoesModel(productname,productDesc,sellerId,id,customerlat,customerLong,marker,productPrice,picurl);
                                dbreference.child(id).setValue(newproduct);*/
                                dialog.dismiss();
                                Toast.makeText(getActivity(),"Created successfully!",Toast.LENGTH_LONG).show();
                                addOtherBtn.setVisibility(View.VISIBLE);


                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "image not uploading correctly.make sure you are connected to internet and try again", Toast.LENGTH_LONG).show();
                            uploadStatus = false;
                        }
                    });
        }else{
            uploadStatus = false;
            Toast.makeText(getActivity(),"EmptyFilepath please select a photo..",Toast.LENGTH_LONG).show();
        }

    }
    }

