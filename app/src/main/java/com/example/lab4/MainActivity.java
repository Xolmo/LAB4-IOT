package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        //Verificar conexión a internet
        boolean isConnected = NetworkUtils.isInternetAvailable(this);
        showConnectionSnackbar(isConnected);

        // Fragment incial
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new LocationsFragment())
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int id = item.getItemId();

            if (id == R.id.nav_locations) {
                selected = new LocationsFragment();
            } else if (id == R.id.nav_pronostico) {
                selected = new PronosticoFragment();
            } else if (id == R.id.nav_futuro) {
                selected = new FuturoFragment();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selected)
                    .commit();
            return true;
        });
    }

    private void showConnectionSnackbar(boolean connected) {
        View rootView = findViewById(android.R.id.content);
        String message = connected ? "Conectado a Internet 🌐" : "Sin conexión a Internet 🚫";
        int bgColor = connected ? 0xFF4CAF50 : 0xFFF44336; // Verde o Rojo

        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(bgColor);
        snackbar.show();
    }
}
