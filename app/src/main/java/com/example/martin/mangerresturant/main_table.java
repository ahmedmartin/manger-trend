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
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class main_table extends AppCompatActivity {

    private DatabaseReference mref ;
    private TextView table_num ;
    private ArrayList<Integer> tables = new ArrayList<>();
    private ListView table_list;
    private ArrayAdapter<Integer> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_table);

        table_list=findViewById(R.id.table_list);
        table_num = findViewById(R.id.table_number);


        mref= FirebaseDatabase.getInstance().getReference().child("table");

        // list view adapter and implement
        adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_list_item_1,tables);
        table_list.setAdapter(adapter);

        // get data from data base and add item added to data base
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    tables.add(Integer.parseInt(dataSnapshot.getKey().toString())); // get key value for tables added to tables array
                    Collections.sort(tables);   // sort tables array
                    adapter.notifyDataSetChanged(); // update data in table list
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {            }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }
            public void onCancelled(DatabaseError databaseError) {            }
        });

    }


    // add table to resturant
    public void add(View view) {
        String table_number = table_num.getText().toString();
        if(TextUtils.isEmpty(table_number)){
           Toast.makeText(this,"يجب اختيار رقم طاوله !!",Toast.LENGTH_SHORT).show();
        }else {
            boolean notfound = true;   // be2ole lw 3nde el 3onsr da wl la2
            for (int i = 0; i < tables.size(); i++) {
                if (Integer.parseInt(table_number) == (tables.get(i))) {  // lw el 3onsor mogod not found =false l2no found
                    notfound = false;
                    Toast.makeText(this, "يجب اختيار طاوله ليست موجوده بالفعل ليتم الاضافه", Toast.LENGTH_SHORT).show();
                }

            }
            if (notfound) {  // f 7alet an mfe4 t4aboh ben el tables eb2a hdef el table
                mref.child(table_num.getText().toString()).setValue(table_num.getText().toString());
                Toast.makeText(this, "تم الاضافه", Toast.LENGTH_SHORT).show();
            }
            //  berg3 el entry fade
            table_num.setText("");
        }

    }


    // delete table from resturant and all information ....
    public void remove(View view) {
        String table_number = table_num.getText().toString();
        if(TextUtils.isEmpty(table_number)){
            Toast.makeText(this,"يجب اختيار رقم طاوله !!",Toast.LENGTH_SHORT).show();
        }else {
            boolean notfound = true;    // be2ole lw 3nde el 3onsr da wl la2
            for (int i = 0; i < tables.size() && notfound; i++) {
                if (Integer.parseInt(table_number) == (tables.get(i))) {  // lw el 3onsor mogod not found =false l2no found f delete el item
                    notfound = false;
                    delete_dialoge(i);
                }
            }
            if (notfound)
                Toast.makeText(this, "يجب اختيار طاوله موجوده بالفعل ليتم الحذف", Toast.LENGTH_SHORT).show();

            // berg3 el entry fade
            table_num.setText("");
        }
    }


    public void delete_dialoge(final int position){
        // warrning message appear when i wanna remove table
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setMessage(" كل المعلومات عن هذه الطاوله سوف يتم حذفها تماما هل تريد الحذف ؟!"); // message body
        build.setTitle("تحذير!!  اقرانى مهم");  // message title
        build.setPositiveButton(" نعم", new DialogInterface.OnClickListener() {
            // if press yes all data will remove
            public void onClick(DialogInterface dialogInterface, int iii) {
                DatabaseReference tableref = FirebaseDatabase.getInstance().getReference().child("table");
                tableref.child(tables.get(position).toString()).removeValue(); // delete from data base
                tables.remove(position);   // delete from table array
                Collections.sort(tables);  // sort tables array
                adapter.notifyDataSetChanged(); // add update to Array Adapter
                Toast.makeText(main_table.this,"تم الحذف",Toast.LENGTH_SHORT).show();
            }
        });
        build.setNegativeButton("لا ", new DialogInterface.OnClickListener() {
            // if press no  no thing happend
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(main_table.this,"تم الغاء الحذف",Toast.LENGTH_SHORT).show();
            }
        });
        build.show();
    }


    public void day_price(View view) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
