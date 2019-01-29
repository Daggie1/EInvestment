package  com.example.anonymous.e_investment.mpesa;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ButtonCallback;
import com.bdhobare.mpesa.Mode;
import com.bdhobare.mpesa.Mpesa;
import com.bdhobare.mpesa.interfaces.AuthListener;
import com.bdhobare.mpesa.interfaces.MpesaListener;
import com.bdhobare.mpesa.models.STKPush;
import com.bdhobare.mpesa.models.STKPush.Builder;
import com.bdhobare.mpesa.utils.Pair;
import com.example.anonymous.e_investment.R;
import com.example.anonymous.e_investment.models.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AuthListener, MpesaListener {
    //TODO: Replace these values from
    public static final String BUSINESS_SHORT_CODE = "174379";
    public static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    public static final String CONSUMER_KEY = "YTZZTAUnMdOccHYU6ipgdCBmRHMHPDqA";
    public static final String CONSUMER_SECRET = "NKTmGMpbMCuo1pQc";
    public static final String CALLBACK_URL = "YOUR_CALLBACK_URL";


    public static final String  NOTIFICATION = "PushNotification";
    public static final String SHARED_PREFERENCES = "com.bdhobare.mpesa_android_sdk";

    Button pay;
    ProgressDialog dialog;
    EditText phone;
    EditText amount;
String mygroup_id;
    int amount_to_transact;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mygroup_id=getIntent().getStringExtra("groupid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mpesa);
        pay = (Button)findViewById(R.id.pay);
        phone = (EditText)findViewById(R.id.phone);
        amount = (EditText)findViewById(R.id.amount);
        Mpesa.with(this, CONSUMER_KEY, CONSUMER_SECRET);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Processing");
        dialog.setIndeterminate(true);

        pay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String p = phone.getText().toString();
                amount_to_transact = Integer.valueOf(amount.getText().toString());
                if (p.isEmpty()){
                    phone.setError("Enter phone.");
                    return;
                }
                pay(p, amount_to_transact);
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(NOTIFICATION)) {
                    String title = intent.getStringExtra("title");
                    String message = intent.getStringExtra("message");
                    int code = intent.getIntExtra("code", 0);
                    showDialog(title, message, code);

                }
            }
        };
    }

    @Override
    public void onAuthError(Pair<Integer, String> result) {
        Log.e("Error", result.message);
    }

    @Override
    public void onAuthSuccess() {

        //TODO make payment
        pay.setEnabled(true);
    }
    private void pay(String phone, int amount){
        dialog.show();
        STKPush.Builder builder = new Builder(BUSINESS_SHORT_CODE, PASSKEY, amount,BUSINESS_SHORT_CODE, phone);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String token = sharedPreferences.getString("InstanceID", "");

        builder.setFirebaseRegID(token);
        STKPush push = builder.build();



        Mpesa.getInstance().pay(this, push);

    }
    private void showDialog(String title, String message,int code){
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(title)
                .titleGravity(GravityEnum.CENTER)
                .customView(R.layout.success_dialog, true)
                .positiveText("OK")
                .cancelable(false)
                .widgetColorRes(R.color.colorPrimary)
                .callback(new ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                        finish();
                    }
                })
                .build();
        View view=dialog.getCustomView();
        TextView messageText = (TextView)view.findViewById(R.id.message);
        ImageView imageView = (ImageView)view.findViewById(R.id.success);
        if (code != 0){
            imageView.setVisibility(View.GONE);
        }
        messageText.setText(message);
        dialog.show();
    }

    @Override
    public void onMpesaError(Pair<Integer, String> result) {
        dialog.hide();
        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage) {
        dialog.hide();
        Toast.makeText(this, CustomerMessage, Toast.LENGTH_SHORT).show();

        //add transaction
        addingToTransaction();
    }
    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(NOTIFICATION));

    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    public void addingToTransaction(){
        DatabaseReference query= FirebaseDatabase.getInstance().getReference("Transactions");
        String id=query.push().getKey();
        String transact_Id= UUID.randomUUID().toString();
        final String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        String date_transacted=String.valueOf(new Date().getDate());
        String amount=String.valueOf(amount_to_transact);
       Transaction group=new Transaction(transact_Id,userId,date_transacted,mygroup_id,amount);
       query.child(id).setValue(group);
       //updating contribution
        Query contributionref=FirebaseDatabase.getInstance().getReference("Contribution").orderByChild("group1d").equalTo(mygroup_id);
        contributionref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              if(dataSnapshot.exists()){
                  for(DataSnapshot contribsnap:dataSnapshot.getChildren()){
                      if(contribsnap.child("userId").getValue(String.class)==userId){
                       int current_balance=Integer.valueOf(contribsnap.child("amoount_transacted").getValue(String.class))+amount_to_transact;

                       contribsnap.getRef().child("amoount_transacted").setValue(String.valueOf(current_balance));
                      }
                  }
              }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //updating groups account totalAmount
        Query groupref=FirebaseDatabase.getInstance().getReference("Groups").orderByChild("groupId").equalTo(mygroup_id);
        groupref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              if(dataSnapshot.exists()){
                  for(DataSnapshot groupsnap:dataSnapshot.getChildren()){
                      int current_balance=Integer.valueOf(groupsnap.child("totalAmount").getValue(String.class))+amount_to_transact;
                      groupsnap.getRef().child("totalAmount").setValue(String.valueOf(current_balance));
                  }
              }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
