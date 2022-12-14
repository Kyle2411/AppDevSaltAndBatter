package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.List;

public class viewOrdersListAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderFood> list;
    ImageView image;
    DatabaseHelper db;



    public viewOrdersListAdapter(Context mContext , List<OrderFood> data) {
        this.mContext = mContext;
        this.list = data;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = View.inflate(mContext, R.layout.view_all_orders_child ,null );
        TextView name = view.findViewById(R.id.textViewNameOrder);
        TextView cost = view.findViewById(R.id.textViewPrice);
        TextView quantity = view.findViewById(R.id.textViewQuantity);
        TextView profile = view.findViewById(R.id.textViewProfile);
        TextView seperator = view.findViewById(R.id.textViewSeperate);
        image = view.findViewById(R.id.imageViewOrder);
        db = new DatabaseHelper(view.getContext());




        if(position != 0) {
            if (list.get(position).getProfile_id() != list.get(position - 1).getProfile_id()) {

                seperator.setBackgroundResource(R.color.black);
            }
            else{
                seperator.setBackgroundResource(R.color.purple_200);
            }
        }


        name.setText(list.get(position).getName());
        cost.setText(list.get(position).getCost() + "$");
        quantity.setText(String.valueOf(list.get(position).getQuantity()));
        profile.setText(String.valueOf(db.getDataSpecific(list.get(position).getProfile_id()).getUsername()));


        Bitmap bmp = BitmapFactory.decodeByteArray(db.getDataFoodImage(list.get(position).getFood_id()), 0,db.getDataFoodImage(list.get(position).getFood_id()).length);
        image.setImageBitmap(bmp);




        return view;
    }


}
