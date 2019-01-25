package com.example.anonymous.e_investment.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anonymous.e_investment.R;
import com.example.anonymous.e_investment.models.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText email,password,phone,username;
    TextView backtologin;
    Button sign_Up;
    DatabaseReference dbreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        dbreference= FirebaseDatabase.getInstance().getReference("Members");
        mAuth = FirebaseAuth.getInstance();
        email=(EditText) findViewById(R.id.email_edit_text);
        username=(EditText) findViewById(R.id.username_edit_text);
        phone=(EditText) findViewById(R.id.phone_edit_text);
        password=(EditText) findViewById(R.id.password_edit_text);
        backtologin=(TextView) findViewById(R.id.back_to_signuptxt);
        sign_Up=(Button) findViewById(R.id.loginbtn);
        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,GroupListActivity.class));
            }
        });
        sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupnew_users();
            }
        });

    }

    public  void signupnew_users(){
        final String email=this.email.getText().toString();
        final String pwd=this.password.getText().toString();
        final String phone=this.phone.getText().toString();
        final String user_name=this.username.getText().toString();
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(phone)) {
            mAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String id=dbreference.push().getKey();
                                String memberId= FirebaseAuth.getInstance().getCurrentUser().getUid();

                                Member user=new Member(memberId,user_name,phone,email,pwd,"skip");
                                dbreference.child(id).setValue(user);
                                Toast.makeText(SignupActivity.this,"account created successfully",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignupActivity.this,GroupListActivity.class));
                            }
                        }
                    });
        }else {
            Toast.makeText(this,"blanks not allowed",Toast.LENGTH_LONG).show();}
    }
}