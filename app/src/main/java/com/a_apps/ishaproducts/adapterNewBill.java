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

public class adapterNewBill extends ArrayAdapter<constructorNewBill> {
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




    public adapterNewBill(@NonNull Context context, int i, List<constructorNewBill> newBillList) {
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

                bill.progressDialogP.show();


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
                                        bill.ProgressDialogProgressDelete();
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
                                bill.ProgressDialogProgressDelete();
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


                                bill.ProgressDialogProgressDelete();
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


                                bill.ProgressDialogProgressDelete();
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


                                        bill.ProgressDialogProgressDelete();
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


                                        bill.ProgressDialogProgressDelete();
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
            bill.TVTotal.setText("0.00");
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
            bill.TVTotal.setText(STotal);

            // QTotal = String.format("%.2f", Atotal);
            // MainActivity.amnt.setText(QTotal);

            //Toast.makeText(context.getApplicationContext(),STotal , Toast.LENGTH_SHORT).show();
            String s2Total = String.format("%.2f", Dtotal);
        } else {
            STotal = "";


        }

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

                                        bill.ProgressDialogProgressDelete();
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


}

