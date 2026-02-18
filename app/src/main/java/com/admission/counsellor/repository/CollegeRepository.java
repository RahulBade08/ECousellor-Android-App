package com.admission.counsellor.repository;

import com.admission.counsellor.model.College;
import com.admission.counsellor.network.RetrofitClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollegeRepository {

    private final RetrofitClient client = RetrofitClient.getInstance();

    // ── Get All Colleges (paginated) ────────────────────────
    public void getAllColleges(int page, StudentRepository.ApiCallback<List<College>> callback) {
        client.getApiService()
                .getAllColleges(page, 20)
                .enqueue(new Callback<List<College>>() {
                    @Override
                    public void onResponse(Call<List<College>> call, Response<List<College>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Server error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<College>> call, Throwable t) {
                        callback.onError("Network error: " + t.getMessage());
                    }
                });
    }

    // ── Search Colleges ─────────────────────────────────────
    public void searchColleges(String query, StudentRepository.ApiCallback<List<College>> callback) {
        client.getApiService()
                .searchColleges(query)
                .enqueue(new Callback<List<College>>() {
                    @Override
                    public void onResponse(Call<List<College>> call, Response<List<College>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Search failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<College>> call, Throwable t) {
                        callback.onError("Network error: " + t.getMessage());
                    }
                });
    }

    // ── Get College by ID ───────────────────────────────────
    public void getCollegeById(long id, StudentRepository.ApiCallback<College> callback) {
        client.getApiService()
                .getCollegeById(id)
                .enqueue(new Callback<College>() {
                    @Override
                    public void onResponse(Call<College> call, Response<College> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("College not found");
                        }
                    }

                    @Override
                    public void onFailure(Call<College> call, Throwable t) {
                        callback.onError("Network error: " + t.getMessage());
                    }
                });
    }
}