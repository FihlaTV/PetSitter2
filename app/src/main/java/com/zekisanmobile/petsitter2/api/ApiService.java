package com.zekisanmobile.petsitter2.api;

import com.zekisanmobile.petsitter2.api.body.LoginBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    Call<Void> login(@Body LoginBody body);

}
