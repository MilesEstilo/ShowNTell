package com.example.showntell;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CurrencyApi {

    // Define the endpoint for getting exchange rates for a specific currency
    @GET("latest/{currency}")
    Call<ExchangeRateResponse> getExchangeRate(@Path("currency") String currency);
}
