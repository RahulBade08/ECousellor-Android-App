package com.admission.counsellor.network;

import com.admission.counsellor.model.PageResponse;
import com.admission.counsellor.model.College;
import com.admission.counsellor.model.CollegeResult;
import com.admission.counsellor.model.ShortlistRequest;
import com.admission.counsellor.model.StudentPreferenceRequest;
import com.admission.counsellor.model.ViewEventRequest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // ── STUDENT PANEL ─────────────────────────────────────────────────────────
    @POST("api/student/predict")
    Call<PageResponse<CollegeResult>> predictColleges(
            @Body StudentPreferenceRequest request,
            @Query("page") int page,
            @Query("size") int size
    );

    // ── COLLEGE PANEL ─────────────────────────────────────────────────────────
    @GET("api/college/all")
    Call<List<College>> getAllColleges();

    @GET("api/college/search")
    Call<List<College>> searchColleges(@Query("name") String name);

    @GET("api/college/{id}")
    Call<College> getCollegeById(@Path("id") long id);

    @GET("api/college/districts")
    Call<List<String>> getAllDistricts();

    // NEW — fetches all distinct course names from DB
    @GET("api/college/branches")
    Call<List<String>> getAllBranches();

    @GET("api/college/by-district")
    Call<List<College>> getCollegesByDistricts(@Query("districts") List<String> districts);

    // ── COUNSELLING EVENTS ────────────────────────────────────────────────────
    @POST("api/counselling/event/view")
    Call<Void> recordCollegeView(@Body ViewEventRequest request);

    @POST("api/counselling/event/shortlist")
    Call<Void> recordShortlist(@Body ShortlistRequest request);
}