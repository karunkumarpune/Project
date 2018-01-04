package com.app.bickup_user.tracking_status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {

@SerializedName("timestamp")
@Expose
private String timestamp;
@SerializedName("status")
@Expose
private String status;

public String getTimestamp() {
return timestamp;
}

public void setTimestamp(String timestamp) {
this.timestamp = timestamp;
}

public String getStatus() {
return status;
}

    public Status(String timestamp, String status) {
        this.timestamp = timestamp;
        this.status = status;
    }

    public void setStatus(String status) {
this.status = status;
}


}