package com.example.shariful.bdcash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
         //thanks vai   kaj hoice
                //accha
                User user=new User(SplashScreen.this);
                if(!user.getEmail().isEmpty())
                {

                    Intent intent = new Intent(SplashScreen.this,WorkActivity.class);
                   // intent.putExtra("email",user.getEmail());
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(SplashScreen.this,SignInActivity.class);
                    startActivity(intent);
                    finish();

                }


                finish();
            }
        }, 2000);



    }
}
