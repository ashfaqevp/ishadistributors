package com.a_apps.ishaproducts;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class adapterShopList extends ArrayAdapter<constructorShopDetails> {

    Context context;
    List<constructorShopDetails> shopList;
    int i,shop_items;
    ValueEventListener valueEventListener;

    public adapterShopList(Context context, int i, List<constructorShopDetails> shopList) {
        super(context, i, shopList);
        this.context = context;
        this.i = i;
        this.shopList = shopList;
    }



   /* public shopListAdapter(ValueEventListener valueEventListener, int shop_items, List<shop_details> shopList) {
       super((Context) valueEventListener,shop_items,shopList);
    this.valueEventListener = valueEventListener;
        this.shop_items = shop_items;
        this.shopList = shopList;
    }
    */

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        constructorShopDetails shopListClass = shopList.get(position);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.list_shop_items, null);



        TextView NShopName;
        TextView NShopPlace;
        TextView NShopPending;
        CardView shopCard;
        SharedPreferences sp;


        NShopName = (TextView) view.findViewById(R.id.NShopName);

        NShopPlace= (TextView) view.findViewById(R.id.NShopPlace);

        NShopPending= (TextView) view.findViewById(R.id.NShopPending);

        shopCard = view.findViewById(R.id.shopCard);

        sp= context.getSharedPreferences("carding" , MODE_PRIVATE);
        sp.edit().putString("shop","null").apply();


        final String gsp=shopListClass.getShopPlace();
        NShopName.setText(shopListClass.getShopName());
        NShopPlace.setText(gsp);
        NShopPending.setText(shopListClass.getShopPending());
        final String shopName=shopListClass.getShopName();


        shopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(),gsp, Toast.LENGTH_LONG).show();
                Toast.makeText(context.getApplicationContext(),"sh", Toast.LENGTH_SHORT).show();

            }
        });








        return view;

    }
}
