package com.example.martin.mangerresturant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_details extends AppCompatActivity {

    private String type;
    private String uid;

    private ImageButton block;
    private ImageButton unblock;
    private TextView fname;
    private TextView lname;
    private TextView phone;
    private TextView address;

    private DatabaseReference ref ;

    private user_info use;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        type=getIntent().getStringExtra("type");
        uid=getIntent().getStringExtra("uid");

        block=findViewById(R.id.block);
        unblock=findViewById(R.id.unblock);
        fname=findViewById(R.id.fname);
        lname=findViewById(R.id.lname);
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);

        if(type.equals("unblock")){
            unblock.setVisibility(View.INVISIBLE);
        }else
            block.setVisibility(View.INVISIBLE);

        ref= FirebaseDatabase.getInstance().getReference().child("user").child(type).child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    use = dataSnapshot.getValue(user_info.class);
                    fname.setText(use.getFname());
                    lname.setText(use.getLname());
                    phone.setText(use.getPhone());
                    address.setText(use.getAddress());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void block(View view) {
        DatabaseReference ref_block = FirebaseDatabase.getInstance().getReference().child("user").child("block").child(uid);
        ref_block.setValue(use);

        DatabaseReference unsend_ref = FirebaseDatabase.getInstance().getReference().child("order").child("unsend").child(uid);
        unsend_ref.removeValue();

        DatabaseReference table_ref=FirebaseDatabase.getInstance().getReference().child("table");
        table_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                  for(DataSnapshot data : dataSnapshot.getChildren()){
                      if(data.hasChild("uid")&&data.child("uid").getValue().toString().equals(uid)){
                          data.getRef().setValue(data.getKey().toString());
                      }
                  }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference wait_ref = FirebaseDatabase.getInstance().getReference().child("order").child("wait").child(uid);
        wait_ref.removeValue();


        ref= FirebaseDatabase.getInstance().getReference().child("user").child("unblock").child(uid);
        ref.removeValue();
        unblock.setVisibility(View.VISIBLE);
        block.setVisibility(View.INVISIBLE);
    }



    public void unblock(View view) {
        DatabaseReference ref_unblock = FirebaseDatabase.getInstance().getReference().child("user").child("unblock").child(uid);
        ref_unblock.setValue(use);
        ref = FirebaseDatabase.getInstance().getReference().child("user").child("block").child(uid);
        ref.removeValue();
        block.setVisibility(View.VISIBLE);
        unblock.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
