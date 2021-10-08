package com.example.ApeEats;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;
import java.util.Objects;

public class ProductList extends AppCompatActivity {

    LinearLayout l1;
    TextView retrieveTV;

    DatabaseReference db;
    FirebaseAuth auth;

    //Firebase Storage.
    FirebaseStorage storage;
    StorageReference storageReference;

    Bitmap bitmap;

    String trackID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), FirstUI.class));
            finish();
        }



        // layout which is keep all the food data(Dynamic view)
        // get the id of that  layout

        l1 = findViewById(R.id.DynamicLayout);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Read data from database
        db = FirebaseDatabase.getInstance().getReference().child("Products");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                refreshListLayout();
                if (snapshot.getValue() != null) {
                    collectData((Map<String, Object>) Objects.requireNonNull(snapshot.getValue()));
                } else {
                    LinearLayout layout1 = new LinearLayout(getApplicationContext());

                    layout1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 14));
                    layout1.setOrientation(LinearLayout.VERTICAL);
                    layout1.setGravity(Gravity.CENTER);

                    LinearLayout.MarginLayoutParams params = (LinearLayout.MarginLayoutParams) layout1.getLayoutParams();
                    params.topMargin = 10;

                    TextView textError = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    lpt.gravity = Gravity.CENTER_VERTICAL;
                    textError.setLayoutParams(lpt);
                    textError.setText("No Products Added Under Your Restaurant");
                    layout1.addView(textError);
                    l1.addView(layout1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void refreshListLayout() {
        l1.removeAllViews();
    }

    // function create to generate dynamic layouts (rows)
    @SuppressLint("UseCompatLoadingForDrawables")
    private void generateLayout(String[][] arry) {


        for (int i = 0; i < arry.length; i++) {

            if(arry[i][0] != null){

                // setup image properties
                ImageView iv = new ImageView(this);
                iv.setImageResource(R.drawable.image);
                int width = 200;
                int height = 200;
                LinearLayout.LayoutParams pSize = new LinearLayout.LayoutParams(width, height);
                pSize.gravity = Gravity.START;
                pSize.gravity = Gravity.CENTER_VERTICAL;
                iv.setLayoutParams(pSize);

                String url = arry[i][0];
                Glide.with(this).load(url).centerCrop().into(iv);

                LinearLayout layout0 = new LinearLayout(this);

                layout0.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,2));
                layout0.setOrientation(LinearLayout.HORIZONTAL);

                //layout for display particular food item (gray clr row)
                LinearLayout layout1 = new LinearLayout(this);

                layout1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,14));
                layout1.setOrientation(LinearLayout.HORIZONTAL);

                // give margin top value
                LinearLayout.MarginLayoutParams params = (LinearLayout.MarginLayoutParams) layout1.getLayoutParams();
                params.topMargin = 10;


                layout1.setBackgroundColor(getResources().getColor(R.color.green1));

                //children of layout1
                // layout showing food name and description
                LinearLayout layout2 = new LinearLayout(this);
                layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,11));
                layout2.setOrientation(LinearLayout.VERTICAL);

                // Layout which is carries buttons
                LinearLayout layout3 = new LinearLayout(this);
                layout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                layout3.setOrientation(LinearLayout.VERTICAL);

                // order of display the contents in layouts
                l1.addView(layout1);
                layout0.addView(iv);
                layout1.addView(layout0);
                layout1.addView(layout2);
                layout1.addView(layout3);

                //children of layout2 LinearLayout
                //create text views to display food name and description
                TextView tv1 = new TextView(this);
                TextView tv2 = new TextView(this);

                LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lpt.gravity = Gravity.CENTER_VERTICAL;
                lpt.topMargin = 15;
                tv1.setLayoutParams(lpt);
                tv2.setLayoutParams(lpt);

                tv1.setText(arry[i][1]);
                tv2.setText(arry[i][2]);

                tv1.setTextSize(20);
                tv2.setTextSize(16);

                //add text views to the previously created layout (layout2)
                layout2.addView(tv1);
                layout2.addView(tv2);

                //children of layout3 LinearLayout
                // create buttons
                Button btn1 = new Button(this);
                Button btn2 = new Button(this);

                // set button properties
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;
                lp.topMargin = 10;
                lp.bottomMargin = 10;
                btn1.setLayoutParams(lp);
                btn2.setLayoutParams(lp);


                // add drawables to the buttons
                btn1.setBackground(getResources().getDrawable(R.drawable.relative_border_outline));
                btn2.setBackground(getResources().getDrawable(R.drawable.relativeborder));

                btn1.setText("Edit");
                btn2.setText("Delete");

                // take the product id for move to the edit product
                int finalI = i;
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pDetails = String.valueOf(arry[finalI][6]);
                        Log.d("ProductID", pDetails);

                        //using intent extras
                        Intent i = new Intent(getApplicationContext(), EditProduct.class);
                        i.putExtra("pID", pDetails);
                        startActivity(i);
                    }
                });

                //delete product button
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String temID = String.valueOf(arry[finalI][6]);
                        removeProduct(temID);
                    }

                });

                // add buttons to the particular layout
                layout3.addView(btn1);
                layout3.addView(btn2);
            }



        }
    }

    private void collectData(Map<String, Object> users) {

        //iterate through each user, ignoring their UID
        // measure count to define a array size (reserve a place)
        int cnt = 0;
        boolean forActivate = false;

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            cnt = cnt + 1;
        }
        String[][] arry2 = new String[cnt][8];


        //initialize each product details to array
        int tempCount = 0;
        for (Map.Entry<String, Object> entry : users.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();

            if (Objects.equals(singleUser.get("uID"), Objects.requireNonNull(auth.getCurrentUser()).getUid())) {
                arry2[tempCount][0] = (String) singleUser.get("pImage");
                arry2[tempCount][1] = (String) singleUser.get("pFoodname");
                arry2[tempCount][2] = (String) singleUser.get("pDescription");
                arry2[tempCount][3] = (String) singleUser.get("pPrice");
                arry2[tempCount][4] = (String) singleUser.get("pCetogory");
                arry2[tempCount][5] = (String) singleUser.get("pDeliveryAvailable");
                arry2[tempCount][6] = (String) singleUser.get("pID");
                arry2[tempCount][7] = (String) singleUser.get("uID");

                trackID = (String) singleUser.get("pID");
                forActivate = true;
            } else {
                Toast.makeText(getApplicationContext(), "User Not Matched", Toast.LENGTH_SHORT).show();
            }

            tempCount = tempCount + 1;

            String te = String.valueOf(singleUser);
            Log.d("Hell", te);
        }
//        Arrays.sort(arry2, (a, b) -> a[1].compareTo(b[1]));
//        Arrays.sort(arry2, (a, b) -> a[0].compareTo(b[0]));
        if (forActivate) {
            generateLayout(arry2);
        } else {
            Toast.makeText(getApplicationContext(), "No Products Inserted", Toast.LENGTH_SHORT).show();
        }

    }

    private void removeProduct(String temID) {

        new AlertDialog.Builder(this)

                //confirmation box
                .setMessage("Are you sure you want to delete this product?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.child(temID).removeValue();
                        Toast.makeText(getApplicationContext(), "Product Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }



}