package com.app.bickup_user.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileImage {

@SerializedName("file_format")
@Expose
private String fileFormat;
@SerializedName("image_url")
@Expose
private String imageUrl;
@SerializedName("actual_path")
@Expose
private String actualPath;
@SerializedName("profile_image")
@Expose
private Boolean profileImage;

public String getFileFormat() {
return fileFormat;
}

public void setFileFormat(String fileFormat) {
this.fileFormat = fileFormat;
}

public String getImageUrl() {
return imageUrl;
}

public void setImageUrl(String imageUrl) {
this.imageUrl = imageUrl;
}

public String getActualPath() {
return actualPath;
}

public void setActualPath(String actualPath) {
this.actualPath = actualPath;
}

public Boolean getProfileImage() {
return profileImage;
}

public void setProfileImage(Boolean profileImage) {
this.profileImage = profileImage;
}

}