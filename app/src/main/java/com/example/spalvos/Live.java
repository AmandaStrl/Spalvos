package com.example.spalvos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


import java.io.ByteArrayOutputStream;
import java.security.Policy;
import java.util.List;

import static android.graphics.ColorSpace.Model.RGB;

    public class Live extends AppCompatActivity  implements CameraBridgeViewBase.CvCameraViewListener2, SurfaceHolder.Callback {

        private static final String TAG = "OCVSample::Activity";
        CameraBridgeViewBase cameraBridgeViewBase;
        Mat mat1, mat2, mat3, mRgba, mHsv, mask;

        BaseLoaderCallback baseLoaderCallback;
        View mColorView;
        TextView mResult;
        Bitmap bitmap;
        double[] rgb;
        int x = -1, y = -1;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_live);
            mResult = findViewById(R.id.textView6);
            mColorView = findViewById(R.id.view2);
            cameraBridgeViewBase = (JavaCameraView) findViewById(R.id.javaCameraView);
            cameraBridgeViewBase.setMaxFrameSize(720, 1280);
            cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
            cameraBridgeViewBase.setCvCameraViewListener(this);
            //     cameraBridgeViewBase.setOnCameraMoveListener(this);
            // cameraBridgeViewBase.setOnTouchListener(this);


            baseLoaderCallback = new BaseLoaderCallback(this) {

                @Override
                public void onManagerConnected(int status) {

                    switch (status) {
                        case BaseLoaderCallback.SUCCESS:
                            cameraBridgeViewBase.enableView();

                            break;
                        default:
                            super.onManagerConnected(status);
                            break;
                    }
                }
            };

        }

        @Override
        public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
            mat1 = inputFrame.rgba();

            Imgproc.rectangle(mat1, new Point(625, 375), new Point(
                    655, 345 ), new Scalar( 255, 0, 0 ), 1
            );

            return mat1;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            x = (int) event.getX();
            y = (int) event.getY();


            Bitmap bitmap = Bitmap.createBitmap(mat1.cols(), mat1.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat1, bitmap);
            float angle = 90;
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, mat1.cols(), mat1.rows(), matrix, true);


            int pixel = bitmap1.getPixel(360, 640);

            int r = Color.red(pixel);
            int g = Color.green(pixel);
            int b = Color.blue(pixel);

            int red = 0;
            int green = 0;
            int blue = 0;
            int pixelCount = 0;

            for (int y = 625; y < 655; y++) {
                for (int x = 345; x < 375; x++) {
                    int c = bitmap1.getPixel(x, y);

                    pixelCount++;
                    red += Color.red(c);
                    green += Color.green(c);
                    blue += Color.blue(c);

                }
            }

            int averageColor = Color.rgb(red / pixelCount, green
                    / pixelCount, blue / pixelCount);
            String hex = "#"+Integer.toHexString(averageColor);


            if (x != -1 && y != -1) {
                mResult.setText("HEX: #" + Integer.toHexString(averageColor));
                mColorView.setBackgroundColor(Color.rgb(red / pixelCount, green
                        / pixelCount, blue / pixelCount));

            }
            return super.onTouchEvent(event);
        }



    void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {


        final int frameSize = width * height;



        for (int j = 0, yp = 0; j < height; j++) {       int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;

            for (int i = 0; i < width; i++, yp++) {

                int y = (0xff & ((int) yuv420sp[yp])) - 16;

                if (y < 0)

                y = 0;

                if ((i & 1) == 0) {

                    v = (0xff & yuv420sp[uvp++]) - 128;

                    u = (0xff & yuv420sp[uvp++]) - 128;

                }



                int y1192 = 1192 * y;

                int r = (y1192 + 1634 * v);

                int g = (y1192 - 833 * v - 400 * u);

                int b = (y1192 + 2066 * u);



                if (r < 0)

                r = 0;

        else if (r > 262143)

                r = 262143;

                if (g < 0)

                g = 0;

        else if (g > 262143)

                g = 262143;

                if (b < 0)

                b = 0;

        else if (b > 262143)

                b = 262143;



                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);

            }

        }

    }




    /*
            mRgba = inputFrame.rgba();

            //convert mat rgb to mat hsv
            Imgproc.cvtColor(mRgba, mHsv, Imgproc.COLOR_RGB2HSV);

            //find scalar sum of hsv
            Scalar mColorHsv = Core.sumElems(mHsv);

            int pointCount = 320*240;

            //convert each pixel
            for (int i = 0; i < mColorHsv.val.length; i++) {
                mColorHsv.val[i] /= pointCount;
            }

            //convert hsv scalar to rgb scalar
            Scalar mColorRgb = convertScalarHsv2Rgba(mColorHsv);

            Log.d("intensity", "Color: #" + String.format("%02X", (int)mColorHsv.val[0])
                + String.format("%02X", (int)mColorHsv.val[1])
                + String.format("%02X", (int)mColorHsv.val[2]) );
            //print scalar value
            Log.d("intensity", "R:"+ String.valueOf(mColorRgb.val[0])+" G:"+String.valueOf(mColorRgb.val[1])+" B:"+String.valueOf(mColorRgb.val[2]));

            int R = (int) mColorRgb.val[0];
            int G = (int) mColorRgb.val[1];
            int B = (int) mColorRgb.val[2];

            int Y = (int) (R *  .299000 + G *  .587000 + B *  .114000);
            int U = (int) (R * -.168736 + G * -.331264 + B *  .500000 + 128);
            int V = (int) (R *  .500000 + G * -.418688 + B * -.081312 + 128);


            Log.d("intensity", "Y:"+ String.valueOf(Y)+" U:"+String.valueOf(U)+" V:"+String.valueOf(V));

             return mRgba;



        }
*/
    //convert Mat hsv to scalar
    private Scalar convertScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB);

        return new Scalar(pointMatRgba.get(0, 0));
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mat1 = new Mat(width, height, CvType.CV_8UC4);
     //   mat2 = new Mat(width, height, CvType.CV_8UC4);
      //  mat3 = new Mat(width, height, CvType.CV_8UC4);
        //mRgba = new Mat(width, height, CvType.CV_8UC4);
        // mHsv = new Mat(width, height, CvType.CV_8UC3);

    }

    @Override
    public void onCameraViewStopped() {

        mat1.release();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
        } else {
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        }
    }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }



