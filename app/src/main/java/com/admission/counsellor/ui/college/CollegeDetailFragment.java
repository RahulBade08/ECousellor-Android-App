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
import com.admission.counsellor.model.ShortlistRequest;
import com.admission.counsellor.model.ViewEventRequest;
import com.admission.counsellor.network.RetrofitClient;
import com.admission.counsellor.viewmodel.CollegeViewModel;
import com.admission.counsellor.viewmodel.StudentViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollegeDetailFragment extends Fragment {

    private CollegeViewModel collegeViewModel;
    private StudentViewModel studentViewModel;

    private String  collegeCode;
    private String  courseCode;
    private long    collegeId   = -1;
    private boolean isShortlisted = false;

    private Button btnShortlist;

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

        collegeViewModel = new ViewModelProvider(requireActivity()).get(CollegeViewModel.class);
        studentViewModel = new ViewModelProvider(requireActivity()).get(StudentViewModel.class);

        // Read args — accept either collegeId (long) or collegeCode (String)
        if (getArguments() != null) {
            collegeCode = getArguments().getString("collegeCode");
            courseCode  = getArguments().getString("courseCode");
            collegeId   = getArguments().getLong("collegeId", -1);
        }

        // Toolbar back button
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v ->
                    Navigation.findNavController(view).popBackStack());
        }

        // Shortlist button — declared in layout as btn_shortlist
        // If your layout doesn't have it yet, this is safely null-checked
        btnShortlist = view.findViewById(R.id.btn_shortlist);
        if (btnShortlist != null) {
            btnShortlist.setOnClickListener(v -> toggleShortlist());
        }

        // Observe college data and bind to existing layout views
        collegeViewModel.getSelectedCollege().observe(getViewLifecycleOwner(), college -> {
            if (college == null) return;
            bindCollegeData(view, college);
        });

        // Load college — use collegeId if available, fallback to finding via code
        if (collegeId != -1) {
            // ✅ Uses existing loadCollegeDetail(long id) method
            collegeViewModel.loadCollegeDetail(collegeId);
            fireViewEvent(collegeCode != null ? collegeCode : String.valueOf(collegeId), courseCode);

        } else if (collegeCode != null) {
            // No ID passed — load from the already-observed selectedCollege
            // (should already be set if navigating from result list)
            // Fire the view event regardless
            fireViewEvent(collegeCode, courseCode);
        }
    }

    private void bindCollegeData(View view, College college) {
        // ── Uses the ACTUAL IDs in fragment_college_detail.xml ────────────────

        // tv_college_name — exists in layout ✅
        setText(view, R.id.tv_college_name,
                college.getCollegeName() != null ? college.getCollegeName() : "");

        // tv_college_code — exists in layout ✅
        setText(view, R.id.tv_college_code,
                "Code: " + college.getCollegeCode());

        // tv_university — exists in layout (NOT tv_district/funding_type/intake) ✅
        setText(view, R.id.tv_university,
                college.getCourseUniversity() != null ? college.getCourseUniversity() : "—");

        // tv_location — exists in layout (used for district) ✅
        setText(view, R.id.tv_location,
                college.getDistrict() != null ? college.getDistrict() : "—");

        // tv_address — exists in layout ✅
        setText(view, R.id.tv_address,
                college.getAddress() != null ? college.getAddress() : "—");

        // tv_phone — exists in layout (used for region) ✅
        setText(view, R.id.tv_phone,
                college.getRegion() != null ? college.getRegion() : "—");

        // tv_website — exists in layout (used for funding type + intake) ✅
        String fundingInfo = (college.getFundingType() != null ? college.getFundingType() : "—")
                + (college.getTotalIntake() != null ? "  ·  Intake: " + college.getTotalIntake() : "");
        setText(view, R.id.tv_website, fundingInfo);

        // Chips — autonomous and aided
        Chip chipAutonomous = view.findViewById(R.id.chip_autonomous);
        Chip chipAided      = view.findViewById(R.id.chip_aided);
        if (chipAutonomous != null)
            chipAutonomous.setText(college.isAutonomous() ? "Autonomous" : "Affiliated");
        if (chipAided != null)
            chipAided.setText(college.isAided() ? "Aided" : "Un-Aided");

        // Courses chip group
        ChipGroup chipGroupCourses = view.findViewById(R.id.chip_group_courses);
        if (chipGroupCourses != null && college.getCourses() != null) {
            chipGroupCourses.removeAllViews();
            for (College.CourseDTO course : college.getCourses()) {
                Chip chip = new Chip(requireContext());
                chip.setText(course.getCourseName());
                chip.setCheckable(false);
                chipGroupCourses.addView(chip);
            }
        }
    }

    private void setText(View root, int id, String text) {
        TextView tv = root.findViewById(id);
        if (tv != null) tv.setText(text);
    }

    // ── Fire view event silently ──────────────────────────────────────────────
    private void fireViewEvent(String code, String course) {
        ViewEventRequest req = new ViewEventRequest(
                code,
                course,
                studentViewModel.percentile > 0 ? studentViewModel.percentile : null,
                studentViewModel.selectedCategory,
                studentViewModel.selectedGender,
                studentViewModel.selectedAdmissionType
        );

        RetrofitClient.getInstance().getApiService()
                .recordCollegeView(req)
                .enqueue(new Callback<Void>() {
                    @Override public void onResponse(Call<Void> c, Response<Void> r) {}
                    @Override public void onFailure(Call<Void> c, Throwable t) {}
                });
    }

    // ── Shortlist toggle ──────────────────────────────────────────────────────
    private void toggleShortlist() {
        isShortlisted = !isShortlisted;

        if (btnShortlist != null) {
            btnShortlist.setText(isShortlisted ? "✓ Shortlisted" : "☆ Shortlist");
        }

        if (isShortlisted && collegeCode != null) {
            String capCode = derivedCapCategoryCode(
                    studentViewModel.selectedCategory,
                    studentViewModel.selectedGender,
                    studentViewModel.selectedAdmissionType
            );

            ShortlistRequest req = new ShortlistRequest(
                    collegeCode,
                    courseCode != null ? courseCode : "",
                    "",
                    studentViewModel.percentile > 0 ? studentViewModel.percentile : null,
                    studentViewModel.selectedCategory,
                    studentViewModel.selectedGender,
                    studentViewModel.selectedAdmissionType,
                    capCode
            );

            RetrofitClient.getInstance().getApiService()
                    .recordShortlist(req)
                    .enqueue(new Callback<Void>() {
                        @Override public void onResponse(Call<Void> c, Response<Void> r) {}
                        @Override public void onFailure(Call<Void> c, Throwable t) {}
                    });

            Snackbar.make(requireView(), "College shortlisted!", Snackbar.LENGTH_SHORT).show();
        }
    }

    private String derivedCapCategoryCode(String category, String gender, String admissionType) {
        if ("EWS".equals(category) || "TFWS".equals(category)) return category;
        String prefix = "LADIES".equalsIgnoreCase(gender) ? "L" : "G";
        String suffix = "HOME".equalsIgnoreCase(admissionType) ? "H"
                : "OTHER".equalsIgnoreCase(admissionType) ? "O" : "S";
        return prefix + normalizeCategory(category) + suffix;
    }

    private String normalizeCategory(String cat) {
        if (cat == null) return "OPEN";
        switch (cat.toUpperCase().trim()) {
            case "OPEN":            return "OPEN";
            case "OBC":             return "OBC";
            case "SC":              return "SC";
            case "ST":              return "ST";
            case "NT1": case "NT-1": case "VJ": return "NT1";
            case "NT2": case "NT-2":            return "NT2";
            case "NT3": case "NT-3":            return "NT3";
            default:                return cat.toUpperCase().trim();
        }
    }
}
