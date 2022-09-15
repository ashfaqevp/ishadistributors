package com.a_apps.ishaproducts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class shopHistory extends AppCompatActivity {






    EditText editDate;
    ImageButton search;
    SharedPreferences sp2;
    String ShopNam;
    RecyclerView listView2;
    List reportDateList=new ArrayList();

    AutoCompleteTextView sDay,sMonth,sYear;
    ImageButton sBtn;
    TextView price,paid,balance,total,lPending,showdate;


    String[] dayArray={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    String[] monArray={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    String[] yearArray={"2020","2021","2022"};

    String day="0";
    String mon="0";
    String year="2021";

    String cprice,cpaid,cbalance;
    String date;


    adapterFirebasePurchase2 adapter;
    RecyclerView  listView;

    @Override
    public void onBackPressed(){
        finish();
        Intent intent=new Intent(this,shop.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_history);


        sp2 = getSharedPreferences("shoping", MODE_PRIVATE);
        final String SName = sp2.getString("SName", "");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ShopNam = SName;
        getSupportActionBar().setTitle(ShopNam);
        Toast.makeText(getApplicationContext(), ShopNam, Toast.LENGTH_LONG).show();


        sDay=findViewById(R.id.day);
        sMonth=findViewById(R.id.mon);
        sYear=findViewById(R.id.year);

        price=findViewById(R.id.WLDprice);
        paid=findViewById(R.id.WLDpaid);
        balance=findViewById(R.id.WLDpending);
        lPending=findViewById(R.id.WLDlpend);
        total=findViewById(R.id.WLDt);
        showdate=findViewById(R.id.date);



        sDay.setThreshold(1);
        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(shopHistory.this, R.layout.support_simple_spinner_dropdown_item, dayArray);
        sDay.setAdapter(adapterDay);
        sDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                day= parent.getItemAtPosition(position).toString();
            }
        });


        sMonth.setThreshold(1);
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(shopHistory.this, R.layout.support_simple_spinner_dropdown_item, monArray);
        sMonth.setAdapter(adapterMonth);
        sMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mon= parent.getItemAtPosition(position).toString();
            }
        });

        sYear.setThreshold(1);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(shopHistory.this, R.layout.support_simple_spinner_dropdown_item, yearArray);
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
                day=sDay.getText().toString();
                mon=sMonth.getText().toString();
                year=sYear.getText().toString();

                date=day+" "+mon+" "+year;

                try {
                    DatabaseReference databaseReference;
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Reports").child("Daily Reports").child(date).child("Shop Reports").child(ShopNam).child("csr");
                    databaseReference.addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                                constructerShopReport csr = snapshot.getValue(constructerShopReport.class);
                                cprice= csr.getPrice();
                                cpaid= csr.getPaid();
                                cbalance=csr.getBalance();



                            }

                            showPurchase();
                            adapter.notifyDataSetChanged();
                            price.setText(cprice);
                            paid.setText(cpaid);
                            balance.setText(cbalance);


                            showdate.setText(day+" "+mon+" "+year);
                            String Total2=String.format("%.2f", Double.parseDouble(cbalance) + Double.parseDouble(cpaid));
                            total.setText(Total2);
                            lPending.setText(String.format("%.2f", Double.parseDouble(Total2) - Double.parseDouble(cprice)));



                            adapter.startListening();



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }


                Toast.makeText(getApplicationContext(), day+" "+mon+" "+year ,Toast.LENGTH_LONG).show();

            }
        });


    }


    private void showPurchase() {


        listView = findViewById(R.id.WrPurchase);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        date=day+" "+mon+" "+year;


        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Reports").child("Daily Reports").child(date).child("Shop Reports").child(ShopNam).child("pList");

        FirebaseRecyclerOptions<constructerShopReportPurchase> options2 = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();

        adapter = new adapterFirebasePurchase2(options2);
        listView.setAdapter(adapter);





    }



/*   @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();


        adapter.startListening();
    }

 */


}
