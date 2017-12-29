package com.app.bickup_user.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by fluper-pc on 9/11/17.
 */

public class GoodsAndHelper implements Parcelable {

    public ArrayList<Goods> goods;
    public ArrayList<Helper> helper;
    public GoodsAndHelper(Parcel in){
        this.goods = in.readArrayList(Goods.class.getClassLoader());
        this.helper = in.readArrayList(Helper.class.getClassLoader());
    }
    public GoodsAndHelper(){
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public GoodsAndHelper createFromParcel(Parcel in) {
            return new GoodsAndHelper(in);
        }

        public GoodsAndHelper[] newArray(int size) {
            return new GoodsAndHelper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(goods);
        parcel.writeList(helper);

    }

    public ArrayList<Helper> getHelper() {
        return helper;
    }

    public void setHelper(ArrayList<Helper> helper) {
        this.helper = helper;
    }

    public ArrayList<Goods> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<Goods> goods) {
        this.goods = goods;
    }

    public Goods createGoodsObject(){
        Goods goods=new Goods();
        return goods;
    }
    public Helper createHelperObject(){
        Helper helper=new Helper();
        return helper;
    }





   // Class for goods
    public static class Goods implements Parcelable{
        String goodsID;
        String goodsName;
       boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }



        public String getGoodsID() {
            return goodsID;
        }

        public void setGoodsID(String goodsID) {
            this.goodsID = goodsID;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

       public Goods(Parcel in){
           this.goodsID = in.readString();
           this.goodsName = in.readString();
           this.isSelected=  in.readInt() == 1;
       }
       public Goods(){

       }

       @Override
       public int describeContents() {
           return 0;
       }

       @Override
       public void writeToParcel(Parcel parcel, int i) {

           parcel.writeString(goodsID);
           parcel.writeString(goodsName);
           parcel.writeInt(isSelected ? 1 : 0);

       }
       public static Parcelable.Creator CREATOR = new Parcelable.Creator() {
           public Goods createFromParcel(Parcel in) {
               return new Goods(in);
           }

           public Goods[] newArray(int size) {
               return new Goods[size];
           }
       };
   }



    //Class for helper
    public static class Helper implements Parcelable{
        String helperID;
        String helperCount;
        String price;
        boolean isSelected;
        public Helper(Parcel in){
            this.helperID = in.readString();
            this.helperCount = in.readString();
            this.price = in.readString();
            this.isSelected=  in.readInt() == 1;
        }


        public  Helper(){

        }
        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getHelperID() {
            return helperID;
        }

        public void setHelperID(String helperID) {
            this.helperID = helperID;
        }

        public String getHelperCount() {
            return helperCount;
        }

        public void setHelperCount(String helperCount) {
            this.helperCount = helperCount;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(helperID);
            parcel.writeString(helperCount);
            parcel.writeString(price);
            parcel.writeInt(isSelected ? 1 : 0);

        }
        public static Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public Helper createFromParcel(Parcel in) {
                return new Helper(in);
            }

            public Helper[] newArray(int size) {
                return new Helper[size];
            }
        };
    }
}
