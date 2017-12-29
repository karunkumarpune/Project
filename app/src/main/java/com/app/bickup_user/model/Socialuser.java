package com.app.bickup_user.model;

/**
 * Created by fluper-pc on 31/10/17.
 */

public class Socialuser {
    private String firstName=null;
    private String socialtype=null;
    private String lastName=null;
    private String email=null;
    private String countryCode="+91";

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFacebooID() {
        return facebooID;
    }

    public void setFacebooID(String facebooID) {
        this.facebooID = facebooID;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    private String mobileNumber=null;
    private String facebooID="";
    private String googleID="";

    public String getSocialtype() {
        return socialtype;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setSocialtype(String socialtype) {

        this.socialtype = socialtype;
    }

    public static Socialuser getOurInstance() {
        return ourInstance;
    }

    private String userId=null;
    private String image=null;
    private static final Socialuser ourInstance = new Socialuser();

    public static Socialuser getInstance() {
        return ourInstance;
    }

    private Socialuser() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
