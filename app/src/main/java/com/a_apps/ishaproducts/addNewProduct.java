package com.a_apps.ishaproducts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addNewProduct extends AppCompatActivity {

    Button submitProduct;
    EditText productField,priceField,wholesaleFild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);


        productField=findViewById(R.id.productWholesale);
        wholesaleFild=findViewById(R.id.productName);
        priceField=findViewById(R.id.productPrice);


        submitProduct=findViewById(R.id.submitNewProduct);

        submitProduct.setOnClickListener(new View.OnClickListener() {

            private String productHolder;
            private String priceHolder;
            private String wholesaleHolder;



            @Override
            public void onClick(View view) {


                final DatabaseReference databaseReference,rootRef;


                constructorProductDetails cProductDetails=new constructorProductDetails();
                GetDataFromEditText();


                cProductDetails.setProductName(productHolder);
                cProductDetails.setProductPrice(priceHolder);
                cProductDetails.setProductWholesale(wholesaleHolder);



                rootRef = FirebaseDatabase.getInstance().getReference().child("products");
                String UserId = rootRef.push().getKey();

                rootRef.child(UserId).setValue(cProductDetails);

                Toast.makeText(addNewProduct.this,"Data Saved", Toast.LENGTH_LONG).show();
                openhome();
            }


            private void GetDataFromEditText() {

                productField=findViewById(R.id.productName);

                priceField=findViewById(R.id.productPrice);

                wholesaleFild=findViewById(R.id.productWholesale);

                productHolder = productField.getText().toString().trim();
                priceHolder= priceField.getText().toString().trim();
                wholesaleHolder= wholesaleFild.getText().toString().trim();



            }
        });


    }
    private void openhome() {
        Intent intent=new Intent(this,home.class);
        startActivity(intent);
    }




}
