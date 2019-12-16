package com.example.shariful.bdcash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText email_signUp,pass_signUp,name,mobile;
    Button signUpBtn;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    DatabaseReference databaseReference;

    String uId;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("SignUp");


        databaseReference= FirebaseDatabase.getInstance().getReference("employee");

        email_signUp=findViewById(R.id.emailID_signUp);
        pass_signUp=findViewById(R.id.passID_signUp);
        signUpBtn=findViewById(R.id.singUpBtnID);

        name=findViewById(R.id.nameID);
        mobile=findViewById(R.id.mobileID);


        mAuth = FirebaseAuth.getInstance();





        progressBar=findViewById(R.id.progressbarID);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }


    private void register() {
        String email=email_signUp.getText().toString().trim();
        String password=pass_signUp.getText().toString().trim();

        if(email.isEmpty())
        {
            email_signUp.setError("Enter an email address");
            email_signUp.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            email_signUp.setError("Enter a valid email address");
            email_signUp.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            pass_signUp.setError("Enter a password");
            pass_signUp.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                           saveUserInfo();

                               finish();
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUpActivity.this, "SignUp Successful!!", Toast.LENGTH_SHORT).show();
                              Intent intent=new Intent(SignUpActivity.this,WorkActivity.class);
                               startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Toast.makeText(getApplicationContext(), "User is already registered !!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            //Toast.makeText(SignUpActivity.this, "Failed To SignUp !!", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                });
    }

    private void saveUserInfo(){
      String userName=name.getText().toString();
      String userMobile=mobile.getText().toString();

        user = mAuth.getCurrentUser();
        uId = user.getUid();

        databaseReference.child(uId).child("Name").setValue(userName);
        databaseReference.child(uId).child("Mobile").setValue(userMobile);

    }





}
