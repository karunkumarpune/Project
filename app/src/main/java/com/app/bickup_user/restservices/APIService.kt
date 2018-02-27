package com.app.bickup_user.restservices


import com.app.bickup_user.restservices.Constant.key_accessToken
import com.app.bickup_user.restservices.model.Example
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface APIService {


    //https://raw.githubusercontent.com/karunkumarpune/Expandeble/master/question_test.json

    //https://code.tutsplus.com/tutorials/sending-data-with-retrofit-2-http-client-for-android--cms-27845

    // http://www.androidhub4you.com/p/blog-page_27.html
   
   
  // Image1852672049.jpeg
   
  /// http://worklime.com/immigration/images/Image1852672049.jpeg
    
/*

    @GET("/karunkumarpune/Expandeble/master/question_test.json")
    fun getQuestion(): Call<Status>

    @Headers("Content-Type: application/json")
    @POST(urlSignUp)
    fun getUser(@Body body: Map<String, String>): Call<ResponseModel>

    @Headers("Content-Type: application/json")
    @POST(urlVerifyOtp)
    fun verifyOtp(@Body body: Map<String, String>): Call<ResponseModel>

    @Headers("Content-Type: application/json")
    @POST(urlResendOtp)
    fun resendOtp(@Body body: Map<String, String>): Call<ResponseModel>
*/

/*
    @Multipart
    @POST(urlUpdateProfile)
    fun postImage(@Header (key_accessToken) accessToken:String,
                  @Part image: MultipartBody.Part?,
                  @Part(key_Fname) firstName: RequestBody,
                  @Part(key_Lname) lastName: RequestBody,
                  @Part(key_contact) contact: RequestBody,
                  @Part(key_countryCode) countryCode: RequestBody
                 ): Call<ResponseModel>*/
   
   
/*   {
      "user": {
      "profile_photo": image_file,
      "username": "android",
      ...,
      "banner_pictures": [
      file1,
      file2,
      file3,
      ...
      ]
   }
   }
   */
     
     
     @Headers("Content-Type: application/json")
     @Multipart
     @POST("/ride")
     fun postImage(
     @Header(key_accessToken) accessToken:String,
     @Part image: MultipartBody.Part): Call<Example>
}







/*  @Headers("Content-Type: application/json")
     @POST(urlLogin)
     fun login(@Body body: Map<String, String>): Call<ResponseModel>
 
     @Headers("Content-Type: application/json")
     @POST(urlForgotPassword)
     fun forgotPassword(@Body body: Map<String, String>): Call<ResponseModel>
 
     @Headers("Content-Type: application/json")
     @POST(urlSetPassword)
     fun setPassword(@Body body: Map<String, String>): Call<ResponseModel>
 
     @Headers("Content-Type: application/json")
     @POST(urlChangePassword)
     fun changePasswords(@Header ("accessToken") accessToken:String,
                        @Body body: Map<String, String>): Call<ResponseModel>
 
     @Headers("Content-Type: application/json")
     @GET(urlLogout)
     fun logout(@Header ("accessToken") accessToken:String): Call<ResponseModel>*/



/*



    @FormUrlEncoded
    @POST("/")
    fun Save(@Field("answer") name:String,
             @Field("Date") Date:String):Call<JSONObject>
*/


