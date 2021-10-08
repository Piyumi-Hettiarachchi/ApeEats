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

import com.example.ApeEats.models.RestaurantModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Objects;
import java.util.UUID;

public class createAccount extends AppCompatActivity {


    ImageView imgGet, uploadImg;
    EditText rName, rMobile, rAbout, rEmail, rAddress;
    RadioGroup dGroup, bGroup;

    Spinner rDistrict;
    Button saveButton, cancelButton;

    String sName, sMobile, sAbout, sEmail, sAddress, sDistrict, bRadio, dRadio, sImgUri;

    //this must be true if uploaded image is ok.
    Boolean setState = true;

    Uri mimgUri;

    DatabaseReference db;
    FirebaseAuth auth;

    //drop down list values
    String[] setListDistrict = new String[]{"Select a district", "Kalutara", "Colombo", "Gampaha"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //if the user already create an account he will navigate to sign in ui
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), FirstUI.class));
            finish();
        }

        // Identify every ids to these variables
        imgGet = findViewById(R.id.img_profile);
        uploadImg = findViewById(R.id.img_plus);
        rDistrict = findViewById(R.id.rspinn);

        rName = findViewById(R.id.restaurantname);
        rMobile = findViewById(R.id.resphone);
        rAbout = findViewById(R.id.editTextAbout);
        rEmail = findViewById(R.id.resmail);
        rAddress = findViewById(R.id.editTextTextPostalAddress);

        saveButton = findViewById(R.id.btnSave);
        cancelButton = findViewById(R.id.btnCancel);

        // get data to the dropdown
        setDataSpinners();
        //get selected values
        getDataSpinners();

        String test = String.valueOf(db);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("helloTest", "Button Ok");
            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseFile(imgGet);
            }
        });

    }

    private void getDataSpinners() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get data from spinner
                if (rDistrict.getSelectedItem().toString() != "Select a district") {
                    sDistrict = rDistrict.getSelectedItem().toString();
                }

                //get data from other text fields
                sName = rName.getText().toString();
                sMobile = rMobile.getText().toString();
                sAbout = rAbout.getText().toString();
                sEmail = rEmail.getText().toString();
                sAddress = rAddress.getText().toString();

                //Identify radio group
                //get the  relevant text of checked button from identified radio group
                bGroup = findViewById(R.id.branchGroup);
                bRadio = ((RadioButton) findViewById(bGroup.getCheckedRadioButtonId())).getText().toString();

                //Identify radio group
                //get the  relevant text of checked button from identified radio group
                dGroup = findViewById(R.id.deliveryGroup);
                dRadio = ((RadioButton) findViewById(dGroup.getCheckedRadioButtonId())).getText().toString();

                sImgUri = String.valueOf(mimgUri);

                final String resID = UUID.randomUUID().toString();
                final String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();

                //load the restaurant model
                RestaurantModel resModel = new RestaurantModel(resID, sImgUri, sName, sMobile, sAbout, sEmail, sAddress, sDistrict, bRadio, dRadio, userID);

                // set db path
                // push data into database
                db = FirebaseDatabase.getInstance().getReference().child("Restaurants").child(resID);
                db.setValue(resModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Restaurant Inserted!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),ResHome.class);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Restaurant Insertion Failed, Try Again!", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

    private void setDataSpinners() {

        //create an adapter to describe how the items are displayed
        ArrayAdapter<String> adapterTemp = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, setListDistrict);

        //set the spinners adapter to the previously created one.
        rDistrict.setAdapter(adapterTemp);

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
                mimgUri = result.getUri();
                if (setState) {
                    imgGet.setImageURI(mimgUri);
                } else {
                    imgGet.setImageResource(R.drawable.cooking);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception e = result.getError();
                Toast.makeText(this, "Possible error is : " + e, Toast.LENGTH_SHORT).show();
            }
        }

    }
}