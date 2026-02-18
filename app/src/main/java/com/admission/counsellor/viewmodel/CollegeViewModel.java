package com.admission.counsellor.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.admission.counsellor.model.College;
import com.admission.counsellor.repository.CollegeRepository;
import com.admission.counsellor.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

public class CollegeViewModel extends ViewModel {

    private final CollegeRepository repository = new CollegeRepository();

    private final MutableLiveData<List<College>> colleges = new MutableLiveData<>();
    private final MutableLiveData<College> selectedCollege = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private int currentPage = 0;
    private final List<College> allColleges = new ArrayList<>();

    public LiveData<List<College>> getColleges() { return colleges; }
    public LiveData<College> getSelectedCollege() { return selectedCollege; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    // ── Load paginated college list ─────────────────────────
    public void loadColleges(boolean refresh) {
        if (refresh) {
            currentPage = 0;
            allColleges.clear();
        }
        isLoading.setValue(true);

        repository.getAllColleges(currentPage, new StudentRepository.ApiCallback<List<College>>() {
            @Override
            public void onSuccess(List<College> data) {
                isLoading.postValue(false);
                allColleges.addAll(data);
                currentPage++;
                colleges.postValue(new ArrayList<>(allColleges));
            }
            @Override
            public void onError(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    // ── Search ──────────────────────────────────────────────
    public void searchColleges(String query) {
        if (query.isEmpty()) {
            loadColleges(true);
            return;
        }
        isLoading.setValue(true);
        repository.searchColleges(query, new StudentRepository.ApiCallback<List<College>>() {
            @Override
            public void onSuccess(List<College> data) {
                isLoading.postValue(false);
                colleges.postValue(data);
            }
            @Override
            public void onError(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }

    // ── Load college detail ─────────────────────────────────
    public void loadCollegeDetail(long id) {
        repository.getCollegeById(id, new StudentRepository.ApiCallback<College>() {
            @Override
            public void onSuccess(College data) { selectedCollege.postValue(data); }
            @Override
            public void onError(String message) { errorMessage.postValue(message); }
        });
    }
}