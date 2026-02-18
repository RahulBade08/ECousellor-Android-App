package com.admission.counsellor.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.admission.counsellor.model.AppConstants;
import com.admission.counsellor.model.CollegeResult;
import com.admission.counsellor.model.CutoffHistory;
import com.admission.counsellor.model.StudentPreferenceRequest;
import com.admission.counsellor.repository.StudentRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentViewModel extends ViewModel {

    private final StudentRepository repository = new StudentRepository();

    // ── LiveData observed by Fragments ──────────────────────
    private final MutableLiveData<List<CollegeResult>> recommendedColleges = new MutableLiveData<>();
    private final MutableLiveData<List<String>> branches = new MutableLiveData<>();
    private final MutableLiveData<List<String>> districts = new MutableLiveData<>();
    private final MutableLiveData<List<CutoffHistory>> cutoffHistory = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // ── Form state (survives screen rotation) ───────────────
    public double percentile = 0;
    public String selectedCategory = "OPEN";
    public String selectedGender = "GENERAL";
    public String selectedAdmissionType = "STATE";
    public int selectedRound = 1;
    public List<String> selectedBranches = new ArrayList<>();
    public List<String> selectedDistricts = new ArrayList<>();

    // ── Public LiveData getters ─────────────────────────────
    public LiveData<List<CollegeResult>> getRecommendedColleges() { return recommendedColleges; }
    public LiveData<List<String>> getBranches() { return branches; }
    public LiveData<List<String>> getDistricts() { return districts; }
    public LiveData<List<CutoffHistory>> getCutoffHistory() { return cutoffHistory; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    // ── Load branches & districts from API ──────────────────
    public void loadFormData() {
        repository.getAllBranches(new StudentRepository.ApiCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> data) { branches.postValue(data); }
            @Override
            public void onError(String message) {
                // Fallback to static constants if API fails
                branches.postValue(Arrays.asList(AppConstants.BRANCHES));
            }
        });

        repository.getAllDistricts(new StudentRepository.ApiCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> data) { districts.postValue(data); }
            @Override
            public void onError(String message) {
                districts.postValue(Arrays.asList(AppConstants.DISTRICTS));
            }
        });
    }

    // ── Build request and call API ──────────────────────────
    public void findColleges() {
        isLoading.setValue(true);

        StudentPreferenceRequest request = new StudentPreferenceRequest(
                percentile,
                selectedCategory,
                selectedGender,
                selectedAdmissionType,
                selectedRound,
                new ArrayList<>(selectedBranches),
                new ArrayList<>(selectedDistricts)
        );

        repository.getRecommendedColleges(request, new StudentRepository.ApiCallback<List<CollegeResult>>() {
            @Override
            public void onSuccess(List<CollegeResult> data) {
                isLoading.postValue(false);
                recommendedColleges.postValue(data);
            }
            @Override
            public void onError(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    // ── Load cutoff chart data ──────────────────────────────
    public void loadCutoffHistory(String collegeCode, String branch) {
        repository.getCutoffHistory(collegeCode, branch, new StudentRepository.ApiCallback<List<CutoffHistory>>() {
            @Override
            public void onSuccess(List<CutoffHistory> data) { cutoffHistory.postValue(data); }
            @Override
            public void onError(String message) { errorMessage.postValue(message); }
        });
    }
}