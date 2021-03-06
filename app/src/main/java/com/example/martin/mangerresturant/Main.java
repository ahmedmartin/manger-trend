package com.example.martin.mangerresturant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main extends AppCompatActivity {


    private DrawerLayout dl ;
    private ActionBarDrawerToggle abdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar); dl =findViewById(R.id.draw_layout);
        abdt = new ActionBarDrawerToggle(this,dl,toolbar,R.string.open,R.string.close);
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nav = findViewById(R.id.navigation);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =item.getItemId();
                switch (id){
                    case R.id.table_order:
                        table();
                        break;

                    case R.id.delivery_order :
                        delivery();
                        break;

                    case R.id.Menu :
                      menu();
                        break;

                    case R.id.log_out :
                        exit();
                        break;
                }

                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }


    public void table() {
        Intent table= new Intent(this,main_table.class);
        startActivity(table);
    }

    public void delivery() {
      Intent delivery = new Intent(this,main_delivery.class);
      startActivity(delivery);
    }



    @Override
    public void onBackPressed() {

    }

    public void exit() {
        FirebaseAuth mauth=FirebaseAuth.getInstance();
        FirebaseUser carrent =mauth .getCurrentUser();
        mauth.signOut();
        Intent sign_in=new Intent(Main.this,sign_in.class);
        sign_in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(sign_in);
    }

    public void menu() {
        Intent menu = new Intent(this,main_menu.class);
        startActivity(menu);
    }

    public void user_info(View view) {
        Intent user = new Intent(Main.this, user.class);
        startActivity(user);
    }

    public void cashier(View view) {
        Intent casheir =new Intent(Main.this,cashier.class);
        startActivity(casheir);
    }
}
