package com.example.martin.mangerresturant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class delivery_frag extends Fragment{

    private View view;

    private Spinner days_spinner;
    private ListView delivery_name_list;
    private ImageButton delete;
    private ArrayAdapter<String> day_adapt;
    private ArrayAdapter<String> name_adapt;
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();

    private String selected_date;

    private DatabaseReference myref;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.delivery_frag, container, false);

        delivery_name_list =view.findViewById(R.id.delivery_list);
        days_spinner =view.findViewById(R.id.delivery_days);
        delete=view.findViewById(R.id.delivery_date_delete);

        day_adapt =new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,date);
         day_adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         days_spinner.setAdapter(day_adapt);

         name_adapt=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,name);
         delivery_name_list.setAdapter(name_adapt);

        myref= FirebaseDatabase.getInstance().getReference().child("order") .child("delivery");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                date.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    date.add(data.getKey().toString());
                    day_adapt.notifyDataSetChanged();
                }
                if(dataSnapshot.exists()) {
                    selected_date = date.get(0);
                    get_delivery();
                }
                day_adapt.notifyDataSetChanged();
            }
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        delivery_name_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent delivery_orders =new Intent(getActivity(),orders.class);
                delivery_orders.putExtra("name",name.get(position));
                delivery_orders.putExtra("date",selected_date);
                delivery_orders.putExtra("type","delivery");
                startActivity(delivery_orders);
            }
        });


        days_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selected_date=parent.getItemAtPosition(position).toString();
                get_delivery();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.child(selected_date).removeValue();
                day_adapt.notifyDataSetChanged();
            }
        });

        return view;
    }

    void get_delivery(){

        myref.child(selected_date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                     name.add(data.getKey().toString());
                     name_adapt.notifyDataSetChanged();
                }
                name_adapt.notifyDataSetChanged();
            }
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
}
