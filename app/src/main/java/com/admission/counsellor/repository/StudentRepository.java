package com.admission.counsellor.repository;

import com.admission.counsellor.model.CollegeResult;
import com.admission.counsellor.model.CutoffHistory;
import com.admission.counsellor.model.StudentPreferenceRequest;
import com.admission.counsellor.network.RetrofitClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentRepository {

    private final RetrofitClient client = RetrofitClient.getInstance();

    // ── Generic callback interface ──────────────────────────
    // Used by ViewModels to receive success/failure from any API call
    public interface ApiCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    // ── Get Recommended Colleges ────────────────────────────
    public void getRecommendedColleges(StudentPreferenceRequest request,
                                       ApiCallback<List<CollegeResult>> callback) {
        client.getApiService()
                .getRecommendedColleges(request)
                .enqueue(new Callback<List<CollegeResult>>() {
                    @Override
                    public void onResponse(Call<List<CollegeResult>> call,
                                           Response<List<CollegeResult>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Server error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CollegeResult>> call, Throwable t) {
                        callback.onError("Network error: " + t.getMessage());
                    }
                });
    }

    // ── Get All Branches ────────────────────────────────────
    public void getAllBranches(ApiCallback<List<String>> callback) {
        client.getApiService()
                .getAllBranches()
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
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

    // ── Get All Districts ───────────────────────────────────
    public void getAllDistricts(ApiCallback<List<String>> callback) {
        client.getApiService()
                .getAllDistricts()
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
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

    // ── Get Cutoff History ──────────────────────────────────
    public void getCutoffHistory(String collegeCode, String branch,
                                 ApiCallback<List<CutoffHistory>> callback) {
        client.getApiService()
                .getCutoffHistory(collegeCode, branch)
                .enqueue(new Callback<List<CutoffHistory>>() {
                    @Override
                    public void onResponse(Call<List<CutoffHistory>> call,
                                           Response<List<CutoffHistory>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Failed to load cutoff history");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CutoffHistory>> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }
}