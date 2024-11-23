package com.example.showntell;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Spinner fromCurrency, toCurrency;
    private EditText inputAmount;
    private TextView resultView;
    private CurrencyApi currencyApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        inputAmount = findViewById(R.id.inputAmount);
        fromCurrency = findViewById(R.id.fromCurrency);
        toCurrency = findViewById(R.id.toCurrency);
        resultView = findViewById(R.id.resultView);
        Button convertButton = findViewById(R.id.convertButton);

        // Set up Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://open.er-api.com/v6/") // You may need to check if this API supports all the currencies you need
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        currencyApi = retrofit.create(CurrencyApi.class);

        String[] currencies = {
                "USD - US Dollar",
                "EUR - Euro",
                "GBP - British Pound",
                "JPY - Japanese Yen",
                "AUD - Australian Dollar",
                "CAD - Canadian Dollar",
                "INR - Indian Rupee",
                "CNY - Chinese Yuan",
                "MXN - Mexican Peso"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromCurrency.setAdapter(adapter);
        toCurrency.setAdapter(adapter);

        // Handle Convert button click
        convertButton.setOnClickListener(v -> convertCurrency());
    }

    private void convertCurrency() {
        String from = fromCurrency.getSelectedItem().toString().split(" - ")[0]; // Extracting currency code (USD, EUR, etc.)
        String to = toCurrency.getSelectedItem().toString().split(" - ")[0]; // Extracting currency code (USD, EUR, etc.)
        String amountStr = inputAmount.getText().toString();

        if (amountStr.isEmpty()) {
            resultView.setText("Enter a valid amount");
            return;
        }

        double amount = Double.parseDouble(amountStr);

        // Make API call to fetch exchange rate
        currencyApi.getExchangeRate(from).enqueue(new Callback<ExchangeRateResponse>() {
            @Override
            public void onResponse(Call<ExchangeRateResponse> call, Response<ExchangeRateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double rate = response.body().getRates().get(to);
                    double result = amount * rate;
                    resultView.setText(String.format("%.2f %s", result, to));
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch exchange rate", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ExchangeRateResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
