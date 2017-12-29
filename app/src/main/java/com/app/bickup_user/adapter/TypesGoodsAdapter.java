package com.app.bickup_user.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bickup_user.R;
import com.app.bickup_user.model.GoodsAndHelper;

import java.util.ArrayList;

/**
 * Created by fluper-pc on 7/10/17.
 */

public class TypesGoodsAdapter extends RecyclerView.Adapter<TypesGoodsAdapter.MyViewHolder> {


    private Activity activity;
    private ArrayList<GoodsAndHelper.Goods> array;
    private LayoutInflater inflater;

    public TypesGoodsAdapter(Activity activity, ArrayList<GoodsAndHelper.Goods> array){
        this.array=array;
        this.activity=activity;
        inflater=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_types_goods, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.goodsName.setText(array.get(position).getGoodsName());
        holder.checkBoximage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean ischecked= (boolean) view.getTag();
                ImageView image=(ImageView)view;
                if(!ischecked){
                    image.setImageResource(R.drawable.ac_checkbox);
                    image.setTag(true);
                }else{
                    image.setImageResource(R.drawable.de_checkbox);
                    image.setTag(false);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != array ? array.size() : 0);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView goodsName;
        public ImageView checkBoximage;

        public MyViewHolder(View view) {
            super(view);
            goodsName=(TextView)view.findViewById(R.id.txt_good_name);
             checkBoximage=(ImageView)view.findViewById(R.id.img_row_checkbox);
        }
    }



          /*  rowView = inflater.inflate(R.layout.row_driver_contacts, null);
            TextView textView=(TextView)rowView.findViewById(R.id.driver_name);
            textView.setText(array.get(i).getGoodsName());
        //    ImageView imageView=(ImageView)rowView.findViewById(R.id.img_row_checkbox);
          //  imageView.setTag(false);*/

}
