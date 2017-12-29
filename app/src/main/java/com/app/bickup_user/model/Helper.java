package com.app.bickup_user.model;

/**
 * Created by fluper-pc on 27/11/17.
 */

public class Helper {

    private String helper_person_count;
    private String helper_id;
    private String helper_price;


    public Helper(String helper_person_count, String helper_id, String helper_price) {
        this.helper_person_count = helper_person_count;
        this.helper_id = helper_id;
        this.helper_price = helper_price;
    }


    public String getHelper_person_count() {
        return helper_person_count;
    }

    public String getHelper_id() {
        return helper_id;
    }

    public String getHelper_price() {
        return helper_price;
    }

}
