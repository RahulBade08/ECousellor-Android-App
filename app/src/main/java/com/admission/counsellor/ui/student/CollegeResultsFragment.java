package com.admission.counsellor.ui.student;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admission.counsellor.R;
import com.admission.counsellor.adapter.CollegeResultAdapter;
import com.admission.counsellor.model.CollegeResult;
import com.admission.counsellor.viewmodel.StudentViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import android.widget.ProgressBar;
import android.widget.TextView;

public class CollegeResultsFragment extends Fragment {

    private StudentViewModel     viewModel;
    private CollegeResultAdapter adapter;
    private ProgressBar          progressBar;
    private TextView             tvEmpty, tvCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_college_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(StudentViewModel.class);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v ->
                Navigation.findNavController(view).popBackStack());

        progressBar = view.findViewById(R.id.progress_bar);
        tvEmpty     = view.findViewById(R.id.tv_empty);
        tvCount     = view.findViewById(R.id.tv_result_count);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new CollegeResultAdapter(result -> {
            // Future: navigate to college detail on card tap
        });

        // ── Pass student's own context to adapter so shortlist events are accurate
        adapter.studentPercentile    = viewModel.percentile;
        adapter.studentCategory      = viewModel.selectedCategory;
        adapter.studentGender        = viewModel.selectedGender;
        adapter.studentAdmissionType = viewModel.selectedAdmissionType;

        recyclerView.setAdapter(adapter);

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), loading ->
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE));

        viewModel.getRecommendedColleges().observe(getViewLifecycleOwner(), results -> {
            if (results == null || results.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("No colleges found.\nTry adjusting your preferences.");
                tvCount.setText("0 results");
            } else {
                tvEmpty.setVisibility(View.GONE);
                tvCount.setText(results.size() + " colleges found");
                adapter.setData(results);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty())
                Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).show();
        });
    }
}