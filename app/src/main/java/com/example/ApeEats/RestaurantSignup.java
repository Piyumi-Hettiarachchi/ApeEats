package com.example.ApeEats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RestaurantSignup extends AppCompatActivity {

    EditText resname, resemail, respassword, resphone;
    Button ressignup;
    FirebaseAuth fbauthentication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_signup);

        resname = findViewById(R.id.resname);
        resemail= findViewById(R.id.resmail);
        respassword=findViewById(R.id.repw);
        resphone= findViewById(R.id.resphone);
        ressignup = findViewById(R.id.ressignup);

        fbauthentication = FirebaseAuth.getInstance();


        //check if the user is already login to the app
        if(fbauthentication.getCurrentUser()!= null){

            startActivity(new Intent(getApplicationContext(),createAccount.class));
            finish();
        }


        ressignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = respassword.getText().toString().trim();
                String email = resemail.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    resemail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    respassword.setError("Password is required");
                    return;
                }
                if (password.length()<6){
                    respassword.setError("Password must be more than 6 characters");
                }


                // Register the user in Firebase

                fbauthentication.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                      // Process of registering user is called task
                      if(task.isSuccessful()){
                        Toast.makeText(RestaurantSignup.this, "User created", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),createAccount.class));
                      }else{
                          Toast.makeText(RestaurantSignup.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                      }

                    }
                });



            }
        });

    }
}