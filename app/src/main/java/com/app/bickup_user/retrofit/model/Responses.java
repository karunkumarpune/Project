package com.app.bickup_user.retrofit.model;

import com.app.bickup_user.tracking_status.Status;
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

@SerializedName("status")
@Expose
private List<Status> status = null;



public String getDate() {
return date;
}
public List<Ride> getRide() {
return ride;
}
    public List<Status> getStatus() {
        return status;
    }
    public void setStatus(List<Status> status) {
        this.status = status;
    }

}

