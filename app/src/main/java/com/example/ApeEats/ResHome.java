package com.example.ApeEats;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class ResHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    MenuItem addProduct;
    RelativeLayout addBranch;
    TextView btnProductList;

    FirebaseAuth auth;
    ActionBarDrawerToggle mToogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_home);

        // Check current User already signIn or not
        // if he is not login redirect to the signIn ui
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), FirstUI.class));
            finish();
        }


        // ResHome drawer properties (drawer is inside the navigation view)

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        addBranch = findViewById(R.id.btnAddBranch);
        btnProductList = findViewById(R.id.btnProductList);


        //To hide or show items
        //Menu menu = navigationView.getMenu();
        //menu.findItem(R.id.nav_addProduct).setVisible(false);

//        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        //mtoogle - to hide drawer by clicking anywhere
        mToogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        //add branch from Res home.
        // when restaurant owner click he will navigate to Create account Ui
        addBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), createAccount.class);
                startActivity(i);
            }
        });

        //View all products from Res home.
        // when restaurant owner click on this he will navigate to product list Ui
        btnProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ProductList.class);
                startActivity(i);
            }
        });

//        String checkUser = Objects.requireNonNull(auth.getCurrentUser()).getUid();
//
//        Log.d("Hello",checkUser);
    }


    // auto created(Override) by  navigationView.setNavigationItemSelectedListener(this);
    //identify drawer items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_addProduct) {

            //when user click on Add product option in drawer he will navigate to Add product UI
            Intent intent = new Intent(getApplicationContext(), AddProduct.class);
            startActivity(intent);
        }

        //when user click on log out option in he will navigate to sign in ui
        if (item.getItemId() == R.id.nav_logOut) {
            try {
                auth.signOut();
                Intent i = new Intent(getApplicationContext(),RestaurantSignin.class);
                startActivity(i);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // order dashboard ui from Anushka
        //when use r click on Order Management option in he will navigate to order Dashboard ui
//        if (item.getItemId() == R.id.nav_manageOrder) {
//            try {
//                auth.signOut();
//                Intent i = new Intent(getApplicationContext(),Orderdashboard.class);
//                startActivity(i);
//            } catch (Exception e) {
//                e.printStackTrace();
//        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}