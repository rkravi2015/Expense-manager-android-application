package com.example.dell.rcplproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

public class ViewReceiptImageActivity extends AppCompatActivity {
   // int tripid,rowid;
    ImageView imageView;
    byte[] img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_receipt_image);
        imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setImageResource(android.R.drawable.ic_menu_report_image);
        Intent i=getIntent();

        Bundle b=i.getExtras();

        img =  b.getByteArray("img");

               try{ Bitmap b1 = BitmapFactory.decodeByteArray(img, 0, img.length);
                imageView.setImageBitmap(b1);}catch (Exception ae){
               }



    }
}
