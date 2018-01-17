package com.app.bickup_user.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickup_user.GlobleVariable.GloableVariable;
import com.app.bickup_user.GoodsActivity;
import com.app.bickup_user.R;
import com.app.bickup_user.TrackDriverActivity;
import com.app.bickup_user.adapter.GoodAddAdapter;
import com.app.bickup_user.adapter.GoodsImagesAdapter;
import com.app.bickup_user.controller.WebAPIManager;
import com.app.bickup_user.interfaces.GoodsImagesInterface;
import com.app.bickup_user.interfaces.HandlerGoodsNavigations;
import com.app.bickup_user.model.GoodsAddModel;
import com.app.bickup_user.model.Helper;
import com.app.bickup_user.model.User;
import com.app.bickup_user.utility.CommonMethods;
import com.app.bickup_user.utility.ConstantValues;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class BookingDetailsFragment extends Fragment implements View.OnClickListener, GoodsImagesInterface {

    private SharedPreferences Image_Sp;
    private SharedPreferences Image_Sp1;

    private  JSONArray array_ImageBitmap;
    public static String Tag = BookingDetailsFragment.class.getSimpleName();
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private GoodsActivity mActivityReference;
    private Button btnConfirmBooking;
    private EditText edtContactNumber;
    private EditText edtContactName;
    private TextView txtPickupLocation;
    private TextView txtDropLocation;
    private TextView txtPickupContactname;
    private TextView txtDropContactname;
    private TextView txtPickupContactNumber;
    private TextView txtDropContactNumber;
    private TextView txtDescription;
    private TextView txtPriceWithHelper;
    private TextView txtTotalAmount;
    private TextView btnPaidByMe;
    private TextView btnOther;
    private TextView txtHelerString;
    private ArrayList<Bitmap> listImages;
    private RecyclerView recyclerView;

    private RecyclerView types_good_recyclerView;
    private ArrayList<GoodsAddModel> lists;
    private GoodAddAdapter goodAddAdapter;
    private String response;
    private ArrayList<String> types_good;

    private String select_Data;
    private int helper_check=0;
    private List<Helper> list_helper;
    private String helper_json,helper_prices;
    private SharedPreferences sp_heper;

    private ImageView img_helpers;
    private String helper_id = "", helper_price = "", helper_person_count = "";
    private double Total_prices=0.0;
    private CoordinatorLayout mCoordinatorLayout;
    private CircularProgressView circularProgressView;
    private  JSONArray array_list_helper;
    private  String data;
    private  String list_no_of_helper;
    private JSONArray typesOfGoodArray;

    public BookingDetailsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lists = new ArrayList<>();
        list_helper = new ArrayList<>();
        GloableVariable.Tag_paid_by_type = "1";

        Image_Sp = getApplicationContext().getSharedPreferences("Image_Sp", 0);
        Image_Sp1 = getApplicationContext().getSharedPreferences("LoginInfos", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_detail, container, false);
        initializeViews(view);

        return view;
    }

    private void setImagesList() {
        Bitmap bitmap1 = null, bitmap2 = null, bitmap3 = null, bitmap4 = null;
        if (listImages != null) {
            GoodsImagesAdapter goodsImagesAdapter = new GoodsImagesAdapter(mActivityReference, listImages);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivityReference, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(goodsImagesAdapter);
        }


    }

    @Override
    public void onResume() {
        super.onResume();

//      ---------------------------------------------------Helper--------------------------------
        sp_heper = getActivity().getSharedPreferences("helper", 0);
        helper_json = sp_heper.getString("key_helper", "");
        Log.d("TAGS Data:", "helper_json" + helper_json);

        try {

            JSONArray arr = new JSONArray(helper_json.toString());
            for (int i = 0; i < helper_json.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                list_helper.add(new Helper(obj.getString("person_count"), obj.getString("helper_id"), obj.getString("price")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

//---------------------------------Type of Goods------------------------------------

        SharedPreferences sp = getActivity().getSharedPreferences("LoginInfos", 0);
        select_Data = sp.getString("key", "");
        Log.d("TAGS Data:", select_Data);


        if (select_Data != null) {
            lists.clear();
            JSONArray array = null;
            try {
                array = new JSONArray(select_Data);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    lists.add(new GoodsAddModel(obj.getString("id"), obj.getString("name")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        typesOfGoodArray = new JSONArray();
        types_good = new ArrayList<>();
        for (GoodsAddModel n : lists) {
            types_good.add(n.getName());
            typesOfGoodArray.put(n.getName());
        }



        //------------------------------------------------------------------------------------------
    }

    private void initializeViews(View view) {

        types_good_recyclerView = view.findViewById(R.id.types_good_recyclerView);
        mCoordinatorLayout=view.findViewById(R.id.cordinatorlayout);
        circularProgressView=view.findViewById(R.id.progress_view);

        mTypefaceRegular = Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_BOLD);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerImages_booking);
        setImagesList();
        AddGoodOption();

        btnConfirmBooking = (Button) view.findViewById(R.id.btn_confirm_booking);
        btnPaidByMe = (TextView) view.findViewById(R.id.btn_paid_by_me);
        btnOther = (TextView) view.findViewById(R.id.paid_by_other);
        btnPaidByMe.setOnClickListener(this);
        btnOther.setOnClickListener(this);
        btnConfirmBooking.setOnClickListener(this);
        btnConfirmBooking.setOnClickListener(this);

        btnPaidByMe.setOnClickListener(this);
        btnOther.setOnClickListener(this);
        btnPaidByMe.setTag(false);
        btnOther.setTag(true);
        btnConfirmBooking.setTypeface(mTypefaceRegular);
        btnConfirmBooking.setTypeface(mTypefaceRegular);
        btnPaidByMe.setTypeface(mTypefaceRegular);
        btnOther.setTypeface(mTypefaceRegular);


        img_helpers = view.findViewById(R.id.img_helpers);


        edtContactName = (EditText) view.findViewById(R.id.edt_contact_person_name);
        edtContactNumber = (EditText) view.findViewById(R.id.edt_contact_person_number);
        txtPickupContactname = (TextView) view.findViewById(R.id.value_pickup_contact_name);
        txtPickupContactNumber = (TextView) view.findViewById(R.id.value_pickup_contact_number);
        txtPickupLocation = (TextView) view.findViewById(R.id.value_pickup_location);

        edtContactNumber.setTypeface(mTypefaceRegular);
        edtContactName.setTypeface(mTypefaceRegular);
        txtPickupContactNumber.setTypeface(mTypefaceRegular);
        txtPickupContactname.setTypeface(mTypefaceRegular);
        txtPickupLocation.setTypeface(mTypefaceRegular);

        txtDropContactname = (TextView) view.findViewById(R.id.value_drop_contact_name);
        txtDropContactNumber = (TextView) view.findViewById(R.id.value_drop_contact_number);
        txtDropLocation = (TextView) view.findViewById(R.id.value_drop_location);

        txtDropContactNumber.setTypeface(mTypefaceRegular);
        txtDropContactname.setTypeface(mTypefaceRegular);
        txtDropLocation.setTypeface(mTypefaceRegular);

        txtDescription = (TextView) view.findViewById(R.id.value_description);
        txtPriceWithHelper = (TextView) view.findViewById(R.id.value_amount_string);
        txtTotalAmount = (TextView) view.findViewById(R.id.value_total_string);
        txtHelerString = (TextView) view.findViewById(R.id.txt_helper_string);

        txtDescription.setTypeface(mTypefaceRegular);
        txtPriceWithHelper.setTypeface(mTypefaceRegular);
        txtTotalAmount.setTypeface(mTypefaceBold);
        txtHelerString.setTypeface(mTypefaceRegular);


        txtPickupLocation.setText(GloableVariable.Tag_pickup_location_address);
        txtPickupContactname.setText(GloableVariable.Tag_pickup_contact_name);
        txtPickupContactNumber.setText(GloableVariable.Tag_pickup_contact_number);


        txtDropLocation.setText(GloableVariable.Tag_drop_location_address);
        txtDropContactname.setText(GloableVariable.Tag_drop_contact_name);
        txtDropContactNumber.setText(GloableVariable.Tag_drop_contact_number);


        if(GloableVariable.Tag_paid_by_type.equals("1")) {
            edtContactName.setText(GloableVariable.Tag_pickup_contact_name);
            edtContactNumber.setText(GloableVariable.Tag_pickup_contact_number);
        }


        sp_heper = getActivity().getSharedPreferences("helper", 0);
        helper_json = sp_heper.getString("key_helper", "");
        try {

            JSONArray arr = new JSONArray(helper_json.toString());
            for (int i = 0; i < helper_json.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                list_helper.add(new Helper(obj.getString("person_count"), obj.getString("helper_id"), obj.getString("price")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        String help = GloableVariable.Tag_helper;

        if (help.equals("2")) {
            helper_check=2;
            img_helpers.setVisibility(View.VISIBLE);
            img_helpers.setImageResource(R.drawable.ac_double_helper);

            ArrayList<HashMap<String, String>>  mylist =new ArrayList<>();
            HashMap<String, String> map;
            for (Helper h : list_helper) {
                if (h.getHelper_person_count().equals("2")) {
                    map = new HashMap();
                    map.put("helper_id", h.getHelper_id());
                    map.put("price", h.getHelper_price());
                    helper_prices = h.getHelper_price();
                    mylist.add(map);
                }
            }
            data = new Gson().toJson(mylist);
            Log.d("TAGS-","helper_id 2:"+ data);
        }


        if (help.equals("1")) {
            helper_check=1;
            img_helpers.setVisibility(View.VISIBLE);
            img_helpers.setImageResource(R.drawable.sing_helper);

            ArrayList<HashMap<String, String>>  mylist =new ArrayList<>();
            HashMap<String, String> map;
            for (Helper h : list_helper) {
                if (h.getHelper_person_count().equals("1")) {
                    map = new HashMap();
                        map.put("helper_id", h.getHelper_id());
                        map.put("price", h.getHelper_price());
                        helper_prices = h.getHelper_price();
                        mylist.add(map);
                    }
                }
             data = new Gson().toJson(mylist);
            Log.d("TAGS-","helper_id 1:"+ data);
            }


        if (help.equals("0")) {
            helper_check=0;
            array_list_helper=new JSONArray();
            img_helpers.setVisibility(View.GONE);
            helper_prices= String.valueOf(0.00);

            ArrayList<String>  mylistt =new ArrayList<>();
            data = new Gson().toJson(mylistt);
            Log.d("TAGS-","helper_id 0:"+ data);

        }



        ArrayList<HashMap<String, String>>  mylist_no_of_helper =new ArrayList<>();
        HashMap<String, String> map;
        map = new HashMap();
        map.put("no_of_helpers", String.valueOf(helper_check));
        mylist_no_of_helper.add(map);
        list_no_of_helper = new Gson().toJson(mylist_no_of_helper);
        Log.d("TAGS-","helper_id :"+ list_no_of_helper);



        txtDescription.setText(GloableVariable.Tag_Good_Details_Description);
        setTypeFaces(view);
        double value=0.0;
        double values22=0.0;
        double value2=0.0;
        double  values=0.0;
        try {
             value = Double.parseDouble(GloableVariable.Tag_total_price);
             values = Double.parseDouble(new DecimalFormat("#.##").format(value));
          //  Log.d("TAGS", String.valueOf(values));
        }catch (Exception e){}

        try {

             value2 = Double.parseDouble(helper_prices);
            values22 = Double.parseDouble(new DecimalFormat("#.##").format(value2));
            // Log.d("TAGS", String.valueOf(values22));}
        }
       catch (Exception e){}

        Total_prices=values+values22;
        txtPriceWithHelper.setText("$ "+String.valueOf(values+" +$"+helper_prices));
        txtTotalAmount.setText("= $"+Total_prices);
        GloableVariable.Tag_total_final_prices=""+Total_prices;

      //  Validate();




    }

    private void setTypeFaces(View view) {
        TextView txtPickupLocation = (TextView) view.findViewById(R.id.label_pickup_location);
        TextView txtPickupContact = (TextView) view.findViewById(R.id.label_pickup_contact);
        TextView txtDropLocation = (TextView) view.findViewById(R.id.label_drop_location);
        TextView txtDropContact = (TextView) view.findViewById(R.id.label_drop_contact);
        TextView txtLabelOfNumberHelper = (TextView) view.findViewById(R.id.label_number_of_helpers);
        // TextView txtContactPersonName=(TextView)view.findViewById(R.id.label_contact_person_name);
        // TextView txtContactPersonNumber=(TextView)view.findViewById(R.id.label_contact_person_number);
        TextView txtAmountDetails = (TextView) view.findViewById(R.id.label_amount_details);
        TextView txtDescription = (TextView) view.findViewById(R.id.label_description);
        TextView txtTypeDescription = (TextView) view.findViewById(R.id.label_types_of_goods);

        txtPickupLocation.setTypeface(mTypefaceRegular);
        txtPickupContact.setTypeface(mTypefaceRegular);
        txtAmountDetails.setTypeface(mTypefaceRegular);
        txtDropLocation.setTypeface(mTypefaceRegular);
        txtDropLocation.setTypeface(mTypefaceRegular);
        txtDropContact.setTypeface(mTypefaceRegular);
        txtLabelOfNumberHelper.setTypeface(mTypefaceRegular);
        //  txtContactPersonName.setTypeface(mTypefaceRegular);
        //  txtContactPersonNumber.setTypeface(mTypefaceRegular);
        txtDescription.setTypeface(mTypefaceRegular);
        txtTypeDescription.setTypeface(mTypefaceRegular);

    }


    private boolean validateFields() {
        if (!CommonMethods.getInstance().validateEditFeild(edtContactName.getText().toString())) {
            Toast.makeText(mActivityReference, mActivityReference.getResources().getString(R.string.txt_vaidate_contact_person_name), Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!CommonMethods.getInstance().validateEditFeild(edtContactNumber.getText().toString())) {
            Toast.makeText(mActivityReference, mActivityReference.getResources().getString(R.string.txt_vaidate_mobile), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (!CommonMethods.getInstance().validateMobileNumber(edtContactNumber.getText().toString(), 6)) {
                Toast.makeText(mActivityReference, mActivityReference.getResources().getString(R.string.txt_vaidate_mobile_number), Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference = (GoodsActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void showPopUp(String s) {
        GloableVariable.Tag_booking_id=s;
        final Dialog openDialog = new Dialog(mActivityReference);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.booking_confirmation_dialog);
        openDialog.setTitle("Custom Dialog Box");
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
      /*  TextView travellerName = (TextView)openDialog.findViewById(R.id.txt_traveller_name_dialog);

        TextView travellerCost = (TextView)openDialog.findViewById(R.id.txt_traveller_cost);
        ImageView travellerImage = (ImageView)openDialog.findViewById(R.id.img_traveller);
        Button btnDone = (Button)openDialog.findViewById(R.id.btn_done);
*/
        Button btnAgree = openDialog.findViewById(R.id.btn_done);
        TextView edt_pickup_location = openDialog.findViewById(R.id.edt_pickup_location);
        TextView edt_drop_location = openDialog.findViewById(R.id.edt_drop_location);
        TextView txt_total = openDialog.findViewById(R.id.txt_total);
        TextView txt_booking_id = openDialog.findViewById(R.id.txt_booking_id);

        edt_pickup_location.setText(GloableVariable.Tag_pickup_location_address);
        edt_drop_location.setText(GloableVariable.Tag_drop_location_address);
        txt_total.setText("Total: $"+GloableVariable.Tag_total_final_prices);
        String ss=  GloableVariable.Tag_booking_id;
        if(ss==null){
            txt_booking_id.setText("");
        }else
        txt_booking_id.setText("#"+ss);


        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
                Clear_Preference();
                Intent intent = new Intent(mActivityReference, TrackDriverActivity.class);
                startActivity(intent);

            }
        });
      /*  btnAgree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
                Intent intent=new Intent(DriverActivity.this,GoodsActivity.class);
                startActivity(intent);
            }
        });*/
        openDialog.show();

    }

    @Override
    public void onClick(View view) {
        HandlerGoodsNavigations handlerGoodsNavigations = mActivityReference;
        int id = view.getId();
        switch (id) {
            case R.id.btn_confirm_booking:
                if (validateFields()) {

                    GloableVariable.Tag_paid_by_name = edtContactName.getText().toString();
                    GloableVariable.paid_by_contact_number = edtContactNumber.getText().toString();

            /*        Log.d("TAGS", "Paid price: " + GloableVariable.Tag_total_price);
                    Log.d("TAGS", "Paid type: " + GloableVariable.Tag_paid_by_type);
                    Log.d("TAGS", "Paid name: " + GloableVariable.Tag_paid_by_name);
                    Log.d("TAGS", "Paid num: " + GloableVariable.paid_by_contact_number);

                    Log.d("TAGS", "Paid Good type: " + GloableVariable.Tag_type_of_goods);
*/

                    editProfile();

                }
                break;
            case R.id.btn_paid_by_me:
                btnPaidByMe.setBackground(mActivityReference.getResources().getDrawable(R.drawable.sm_btn));
                btnOther.setBackgroundColor(mActivityReference.getResources().getColor(R.color.white));
                btnPaidByMe.setTextColor(mActivityReference.getResources().getColor(R.color.white));
                btnOther.setTextColor(mActivityReference.getResources().getColor(R.color.grey_text_color));
                btnPaidByMe.setTag(true);
                btnOther.setTag(false);

                GloableVariable.Tag_paid_by_type = "1";


                edtContactName.setText(GloableVariable.Tag_pickup_contact_name);
                edtContactNumber.setText(GloableVariable.Tag_pickup_contact_number);


                break;
            case R.id.paid_by_other:
                btnPaidByMe.setBackgroundColor(mActivityReference.getResources().getColor(R.color.white));
                btnOther.setBackground(mActivityReference.getResources().getDrawable(R.drawable.sm_btn));
                btnPaidByMe.setTextColor(mActivityReference.getResources().getColor(R.color.grey_text_color));
                btnOther.setTextColor(mActivityReference.getResources().getColor(R.color.white));
                btnPaidByMe.setTag(false);
                btnOther.setTag(true);
                GloableVariable.Tag_paid_by_type = "2";
                edtContactName.setText("");
                edtContactNumber.setText("");

                break;

        }
    }


    @Override
    public void setImagelist(ArrayList<Bitmap> listimage) {
        this.listImages = listimage;
    }

    private void AddGoodOption() {

        goodAddAdapter = new GoodAddAdapter(mActivityReference, lists);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivityReference, LinearLayoutManager.HORIZONTAL, false);
        types_good_recyclerView.setLayoutManager(mLayoutManager);
        types_good_recyclerView.setItemAnimator(new DefaultItemAnimator());
        types_good_recyclerView.setAdapter(goodAddAdapter);
    }


    private void Validate() {
         array_ImageBitmap=new JSONArray();
        for (int i = 0; i < listImages.size(); i++) {
            Bitmap b = listImages.get(i);
            if (b != null) {
            //    imgFile1 = onCaptureImageResult(b);
                array_ImageBitmap.put(onCaptureImageResult(b));
              //  m1.setImageBitmap(b);
            }
        }
      //  Log.d("TAGS","setImageBitmap  : "+array_ImageBitmap.toString());
    }
         /*  if (i == 1) {

                if (b != null) {
                    imgFile2 = onCaptureImageResult(b);
                    m2.setImageBitmap(b);

                }
            }*/


    private File onCaptureImageResult(Bitmap bitmap) {
        File imgFile;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        imgFile = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            imgFile.createNewFile();
            fo = new FileOutputStream(imgFile);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgFile;
    }


    private void editProfile() {

       String sss="";
       if(GloableVariable.Tag_distance ==null || GloableVariable.Tag_distance==""){
           sss="0";
       }else sss= GloableVariable.Tag_distance;


        final String[] message = new String[1];
        circularProgressView.setVisibility(View.VISIBLE);

        Builders.Any.B builder = Ion.with(getActivity()).load("POST", WebAPIManager.get_url_Ride).setLogging("", Log.ERROR);
        builder.setHeader(ConstantValues.USER_ACCESS_TOKEN, User.getInstance().getAccesstoken());

       //---------------------------Type of goods----------
                /*for (int i = 0; i < types_good.size(); i++) {
                builder.setMultipartParameter("type_of_goods", types_good.get(i));
                }*/
                builder.setMultipartParameter("type_of_goods", typesOfGoodArray.toString());

       //--------------------No of Helper----------------------------


            builder.setMultipartParameter("helper", data);

  //--------------------No of Helpers----------------------------

            builder.setMultipartParameter("no_of_helpers", String.valueOf(helper_check));

        //--------------maltipart image................

            for (int i = 0; i < listImages.size(); i++) {
            Bitmap b = listImages.get(i);
            if (b != null) {
                builder.setMultipartFile("file", onCaptureImageResult(b));
            }}


           builder.setMultipartParameter("pickup_location_address", GloableVariable.Tag_pickup_location_address)
                .setMultipartParameter("pickup_contact_name", GloableVariable.Tag_pickup_contact_name)
                .setMultipartParameter("pickup_contact_number", GloableVariable.Tag_pickup_contact_number)
                .setMultipartParameter("pickup_comments", GloableVariable.Tag_pickup_comments)

                .setMultipartParameter("pickup_home_type", GloableVariable.Tag_pickup_home_type)
                .setMultipartParameter("pickup_villa_name", GloableVariable.Tag_pickup_villa_no)

                .setMultipartParameter("pickup_building_name", GloableVariable.Tag_pickup_building_name)
                .setMultipartParameter("pickup_floor_number", GloableVariable.Tag_pickup_floor_number)
                .setMultipartParameter("pickup_unit_number", GloableVariable.Tag_pickup_unit_number)


                .setMultipartParameter("pickup_latitude", String.valueOf(GloableVariable.Tag_pickup_latitude))
                .setMultipartParameter("pickup_longitude", String.valueOf(GloableVariable.Tag_pickup_longitude))


                .setMultipartParameter("drop_latitude", String.valueOf(GloableVariable.Tag_drop_latitude))
                .setMultipartParameter("drop_longitude", String.valueOf(GloableVariable.Tag_drop_longitude))


                .setMultipartParameter("drop_location_address", GloableVariable.Tag_drop_location_address)
                .setMultipartParameter("drop_contact_name", GloableVariable.Tag_drop_contact_name)
                .setMultipartParameter("drop_contact_number", GloableVariable.Tag_drop_contact_number)
                .setMultipartParameter("drop_comments", GloableVariable.Tag_drop_comments)

                .setMultipartParameter("drop_home_type", GloableVariable.Tag_drop_home_type)
                .setMultipartParameter("drop_villa_name", GloableVariable.Tag_drop_villa_no)

                .setMultipartParameter("drop_building_name", GloableVariable.Tag_drop_building_name)
                .setMultipartParameter("drop_floor_number", GloableVariable.Tag_drop_floor_number)
                .setMultipartParameter("drop_unit_number", GloableVariable.Tag_drop_unit_number)
                .setMultipartParameter("ride_description", GloableVariable.Tag_Good_Details_Description)

                .setMultipartParameter("distance", sss)

                .setMultipartParameter("pickup_time_type", GloableVariable.Tag_Good_Details_Comming_time_type)

                .setMultipartParameter("pickup_time", String.valueOf(GloableVariable.Tag_Good_Details_Comming_Date_time_Stamp))

                .setMultipartParameter("total_price", String.valueOf(Total_prices))

                .setMultipartParameter("paid_by_type", GloableVariable.Tag_paid_by_type)
                .setMultipartParameter("paid_by_name", GloableVariable.Tag_paid_by_name)
                .setMultipartParameter("paid_by_contact_number", GloableVariable.paid_by_contact_number)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        circularProgressView.setVisibility(View.GONE);
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int status = result.getHeaders().code();
                        JsonObject resultObject = result.getResult();
                        String value = String.valueOf(resultObject);
                        try {
                            JSONObject jsonObject = new JSONObject(value);

                            try {
                                JSONObject sss=jsonObject.getJSONObject("response");
                                response=sss.getString("ride_id");
                            }catch (Exception c){}
                            message[0] = jsonObject.getString("message");
                            Log.d("TAGS", message.toString());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        switch (status) {
                            case 422:
                                Toast.makeText(getApplicationContext(), message[0], Toast.LENGTH_SHORT).show();
                                break;
                            case 400:
                                Toast.makeText(getApplicationContext(), message[0], Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(getApplicationContext(), message[0], Toast.LENGTH_SHORT).show();
                                break;
                            case 200:
                               showPopUp(response);
                            case 202:
                                // loginActivity.onSuccess(resultObject, requestCode, status);
                                break;
                            case 201:
                               // showPopUp(response);
                                Toast.makeText(getApplicationContext(),getResources().getString(R.string.txt_driver_201), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), message[0], Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    private void Clear_Preference(){

        SharedPreferences.Editor editor=Image_Sp.edit();
        editor.clear();
        editor.commit();

        SharedPreferences.Editor editor1=Image_Sp1.edit();
        editor1.clear();
        editor1.commit();

        GloableVariable.Tag_drop_location_check="";
        GloableVariable.Tag_pickup_location_address="";

        GloableVariable.Tag_pickup_home_type="";
        GloableVariable.Tag_pickup_villa_no="";
        GloableVariable.Tag_pickup_building_name="";
        GloableVariable.Tag_pickup_floor_number="";
        GloableVariable.Tag_pickup_unit_number="";





        GloableVariable.Tag_pickup_comments="";



        GloableVariable.Tag_drop_location_address="";



        GloableVariable.Tag_drop_home_type="";
        GloableVariable.Tag_drop_villa_no="";
        GloableVariable.Tag_drop_building_name="";
        GloableVariable.Tag_drop_floor_number="";
        GloableVariable.Tag_drop_unit_number="";






        GloableVariable.Tag_drop_comments="";




        GloableVariable.Tag_helper="";
        GloableVariable.Tag_Good_Details_Description="";

        GloableVariable.Tag_Good_Details_Comming_time_type="";
        GloableVariable.Tag_Good_Details_Comming_Date_time="";

        GloableVariable.Tag_type_of_goods="";

        GloableVariable.Tag_distance="";
        GloableVariable.Tag_total_price="";
        GloableVariable.Tag_total_final_prices="";
        GloableVariable.Tag_booking_id="";



    }
}


