package com.admission.counsellor.ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Student preference form — Fixed version.
 *
 * Branch & District selection now uses searchable AutoCompleteTextView.
 * Typing "s" shows: Satara, Sangli, Solapur, etc.
 * Selecting an option adds a removable chip below the field.
 * Already-selected options are hidden from the dropdown.
 */
public class StudentFormFragment extends Fragment {

    private StudentViewModel viewModel;

    // Form widgets
    private TextInputEditText            etPercentile;
    private AutoCompleteTextView         dropCategory;
    private AutoCompleteTextView         dropAdmissionType;
    private AutoCompleteTextView         dropRound;
    private MaterialButtonToggleGroup    toggleGender;
    private AutoCompleteTextView         autoBranchSearch;
    private AutoCompleteTextView         autoDistrictSearch;
    private ChipGroup                    chipBranches;
    private ChipGroup                    chipDistricts;
    private MaterialButton               btnFind;

    // Live lists of remaining (unselected) options
    private final List<String> availableBranches  = new ArrayList<>();
    private final List<String> availableDistricts = new ArrayList<>();

    // Adapters (updated as user selects/removes items)
    private ArrayAdapter<String> branchAdapter;
    private ArrayAdapter<String> districtAdapter;

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

        viewModel = new ViewModelProvider(requireActivity()).get(StudentViewModel.class);

        // Bind views
        etPercentile      = view.findViewById(R.id.et_percentile);
        dropCategory      = view.findViewById(R.id.drop_category);
        dropAdmissionType = view.findViewById(R.id.drop_admission_type);
        dropRound         = view.findViewById(R.id.drop_round);
        toggleGender      = view.findViewById(R.id.toggle_gender);
        autoBranchSearch  = view.findViewById(R.id.auto_branch_search);
        autoDistrictSearch= view.findViewById(R.id.auto_district_search);
        chipBranches      = view.findViewById(R.id.chip_group_branches);
        chipDistricts     = view.findViewById(R.id.chip_group_districts);
        btnFind           = view.findViewById(R.id.btn_find_colleges);

        setupDropdowns();
        setupGenderToggle();
        setupBranchSearch();
        setupDistrictSearch();
        observeFormData();

        viewModel.loadFormData();

        btnFind.setOnClickListener(v -> validateAndSubmit());
    }

    // ── Standard dropdowns (Category, Admission Type, Round) ─────────────────

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
        viewModel.selectedAdmissionType = "HOME"; // sync ViewModel with what UI shows on load
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

    // ── Gender toggle ─────────────────────────────────────────────────────────

    private void setupGenderToggle() {
        toggleGender.check(R.id.btn_general);
        toggleGender.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                viewModel.selectedGender = (checkedId == R.id.btn_general) ? "GENERAL" : "LADIES";
            }
        });
    }

    // ── Searchable branch autocomplete ────────────────────────────────────────

    private void setupBranchSearch() {
        branchAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                availableBranches);
        autoBranchSearch.setAdapter(branchAdapter);
        autoBranchSearch.setThreshold(1); // Show suggestions from first keystroke

        autoBranchSearch.setOnItemClickListener((parent, v, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            addBranchChip(selected);
            // Clear the search field so user can search for more
            autoBranchSearch.setText("");
        });
    }

    private void addBranchChip(String branch) {
        if (viewModel.selectedBranches.contains(branch)) return;

        viewModel.selectedBranches.add(branch);
        availableBranches.remove(branch);
        branchAdapter.notifyDataSetChanged();

        Chip chip = new Chip(requireContext());
        chip.setText(branch);
        chip.setCloseIconVisible(true);
        chip.setCheckable(false);
        chip.setOnCloseIconClickListener(v -> removeBranchChip(branch, chip));
        chipBranches.addView(chip);
    }

    private void removeBranchChip(String branch, Chip chip) {
        viewModel.selectedBranches.remove(branch);
        chipBranches.removeView(chip);
        // Put it back in the available list, sorted
        availableBranches.add(branch);
        availableBranches.sort(String::compareToIgnoreCase);
        branchAdapter.notifyDataSetChanged();
    }

    // ── Searchable district autocomplete ──────────────────────────────────────

    private void setupDistrictSearch() {
        districtAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                availableDistricts);
        autoDistrictSearch.setAdapter(districtAdapter);
        autoDistrictSearch.setThreshold(1);

        autoDistrictSearch.setOnItemClickListener((parent, v, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            addDistrictChip(selected);
            autoDistrictSearch.setText("");
        });
    }

    private void addDistrictChip(String district) {
        if (viewModel.selectedDistricts.contains(district)) return;

        viewModel.selectedDistricts.add(district);
        availableDistricts.remove(district);
        districtAdapter.notifyDataSetChanged();

        Chip chip = new Chip(requireContext());
        chip.setText(district);
        chip.setCloseIconVisible(true);
        chip.setCheckable(false);
        chip.setOnCloseIconClickListener(v -> removeDistrictChip(district, chip));
        chipDistricts.addView(chip);
    }

    private void removeDistrictChip(String district, Chip chip) {
        viewModel.selectedDistricts.remove(district);
        chipDistricts.removeView(chip);
        availableDistricts.add(district);
        availableDistricts.sort(String::compareToIgnoreCase);
        districtAdapter.notifyDataSetChanged();
    }

    // ── Observe loaded data from ViewModel ───────────────────────────────────

    private void observeFormData() {
        viewModel.getBranches().observe(getViewLifecycleOwner(), branches -> {
            availableBranches.clear();
            availableBranches.addAll(branches);
            availableBranches.sort(String::compareToIgnoreCase);
            branchAdapter.notifyDataSetChanged();
        });

        viewModel.getDistricts().observe(getViewLifecycleOwner(), districts -> {
            availableDistricts.clear();
            availableDistricts.addAll(districts);
            availableDistricts.sort(String::compareToIgnoreCase);
            districtAdapter.notifyDataSetChanged();
        });
    }

    // ── Validation & submit ───────────────────────────────────────────────────

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

        viewModel.percentile = percentile;
        viewModel.findColleges();
        Navigation.findNavController(requireView())
                .navigate(R.id.action_studentForm_to_collegeResults);
    }
}