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

public class RestaurantSignin extends AppCompatActivity {

    EditText resemail, respassword;
    Button login , register;
    FirebaseAuth fbauthentication;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_signin);


        resemail = findViewById(R.id.resmail);
        respassword= findViewById(R.id.repw);
        login = findViewById(R.id.reslog);
        register= findViewById(R.id.reregister);
        fbauthentication = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
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


                //authenticate the user
fbauthentication.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {

        if(task.isSuccessful()){

            Toast.makeText(RestaurantSignin.this, "loged in successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),ResHome.class));
        }else{
            Toast.makeText(RestaurantSignin.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
});

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RestaurantSignup.class));

            }
        });


            }
        });




    }
}