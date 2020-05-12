package com.example.spalvos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Meniu2 extends AppCompatActivity {

    private Button button14,button15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_meniu2);

        button14 = findViewById(R.id.button14);
        button15 = findViewById(R.id.button15);

        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKamera();

            }
        });
        button15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLive();

            }
        });
    }

        public void openLive() {
            Intent intent = new Intent(this, Live.class);
            startActivity(intent);
        }

        public void openKamera() {
            Intent intent = new Intent(this, Kamera.class);
            startActivity(intent);
        }
    }

