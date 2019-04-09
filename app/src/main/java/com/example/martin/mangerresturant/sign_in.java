package com.example.martin.mangerresturant;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class sign_in extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView email;
    private TextView password;
    private Intent main ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        main = new Intent(sign_in.this, Main.class);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null &&currentUser.isEmailVerified())
            startActivity(main);
    }

    public void sign_in(View view) {
        String Email=email.getText().toString();
        String Password =password.getText().toString();
        if(TextUtils.isEmpty(Email)){
            Toast.makeText(sign_in.this, "قم بادخال البريد الالكترونى", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Password)){
            Toast.makeText(sign_in.this, "ادخل الرقم السرى ", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                startActivity(main);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(sign_in.this, "حدث خطا ما تاكد من الانترنت او بيانات الدخول", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
