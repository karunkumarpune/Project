package com.app.bickup_user.restservices.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

@SerializedName("ride_id")
@Expose
private String rideId;
@SerializedName("pickup_location_address")
@Expose
private String pickupLocationAddress;
@SerializedName("pickup_longitude")
@Expose
private Double pickupLongitude;
@SerializedName("pickup_latitude")
@Expose
private Double pickupLatitude;
@SerializedName("drop_location_address")
@Expose
private String dropLocationAddress;
@SerializedName("drop_longitude")
@Expose
private Double dropLongitude;
@SerializedName("drop_latitude")
@Expose
private Double dropLatitude;
@SerializedName("total_price")
@Expose
private Integer totalPrice;

public String getRideId() {
return rideId;
}

public void setRideId(String rideId) {
this.rideId = rideId;
}

public String getPickupLocationAddress() {
return pickupLocationAddress;
}

public void setPickupLocationAddress(String pickupLocationAddress) {
this.pickupLocationAddress = pickupLocationAddress;
}

public Double getPickupLongitude() {
return pickupLongitude;
}

public void setPickupLongitude(Double pickupLongitude) {
this.pickupLongitude = pickupLongitude;
}

public Double getPickupLatitude() {
return pickupLatitude;
}

public void setPickupLatitude(Double pickupLatitude) {
this.pickupLatitude = pickupLatitude;
}

public String getDropLocationAddress() {
return dropLocationAddress;
}

public void setDropLocationAddress(String dropLocationAddress) {
this.dropLocationAddress = dropLocationAddress;
}

public Double getDropLongitude() {
return dropLongitude;
}

public void setDropLongitude(Double dropLongitude) {
this.dropLongitude = dropLongitude;
}

public Double getDropLatitude() {
return dropLatitude;
}

public void setDropLatitude(Double dropLatitude) {
this.dropLatitude = dropLatitude;
}

public Integer getTotalPrice() {
return totalPrice;
}

public void setTotalPrice(Integer totalPrice) {
this.totalPrice = totalPrice;
}

}