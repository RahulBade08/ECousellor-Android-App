package com.admission.counsellor.network;

import com.admission.counsellor.model.College;
import com.admission.counsellor.model.CollegeResult;
import com.admission.counsellor.model.CutoffHistory;
import com.admission.counsellor.model.StudentPreferenceRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // ─────────────────────────────────────────────────────────
    // STUDENT PANEL
    // Update the path string to match your @PostMapping in Spring Boot
    // Example Spring Boot: @PostMapping("/api/student/recommend")
    // ─────────────────────────────────────────────────────────

    @POST("api/student/predict?page=0&size=20")
    Call<List<CollegeResult>> getRecommendedColleges(@Body StudentPreferenceRequest request);

    @GET("api/branches")
    Call<List<String>> getAllBranches();

    @GET("api/districts")
    Call<List<String>> getAllDistricts();

    // Cutoff history for a college+branch combination (used for chart)
    @GET("api/cutoff/history")
    Call<List<CutoffHistory>> getCutoffHistory(
            @Query("collegeCode") String collegeCode,
            @Query("branch") String branch
    );

    // ─────────────────────────────────────────────────────────
    // COLLEGE PANEL
    // ─────────────────────────────────────────────────────────

    @GET("api/college/all")
    Call<List<College>> getAllColleges(
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("api/college/search")
    Call<List<College>> searchColleges(@Query("q") String query);

    @GET("api/college/code/{id}")
    Call<College> getCollegeById(@Path("id") long id);

    @GET("api/colleges/filter")
    Call<List<College>> filterColleges(
            @Query("branch") String branch,
            @Query("district") String district
    );
}