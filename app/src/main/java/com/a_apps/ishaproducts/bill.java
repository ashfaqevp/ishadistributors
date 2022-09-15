package com.a_apps.ishaproducts;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.a_apps.ishaproducts.shop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class bill extends AppCompatActivity {


    @Override
    public void onBackPressed(){

        if (adapterNewBill.newBillList.isEmpty()){
            finish();
            Intent intent=new Intent(this, shop.class);
            startActivity(intent);
        }
        else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(bill.this);
            alertDialogBuilder.setMessage("Delete All Purchased Product !");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {



                        }
                    });



            AlertDialog alertDialog=alertDialogBuilder.create();
            alertDialog.show();
        }

    }

    EditText PQ;
    AutoCompleteTextView searchProduct;
    String NPName, NPqnty;
    ImageButton ANP;
    static TextView TVTotal;

    TextView title;
    FloatingActionButton saveBtn;
    ImageButton Nav;

    Button slctHM,slctHM525,slctCurd,slctPeda,slctPlusPP,slctLassi,slctSambaram,slctBread;

    String[] AllProducts = {"yy,Tt"};
    List peerList = new ArrayList();
    String[] AllPrice = {"yy,Tt"};
    List velaList = new ArrayList();
    String[] AllAmount = {"yy,Tt"};
    List amountList = new ArrayList();

    String item;
    String Quantity = "0";
    String rate = "0";
    String amounRrate="0";
    String amount = "0";
    String price = "0";


    ListView addedProducts;
    adapterNewBill adapterNewBill;
    adapterNewBill adapter;

    Boolean itemSelected = false;

    ProgressDialog progressDialog;
    ProgressDialog LoadingProduct;
    static ProgressDialog progressDialogP;

    SharedPreferences sp2;
    static String ShopNam = "Test2";

    static String TDate;


    List<constructorNewBill> newBillList;


    String sp1;
    String sItem;

    static int progress = 0;
    static int progressP = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        getSupportActionBar().hide();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#1a67d1"));

        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        TDate = dateFormat.format(Calendar.getInstance().getTime());

        sp2 = getSharedPreferences("shoping", MODE_PRIVATE);
        final String SName = sp2.getString("SName", "");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ShopNam = SName;
        getSupportActionBar().setTitle(ShopNam);


        searchProduct = findViewById(R.id.searchProduct);
        PQ = findViewById(R.id.PQ);
        ANP = findViewById(R.id.ANP);

        TVTotal = findViewById(R.id.NPTotal);


        newBillList = new ArrayList<>();
        addedProducts = findViewById(R.id.newProductList);
        adapter = new adapterNewBill(this, R.layout.list_added_products, newBillList);
        addedProducts.setAdapter(adapter);



        LoadingProduct = new ProgressDialog(bill.this);
        LoadingProduct.setMessage("Loading");
        LoadingProduct.setCanceledOnTouchOutside(false);
        LoadingProduct.show();



        progressDialog = new ProgressDialog(bill.this);
        progressDialog.setMessage("Saving Data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        progressDialogP = new ProgressDialog(bill.this);
        progressDialogP.setMessage("Deleting");
        progressDialogP.setCanceledOnTouchOutside(false);
        progressDialogP.setCancelable(false);
        progressDialogP.setMax(100);
        progressDialogP.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        title=findViewById(R.id.title);
        title.setText(ShopNam);



        getProductNameAndPriceFromDatabase();

        saveBtn=findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShop();
            }
        });

        Nav=findViewById(R.id.Nav);
        Nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (adapterNewBill.newBillList.isEmpty()){
                    openShop();
                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(bill.this);
                    alertDialogBuilder.setMessage("Delete All Purchased Product !");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {



                                }
                            });



                    AlertDialog alertDialog=alertDialogBuilder.create();
                    alertDialog.show();
                }

            }
        });



        easybar();
        ANP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NPName = searchProduct.getText().toString();
                NPqnty = PQ.getText().toString();


                if (itemSelected == true && NPqnty.length() > 0) {


                    checkDatabaseConnectionState();


                } else {
                    Toast.makeText(getApplicationContext(), "Product Name or Product Quantity are Not Selected", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    private void checkDatabaseConnectionState() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {


                    // item=item;
                    // rate=rate;
                    Quantity = PQ.getText().toString();
                    price = String.format("%.2f", Double.parseDouble(rate) * Double.parseDouble(Quantity));
                    amount = String.format("%.2f", Double.parseDouble(amounRrate) * Double.parseDouble(Quantity));

                    newBillList.add(new constructorNewBill(Quantity, item, rate, amounRrate));
                    searchProduct.setText("");
                    PQ.setText("");
                    adapter.notifyDataSetChanged();

                    progressDialog.show();

                    shopPending();
                    productPrice();
                    stock();
                    totalPending();
                    SaleAPendingWholesaleAmount();

                    shopPList();
                    fullPList();


                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();


                } else {

                    Toast.makeText(getApplicationContext(), "notConnected", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(bill.this);
                    alertDialogBuilder.setMessage("Network Not Connected");
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        DataSnapshot.getRef().child("shopPending").setValue(String.format("%.2f", Double.parseDouble(sp2) + Double.parseDouble(price)))
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
                csr.setPrice(String.format("%.2f", Double.parseDouble(price) + Double.parseDouble(String.valueOf(ppPrice[0]))));
                csr.setBalance(String.format("%.2f", Double.parseDouble(price) + Double.parseDouble(String.valueOf(ppBalance[0]))));
                csr.setPaid(ppPaid[0]);


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

    private void stock() {




        if(item.equals("HM 525ml Dealer") || item.equals("HM 525ml sp")  || item.equals("HM 525ml sp2")){ sItem="HM 525ml" ;
            updateStockList();}
        else if
        (item=="HM 500ml Dealer" || item=="HM 500ml sp" || item=="HM 500ml sp2" ){sItem="HM 500ml";
            updateStockList();}

        else if
        (item.equals("Curd 500ml Dealer")
                        || item=="Curd 500ml sp" || item=="Curd 500ml sp2"
        ){sItem="Curd 500ml";
            Toast.makeText(getApplicationContext(), sItem, Toast.LENGTH_SHORT).show();
            updateStockList();

        }
        else {sItem=item;
            Toast.makeText(getApplicationContext(), sItem+"   new", Toast.LENGTH_SHORT).show();
            updateStockList();
        }




    }

    private void updateStockList(){

        final DatabaseReference currentStockRef = FirebaseDatabase.getInstance().getReference().child("Stock").child("currentStock").child("stock");


        Query query3 = currentStockRef.orderByChild("stockItem").equalTo(sItem);
        query3.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                        String stockQnty = DataSnapshot.child("stockQnty").getValue(String.class);
                        stockQnty = String.format("%.0f", Double.parseDouble(stockQnty) - Double.parseDouble(Quantity));
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


                    String key = currentStockRef.push().getKey();
                    String stockProduct = sItem;
                    String stockQuantity=String.format("%.0f", Double.parseDouble("0") - Double.parseDouble(Quantity));

                    currentStockRef.child(key).child("stockItem").setValue(stockProduct)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    ProgressDialogProgress();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                    currentStockRef.child(key).child("stockQnty").setValue(stockQuantity)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {




                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(bill.this);
                    alertDialogBuilder.setMessage("Not In Stock");
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


            @Override
            public void  onCancelled(@NonNull DatabaseError error) {

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
                ref4.child("tp").setValue(String.format("%.2f", Double.parseDouble(price) + Double.parseDouble(String.valueOf(tp[0]))))
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
                csp.sets(String.format("%.2f", Double.parseDouble(price) + Double.parseDouble(sale[0])));
                csp.setp(String.format("%.2f", Double.parseDouble(price) + Double.parseDouble(allPending[0])));
                csp.seta(String.format("%.2f", Double.parseDouble(amount) + Double.parseDouble(wholesaleAmount[0])));
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


    }



    private void shopPList() {


        final DatabaseReference ref6;
        final String[] r = {"0"};
        final String[] q = {"0"};
        final String[] i = {item};

        FirebaseDatabase database6=FirebaseDatabase.getInstance();
        ref6 =database6.getReference().child("Reports").child("Daily Reports").child(TDate).child("Shop Reports").child(ShopNam).child("pList");
        Query query6 = ref6.orderByChild("purchasedItem").equalTo(item);
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

                        DataSnapshot.getRef().child("purchasedQnty").setValue(String.format("%.0f", Double.parseDouble(ab) + Double.parseDouble(Quantity)));
                        DataSnapshot.getRef().child("purchasedPrice").setValue(String.format("%.2f", Double.parseDouble(ab2)
                                +Double.parseDouble(price)))


                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                        ProgressDialogProgress();
                                    }
                                });






                    }





                }   if(! snapshot.exists()){

                    constructerShopReportPurchase csrp = new constructerShopReportPurchase();
                    csrp.setPurchasedItem(item);
                    csrp.setPurchasedPrice(price);
                    csrp.setPurchasedQnty(Quantity);


                    String UserId = ref6.push().getKey();
                    ref6.child(UserId).setValue(csrp)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    ProgressDialogProgress();
                                }
                            });
                }

            }


            @Override
            public void  onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void fullPList() {





        final String[] r = {"0"};
        final String[] q = {"0"};
        final String[] i = {item};

        final DatabaseReference ref7;
        FirebaseDatabase database7=FirebaseDatabase.getInstance();
        ref7 =database7.getReference().child("Reports").child("Daily Reports").child(TDate).child("pList");
        Query query7 = ref7.orderByChild("purchasedItem").equalTo(item);
        query7.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //for (DataSnapshot data : snapshot.getChildren()){
                if(snapshot.exists()){

                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {




                        String ab = DataSnapshot.child("purchasedQnty").getValue(String.class);
                        String ab2 = DataSnapshot.child("purchasedPrice").getValue(String.class);

                        DataSnapshot.getRef().child("purchasedQnty").setValue(String.format("%.0f", Double.parseDouble(ab) + Double.parseDouble(Quantity)))





                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                        ProgressDialogProgress();
                                    }
                                });




                    }





                }   if(! snapshot.exists()){

                    constructerShopReportPurchase csrp = new constructerShopReportPurchase();
                    csrp.setPurchasedItem(item);

                    csrp.setPurchasedQnty(Quantity);


                    String UserId = ref7.push().getKey();
                    ref7.child(UserId).setValue(csrp)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    ProgressDialogProgress();
                                }
                            });
                }

            }


            @Override
            public void  onCancelled(@NonNull DatabaseError error) {

            }
        });


    }






    //
    //
    //
    //
    //



    private void getProductNameAndPriceFromDatabase() {

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
                AllProducts = (String[]) peerList.toArray(new String[peerList.size()]);

                viewProductNameFromDatabase();
                LoadingProduct.dismiss();


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference dr2;
        dr2 = FirebaseDatabase.getInstance().getReference().child("products");
        dr2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    constructorProductDetails cProductDetails = dataSnapshot.getValue(constructorProductDetails.class);
                    String svela = cProductDetails.getProductPrice();

                    velaList.add(svela);

                }

                AllPrice = (String[]) velaList.toArray(new String[velaList.size()]);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });





        DatabaseReference dr3;

        dr3= FirebaseDatabase.getInstance().getReference().child("products");
        dr3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    constructorProductDetails cProductDetails= dataSnapshot.getValue(constructorProductDetails.class);
                    String svela=cProductDetails.getProductWholesale();

                    amountList.add(svela);

                }

                AllAmount=(String[]) amountList.toArray(new String[amountList.size()]);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {



            }
        });


    }


    private void viewProductNameFromDatabase() {

        ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, AllProducts);
        searchProduct.setThreshold(1);
        searchProduct.setAdapter(adapterSearch);
        searchProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                item = parent.getItemAtPosition(position).toString();
                getProductPrice();
                itemSelected = true;

                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void getProductPrice() {


        int len = AllProducts.length;
        int i = 0;
        int j = 0;
        while (i < len) {
            if (AllProducts[i] == item) {
                j = i;
                break;
            } else {
                i = i + 1;
            }

        }
        rate = AllPrice[j];

        amounRrate = AllAmount[j];

    }




    private void easybar() {



        slctHM525=findViewById(R.id.slctHm525);
        slctHM525.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSelected=true;

                searchProduct.setText("HM 525ml");

                item="HM 525ml";
                addingPriceFromQuery();
            }
        });

        slctHM=findViewById(R.id.slctHm);
        slctHM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSelected=true;

                searchProduct.setText("HM 500ml");

                item="HM 500ml";
                addingPriceFromQuery();
            }
        });



        slctCurd=findViewById(R.id.slctCurd);
        slctCurd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSelected=true;

                searchProduct.setText("Curd 525ml");

                item="Curd 525ml";

                addingPriceFromQuery();



            }
        });







        slctPeda=findViewById(R.id.slctPeda);
        slctPeda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSelected=true;

                searchProduct.setText("Peda");

                item="Peda";

                addingPriceFromQuery();





            }
        });




        slctPlusPP=findViewById(R.id.slctPlus);
        slctPlusPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSelected=true;

                searchProduct.setText("Plus PP");

                item="Plus PP";
                addingPriceFromQuery();
            }
        });




        slctLassi=findViewById(R.id.slctLassi);
        slctLassi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSelected=true;

                searchProduct.setText("Lassi");

                item="Lassi";
                addingPriceFromQuery();
            }
        });




        slctSambaram=findViewById(R.id.slctSambaram);
        slctSambaram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSelected=true;

                searchProduct.setText("Sambaram");

                item="Sambaram";
                addingPriceFromQuery();
            }
        });




        slctBread=findViewById(R.id.slctBread);
        slctBread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemSelected=true;

                searchProduct.setText("Bread");

                item="Bread";
                addingPriceFromQuery();
            }
        });


    }


    private void addingPriceFromQuery(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("products").orderByChild("productName").equalTo(item);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {



                        String ab = DataSnapshot.child("productPrice").getValue(String.class);
                        String ab2 = DataSnapshot.child("productWholesale").getValue(String.class);
                        rate = ab;
                        amounRrate=ab2;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }







    private void openShop() {
        finish();
        Intent intent=new Intent(this,shop.class);
        startActivity(intent);

    }


    public void ProgressDialogProgress() {
        progress += 10;
        if (progress == 70) {
            progressDialog.dismiss();
            progress = 0;
        }

    }

    static public void ProgressDialogProgressDelete() {
        progressP += 10;
        if (progressP == 70) {
            progressDialogP.dismiss();
            progressP = 0;
        }

    }




}