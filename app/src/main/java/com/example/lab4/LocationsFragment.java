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
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//ChatGPT
//Prompt: quiero que para el fragment locations, se muestre un reciclerView con un placeholder para el nombre de la ubicacion(location), junto a un botón de búsqueda el cual deberá generar el recyclerView
public class LocationsFragment extends Fragment {

    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rvLocations;
    private LocationAdapter adapter;
    private static final String API_KEY = "5586a0acd5a345e0b4361158250210";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_locations, container, false);
        etSearch = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        rvLocations = view.findViewById(R.id.rvLocations);

        adapter =  new LocationAdapter(new ArrayList<>(), location -> {
            Bundle bundle = new Bundle();
            bundle.putString("idLocation", String.valueOf(location.getId()));
            bundle.putString("daysForecaster", "14");

            PronosticoFragment pronosticoFragment = new PronosticoFragment();
            pronosticoFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, pronosticoFragment)
                    .addToBackStack(null)
                    .commit();
        });
        rvLocations.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLocations.setAdapter(adapter);

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(getContext(), "Ingrese una ubicación", Toast.LENGTH_SHORT).show();
            } else {
                fetchLocations(query);
            }
        });

        return view;
    }

    private void fetchLocations(String query) {
        WeatherApi api = ApiClient.getClient().create(WeatherApi.class);
        Call<List<LocationResponse>> call = api.searchLocation(API_KEY, query);

        call.enqueue(new Callback<List<LocationResponse>>() {
            @Override
            public void onResponse(Call<List<LocationResponse>> call, Response<List<LocationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateData(response.body());
                    Toast.makeText(getContext(), "Resultados: " + response.body().size(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Sin resultados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LocationResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
