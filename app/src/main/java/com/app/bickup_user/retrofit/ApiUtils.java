package com.app.bickup_user.retrofit;

import com.app.bickup_user.utility.ConstantValues;

public class ApiUtils {
    private ApiUtils() {}
    public static APIService getAPIService() {
 
        return RetrofitClient.getClient(ConstantValues.BASE_URL).create(APIService.class);
    }
}