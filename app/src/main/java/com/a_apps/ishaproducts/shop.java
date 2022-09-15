package com.a_apps.ishaproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class shop extends AppCompatActivity {


    SharedPreferences sp2;

    String ShopNam;
    String sPending;

    Button billBtn, orderBtn;
    FloatingActionButton addPaidBtn;
    EditText editPaid;
    TextView shopHeadPending;


    String pay;
    ProgressDialog progressDialog;
    int progress;


    RecyclerView.Adapter adapter;
    RecyclerView listView, listView2;
    List<constructerShopReportPurchase> csrpList = new ArrayList<>();
    String TDate;
    String ab;
    String itemS;

    String YDate;
    String LDate;

    List<constructorShopDetails> list = new ArrayList<>();
    List<constructorShopDetails> shopList = new ArrayList<>();
    adapterShop shop_adapter;
    private onClickInterface onclickInterface;


    adapterFirebasePurchase adapter2, adapter3;

    String STLPending = "0", STPurchase = "0", STPrice = "0", STPaid = "0", STTotal = "0", STBalance = "0";
    int K = 1;


    String stringTotalPending;
    public String Price = "00", Paid = "0", LPending = "0", Balance = "0", LBalance = "0", cprice = "00", cpaid = "00", cbalance = "00";
    public String yPrice = "00", yPaid = "0", yBalance = "0", yLBalance = "0", ycprice = "00", ycpaid = "00", ycbalance = "00";
    String CurrentPrice = "0a", CurrentPaid = "0s", CurrentBalance = "0";
    TextView TTLPending, TTPurchase, TTPrice, TTPaid, TTotal, TTBalance;
    TextView yTTLPending, yTTPurchase, yTTPrice, yTTPaid, yTTotal, yTTBalance;

    String cpr2;

    String rs = "0", rp = "0", rc = "0", rtp = "0";

    ImageButton btnOpenHistory;
    Button editBtn;
    ImageButton historyBtn;

    TextView title;

    ImageButton edit;

    Boolean P1 = false;
    Boolean P2 = false;
    Boolean P3 = false;


    adapterFirebasePurchase2 adapterO2;
    adapterFirebasePurchase3 adapterO3;

    RecyclerView listViewO;
    RecyclerView listViewO2;
    ImageButton deleteOrderBtn;
    final Context c = this;


    List peerList = new ArrayList();
    String[] AllProducts2 = {"yy,T"};
    String item, price = "0.00", amount2 = "0.00";
    String QuantityP = "0", priceP = "0", itemP = "0", sItemP = "0", amountP = "0";
    String Quantity = "1";


    String lastOrderDate;
    TextView lastOrderDateView;

    String[] allOrderDate = {"yyTt"};
    List orderDateList = new ArrayList();


    //history Dialog
    ImageButton orderHistoryBtn;
    String sDate;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    DatePickerDialog picker;
    adapterFirebasePurchase2 adapterH;
    String hsprice, hsbalance, hspaid;


    //share
    ImageButton shareBtn;
    View view;
    String ImagePath;
    Uri URI;
    String sPhone;

    ImageButton shopDetailsBtn;

    String iname, iplace, iphone;


    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, home.class);
        startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);


        getSupportActionBar().hide();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#f56899"));


        orderDate();


        progressDialog = new ProgressDialog(shop.this);
        progressDialog.setMessage("Saving Data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        // progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        TTLPending = findViewById(R.id.LDlpend);
        TTPrice = findViewById(R.id.LDprice);
        TTPaid = findViewById(R.id.LDpaid);
        TTotal = findViewById(R.id.LDt);
        TTBalance = findViewById(R.id.LDpending);


        yTTLPending = findViewById(R.id.YLDlpend);
        yTTPrice = findViewById(R.id.YLDprice);
        yTTPaid = findViewById(R.id.YLDpaid);
        yTTotal = findViewById(R.id.YLDt);
        yTTBalance = findViewById(R.id.YLDpending);


        btnOpenHistory = findViewById(R.id.showHistoryBtn);

        historyBtn = findViewById(R.id.history);


        // permission

        checkPermission();


// today date

        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        TDate = dateFormat.format(Calendar.getInstance().getTime());


// reciving value of ShopNam and setting Title
        sp2 = getSharedPreferences("shoping", MODE_PRIVATE);
        final String SName = sp2.getString("SName", "");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ShopNam = SName;
        getSupportActionBar().setTitle(ShopNam);
        //Toast.makeText(getApplicationContext(), ShopNam, Toast.LENGTH_LONG).show();


        title = findViewById(R.id.title);
        title.setText(ShopNam);

        String YD;


// shop purchase report
        listView = findViewById(R.id.rPurchaseT);
        // listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Reports").child("Daily Reports").child(TDate).child("Shop Reports").child(ShopNam).child("pList");

        FirebaseRecyclerOptions<constructerShopReportPurchase> options = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();

        adapter2 = new adapterFirebasePurchase(options);
        listView.setAdapter(adapter2);
        //listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));


//get Pending Value and today Report
        showPending();


// show yest report
        showYReport();


//order history

        orderHistoryBtn = findViewById(R.id.orderhistoryBtn);
        orderHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                createDialogOrderHistory();
            }
        });


