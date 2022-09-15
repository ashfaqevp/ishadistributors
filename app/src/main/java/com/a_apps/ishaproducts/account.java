package com.a_apps.ishaproducts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class account extends AppCompatActivity {

    CardView logout;
    SharedPreferences sp;
    String a1,n,m;
    TextView name,mobile;

    Button L1,L2,L3;

    Boolean P1=false;
    Boolean P2=false;
    Boolean P3=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        getSupportActionBar().hide();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#FF033657"));


        logout = findViewById(R.id.logout);
        sp = getSharedPreferences("login" , MODE_PRIVATE);
        String a = sp.getString("name","");
        a1=a;
        name=findViewById(R.id.nam);
        mobile=findViewById(R.id.num);


        L1=findViewById(R.id.btnLSale);
        L2=findViewById(R.id.btnLPaid);
        L3=findViewById(R.id.btnLStock);

        L1.setVisibility(View.GONE);
        L2.setVisibility(View.GONE);
        L3.setVisibility(View.GONE);


        if(a1.equals("mufsil")||a1.equals("ashfaqe")){

            L1.setVisibility(View.VISIBLE);
            L2.setVisibility(View.VISIBLE);
            L3.setVisibility(View.VISIBLE);
        }






        L1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M1();
                modifyButtonP1();
            }
        });


        L2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M2();
                modifyButtonP2();
            }
        });



        checkPermission();

        modifyButtonP1();
        modifyButtonP2();









        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("accounts").orderByChild("name").equalTo(a1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                        m = DataSnapshot.child("mobile").getValue(String.class);
                    }}

                name.setText(a1);
                mobile.setText(m);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp.edit().putBoolean("logged" , false).apply();

                goLogin();
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




                    }
                    L1.setText("LOCK SALE EDIT");
                    P1=true;

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




                    }

                    L2.setText("LOCK PAID EDIT");
                    P2=true;


                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference();
        Query query3 = databaseReference3.child("Permissions").orderByChild("StockEdit").equalTo("true");
        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot DataSnapshot : snapshot.getChildren()) {



                    }
                    P3=true;

                    L3.setText("LOCK STOCK EDIT");
                    L3.setBackgroundColor(Color.parseColor("#FF033657"));
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }







    private void M1() {

        if(P1==true) {
            final DatabaseReference ref7;
            FirebaseDatabase database7 = FirebaseDatabase.getInstance();
            ref7 = database7.getReference().child("Permissions");
            Query query7 = ref7.orderByChild("edit").equalTo("Sale");
            query7.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //for (DataSnapshot data : snapshot.getChildren()){
                    if (snapshot.exists()) {

                        for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                            DataSnapshot.getRef().child("SaleEdit").setValue("false")

                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                        }

                    }
                    if (!snapshot.exists()) {

                        String UserId = ref7.push().getKey();
                        ref7.child(UserId).child("edit").setValue("Sale");
                        ref7.child(UserId).child("SaleEdit").setValue("false");

                    }
                    goAccount();

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


        else{
            final DatabaseReference ref7;
            FirebaseDatabase database7=FirebaseDatabase.getInstance();
            ref7 =database7.getReference().child("Permissions");
            Query query7 = ref7.orderByChild("edit").equalTo("Sale");
            query7.addListenerForSingleValueEvent (new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //for (DataSnapshot data : snapshot.getChildren()){
                    if(snapshot.exists()){

                        for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                            DataSnapshot.getRef().child("SaleEdit").setValue("true")

                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                        }

                    }   if(! snapshot.exists()){

                        String UserId = ref7.push().getKey();
                        ref7.child(UserId).child("edit").setValue("Sale");
                        ref7.child(UserId).child("SaleEdit").setValue("true");

                    }  goAccount();

                }


                @Override
                public void  onCancelled(@NonNull DatabaseError error) {

                }
            });




        }



    }










    private void M2() {


        if(P2==true) {
            final DatabaseReference ref7;
            FirebaseDatabase database7 = FirebaseDatabase.getInstance();
            ref7 = database7.getReference().child("Permissions");
            Query query7 = ref7.orderByChild("edit").equalTo("Paid");
            query7.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //for (DataSnapshot data : snapshot.getChildren()){
                    if (snapshot.exists()) {

                        for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                            DataSnapshot.getRef().child("PaidEdit").setValue("false")

                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                        }

                    }
                    if (!snapshot.exists()) {

                        String UserId = ref7.push().getKey();
                        ref7.child(UserId).child("edit").setValue("Paid");
                        ref7.child(UserId).child("PaidEdit").setValue("false");

                    }
                    goAccount();

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


        else {
            final DatabaseReference ref7;
            FirebaseDatabase database7 = FirebaseDatabase.getInstance();
            ref7 = database7.getReference().child("Permissions");
            Query query7 = ref7.orderByChild("edit").equalTo("Paid");
            query7.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //for (DataSnapshot data : snapshot.getChildren()){
                    if (snapshot.exists()) {

                        for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                            DataSnapshot.getRef().child("PaidEdit").setValue("true")

                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                        }

                    }
                    if (!snapshot.exists()) {

                        String UserId = ref7.push().getKey();
                        ref7.child(UserId).child("edit").setValue("Paid");
                        ref7.child(UserId).child("PaidEdit").setValue("true");


                    }
                    goAccount();

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }}







    private void modifyButtonP1() {

        if (P1 == true) {

            L1.setText("LOCK SALE EDIT");
            // L1.setBackgroundColor(Color.parseColor(" #B00020"));

        } else {

            L1.setText("UNLOCK SALE EDIT");
            // L1.setBackgroundColor(Color.parseColor(" #018786"));

        }

    }


    private void modifyButtonP2(){

        if(P2==true){

            L2.setText("LOCK PAID EDIT");
            // L2.setBackgroundColor(Color.parseColor(" #B00020"));

        }
        else{

            L2.setText("UNLOCK PAID EDIT");
            //  L2.setBackgroundColor(Color.parseColor(" #018786"));

        }

    }




    public void goLogin(){
        Intent i = new Intent(this,login.class);
        startActivity(i);

    }


    public void goAccount(){
        Intent i = new Intent(this,account.class);
        startActivity(i);

    }
}
