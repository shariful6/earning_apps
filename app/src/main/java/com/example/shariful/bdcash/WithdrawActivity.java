package com.example.shariful.bdcash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WithdrawActivity extends AppCompatActivity {

    EditText mobileET,ammountET;
    RadioGroup  rg;
    Button submitBtn;
    TextView balance_tv;

    String method;
    int balance;

    DatabaseReference databaseReference,databaseReference2;
    String uId;
    FirebaseAuth auth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        String value=getIntent().getStringExtra("value");

        // Toast.makeText(this, "Recive value is: "+value, Toast.LENGTH_SHORT).show();

         databaseReference= FirebaseDatabase.getInstance().getReference("Withdraw");
         databaseReference2= FirebaseDatabase.getInstance().getReference("employee");

         balance_tv=findViewById(R.id.balanceId);
         mobileET=findViewById(R.id.mobileID);
         ammountET=findViewById(R.id.amountID);
         rg=findViewById(R.id.radioGrpID);
         submitBtn=findViewById(R.id.submitBtnId);

          balance_tv.setText(value);

         balance=Integer.parseInt(value);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        uId = user.getUid();


         rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup radioGroup, int i) {
                 RadioButton rb=findViewById(i);
                 method=rb.getText().toString();
             }
         });

         submitBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 withdraw();
             }
         });






    }

    private void withdraw() {


        String mobile=mobileET.getText().toString().trim();
        String amount=ammountET.getText().toString().trim();
        int withdraw_balance=Integer.parseInt(amount);

        
        if(balance<10)
        {
            Toast.makeText(this, "Insufficiant Balance !!", Toast.LENGTH_SHORT).show();
        }
       
       else if(withdraw_balance>balance)
        {
            Toast.makeText(this, "Reduce Amount !", Toast.LENGTH_SHORT).show();

        }


        else {

            databaseReference.child(uId).child("Mobile").setValue(mobile);
            databaseReference.child(uId).child("Amount").setValue(amount);
            databaseReference.child(uId).child("Method").setValue(method);

            int rest_balance = balance - withdraw_balance;
            String final_balance = String.valueOf(rest_balance);

            databaseReference2.child(uId).child("Balance").setValue(final_balance);

            Toast.makeText(this, "Payment Request Success !!", Toast.LENGTH_LONG).show();

            mobileET.setText("");
            ammountET.setText("");

            Intent intent=new Intent(WithdrawActivity.this,WorkActivity.class);
            startActivity(intent);
        }

    }




}
