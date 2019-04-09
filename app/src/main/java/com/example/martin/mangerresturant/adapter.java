package com.example.martin.mangerresturant;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> food_name;
    private final ArrayList<Double> price;
    private final ArrayList<Integer> count;

    public adapter(Activity context, ArrayList<String> food_name, ArrayList<Double> price , ArrayList<Integer> count) {
        super(context, R.layout.check_show, food_name);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.food_name=food_name;
        this.price=price;
        this.count=count;
    }

    public View getView(int position, View view, ViewGroup parent) {

       // LayoutInflater inflater=context.getLayoutInflater();
        if(view==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.check_show, null);
        }
        TextView food_name_show = (TextView) view.findViewById(R.id.food);
        TextView price_show = (TextView) view.findViewById(R.id.price);
        TextView counts = (TextView)view.findViewById(R.id.count);

        food_name_show.setText(food_name.get(position));
        price_show.setText(""+price.get(position));
        counts.setText(""+count.get(position));

        return view;
    }
}
