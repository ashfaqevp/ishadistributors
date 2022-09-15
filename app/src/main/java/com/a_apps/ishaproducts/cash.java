package com.a_apps.ishaproducts;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class cash extends AppCompatActivity {

    adapterFirebasePurchase2 adapter,adapter2;
    RecyclerView listView,listView2;
    String TDate;

    EditText PQ;
    AutoCompleteTextView searchExpense;
    String NPName, expense;
    FloatingActionButton ANP;


    TextView title;

    Boolean itemSelected = false;
    String item;
    String expenseCash = "0.00";


    TextView DCash,DExpense,DBalance;
    String BCash="0.00",BExpense="0.00",BBalance="0.00";




    String[] AllExpense = {"Fuel","Service","Food","Others"};
    List peerList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);

        getSupportActionBar().hide();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#179cc7"));

        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        TDate = dateFormat.format(Calendar.getInstance().getTime());

        DCash=findViewById(R.id.collectionCash);
        DExpense=findViewById(R.id.expenseText);
        DBalance=findViewById(R.id.balanceCash);


        listView = findViewById(R.id.cashkList);
       // listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));

        listView2 = findViewById(R.id.expenseList);
        //listView2.setHasFixedSize(true);
        listView2.setLayoutManager(new LinearLayoutManager(this));

        cashList();
        expenseList();

        searchExpense = findViewById(R.id.expenseItem);
        PQ = findViewById(R.id.amountText);
        ANP = findViewById(R.id.addExpenseBtn);


        DCash.setText("0.00");
        DExpense.setText("0.00");
        DBalance.setText("0.00");

        Dashboard();

        expenseItemSelection();




        ANP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                expense = PQ.getText().toString();


                if (itemSelected == true && expense.length() > 0) {


                    addingExpense();


                } else {
                    Toast.makeText(getApplicationContext(), "Expense are Not Selected", Toast.LENGTH_SHORT).show();
                }


            }
        });









    }





    private void cashList() {





        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Reports").child("Daily Reports").child(TDate).child("Cash List");

        FirebaseRecyclerOptions<constructerShopReportPurchase> options2 = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();

        adapter = new adapterFirebasePurchase2(options2);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



    }

    private void expenseList() {





        DatabaseReference mbase3;
        mbase3 = FirebaseDatabase.getInstance().getReference().child("Reports").child("Daily Reports").child(TDate).child("Expense").child("Expense List");

        FirebaseRecyclerOptions<constructerShopReportPurchase> options2 = new FirebaseRecyclerOptions.Builder<constructerShopReportPurchase>()
                .setQuery(mbase3, constructerShopReportPurchase.class).build();

        adapter2 = new adapterFirebasePurchase2(options2);
        listView2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();



    }


    private void expenseItemSelection() {

        ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, AllExpense);
        searchExpense.setThreshold(0);
        searchExpense.setAdapter(adapterSearch);
        searchExpense.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                item = parent.getItemAtPosition(position).toString();
                // getProductPrice();
                itemSelected = true;

                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void addingExpense(){

        final String[] ppExpense = {"0"};

        final DatabaseReference ref1;
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        ref1 = database1.getReference().child("Reports").child("Daily Reports").child(TDate).child("Expense").child("Amount");
        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {

                    constructorCSP csp = snapshot.getValue(constructorCSP.class);

                    ppExpense[0] = csp.geta();




                }
                constructorCSP csp=new constructorCSP();
                csp.seta(String.format("%.2f", Double.parseDouble(expense) + Double.parseDouble(String.valueOf(ppExpense[0]))));
                ref1.setValue(csp)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Dashboard();

                            }
                        });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        final DatabaseReference ref2;
        FirebaseDatabase database7=FirebaseDatabase.getInstance();
        ref2 =database7.getReference().child("Reports").child("Daily Reports").child(TDate).child("Expense").child("Expense List");
        Query query7 = ref2.orderByChild("purchasedItem").equalTo(item);
        query7.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //for (DataSnapshot data : snapshot.getChildren()){
                if(snapshot.exists()){

                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {




                        String ab = DataSnapshot.child("purchasedQnty").getValue(String.class);


                        DataSnapshot.getRef().child("purchasedQnty").setValue(String.format("%.2f", Double.parseDouble(ab) + Double.parseDouble(expense)))


                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {



                                    }
                                });

                    }



                }   if(! snapshot.exists()){

                    constructerShopReportPurchase csrp = new constructerShopReportPurchase();
                    csrp.setPurchasedItem(item);

                    csrp.setPurchasedQnty(expense);


                    String UserId = ref2.push().getKey();
                    ref2.child(UserId).setValue(csrp)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                }

            }


            @Override
            public void  onCancelled(@NonNull DatabaseError error) {

            }
        });




        searchExpense.setText("");
        PQ.setText("");

    }

    private  void Dashboard(){



        final DatabaseReference ref3;
        FirebaseDatabase database3 = FirebaseDatabase.getInstance();
        ref3 = database3.getReference().child("Reports").child("Daily Reports").child(TDate).child("CSPA");
        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.exists()){

                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {

                        constructorCSP csp = snapshot.getValue(constructorCSP.class);
                        BCash= csp.getc();
                    }
                }


                DCash.setText(BCash);
                DExpense.setText("0.00");
                DBalance.setText(BCash);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        final DatabaseReference ref5;
        FirebaseDatabase database5 = FirebaseDatabase.getInstance();
        ref5 = database5.getReference().child("Reports").child("Daily Reports").child(TDate).child("Expense").child("Amount");
        ref5.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(!snapshot.exists()) {
                    ref5.child("a").setValue("0.00");
                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        final DatabaseReference ref4;
        FirebaseDatabase database4 = FirebaseDatabase.getInstance();
        ref4 = database4.getReference().child("Reports").child("Daily Reports").child(TDate).child("Expense").child("Amount");
        ref4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                        constructorCSP csp = snapshot.getValue(constructorCSP.class);
                        BExpense= csp.geta();




                    }DExpense.setText(BExpense);
                    DBalance.setText(String.format("%.2f", Double.parseDouble(BCash) - Double.parseDouble(BExpense)));
                }




            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();
        adapter2.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();


        adapter.stopListening();
        adapter2.stopListening();
    }


}
