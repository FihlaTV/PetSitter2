package com.zekisanmobile.petsitter2.api;

import com.zekisanmobile.petsitter2.api.body.DeviceTokenBody;
import com.zekisanmobile.petsitter2.api.body.LoginBody;
import com.zekisanmobile.petsitter2.vo.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("login")
    Call<User> login(@Body LoginBody body);

    @POST("users/{user}/update_device_token")
    Call<Void> updateDeviceToken(@Path("user") long user_id, @Body DeviceTokenBody body);

}
