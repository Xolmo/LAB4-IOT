package com.example.lab4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FuturoAdapter extends RecyclerView.Adapter<FuturoAdapter.ViewHolder> {

    private List<FuturoResponse.ForecastDay.Hour> hourList;
    private String locationName;

    public FuturoAdapter(List<FuturoResponse.ForecastDay.Hour> hourList, String locationName) {
        this.hourList = hourList;
        this.locationName = locationName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_futuro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FuturoResponse.ForecastDay.Hour item = hourList.get(position);

        holder.tvLocation.setText(locationName);
        holder.tvTime.setText(item.time);
        holder.tvTemp.setText("Temp: " + item.temp_c + "°C");
        holder.tvHumidity.setText("Humedad: " + item.humidity + "%");
        holder.tvRain.setText("Lluvia: " + item.precip_mm + " mm (" + item.chance_of_rain + "%)");
        holder.tvCondition.setText(item.condition.text);
        holder.tvWind.setText("Viento: " + item.wind_kph + " km/h");
        holder.tvUV.setText("UV: " + item.uv);
        Glide.with(holder.itemView.getContext())
                .load("https:" + item.condition.icon)
                .into(holder.ivIcon);
    }

    @Override
    public int getItemCount() {
        return hourList != null ? hourList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocation, tvTime, tvTemp, tvHumidity, tvRain, tvCondition, tvWind, tvUV;
        ImageView ivIcon;
        ViewHolder(View view) {
            super(view);
            tvLocation = view.findViewById(R.id.tvLocation);
            tvTime = view.findViewById(R.id.tvTime);
            tvTemp = view.findViewById(R.id.tvTemp);
            tvHumidity = view.findViewById(R.id.tvHumidity);
            tvRain = view.findViewById(R.id.tvRain);
            tvCondition = view.findViewById(R.id.tvCondition);
            tvWind = view.findViewById(R.id.tvWind);
            tvUV = view.findViewById(R.id.tvUV);
            ivIcon = view.findViewById(R.id.ivIcon);
        }
    }
}