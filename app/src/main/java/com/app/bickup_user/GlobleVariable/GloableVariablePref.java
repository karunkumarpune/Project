/*
package com.app.bickup.GlobleVariable;

import android.content.Context;
import android.content.SharedPreferences;

public class GloableVariablePref {

    private static GloableVariablePref instance = null;

    private GloableVariablePref() {
    };


    public static GloableVariablePref getInstance(){
        if(instance==null){
            instance= new GloableVariablePref();
        }
        return instance;
    }

    public static final String PRE_NAME = "Bickup";


    public static final String PRE_pickup_location_address = "pickup_location_address";
    public static final String PRE_pickup_contact_name = "pickup_contact_name";
    public static final String PRE_pickup_home_type = "pickup_home_type";
    public static final String PRE_pickup_floor_number = "pickup_floor_number";
    public static final String PRE_pickup_comments = "pickup_comments";
    public static final String PRE_pickup_unit_number = "pickup_unit_number";
    public static final String PRE_pickup_contact_number = "pickup_contact_number";


    public static final String PRE_drop_location_address = "drop_location_address";
    public static final String PRE_drop_contact_name = "drop_contact_name";
    public static final String PRE_drop_floor_number = "drop_floor_number";
    public static final String PRE_drop_unit_number = "drop_unit_number";
    public static final String PRE_drop_comments = "drop_comments";
    public static final String PRE_drop_contact_number = "drop_contact_number";
    public static final String PRE_drop_home_type = "drop_home_type";

    public static final String PRE_helper = "helper";

    public static final String PRE_type_of_goods = "type_of_goods";

    public static final String PRE_pickup_time = "pickup_time";
    public static final String PRE_pickup_latitude = "pickup_latitude";
    public static final String PRE_pickup_longitude = "pickup_longitude";

    public static final String PRE_drop_latitude = "drop_latitude";
    public static final String PRE_drop_longitude = "drop_longitude";

    public static final String PRE_distance = "distance";
    public static final String PRE_pickup_time_type = "pickup_time_type";
    public static final String PRE_total_price = "total_price";


    public static final String PRE_paid_by_type = "paid_by_type";
    public static final String PRE_paid_by_name = "paid_by_name";
    public static final String PRE_paid_by_contact_number = "paid_by_contact_number";



    SharedPreferences preferences;

    public void addData(Context context, String mobile, String password, String user_id, String name, String email, String dob, String user_image) {
        this.preferences = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        editor.putString(PRE_Mobile, mobile.trim());
        editor.putString(PRE_pass, password.trim());


        editor.putString(PRE_USER_ID, user_id);
        editor.putString(PRE_USER_NAME, name);
        editor.putString(PRE_USER_Email, email);
        editor.putString(PRE_USER_DOB  , dob);
        editor.putString(PRE_USER_IMAGE_PIC, user_image);

        editor.commit();

    }


    public void removeData(SharedPreferences preferences) {

        SharedPreferences.Editor editor = preferences.edit().clear();
        editor.apply();
    }


    public SharedPreferences getLoginPreferences(Context context) {
        this.preferences = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        return preferences;
    }



    public boolean hasValue(SharedPreferences preferences) {
        if ((preferences.getString(PRE_Mobile, "").equalsIgnoreCase("")) && preferences.getString(PRE_pass, "").equalsIgnoreCase("")) {
            return true;
        } else {
            return false;
        }
    }


    public String getUID(SharedPreferences preferences) {
        if (preferences.contains(PRE_USER_ID)) {
            return preferences.getString(PRE_USER_ID, "");
        } else {
            return null;
        }
    }


    public String getUser_NAME(SharedPreferences preferences) {
        if (preferences.contains(PRE_USER_NAME)) {
            return preferences.getString(PRE_USER_NAME, "");
        } else {
            return null;
        }

    }


    public String getPRE_Mobile(SharedPreferences preferences) {
        if (preferences.contains(PRE_Mobile)) {
            return preferences.getString(PRE_Mobile, "");
        } else {
            return null;
        }
    }

    public String getPRE_Pass(SharedPreferences preferences) {
        if (preferences.contains(PRE_pass)) {
            return preferences.getString(PRE_pass, "");
        } else {
            return null;
        }
    }

    public String getPRE_USER_Email(SharedPreferences preferences) {
        if (preferences.contains(PRE_USER_Email)) {
            return preferences.getString(PRE_USER_Email, "");
        } else {
            return null;
        }

    }


    public String getPRE_DOB(SharedPreferences preferences) {
        if (preferences.contains(PRE_USER_DOB  )) {
            return preferences.getString(PRE_USER_DOB  , "");
        } else {
            return null;
        }

    }



    public String getUser_IMAGE(SharedPreferences preferences) {
        if (preferences.contains(PRE_USER_IMAGE_PIC)) {
            return preferences.getString(PRE_USER_IMAGE_PIC, "");
        } else {
            return null;
        }

    }






	*/
/*
    public String getSchool_Name(SharedPreferences preferences){
		if (preferences.contains(PRE_school_name) ) {
			return preferences.getString(PRE_school_name, "");
		}
		else {
			return null;
		}

	}


	public String getAddmission_no(SharedPreferences preferences){
		if (preferences.contains(PRE_addmission_no) ) {
			return preferences.getString(PRE_addmission_no, "");
		}
		else {
			return null;
		}

	}


	public String getMotherName(SharedPreferences preferences){
		if (preferences.contains(PRE_mother_name) ) {
			return preferences.getString(PRE_mother_name, "");
		}
		else {
			return null;
		}

	}

	public String getFatherName(SharedPreferences preferences){
		if (preferences.contains(PRE_father_name) ) {
			return preferences.getString(PRE_father_name, "");
		}
		else {
			return null;
		}
	}


	public String getStudentName(SharedPreferences preferences){
		if (preferences.contains(PRE_student_name) ) {
			return preferences.getString(PRE_student_name, "");
		}
		else {
			return null;
		}
	}

	public String getStudent_ID(SharedPreferences preferences){
		if (preferences.contains(PRE_student_id) ) {
			return preferences.getString(PRE_student_id, "");
		}
		else {
			return null;
		}

	}


	public String getClass_Name(SharedPreferences preferences){
		if (preferences.contains(PRE_class_name) ) {
			return preferences.getString(PRE_class_name, "");
		}
		else {
			return null;
		}
	}

	public String getSESSION_Name(SharedPreferences preferences){
		if (preferences.contains(PRE_section_name) ) {
			return preferences.getString(PRE_section_name, "");
		}
		else {
			return null;
		}
	}
*//*



*/
/*
    public boolean hasValue(SharedPreferences preferences){
		if (!(preferences.getString(PRE_USER_PASS, "").equalsIgnoreCase("") && preferences.getString(PRE_USER_NAME, "").equalsIgnoreCase("") )) {
			return true;
		}
		else {
			return false;
		}
	}*//*



    // set data

    //new LoginPrefencesParent().addData(LoginActivity.this, resultSet.getString(4), resultSet.getString(5), resultSet.getString(2));
    // set data

    //get data
//		LoginPrefencesParent prefences = new LoginPrefencesParent();
//		view.setText(prefences.getName(prefences.getLoginPreferences()));

}
*/
