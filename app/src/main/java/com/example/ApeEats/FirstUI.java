package com.example.ApeEats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstUI extends AppCompatActivity {

    Button btnCustomer, btnRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_ui);

        btnCustomer = (Button) findViewById(R.id.btnc);
        btnRestaurant = (Button) findViewById(R.id.btnr);

//        btnCustomer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               Intent ic = new Intent(new View.OnClickListener() {
//                   @Override
//                   public void onClick(View v) {
//                       Intent ic = new Intent(getApplicationContext().class);
//                   }
//               });
//            }
//        });

        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir = new Intent(getApplicationContext(),RestaurantSignin.class);
                startActivity(ir);
            }
        });



    }
}