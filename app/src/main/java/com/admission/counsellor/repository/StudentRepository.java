package com.admission.counsellor.repository;

import com.admission.counsellor.model.CollegeResult;
import com.admission.counsellor.model.PageResponse;
import com.admission.counsellor.model.StudentPreferenceRequest;
import com.admission.counsellor.network.RetrofitClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentRepository {

    private final RetrofitClient client = RetrofitClient.getInstance();

    public interface ApiCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    public void getRecommendedColleges(StudentPreferenceRequest request,
                                       ApiCallback<List<CollegeResult>> callback) {
        // size=10000 — get ALL matching results in one call.
        // Backend fetches up to 10,000 from DB, deduplicates, sorts, then slices.
        // The full sorted list is what the student needs to see.
        client.getApiService()
                .predictColleges(request, 0, 10000)
                .enqueue(new Callback<PageResponse<CollegeResult>>() {
                    @Override
                    public void onResponse(Call<PageResponse<CollegeResult>> call,
                                           Response<PageResponse<CollegeResult>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<CollegeResult> content = response.body().getContent();
                            if (content != null) {
                                callback.onSuccess(content);
                            } else {
                                callback.onError("Empty response from server");
                            }
                        } else {
                            callback.onError("Server error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<PageResponse<CollegeResult>> call, Throwable t) {
                        callback.onError("Network error: " + t.getMessage());
                    }
                });
    }

    // ── Loads all distinct branch names from DB ───────────────────────────────
    public void getAllBranches(ApiCallback<List<String>> callback) {
        client.getApiService()
                .getAllBranches()
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call,
                                           Response<List<String>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Failed to load branches");
                        }
                    }
                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public void getAllDistricts(ApiCallback<List<String>> callback) {
        client.getApiService()
                .getAllDistricts()
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call,
                                           Response<List<String>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Failed to load districts");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }
}