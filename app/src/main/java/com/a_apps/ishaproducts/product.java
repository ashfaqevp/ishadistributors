package com.a_apps.ishaproducts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class product extends AppCompatActivity {


    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    List<constructorProductDetails> list = new ArrayList<>();
    private onClickInterface onclickInterface;
    RecyclerView.Adapter adapter;
    List peerList=new ArrayList();
    String[] peerArray={"yy,T"};


    AutoCompleteTextView serchProduct;




    FloatingActionButton newP;

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, house.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);



        getSupportActionBar().hide();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#f56899"));




        recyclerView = findViewById(R.id.recycleP);

        onclickInterface = new onClickInterface() {
            @Override
            public void setClick(int abc) {


                openProductEdit();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void shopName(String shopN) {
                Toast.makeText(getApplicationContext(),shopN,Toast.LENGTH_LONG).show();

                SharedPreferences sp;
                sp= getApplicationContext().getSharedPreferences("products" , MODE_PRIVATE);
                sp.edit().putString("PName",shopN).apply();
                openProductEdit();

                adapter.notifyDataSetChanged();
            }
        };




        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        progressDialog = new ProgressDialog(product.this);

        progressDialog.setMessage("Loading Data");

        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();



        newP = findViewById(R.id.addProducts);
        newP.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        openNewProduct();
                                    }
                                }
        );


        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    constructorProductDetails cProductDetails = dataSnapshot.getValue(constructorProductDetails.class);
                    String speer=cProductDetails.getProductName();

                    peerList.add(speer);
                    list.add(cProductDetails);


                }
                peerArray =(String[]) peerList.toArray(new String[peerList.size()]);
                // Toast.makeText(getApplicationContext(), "peeer3  "+peerArray[3], Toast.LENGTH_LONG).show();


                serchProduct = findViewById(R.id.searchProduct);
                serchProduct.setThreshold(1);
                ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(product.this, R.layout.support_simple_spinner_dropdown_item, peerArray);
                serchProduct.setAdapter(adapterSearch);
                serchProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String item = parent.getItemAtPosition(position).toString();
                        SharedPreferences sp;
                        sp=getSharedPreferences("products" , MODE_PRIVATE);
                        sp.edit().putString("PName",item).apply();
                        openProductEdit();

                    }



                });

                adapter = new adapterProduct (product.this,list,onclickInterface);
                recyclerView.setAdapter(adapter);


                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });






    }

    private void openProductEdit() {
        Intent intent = new Intent(this, productEdit.class);
        startActivity(intent);
    }

    private void openNewProduct() {
        Intent intent = new Intent(this, addNewProduct.class);
        startActivity(intent);
    }

}
