package com.example.martin.mangerresturant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class main_delivery extends AppCompatActivity {

    private ArrayList <String> deliveries = new ArrayList<>();

    private DatabaseReference myRef ;

    private ListView delivery_list;
    private TextView delivery;
    private TextView phone;
    private TextView address;
    private TextView ssn;

    private ArrayAdapter adpter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_delivery);

        myRef= FirebaseDatabase.getInstance().getReference().child("delivery");

        delivery = findViewById(R.id.delivery);
        phone = findViewById(R.id.block_phone);
        address= findViewById(R.id.address);
        ssn=findViewById(R.id.ssn);

        delivery_list = findViewById(R.id.delivery_list);
         adpter = new ArrayAdapter<String>(main_delivery.this,android.R.layout.simple_list_item_1,deliveries);
        delivery_list.setAdapter(adpter);
        delivery_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent delivery_info = new Intent(main_delivery.this, delivery_info.class);
                Bundle b = new Bundle();
                b.putString("delivery_name",deliveries.get(position).toString());
                delivery_info.putExtras(b);
                startActivity(delivery_info);
            }
        });



        myRef.addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                deliveries.add(dataSnapshot.getKey().toString()); // get key value for tables added to tables array
                Collections.sort(deliveries);   // sort tables array
                adpter.notifyDataSetChanged(); // update data in table list
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) { }
        });


    }

    public void add(View view) {
        String delivery_name = delivery.getText().toString();
        String delivery_phone=phone.getText().toString();
        String delivery_address = address.getText().toString();
        String delivery_ssn = ssn.getText().toString();
        if(TextUtils.isEmpty(delivery_name)){
            Toast.makeText(this,"يجب كتابه الطيار المراد اضافته",Toast.LENGTH_SHORT).show();
        }else{
            boolean notfound = true;
            for(int i=0;i<deliveries.size();i++) {
                if (delivery_name.equals(deliveries.get(i))) {
                    notfound = false;
                    Toast.makeText(this, "يجب اختيار طيار ليس موجوده بالفعل ليتم الاضافه", Toast.LENGTH_SHORT).show();
                }
            }
            if (notfound) {
                delivery_details del=new delivery_details(delivery_phone,delivery_address,delivery_ssn);
                myRef.child(delivery_name).setValue(del);
                Toast.makeText(this, "تم الاضافه", Toast.LENGTH_SHORT).show();
            }

            // berg3 el entry fade
            delivery.setText("");
            phone.setText("");
            address.setText("");
            ssn.setText("");
        }
    }

    public void delete(View view) {
        String part_name = delivery.getText().toString();
        if(TextUtils.isEmpty(part_name)){
            Toast.makeText(this,"يجب كتابه الطيار المراد حذفه",Toast.LENGTH_SHORT).show();
        }else{
            boolean notfound = true;
            for(int i=0;i<deliveries.size();i++){
                if(part_name.equals(deliveries.get(i))){
                    notfound = false;
                    delete_dialoge(i);
                }
            }
            if (notfound)
                Toast.makeText(this, "يجب اختيار طيار موجوده بالفعل ليتم الحذف", Toast.LENGTH_SHORT).show();

            // berg3 el entry fade
            delivery.setText("");
        }
    }

    public void delete_dialoge(final int position){

        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("تحذير !!  اقرانى مهم ");
        build.setMessage(" هل تريد الحذف  الطيار ..؟!");
        build.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference remove_delivery = FirebaseDatabase.getInstance().getReference().child("delivery");
                remove_delivery.child(deliveries.get(position).toString()).removeValue();
                deliveries.remove(position);
                Collections.sort(deliveries);  // sort parts array
                adpter.notifyDataSetChanged(); // add update to Array Adapter
                Toast.makeText(main_delivery.this,"تم الحذف",Toast.LENGTH_SHORT).show();
            }
        });
        build.setNegativeButton("لا ", new DialogInterface.OnClickListener() {
            // if press no  no thing happend
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(main_delivery.this,"تم الغاء الحذف",Toast.LENGTH_SHORT).show();
            }
        });
        build.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
