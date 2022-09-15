package com.a_apps.ishaproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class productEdit extends AppCompatActivity {
    SharedPreferences sp2;
    String PNam;
    EditText n,p,w,n3,p3,w3;

    TextView wsp;
    String name,price,wholesale,nnn;
    Button saveP;

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, product.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);



        // reciving value of ShopNam and setting Title
        sp2 = getSharedPreferences("products", MODE_PRIVATE);
        final String SName = sp2.getString("PName", "");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        PNam = SName;
        getSupportActionBar().setTitle(PNam);

        n=findViewById(R.id.editName);
        p=findViewById(R.id.editPrice);
        w=findViewById(R.id.editWholesale);
        wsp=findViewById(R.id.wsp);

        saveP=findViewById(R.id.saveP);







        Query ref;
        ref = FirebaseDatabase.getInstance().getReference().child("products").orderByChild("productName").equalTo(PNam);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                        constructorProductDetails csp = DataSnapshot.getValue(constructorProductDetails.class);
                        name = csp.getProductName();
                        price = csp.getProductPrice();
                        wholesale = csp.getProductWholesale();
                        //nnn = DataSnapshot.child("productName").getValue(String.class);
                    }
                    //Toast.makeText(getApplicationContext(), nnn, Toast.LENGTH_LONG).show();
                    n.setText(name);
                    p.setText(price);
                    w.setText(wholesale);
                    //wsp.setText(name);
                }
                if (!snapshot.exists()) {
                    // Toast.makeText(getApplicationContext(), "caaaaaaaaaaaaaaancel", Toast.LENGTH_LONG).show();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        n=findViewById(R.id.editName);
        p=findViewById(R.id.editPrice);
        w=findViewById(R.id.editWholesale);



        saveP.setOnClickListener(new View.OnClickListener() {

            String n2,p2,w2;

            @Override
            public void onClick(View v) {

                GetDataFromEditText();

                Query ref;
                ref = FirebaseDatabase.getInstance().getReference().child("products").orderByChild("productName").equalTo(PNam);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().child("productName").setValue(n2);
                            ds.getRef().child("productPrice").setValue(p2);
                            ds.getRef().child("productWholesale").setValue(w2);


                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                openProductsMain();



            }


            private void GetDataFromEditText() {

                n3=findViewById(R.id.editName);
                p3=findViewById(R.id.editPrice);
                w3=findViewById(R.id.editWholesale);

                n2=n3.getText().toString();
                p2=p3.getText().toString();
                w2=w3.getText().toString();}

        });









    }






    private void openProductsMain() {
        Intent intent = new Intent(this, product.class);
        startActivity(intent);
    }
}
