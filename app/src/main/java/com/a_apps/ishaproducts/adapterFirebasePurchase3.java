package com.a_apps.ishaproducts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class adapterFirebasePurchase3 extends FirebaseRecyclerAdapter<constructerShopReportPurchase, com.a_apps.ishaproducts.adapterFirebasePurchase3.purchaseViewholder> {

    public adapterFirebasePurchase3(
            @NonNull FirebaseRecyclerOptions<constructerShopReportPurchase> options
    ) {
        super(options);
    }

    @Override
    protected void
    onBindViewHolder(@NonNull com.a_apps.ishaproducts.adapterFirebasePurchase3.purchaseViewholder holder,
                     int position, @NonNull constructerShopReportPurchase csrp) {

        holder.PI.setText(csrp.getPurchasedItem());

        holder.PQ.setText(csrp.getPurchasedQnty());

        //  holder.PP.setText(csrp.getPurchasedPrice());
    }


    @NonNull
    @Override
    public com.a_apps.ishaproducts.adapterFirebasePurchase3.purchaseViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_purchase, parent, false);
        return new com.a_apps.ishaproducts.adapterFirebasePurchase3.purchaseViewholder(view);
    }

    class purchaseViewholder extends RecyclerView.ViewHolder {

        public TextView PI, PQ, PP;


        public purchaseViewholder(@NonNull View itemView) {
            super(itemView);


            PI = (TextView) itemView.findViewById(R.id.purchasedItem);

            PQ = (TextView) itemView.findViewById(R.id.purchasedQnty);

            PP = (TextView) itemView.findViewById(R.id.purchasedPrice);

        }
    }
}

