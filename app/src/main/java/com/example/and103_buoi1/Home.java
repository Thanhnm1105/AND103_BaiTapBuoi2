package com.example.and103_buoi1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CityAdapter cityAdapter;
    private List<City> cityList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cityList = new ArrayList<>();
        cityAdapter = new CityAdapter(cityList, new CityAdapter.CityClickListener() {
            @Override
            public void onEditClick(City city, int position) {
                showEditDialog(city, position);
            }

            @Override
            public void onDeleteClick(City city, int position) {
                showDeleteDialog(city, position);
            }

        });
        Button btnAddCity = findViewById(R.id.btnAddCity);
        btnAddCity.setOnClickListener(v -> showAddDialog());


        recyclerView.setAdapter(cityAdapter); // Sửa lỗi

        db = FirebaseFirestore.getInstance();
        loadCitiesFromFirestore();
    }
    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm thành phố");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_city, null);
        builder.setView(view);

        EditText edtFullName = view.findViewById(R.id.edtFullName);
        EditText edtCountry = view.findViewById(R.id.edtCountry);
        EditText edtPopulation = view.findViewById(R.id.edtPopulation);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String fullName = edtFullName.getText().toString().trim();
            String country = edtCountry.getText().toString().trim();
            String populationStr = edtPopulation.getText().toString().trim();

            if (fullName.isEmpty() || country.isEmpty() || populationStr.isEmpty()) {
                Toast.makeText(this, "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            int population = Integer.parseInt(populationStr);
            City newCity = new City(fullName, country, population);


            db.collection("cities")
                    .add(newCity)
                    .addOnSuccessListener(documentReference -> {
                        newCity.setId(documentReference.getId());
                        cityList.add(newCity);
                        cityAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "thêm thành công!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                    });

        });

        builder.setNegativeButton("Huỷ", null);
        builder.show();
    }




    private void showDeleteDialog(City city, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xoá")
                .setMessage("Bạn có chắc muốn xoá thành phố này ? " + city.getFullName() + "?")
                .setPositiveButton("Có", (dialog, which) -> {
                    db.collection("cities").document(city.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                cityList.remove(position);
                                cityAdapter.notifyItemRemoved(position);
                                Toast.makeText(this, "Xoá thành công", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                            });
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void showEditDialog(City city, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_city, null);
        builder.setView(view);

        EditText edtFullName = view.findViewById(R.id.edtFullName);
        EditText edtCountry = view.findViewById(R.id.edtCountry);
        EditText edtPopulation = view.findViewById(R.id.edtPopulation);

        // Gán dữ liệu cũ vào dialog
        edtFullName.setText(city.getFullName());
        edtCountry.setText(city.getCountry());
        edtPopulation.setText(String.valueOf(city.getPopulation()));

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String updatedFullName = edtFullName.getText().toString().trim();
            String updatedCountry = edtCountry.getText().toString().trim();
            int updatedPopulation = Integer.parseInt(edtPopulation.getText().toString().trim());

            if (updatedFullName.isEmpty() || updatedCountry.isEmpty()) {
                Toast.makeText(this, "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật vào Firestore
            db.collection("cities").document(city.getId())
                    .update("fullName", updatedFullName, "country", updatedCountry, "population", updatedPopulation)
                    .addOnSuccessListener(aVoid -> {
                        city.setFullName(updatedFullName);
                        city.setCountry(updatedCountry);
                        city.setPopulation(updatedPopulation);
                        cityAdapter.notifyItemChanged(position);
                        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    });

        });

        builder.setNegativeButton("Huỷ", null);
        builder.show();
    }




    private void loadCitiesFromFirestore() {
        db.collection("cities")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cityList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        City city = document.toObject(City.class);
                        city.setId(document.getId()); // Lưu ID của Firestore
                        cityList.add(city);
                    }
                    cityAdapter.notifyDataSetChanged();
                    Toast.makeText(Home.this, "Dữ liệu đã tải thành công!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Lỗi khi tải dữ liệu: " + e.getMessage());
                    Toast.makeText(Home.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
                });
    }

}
