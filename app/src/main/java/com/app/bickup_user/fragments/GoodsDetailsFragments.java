package com.app.bickup_user.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import com.app.bickup_user.adapter.GoodAddAdapter;
import com.app.bickup_user.adapter.GoodsImagesAdapter;
import com.app.bickup_user.controller.NetworkCallBack;
import com.app.bickup_user.controller.WebAPIManager;
import com.app.bickup_user.interfaces.HandlerGoodsNavigations;
import com.app.bickup_user.model.GoodsAddModel;
import com.app.bickup_user.model.GoodsAndHelper;
import com.app.bickup_user.model.User;
import com.app.bickup_user.select.MainActivity;
import com.app.bickup_user.utility.ConstantValues;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import com.stacktips.view.utils.CalendarUtils;
import com.xw.repo.BubbleSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class GoodsDetailsFragments extends Fragment implements View.OnClickListener, NetworkCallBack {

    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 1223;

    // Request code for camera
    private final int CAMERA_REQUEST_CODE = 100;

    // Request code for runtime permissions
    private final int REQUEST_CODE_STORAGE_PERMS = 321;

    private Uri file;
    private String mCurrentPhotoPath;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private  SharedPreferences Image_Sp;
    HandlerGoodsNavigations handlerGoodsNavigations;
    private  JSONArray array_ImageBitmap;
    private ArrayList<Bitmap> listImages;
    public static String TAG = GoodsDetailsFragments.class.getSimpleName();
    private GoodsActivity mGoodsActivity;
    private Activity mActivity;
    private Button btnSaveBooking;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private GoodsActivity mActivityReference;
    private ImageView imgOneHelper;
    private ImageView imgTwoHelper;
    private ImageView imgTickOneHelper;
    private ImageView imgTickTwoHelper;
    private EditText edtDescription;
    private TextView btnComeNow;
    private TextView btnComeLater;
    private ImageView imgUploadImage;
    private ImageView imghelperCheckBox;
    private ImageView imgTypesGoods;
    private RecyclerView recyclerView;
    private int REQUEST_GOODS = 100;
    private boolean isFirstTime = true;



    private CircularProgressView circularProgressBar;
    private String message = "";
    private GoodsAndHelper goodsAndHelper;
    private TextView txtdateTime;

    private RecyclerView types_good_recyclerView;
    private ArrayList<GoodsAddModel> lists;
    private GoodAddAdapter goodAddAdapter;
    private ArrayList<Bitmap> listImagesGoods;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int imagecount = 0;



    private String select_Data;
    private String Image_Data;
    private String image_id;
    private String add_date_time;
    private String add_date_time_time_stamp;
    private String add_mit="";
    private String add_hours="";
    private TextView txtdateTime_;
    private String current_Date;
    Bitmap bitmap1 = null, bitmap2 = null, bitmap3 = null, bitmap4 = null;
    private File imgFile;
    private String time_Stamp_month;
    private String time_Stamp_year;
    private String time_Stamp_day;
    private boolean isCheckButton=false;

    public GoodsDetailsFragments() {
        // Required empty public constructor
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Image_Sp = getActivity().getSharedPreferences("Image_Sp", 0);
        Image_Data=Image_Sp.getString("Image_Array","");
        image_id=Image_Sp.getString("image_id","");

        goodsAndHelper = new GoodsAndHelper();
        lists = new ArrayList<>();
        listImagesGoods = new ArrayList<>();

        Log.d("TAGS_setImageBitmap ","get Shared ImageBitmap  : "+ Image_Data);
        String s="";
        try {
            JSONArray a = new JSONArray(Image_Data);
            for(int i=0;i<a.length();i++) {
                File imgFile = new  File(a.optString(i));
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    listImagesGoods.add(myBitmap);
                    imagecount++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        if(image_id.equals("123")) {

        }else {
          //  listImagesGoods.add(bitmap1);
        }


        GloableVariable.Tag_helper="1";
        GloableVariable.Tag_Good_Details_Comming_time_type="1";

    }
    //--------------------------Calender-------------------------------
    private void showPopUp(int choosetraveller) {
        final Dialog openDialog = new Dialog(mActivityReference);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.calender_view_dialog);
        openDialog.setTitle("Custom Dialog Box");
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnCancel = (Button) openDialog.findViewById(R.id.btn_cancel);
        final CustomCalendarView calendarView = (CustomCalendarView) openDialog.findViewById(R.id.calendar_view);
        final Button btnok = (Button) openDialog.findViewById(R.id.btn_ok);


        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        add_date_time= new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        //Show Monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);


        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {


            @Override
            public void onDateSelected(Date date) {

                if (!CalendarUtils.isPastDay(date)) {

                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String dates=df.format(date);
                    add_date_time=dates;

                    btnok.setEnabled(true);
                } else {
                    btnok.setEnabled(false);
                    Toast.makeText(getActivity(),"Please select valid date!",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                //  Toast.makeText(getApplicationContext(), df.format(date), Toast.LENGTH_SHORT).show();
            }
        });


        //adding calendar day decorators
        List<DayDecorator> decorators = new ArrayList<>();
        decorators.add(new DisabledColorDecorator());
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);


        btnCancel.setTypeface(mTypefaceRegular);
        btnok.setTypeface(mTypefaceRegular);

        if (choosetraveller == 1) {
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
                showPopUpForTime(0);
            }
        });
        openDialog.show();

    }


    private void showPopUpForTime(int choosetraveller) {
        final Dialog openDialog = new Dialog(mActivityReference);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.time_dialog);
        openDialog.setTitle("Custom Dialog Box");
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final TextView fifteenMinuit = (TextView) openDialog.findViewById(R.id.fifteen_minuit);
        final TextView thirtyMinuit = (TextView) openDialog.findViewById(R.id.thirty_minuit);
        final TextView fourtyMinuit = (TextView) openDialog.findViewById(R.id.fourty_minuit);
        final TextView minuitText = (TextView) openDialog.findViewById(R.id.minuit_txt);
        final TextView hourtext = (TextView) openDialog.findViewById(R.id.hourtext);
        minuitText.setTypeface(mTypefaceRegular);
        fifteenMinuit.setTypeface(mTypefaceRegular);
        thirtyMinuit.setTypeface(mTypefaceRegular);
        fourtyMinuit.setTypeface(mTypefaceRegular);
        setColorWhite(fifteenMinuit);
        setColorWhite(thirtyMinuit);
        setColorWhite(fourtyMinuit);
        final com.xw.repo.BubbleSeekBar seekBar = (com.xw.repo.BubbleSeekBar) openDialog.findViewById(R.id.seekbar);
        add_hours="1";
        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                hourtext.setText(String.valueOf(progress));
                add_hours=String.valueOf(progress);
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        add_mit="15";
        fifteenMinuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minuitText.setText("15");
                add_mit="15";
                fifteenMinuit.setTextColor(getActivity().getResources().getColor(R.color.grey_text_color));
                thirtyMinuit.setTextColor(getActivity().getResources().getColor(R.color.greyColor));
                fourtyMinuit.setTextColor(getActivity().getResources().getColor(R.color.greyColor));
                setColorYellow(view);
                setColorWhite(thirtyMinuit);
                setColorWhite(fourtyMinuit);

            }


        });

        thirtyMinuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minuitText.setText("30");
                add_mit="30";

                thirtyMinuit.setTextColor(getActivity().getResources().getColor(R.color.grey_text_color));
                fifteenMinuit.setTextColor(getActivity().getResources().getColor(R.color.greyColor));
                fourtyMinuit.setTextColor(getActivity().getResources().getColor(R.color.greyColor));
                setColorYellow(view);
                setColorWhite(fifteenMinuit);
                setColorWhite(fourtyMinuit);
            }
        });

        fourtyMinuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorYellow(view);
                minuitText.setText("45");
                add_mit="45";
                fourtyMinuit.setTextColor(getActivity().getResources().getColor(R.color.grey_text_color));
                fifteenMinuit.setTextColor(getActivity().getResources().getColor(R.color.greyColor));
                thirtyMinuit.setTextColor(getActivity().getResources().getColor(R.color.greyColor));
                setColorWhite(thirtyMinuit);
                setColorWhite(fifteenMinuit);
            }
        });



        TextView travellerCost = (TextView) openDialog.findViewById(R.id.txt_traveller_cost);
        ImageView travellerImage = (ImageView) openDialog.findViewById(R.id.img_traveller);
        Button btnCancel = (Button) openDialog.findViewById(R.id.btn_cancel);

        Button btnok = (Button) openDialog.findViewById(R.id.btn_ok);
        btnCancel.setTypeface(mTypefaceRegular);
        btnok.setTypeface(mTypefaceRegular);
       /* travellerName.setTypeface(mTypefaceBold);
        travellerCost.setTypeface(mTypefaceRegular);
        btnDisagree.setTypeface(mTypefaceBold);
        btnAgree.setTypeface(mTypefaceBold);*/
        if (choosetraveller == 1) {

        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();

                Log.d("TAGS Time",add_date_time +"    "+add_hours+":"+ add_mit);
                GloableVariable.Tag_Good_Details_Comming_Date_time=add_date_time +" - "+add_hours+":"+ add_mit;
                String[] parsedate= add_date_time.split("/");
                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(parsedate[2]),
                        Integer.parseInt(parsedate[1]) - 1,
                        Integer.parseInt(parsedate[0]),
                        Integer.parseInt(add_hours),
                        Integer.parseInt(add_mit));
                long timestamp = c.getTimeInMillis();
                GloableVariable.Tag_Good_Details_Comming_Date_time_Stamp=timestamp;

                if (GloableVariable.Tag_Good_Details_Comming_time_type.equals("2")){

                    txtdateTime_.setVisibility(View.VISIBLE);
                    txtdateTime_.setText("Schedule Booking : "+GloableVariable.Tag_Good_Details_Comming_Date_time);


                   // Save();
                }


            }
        });
        openDialog.show();


        //   txtdateTime.setText(""+GloableVariable.Tag_Good_Details_Comming_Date_time);

    }

    private void setColorYellow(View view) {
        TextView view1 = (TextView) view;
        StateListDrawable bgShape = (StateListDrawable) view1.getBackground();
        bgShape.setColorFilter(Color.parseColor("#e6ba13"), PorterDuff.Mode.SRC_ATOP);
    }

    private void setColorWhite(TextView view) {
        StateListDrawable bgShape = (StateListDrawable) view.getBackground().mutate();
        bgShape.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
    }


    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            if (CalendarUtils.isPastDay(dayView.getDate())) {
                int color = Color.parseColor("#a9afb9");
                dayView.setBackgroundColor(color);
            }
        }
    }
