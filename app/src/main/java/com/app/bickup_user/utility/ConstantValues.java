package com.app.bickup_user.utility;

/**
 * Created by fluper-pc on 14/9/17.
 */

public class ConstantValues {


    // User Details
    public static  String USER_FIRSTNAME="first_name";
    public static  String USER_IMAGE="image_url";
    public static  String USER_LASTNAME="last_name";
    public static  String USER_MOBILENUMBER="phone_number";
    public static  String USER_EMAILADDRESS="email";
    public static  String USER_PASSWORD="password";
    public static  final String USER_ACCESS_TOKEN="access_token";
    public static  String USER_ID="user_id";
    public static  String COUNTRY_CODE="country_code";
    public static  String GOOGLE_ID="google_id";
    public static  String FACEBOOK_ID="fb_id";
    public static  String SOCIAL_TYPE="social_type";
    public static  String USER_PREFERENCES="userPreferences";
    public static  String ISverified="isverified_user";
    public static  String Is_device_token="";
    public static  String Is_Device_Key="device_token";
    public static  String ISLogin="islogin";

    public static String BASE_URL="http://18.221.82.73:3004";

    //permissions
    public static int PERMISSION_CURRENTLOCATION=1;
    public static int PERMISSION_READSTORAGE=2;
    public static int PERMISSION_WRITESTORAGE=3;
    public static int PERMISSION_INTERNET=4;


    public static final String KEY_CAMERA_POSITION = "camera_position";
    public static final String KEY_LOCATION = "location";


    //BroadcastActions
    public static String INTERNETACTION="android.provider.INTERNETCONECTION";


    // Basic constants
    public static boolean INTERNET_AVAILABLE=true;
    public static boolean INTERNET_NOT_AVAILABLE=false;

    public static final int GOOGLE_SIGN_IN=111;
    public static final int GOOGLE_SIGN_UP=222;


    //DataBase Constants
    public static String DATABASE_NAME="BickUpdb";
    public static int DATABASE_VERSION=1;
    public static String BOOKING_TABLE="bookingtable";


    public  static String BUILDING_NAME="building_name";
    public  static String FLOOR_NUMBER="floor_no";
    public  static String UNIT_NUMBER="unit_no";
    public  static String VILLA_NAME="villa_name";
    public  static String CONTACT_PERSON_NAME="location_contact_name";
    public  static String CONTACT_PERSON_NUMBER="location_contact_number";
    public  static String LATITUDE="latitude";
    public static String  LONGITUDE="longitude";
    public static String  ADDRESS="address";
    public static String  COMMENTS="comments";

    public static  String PICKUP_LATTITUDE="pickup_latitude";
    public static  String PICKUP_LONGITUDE="pickup_longitude";



    public static String  IS_RIDE_ACTIVE="ride_active";


    public  static String DROP_BUILDING_NAME="drop_building_name";
    public  static String DROP_FLOOR_NUMBER="drop_floor_no";
    public  static String DROP_UNIT_NUMBER="drop_unit_no";
    public  static String DROP_CONTACT_PERSON_NAME="drop_location_contact_name";
    public  static String DROP_CONTACT_PERSON_NUMBER="drop_location_contact_number";
    public  static String DROP_LATITUDE="drop_latitude";
    public static String  DROP_LONGITUDE="drop_longitude";
    public static String  DROP_ADDRESS="drop_address";
    public static String  DROP_COMMENTS="drop_comments";

    public static String  LOCATION_TYPE="is_location_type";
    public static String  LOCATION_CONTACT="is_location_contact";

    public static String  GOODSDETAILS="goods_details";

    public static String  CAR_NAME="small_car_name";
    public static String  CAR_TYPE="small_car_type";
    public static String  TOTAL_FARE="small_total_fare";
    public static String  TOTAL_Distance="small_total_distance";

    public static String  LARGE_CAR_NAME="large_car_name";
    public static String  LARGE_CAR_TYPE="large_car_type";
    public static String  LARGE_TOTAL_FARE="large_total_fare";
    public static String  LARGE_TOTAL_Distance="large_total_distance";





    public static String CHOOSE_PAGE="choosepage";
    public static final int FORGOT_PASSWORD=1;
    public static final int RESET_PASSWORD=2;
    public static final int OTP=3;

    public static final String  txt_OTP="password_otp";
    public static final String  txt_Verification_code="verification_code";


    public static String TYPEFACE_REGULAR="fonts/regular.ttf";
    public static String TYPEFACE_BOLD="fonts/bold.ttf";


    public static String CHANGE_NUMBER="changeNumber";

    public static String LOGINACTIVITYREQUEST="loginrequest";
}