//shop history


        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogShopHistory();
            }
        });


        orders();


//Set  Current Paid Pending

        //setCurrentPricePaid();

        DatabaseReference ref3;
        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        ref3 = database2.getReference().child("Reports").child("Daily Reports").child(TDate).child("Shop Reports").child(ShopNam).child("csr");
        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                    constructerShopReport csr = snapshot.getValue(constructerShopReport.class);
                    cprice = csr.getPrice();
                    cpaid = csr.getPaid();
                    cbalance = csr.getBalance();


                }

                cprice = cprice;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//open Bill Menu
        billBtn = findViewById(R.id.billBtn);
        billBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBill2();
            }
        });


        orderBtn = findViewById(R.id.orderButton);
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrder();
            }
        });


// add Paid
        editPaid = findViewById(R.id.editPaid);
        addPaidBtn = findViewById(R.id.addPaidBtn);

        addPaidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(shop.this);
                alertDialogBuilder.setMessage("Are you sure    " + editPaid.getText().toString());
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pay = editPaid.getText().toString();
                                progressDialog.show();

                                shopPending();
                                productPrice();

                                totalPending();
                                SaleAPendingWholesaleAmount();

                                shopCashList();
                                editPaid.setText("");

                            }
                        });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        editPaid.setText("");
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });


        DatabaseReference ref4;
        FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        ref4 = database3.getReference().child("Reports").child("Daily Reports").child(TDate).child("Shop Reports").child(ShopNam).child("csr");
        ref4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {

                    constructerShopReport csr = snapshot.getValue(constructerShopReport.class);
                    Price = csr.getPrice();
                    Paid = csr.getPaid();
                    Balance = csr.getBalance();

                }

                TTLPending.setText(LPending);
                TTPrice.setText(Price);
                TTPaid.setText(Paid);
                TTBalance.setText(Balance);
                TTotal.setText(String.format("%.2f", Double.parseDouble(Balance) + Double.parseDouble(Price)));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // get csr Database value


        // AddEditBtn
        AddEditBtn();


//SHARE

        shareBtn = findViewById(R.id.share);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareDialog();
            }

        });


