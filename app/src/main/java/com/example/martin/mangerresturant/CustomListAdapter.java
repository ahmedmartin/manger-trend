package com.example.martin.mangerresturant;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> food_name;
    private final ArrayList<Double> price;

    public CustomListAdapter(Activity context, ArrayList<String> food_name, ArrayList<Double> price) {
        super(context, R.layout.food_list_show, food_name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.food_name=food_name;
        this.price=price;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.food_list_show, null,true);

        TextView food_name_show = (TextView) rowView.findViewById(R.id.food_name_show);
        TextView price_show = (TextView) rowView.findViewById(R.id.price_show);

        food_name_show.setText(food_name.get(position));
        price_show.setText(""+price.get(position));
        return rowView;


    }
}

