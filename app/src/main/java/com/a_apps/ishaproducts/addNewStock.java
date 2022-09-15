package com.a_apps.ishaproducts;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import static com.a_apps.ishaproducts.adapterNewStock.newStockList;

public class addNewStock extends AppCompatActivity {



    static String TDate;

    static ProgressDialog progressDialog;
    ImageButton ANS;
    EditText PQ;
    AutoCompleteTextView searchProduct;
    String NSName,NSqnty;
    String[] AllProducts={"yy,Tt"};
    List peerList=new ArrayList();
    String item;
    ListView addedProducts;
    Boolean itemSelected=false;
    Boolean updatedToDatabase=false;
    adapterNewStock adapterNewStock;
    String sitem;
    Boolean b1=false,b2=false;

    String Quantity;
    FloatingActionButton save;

    static int progress=0;



    @Override
    public void onBackPressed() {

        if (adapterNewStock.newStockList.isEmpty()) {
            finish();
            Intent intent = new Intent(this, shop.class);
            startActivity(intent);
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(addNewStock.this);
            alertDialogBuilder.setMessage("Delete All Stock Product !");
            alertDialogBuilder.setPositiveButton("OK",
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_stock);




        getSupportActionBar().hide();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#370774"));

        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        TDate = dateFormat.format(Calendar.getInstance().getTime());

        ActionBar aBar;
        aBar= getSupportActionBar();
        ColorDrawable cd = new ColorDrawable(Color.parseColor("#370774"));
        aBar.setBackgroundDrawable(cd);

        searchProduct=findViewById(R.id.searchProduct);
        PQ=findViewById(R.id.PQ);
        ANS=findViewById(R.id.ANS);

        save=findViewById(R.id.saveButton);

        newStockList = new ArrayList<>();
        addedProducts = findViewById(R.id.newProductList);
        adapterNewStock = new adapterNewStock(this, R.layout.list_product, newStockList);
        addedProducts.setAdapter(adapterNewStock);

        progressDialog = new ProgressDialog(addNewStock.this);

        getProductNameFromDatabase();



        ANS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProductNameFromDatabase();

                NSName=searchProduct.getText().toString();
                NSqnty=PQ.getText().toString();
                Quantity = PQ.getText().toString();

                updatedToDatabase=false;

                if (itemSelected==true && NSqnty.length() > 0) {

                    checkDatabaseConnectionState();
                    updateLocalList();







                } else {
                    Toast.makeText(getApplicationContext(), "Product Name or Product Quantity are Not Selected", Toast.LENGTH_SHORT).show();
                }
            }


            private void checkDatabaseConnectionState() {
                DatabaseReference connectedRef= FirebaseDatabase.getInstance().getReference(".info/connected");
                connectedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean connected =snapshot.getValue(Boolean.class);
                        if (connected){
                            progressDialog.setMessage("Saving Data");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setCancelable(false);
                            progressDialog.show();


                            if(item.equals("HM 525ml Dealer") || item.equals("HM 525ml sp")){ sitem="HM 525ml";
                                updateStockList();
                                updateTodayStockList();}

                            else if
                            (item.equals("Curd 500ml Dealer")

                            ){sitem="Curd 500ml";
                                Toast.makeText(getApplicationContext(), sitem, Toast.LENGTH_SHORT).show();
                                updateStockList();
                                updateTodayStockList();
                                Toast.makeText(getApplicationContext(), sitem, Toast.LENGTH_SHORT).show();
                            }
                            else {sitem=item;
                                Toast.makeText(getApplicationContext(), sitem+"   new", Toast.LENGTH_SHORT).show();
                                updateStockList();
                                updateTodayStockList();
                            }




                        }

                        else{

                            Toast.makeText(getApplicationContext(), "notConnected", Toast.LENGTH_SHORT).show();

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(addNewStock.this);
                            alertDialogBuilder.setMessage("Network Not Connected");
                            alertDialogBuilder.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                        }
                                    });

