package com.example.lab4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lab4.R;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<LocationResponse> locations;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(LocationResponse location);
    }

    public LocationAdapter(List<LocationResponse> locations, OnItemClickListener listener) {
        this.locations = locations;
        this.listener = listener;
    }

    public void updateData(List<LocationResponse> newLocations) {
        this.locations = newLocations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationResponse location = locations.get(position);
        holder.tvId.setText("ID: " + location.getId());
        holder.tvName.setText(location.getName());
        holder.tvRegion.setText(location.getRegion() + ", " + location.getCountry());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(location);
        });
    }

    @Override
    public int getItemCount() {
        return locations != null ? locations.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvName, tvRegion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvName = itemView.findViewById(R.id.tvName);
            tvRegion = itemView.findViewById(R.id.tvRegion);
        }
    }
}
