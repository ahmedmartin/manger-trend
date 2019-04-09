package com.example.martin.mangerresturant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class delivery_info extends AppCompatActivity {

    private TextView del_name;
    private TextView del_phone;
    private TextView del_address;
    private TextView del_ssn;

    private DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_info);

        del_name = findViewById(R.id.name);
        del_phone=findViewById(R.id.del_phone);
        del_address=findViewById(R.id.del_address);
        del_ssn = findViewById(R.id.del_ssn);

        Bundle b = getIntent().getExtras();
        final String delivery_name=b.getString("delivery_name");
        Toast.makeText(this,delivery_name,Toast.LENGTH_SHORT).show();

        myref= FirebaseDatabase.getInstance().getReference().child("delivery").child(delivery_name);
        myref.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    delivery_details del = dataSnapshot.getValue(delivery_details.class);
                    del_name.setText(delivery_name);
                    del_address.setText(del.getAddress());
                    del_phone.setText(del.getPhone());
                    del_ssn.setText(del.getSsn());
                }
            }
            public void onCancelled(DatabaseError databaseError) {            }
        });





    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
