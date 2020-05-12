package com.example.spalvos;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Policy;
import java.util.HashMap;

public class TakePicture extends Activity implements SurfaceHolder.Callback {
    // a variable to store a reference to the Image View at the main.xml file
    private ImageView iv_image;
    // a variable to store a reference to the Surface View at the main.xml file
    private SurfaceView sv;

    // a bitmap to display the captured image
    private Bitmap bmp;

    // Camera variables
// a surface holder
    private SurfaceHolder sHolder;
    // a variable to control the camera
    Camera mCamera;
    // the camera parameters
    private Camera.Parameters parameters;
    Camera.PictureCallback mCall;
    Button takePicture;
    Handler handler;
    TextView colorName, Hex;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int width = 0, height = 0;
    private Camera.Size pictureSize;

    boolean mStopHandler = false;
    private static final HashMap<String, String> sColorNameMap;

    static {
        sColorNameMap = new HashMap();
        sColorNameMap.put("#000000", "black");
        sColorNameMap.put("#A9A9A9", "darkgray");
        sColorNameMap.put("#808080", "gray");
        sColorNameMap.put("#D3D3D3", "lightgray");
        sColorNameMap.put("#FFFFFF", "white");
        // .....

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mCamera != null) {
                // do your stuff - don't create a new runnable here!
                mCamera.takePicture(null, null, mCall);

                if (!mStopHandler) {
                    handler.postDelayed(this, 500);
                }
            }
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        handler = new Handler();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        colorName = (TextView) findViewById(R.id.textView7);
        Hex = (TextView) findViewById(R.id.textView8);
        // get the Image View at the main.xml file
        iv_image = (ImageView) findViewById(R.id.imageView);
     //   takePicture = (Button) findViewById(R.id.button14);

        // get the Surface View at the main.xml file
        sv = (SurfaceView) findViewById(R.id.surfaceView);

        // Get a surface
        sHolder = sv.getHolder();

        // add the callback interface methods defined below as the Surface View
        // callbacks
        sHolder.addCallback(this);

        // tells Android that this surface will have its data constantly
        // replaced
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
     /*   takePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCamera.takePicture(null, null, mCall);

            }
        }); */
    }

    @Override
    public void surfaceChanged(SurfaceHolder sv, int arg1, int arg2, int arg3) {
        // get camera parameters
        parameters = mCamera.getParameters();
        // parameters.setPreviewFormat(ImageFormat.NV21);
        mCamera.setDisplayOrientation(90);
        setBesttPictureResolution();

        mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewDisplay(sv);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mCamera.setParameters(parameters);
        // set camera parameters
        mCamera.startPreview();

        // sets what code should be executed after the picture is taken
        mCall = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                // decode the data obtained by the camera into a Bitmap
                if (data != null) {
                    bmp = decodeBitmap(data);
                }
                // set the iv_image
                if (bmp != null) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    // bmp.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
                    Bitmap resizebitmap = Bitmap.createBitmap(bmp,
                            bmp.getWidth() / 2, bmp.getHeight() / 2, 60, 60);
                    iv_image.setImageBitmap(rotateImage(resizebitmap, 90));

                    int color = getAverageColor(resizebitmap);
                    Log.i("Color Int", color + "");
                    // int color =
                    // resizebitmap.getPixel(resizebitmap.getWidth()/2,resizebitmap.getHeight()/2);

                    String strColor = String.format("#%06X", 0xFFFFFF & color);
                    Hex.setText(strColor);
                    String colorname = sColorNameMap.get(strColor);
                    if (colorName != null) {
                        colorName.setText(colorname);
                    }

                    Log.i("Pixel Value",
                            "Top Left pixel: " + Integer.toHexString(color));
                }

            }
        };
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, acquire the camera and tell it where
        // to draw the preview.
        // mCamera = Camera.open();
        mCamera = getCameraInstance();
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
                handler.post(runnable);

            } catch (IOException exception) {
                mCamera.release();
                mCamera = null;
            }
        } else
            Toast.makeText(getApplicationContext(), "Camera is not available",
                    Toast.LENGTH_SHORT).show();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        if (mCamera != null) {
            // stop the preview
            mCamera.stopPreview();
            // release the camera
            mCamera.release();
        }
        // unbind the camera from this object
        if (handler != null)
            handler.removeCallbacks(runnable);
    }

    public static Bitmap decodeBitmap(byte[] data) {

        Bitmap bitmap = null;
        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inDither = false; // Disable Dithering mode
        bfOptions.inPurgeable = true; // Tell to gc that whether it needs free
        // memory, the Bitmap can be cleared
        bfOptions.inInputShareable = true; // Which kind of reference will be
        // used to recover the Bitmap data
        // after being clear, when it will
        // be used in the future
        bfOptions.inTempStorage = new byte[32 * 1024];

        if (data != null)
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
                    bfOptions);

        return bitmap;
    }

    public Bitmap rotateImage(Bitmap src, float degree) {
        // create new matrix object
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        // return new bitmap rotated using matrix
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(),
                matrix, true);
    }

    public int getAverageColor(Bitmap bitmap) {
        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int pixelCount = 0;

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int c = bitmap.getPixel(x, y);

                pixelCount++;
                redBucket += Color.red(c);
                greenBucket += Color.green(c);
                blueBucket += Color.blue(c);
                // does alpha matter?
            }
        }

        int averageColor = Color.rgb(redBucket / pixelCount, greenBucket
                / pixelCount, blueBucket / pixelCount);
        return averageColor;
    }

    int[] averageARGB(Bitmap pic) {
        int A, R, G, B;
        A = R = G = B = 0;
        int pixelColor;
        int width = pic.getWidth();
        int height = pic.getHeight();
        int size = width * height;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixelColor = pic.getPixel(x, y);
                A += Color.alpha(pixelColor);
                R += Color.red(pixelColor);
                G += Color.green(pixelColor);
                B += Color.blue(pixelColor);
            }
        }

        A /= size;
        R /= size;
        G /= size;
        B /= size;

        int[] average = {A, R, G, B};
        return average;

    }

    private void setBesttPictureResolution() {
        // get biggest picture size
        width = pref.getInt("Picture_Width", 0);
        height = pref.getInt("Picture_height", 0);

        if (width == 0 | height == 0) {
            pictureSize = getBiggesttPictureSize(parameters);
            if (pictureSize != null)
                parameters
                        .setPictureSize(pictureSize.width, pictureSize.height);
            // save width and height in sharedprefrences
            width = pictureSize.width;
            height = pictureSize.height;
            editor.putInt("Picture_Width", width);
            editor.putInt("Picture_height", height);
            editor.commit();

        } else {
            // if (pictureSize != null)
            parameters.setPictureSize(width, height);
        }
    }

    private Camera.Size getBiggesttPictureSize(Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPictureSizes()) {
            if (result == null) {
                result = size;
            } else {
                int resultArea = result.width * result.height;
                int newArea = size.width * size.height;

                if (newArea > resultArea) {
                    result = size;
                }
            }
        }

        return (result);
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
}