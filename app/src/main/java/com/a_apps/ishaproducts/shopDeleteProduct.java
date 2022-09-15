package com.a_apps.ishaproducts;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class shopDeleteProduct extends AppCompatActivity {
    ImageButton ANP ;
    Button del;


    SharedPreferences sp2;
    EditText PQ;


    AutoCompleteTextView searchProduct;


    TextView pName,pRate,pQnty,pTotal;


    List peerList=new ArrayList();
    String[] AllProducts2={"yy,T"};
    String TDate,ShopNam,item,price="0.00",amount2="0.00";
    String QuantityP="0",priceP="0",itemP="0",sItemP="0",amountP="0";
    String Quantity="1";

    String product,q,r,t,a;
    ProgressDialog progressDialog;


    int progress=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_delete_product);
        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        TDate =dateFormat.format(Calendar.getInstance().getTime());


        sp2 = getSharedPreferences("shoping", MODE_PRIVATE);
        final String SName = sp2.getString("SName", "");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ShopNam = SName;
        getSupportActionBar().setTitle(ShopNam);






        searchProduct=findViewById(R.id.searchProduct);
        DatabaseReference dr;
        dr= FirebaseDatabase.getInstance().getReference().child("products");
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    constructorProductDetails cProductDetails= dataSnapshot.getValue(constructorProductDetails.class);
                    String speer=cProductDetails.getProductName();

                    peerList.add(speer);

                }
                AllProducts2=(String[]) peerList.toArray(new String[peerList.size()]);

                searchP();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        PQ=findViewById(R.id.PQ);
        ANP=findViewById(R.id.ANP);








        ANP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //item=searchProduct.getText().toString();
                Quantity =PQ.getText().toString();


                pName=findViewById(R.id.NPName);
                pRate=findViewById(R.id.NPRate);
                pQnty=findViewById(R.id.addedProductsQnty);
                pTotal=findViewById(R.id.NPTotal);


                // if (PQ.length() > 0 && item.length()>0) {

                pName.setText(item);
                pRate.setText(price);
                pQnty.setText(Quantity);
                pTotal.setText(String.format("%.2f", Double.parseDouble(price) * Double.parseDouble(Quantity)));




                //}



                progressDialog = new ProgressDialog(shopDeleteProduct.this);
                progressDialog.setMessage("Saving Data");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                // progressDialog.setIndeterminate(false);
                progressDialog.setMax(100);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);




            }



        });

        del=findViewById(R.id.delete);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(shopDeleteProduct.this);
                alertDialogBuilder.setMessage("Are you sure want to DELETE "+pName.getText().toString());
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {




                                //  progressDialog.show();

                                deleting();
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



    private void searchP() {







        ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, AllProducts2);
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




    private void getPaid(){


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
                        amount2=ab2;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void deleting(){

        // product=pName.getText().toString();
        // t=pName.getText().toString();
        // q=pQnty.getText().toString();
        itemP=item;
        priceP=String.format("%.2f", Double.parseDouble(price) * Double.parseDouble(Quantity));
        QuantityP=Quantity;
        amountP=String.format("%.2f", Double.parseDouble(amount2) * Double.parseDouble(Quantity));
        sItemP=itemP;

        deleteFromAll();


    }



    private  void deleteFromAll(){



        //shop pending

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
        Query query1 = databaseReference1.child("shop").orderByChild("shopName").equalTo(bill.ShopNam);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {

                        // progressDialog.setMessage("Saving Data");
                        // progressDialog.setCanceledOnTouchOutside(false);
                        //progressDialog.show();

                        String sp2 = DataSnapshot.child("shopPending").getValue(String.class);
                        DataSnapshot.getRef().child("shopPending").setValue(String.format("%.2f", Double.parseDouble(sp2) - Double.parseDouble(priceP)))
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



        //shop price and paid

        final String[] ppPrice = {"0"};
        final String[] ppPaid = {"0"};
        final String[] ppBalance = {"0"};

        final DatabaseReference ref2;
        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        ref2 = database2.getReference().child("Reports").child("Daily Reports").child(bill.TDate).child("Shop Reports").child(bill.ShopNam).child("csr");
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
                csr.setPrice(String.format("%.2f",   Double.parseDouble(String.valueOf(ppPrice[0]))-Double.parseDouble(priceP)));
                csr.setBalance(String.format("%.2f",  Double.parseDouble(String.valueOf(ppBalance[0]))-Double.parseDouble(priceP)));
                csr.setPaid(ppPaid[0]);


                ref2.setValue(csr)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                // progressDialog.dismiss();
                                ProgressDialogProgress();
                            }
                        });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //stock

        if(itemP.equals("HM 525ml Dealer") || itemP.equals("HM 525ml sp")  || itemP.equals("HM 525ml sp2")){ sItemP="HM 525ml" ;
            updateStockList();}
        else if
        (itemP=="HM 500ml Dealer" || itemP=="HM 500ml sp" || itemP=="HM 500ml sp2" ){sItemP="HM 500ml";
            updateStockList();}

        else if
        (itemP.equals("Curd 500ml Dealer")
                        || itemP=="Curd 500ml sp2"
        ){sItemP="Curd 500ml";

            updateStockList();

        }
        else {sItemP=itemP;

            updateStockList();
        }



        //tp

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
                ref4.child("tp").setValue(String.format("%.2f",  Double.parseDouble(String.valueOf(tp[0]))-Double.parseDouble(priceP)))
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



        //CSPWA


        final String[] sale = {"0"};
        final String[] allPending = {"0"};
        final String[] wholesaleAmount = {"0"};
        final String[] collection = {"0"};

        final DatabaseReference ref5;
        FirebaseDatabase database5 = FirebaseDatabase.getInstance();
        ref5 = database5.getReference().child("Reports").child("Daily Reports").child(bill.TDate).child("CSPA");
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
                csp.sets(String.format("%.2f", Double.parseDouble(sale[0])-Double.parseDouble(priceP)));
                csp.setp(String.format("%.2f",   Double.parseDouble(allPending[0])-Double.parseDouble(priceP)));
                csp.seta(String.format("%.2f",  Double.parseDouble(wholesaleAmount[0])-Double.parseDouble(amountP)));
                csp.setc(collection[0]);



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




        //shopList

        final DatabaseReference ref6;
        final String[] r = {"0"};
        final String[] q = {"0"};
        final String[] i = {itemP};

        FirebaseDatabase database6=FirebaseDatabase.getInstance();
        ref6 =database6.getReference().child("Reports").child("Daily Reports").child(bill.TDate).child("Shop Reports").child(bill.ShopNam).child("pList");
        Query query6 = ref6.orderByChild("purchasedItem").equalTo(itemP);
        query6.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //for (DataSnapshot data : snapshot.getChildren()){
                if(snapshot.exists()){

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
                                -Double.parseDouble(priceP)))


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



        //fullList


        final DatabaseReference ref7;
        FirebaseDatabase database7=FirebaseDatabase.getInstance();
        ref7 =database7.getReference().child("Reports").child("Daily Reports").child(bill.TDate).child("pList");
        Query query7 = ref7.orderByChild("purchasedItem").equalTo(itemP);
        query7.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //for (DataSnapshot data : snapshot.getChildren()){
                if(snapshot.exists()){

                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {




                        String ab = DataSnapshot.child("purchasedQnty").getValue(String.class);

                        DataSnapshot.getRef().child("purchasedQnty").setValue(String.format("%.0f", Double.parseDouble(ab) - Double.parseDouble(QuantityP)))





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








    private void updateStockList(){

        final DatabaseReference currentStockRef = FirebaseDatabase.getInstance().getReference().child("Stock").child("currentStock").child("stock");


        Query query3 = currentStockRef.orderByChild("stockItem").equalTo(sItemP);
        query3.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                        String stockQnty = DataSnapshot.child("stockQnty").getValue(String.class);
                        stockQnty = String.format("%.0f", Double.parseDouble(stockQnty) + Double.parseDouble(QuantityP));
                        DataSnapshot.getRef().child("stockQnty").setValue(stockQnty)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        ProgressDialogProgress();
                                    }
                                });
                    }


                }

                if (!snapshot.exists()) {

                }


            }


            @Override
            public void  onCancelled(@NonNull DatabaseError error) {

            }
        });




    }




    public void ProgressDialogProgress() {
        // progress += 10;
        // if (progress == 70) {

        openShop();

        //}

    }














    private void openShop() {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
    }


}

