package com.example.mytranslationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;


public class MainActivity extends AppCompatActivity {

    private Button captureImageBtn, detectTextBtn;
    private ImageView imageView;
    private TextView textView;
    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureImageBtn = findViewById(R.id.capture_image_btn);
        detectTextBtn = findViewById(R.id.detect_text_image_btn);
        imageView = findViewById(R.id.image_view);
        textView = findViewById(R.id.text_display);

        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                textView.setText("");
            }
        });
        detectTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectTextFromImage();
            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void detectTextFromImage() {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectImage(firebaseVisionImage).addOnSuccessListener(new OnSuccess() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText)
            }
        }).addOnfailure(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                Log.d("Error: ", e.getMessage());
            }
        });
    }

    private void displayTextFromImage(FirebaseVisionText firebaseVis.onText) {
        List(FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks());
        if(blockList.size() == 0) {
            Toast.makeText(this, "No Text Found In Image", Toast.LENGTH_SHORT).show();
        }
        else {
            for(FirebaseVisionText.Block block : firebaseVisionText.getBlock()) {
                String text = block.getText();
                textView.setText(text);
            }
        }
    }

}