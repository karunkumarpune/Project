package com.app.bickup_user.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OnGoing {

@SerializedName("response")
@Expose
private List<Responses> responses = null;
@SerializedName("message")
@Expose
private String message;
@SerializedName("flag")
@Expose
private Integer flag;

public List<Responses> getResponses() {
return responses;
}

public void setResponses(List<Responses> responses) {
this.responses = responses;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public Integer getFlag() {
return flag;
}

public void setFlag(Integer flag) {
this.flag = flag;
}

}