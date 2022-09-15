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

public class addNewShop extends AppCompatActivity {
    Button submitShop,addProduct;
    EditText shopNameField,shopPlaceField,shopPhoneField;


    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, home.class);
        startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_shop);

        shopNameField=findViewById(R.id.shopName);
        shopPlaceField=findViewById(R.id.shopPlace);



        submitShop=findViewById(R.id.submitShop);
        addProduct=findViewById(R.id.addProduct);


        submitShop.setOnClickListener(new View.OnClickListener() {
            private String PlaceHolder;
            private String NameHolder;
            private String PendingHolder;
            private String PhoneHolder;

            @Override
            public void onClick(View view) {


                final DatabaseReference databaseReference, rootRef;

                constructorShopDetails shop_details = new constructorShopDetails();
                GetDataFromEditText();


                shop_details.setShopName(NameHolder);
                shop_details.setPhoneNumber(PhoneHolder);
                shop_details.setShopPlace(PlaceHolder);
                shop_details.setShopPending(PendingHolder);

                rootRef = FirebaseDatabase.getInstance().getReference().child("shop");
                String UserId = rootRef.push().getKey();

                rootRef.child(UserId).setValue(shop_details);

                Toast.makeText(addNewShop.this, "Data Inserted Successfully into Firebase Database", Toast.LENGTH_LONG).show();
                openhome();
            }


            private void GetDataFromEditText() {

                shopNameField = findViewById(R.id.shopName);
                shopPlaceField = findViewById(R.id.shopPlace);
                shopPhoneField = findViewById(R.id.shopPhone);

                NameHolder = shopNameField.getText().toString().trim();

                PlaceHolder = shopPlaceField.getText().toString().trim();

                PhoneHolder = shopPhoneField.getText().toString().trim();

                PendingHolder = "0.00";

            }
        });




    }
    private void openhome() {
        Intent intent=new Intent(this,home.class);
        startActivity(intent);
    }


}

