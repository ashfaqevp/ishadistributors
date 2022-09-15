package com.a_apps.ishaproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class reportHistory extends AppCompatActivity {

    AutoCompleteTextView sDay,sMonth,sYear;
    ImageButton sBtn;
    TextView showdate;


    String[] dayArray={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    String[] monArray={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String[] yearArray={"2020","2021","2022"};

    String day="0";
    String mon="0";
    String year="2021";
    String date;

    TextView TVSale, TVCollection, TVPending, TVTotal,dateName,amount,profit;
    String rtp;
    String TS;
    String TC;
    String TP;
    String amountS="0";

    adapterFirebasePurchase2 adapter;
    RecyclerView listView;

    @Override
    public void onBackPressed(){
        finish();
        Intent intent=new Intent(this,reportMenu.class);
        startActivity(intent);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);



        amount=findViewById(R.id.amount);
        profit=findViewById(R.id.profit);


        dateName=findViewById(R.id.date);
        sDay=findViewById(R.id.day);
        sMonth=findViewById(R.id.mon);
        sYear=findViewById(R.id.year);

        TVTotal = findViewById(R.id.totalPending);

        TVSale = findViewById(R.id.saleTV2);
        TVCollection = findViewById(R.id.collectionTV2);
        TVPending = findViewById(R.id.pendingTV2);


        showdate=findViewById(R.id.showDate);



        sDay.setThreshold(1);
        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(reportHistory.this, R.layout.support_simple_spinner_dropdown_item, dayArray);
        sDay.setAdapter(adapterDay);
        sDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                day= parent.getItemAtPosition(position).toString();
            }
        });


        sMonth.setThreshold(1);
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(reportHistory.this, R.layout.support_simple_spinner_dropdown_item, monArray);
        sMonth.setAdapter(adapterMonth);
        sMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mon= parent.getItemAtPosition(position).toString();
            }
        });

        sYear.setThreshold(1);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(reportHistory.this, R.layout.support_simple_spinner_dropdown_item, yearArray);
        sYear.setAdapter(adapterYear);
        sYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                year= parent.getItemAtPosition(position).toString();
            }
        });

        sBtn=findViewById(R.id.sBtn);
        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = sDay.getText().toString();
                mon = sMonth.getText().toString();
                year = sYear.getText().toString();

                date = day + " " + mon + " " + year;
                showReport();
                adapter.startListening();


                try {
                    DatabaseReference ref3;
                    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                    ref3 = database2.getReference().child("Report").child(date).child("shopReport").child("csp");
                    ref3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                                constructorCSP csp = snapshot.getValue(constructorCSP.class);
                                TC = csp.getc();
                                TP = csp.getp();
                                TS = csp.gets();
                            }
                            TVSale.setText(TS);
                            TVPending.setText(TP);
                            TVCollection.setText(TC);
                            dateName.setText(date);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }



                DatabaseReference ref33;
                FirebaseDatabase database23 = FirebaseDatabase.getInstance();
                ref33 = database23.getReference().child("Report").child(date).child("data");
                ref33.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                            amountS = snapshot.child("amount").getValue(String.class);


                        }
                        amount.setText(amountS);
                        profit.setText(String.format("%.2f", Double.parseDouble(TS) - Double.parseDouble(amountS)));



                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }});
    }


    private void showReport() {


        listView = findViewById(R.id.wholsaleProductsH);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Report").child("pSale").child(date);

        FirebaseRecyclerOptions<constructerShopReportPurchase> options2 = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();

        adapter = new adapterFirebasePurchase2(options2);
        listView.setAdapter(adapter);

















    }
}
