package com.app.bickup_user.GlobleVariable;

/**
 * Created by fluper-pc on 25/11/17.
 */

public class GloableVariable {


    private static GloableVariable instance = null;

    private GloableVariable() {
    };


    public static GloableVariable getInstance(){
        if(instance==null){
            instance= new GloableVariable();
        }
        return instance;
    }





    public static String Tag_drop_location_check="";

    public static String Tag_Is_device_token="";
    public static String Tag_pickup_location_address="";
    public static double Tag_pickup_latitude=0.0;
    public static double Tag_pickup_longitude=0.0;


    public static int Tag_check_locaton_type=0;

    public static String Tag_pickup_home_type="";
    public static String Tag_pickup_villa_no="";
    public static String Tag_pickup_building_name="";
    public static String Tag_pickup_floor_number="";
    public static String Tag_pickup_unit_number="";


    public static String Tag_pickup_Contact_type="";
    public static String Tag_pickup_contact_name="";
    public static String Tag_pickup_contact_number="";
    public static String Tag_pickup_comments="";



    public static String Tag_drop_location_address="";
    public static double Tag_drop_latitude=0.0;
    public static double Tag_drop_longitude=0.0;


    public static String Tag_drop_home_type="";
    public static String Tag_drop_villa_no="";
    public static String Tag_drop_building_name="";
    public static String Tag_drop_floor_number="";
    public static String Tag_drop_unit_number="";



    public static String Tag_drop_Contact_type="";
    public static String Tag_drop_contact_name="";
    public static String Tag_drop_contact_number="";
    public static String Tag_drop_comments="";




    public static String Tag_helper="";
    public static String Tag_Good_Details_Description="";

    public static String Tag_Good_Details_Comming_time_type="";
    public static String Tag_Good_Details_Comming_Date_time="";
    public static long Tag_Good_Details_Comming_Date_time_Stamp;




    public static String Tag_type_of_goods;


   /* public static String Tag_pickup_time="";
    Tag_Good_Details_Comming_time_type
*/


    public static String Tag_distance="";
    public static String Tag_total_price="";
    public static String Tag_total_final_prices="";
    public static String Tag_booking_id="";

    public static String Tag_paid_by_type="";
    public static String Tag_paid_by_name="";
    public static String paid_by_contact_number="";





}
