package com.example.lab4;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//ChatGPT
//Prompt: Ahora para el pronostico fragment, el usuario podrá dar click a cualquier elemento del recyclerView del location fragment y se redireccionaraá
// al fragmento pronóstico donde podrá ver los pronósticos esperados para el clima de hoy y los próximos 14 días.
// El metodo GET es el siguiente: http://api.weatherapi.com/v1/forecast.json?key=ec24b1c6dd8a4d528c1205500250305&q=id:{idLocation}&days={daysForecaster}.
// Un ejemplo: http://api.weatherapi.com/v1/forecast.json?key=ec24b1c6dd8a4d528c1205500250305&q=id:1802603&days=14 y la respuesta está adjuntada en un archivo JSON.
// El recyclerView deberá mostrar un elemento por día de pronóstico, mostrando los atributos: Location, ID Location, Fecha de Pronóstico, Máxima Temperatura, Mínima Temperatura, Condición del clima y los que creas conveniente.
// En caso se acceda mediante el bottomNavBar, la vista deberá tener dos Place Holder para el id del Location y para los días que pronóstico que se requieran y mediante un botón de Búsqueda.
public class PronosticoFragment extends Fragment {

    private static final String API_KEY = "ec24b1c6dd8a4d528c1205500250305";
    private WeatherApi api;
    private PronosticoAdapter adapter;
    private RecyclerView recyclerView;
    private EditText etIdLoc, etDays;
    private Button btnSearch;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pronostico, container, false);
        etIdLoc = view.findViewById(R.id.etIdLoc);
        etDays = view.findViewById(R.id.etDays);
        btnSearch = view.findViewById(R.id.btnSearchForecast);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerViewForecast);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        api = ApiClient.getClient().create(WeatherApi.class);

        btnSearch.setOnClickListener(v -> {
            String idLoc = etIdLoc.getText().toString().trim();
            String days = etDays.getText().toString().trim();

            if (idLoc.isEmpty() || days.isEmpty()) {
                Toast.makeText(getContext(), "Ingrese ID y días válidos", Toast.LENGTH_SHORT).show();
            } else {
                loadForecast(idLoc, days);
            }
        });

        // Si viene desde LocationFragment
        if (getArguments() != null) {
            String idLoc = getArguments().getString("idLocation");
            String days = getArguments().getString("daysForecaster", "14");
            loadForecast(idLoc, days);
        }

        return view;
    }

    private void loadForecast(String idLocation, String days) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        Call<PronosticoResponse> call = api.getForecast(API_KEY, "id:" + idLocation, days);
        call.enqueue(new Callback<PronosticoResponse>() {
            @Override
            public void onResponse(Call<PronosticoResponse> call, Response<PronosticoResponse> response) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    PronosticoResponse data = response.body();

                    if (data.forecast != null && data.forecast.forecastday != null) {
                        adapter = new PronosticoAdapter(
                                data.forecast.forecastday,
                                data.location.name,
                                idLocation
                        );
                        Toast.makeText(getContext(),
                                "Días recibidos: " + data.forecast.forecastday.size(),
                                Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "Sin pronósticos disponibles", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PronosticoResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
