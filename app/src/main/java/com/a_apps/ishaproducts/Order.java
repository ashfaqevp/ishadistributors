package com.a_apps.ishaproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Order extends AppCompatActivity {

    adapterFirebasePurchase2 adapter2;
    adapterFirebasePurchase3 adapter3;
    adapterFirebasePurchase2 adapterH;

    RecyclerView listView;
    RecyclerView  listView2;
    String TDate,YDate,sDate="2";
    ProgressDialog progressDialog;

    ImageButton historyBtn;


    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    DatePickerDialog picker;

    final Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        getSupportActionBar().hide();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#f56899"));



        // today date
        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        TDate = dateFormat.format(Calendar.getInstance().getTime());

// Yesterday Date
        DateFormat dateFormat2 = new SimpleDateFormat("d MMM yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        YDate = dateFormat2.format((cal.getTime()));



        listView = findViewById(R.id.todayOrderList);
        //listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));

        listView2 = findViewById(R.id.yesterdayOrderList);
        //listView2.setHasFixedSize(true);
        listView2.setLayoutManager(new LinearLayoutManager(this));




        progressDialog = new ProgressDialog(Order.this);

        progressDialog.setMessage("Loading Data");

        progressDialog.setCanceledOnTouchOutside(false);

        showTReport();
        showYReport();








        historyBtn=findViewById(R.id.historyBtnOrder);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final Calendar cldr = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
                sDate = dateFormat.format(cldr.getTime());





                final Calendar cldr2 = Calendar.getInstance();
                DateFormat dateFormat2 = new SimpleDateFormat("d MMM yyyy");
                sDate = dateFormat2.format(cldr2.getTime());

                int day = cldr2.get(Calendar.DAY_OF_MONTH);
                final int month = cldr2.get(Calendar.MONTH);
                int year = cldr2.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Order.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                sDate=dayOfMonth+" "+MONTHS[monthOfYear]+" "+year;

                                dialog();

                               //  Toast.makeText(getApplicationContext(), sDate+"Hell0", Toast.LENGTH_LONG).show();
                            }
                        }, year, month, day);
                picker.show();




            }
        });



    }



    private void dialog(){




        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_order, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c, R.style.CustomDialog2);
        alertDialogBuilderUserInput.setView(mView);
        final AlertDialog dialogR = alertDialogBuilderUserInput.create();


        final TextView dateView = mView.findViewById(R.id.DateOrder);

        final RecyclerView orderView=mView.findViewById(R.id.orderView);
        orderView.setLayoutManager(new LinearLayoutManager(c));
        orderView.hasFixedSize();



        dateView.setText(sDate);





        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Orders").child("Daily Orders").child(sDate).child("pList");

        FirebaseRecyclerOptions<constructerShopReportPurchase> options2 = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();

        adapterH = new adapterFirebasePurchase2(options2);
        adapterH.startListening();
        orderView.setAdapter(adapterH);
        adapterH.notifyDataSetChanged();





        dialogR.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogR.show();

    }





    private void showTReport() {




        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Orders").child("Daily Orders").child(TDate).child("pList");

        FirebaseRecyclerOptions<constructerShopReportPurchase> options2 = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();

        adapter2 = new adapterFirebasePurchase2(options2);
        listView.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();



    }







    private void showYReport() {




        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Orders").child("Daily Orders").child(YDate).child("pList");

        FirebaseRecyclerOptions<constructerShopReportPurchase> options2 = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();

        adapter3 = new adapterFirebasePurchase3(options2);
        listView2.setAdapter(adapter3);
        adapter3.notifyDataSetChanged();


    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter3.startListening();
        adapter2.startListening();

       // adapterH.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();


        adapter3.stopListening();
        adapter2.stopListening();

       // adapterH.stopListening();
    }





}
