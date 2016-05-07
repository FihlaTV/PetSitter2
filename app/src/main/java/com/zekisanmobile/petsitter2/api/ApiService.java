package com.zekisanmobile.petsitter2.api;

import com.zekisanmobile.petsitter2.api.body.DeviceTokenBody;
import com.zekisanmobile.petsitter2.api.body.JobRequestBody;
import com.zekisanmobile.petsitter2.api.body.JobStatusBody;
import com.zekisanmobile.petsitter2.api.body.LoginBody;
import com.zekisanmobile.petsitter2.api.body.SearchSittersBody;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.Sitter;
import com.zekisanmobile.petsitter2.vo.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("login")
    Call<User> login(@Body LoginBody body);

    @POST("users/{user}/logout")
    Call<Void> logout(@Path("user") long user_id);

    @POST("users/{user}/update_device_token")
    Call<Void> updateDeviceToken(@Path("user") long user_id, @Body DeviceTokenBody body);

    @GET("pet_owners/{owner}")
    Call<Owner> getOwner(@Path("owner") long owner_id);

    @GET("sitters/{sitter}")
    Call<Sitter> getSitter(@Path("sitter") long sitter_id);

    @POST("pet_owners/{owner}/search_sitters")
    Call<List<Sitter>> searchSitters(@Path("owner") long owner_id, @Body SearchSittersBody body);

    @POST("pet_owners/{owner}/request_contact")
    Call<Void> sendJobRequest(@Path("owner") long owner_id, @Body JobRequestBody body);

    @GET("sitters/{sitter}/contacts.json")
    Call<List<Job>> sitterJobs(@Path("sitter") long sitter_id);

    @GET("pet_owners/{owner}/contacts.json")
    Call<List<Job>> ownerJobs(@Path("owner") long owner_id);

    @POST("contacts/{contact}/update_status")
    Call<Void> sendJobStatusUpdate(@Path("contact") long contact_id, @Body JobStatusBody body);
}
