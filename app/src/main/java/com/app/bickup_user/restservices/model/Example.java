package com.app.bickup_user.restservices.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

@SerializedName("response")
@Expose
private Response response;
@SerializedName("message")
@Expose
private String message;
@SerializedName("flag")
@Expose
private Integer flag;

public Response getResponse() {
return response;
}

public void setResponse(Response response) {
this.response = response;
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
