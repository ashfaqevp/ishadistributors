package com.a_apps.ishaproducts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class stock extends AppCompatActivity {
    //Button addStock;
    FloatingActionButton addStock;
    String TDate;
    adapterStockListView adapter;
    adapterStockListView adapter2;
    RecyclerView currentSL,NewSL;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(Color.parseColor("#370774"));

        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar aBar;
        //aBar= getSupportActionBar();
        //ColorDrawable cd = new ColorDrawable(Color.parseColor("#573c7e"));
        //aBar.setBackgroundDrawable(cd);




        addStock=findViewById(R.id.addStock);
        addStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddStock();

            }
        });

        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        TDate = dateFormat.format(Calendar.getInstance().getTime());

        NewSL= findViewById(R.id.newStockList);
        // NewSL.setHasFixedSize(true);
        NewSL.setLayoutManager(new LinearLayoutManager(this));

        currentSL= findViewById(R.id.currentStockList);
        // currentSL.setHasFixedSize(true);
        currentSL.setLayoutManager(new LinearLayoutManager(this));


        progressDialog = new ProgressDialog(stock.this);

        progressDialog.setMessage("Loading Data");

        progressDialog.setCanceledOnTouchOutside(false);




        showCurrentStock();
        showNewStock();









    }
    private void openAddStock() {
        Intent intent=new Intent(this,addNewStock.class);
        startActivity(intent);
    }

    private void showCurrentStock() {




        DatabaseReference mbase3;

        mbase3 = FirebaseDatabase.getInstance().getReference()
                .child("Stock").child("currentStock").child("stock");
        // .child("Reports").child("Daily Reports").child(TDate).child("Cash List");
        FirebaseRecyclerOptions<constructorViewStockProduct> options2 = new FirebaseRecyclerOptions.Builder<constructorViewStockProduct>()
                .setQuery(mbase3, constructorViewStockProduct.class).build();



        adapter = new adapterStockListView(options2);


        currentSL.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //progressDialog.dismiss();
    }


    private void showNewStock() {




        DatabaseReference mbase3;

        mbase3 = FirebaseDatabase.getInstance().getReference()
                //.child("Reports").child("Daily Reports").child(TDate).child("pList");
                .child("Stock").child("DateVise").child(TDate).child("stock");
        FirebaseRecyclerOptions<constructorViewStockProduct> options2 = new FirebaseRecyclerOptions.Builder<constructorViewStockProduct>()
                .setQuery(mbase3, constructorViewStockProduct.class).build();



        adapter2 = new adapterStockListView(options2);


        NewSL.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }


    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();
        adapter2.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();


        adapter.stopListening();
        adapter2.stopListening();
    }


}
