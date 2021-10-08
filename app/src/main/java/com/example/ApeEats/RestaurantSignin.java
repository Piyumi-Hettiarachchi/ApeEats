package com.example.ApeEats;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RestaurantSignin extends AppCompatActivity {

    EditText resemail, respassword;
    Button login, register;

    FirebaseAuth Auth;

    String email,password,name,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_signin);


        //identify every ids to the variables
        resemail = findViewById(R.id.resmail);
        respassword = findViewById(R.id.repw);
        login = findViewById(R.id.reslog);
        register = findViewById(R.id.register);

        Auth = FirebaseAuth.getInstance();

        //check if the user is already login to the app
        if (Auth.getCurrentUser() != null) {

            startActivity(new Intent(getApplicationContext(), ResHome.class));
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                password = respassword.getText().toString().trim();
                email = resemail.getText().toString().trim();

                //email is required
                if (TextUtils.isEmpty(email)) {
                    resemail.setError("Email is required");
                    return;
                }

                //password is must
                if (TextUtils.isEmpty(password)) {
                    respassword.setError("Password is required");
                    return;
                }

                //password length should be greater than 6 characters
                if (password.length() < 6) {
                    respassword.setError("Password must be more than 6 characters");
                }


                //authenticate the user
                Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(RestaurantSignin.this, "loged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ResHome.class));
                        } else {
                            Toast.makeText(RestaurantSignin.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RestaurantSignup.class);
                startActivity(i);
                Log.d("test123","working");

            }
        });


    }
}