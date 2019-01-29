package com.example.anonymous.e_investment;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

public class AboutUs extends AppCompatActivity {
private CircularImageView email,whatsApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        email=(CircularImageView) findViewById(R.id.email);
        whatsApp=(CircularImageView) findViewById(R.id.whatsapp);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailintent =new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","e_investiment@info.com",null));
                emailintent.putExtra(Intent.EXTRA_SUBJECT,"E-Investiment Customer Feedback " );
                emailintent.putExtra(Intent.EXTRA_TEXT, FirebaseAuth.getInstance().getCurrentUser().getEmail()+" :");
                startActivity(Intent.createChooser(emailintent,"Choose am a mail sender"));
            }
        });
        whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="https://api.whatsapp.com/send?phone="+"+254796474713";
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    }

