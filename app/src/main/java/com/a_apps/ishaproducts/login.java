package com.a_apps.ishaproducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    Button login;
    SharedPreferences sp;
    EditText acc,pass;
    String a1,a2,p1,p2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);


        login=findViewById(R.id.login);
        sp = getSharedPreferences("login" , MODE_PRIVATE);


        if(sp.getBoolean("logged",false)){
            goHome();

        }




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getdata();


                //Toast.makeText(getApplicationContext(), "your acoount    "+a1+"      "+p1, Toast.LENGTH_LONG).show();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query = databaseReference.child("accounts").orderByChild("name").equalTo(a1);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot DataSnapshot : snapshot.getChildren()) {


                                p2 = DataSnapshot.child("pass").getValue(String.class);
                                if(p1.equals(p2)){

                                    loginDone();

                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Invalid Account or Password", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        if (!snapshot.exists()) {

                            Toast.makeText(getApplicationContext(), "Incorrect Username", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Toast.makeText(getApplicationContext(), p1+"      "+p2, Toast.LENGTH_LONG).show();

            }
        });


    }

    private void getdata() {

        acc=findViewById(R.id.acc);
        pass=findViewById(R.id.pass);

        a1=acc.getText().toString();
        p1=pass.getText().toString();
    }

    private void loginDone(){


        sp = getSharedPreferences("login" , MODE_PRIVATE);





        goHome();
        sp.edit().putBoolean("logged" , true).apply();
        sp.edit().putString("name",a1).apply();

        goHome();



    }

    private void goHome() {
        Intent intent=new Intent(this,house.class);
        startActivity(intent);
    }
}
