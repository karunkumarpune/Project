package com.app.bickup_user.retrofit;

import com.app.bickup_user.controller.WebAPIManager;
import com.app.bickup_user.retrofit.model.OnGoing;
import com.app.bickup_user.utility.ConstantValues;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface APIService {

    //https://code.tutsplus.com/tutorials/sending-data-with-retrofit-2-http-client-for-android--cms-27845

    @GET(WebAPIManager.get_url_ongoingRide)
    Call<OnGoing> getOngoingRide(@Header(ConstantValues.USER_ACCESS_TOKEN) String accessToken);

    @GET(WebAPIManager.get_url_historyRide)
    Call<OnGoing> getHistoryRide(@Header(ConstantValues.USER_ACCESS_TOKEN) String accessToken);

    @GET(WebAPIManager.get_url_scheduledRide)
    Call<OnGoing> getSchedul(@Header(ConstantValues.USER_ACCESS_TOKEN) String accessToken);

   /* @POST(WebAPIManager.get_url_rateDriver)
    @FormUrlEncoded
    Call<OnGoing> PostDriverRating(@Header(ConstantValues.USER_ACCESS_TOKEN) String accessToken,
                                      @Field("ride_id") String ride_id,
                                      @Field("driver_id") String driver_id,
                                      @Field("rating") String rating,
                                      @Field("comment") String comment);

*/



}