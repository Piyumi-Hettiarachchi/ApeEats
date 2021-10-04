package com.example.ApeEats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ApeEats.models.ProductModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;

public class AddProduct extends AppCompatActivity {

    ImageView rAddimage,rplus;
    EditText addFoodname, addPrice, addDescription;
    TextView addCategory, addDelivery;
    RadioGroup GDelivery;
    Spinner dspinner;
    Button addFood;

    String  foodname,price,description,yradio,addimgUri,Categ;

    Boolean setState = true; //this must be true if uploaded image is ok.

    Uri rimgUri;

    DatabaseReference db;

    //Drop down List Values
    String[] setListDistrict = new String[]{"Select Category","Rice ","Noodles","Beverage","Pizza","Burger","Kottu","Other"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

         rAddimage = findViewById(R.id.Addimage);
         rplus = findViewById(R.id.img_plus);
         addFoodname = findViewById(R.id.branchname);
        addDescription = findViewById(R.id.rfooddes);
        addCategory = findViewById(R.id.rcategory);
        addPrice = findViewById(R.id.rprice);
        dspinner = findViewById(R.id.rspinn);
        addFood = findViewById(R.id.raddFood);


// get data to the dropdown
        setDataSpinners();
//get selected values
        getDataSpinners();

        db = FirebaseDatabase.getInstance().getReference().child("Products");

        String test = String.valueOf(db);


        rplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseFile(rAddimage);
            }
        });

    }

    private void getDataSpinners() {

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dspinner.getSelectedItem().toString() != "Select a Cetegory")
                {
                    Categ = dspinner.getSelectedItem().toString();
                }
                foodname = addFoodname.getText().toString();
                price = addPrice.getText().toString();
                description = addDescription.getText().toString();

                GDelivery = findViewById(R.id.Dgroup);
                yradio = ((RadioButton)findViewById(GDelivery.getCheckedRadioButtonId())).getText().toString();


                addimgUri = String.valueOf(rimgUri);

                ProductModel promodel = new ProductModel (addimgUri,foodname,description,price,yradio,Categ);


                db.push().setValue(promodel);
                Toast.makeText(getApplicationContext(),"Data Inserted!",Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void setDataSpinners() {

        //create an adapter to describe how the items are displayed
        ArrayAdapter<String> adapterTemp = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,setListDistrict);

        //set the spinners adapter to the previously created one.
        dspinner.setAdapter(adapterTemp);

    }

    public void onChooseFile(View v){
        CropImage.activity().start(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode==RESULT_OK){
                rimgUri=result.getUri();
                if(setState){
                    rAddimage.setImageURI(rimgUri);
                }
                else{
                    rAddimage.setImageResource(R.drawable.cooking);
                }
            }
            else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(this,"Possible error is : "+e,Toast.LENGTH_SHORT).show();
            }
        }

    }
}