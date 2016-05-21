package com.zekisanmobile.petsitter2.api;

import com.zekisanmobile.petsitter2.api.body.DeviceTokenBody;
import com.zekisanmobile.petsitter2.api.body.GetJobsBody;
import com.zekisanmobile.petsitter2.api.body.GetOwnerBody;
import com.zekisanmobile.petsitter2.api.body.GetSitterBody;
import com.zekisanmobile.petsitter2.api.body.JobRequestBody;
import com.zekisanmobile.petsitter2.api.body.JobStatusBody;
import com.zekisanmobile.petsitter2.api.body.LoginBody;
import com.zekisanmobile.petsitter2.api.body.LogoutBody;
import com.zekisanmobile.petsitter2.api.body.RateJobBody;
import com.zekisanmobile.petsitter2.api.body.ReplyRateBody;
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

    @POST("users/logout")
    Call<Void> logout(@Body LogoutBody body);

    @POST("users/update_device_token")
    Call<Void> updateDeviceToken(@Body DeviceTokenBody body);

    @POST("pet_owners/get_owner")
    Call<Owner> getOwner(@Body GetOwnerBody body);

    @POST("sitters/get_sitter")
    Call<Sitter> getSitter(@Body GetSitterBody body);

    @POST("pet_owners/search_sitters")
    Call<List<Sitter>> searchSitters(@Body SearchSittersBody body);

    @POST("pet_owners/request_contact")
    Call<Void> sendJobRequest(@Path("owner") @Body JobRequestBody body);

    @POST("sitter_contacts.json")
    Call<List<Job>> sitterJobs(@Body GetJobsBody body);

    @POST("pet_owner_contacts.json")
    Call<List<Job>> ownerJobs(@Body GetJobsBody body);

    @POST("contacts/update_status")
    Call<Void> sendJobStatusUpdate(@Body JobStatusBody body);

    @POST("pet_owners/rate_contact")
    Call<Void> ratejob(@Body RateJobBody body);

    @POST("rates/reply_rate")
    Call<Void> replyRate(@Body ReplyRateBody body);
}