                            AlertDialog alertDialog=alertDialogBuilder.create();
                            alertDialog.show();
                           /* progressDialog = new ProgressDialog(add_new_stock.this);
                            progressDialog.setMessage("Loading Data");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();

                            */


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





            }


        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNullStock();
                openStockMain();
            }
        });



    }




    private void getProductNameFromDatabase() {

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
                AllProducts=(String[]) peerList.toArray(new String[peerList.size()]);

                viewProductNameFromDatabase();

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
                itemSelected=true;

                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();



            }
        });

    }



    private void updateStockList() {
        final DatabaseReference currentStockRef = FirebaseDatabase.getInstance().getReference().child("Stock").child("currentStock").child("stock");



        Query query1 = currentStockRef.orderByChild("stockItem").equalTo(sitem);
        query1.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                        String stockQnty = DataSnapshot.child("stockQnty").getValue(String.class);




                        stockQnty=String.format("%.0f", Double.parseDouble(stockQnty) + Double.parseDouble(Quantity));
                        DataSnapshot.getRef().child("stockQnty").setValue(stockQnty).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Write was successful!
                                        // ...
                                        // progressDialog.setVisibility(View.GONE);

                                        progress+=10;
                                        ProgressDialogProgress();
                                        //progressDialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Write failed
                                        // ...
                                        //progressDialog.setVisibility(View.GONE);
                                        //progressDialog.dismiss();
                                    }
                                });
                        b1=true;
                        b2=true;

                    }


                }   if(! snapshot.exists()){

                    String key = currentStockRef.push().getKey();
                    String stockProduct = sitem;

                    //progressDialog = new ProgressDialog(add_new_stock.this);
                    // progressDialog.setMessage("Saving Data");
                    //progressDialog.setCanceledOnTouchOutside(false);

                    // progressDialog.show();
                    currentStockRef.child(key).child("stockItem").setValue(stockProduct)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    // ...
                                    // progressDialog.setVisibility(View.GONE);
                                    //progressDialog.dismiss();
                                    progress+=5;
                                    ProgressDialogProgress();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    // ...
                                    //progressDialog.setVisibility(View.GONE);
                                    // progressDialog.dismiss();
                                }
                            });

                    //progressDialog.show();
                    currentStockRef.child(key).child("stockQnty").setValue(Quantity)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    // ...
                                    //
                                    // progressDialog.dismiss();
                                    //progressDialog.setVisibility(View.GONE);

                                    progress+=5;
                                    ProgressDialogProgress();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    // ...
                                    // progressDialog.dismiss();
                                    //progressDialog.setVisibility(View.GONE);
                                }
                            });
                    b1=true;
                    b2=true;

                }

                // updateLocalList();
            }


            @Override
            public void  onCancelled(@NonNull DatabaseError error) {

            }
        });

    }





    private void updateTodayStockList() {
        final DatabaseReference currentStockRef = FirebaseDatabase.getInstance().getReference().child("Stock").child("DateVise")
                .child(TDate).child("stock");


        Query query1 = currentStockRef.orderByChild("stockItem").equalTo(sitem);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                        String stockQnty = DataSnapshot.child("stockQnty").getValue(String.class);


                        stockQnty = String.format("%.0f", Double.parseDouble(stockQnty) + Double.parseDouble(Quantity));
                        DataSnapshot.getRef().child("stockQnty").setValue(stockQnty).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Write was successful!
                                        // ...
                                        // progressDialog.setVisibility(View.GONE);
                                        progress+=10;
                                        ProgressDialogProgress();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Write failed
                                        // ...
                                        //progressDialog.setVisibility(View.GONE);

                                    }
                                });
                        b1 = true;
                        b2 = true;

                    }


                }
                if (!snapshot.exists()) {

                    String key = currentStockRef.push().getKey();
                    String stockProduct = sitem;

                    //progressDialog = new ProgressDialog(add_new_stock.this);


                    // progressDialog.show();
                    currentStockRef.child(key).child("stockItem").setValue(stockProduct)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    // ...
                                    // progressDialog.setVisibility(View.GONE);
                                    progress+=5;
                                    ProgressDialogProgress();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    // ...
                                    //progressDialog.setVisibility(View.GONE);

                                }
                            });

                    // progressDialog.show();
                    currentStockRef.child(key).child("stockQnty").setValue(Quantity)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    // ...
                                    progress+=5;
                                    ProgressDialogProgress();
                                    //progressDialog.setVisibility(View.GONE);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    // ...

                                    //progressDialog.setVisibility(View.GONE);
                                }
                            });
                    b1 = true;
                    b2 = true;

                }

                // updateLocalList();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    private void deleteNullStock(){




        final DatabaseReference ref7;
        final String[] r = {"0"};
        final String[] q = {"0"};
        final String[] i = {item};

        FirebaseDatabase database7=FirebaseDatabase.getInstance();
        ref7 =database7.getReference().child("Stock").child("currentStock").child("stock");
        Query query7 = ref7.orderByChild("stockQnty").equalTo("0");
        query7.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //for (DataSnapshot data : snapshot.getChildren()){
                if(snapshot.exists()){

                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {




                        DataSnapshot.getRef().removeValue();




                    }





                }   if(! snapshot.exists()){

                }

            }


            @Override
            public void  onCancelled(@NonNull DatabaseError error) {

            }
        });

    }














    private void updateLocalList() {
        newStockList.add(new constructorNewStockProduct(PQ.getText().toString(), item));
        searchProduct.setText("");
        PQ.setText("");
        itemSelected=false;

        adapterNewStock.notifyDataSetChanged();

    }



    private void openStockMain() {
        finish();
        Intent intent=new Intent(this,stock.class);
        startActivity(intent);

    }


    public static void ProgressDialogProgress() {
        // progress += 10;
        if (progress == 20) {
            progressDialog.dismiss();
            progress = 0;
        }

    }






}
