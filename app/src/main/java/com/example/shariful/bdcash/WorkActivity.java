package com.example.shariful.bdcash;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class WorkActivity extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 6000;
    private static final long START_TIME_IN_MILLIS2 = 50000;

    TextView Balance_tv,showTimeTV;

    private CountDownTimer mCountDownTimer,mCountDownTimer2;
    private boolean mTimerRunning;
    private boolean mTimerRunning2;

    private boolean mTimerRunning3;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mTimeLeftInMillis2 = START_TIME_IN_MILLIS2;

    //for break time
    private static final long START_TIME_IN_MILLISB = 600000;
    private CountDownTimer mCountDownTimerB;
    private boolean mTimerRunningB;
    private long mTimeLeftInMillisB;
    private long mEndTime;



    DatabaseReference databaseReference;

    String key;
    String value;
    String value2;
    String value3;


    private TextView numberTV,invalidTV;
    private AdView madView;
    private Button ads,withdrawBtn,rulesBtn;
    private InterstitialAd mInterstitialAd;

    int count=0;

    int balance=0;

    int invalid_click=0;
    String invalid_holder;

    int taka;


    String uId;
    FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);



        databaseReference= FirebaseDatabase.getInstance().getReference("employee");

        invalidTV=findViewById(R.id.invalidID);
        showTimeTV=findViewById(R.id.showTimeID);
        Balance_tv=findViewById(R.id.balance_tvID);
        withdrawBtn=findViewById(R.id.withdrawBtnID);
        rulesBtn=findViewById(R.id.tutorialBtnID);
        numberTV=findViewById(R.id.numID);
        madView=findViewById(R.id.adView);
        ads=findViewById(R.id.bnID);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        uId = user.getUid();

        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713"); //  App id//initialize mobile ad

        mInterstitialAd= new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");//interstitial

        AdRequest adRequest = new AdRequest.Builder().build();//ad request

        madView.loadAd(adRequest);//load banner ads

        mInterstitialAd.loadAd(adRequest);


           retriveData();
           retriveData2();
           retriveView();

        mInterstitialAd.setAdListener(new AdListener() {  //interestitial ad listener
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

                mTimeLeftInMillis = START_TIME_IN_MILLIS;//for reseting timmer
                mTimeLeftInMillis2 = START_TIME_IN_MILLIS2;//for reseting timmer


            }
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.

            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                invalidClick();

                startTimerClick();
                updateCountDownTextClick();

            }
            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

        });


        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cashout code here
                if(balance>=10)
                {
                    Intent intent =new Intent(WorkActivity.this,WithdrawActivity.class);
                    intent.putExtra("value",value);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(WorkActivity.this, "Not enough balance !!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        rulesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(WorkActivity.this,RulesActivity.class);
                startActivity(intent);
                //rules activity code here
            }
        });


        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                    count++;
                   // numberTV.setText(""+String.valueOf(count));
                    saveView();

                    startTimer();
                    updateCountDownText();
                        /*
                    if(count==20)
                    {
                        count=0;
                        Toast.makeText(WorkActivity.this, "Click on the Ad !!", Toast.LENGTH_LONG).show();
                    }
                    */

                }
                else{
                    Toast.makeText(WorkActivity.this, "Please wait !!", Toast.LENGTH_SHORT).show();
                }

            }

        });

        madView.setAdListener(new AdListener(){  //Banner ad listener

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {


                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.

            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });




    }



    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                startTimerB();

            }
        }.start();

        // mTimerRunning = true;
    }

    private void updateCountDownText() {
        // int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d",seconds);
        Toast.makeText(this, "Please wait: "+timeLeftFormatted, Toast.LENGTH_SHORT).show();
        //tv.setText(timeLeftFormatted);
        if(seconds==00&count==20)
        {
            Toast.makeText(WorkActivity.this, "Click on the Ad !!", Toast.LENGTH_LONG).show();
        }
    }


    private void startTimerClick() {  //for click event
        mCountDownTimer2 = new CountDownTimer(mTimeLeftInMillis2, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis2 = millisUntilFinished;
                updateCountDownTextClick();
            }

            @Override
            public void onFinish() {
                mTimerRunning2 = false;

            }
        }.start();

        // mTimerRunning = true;
    }

    private void updateCountDownTextClick() {
        // int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis2 / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d",seconds);
        Toast.makeText(this, "Please wait: "+timeLeftFormatted, Toast.LENGTH_SHORT).show();
        //tv.setText(timeLeftFormatted);
        int c=Integer.parseInt(numberTV.getText().toString());
        if(seconds==00&&c==20)
        {
            balance=balance+2;
            Balance_tv.setText(""+String.valueOf(balance));
            saveData();
          // retriveData();
        }

    }

    public void saveData()
    {

        String bal=Balance_tv.getText().toString();
        key=databaseReference.getKey();
       // Employee employee=new Employee(bal);

        databaseReference.child(uId).child("Balance").setValue(bal);

        Toast.makeText(this, "Save successfull", Toast.LENGTH_SHORT).show();

    }

    public void retriveData()
    {

        databaseReference.child(uId).child("Balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if (dataSnapshot.exists()){
                   value = dataSnapshot.getValue(String.class);
                   Balance_tv.setText(value);
                   taka=Integer.parseInt(value);

                   balance=taka;
                   // Toast.makeText(WorkActivity.this, "Balance: "+value, Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(WorkActivity.this, "00", Toast.LENGTH_SHORT).show();
                    Balance_tv.setText("00");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(WorkActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void invalidClick() {
        if(count<20)
        {
            invalid_click++;
            invalidTV.setText(""+String.valueOf(invalid_click));
            String inval=invalidTV.getText().toString();
            key=databaseReference.getKey();
            // Employee employee=new Employee(bal);
            databaseReference.child(uId).child("Invalid_Click").setValue(inval);

        }
    }

    public void retriveData2()
    {

        databaseReference.child(uId).child("Invalid_Click").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if (dataSnapshot.exists()){
                    value2 = dataSnapshot.getValue(String.class);
                    invalidTV.setText(value2);

                    int in=Integer.parseInt(value2);
                    invalid_click=in;
                    // Toast.makeText(WorkActivity.this, "Balance: "+value, Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(WorkActivity.this, "00", Toast.LENGTH_SHORT).show();
                    invalidTV.setText("0");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(WorkActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveView()
    {
        numberTV.setText(""+String.valueOf(count));
        if(count>20)
        {
            count=0;
            numberTV.setText(""+String.valueOf(count));
        }
        String view=numberTV.getText().toString();
        key=databaseReference.getKey();
        databaseReference.child(uId).child("View").setValue(view);

    }
    public void retriveView()
    {

        databaseReference.child(uId).child("View").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    value3 = dataSnapshot.getValue(String.class);
                    numberTV.setText(value3);

                    int v=Integer.parseInt(value3);
                    count=v;
                    // Toast.makeText(WorkActivity.this, "Balance: "+value, Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(WorkActivity.this, "00", Toast.LENGTH_SHORT).show();
                    numberTV.setText("0");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(WorkActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //////////////////////////////////////////////////////////////////////////////////////////////
                            //For working break time  ////////////////////////


    private void startTimerB() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillisB;

        mCountDownTimerB = new CountDownTimer(mTimeLeftInMillisB, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillisB = millisUntilFinished;
                updateCountDownTextB();
            }

            @Override
            public void onFinish() {
                mTimerRunningB = false;
                mTimeLeftInMillisB=START_TIME_IN_MILLISB;
                updateButtonsB();
            }
        }.start();

        mTimerRunningB = true;
        updateButtonsB();
    }

    private void updateCountDownTextB() {
        int minutes = (int) (mTimeLeftInMillisB / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillisB / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        showTimeTV.setText(timeLeftFormatted);
    }

    private void updateButtonsB() {
        if (mTimerRunningB) {
            ads.setEnabled(false);
            ads.setText("Wait");
        }
        else
            {
            ads.setEnabled(true);
           }
    }


    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillisB);
        editor.putBoolean("timerRunning", mTimerRunningB);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimerB != null) {
            mCountDownTimerB.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mTimeLeftInMillisB = prefs.getLong("millisLeft", START_TIME_IN_MILLISB);
        mTimerRunningB = prefs.getBoolean("timerRunning", false);

        updateCountDownTextB();
       // updateButtons();

        if (mTimerRunningB) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillisB = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillisB < 0) {
                mTimeLeftInMillisB = 0;
                mTimerRunningB = false;
                updateCountDownTextB();
               // updateButtons();
            } else {
                startTimerB();
            }
        }
    }




}

