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

public class main_menu extends AppCompatActivity {

    private DatabaseReference myRef;

    private ListView part_list ;
    private TextView part;

    private ArrayList<String> parts= new ArrayList();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        myRef= FirebaseDatabase.getInstance().getReference().child("menu");

        part_list = findViewById(R.id.part_list);
        part = findViewById(R.id.part);

        // list view adapter and implement
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,parts);
        part_list.setAdapter(adapter);

        myRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                parts.add(dataSnapshot.getKey().toString()); // get key value for tables added to tables array
                Collections.sort(parts);   // sort tables array
                adapter.notifyDataSetChanged(); // update data in table list
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {            }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {            }
            public void onCancelled(DatabaseError databaseError) {            }
        });

        part_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent eat = new Intent(main_menu.this,eat_menu.class);
                Bundle b = new Bundle();
                b.putString("eat_name",parts.get(position));
                eat.putExtras(b);
                startActivity(eat);
            }
        });
    }

    public void add(View view) {
        String part_name = part.getText().toString();
        if(TextUtils.isEmpty(part_name)){
            Toast.makeText(this,"يجب كتابه القسم المراد اضافته",Toast.LENGTH_SHORT).show();
        }else{
            boolean notfound = true;
            for(int i=0;i<parts.size();i++) {
                if (part_name.equals(parts.get(i))) {
                    notfound = false;
                    Toast.makeText(this, "يجب اختيار قسم ليس موجوده بالفعل ليتم الاضافه", Toast.LENGTH_SHORT).show();
                }
            }
            if (notfound) {
                myRef.child(part_name).setValue(part_name);
                Toast.makeText(this, "تم الاضافه", Toast.LENGTH_SHORT).show();
            }

            // berg3 el entry fade
            part.setText("");
        }
    }

    public void delete(View view) {
        String part_name = part.getText().toString();
        if(TextUtils.isEmpty(part_name)){
            Toast.makeText(this,"يجب كتابه القسم المراد حذفه",Toast.LENGTH_SHORT).show();
        }else{
            boolean notfound = true;
            for(int i=0;i<parts.size();i++){
                if(part_name.equals(parts.get(i))){
                    notfound = false;
                    delete_dialoge(i);
                }
            }
            if (notfound)
                Toast.makeText(this, "يجب اختيار قسم موجوده بالفعل ليتم الحذف", Toast.LENGTH_SHORT).show();

            // berg3 el entry fade
            part.setText("");
        }
    }


    public void delete_dialoge(final int position){

        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("تحذير !!  اقرانى مهم ");
        build.setMessage("كل ما يحتويه هذا القسم سوف يتم ازالته هل تريد الحذف ..؟!");
        build.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference remove_part = FirebaseDatabase.getInstance().getReference().child("menu");
                remove_part.child(parts.get(position).toString()).removeValue();
                parts.remove(position);
                Collections.sort(parts);  // sort parts array
                adapter.notifyDataSetChanged(); // add update to Array Adapter
                Toast.makeText(main_menu.this,"تم الحذف",Toast.LENGTH_SHORT).show();
            }
        });
        build.setNegativeButton("لا ", new DialogInterface.OnClickListener() {
            // if press no  no thing happend
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(main_menu.this,"تم الغاء الحذف",Toast.LENGTH_SHORT).show();
            }
        });
        build.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
