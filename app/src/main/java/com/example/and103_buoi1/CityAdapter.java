package com.example.and103_buoi1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private List<City> cityList;
    private CityClickListener listener;


    public CityAdapter(List<City> cityList, CityClickListener listener) {
        this.cityList = cityList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = cityList.get(position);
        holder.tvCityName.setText("City: " + city.getFullName());
        holder.tvCountry.setText("Country: " + city.getCountry());
        holder.tvPopulation.setText("Population: " + city.getPopulation());

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(city, position));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(city, position));
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }
    public interface CityClickListener {
        void onEditClick(City city, int position);
        void onDeleteClick(City city, int position);
    }


    public static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView tvCityName, tvCountry, tvPopulation;
        ImageView imgLogo;
        Button btnEdit, btnDelete;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tvCityName);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvPopulation = itemView.findViewById(R.id.tvPopulation);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

}

