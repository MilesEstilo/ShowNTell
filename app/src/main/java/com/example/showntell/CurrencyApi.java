package com.example.showntell;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CurrencyApi {

    // Define the endpoint for getting exchange rates with an API key
    @GET("latest/{currency}")
    Call<ExchangeRateResponse> getExchangeRate(
            @Path("currency") String currency,
            @Query("apikey") String apiKey);  // Include the API key as a query parameter
}
