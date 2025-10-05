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

public class PronosticoAdapter extends RecyclerView.Adapter<PronosticoAdapter.ViewHolder> {
    private List<PronosticoResponse.ForecastDay> forecastList;
    private String locationName;
    private String locationId;

    public PronosticoAdapter(List<PronosticoResponse.ForecastDay> forecastList, String locationName, String locationId) {
        this.forecastList = forecastList;
        this.locationName = locationName;
        this.locationId = locationId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PronosticoResponse.ForecastDay item = forecastList.get(position);
        holder.tvLocation.setText(locationName + " (" + locationId + ")");
        holder.tvDate.setText(item.date);
        holder.tvTempMax.setText("Max: " + item.day.maxtemp_c + "°C");
        holder.tvTempMin.setText("Min: " + item.day.mintemp_c + "°C");
        holder.tvCondition.setText(item.day.condition.text);
        holder.tvHumidity.setText("Humedad: " + item.day.avghumidity + "%");
        holder.tvUV.setText("UV: " + item.day.uv);
        Glide.with(holder.itemView.getContext()).load("https:" + item.day.condition.icon).into(holder.ivIcon);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocation, tvDate, tvTempMax, tvTempMin, tvCondition, tvHumidity, tvUV;
        ImageView ivIcon;
        public ViewHolder(View view) {
            super(view);
            tvLocation = view.findViewById(R.id.tvLocation);
            tvDate = view.findViewById(R.id.tvDate);
            tvTempMax = view.findViewById(R.id.tvTempMax);
            tvTempMin = view.findViewById(R.id.tvTempMin);
            tvCondition = view.findViewById(R.id.tvCondition);
            tvHumidity = view.findViewById(R.id.tvHumidity);
            tvUV = view.findViewById(R.id.tvUV);
            ivIcon = view.findViewById(R.id.ivIcon);
        }
    }
}

