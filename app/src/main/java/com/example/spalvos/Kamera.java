package com.example.spalvos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.core.CvType;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Mat;


public class Kamera extends AppCompatActivity {
    Bitmap bitmap;
    View mColorView;
    ImageView mImageView;
    TextView mResultTv;
    Button mButton12;
    public Uri imguri;
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== 1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            imguri=data.getData();
            mImageView.setImageURI(imguri);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_kamera);
        mImageView = findViewById(R.id.imageView2);
        mColorView = findViewById(R.id.view);
        mResultTv = findViewById(R.id.resultTv);
        mButton12 = findViewById(R.id.button12);
        mImageView.setDrawingCacheEnabled(true);
        mImageView.buildDrawingCache(true);


        mButton12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });

        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    bitmap = mImageView.getDrawingCache();


                    int pixel = bitmap.getPixel((int)event.getX(), (int)event.getY());

                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);

                    String hex = "#"+Integer.toHexString(pixel);

                    mColorView.setBackgroundColor(Color.rgb(r,g,b));

                    mResultTv.setText("RGB: "+r +", "+ g +", "+ b+"\nHEX: "+ hex);


                }
                return true;
            }
        });


        }
    public void Filechooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
}
