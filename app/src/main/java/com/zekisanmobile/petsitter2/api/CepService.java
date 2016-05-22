package com.zekisanmobile.petsitter2.api;

import com.zekisanmobile.petsitter2.vo.ViaCep;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CepService {

    @GET("{cep}/json/")
    Call<ViaCep> getLocationFromCep(@Path("cep") String cep);

}
