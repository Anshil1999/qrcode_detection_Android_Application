package com.example.qrcode_detection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

    ImageView img;
    Button click,detect;
    private int camera_request=100;
    Bitmap ima_rec;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img=findViewById(R.id.img);
        click=findViewById(R.id.click);
        detect=findViewById(R.id.detect);

        click.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,camera_request);
            }
        });

        detect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                detectCodes();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==camera_request) {
            ima_rec = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(ima_rec);
        }
    }
    void detectCodes()

    {

        FirebaseVisionImage image=FirebaseVisionImage.fromBitmap(ima_rec);
        FirebaseVisionBarcodeDetector detector= FirebaseVision.getInstance()
                .getVisionBarcodeDetector();

        detector.detectInImage(image).addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionBarcode>>() {
            @Override
            public void onComplete(@NonNull Task<List<FirebaseVisionBarcode>> task) {

                String outD="";
                for(FirebaseVisionBarcode bar:task.getResult())
                {
                    outD+=bar.getRawValue()+"\n\n";


                }
                Toast.makeText(MainActivity.this, ""+outD, Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}
