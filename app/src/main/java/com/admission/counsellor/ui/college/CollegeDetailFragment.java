package com.admission.counsellor.ui.college;

import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.admission.counsellor.R;
import com.admission.counsellor.model.College;
import com.admission.counsellor.viewmodel.CollegeViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

public class CollegeDetailFragment extends Fragment {

    private CollegeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_college_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CollegeViewModel.class);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        ProgressBar progressBar = view.findViewById(R.id.progress_bar);

        long collegeId = getArguments() != null ? getArguments().getLong("collegeId", -1) : -1;

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), loading ->
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE));

        viewModel.getSelectedCollege().observe(getViewLifecycleOwner(), college -> {
            if (college != null) bindCollege(view, toolbar, college);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        });

        if (collegeId != -1) {
            viewModel.loadCollegeDetail(collegeId);
        }
    }

    private void bindCollege(View view, MaterialToolbar toolbar, College college) {
        toolbar.setTitle(college.getCollegeName());

        ((TextView) view.findViewById(R.id.tv_college_name)).setText(college.getCollegeName());
        ((TextView) view.findViewById(R.id.tv_college_code)).setText("Code: " + college.getCollegeCode());
        ((TextView) view.findViewById(R.id.tv_university)).setText(college.getUniversity());
        ((TextView) view.findViewById(R.id.tv_location)).setText(college.getDistrict());
        ((TextView) view.findViewById(R.id.tv_address)).setText(
                college.getAddress() != null ? college.getAddress() : "N/A");
        ((TextView) view.findViewById(R.id.tv_phone)).setText(
                college.getPhone() != null ? college.getPhone() : "N/A");
        ((TextView) view.findViewById(R.id.tv_website)).setText(
                college.getWebsite() != null ? college.getWebsite() : "N/A");

        // Status chips
        Chip chipAuto  = view.findViewById(R.id.chip_autonomous);
        Chip chipAided = view.findViewById(R.id.chip_aided);
        chipAuto.setText(college.isAutonomous() ? "Autonomous" : "Non-Autonomous");
        chipAided.setText(college.isAided() ? "Government Aided" : "Unaided");

        // Courses chips
        ChipGroup courseGroup = view.findViewById(R.id.chip_group_courses);
        courseGroup.removeAllViews();
        if (college.getCourses() != null) {
            for (College.Course course : college.getCourses()) {
                Chip chip = new Chip(requireContext());
                chip.setText(course.getBranchName() + " (" + course.getIntake() + " seats)");
                chip.setCheckable(false);
                courseGroup.addView(chip);
            }
        }
    }
}