package com.admission.counsellor.ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.admission.counsellor.R;
import com.admission.counsellor.model.AppConstants;
import com.admission.counsellor.viewmodel.StudentViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class StudentFormFragment extends Fragment {

    private StudentViewModel viewModel;

    // Form widgets
    private TextInputEditText etPercentile;
    private AutoCompleteTextView dropCategory;
    private AutoCompleteTextView dropAdmissionType;
    private AutoCompleteTextView dropRound;
    private MaterialButtonToggleGroup toggleGender;
    private ChipGroup chipBranches;
    private ChipGroup chipDistricts;
    private MaterialButton btnFind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get ViewModel (shared with results screen)
        viewModel = new ViewModelProvider(requireActivity()).get(StudentViewModel.class);

        // Bind views
        etPercentile      = view.findViewById(R.id.et_percentile);
        dropCategory      = view.findViewById(R.id.drop_category);
        dropAdmissionType = view.findViewById(R.id.drop_admission_type);
        dropRound         = view.findViewById(R.id.drop_round);
        toggleGender      = view.findViewById(R.id.toggle_gender);
        chipBranches      = view.findViewById(R.id.chip_group_branches);
        chipDistricts     = view.findViewById(R.id.chip_group_districts);
        btnFind           = view.findViewById(R.id.btn_find_colleges);

        setupDropdowns();
        setupGenderToggle();
        observeBranchesAndDistricts();

        viewModel.loadFormData();

        btnFind.setOnClickListener(v -> validateAndSubmit());
    }

    // ── Dropdowns ───────────────────────────────────────────

    private void setupDropdowns() {
        // Category
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, AppConstants.CATEGORIES);
        dropCategory.setAdapter(categoryAdapter);
        dropCategory.setText("OPEN", false);
        dropCategory.setOnItemClickListener((parent, v, position, id) ->
                viewModel.selectedCategory = AppConstants.CATEGORIES[position]);

        // Admission Type
        ArrayAdapter<String> admTypeAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, AppConstants.ADMISSION_TYPES);
        dropAdmissionType.setAdapter(admTypeAdapter);
        dropAdmissionType.setText("HOME", false);
        dropAdmissionType.setOnItemClickListener((parent, v, position, id) ->
                viewModel.selectedAdmissionType = AppConstants.ADMISSION_TYPES[position]);

        // Round
        String[] roundStrings = {"Round 1", "Round 2", "Round 3"};
        ArrayAdapter<String> roundAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_dropdown_item_1line, roundStrings);
        dropRound.setAdapter(roundAdapter);
        dropRound.setText("Round 1", false);
        dropRound.setOnItemClickListener((parent, v, position, id) ->
                viewModel.selectedRound = AppConstants.ROUNDS[position]);
    }

    // ── Gender Toggle ────────────────────────────────────────

    private void setupGenderToggle() {
        toggleGender.check(R.id.btn_general); // default GENERAL
        toggleGender.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                viewModel.selectedGender = (checkedId == R.id.btn_general) ? "GENERAL" : "FEMALE";
            }
        });
    }

    // ── Branch & District Chips ──────────────────────────────

    private void observeBranchesAndDistricts() {
        viewModel.getBranches().observe(getViewLifecycleOwner(), branches -> {
            chipBranches.removeAllViews();
            for (String branch : branches) {
                Chip chip = new Chip(requireContext());
                chip.setText(branch);
                chip.setCheckable(true);
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) viewModel.selectedBranches.add(branch);
                    else           viewModel.selectedBranches.remove(branch);
                });
                chipBranches.addView(chip);
            }
        });

        viewModel.getDistricts().observe(getViewLifecycleOwner(), districts -> {
            chipDistricts.removeAllViews();
            for (String district : districts) {
                Chip chip = new Chip(requireContext());
                chip.setText(district);
                chip.setCheckable(true);
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) viewModel.selectedDistricts.add(district);
                    else           viewModel.selectedDistricts.remove(district);
                });
                chipDistricts.addView(chip);
            }
        });
    }

    // ── Validation & Submit ──────────────────────────────────

    private void validateAndSubmit() {
        String percentileText = etPercentile.getText() != null
                ? etPercentile.getText().toString().trim() : "";

        if (percentileText.isEmpty()) {
            etPercentile.setError("Enter your percentile");
            return;
        }

        double percentile = Double.parseDouble(percentileText);
        if (percentile < 0 || percentile > 100) {
            etPercentile.setError("Percentile must be between 0 and 100");
            return;
        }

        if (viewModel.selectedBranches.isEmpty()) {
            Snackbar.make(requireView(), "Please select at least one branch", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Save to ViewModel
        viewModel.percentile = percentile;

        // Trigger API call and navigate
        viewModel.findColleges();
        Navigation.findNavController(requireView())
                .navigate(R.id.action_studentForm_to_collegeResults);
    }
}