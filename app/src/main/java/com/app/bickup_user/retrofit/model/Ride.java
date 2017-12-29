package com.app.bickup_user.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ride {

    @SerializedName("drop_location_address")
    @Expose
    private String dropLocationAddress;
    @SerializedName("pickup_location_address")
    @Expose
    private String pickupLocationAddress;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_image")
    @Expose
    private ProfileImage profileImage;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("driver_id")
    @Expose
    private String driverId;

    public String getRide_Id() {
        return ride_Id;
    }

    public void setRide_Id(String ride_Id) {
        this.ride_Id = ride_Id;
    }

    @SerializedName("ride_id")
    @Expose
    private String ride_Id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("ride_completed_status")
    @Expose
    private String rideCompletedStatus;

    public String getDropLocationAddress() {
        return dropLocationAddress;
    }

    public void setDropLocationAddress(String dropLocationAddress) {
        this.dropLocationAddress = dropLocationAddress;
    }

    public String getPickupLocationAddress() {
        return pickupLocationAddress;
    }

    public void setPickupLocationAddress(String pickupLocationAddress) {
        this.pickupLocationAddress = pickupLocationAddress;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRideCompletedStatus() {
        return rideCompletedStatus;
    }

    public void setRideCompletedStatus(String rideCompletedStatus) {
        this.rideCompletedStatus = rideCompletedStatus;
    }

}