package com.admission.counsellor.network;

import com.admission.counsellor.model.PageResponse;
import com.admission.counsellor.model.College;
import com.admission.counsellor.model.CollegeResult;
import com.admission.counsellor.model.StudentPreferenceRequest;


import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // ── STUDENT PANEL ────────────────────────────────────────────────────────
    // POST /api/student/predict?page=0&size=20
    @POST("api/student/predict")
    Call<PageResponse<CollegeResult>> predictColleges(
            @Body StudentPreferenceRequest request,
            @Query("page") int page,
            @Query("size") int size
    );

    // ── COLLEGE PANEL ────────────────────────────────────────────────────────
    // GET /api/college/all
    @GET("api/college/all")
    Call<List<College>> getAllColleges();

    // GET /api/college/search?name=Sinhgad
    @GET("api/college/search")
    Call<List<College>> searchColleges(@Query("name") String name);

    // GET /api/college/{id}
    @GET("api/college/{id}")
    Call<College> getCollegeById(@Path("id") long id);

    // GET /api/college/districts
    @GET("api/college/districts")
    Call<List<String>> getAllDistricts();

    // GET /api/college/by-district?districts=Pune&districts=Nashik
    @GET("api/college/by-district")
    Call<List<College>> getCollegesByDistricts(@Query("districts") List<String> districts);
}