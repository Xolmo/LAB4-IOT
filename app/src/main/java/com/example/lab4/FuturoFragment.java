package com.example.lab4;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//ChatGPT
//Prompt: Finalmente para el fragment futuro, se va a usar los siguientes metodo GET :http://api.weatherapi.com/v1/future.json?key=5586a0acd5a345e0b4361158250210&q={CityName}&dt={fecha_futura}
// http://api.weatherapi.com/v1/history.json?key=5586a0acd5a345e0b4361158250210&q={CityName}&dt={fecha_historica} .
// Estos métodos devuelven un estimado de entre 14 y 300 días en el futuro y el pronóstico hasta hace 365 para fechas futuras o pasadas respectivamente.
// La vista del presente fragmento deberá presentar dos Place Holder tanto para el id del Location de interés y del día del cual se desea conocer su pronóstico
// y mediante un botón de búsqueda se deberá mostrar el pronóstico por hora para el día solicitado mostrando atributos como: location, idLocation,
// temperatura esperada, condición de clima, porcentaje de humedad, pronóstico de lluvia y los valores que usted considere pertinente. Se deberá verificar
// la fecha ingresada para determinar qué metodo usar. Ej: http://api.weatherapi.com/v1/future.json?key=5586a0acd5a345e0b4361158250210&q=id:1802603&dt=2026-06-02. La respuesta se ha adjuntado
public class FuturoFragment extends Fragment {

    private static final String API_KEY = "5586a0acd5a345e0b4361158250210";
    private EditText etCity, etDate;
    private Button btnSearch;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private WeatherApi api;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_futuro, container, false);

        etCity = view.findViewById(R.id.etCity);
        etDate = view.findViewById(R.id.etDate);
        btnSearch = view.findViewById(R.id.btnSearch);
        recyclerView = view.findViewById(R.id.recyclerViewFuture);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        api = ApiClient.getClient().create(WeatherApi.class);

        btnSearch.setOnClickListener(v -> {
            String city = etCity.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            if (city.isEmpty() || date.isEmpty()) {
                Toast.makeText(getContext(), "Ingrese ciudad y fecha (YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
                return;
            }
            determineAndFetch(city, date);
        });

        return view;
    }

    private void determineAndFetch(String city, String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date enteredDate = sdf.parse(date);
            Date currentDate = new Date();

            if (enteredDate.after(currentDate)) {
                fetchFuture(city, date);
            } else {
                fetchHistory(city, date);
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Fecha inválida", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchFuture(String city, String date) {
        progressBar.setVisibility(View.VISIBLE);
        api.getFutureWeather(API_KEY, city, date).enqueue(new Callback<FuturoResponse>() {
            @Override
            public void onResponse(Call<FuturoResponse> call, Response<FuturoResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    showData(response.body());
                } else {
                    Toast.makeText(getContext(), "Sin datos futuros", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FuturoResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchHistory(String city, String date) {
        progressBar.setVisibility(View.VISIBLE);
        api.getHistoricalWeather(API_KEY, city, date).enqueue(new Callback<FuturoResponse>() {
            @Override
            public void onResponse(Call<FuturoResponse> call, Response<FuturoResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    showData(response.body());
                } else {
                    Toast.makeText(getContext(), "Sin datos históricos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FuturoResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showData(FuturoResponse data) {
        if (data.forecast != null && !data.forecast.forecastday.isEmpty()) {
            List<FuturoResponse.ForecastDay.Hour> hours = data.forecast.forecastday.get(0).hour;
            recyclerView.setAdapter(new FuturoAdapter(hours, data.location.name));
        }
    }
}
