package com.a_apps.ishaproducts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class reportMenu extends AppCompatActivity {


    String TDate;
    TextView TVSale, TVCollection, TVPending, TVTotal,amount,profit;
    String rtp="0.00";
    String TS="0.00";
    String TC="0.00";
    String TP="0.00";
    String amountS="0.00";
    DatabaseReference databaseReference;
    adapterFirebasePurchase2 adapter3;
    RecyclerView  listView2;
    ImageButton btnHistory;

    adapterStockListView adapter;
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, house.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_menu);

        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        TDate = dateFormat.format(Calendar.getInstance().getTime());


        TVTotal = findViewById(R.id.totalPending);

        TVSale = findViewById(R.id.saleTV);
        TVCollection = findViewById(R.id.collectionTV);
        TVPending = findViewById(R.id.pendingTV);

        amount=findViewById(R.id.amountM);
        profit=findViewById(R.id.profitM);

        Dashboard();
        showYReport();
        amountNprofit();


    }



    private void Dashboard() {


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

        ////
        ////
        ////___________PLEASE NOTE_________
        ////here i set total pending as today pending
        ///
        ///


        DatabaseReference ref3;
        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        ref3 = database2.getReference().child("Reports").child("Daily Reports").child(TDate).child("CSPA");
        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                    constructorCSP csp = snapshot.getValue(constructorCSP.class);
                    TC = csp.getc();
                    TP = csp.getp();
                    TS = csp.gets();
                    amountS=csp.geta();
                }
                TVSale.setText(TS);
                TVPending.setText(rtp);
                TVCollection.setText(TC);
                amount.setText(amountS);
                profit.setText(String.format("%.2f", Double.parseDouble(TS) - Double.parseDouble(amountS)));

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        shopHistory();


    }


    public void shopHistory() {


        btnHistory=findViewById(R.id.showHistoryBtn);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHistory();
            }
        });
    }

    private void openHistory() {
        Intent intent = new Intent(this, reportHistory.class);
        startActivity(intent);
    }


    private void showYReport() {


        listView2 = findViewById(R.id.wholsaleProducts);
        listView2.setHasFixedSize(true);
        listView2.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Reports").child("Daily Reports").child(TDate).child("pList");

        FirebaseRecyclerOptions<constructerShopReportPurchase> options2 = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();

        adapter3 = new adapterFirebasePurchase2(options2);
        listView2.setAdapter(adapter3);



    }

    private void amountNprofit() {



    }


    @Override
    protected void onStart() {
        super.onStart();

        adapter3.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();


        adapter3.startListening();
    }
}

