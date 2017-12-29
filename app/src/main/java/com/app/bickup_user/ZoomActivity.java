package com.app.bickup_user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.github.chrisbanes.photoview.PhotoView;


public class ZoomActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zoom);

        ImageButton btn_close= (ImageButton) findViewById(R.id.btn_close);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("byteArray");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            onBackPressed();
            }
        });


        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
      //  photoView.setImageResource(R.drawable.image);

            photoView.setImageBitmap(bmp);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
