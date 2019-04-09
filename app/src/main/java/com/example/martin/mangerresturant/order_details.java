package com.example.martin.mangerresturant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class order_details extends AppCompatActivity {

    private String type;
    private String date;
    private String time;
    private String name;
    private String uid;
    private double sum= 0;


    private DatabaseReference myref;

    private ArrayList<String> foods=new ArrayList<>();
    private ArrayList<Double> price =new ArrayList<>();
    private ArrayList<Integer> counts=new ArrayList<>();

    private adapter adapt;

    private ListView food_list;
    private TextView fname;
    private TextView lname;
    private TextView phone;
    private TextView address;
    private TextView total;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        name=getIntent().getStringExtra("name");
        date=getIntent().getStringExtra("date");
        type=getIntent().getStringExtra("type");
        time=getIntent().getStringExtra("time");

        fname=findViewById(R.id.order_fname);
        lname=findViewById(R.id.order_lname);
        address=findViewById(R.id.order_address);
        phone=findViewById(R.id.order_phone);
        total=findViewById(R.id.order_price);

        food_list=findViewById(R.id.order_food_list);
        adapt=new adapter(order_details.this,foods,price,counts);
        food_list.setAdapter(adapt);

        myref= FirebaseDatabase.getInstance().getReference().child("order").child(type).child(date).child(name).child(time);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data:dataSnapshot.child("foods").getChildren()){
                    foods.add(data.getKey().toString());
                    String s = data.getValue().toString();
                    String ss[] = s.split("x");
                    price.add(Double.parseDouble(ss[0]));
                    counts.add(Integer.parseInt(ss[1]));
                    adapt.notifyDataSetChanged();
                    sum+=(Double.parseDouble(ss[0])*Integer.parseInt(ss[1]));
                }
                if(dataSnapshot.exists()) {
                    uid = dataSnapshot.child("uid").getValue().toString();
                    total.setText(dataSnapshot.child("total").getValue().toString());
                    get_user_info();
                }
            }
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void get_user_info() {
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference().child("user").child("unblock").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    user_info use = dataSnapshot.getValue(user_info.class);
                    fname.setText(use.getFname());
                    lname.setText(use.getLname());
                    address.setText(use.getAddress());
                    phone.setText(use.getPhone());
                }
            }
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void delete(View view) {
        myref.removeValue();
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
