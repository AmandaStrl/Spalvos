package com.example.spalvos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Uzsakymas extends AppCompatActivity {

    EditText mEditText, mEditText2, mEditText3, mEditText4, mEditText5, mEditText6;
    TextView mTextView4;
    DatabaseReference reff;
    Button mbutton10,mbutton11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_uzsakymas);

        mEditText = findViewById(R.id.editText);
        mEditText2 = findViewById(R.id.editText2);
        mEditText3 = findViewById(R.id.editText3);
        mEditText4 = findViewById(R.id.editText4);
        mEditText5 = findViewById(R.id.editText5);
        mEditText6 = findViewById(R.id.editText6);
        mTextView4 = findViewById(R.id.textView4);

        mbutton10 = findViewById(R.id.button10);
        mbutton11 = findViewById(R.id.button11);

        reff = FirebaseDatabase.getInstance().getReference().child("uzsakymai");

        mbutton11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSuma();
            }
        });



        mbutton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kaina = mEditText5.getText().toString();
                double kaina1 = Integer.parseInt(kaina);

                String vardas = mEditText.getText().toString().trim();
                String elPastas = mEditText2.getText().toString().trim();
                String numeris = mEditText3.getText().toString().trim();
                String kodas = mEditText4.getText().toString().trim();
                String talpa = mEditText5.getText().toString().trim();
                String kiekis = mEditText6.getText().toString().trim();

                if(TextUtils.isEmpty(vardas)){
                    mEditText.setError("Įveskite savo vardą");
                    return;
                }
                else if(TextUtils.isEmpty(elPastas)){
                    mEditText2.setError("Įveskite el. pašto adresą");
                    return;
                }
               else if(TextUtils.isEmpty(numeris)){
                    mEditText3.setError("Įveskite telefono numerį");
                    return;
                }
               else if(TextUtils.isEmpty(kodas)){
                    mEditText4.setError("Įveskite spalvos kodą");
                    return;
                }
               else if(TextUtils.isEmpty(kiekis)){
                    mEditText6.setError("Įveskite kiekį");
                    return;
                }
               else if(kaina1 < 1000){
                    mEditText5.setError("Minimali talpa 1000ml");
                    return;
                } else {
                    addUzsakymas();
                }
            }
        });
    }
        public void addUzsakymas() {
            String kaina = mEditText5.getText().toString();
            double kaina1 = Integer.parseInt(kaina);

                String vardas = mEditText.getText().toString().trim();
                String elPastas = mEditText2.getText().toString().trim();
                String numeris = mEditText3.getText().toString().trim();
                String kodas = mEditText4.getText().toString().trim();
                String talpa = mEditText5.getText().toString().trim();
                String kiekis = mEditText6.getText().toString().trim();




                String id = reff.push().getKey();

                Uzsakymai Uzsakymai = new Uzsakymai(vardas, elPastas, numeris, kodas, talpa, kiekis);

                reff.child(id).setValue(Uzsakymai);

                Toast.makeText(this, "Užsakymas priimtas", Toast.LENGTH_LONG).show();
            }

        public void addSuma() {
            String suma = mEditText6.getText().toString();
            double suma1 = Integer.parseInt(suma);

            String kaina = mEditText5.getText().toString();
            double kaina1 = Integer.parseInt(kaina);
            double total = (int) ((kaina1 * 0.01) * suma1);


            String suma2 = String.valueOf(total);
            mTextView4.setText(suma2);
        }


    }
