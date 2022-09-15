package com.a_apps.ishaproducts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.a_apps.ishaproducts.addNewStock;
import com.a_apps.ishaproducts.constructorNewStockProduct;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class adapterNewStock extends ArrayAdapter<constructorNewStockProduct> {
    static List<constructorNewStockProduct> newStockList;
    static Context context;
    int i;



    public adapterNewStock(@NonNull Context context, int i, List<constructorNewStockProduct> newStockList) {
        super(context, i,newStockList);
        this.context = context;
        this.i=i;
        this.newStockList=newStockList;
    }

    @Override
    public int getCount(){
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        constructorNewStockProduct newStockClass=newStockList.get(position);
        LayoutInflater layoutInflater =(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view =layoutInflater.inflate(R.layout.list_new_stock,null);





        TextView NPQntyU = view.findViewById(R.id.addedProductsQnty);
        TextView NPName =view.findViewById(R.id.NPName);
        TextView NewPID =view.findViewById(R.id.NPI);


        ImageButton removeBtn = view.findViewById(R.id.removeBtn);


        String NPID= String.valueOf(position+1);

        NPQntyU.setText(newStockClass.getNSQnty());
        NPName.setText(newStockClass.getNSProduct());
        NewPID.setText(NPID);


        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                removeProduct(position);
            }


        });




        return view;
    }

    private void removeProduct(int position) {

        final constructorNewStockProduct newStockClass=newStockList.get(position);

        newStockList.remove(position);
        addNewStock.progressDialog.show();
        notifyDataSetChanged();
        Toast.makeText(context.getApplicationContext(),newStockClass.getNSProduct()+" REMOVED", Toast.LENGTH_SHORT).show();



        final DatabaseReference currentStockRef = FirebaseDatabase.getInstance().getReference().child("Stock").child("currentStock").child("stock");
        Query query =currentStockRef.orderByChild("stockItem").equalTo(newStockClass.getNSProduct());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {

                    String stockQnty = DataSnapshot.child("stockQnty").getValue(String.class);
                    String Quantity=newStockClass.NSQnty;





                    stockQnty=String.format("%.0f", Double.parseDouble(stockQnty) - Double.parseDouble(Quantity));
                    DataSnapshot.getRef().child("stockQnty").setValue(stockQnty).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    // ...
                                    // progressDialog.setVisibility(View.GONE);
                                    addNewStock.progress+=10;
                                    addNewStock.ProgressDialogProgress();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    // ...
                                    //progressDialog.setVisibility(View.GONE);
                                    //add_new_stock.progressDialog.dismiss();
                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context.getApplicationContext(),"not successfully removed", Toast.LENGTH_SHORT).show();

            }
        });





        final DatabaseReference newStockRef = FirebaseDatabase.getInstance().getReference().child("Stock").child("DateVise")
                .child(addNewStock.TDate).child("stock");
        Query query2 =newStockRef.orderByChild("stockItem").equalTo(newStockClass.getNSProduct());
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {

                    String stockQnty = DataSnapshot.child("stockQnty").getValue(String.class);
                    String Quantity=newStockClass.NSQnty;



                    addNewStock.progressDialog.show();

                    stockQnty=String.format("%.0f", Double.parseDouble(stockQnty) - Double.parseDouble(Quantity));
                    DataSnapshot.getRef().child("stockQnty").setValue(stockQnty).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    addNewStock.progress+=10;
                                    addNewStock.ProgressDialogProgress();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context.getApplicationContext(),"not successfully removed", Toast.LENGTH_SHORT).show();

            }
        });

    }



}
