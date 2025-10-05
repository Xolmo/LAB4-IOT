package com.example.lab4;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("search.json")
    Call<List<LocationResponse>> searchLocation(
            @Query("key") String apiKey,
            @Query("q") String query
    );

    @GET("forecast.json")
    Call<PronosticoResponse> getForecast(
            @Query("key") String apiKey,
            @Query("q") String locationId,
            @Query("days") String days
    );

    @GET("future.json")
    Call<FuturoResponse> getFutureWeather(
            @Query("key") String apiKey,
            @Query("q") String query,
            @Query("dt") String date
    );

    @GET("history.json")
    Call<FuturoResponse> getHistoricalWeather(
            @Query("key") String apiKey,
            @Query("q") String query,
            @Query("dt") String date
    );
}
