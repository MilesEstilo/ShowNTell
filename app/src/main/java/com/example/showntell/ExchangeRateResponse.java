package com.example.showntell;

import java.util.Map;

public class ExchangeRateResponse {

    // This will store the exchange rates for different currencies (e.g., USD -> EUR, USD -> INR)
    private Map<String, Double> rates;

    // Getter for the rates
    public Map<String, Double> getRates() {
        return rates;
    }

    // Setter for the rates
    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}
