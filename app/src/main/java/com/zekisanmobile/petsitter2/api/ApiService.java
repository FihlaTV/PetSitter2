package com.zekisanmobile.petsitter2.api;

import com.zekisanmobile.petsitter2.api.body.CreateOwnerBody;
import com.zekisanmobile.petsitter2.api.body.CreatePetBody;
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
import com.zekisanmobile.petsitter2.api.body.SendSummaryBody;
import com.zekisanmobile.petsitter2.vo.Job;
import com.zekisanmobile.petsitter2.vo.Owner;
import com.zekisanmobile.petsitter2.vo.Sitter;
import com.zekisanmobile.petsitter2.vo.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @Multipart
    @POST("contacts/save_summary")
    Call<Job> saveSummary(@Part("app_id") RequestBody app_id,
                          @Part("summary_app_id") RequestBody summary_app_id,
                          @Part("text") RequestBody text,
                          @Part("created_at") RequestBody created_at,
                          @Part("photo_app_id") RequestBody photo_app_id,
                          @Part("image\"; filename=\"summary_image.jpg\"") RequestBody image);

    @POST("pet_owners/create_pet_owner")
    Call<Void> createOwner(@Body CreateOwnerBody body);

    @POST("pets/create_pet")
    Call<Void> createPet(@Body CreatePetBody body);

    @Multipart
    @POST("pets/insert_photo")
    Call<Void> insertPetPhoto(@Part("app_id") RequestBody app_id,
                              @Part("photo_app_id") RequestBody photo_app_id,
                              @Part("image\"; filename=\"pet_image.jpg\"") RequestBody image);

    @Multipart
    @POST("pet_owners/insert_photo")
    Call<Void> insertOwnerPhoto(@Part("app_id") RequestBody app_id,
                              @Part("photo_app_id") RequestBody photo_app_id,
                              @Part("image\"; filename=\"pet_image.jpg\"") RequestBody image);
}
