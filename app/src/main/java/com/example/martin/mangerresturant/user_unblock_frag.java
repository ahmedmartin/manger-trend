package com.example.martin.mangerresturant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class user_unblock_frag extends Fragment {

    private View view;
    private DatabaseReference mref;

    private ImageButton  search;
    private TextView phone;

    private ListView unbliock_list;
    private ArrayAdapter <String>adapt;
    private ArrayList <String> users = new ArrayList<>();
    private ArrayList <String> uids =new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.unblock_frag, container, false);

        phone=view.findViewById(R.id.block_phone);

        adapt = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,users);
        unbliock_list =view.findViewById(R.id.unblock_list);
        unbliock_list.setAdapter(adapt);
        unbliock_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent user_details =new Intent(getActivity(),user_details.class);
                user_details.putExtra("uid",uids.get(position));
                user_details.putExtra("type","unblock");
                startActivity(user_details);
            }
        });



        mref = FirebaseDatabase.getInstance().getReference().child("user").child("unblock");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
                uids.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    uids.add(data.getKey().toString());
                    users .add(data.child("phone").getValue().toString());
                    adapt.notifyDataSetChanged();
                }
                adapt.notifyDataSetChanged();
            }
            public void onCancelled(DatabaseError databaseError) {}
        });


        search=view.findViewById(R.id.unblock_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(TextUtils.isEmpty(phone.getText().toString())){
                     Toast.makeText(getContext(), "ادخل الهاتف المراد البحث عنه ...", Toast.LENGTH_SHORT).show();
                 }else {
                     get_specific_phone();
                 }
            }
        });




        return view;
    }

    void get_specific_phone(){
        boolean not_found = true;
        for(int i=0;i<users.size();i++){
             if(users.get(i).equals(phone.getText().toString())){
                 String uid =uids.get(i);
                 users.clear();
                 uids.clear();
                 users.add(phone.getText().toString());
                 uids.add(uid);
                 adapt.notifyDataSetChanged();
                 not_found=false;
             }
        }

        if (not_found)
            Toast.makeText(getContext(), "هذا الهاتف غير موجود تاكد من الرقم...", Toast.LENGTH_SHORT).show();

    }
}
