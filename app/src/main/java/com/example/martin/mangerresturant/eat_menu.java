package com.example.martin.mangerresturant;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class eat_menu extends AppCompatActivity {


    private DatabaseReference myRef;

    private ListView food_list;
    private TextView food_name;
    private TextView price;

    private ArrayList<String> foods=new ArrayList<>();
    private ArrayList<Double> prices = new ArrayList<>();

    private  CustomListAdapter  custom;

    private String part;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eat_menu);

        Bundle b = getIntent().getExtras();
        part =  b.getString("eat_name");

       food_list =findViewById(R.id.food_list);
       food_name=findViewById(R.id.food_name);
       price = findViewById(R.id.price);

        myRef = FirebaseDatabase.getInstance().getReference().child("menu").child(part);


         custom= new CustomListAdapter(eat_menu.this,foods,prices);
        food_list.setAdapter(custom);

        myRef.addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                foods.add(dataSnapshot.getKey().toString());
                prices.add(Double.parseDouble(dataSnapshot.getValue().toString()));
                custom.notifyDataSetChanged();
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        });

       /* food_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String uid=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                myRef = FirebaseDatabase.getInstance().getReference()
                        .child("user").child("unblock").child(uid);
                myRef.child(foods.get(i)).setValue("65ffjnfjknf65f66f1dd");
            }
        });*/

    }

    public void add(View view) {
        String food = food_name.getText().toString();
        String pric = price.getText().toString();
        if(TextUtils.isEmpty(food)){
            Toast.makeText(this,"يجب كتابه الطعام المراد اضافته",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(pric)){
            Toast.makeText(this,"يجب كتابه السعر للطعام المراد اضافته",Toast.LENGTH_SHORT).show();
        }else{
            boolean notfound = true;
            for(int i=0;i<foods.size();i++) {
                if (food.equals(foods.get(i))) {
                    notfound = false;
                    Toast.makeText(this, "يجب اختيار طعام ليس موجوده بالفعل ليتم الاضافه", Toast.LENGTH_SHORT).show();
                }
            }
            if (notfound) {
                myRef.child(food).setValue(pric);
                Toast.makeText(this, "تم الاضافه", Toast.LENGTH_SHORT).show();
            }

            // berg3 el entry fade
            food_name.setText("");
            price.setText("");
        }


    }

    public void delete(View view) {
        String food = food_name.getText().toString();
        String pric = price.getText().toString();
        if(TextUtils.isEmpty(food)){
            Toast.makeText(this,"يجب كتابه الطعام المراد حذفه",Toast.LENGTH_SHORT).show();
        }else {
            boolean notfound = true;
            for(int i=0;i<foods.size();i++){
                if(food.equals(foods.get(i))){
                    notfound = false;
                    delete_dialoge(i);
                }
            }
            if (notfound)
                Toast.makeText(this, "يجب اختيار طعام موجود بالفعل ليتم الحذف", Toast.LENGTH_SHORT).show();

            // berg3 el entry fade
            food_name.setText("");
            price.setText("");
        }
    }

    public void delete_dialoge(final int position){

        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("تحذير !!  اقرانى مهم ");
        build.setMessage(" هل تريد حذف الطعام ..؟!");
        build.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference remove_food = FirebaseDatabase.getInstance().getReference().child("menu").child(part);
                remove_food.child(foods.get(position).toString()).removeValue();
                foods.remove(position);
                prices.remove(position);
                custom.notifyDataSetChanged(); // add update to Array Adapter
                Toast.makeText(eat_menu.this,"تم الحذف",Toast.LENGTH_SHORT).show();
            }
        });
        build.setNegativeButton("لا ", new DialogInterface.OnClickListener() {
            // if press no  no thing happend
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(eat_menu.this,"تم الغاء الحذف",Toast.LENGTH_SHORT).show();
            }
        });
        build.show();
    }

    public void edit(View view) {
        String food = food_name.getText().toString();
        String pric = price.getText().toString();
        if(TextUtils.isEmpty(food)){
            Toast.makeText(this,"يجب كتابه الطعام المراد تعديله",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(pric)){
            Toast.makeText(this,"يجب كتابه السعر للطعام المراد تعديله",Toast.LENGTH_SHORT).show();
        }else{
            boolean notfound = true;
            for(int i=0;i<foods.size();i++) {
                if (food.equals(foods.get(i))) {
                    notfound = false;
                    myRef.child(food).setValue(pric);
                    prices.set(i,Double.parseDouble(pric));
                    custom.notifyDataSetChanged();
                    Toast.makeText(this, "تم التعديل", Toast.LENGTH_SHORT).show();
                }
            }
            if (notfound) {
                Toast.makeText(this, "يجب اختيار طعام  موجود بالفعل ليتم التعديل", Toast.LENGTH_SHORT).show();
            }

            price.setText("");
            food_name.setText("");
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}




