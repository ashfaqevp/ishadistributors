package com.a_apps.ishaproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class house extends AppCompatActivity {

    CardView shops;
    CardView products,stocks,cash;
    CardView report;
    CardView account;
    CardView order;
    TextView TVTotal;

    String rtp;


    String TDate;

    private ProgressBar progressBar;
    private TextView progressText;
    int i = 0;

    int iSale=0;
    int iStock=0;
    int iTotal=0;

    String hm="0",hmd="0",hms="0",sSale="0";
    String item="HM 525ml",itemS="HM 525ml sp",itemD="HM 525ml Dealer";
    String ab;






    @Override
    public void onBackPressed(){

        // super.onBackPressed();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(house.this);
        alertDialogBuilder.setMessage("Exit ?");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();


                    }});


        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                });
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);


        getSupportActionBar().hide();

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(Color.parseColor("#f56899"));


        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        TDate = dateFormat.format(Calendar.getInstance().getTime());









        i=30;










        account=findViewById(R.id.accountCard);
        report=findViewById(R.id.reportCard);
        shops=findViewById(R.id.cardShops);
        products=findViewById(R.id.cardProducts);
        stocks=findViewById(R.id.stocksCard);
        cash=findViewById(R.id.cashCard);
        order=findViewById(R.id.cardOrders);



        TVTotal = findViewById(R.id.totalPending);




        DatabaseReference ref4;
        FirebaseDatabase database4 = FirebaseDatabase.getInstance();
        ref4 = database4.getReference().child("Reports").child("total pending");
        ref4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                    constructorCSP csp = snapshot.getValue(constructorCSP.class);
                    rtp = csp.gettp();
                }
                TVTotal.setText(rtp);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








        shops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openhome();

            }
        });

        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProducts();
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccount();

            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReport();
            }
        });

        stocks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStock();

            }
        });

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCash();

            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrder();

            }
        });

    }




    private void openhome() {
        Intent intent=new Intent(this,home.class);
        startActivity(intent);
    }
    private void openProducts() {
        Intent intent=new Intent(this,product.class);
        startActivity(intent);
    }

    private void openStock() {
        Intent intent=new Intent(this,stock.class);
        startActivity(intent);
    }

    private void openAccount() {
        Intent intent=new Intent(this,account.class);
        startActivity(intent);
    }
    private void openReport() {
        Intent intent=new Intent(this,reportMenu.class);
        startActivity(intent);
    }
    private void openCash() {
        Intent intent=new Intent(this,cash.class);
        startActivity(intent);
    }
    private void openOrder() {
        Intent intent=new Intent(this,Order.class);
        startActivity(intent);
    }
}

