package com.a_apps.ishaproducts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class adapterStockListView extends FirebaseRecyclerAdapter<constructorViewStockProduct,com.a_apps.ishaproducts.adapterStockListView.stockViewholder> {

    public adapterStockListView(
            @NonNull FirebaseRecyclerOptions<constructorViewStockProduct> options
    ) {
        super(options);
    }

    @Override
    protected void
    onBindViewHolder(@NonNull com.a_apps.ishaproducts.adapterStockListView.stockViewholder holder,
                     int position, @NonNull constructorViewStockProduct csvp) {

        holder.PI.setText(csvp.getSVItem());

        holder.PQ.setText(csvp.getSVQnty());

        //  holder.PP.setText(csrp.getPurchasedPrice());
    }


    @NonNull
    @Override
    public com.a_apps.ishaproducts.adapterStockListView.stockViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_stock, parent, false);
        return new com.a_apps.ishaproducts.adapterStockListView.stockViewholder(view);
    }















    class stockViewholder extends RecyclerView.ViewHolder {

        public TextView PI, PQ, PP;


        public stockViewholder(@NonNull View itemView) {
            super(itemView);


            PI = (TextView) itemView.findViewById(R.id.stockItem);

            PQ = (TextView) itemView.findViewById(R.id.stockQnty);

            // PP = (TextView) itemView.findViewById(R.id.purchasedPrice);






        }
    }
}
