package com.a_apps.ishaproducts;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class adapterNewOrder extends ArrayAdapter<constructorNewBill> {
    static List<constructorNewBill> newBillList;
    static Context context;
    int i;

    static String STotal ;
    static Double Dtotal2;
    static String QTotal ;
    static Double Atotal2;
    String amount="0";
    String amount2="0";

    String QuantityP="0",priceP="0",itemP="0",sItemP,amountP="0";




    public adapterNewOrder(@NonNull Context context, int i, List<constructorNewBill> newBillList) {
        super(context, i,newBillList);
        this.context = context;
        this.i=i;
        this.newBillList=newBillList;
    }

    @Override
    public int getCount(){
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        constructorNewBill newBillClass=newBillList.get(position);
        LayoutInflater layoutInflater =(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view =layoutInflater.inflate(R.layout.list_new_bill,null);





        TextView NPQntyU = view.findViewById(R.id.addedProductsQnty);
        TextView NPName =view.findViewById(R.id.NPName);
        TextView NewPID =view.findViewById(R.id.NPI);
        TextView NPRate =view.findViewById(R.id.NPRate);
        TextView NPTotal =view.findViewById(R.id.NPTotal);


        ImageButton removeBtn = view.findViewById(R.id.removeBtn);


        String NPID= String.valueOf(position+1);

        NPQntyU.setText(newBillClass.getNSQnty());
        NPName.setText(newBillClass.getNSProduct());
        NPRate.setText(newBillClass.getNSPrice());

        Double DNPQ =  Double.parseDouble(newBillClass.getNSQnty());
        Double DNPR =  Double.parseDouble(newBillClass.getNSPrice());
        Double DNPT = DNPQ*DNPR;
        final String SNPT = String.valueOf(DNPT);

        NPTotal.setText(SNPT);
        NewPID.setText(NPID);



        totalingBill();


        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newOrder.progressDialogP.show();


                removeProduct(position);
            }


        });


        return view;
    }

    private void removeProduct(int position) {


        final constructorNewBill newBIllClass=newBillList.get(position);

        priceP=String.format("%.2f",Double.parseDouble(newBIllClass.getNSPrice())

                * Double.parseDouble(newBIllClass.getNSQnty()));
        amountP=String.format("%.2f",Double.parseDouble(newBIllClass.getNSAmount())

                * Double.parseDouble(newBIllClass.getNSQnty()));
        QuantityP=newBIllClass.getNSQnty();
        itemP=newBIllClass.getNSProduct();
        sItemP=itemP;








        //shopList

        final DatabaseReference ref6;
        final String[] r = {"0"};
        final String[] q = {"0"};
        final String[] i = {itemP};

        FirebaseDatabase database6=FirebaseDatabase.getInstance();
        ref6 =database6.getReference().child("Orders").child("Daily Orders").child(newOrder.TDate).child("Shop Orders").child(newOrder.ShopNam).child("pList");
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


                                        newOrder.ProgressDialogProgressDelete();
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
        ref7 =database7.getReference().child("Orders").child("Daily Orders").child(newOrder.TDate).child("pList");
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


                                        newOrder.ProgressDialogProgressDelete();
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































        newBillList.remove(position);


        if (newBillList.isEmpty()){
            newOrder.TVTotal.setText("0.00");
        }
        else{
            totalingBill();

        }


        notifyDataSetChanged();

    }


    public static void totalingBill() {

        if (!newBillList.isEmpty()) {
            Double Dtotal = 0.0;
            Double Atotal = 0.0;
            for (int x = 0; x < newBillList.size(); x++) {
                Dtotal += Double.parseDouble(newBillList.get(x).getNSQnty()) * Double.parseDouble(newBillList.get(x).getNSPrice());

                // Atotal += Double.parseDouble(newBillList.get(x).getNSQnty())*Double.parseDouble(newBillList.get(x).getwp());

                Dtotal2 = Dtotal;


                if (newBillList.isEmpty()) {
                    STotal = "";
                    QTotal = "";
                }

            }

            //double MAT2 = MainActivity.doubleTotal;
            //Dtotal += MAT2;

            STotal = String.format("%.2f", Dtotal);
            newOrder.TVTotal.setText(STotal);

            // QTotal = String.format("%.2f", Atotal);
            // MainActivity.amnt.setText(QTotal);

            //Toast.makeText(context.getApplicationContext(),STotal , Toast.LENGTH_SHORT).show();
            String s2Total = String.format("%.2f", Dtotal);
        } else {
            STotal = "";


        }





    }


}

