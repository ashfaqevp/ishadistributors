package com.a_apps.ishaproducts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    List<constructorShopDetails> list = new ArrayList<>();
    List<constructorShopDetails> shopList = new ArrayList<>();
    adapterShop shop_adapter;
    private onClickInterface onclickInterface;

    RecyclerView.Adapter adapter;

    View ChildView ;
    int RecyclerViewItemPosition ;
    ListView listView;

    List<constructorShopDetails> detailsList;
    adapterShopList shopListAdapter;

    SearchView searchView;
    Button reportMenuBtn;
    AutoCompleteTextView serchShop;


    List peerList=new ArrayList();

    String[] peerArray={"yy,T"};

    @Override
    public void onBackPressed(){
        finish();
        Intent intent=new Intent(this,house.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(Color.parseColor("#f56899"));

        report();


        recyclerView = findViewById(R.id.recycle);




        onclickInterface = new onClickInterface() {
            @Override
            public void setClick(int abc) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void shopName(String shopN) {
                Toast.makeText(getApplicationContext(),shopN,Toast.LENGTH_LONG).show();

                SharedPreferences sp;
                sp= getApplicationContext().getSharedPreferences("shoping" , MODE_PRIVATE);
                sp.edit().putString("SName",shopN).apply();
                openShop();

                adapter.notifyDataSetChanged();
            }
        };




        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(home.this));

        progressDialog = new ProgressDialog(home.this);

        progressDialog.setMessage("Loading Data");

        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();



        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("shop");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    constructorShopDetails shop_details = dataSnapshot.getValue(constructorShopDetails.class);
                    String speer=shop_details.getShopName();

                    peerList.add(speer);
                    list.add(shop_details);


                }
                peerArray =(String[]) peerList.toArray(new String[peerList.size()]);
                // Toast.makeText(getApplicationContext(), "peeer3  "+peerArray[3], Toast.LENGTH_LONG).show();


                serchShop = findViewById(R.id.searchShop);
                serchShop.setThreshold(1);
                ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(home.this, R.layout.support_simple_spinner_dropdown_item, peerArray);
                serchShop.setAdapter(adapterSearch);
                serchShop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String item = parent.getItemAtPosition(position).toString();
                        SharedPreferences sp;
                        sp=getSharedPreferences("shoping" , MODE_PRIVATE);
                        sp.edit().putString("SName",item).apply();
                        openShop();

                    }



                });

                adapter = new adapterShop (home.this,list,onclickInterface);
                recyclerView.setAdapter(adapter);

                searchView = findViewById(R.id.searchView);
                //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
                searchShop();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });


        FloatingActionButton add;
        add=findViewById(R.id.addBtnShop);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddShop();

            }
        });





        searchView = findViewById(R.id.searchView);




    }

    private void report() {
        reportMenuBtn = findViewById(R.id.viewReportBtn);
        reportMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReport();
            }
        });
    }

    private void searchShop() {


    }


    private void openAddShop() {
        Intent intent=new Intent(this,addNewShop.class);
        startActivity(intent);
    }

    private void openShop() {
        Intent intent=new Intent(this,shop.class);
        startActivity(intent);
    }
    private void openReport() {
        Intent intent=new Intent(this,reportMenu.class);
        startActivity(intent);
    }
}