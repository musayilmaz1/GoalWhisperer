package com.example.tahminci1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiService {
    @Headers("x-apisports-key:adbb7eb99a9e6d74b2bc23b9f1eaa23a") // YOUR_API_KEY kısmına API anahtarınızı koyun
    @GET("standings?league=203&season=2023")
    Call<StandingsResponse> getStandings();
}

