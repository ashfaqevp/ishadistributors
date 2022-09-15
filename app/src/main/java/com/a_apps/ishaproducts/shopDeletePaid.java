package com.a_apps.ishaproducts;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.a_apps.ishaproducts.home;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class shopDeletePaid extends AppCompatActivity {
    SharedPreferences sp2;
    String ShopNam;
    String TDate;


    Button ok;
    EditText dltText;
    String dltAmount;
    TextView todayPaid;
    String todayPaidAmnt;
    TextView shopHeadPending;
    String sPending;

    String pay;
    ProgressDialog progressDialog;
    int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_delete_paid);




        // today date

        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        TDate = dateFormat.format(Calendar.getInstance().getTime());





        progressDialog = new ProgressDialog(shopDeletePaid.this);
        progressDialog.setMessage("Saving Data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        // progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);



        // reciving value of ShopNam and setting Title
        sp2 = getSharedPreferences("shoping", MODE_PRIVATE);
        final String SName = sp2.getString("SName", "");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ShopNam = SName;
        getSupportActionBar().setTitle(ShopNam);
        Toast.makeText(getApplicationContext(), ShopNam, Toast.LENGTH_LONG).show();


        showTodayPaid();
        showShopPending();



        ok=findViewById(R.id.OkDeletPaid);
        dltText=findViewById(R.id.deleteText);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dltAmount=dltText.getText().toString();
                pay=dltText.getText().toString();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(shopDeletePaid.this);
                alertDialogBuilder.setMessage("Are you sure want to DELETE "+dltAmount);
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                progressDialog.show();

                                shopPending();
                                productPrice();

                                totalPending();
                                SaleAPendingWholesaleAmount();

                                shopCashList();


                                dltText.setText("");
                                openShop();

                            }


                        });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

                AlertDialog alertDialog=alertDialogBuilder.create();
                alertDialog.show();




















            }
        });



    }

    private void showShopPending() {

        shopHeadPending = findViewById(R.id.shopHeadPendingDE);
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

                shopHeadPending.setText(sPending);





            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    private void showTodayPaid() {
        todayPaid=findViewById(R.id.TodayPaid);

        final DatabaseReference ref2;
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        ref2 =database.getReference().child("Report").child(TDate).child("shopReport").child(ShopNam).child("csr");
        ref2.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //for (DataSnapshot data : snapshot.getChildren()){


                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {



                    String ab = snapshot.child("paid").getValue(String.class);
                    todayPaidAmnt=ab;


                }
                todayPaid.setText(todayPaidAmnt);




            }


            @Override
            public void  onCancelled(@NonNull DatabaseError error) {

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
                        DataSnapshot.getRef().child("shopPending").setValue(String.format("%.2f", Double.parseDouble(sp2) + Double.parseDouble(pay)))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        //progressDialog.dismiss();
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
                csr.setPaid(String.format("%.2f",  Double.parseDouble(String.valueOf(ppPaid[0]))- Double.parseDouble(pay) ));


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
                ref4.child("tp").setValue(String.format("%.2f",  Double.parseDouble(String.valueOf(tp[0]))+Double.parseDouble(pay )))
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

                constructorCSP csp=new constructorCSP();
                csp.sets(sale[0]);
                csp.setp(allPending[0]);
                csp.seta(wholesaleAmount[0]);
                csp.setc(String.format("%.2f",  Double.parseDouble(collection[0])-Double.parseDouble(pay) ));



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


        FirebaseDatabase database7=FirebaseDatabase.getInstance();
        ref8 =database7.getReference().child("Reports").child("Daily Reports").child(TDate).child("Cash List");
        Query query7 = ref8.orderByChild("purchasedItem").equalTo(ShopNam);
        query7.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //for (DataSnapshot data : snapshot.getChildren()){
                if(snapshot.exists()){

                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {




                        String ab = DataSnapshot.child("purchasedQnty").getValue(String.class);


                        DataSnapshot.getRef().child("purchasedQnty").setValue(String.format("%.2f", Double.parseDouble(ab) - Double.parseDouble(pay)))


                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                        ProgressDialogProgress();
                                    }
                                });

                    }



                }   if(! snapshot.exists()){


                }

            }


            @Override
            public void  onCancelled(@NonNull DatabaseError error) {

            }
        });


    }









    public void ProgressDialogProgress() {
        progress += 10;
        if (progress == 50) {
            progressDialog.dismiss();
            openShop();
            progress = 0;
        }

    }


























































    private void openShop() {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
    }

}
