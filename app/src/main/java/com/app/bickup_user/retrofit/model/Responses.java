package com.app.bickup_user.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Responses {

@SerializedName("date")
@Expose
private String date;
@SerializedName("ride")
@Expose
private List<Ride> ride = null;
public String getDate() {
return date;
}
public List<Ride> getRide() {
return ride;
}



}