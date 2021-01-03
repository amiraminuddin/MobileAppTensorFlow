package com.example.myapplication;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {

    //for CameraIntent
    private int PICK_IMAGE_REQUEST = 1;

    //new content
    private Bitmap mImageBitmap;
    private String mCurrentPhoto;
    private ImageView mImageView;

    //setUp for tensorflow
    public static final int INPUT_SIZE = 224;
    public static final int IMAGE_MEAN = 128;
    public static final float IMAGE_STD = 128;
    public static final String INPUT_NAME = "input";
    public static final String OUTPUT_NAME = "final_result";
    public static final String MODEL_FILE = "file:///android_asset/rounded_graph2.pb";
    public static final String LABEL_FILE = "file:///android_asset/labels2.txt";

    public Classifier classifier;
    public Executor executor = Executors.newSingleThreadExecutor();
    public TextView TV1;
    public TextView TV2;
    public ImageView iV1;
    public Button bCheck;
    public TextView TVAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TV1 = (TextView) findViewById(R.id.textView1);
        iV1 = (ImageView) findViewById(R.id.imageView1);
        TV2 = (TextView) findViewById(R.id.textView2);
        bCheck = (Button) findViewById(R.id.buttonCheck);
        TVAvailable = (TextView) findViewById(R.id.textViewAvailable);
        initTensorFlowAndLoadModel();
    }

    //declaration for TensorFlow
    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            IMAGE_MEAN,
                            IMAGE_STD,
                            INPUT_NAME,
                            OUTPUT_NAME);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }


    public List<Classifier.Recognition> analyse(Bitmap bitmap)
    {
        bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
        final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
        return results;
    }

    public void selectPhoto(View v)
    {
        //old coding just drag picture from gallery
        /**Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
       **/
        Runnable run = new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                    }
                });
            }
        };
        Thread thr = new Thread(run);
        thr.start();
        TV1.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //show the result if the picture exist in classifier
        if (resultCode == RESULT_OK) {
            //try
           // {
                //final Uri imageUri = data.getData();
                Bundle extras = data.getExtras();
                //final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                //final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Bitmap selectedImage = (Bitmap) extras.get("data");


                List<Classifier.Recognition> results = analyse(selectedImage);
                TV1.setText(results.get(0).toString());
                setPicture(selectedImage);
                iV1.setImageBitmap(selectedImage);

           // } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
        //String confidence = TV1.getText().toString();
        //String[] splitStr = confidence.split("\\s+");
        //TV2.setText(splitStr[3]);

    //}

    public void checkPhoto(View v)
    {
        String confidence = TV1.getText().toString();
        String[] splited = confidence.split("\\s+");
        TV2.setText(splited[2]);

        String confiden = TV2.getText().toString();
        Float confidenf = Float.parseFloat(confiden);

        if (confidenf > 99)
        {
            TVAvailable.setVisibility(TextView.VISIBLE);
            TVAvailable.setText(splited[1]);
            String id = TVAvailable.getText().toString();
            Intent i = new Intent(MainActivity.this,displayResult.class);
            i.putExtra("id", id);
            startActivity(i);
        }
        else
        {
            TVAvailable.setVisibility(TextView.VISIBLE);
            TVAvailable.setText("ITEM NOT AVAILABLE");
        }
    }



    public void setPicture(Bitmap bp)
    {
        Bitmap scaledBp =  Bitmap.createScaledBitmap(bp, iV1.getWidth(), iV1.getHeight(), false);
       iV1.setImageBitmap(scaledBp);
    }

}
