package com.example.spalvos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText mFullName, mPassword, mPhone, mEmail;
    Button mRegisterBtn,mButton13;
    TextView mLoginBtn;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);



        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);

        mRegisterBtn = findViewById(R.id.registerBtn);
        mButton13 = findViewById(R.id.button13);
        //     mLoginBtn = findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();


        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), Meniu.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Butina ivesti el. pasto adresa");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Iveskite slaptazodi");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Slaptazodis turi buti ilgesnis nei 6 simobliai");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Vartotojas sukurtas", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Meniu.class));

                        } else {

                            Toast.makeText(Register.this, "Toks vartotojas jau yra" , Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });
    }

}




