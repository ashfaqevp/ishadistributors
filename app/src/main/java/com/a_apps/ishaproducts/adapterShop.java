package com.a_apps.ishaproducts;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class adapterShop extends RecyclerView.Adapter<adapterShop.ViewHolder> implements Filterable {

    Context context;
    List<constructorShopDetails> MainImageUploadInfoList;
    List<constructorShopDetails> shopListFull;
    String shname;
    onClickInterface onClickInterface;


    public adapterShop(Context context, List<constructorShopDetails> TempList,onClickInterface onClickInterface) {

        this.MainImageUploadInfoList = TempList;
        this.onClickInterface = onClickInterface;
        this.context = context;
        shopListFull=new ArrayList<>(MainImageUploadInfoList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_shop_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    public String shopn(int position ) {
        constructorShopDetails shop_details = MainImageUploadInfoList.get(position);

        String sn =shop_details.getShopName();
        return sn;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final constructorShopDetails shop_details = MainImageUploadInfoList.get(position);


        holder.NShopName.setText(shop_details.getShopName());
        holder.NShopPlace.setText(shop_details.getShopPlace());
        holder.NShopPending.setText(shop_details.getShopPending());
        shname = shop_details.getShopName();

        holder.sp.edit().putString("SName",shop_details.getShopName()).apply();

        final String SN2 = shop_details.getShopPending();


        holder.shopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                onClickInterface.shopName(shop_details.getShopName());
            }
        });

        holder.layoutShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                onClickInterface.shopName(shop_details.getShopName());
            }
        });

        holder.NShopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                onClickInterface.shopName(shop_details.getShopName());
            }
        });


        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        String TDate = dateFormat.format(Calendar.getInstance().getTime());

        holder.priceTrue.setVisibility(View.INVISIBLE);
        holder.paidTrue.setVisibility(View.INVISIBLE);
        holder.cprice="0";
        holder.cpaid="0";

        DatabaseReference ref3;
        FirebaseDatabase database2=FirebaseDatabase.getInstance();
        ref3 =database2.getReference().child("Reports").child("Daily Reports").child(TDate).child("Shop Reports").child(shop_details.getShopName()).child("csr");
        ref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot DataSnapshot : snapshot.getChildren()) {
                    constructerShopReport csr = snapshot.getValue(constructerShopReport.class);
                    holder.cprice= csr.getPrice();

                    holder.cpaid= csr.getPaid();





                }


                if(Double.parseDouble(holder.cprice)>1){
                    holder.priceTrue.setVisibility(View.VISIBLE);
                    holder.cprice="0";

                }
                // if(Double.parseDouble(holder.cprice)==0){
                // holder.priceTrue.setVisibility(View.INVISIBLE);
                // }
                else{

                    holder.priceTrue.setVisibility(View.INVISIBLE);

                }


                if(Double.parseDouble(holder.cpaid)>1){

                    holder.paidTrue.setVisibility(View.VISIBLE);
                }
                // if(Double.parseDouble(holder.cprice)==0){
                // holder.priceTrue.setVisibility(View.INVISIBLE);
                // }
                else{


                    holder.paidTrue.setVisibility(View.GONE);
                }




            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                holder.priceTrue.setVisibility(View.INVISIBLE);
                holder.paidTrue.setVisibility(View.INVISIBLE);
                holder.cprice="0";
                holder.cpaid="0";
            }
        });





    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }


    @Override
    public Filter getFilter() {
        return shopFilter;
    }
    private Filter shopFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<constructorShopDetails> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(shopListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (constructorShopDetails item : shopListFull) {
                    if (item.getShopName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            MainImageUploadInfoList.clear();
            MainImageUploadInfoList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };




    class ViewHolder extends RecyclerView.ViewHolder {



        public TextView NShopName;
        public TextView NShopPlace;
        public TextView NShopPending;
        public CardView shopCard;
        public ConstraintLayout layoutShop;
        public SharedPreferences sp;
        public ImageView priceTrue;
        public ImageView paidTrue;
        public String cprice="0",cpaid="0";

        public ViewHolder(View itemView) {

            super(itemView);

            priceTrue=itemView.findViewById(R.id.priceTrue);
            paidTrue=itemView.findViewById(R.id.paidTrue);

            NShopName = (TextView) itemView.findViewById(R.id.NShopName);

            NShopPlace= (TextView) itemView.findViewById(R.id.NShopPlace);

            NShopPending= (TextView) itemView.findViewById(R.id.NShopPending);

            shopCard = (CardView) itemView.findViewById(R.id.shopCard);

            layoutShop =(ConstraintLayout) itemView.findViewById(R.id.layoutShop);

            sp= context.getSharedPreferences("carding" , MODE_PRIVATE);
            sp.edit().putString("shop","null").apply();








        }
    }


}




