package com.a_apps.ishaproducts;


import android.content.Context;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class adapterProduct extends RecyclerView.Adapter<adapterProduct.ViewHolder>   {

    Context context;
    List<constructorProductDetails> MainImageUploadInfoList;
    List<constructorProductDetails> pListFull;
    String shname;
    onClickInterface onClickInterface;


    public adapterProduct(Context context, List<constructorProductDetails> TempList, onClickInterface onClickInterface) {

        this.MainImageUploadInfoList = TempList;
        this.onClickInterface = onClickInterface;
        this.context = context;
        pListFull=new ArrayList<>(MainImageUploadInfoList);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    public String shopn(int position ) {
        constructorProductDetails cProductDetails = MainImageUploadInfoList.get(position);

        String sn =cProductDetails.getProductName();
        return sn;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final constructorProductDetails cProductDetails = MainImageUploadInfoList.get(position);


        holder.pName.setText(cProductDetails.getProductName());



        holder.pPrice.setText(cProductDetails.getProductPrice());

        shname = cProductDetails.getProductName();

        holder.sp.edit().putString("PName",cProductDetails.getProductName()).apply();

        final String SN2 = cProductDetails.getProductPrice();


        holder.productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                onClickInterface.shopName(cProductDetails.getProductName());
            }
        });

        holder.layoutP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                onClickInterface.shopName(cProductDetails.getProductName());
            }
        });




        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        String TDate = dateFormat.format(Calendar.getInstance().getTime());





    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }







    class ViewHolder extends RecyclerView.ViewHolder {



        public TextView pName;

        public TextView pPrice;
        public CardView productCard;
        public ConstraintLayout layoutP;
        public SharedPreferences sp;


        public ViewHolder(View itemView) {

            super(itemView);



            pName = (TextView) itemView.findViewById(R.id.pName);


            pPrice= (TextView) itemView.findViewById(R.id.pPrice);

            productCard= (CardView) itemView.findViewById(R.id.pCard);

            layoutP =(ConstraintLayout) itemView.findViewById(R.id.layoutP);

            sp= context.getSharedPreferences("carding" , MODE_PRIVATE);
            sp.edit().putString("products","null").apply();








        }
    }


}
