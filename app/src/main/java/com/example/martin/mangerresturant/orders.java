package com.example.martin.mangerresturant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class orders extends AppCompatActivity {

    private String name;
    private String date;
    private String type;

    private ListView order_list;
    private CustomListAdapter adapter;
    private ArrayList<String> time = new ArrayList<>();
    private ArrayList<Double> total=new ArrayList<>();

    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        name=getIntent().getStringExtra("name");
        date=getIntent().getStringExtra("date");
        type=getIntent().getStringExtra("type");

        myref= FirebaseDatabase.getInstance().getReference().child("order") .child(type).child(date).child(name);

        adapter=new CustomListAdapter(orders.this,time,total);
        order_list=findViewById(R.id.delivery_orders_list);
        order_list.setAdapter(adapter);
        order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent order_details=new Intent(orders.this, com.example.martin.mangerresturant.order_details.class);
                order_details.putExtra("name",name);
                order_details.putExtra("date",date);
                order_details.putExtra("time",time.get(position));
                order_details.putExtra("type",type);
                startActivity(order_details);
            }
        });

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    time.clear();
                    total.clear();
                  for(DataSnapshot data :dataSnapshot.getChildren()){
                      time.add(data.getKey().toString());
                      total.add(Double.parseDouble(data.child("total").getValue().toString()));
                      adapter.notifyDataSetChanged();
                  }
                  adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
    }


}
