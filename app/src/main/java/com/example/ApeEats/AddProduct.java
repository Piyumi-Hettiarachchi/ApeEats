package com.example.ApeEats;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ApeEats.models.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Objects;
import java.util.UUID;

public class AddProduct extends AppCompatActivity {

    ImageView rAddimage, rplus;
    EditText addFoodname, addPrice, addDescription;
    TextView addCategory, addDelivery;
    RadioGroup GDelivery;
    Spinner dspinner;
    Button addFood;

    RadioButton yesRadio;

    String foodname, price, description, yradio, addimgUri, Categ;
    String imgID;
    String randomKey;

    //this must be true if uploaded image is ok.
    Boolean setState = false;

    Uri rimgUri;
    Uri getRimgUri;

    DatabaseReference db;
    FirebaseAuth auth;

    //Firebase Storage.
    FirebaseStorage storage;
    StorageReference storageReference;

    //Drop down List Values
    String[] setListDistrict = new String[]{"Select Category", "Rice ", "Noodles", "Beverage", "Pizza", "Burger", "Kottu", "Other"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // user must sign in to the app first
        //otherwise he will navigate to restaurant sign in

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(),FirstUI.class));
            finish();
        }

        // identify every ids to these variables

        rAddimage = findViewById(R.id.Addimage);
        rplus = findViewById(R.id.img_plus);
        addFoodname = findViewById(R.id.addfname);
        addDescription = findViewById(R.id.rfooddes);
        addCategory = findViewById(R.id.rcategory);
        addPrice = findViewById(R.id.rprice);
        dspinner = findViewById(R.id.rspinn);
        addFood = findViewById(R.id.raddFood);

        yesRadio = findViewById(R.id.DeliveryY);


        // get data to the dropdown
        setDataSpinners();
        //get selected values
        getDataSpinners();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        rplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseFile(rAddimage);
            }
        });

    }

    // function happens when user click on ADD FOOD button
    private void getDataSpinners() {

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get data from the spinner
                if (!dspinner.getSelectedItem().toString().equals("Select a district")) {
                    Categ = dspinner.getSelectedItem().toString();
                }

                //get data from other text fields
                foodname = addFoodname.getText().toString();
                price = addPrice.getText().toString();
                description = addDescription.getText().toString();

                //identify radio group
                GDelivery = findViewById(R.id.Dgroup);
                //get the  relevant text of checked button from identified radio group
                yradio = ((RadioButton) findViewById(GDelivery.getCheckedRadioButtonId())).getText().toString();

                //get image
                addimgUri = String.valueOf(rimgUri);

                randomKey = UUID.randomUUID().toString();
                final String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();

                //load to the product model
                ProductModel promodel = new ProductModel(randomKey, addimgUri, foodname, description, price, Categ, yradio, userID);


                //db path
                db = FirebaseDatabase.getInstance().getReference().child("Products").child(randomKey);

                //insert data to the db
                db.setValue(promodel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Food Inserted!", Toast.LENGTH_SHORT).show();

                        // if food data is successfully inserted upload image function will call
                        uploadImage(randomKey, db);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Food Insertion Failed, Try Again!", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

    private void setDataSpinners() {

        //create an adapter to describe how the items are displayed
        ArrayAdapter<String> adapterTemp = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, setListDistrict);

        //set the spinners adapter to the previously created one.
        dspinner.setAdapter(adapterTemp);

    }

    public void onChooseFile(View v) {
        //when user click on add sign camera will open
        CropImage.activity().start(this);

    }

    //take the image by opening the camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                //once app capture the image it will assign to rimageUri
                rimgUri = result.getUri();
                if(rimgUri != null){
                    setState=true;
                }
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

    //Upload image Function
    public void uploadImage(String pID, DatabaseReference db) {

        // to show the place where image uploaded
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        //access the db storage
        StorageReference riversRef = storageReference.child("images/" + pID);

        imgID = randomKey.trim();
        Log.d("ImgID", imgID);

        riversRef.putFile(rimgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    //if  riversRef.putFile(rimgUri) success it will come inside this onSuccess
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //if this is success get the rivers ref download url
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // if download url success, get that url into this uri type variable
                                getRimgUri = uri;

                                String userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                                //converting the uri type image into string
                                String imageDown = String.valueOf(getRimgUri);

                                ProductModel promodel1 = new ProductModel(randomKey, imageDown, foodname, description, price, Categ, yradio, userID);

                                // load data inside the model to the db
                                db.setValue(promodel1).addOnSuccessListener(new OnSuccessListener<Void>() {

                                    // if successfully image inserted user wil navigate to Res home
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), ResHome.class);
                                        startActivity(i);
                                    }
                                });
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed To Upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + (int) progressPercent + "%");
                    }
                });
    }
}