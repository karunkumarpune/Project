package com.app.bickup_user.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.bickup_user.R;
import com.app.bickup_user.ZoomActivity;
import com.app.bickup_user.fragments.GoodsDetailsFragments;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.app.bickup_user.GlobleVariable.GloableVariable.is_check_image_product;

/**
 * Created by fluper-pc on 14/10/17.
 */

public class GoodsImagesAdapter extends RecyclerView.Adapter<GoodsImagesAdapter.MyViewHolder> {

    private Activity activity;
    private ArrayList<Bitmap> listImages;
    public GoodsImagesAdapter(Activity activity,ArrayList<Bitmap> listImages){
        this.activity=activity;
        this.listImages=listImages;
    }
/*
    public void getImage(){

    }*/

    public void notiFydata(Bitmap bitmap,int count)
    {
        if(count==listImages.size()){
            listImages.add(bitmap);
            notifyDataSetChanged();
        }else{
            listImages.set(count,bitmap);
            notifyItemChanged(count);
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView,crossImage;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.img_goods);
            crossImage = view.findViewById(R.id.cross_image);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_goods_image, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Bitmap bitmap = listImages.get(position);
        if (bitmap != null){
           // Bitmap scaledImage = setPic(holder.imageView, bitmap);
        //   holder.imageView.animate().rotation(90).start();

            holder.imageView.setImageBitmap(bitmap);


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            final byte[] byteArray = stream.toByteArray();
            holder.imageView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   Bundle bundle=new Bundle();
                   Intent intent =new Intent(activity, ZoomActivity.class);
                   bundle.putByteArray("byteArray",byteArray);
                   intent.putExtras(bundle);
                   activity.startActivity(intent);

               }
           });

          // holder.imageView.setImageResource(R.drawable.driver_2);
        }
            if(is_check_image_product==1){
                holder.crossImage.setVisibility(View.VISIBLE);
            }else holder.crossImage.setVisibility(View.GONE);


            holder.crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemRangeChanged(position, listImages.size());
                listImages.remove(position);
                notifyItemRemoved(position);
                GoodsDetailsFragments.imagecount--;

            }
        });

    }



    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return (null != listImages ? listImages.size() : 0);
    }

       private Bitmap setPic(ImageView mImageView,Bitmap bitmap) {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

      /*  // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(bitmap, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;*/
           Bitmap bitmap1 =Bitmap.createScaledBitmap(bitmap,targetW,targetH,false);
           return bitmap1;
    }


}