//------------------------------------------------------------





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_goods_details_fragments, container, false);
        try {
            initializeViews(view);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GloableVariable.Tag_Good_Details_Description=edtDescription.getText().toString();

        return view;
    }

    private void initializeViews(View view) throws JSONException {

        types_good_recyclerView = view.findViewById(R.id.types_good_recyclerView);
        circularProgressBar = (CircularProgressView) view.findViewById(R.id.progress_view);
        mTypefaceRegular = Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_BOLD);
        setTypefaceToviews(view);

        imgOneHelper = (ImageView) view.findViewById(R.id.img_helper_single);
        imgTwoHelper = (ImageView) view.findViewById(R.id.img_double_helper);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerImages);
        setImagesList();
        AddGoodOption();

        imgTickOneHelper = (ImageView) view.findViewById(R.id.tick_single_helper);
        imgTickTwoHelper = (ImageView) view.findViewById(R.id.tick_double_helper);
        imghelperCheckBox = (ImageView) view.findViewById(R.id.check_box_img);
        imgTypesGoods = (ImageView) view.findViewById(R.id.img_types_goods);
        edtDescription = (EditText) view.findViewById(R.id.edt_description);
        btnComeNow = (TextView) view.findViewById(R.id.btn_come_now);
        btnComeLater = (TextView) view.findViewById(R.id.btn_come_later);
        imgUploadImage = (ImageView) view.findViewById(R.id.img_upload_image);

        imgOneHelper.setOnClickListener(this);
        imgUploadImage.setOnClickListener(this);
        imgOneHelper.setTag(R.drawable.ac_sing_helper);
        imgTwoHelper.setOnClickListener(this);
        imgTwoHelper.setTag(R.drawable.de_double_helper);
        btnComeNow.setOnClickListener(this);
        btnComeNow.setTag(true);
        btnComeLater.setOnClickListener(this);
        btnComeLater.setTag(false);


        btnComeNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Save();
                isCheckButton=true;

                btnComeNow.setBackgroundColor(mActivityReference.getResources().getColor(R.color.white));
                btnComeLater.setBackground(mActivityReference.getResources().getDrawable(R.drawable.sm_btn));

                btnComeNow.setTextColor(mActivityReference.getResources().getColor(R.color.grey_text_color));
                btnComeLater.setTextColor(mActivityReference.getResources().getColor(R.color.white));

                String date = new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(new Date());
                GloableVariable.Tag_Good_Details_Comming_Date_time=date;
                GloableVariable.Tag_Good_Details_Comming_time_type="1";
                txtdateTime_.setVisibility(View.VISIBLE);
                txtdateTime_.setText("Current Booking : "+date);

            }
        });


        imgTypesGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (goodsAndHelper.getGoods() != null) {
                    if (goodsAndHelper.getGoods().size() > 0) {
                      /*  Intent intent = new Intent(mActivityReference, TypesGoods.class);
                        intent.putExtra(TrackDriverMap.OPENTYPESGOODS, 0);
                        intent.putExtra(ConstantValues.GOODSDETAILS, goodsAndHelper);
                        startActivity(intent);*/
                        StringBuilder  sb=new StringBuilder();
                        for(GoodsAddModel m : lists){
                            sb.append(m.getId()+"~");
                        }
                        try {
                            sb.deleteCharAt(sb.length()-1);
                            Log.d("TAGS ","Responses StringBuilder: "+sb.toString());
                            startActivity(new Intent(mActivityReference, MainActivity.class).putExtra("key",sb.toString()).putExtra(ConstantValues.GOODSDETAILS, goodsAndHelper));
                        } catch (Exception e) {
                            startActivity(new Intent(mActivityReference, MainActivity.class).putExtra("key",sb.toString()).putExtra(ConstantValues.GOODSDETAILS, goodsAndHelper));
                        }





                    }
                }
            }

        });

        imghelperCheckBox.setOnClickListener(this);
        imghelperCheckBox.setTag(false);

        edtDescription.setTypeface(mTypefaceRegular);
        btnComeNow.setTypeface(mTypefaceRegular);
        btnComeLater.setTypeface(mTypefaceRegular);

        btnSaveBooking = (Button) view.findViewById(R.id.btn_save_booking);
        btnSaveBooking.setOnClickListener(this);
        btnSaveBooking.setTypeface(mTypefaceRegular);


        String date = new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(new Date());
        GloableVariable.Tag_Good_Details_Comming_Date_time=date;

        Long tsLong = System.currentTimeMillis()/1000;
        GloableVariable.Tag_Good_Details_Comming_Date_time_Stamp=tsLong;
        txtdateTime_.setText("Current Booking : "+date);



    }

    private void setImagesList() {
        GoodsImagesAdapter goodsImagesAdapter = new GoodsImagesAdapter(mActivityReference, listImagesGoods);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivityReference, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(goodsImagesAdapter);
    }

    private void setTypefaceToviews(View view) {
        TextView txtHelper = (TextView) view.findViewById(R.id.txt_helper);
        TextView txtChooseHelper = (TextView) view.findViewById(R.id.txt_choose_helper);
        TextView txtNohelperRequired = (TextView) view.findViewById(R.id.txt_no_helper_required);
        TextView txtDescription = (TextView) view.findViewById(R.id.txt_description);
        TextView txtComingTime = (TextView) view.findViewById(R.id.txt_coming_time);
        txtdateTime_ = (TextView) view.findViewById(R.id.txt_date_time);

        txtHelper.setTypeface(mTypefaceRegular);
        txtChooseHelper.setTypeface(mTypefaceRegular);
        txtComingTime.setTypeface(mTypefaceRegular);
        txtNohelperRequired.setTypeface(mTypefaceRegular);
        txtDescription.setTypeface(mTypefaceRegular);
        txtdateTime_.setTypeface(mTypefaceRegular);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getGoodsDetails();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference = (GoodsActivity) context;

    }

    @Override
    public void onResume() {
        super.onResume();


        Image_Sp = getActivity().getSharedPreferences("Image_Sp", 0);
        Image_Data=Image_Sp.getString("Image_Array","");
        image_id=Image_Sp.getString("image_id","");
        Log.d("TAGS_setImageBitmap ","get Shared ImageBitmap  : "+ Image_Data);


        GloableVariable.Tag_helper="1";
        SharedPreferences sp = getActivity().getSharedPreferences("LoginInfos", 0);
        select_Data= sp.getString("key", "");
        Log.d("TAGS Data:",select_Data);


        if(select_Data!=null){
            lists.clear();
            JSONArray array= null;
            try {
                array = new JSONArray(select_Data);

                for(int i=0;i<array.length();i++){
                    JSONObject obj=array.getJSONObject(i);
                    lists.add(new GoodsAddModel(obj.getString("id"),obj.getString("name")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        AddGoodOption();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View view) {
        handlerGoodsNavigations = mActivityReference;
        int id = view.getId();
        switch (id) {
            case R.id.btn_save_booking:

                if(!isCheckButton){
                    buildDialog(R.style.DialogAnimation, "Please choose Coming Time");
                }else
                Save();
               /* try {
                    listImagesGoods.remove(bitmap1);
                }catch (Exception e){}
                if(listImagesGoods.size()==0) {
                    Toast.makeText(getActivity(), "Please upload Image", Toast.LENGTH_SHORT).show();
                }else {
                    // Toast.makeText(getActivity(), "Good", Toast.LENGTH_SHORT).show();
                    handlerGoodsNavigations.callBookingDetailsFragment(listImagesGoods);
                    GloableVariable.Tag_Good_Details_Description=edtDescription.getText().toString();
                    //  ImageValidate();
                }

                if(listImagesGoods.size()>=1) {
                    ImageValidate();
                }*/

             /*   Log.d("TAGS","Helper   "+GloableVariable.Tag_helper);
                Log.d("TAGS","Descrtion  "+GloableVariable.Tag_Good_Details_Description);
                Log.d("TAGS","Comming_Type   "+GloableVariable.Tag_Good_Details_Comming_time_type);
                Log.d("TAGS","Comming_Date Time  "+GloableVariable.Tag_Good_Details_Comming_Date_time);
*/

                break;
            case R.id.btn_come_now:

                break;
            case R.id.btn_come_later:
                isCheckButton=true;
                btnComeLater.setBackgroundColor(mActivityReference.getResources().getColor(R.color.white));
                btnComeNow.setBackground(mActivityReference.getResources().getDrawable(R.drawable.sm_btn));

                btnComeLater.setTextColor(mActivityReference.getResources().getColor(R.color.grey_text_color));
                btnComeNow.setTextColor(mActivityReference.getResources().getColor(R.color.white));


                showPopUp(0);
                GloableVariable.Tag_Good_Details_Comming_time_type="2";
                break;
            case R.id.img_helper_single:
                if (!(boolean) imghelperCheckBox.getTag()) {
                    imgOneHelper.setImageResource(R.drawable.ac_sing_helper);
                    imgTwoHelper.setImageResource(R.drawable.de_double_helper);
                    imgTickTwoHelper.setVisibility(View.GONE);
                    imgTickOneHelper.setVisibility(View.VISIBLE);
                    imgOneHelper.setTag(true);
                    imgTwoHelper.setTag(false);
                    GloableVariable.Tag_helper="1";
                    helperDilog(getResources().getString(R.string.helper_info1));
                }
                break;
            case R.id.img_double_helper:
                if (!(boolean) imghelperCheckBox.getTag()) {
                    imgOneHelper.setImageResource(R.drawable.de_sing_helper);
                    imgTwoHelper.setImageResource(R.drawable.ac_double_helper);
                    imgTickTwoHelper.setVisibility(View.VISIBLE);
                    imgTickOneHelper.setVisibility(View.GONE);
                    imgOneHelper.setTag(false);
                    imgTwoHelper.setTag(true);
                    GloableVariable.Tag_helper="2";
                    helperDilog(getResources().getString(R.string.helper_info2));
                }
                break;
            case R.id.check_box_img:
                boolean ischecked = (boolean) imghelperCheckBox.getTag();
                if (!ischecked) {
                    imghelperCheckBox.setImageResource(R.drawable.ac_checkbox);
                    imgOneHelper.setImageResource(R.drawable.de_sing_helper);
                    imgTwoHelper.setImageResource(R.drawable.de_double_helper);
                    imgTickTwoHelper.setVisibility(View.GONE);
                    imgTickOneHelper.setVisibility(View.GONE);
                    imghelperCheckBox.setTag(true);
                    GloableVariable.Tag_helper="0";
                    helperDilog(getResources().getString(R.string.helper_info3));
                } else {
                    //helperDilog(getResources().getString(R.string.helper_info1));
                    imghelperCheckBox.setImageResource(R.drawable.de_checkbox);
                    imghelperCheckBox.setTag(false);
                    imgOneHelper.setImageResource(R.drawable.ac_sing_helper);
                    imgTwoHelper.setImageResource(R.drawable.de_double_helper);
                    imgTickTwoHelper.setVisibility(View.GONE);
                    imgTickOneHelper.setVisibility(View.VISIBLE);
                    imgOneHelper.setTag(true);
                    imgTwoHelper.setTag(false);

                }
                break;
            case R.id.img_upload_image:
                try {
                    listImagesGoods.remove(bitmap1);
                }catch (Exception e){}



                if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    if (!hasPermissions()){
                        // your app doesn't have permissions, ask for them.
                        requestNecessaryPermissions();
                    }
                    else {
                        // your app already have permissions allowed.
                        // do what you want.
                        openCamera();
                    }


                } else {
                    Toast.makeText(getActivity(), "Camera not supported", Toast.LENGTH_LONG).show();
                }
        }

    }
    private void buildDialog(int animationSource, String type) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setMessage(type);
        builder.setNegativeButton("OK", null);
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = animationSource;
        dialog.show();
    }
    private void Save(){
        handlerGoodsNavigations = mActivityReference;
        try {
            listImagesGoods.remove(bitmap1);
        }catch (Exception e){}
        if(listImagesGoods.size()==0) {
            Toast.makeText(getActivity(), "Please upload Image!!", Toast.LENGTH_SHORT).show();
        }else if(lists.size()==0) {
            Toast.makeText(getActivity(), "Please select type of goods!!", Toast.LENGTH_SHORT).show();
        }else {
            // Toast.makeText(getActivity(), "Good", Toast.LENGTH_SHORT).show();
            handlerGoodsNavigations.callBookingDetailsFragment(listImagesGoods);
            GloableVariable.Tag_Good_Details_Description=edtDescription.getText().toString();
            //  ImageValidate();
        }

        if(listImagesGoods.size()>=1) {
            ImageValidate();
        }else listImagesGoods.add(bitmap1);
    }


    //--------------------------------------------Camera Permisstion------------------------
    @SuppressLint("WrongConstant")
    private boolean hasPermissions() {
        int res = 0;
        // list all permissions which you want to check are granted or not.
        String[] permissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (String perms : permissions){
            res = getActivity().checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                // it return false because your app dosen't have permissions.
                return false;
            }

        }
        // it return true, your app has permissions.
        return true;
    }

    private void requestNecessaryPermissions() {
        // make array of permissions which you want to ask from user.
        String[] permissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // have arry for permissions to requestPermissions method.
            // and also send unique Request code.
            requestPermissions(permissions, REQUEST_CODE_STORAGE_PERMS);
        }
    }

    /* when user grant or deny permission then your app will check in
      onRequestPermissionsReqult about user's response. */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grandResults) {
        // this boolean will tell us that user granted permission or not.
        boolean allowed = true;
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMS:
                for (int res : grandResults) {
                    // if user granted all required permissions then 'allowed' will return true.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                // if user denied then 'allowed' return false.
                allowed = false;
                break;
        }
        if (allowed) {
            // if user granted permissions then do your work.
            openCamera();
        }
        else {
            // else give any custom waring message.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(getActivity(), "Camera Permissions denied", Toast.LENGTH_SHORT).show();
                }
                else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(getActivity(), "Storage Permissions denied", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }





//--------------------------------openCamera----------------

    private void openCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),file);
                // onCaptureImageResult(help1);
                ExifInterface ei = new ExifInterface(file.getPath());

                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap rotatedBitmap = null;
                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = rotateImage(bitmap, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = rotateImage(bitmap, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = rotateImage(bitmap, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        rotatedBitmap = bitmap;
                }
                GoodsImagesAdapter goodsImagesAdapter = (GoodsImagesAdapter) recyclerView.getAdapter();
                goodsImagesAdapter.notiFydata(getResizedBitmap(rotatedBitmap,400), imagecount);

                imagecount++;

            }catch (Exception e){

            }
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width/ (float) height;
        if (bitmapRatio < 1 && width > maxSize) {

            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else if(height > maxSize){
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private File getFile() {
        File folder = Environment.getExternalStoragePublicDirectory("/From_camera/imagens");// the file path
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+ timeStamp + "_";
        File image_file = null;
        try {
            image_file = File.createTempFile(imageFileName,".jpg",folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCurrentPhotoPath = image_file.getAbsolutePath();
        return image_file;
    }




//---------------------------------------------------End Camera Permisstion-----------------

    public void getGoodsDetails() {
        String createUserUrl = WebAPIManager.getInstance().getTypesGoods();
        final JsonObject requestBody = new JsonObject();
        callAPI(requestBody, createUserUrl, this, 60 * 1000, REQUEST_GOODS);
    }


    private void callAPI(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode) {
        circularProgressBar.setVisibility(View.VISIBLE);
        Ion.with(this)
                .load("GET", createUserUrl)
                .setHeader(ConstantValues.USER_ACCESS_TOKEN, User.getInstance().getAccesstoken())
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        circularProgressBar.setVisibility(View.GONE);
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int status = result.getHeaders().code();
                        JsonObject resultObject = result.getResult();
                        String value = String.valueOf(resultObject);
                        try {
                            JSONObject jsonObject = new JSONObject(value);
                            message = jsonObject.getString("message");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        switch (status) {
                            case 422:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 400:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                break;
                            case 200:
                            case 202:
                                loginActivity.onSuccess(resultObject, requestCode, status);
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onSuccess(JsonObject data, int requestCode, int statusCode) {
        ParseGoodsDetails parseSaveBooking = new ParseGoodsDetails();
        parseSaveBooking.execute(String.valueOf(data));
    }

    @Override
    public void onError(String msg) {

    }

    class ParseGoodsDetails extends AsyncTask<String, Object, GoodsAndHelper> {
        @Override
        protected GoodsAndHelper doInBackground(String... strings) {
            SharedPreferences sp;
            SharedPreferences.Editor editor;
            String message, flag = "0";
            GoodsAndHelper goodsAndHelper = new GoodsAndHelper();
            ArrayList<GoodsAndHelper.Goods> goodses = new ArrayList<>();
            ArrayList<GoodsAndHelper.Helper> helpers = new ArrayList<>();
            HashMap<String, String> map = new HashMap<>();
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                message = jsonObject.getString("message");
                flag = jsonObject.getString("flag");
                map.put("flag", flag);
                map.put("message", message);
                JSONObject data = jsonObject.getJSONObject("response");
                JSONArray goodsArray = data.getJSONArray("types_of_goods");
                JSONArray helperArray = data.getJSONArray("helper");

                sp = getActivity().getSharedPreferences("helper", 0);
                editor = sp.edit();
                editor.clear();
                editor.commit();
                editor.putString("key_helper", helperArray.toString());
                editor.commit();

                for (int i = 0; i < goodsArray.length(); i++) {
                    GoodsAndHelper.Goods goods = goodsAndHelper.createGoodsObject();
                    JSONObject jsonObject1 = goodsArray.getJSONObject(i);
                    goods.setGoodsID(jsonObject1.getString("goods_id"));
                    goods.setGoodsName(jsonObject1.getString("name"));
                    goods.setSelected(false);
                    goodses.add(goods);
                }
                for (int i = 0; i < helperArray.length(); i++) {
                    GoodsAndHelper.Helper helper = goodsAndHelper.createHelperObject();
                    JSONObject jsonObject1 = helperArray.getJSONObject(i);
                    helper.setHelperID(jsonObject1.getString("helper_id"));
                    helper.setHelperCount(jsonObject1.getString("person_count"));
                    helper.setPrice(jsonObject1.getString("price"));
                    helper.setSelected(false);
                    helpers.add(helper);
                }
                goodsAndHelper.setGoods(goodses);
                goodsAndHelper.setHelper(helpers);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return goodsAndHelper;
        }

        @Override
        protected void onPostExecute(GoodsAndHelper goodsAndHelper) {
            super.onPostExecute(goodsAndHelper);
            GoodsDetailsFragments.this.goodsAndHelper = goodsAndHelper;
        }
    }

    private void AddGoodOption() {

        goodAddAdapter = new GoodAddAdapter(mActivityReference, lists);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivityReference, LinearLayoutManager.HORIZONTAL, false);
        types_good_recyclerView.setLayoutManager(mLayoutManager);
        types_good_recyclerView.setItemAnimator(new DefaultItemAnimator());
        types_good_recyclerView.setAdapter(goodAddAdapter);
        goodAddAdapter.notifyDataSetChanged();

    }

    private void ImageValidate() {
        array_ImageBitmap=new JSONArray();

        for (int i = 0; i < listImagesGoods.size(); i++) {
            Bitmap b = listImagesGoods.get(i);
            if (b != null) {
                array_ImageBitmap.put(onCaptureImageResult(b));

            }
        }
        SharedPreferences.Editor editor_Image=Image_Sp.edit();
        editor_Image.putString("Image_Array",array_ImageBitmap.toString());
        editor_Image.putString("image_id","123");
        editor_Image.commit();
        Log.d("TAGS_setImageBitmap ","setImageBitmap  : "+array_ImageBitmap.toString());
    }

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

    private void helperDilog(String s ){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(s);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }
}