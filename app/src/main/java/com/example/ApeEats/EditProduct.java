package com.example.ApeEats;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ApeEats.models.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Map;

import static java.util.Objects.requireNonNull;

public class EditProduct extends AppCompatActivity {

    String getPID;

    ImageView updateImg, rAddimage, rplus;
    EditText updateName;
    Spinner updateCategory;
    EditText updateDesc;
    EditText updatePrice;
    RadioGroup updateRadio;
    RadioButton radio1, radio2;

    String foodname, price, description, yradio, addimgUri, Categ, uID, pID,tempLoadImg;
    String tackID;

    //this must be true if uploaded image is ok.
    Boolean setState = false;

    Button btnUpdate;

    Uri rimgUri;

    DatabaseReference db,dbUpdate;
    FirebaseAuth auth;

    //Drop down List Values
    String[] setListCategory = new String[]{"Select a Category", "Rice ", "Noodles", "Beverage", "Pizza", "Burger", "Kottu", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);


        // user must sign in to the app first
        //otherwise he will navigate to restaurant sign in
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), FirstUI.class));
            finish();
        }

        //Identify variables by id
        rAddimage = findViewById(R.id.updateImg);
        rplus = findViewById(R.id.Edit_img_plus);

        updateName = findViewById(R.id.editfname);
        updateCategory = findViewById(R.id.editspinn);
        updateDesc = findViewById(R.id.editdesc);
        updatePrice = findViewById(R.id.editprice);
        updateRadio = findViewById(R.id.editrgroup);
        radio1 = findViewById(R.id.edityes);
        radio2 = findViewById(R.id.editno);

        btnUpdate = findViewById(R.id.btnsv);

        //retrieve intent extra from Product list data
        getPID = getIntent().getExtras().getString("pID");
        Log.d("recievedID", getPID);


        //db path
        db = FirebaseDatabase.getInstance().getReference().child("Products");

        //load the data from db(realtime data update)
        db.addValueEventListener(new ValueEventListener() {
            @Override

            //load the data to the snapshot
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    collectData((Map<String, Object>) requireNonNull(snapshot.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseFile(rAddimage);
            }
        });

        // get data to the dropdown
        setDataSpinners();
        //get selected values
        getDataSpinners();

    }

    //retrieve data
    private void collectData(Map<String, Object> users) {

        String[] arrTemp = new String[8];

        int tempCount = 0;

        //iterate snapshot data to the map
        for (Map.Entry<String, Object> entry : users.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();

            //getUID=> id which is brought by intent from product list data
            if (String.valueOf(singleUser.get("pID")).equals(getPID)) {
                arrTemp[0] = (String) singleUser.get("pImage");
                arrTemp[1] = (String) singleUser.get("pFoodname");
                arrTemp[2] = (String) singleUser.get("pDescription");
                arrTemp[3] = (String) singleUser.get("pPrice");
                arrTemp[4] = (String) singleUser.get("pCetogory");
                arrTemp[5] = (String) singleUser.get("pDeliveryAvailable");
                arrTemp[6] = (String) singleUser.get("pID");
                arrTemp[7] = (String) singleUser.get("uID");

                tackID = (String) singleUser.get("pID");
            }

            tempCount = tempCount + 1;

            String te = String.valueOf(singleUser);
            Log.d("Hell", te);
        }

        //load the pass to this function
        loadRecievedData(arrTemp);

    }

    private void getDataSpinners() {

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!updateCategory.getSelectedItem().toString().equals("Select a Category")) {
                    Categ = updateCategory.getSelectedItem().toString();
                }
                foodname = updateName.getText().toString();
                price = updatePrice.getText().toString();
                description = updateDesc.getText().toString();

                yradio = ((RadioButton) findViewById(updateRadio.getCheckedRadioButtonId())).getText().toString();


                addimgUri = tempLoadImg;

                final String randomKey = pID;
                final String userID = requireNonNull(auth.getCurrentUser()).getUid();

                //load the product model with updated data
                ProductModel promodel = new ProductModel(randomKey, addimgUri, foodname, description, price, Categ, yradio, userID);


                if(tackID.equals(getPID)){
                    dbUpdate = FirebaseDatabase.getInstance().getReference().child("Products").child(tackID);
                    dbUpdate.setValue(promodel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),ProductList.class);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Updation Failed, Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void setDataSpinners() {

        //create an adapter to describe how the items are displayed
        ArrayAdapter<String> adapterTemp = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, setListCategory);

        //set the spinners adapter to the previously created one.
        updateCategory.setAdapter(adapterTemp);

    }

    private void loadRecievedData(String[] arrTemp) {

        String url = arrTemp[0];
        Glide.with(this).load(url).into(rAddimage);

        tempLoadImg = arrTemp[0];
        updateName.setText(arrTemp[1]);
        updateDesc.setText(arrTemp[2]);
        updatePrice.setText(arrTemp[3]);
        for (int i = 0; i < setListCategory.length; i++) {

            if (setListCategory[i].equals(arrTemp[4])) {
                updateCategory.setSelection(i);
            }
        }

        if (arrTemp[5].equals("Yes")) {
            radio1.setChecked(true);
        } else if (arrTemp[5].equals("No")) {
            radio2.setChecked(true);
        }
        pID = arrTemp[6];
        uID = arrTemp[7];


    }

    public void onChooseFile(View v) {
        CropImage.activity().start(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                rimgUri = result.getUri();
                if (setState) {
                    rAddimage.setImageURI(rimgUri);
                } else {
                    rAddimage.setImageResource(R.drawable.cooking);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception e = result.getError();
                Toast.makeText(this, "Possible error is : " + e, Toast.LENGTH_SHORT).show();
            }
        }

    }
}