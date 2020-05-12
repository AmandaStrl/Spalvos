package com.example.spalvos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Meniu extends AppCompatActivity {

    private Button button,button4,button8,button7,button13,button15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_meniu);

        button = findViewById(R.id.button);
        button4 = findViewById(R.id.button4);
        button13 = findViewById(R.id.button13);
        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openPalete();

        }
    });

            button4 = findViewById(R.id.button4);
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPaieska();
                }
            });
        button8 = findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUzsakymas();
            }
        });
        button7 = findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMeniu2();
            }
        });
        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog1();
            }
        });
}

    public void openPalete() {
        Intent intent = new Intent(this, Palete.class);
        startActivity(intent);

    }
    public void openDialog1() {
        Dialog1 dialog = new Dialog1();
        dialog.show(getSupportFragmentManager(),"dialog");
    }
    public void openMeniu2() {
        Intent intent = new Intent(this, Meniu2.class);
        startActivity(intent);
    }

            public void openPaieska() {
                Intent intent = new Intent(this, Paieska.class);
                startActivity(intent);

            }
    public void openUzsakymas() {
        Intent intent = new Intent(this, Uzsakymas.class);
        startActivity(intent);
    }
}