//Info

        shopDetailsBtn = findViewById(R.id.info);
        shopDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogShopDetails();
            }

        });


    }






















    private void showYReport() {


        showYProductReport();
        DateFormat dateFormat2 = new SimpleDateFormat("d MMM yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        YDate = dateFormat2.format((cal.getTime()));


        DatabaseReference ref6;
        FirebaseDatabase database6 = FirebaseDatabase.getInstance();
        ref6 = database6.getReference().child("Reports").child("Daily Reports").child(YDate).child("Shop Reports").child(ShopNam).child("csr");
        ref6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                    constructerShopReport ycsr = snapshot.getValue(constructerShopReport.class);
                    ycprice = ycsr.getPrice();
                    ycpaid = ycsr.getPaid();
                    ycbalance = ycsr.getBalance();
                }

                showYProductReport();
                yTTBalance.setText(ycbalance);


                yTTPrice.setText(ycprice);
                yTTPaid.setText(ycpaid);
                String yTotal2 = String.format("%.2f", Double.parseDouble(ycbalance) + Double.parseDouble(ycpaid));
                yTTotal.setText(yTotal2);
                yTTLPending.setText(String.format("%.2f", Double.parseDouble(yTotal2) - Double.parseDouble(ycprice)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        listView2 = findViewById(R.id.YrPurchaseT);
        //listView2.setHasFixedSize(true);
        listView2.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Reports").child("Daily Reports").child(YDate).child("Shop Reports").child(ShopNam).child("pList");

        FirebaseRecyclerOptions<constructerShopReportPurchase> options2 = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();

        adapter3 = new adapterFirebasePurchase(options2);
        listView2.setAdapter(adapter3);


    }


    private void showPending() {
        shopHeadPending = findViewById(R.id.shopHeadPending);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("shop").orderByChild("shopName").equalTo(ShopNam);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                        constructorShopDetails shop_details = DataSnapshot.getValue(constructorShopDetails.class);
                        sPending = shop_details.getShopPending();
                        sPhone = shop_details.getPhoneNumber();
                    }
                }
                // Toast.makeText(getApplicationContext(), sPending, Toast.LENGTH_LONG).show();
                shopHeadPending.setText(sPending);

                DatabaseReference ref2;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                ref2 = database.getReference().child("Reports").child("Daily Reports").child(TDate).child("Shop Reports").child(ShopNam).child("csr");

                DatabaseReference ref3;
                FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                ref3 = database2.getReference().child("Reports").child("Daily Reports").child(TDate).child("Shop Reports").child(ShopNam).child("csr");
                ref3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                            constructerShopReport csr = snapshot.getValue(constructerShopReport.class);
                            cprice = csr.getPrice();
                            cpaid = csr.getPaid();
                            cbalance = csr.getBalance();
                        }
                        showShopReport();
                        DatabaseReference ref2;
                        FirebaseDatabase database25 = FirebaseDatabase.getInstance();
                        ref2 = database25.getReference().child("Reports").child("Daily Reports").child(TDate).child("Shop Reports").child(ShopNam).child("csr");
                        constructerShopReport csr = new constructerShopReport();
                        csr.setBalance(sPending);
                        csr.setPrice(cprice);
                        csr.setPaid(cpaid);

                        ref2.setValue(csr);
                        // Toast.makeText(getApplicationContext(), "CurrentPrice  "+cprice, Toast.LENGTH_LONG).show();
                        TTBalance.setText(sPending);


                        TTPrice.setText(Price);
                        TTPaid.setText(Paid);
                        String Total2 = String.format("%.2f", Double.parseDouble(sPending) + Double.parseDouble(Paid));
                        TTotal.setText(Total2);
                        TTLPending.setText(String.format("%.2f", Double.parseDouble(Total2) - Double.parseDouble(Price)));

                        cprice = cprice;


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        DatabaseReference ref2;
                        FirebaseDatabase database25 = FirebaseDatabase.getInstance();
                        ref2 = database25.getReference().child("Reports").child("Daily Reports").child(TDate).child("Shop Reports").child(ShopNam).child("csr");
                        constructerShopReport csr = new constructerShopReport();
                        csr.setBalance(sPending);
                        csr.setPrice(cprice);
                        csr.setPaid(cpaid);

                        ref2.setValue(csr);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void openOrder() {
        Intent intent = new Intent(this, newOrder.class);
        startActivity(intent);
    }


    public void showShopReport() {


    }

    public void showYProductReport() {


        DateFormat dateFormat2 = new SimpleDateFormat("d MMM yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String YDate = dateFormat2.format((cal.getTime()));
        FirebaseDatabase mbase;
        DatabaseReference mbase3;
        DatabaseReference requests;


    }


    private void openHistory() {
        Intent intent = new Intent(this, shopHistory.class);
        startActivity(intent);
    }


    private void openShopEdit() {
        Intent intent = new Intent(this, shopEdit.class);
        startActivity(intent);
    }


    private void openShopAgain() {
        Intent intent = new Intent(this, shop.class);
        startActivity(intent);
    }


    private void openShop() {
        Intent intent = new Intent(this, shop.class);
        startActivity(intent);
    }

    private void openBill2() {
        Intent intent = new Intent(this, bill.class);
        startActivity(intent);
    }

    private void goTOdeletePaid() {
        Intent intent = new Intent(this, shopDeletePaid.class);
        startActivity(intent);
    }


    private void goTOdeleteProduct() {
        Intent intent = new Intent(this, shopDeleteProduct.class);
        startActivity(intent);
    }


    private void AddEditBtn() {

        edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(shop.this);
                alertDialogBuilder.setMessage("What You Want To Edit");
                alertDialogBuilder.setPositiveButton("Paid Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                checkEditPaidPermission();


                            }
                        });
                alertDialogBuilder.setNegativeButton("Delete Product", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        checkEditProductPermission();

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }


    private void shopPending() {


        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        Query query1 = databaseReference1.child("shop").orderByChild("shopName").equalTo(ShopNam);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {

                        // progressDialog.setMessage("Saving Data");
                        // progressDialog.setCanceledOnTouchOutside(false);
                        //progressDialog.show();

                        String sp2 = DataSnapshot.child("shopPending").getValue(String.class);
                        DataSnapshot.getRef().child("shopPending").setValue(String.format("%.2f", Double.parseDouble(sp2) - Double.parseDouble(pay)))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        ProgressDialogProgress();
                                    }
                                });

                    }

                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void productPrice() {

        // progressDialog.show();
        //progressDialog.setMessage("Saving Data");
        // progressDialog.setCanceledOnTouchOutside(false);

        final String[] ppPrice = {"0"};
        final String[] ppPaid = {"0"};
        final String[] ppBalance = {"0"};

        final DatabaseReference ref2;
        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        ref2 = database2.getReference().child("Reports").child("Daily Reports").child(TDate).child("Shop Reports").child(ShopNam).child("csr");
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                    constructerShopReport csr = snapshot.getValue(constructerShopReport.class);
                    ppPrice[0] = csr.getPrice();
                    ppPaid[0] = csr.getPaid();
                    ppBalance[0] = csr.getBalance();
                    // progressDialog.show();


                }

                constructerShopReport csr = new constructerShopReport();
                csr.setPrice(ppPrice[0]);
                csr.setBalance(ppBalance[0]);
                csr.setPaid(String.format("%.2f", Double.parseDouble(pay) + Double.parseDouble(String.valueOf(ppPaid[0]))));


                ref2.setValue(csr)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                // progressDialog.dismiss();
                                ProgressDialogProgress();
                            }
                        });
                // ref2.child("balance").setValue(String.format("%.2f", Double.parseDouble(price) + Double.parseDouble("5")));


                // progressDialog.dismiss();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void totalPending() {


        final String[] tp = {"0"};
        final DatabaseReference ref4;
        FirebaseDatabase database4 = FirebaseDatabase.getInstance();
        ref4 = database4.getReference().child("Reports").child("total pending");
        ref4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {

                    constructorCSP csp = snapshot.getValue(constructorCSP.class);
                    tp[0] = csp.gettp();


                }
                ref4.child("tp").setValue(String.format("%.2f", Double.parseDouble(String.valueOf(tp[0])) - Double.parseDouble(pay)))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                ProgressDialogProgress();
                            }
                        });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void SaleAPendingWholesaleAmount() {


        final String[] sale = {"0"};
        final String[] allPending = {"0"};
        final String[] wholesaleAmount = {"0"};
        final String[] collection = {"0"};

        final DatabaseReference ref5;
        FirebaseDatabase database5 = FirebaseDatabase.getInstance();
        ref5 = database5.getReference().child("Reports").child("Daily Reports").child(TDate).child("CSPA");
        ref5.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {

                    constructorCSP csp = snapshot.getValue(constructorCSP.class);

                    sale[0] = csp.gets();
                    allPending[0] = csp.getp();
                    wholesaleAmount[0] = csp.geta();
                    collection[0] = csp.getc();


                }

                constructorCSP csp = new constructorCSP();
                csp.sets(sale[0]);
                csp.setp(allPending[0]);
                csp.seta(wholesaleAmount[0]);
                csp.setc(String.format("%.2f", Double.parseDouble(pay) + Double.parseDouble(collection[0])));


                ref5.setValue(csp)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                ProgressDialogProgress();
                            }
                        });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void shopCashList() {


        final DatabaseReference ref8;
        FirebaseDatabase database7 = FirebaseDatabase.getInstance();
        ref8 = database7.getReference().child("Reports").child("Daily Reports").child(TDate).child("Cash List");
        Query query7 = ref8.orderByChild("purchasedItem").equalTo(ShopNam);
        query7.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //for (DataSnapshot data : snapshot.getChildren()){
                if (snapshot.exists()) {

                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                        String ab = DataSnapshot.child("purchasedQnty").getValue(String.class);


                        DataSnapshot.getRef().child("purchasedQnty").setValue(String.format("%.2f", Double.parseDouble(ab) + Double.parseDouble(pay)))


                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                        ProgressDialogProgress();
                                    }
                                });

                    }


                }
                if (!snapshot.exists()) {

                    constructerShopReportPurchase csrp = new constructerShopReportPurchase();
                    csrp.setPurchasedItem(ShopNam);

                    csrp.setPurchasedQnty(pay);


                    String UserId = ref8.push().getKey();
                    ref8.child(UserId).setValue(csrp)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    ProgressDialogProgress();
                                }
                            });
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void ProgressDialogProgress() {
        progress += 10;
        if (progress == 50) {
            progressDialog.dismiss();
            //showPending();
            openShopAgain();
            progress = 0;
        }

    }


    private void showTodayReport() {


        DatabaseReference ref4;
        FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        ref4 = database3.getReference().child("Reports").child("Daily Reports").child(TDate).child("Shop Reports").child(ShopNam).child("csr");
        ref4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {

                    constructerShopReport csr = snapshot.getValue(constructerShopReport.class);
                    Price = csr.getPrice();
                    Paid = csr.getPaid();
                    Balance = csr.getBalance();

                }

                TTLPending.setText(LPending);
                TTPrice.setText(Price);
                TTPaid.setText(Paid);
                TTBalance.setText(Balance);
                TTotal.setText(String.format("%.2f", Double.parseDouble(LBalance) - Double.parseDouble(Price)));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        shopHeadPending = findViewById(R.id.shopHeadPending);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("shop").orderByChild("shopName").equalTo(ShopNam);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                        constructorShopDetails shop_details = DataSnapshot.getValue(constructorShopDetails.class);
                        sPending = shop_details.getShopPending();
                    }
                }
                // Toast.makeText(getApplicationContext(), sPending, Toast.LENGTH_LONG).show();
                shopHeadPending.setText(sPending);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void checkPermission() {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("Permissions").orderByChild("SaleEdit").equalTo("true");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {

                        Toast.makeText(getApplicationContext(), "p t 1", Toast.LENGTH_LONG).show();


                        P1 = true;

                    }
                    P1 = true;
                    Toast.makeText(getApplicationContext(), "p t 2", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference();
        Query query2 = databaseReference2.child("Permissions").orderByChild("PaidEdit").equalTo("true");
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                        P2 = true;

                    }
                    P2 = true;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void checkEditPaidPermission() {

        if (P2 == true) {
            goTOdeletePaid();
        } else {


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(shop.this);
            alertDialogBuilder.setMessage("You Have No Permission");
            alertDialogBuilder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }


    private void checkEditProductPermission() {

        if (P1 == true) {
            goTOdeleteProduct();
        } else {


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(shop.this);
            alertDialogBuilder.setMessage("You Have No Permission");
            alertDialogBuilder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }


    private void orders() {


        listViewO = findViewById(R.id.todayOrder);
        //listView.setHasFixedSize(true);
        listViewO.setLayoutManager(new LinearLayoutManager(this));

        listViewO2 = findViewById(R.id.yesterdayOrder);
        //listView2.setHasFixedSize(true);
        listViewO2.setLayoutManager(new LinearLayoutManager(this));


        showTOrder();
        showLOrder();
    }


    private void showLOrder() {

        LDate = YDate;

        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Orders").child("Daily Orders").child(LDate).child("Shop Orders").child(ShopNam).child("pList");

        FirebaseRecyclerOptions<constructerShopReportPurchase> options2 = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();

        adapterO2 = new adapterFirebasePurchase2(options2);
        listViewO.setAdapter(adapterO2);
        adapterO2.notifyDataSetChanged();


    }


    private void showTOrder() {

        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Orders").child("Daily Orders").child(TDate).child("Shop Orders").child(ShopNam).child("pList");

        FirebaseRecyclerOptions<constructerShopReportPurchase> options2 = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();

        adapterO3 = new adapterFirebasePurchase3(options2);
        adapterO3.startListening();
        listViewO2.setAdapter(adapterO3);
        adapterO3.notifyDataSetChanged();


        deleteOrderBtn = findViewById(R.id.btnDeleteOrder);

        deleteOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_delete_order, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c, R.style.CustomDialog2);
                alertDialogBuilderUserInput.setView(mView);
                final AlertDialog dialogR = alertDialogBuilderUserInput.create();


                final ImageButton ANP = mView.findViewById(R.id.ANP);

                final TextView pName = mView.findViewById(R.id.NPName);
                final TextView pRate = mView.findViewById(R.id.NPRate);
                final TextView pQnty = mView.findViewById(R.id.addedProductsQnty);
                final TextView pTotal = mView.findViewById(R.id.NPTotal);

                final AutoCompleteTextView searchProduct = mView.findViewById(R.id.searchProduct);

                final EditText PQ = mView.findViewById(R.id.PQ);

                final FloatingActionButton del = mView.findViewById(R.id.deleteButton);


                DatabaseReference dr;
                dr = FirebaseDatabase.getInstance().getReference().child("products");
                dr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            constructorProductDetails cProductDetails = dataSnapshot.getValue(constructorProductDetails.class);
                            String speer = cProductDetails.getProductName();

                            peerList.add(speer);

                        }
                        AllProducts2 = (String[]) peerList.toArray(new String[peerList.size()]);


                        ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(c, R.layout.support_simple_spinner_dropdown_item, AllProducts2);
                        searchProduct.setThreshold(1);
                        searchProduct.setAdapter(adapterSearch);
                        searchProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                item = parent.getItemAtPosition(position).toString();
                                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
                                //addingPrice();
                                getPaid();


                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                ANP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Quantity = PQ.getText().toString();
                        //item=searchProduct.getText().toString();


                        // if (PQ.length() > 0 && item.length()>0) {

                        pName.setText(item);
                        pRate.setText(price);
                        pQnty.setText(Quantity);
                        pTotal.setText(String.format("%.2f", Double.parseDouble(price) * Double.parseDouble(Quantity)));


                    }


                });


                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleting();
                        openShop();

                    }
                });


                // final TextView dateView = mView.findViewById(R.id.DateOrder);


                dialogR.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogR.show();


            }
        });


    }


    private void getPaid() {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("products").orderByChild("productName").equalTo(item);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                        String ab = DataSnapshot.child("productPrice").getValue(String.class);
                        String ab2 = DataSnapshot.child("productWholesale").getValue(String.class);
                        price = ab;
                        amount2 = ab2;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void deleting() {

        // product=pName.getText().toString();
        // t=pName.getText().toString();
        // q=pQnty.getText().toString();
        itemP = item;
        priceP = String.format("%.2f", Double.parseDouble(price) * Double.parseDouble(Quantity));
        QuantityP = Quantity;
        amountP = String.format("%.2f", Double.parseDouble(amount2) * Double.parseDouble(Quantity));
        sItemP = itemP;


        //shopList

        final DatabaseReference ref6;
        final String[] r = {"0"};
        final String[] q = {"0"};
        final String[] i = {itemP};

        FirebaseDatabase database6 = FirebaseDatabase.getInstance();
        ref6 = database6.getReference().child("Orders").child("Daily Orders").child(TDate).child("Shop Orders").child(ShopNam).child("pList");
        Query query6 = ref6.orderByChild("purchasedItem").equalTo(itemP);
        query6.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //for (DataSnapshot data : snapshot.getChildren()){
                if (snapshot.exists()) {

                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                        constructerShopReportPurchase csrp = snapshot.getValue(constructerShopReportPurchase.class);
                        q[0] = csrp.getPurchasedQnty();
                        r[0] = csrp.getPurchasedPrice();


                        // csrp.setPurchasedItem(item);
                        // csrp.setPurchasedPrice(String.format("%.2f", Double.parseDouble(rate) + Double.parseDouble(r[0])));
                        // csrp.setPurchasedQnty(String.format("%.2f", Double.parseDouble(Quantity) + Double.parseDouble(q[0])));

                        // String UserId2 = ref6.push().getKey();

                        String ab = DataSnapshot.child("purchasedQnty").getValue(String.class);
                        String ab2 = DataSnapshot.child("purchasedPrice").getValue(String.class);

                        DataSnapshot.getRef().child("purchasedQnty").setValue(String.format("%.0f", Double.parseDouble(ab) - Double.parseDouble(QuantityP)));
                        DataSnapshot.getRef().child("purchasedPrice").setValue(String.format("%.2f", Double.parseDouble(ab2)
                                - Double.parseDouble(priceP)))


                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                    }
                                });
                    }


                }
                if (!snapshot.exists()) {


                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //fullList


        final DatabaseReference ref7;
        FirebaseDatabase database7 = FirebaseDatabase.getInstance();
        ref7 = database7.getReference().child("Orders").child("Daily Orders").child(TDate).child("pList");
        Query query7 = ref7.orderByChild("purchasedItem").equalTo(itemP);
        query7.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //for (DataSnapshot data : snapshot.getChildren()){
                if (snapshot.exists()) {

                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                        String ab = DataSnapshot.child("purchasedQnty").getValue(String.class);

                        DataSnapshot.getRef().child("purchasedQnty").setValue(String.format("%.0f", Double.parseDouble(ab) - Double.parseDouble(QuantityP)))


                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                    }
                                });


                    }


                }
                if (!snapshot.exists()) {


                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void orderDate() {

        // lastOrderDateView=findViewById(R.id.orderDate);


    }


//SHOP HISTORY

    private void createDialogShopHistory() {


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
        picker = new DatePickerDialog(shop.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        sDate = dayOfMonth + " " + MONTHS[monthOfYear] + " " + year;

                        dialogShopHistory();

                        //  Toast.makeText(getApplicationContext(), sDate+"Hell0", Toast.LENGTH_LONG).show();
                    }
                }, year, month, day);
        picker.show();

    }


    private void dialogShopHistory() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_shop_history, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c, R.style.CustomDialog2);
        alertDialogBuilderUserInput.setView(mView);
        final AlertDialog dialogR = alertDialogBuilderUserInput.create();


        final TextView dateView = mView.findViewById(R.id.dateView);
        final TextView hlPending = mView.findViewById(R.id.hpending);
        final TextView hprice = mView.findViewById(R.id.hprice);
        final TextView htotal = mView.findViewById(R.id.htotal);
        final TextView hpaid = mView.findViewById(R.id.hpaid);
        final TextView hbalance = mView.findViewById(R.id.hbalance);

        final RecyclerView orderView = mView.findViewById(R.id.hpurchase);
        orderView.setLayoutManager(new LinearLayoutManager(c));
        orderView.hasFixedSize();
        dateView.setText(sDate);


        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Reports").child("Daily Reports").child(sDate).child("Shop Reports").child(ShopNam).child("csr");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                    constructerShopReport csr = snapshot.getValue(constructerShopReport.class);
                    hsprice = csr.getPrice();
                    hspaid = csr.getPaid();
                    hsbalance = csr.getBalance();


                }

                hprice.setText(hsprice);
                hpaid.setText(hspaid);
                hbalance.setText(hsbalance);

                String Total2 = String.format("%.2f", Double.parseDouble(hsbalance) + Double.parseDouble(hspaid));
                htotal.setText(Total2);
                hlPending.setText(String.format("%.2f", Double.parseDouble(Total2) - Double.parseDouble(hsprice)));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Reports").child("Daily Reports").child(sDate).child("Shop Reports").child(ShopNam).child("pList");
        FirebaseRecyclerOptions<constructerShopReportPurchase> options2 = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();
        adapterH = new adapterFirebasePurchase2(options2);
        adapterH.startListening();
        orderView.setAdapter(adapterH);
        adapterH.notifyDataSetChanged();


        dialogR.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogR.show();


    }


    //ORDER HISTORY

    private void createDialogOrderHistory() {


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
        picker = new DatePickerDialog(shop.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        sDate = dayOfMonth + " " + MONTHS[monthOfYear] + " " + year;

                        dialogOrderHistory();

                        //  Toast.makeText(getApplicationContext(), sDate+"Hell0", Toast.LENGTH_LONG).show();
                    }
                }, year, month, day);
        picker.show();

    }

    private void dialogOrderHistory() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_shop_order_history, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c, R.style.CustomDialog2);
        alertDialogBuilderUserInput.setView(mView);
        final AlertDialog dialogR = alertDialogBuilderUserInput.create();


        final TextView dateView = mView.findViewById(R.id.DateOrder);

        final RecyclerView orderView = mView.findViewById(R.id.orderView);
        orderView.setLayoutManager(new LinearLayoutManager(c));
        orderView.hasFixedSize();

        dateView.setText(sDate);


        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Orders").child("Daily Orders").child(sDate).child("Shop Orders").child(ShopNam).child("pList");
        FirebaseRecyclerOptions<constructerShopReportPurchase> options2 = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();
        adapterH = new adapterFirebasePurchase2(options2);
        adapterH.startListening();
        orderView.setAdapter(adapterH);
        adapterH.notifyDataSetChanged();


        dialogR.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogR.show();


    }














    private void dialogShopDetails() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_shop_details, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c, R.style.CustomDialog2);
        alertDialogBuilderUserInput.setView(mView);
        final AlertDialog dialogR = alertDialogBuilderUserInput.create();


        final TextView in = mView.findViewById(R.id.editName);
        final TextView ip = mView.findViewById(R.id.editPlace);
        final TextView iph = mView.findViewById(R.id.editPhone);
        final FloatingActionButton editSaveBtn = mView.findViewById(R.id.editSaveBtn);
        final FloatingActionButton phoneBtn = mView.findViewById(R.id.phonBtn);


        Query ref;
        ref = FirebaseDatabase.getInstance().getReference().child("shop").orderByChild("shopName").equalTo(ShopNam);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                        constructorShopDetails csp = DataSnapshot.getValue(constructorShopDetails.class);
                        iname = csp.getShopName();
                        iplace = csp.getShopPlace();
                        // ipending = csp.getShopPending();
                        iphone = csp.getPhoneNumber();
                    }
                    //Toast.makeText(getApplicationContext(), nnn, Toast.LENGTH_LONG).show();
                    in.setText(iname);
                    ip.setText(iplace);
                    iph.setText(iphone);

                }
                if (!snapshot.exists()) {
                    // Toast.makeText(getApplicationContext(), "caaaaaaaaaaaaaaancel", Toast.LENGTH_LONG).show();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        editSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Query ref;
                ref = FirebaseDatabase.getInstance().getReference().child("shop").orderByChild("shopName").equalTo(ShopNam);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().child("shopName").setValue(in.getText().toString());
                            ds.getRef().child("shopPending").setValue(sPending);
                            ds.getRef().child("shopPlace").setValue(ip.getText().toString());
                            ds.getRef().child("phoneNumber").setValue(iph.getText().toString());


                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }



                });

                dialogR.dismiss();

            }
        });


        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String number = iph.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                startActivity(callIntent);

                dialogR.dismiss();

            }
        });




      //  final TextView dateView = mView.findViewById(R.id.DateOrder);





        dialogR.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogR.show();


    }





















    public void shareDialog() {





        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_share, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c, R.style.CustomDialog2);
        alertDialogBuilderUserInput.setView(mView);
        final AlertDialog dialogR = alertDialogBuilderUserInput.create();


        final FloatingActionButton btnShareWA = mView.findViewById(R.id.shareWAbtn);
        final LinearLayout content = mView.findViewById(R.id.layout_sharable);

        final TextView DateView = mView.findViewById(R.id.DateView);
        final TextView ShopView = mView.findViewById(R.id.ShopView);

        final TextView swOB = mView.findViewById(R.id.hpending);
        final TextView swPrice = mView.findViewById(R.id.hprice);
        final TextView swTotal = mView.findViewById(R.id.htotal);
        final TextView swPaid = mView.findViewById(R.id.hpaid);
        final TextView swBalance = mView.findViewById(R.id.hbalance);

        final RecyclerView sPurchase = mView.findViewById(R.id.hpurchase);


        DateView.setText("Date : "+TDate.toString());
        ShopView.setText("Shop : "+ShopNam.toString());
        swOB.setText(TTLPending.getText().toString());
        swPrice.setText(TTPrice.getText().toString());
        swTotal.setText(TTotal.getText().toString());
        swPaid.setText(TTPaid.getText().toString());
        swBalance.setText(TTBalance.getText().toString());



        sPurchase.setLayoutManager(new LinearLayoutManager(this));
        adapter2.startListening();
        sPurchase.setAdapter(adapter2);




        btnShareWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Bitmap bitmap = Bitmap.createBitmap(content.getWidth(), content.getHeight(),Bitmap.Config.ARGB_8888);
                //Bind a canvas to it
                Canvas canvas = new Canvas(bitmap);
                //Get the view's background
                Drawable bgDrawable =content.getBackground();
                if (bgDrawable!=null) {
                    //has background drawable, then draw it on the canvas
                    bgDrawable.draw(canvas);
                }   else{
                    //does not have background drawable, then draw white background on the canvas
                    canvas.drawColor(Color.WHITE);
                }
                // draw the view on the canvas
                content.draw(canvas);




                 ImagePath = MediaStore.Images.Media.insertImage(
                        getContentResolver(),
                        bitmap,
                        "demo_image",
                        "demo_image"
                );

                 URI = Uri.parse(ImagePath);

             //   Toast.makeText(shop.this, "Image Saved Successfully", Toast.LENGTH_LONG).show();





                shareonWhatsapp();

             dialogR.dismiss();





            }
        });






        dialogR.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogR.show();

    }






















    public void shareonWhatsapp(){
        try {


            String text = "Balance : "+sPending;

            String toNumber = "91"+sPhone;

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }








































    @Override
    protected void onStart() {
        super.onStart();
        adapter2.startListening();
        adapter3.startListening();

        adapterO2.startListening();
        adapterO3.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        adapter2.startListening();
        adapter3.startListening();

        adapterO2.startListening();
        adapterO3.startListening();
    }




}