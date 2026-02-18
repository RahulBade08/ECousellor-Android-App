package com.admission.counsellor.ui.college;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.admission.counsellor.R;
import com.admission.counsellor.adapter.CollegeListAdapter;
import com.admission.counsellor.viewmodel.CollegeViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class CollegeListFragment extends Fragment {

    private CollegeViewModel viewModel;
    private CollegeListAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_college_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel   = new ViewModelProvider(requireActivity()).get(CollegeViewModel.class);
        progressBar = view.findViewById(R.id.progress_bar);
        tvCount     = view.findViewById(R.id.tv_count);

        // RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CollegeListAdapter(college -> {
            Bundle bundle = new Bundle();
            bundle.putLong("collegeId", college.getId());
            Navigation.findNavController(view)
                    .navigate(R.id.action_collegeList_to_collegeDetail, bundle);
        });
        recyclerView.setAdapter(adapter);

        // Search
        TextInputEditText etSearch = view.findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.searchColleges(s != null ? s.toString() : "");
            }
        });

        observeViewModel();
        viewModel.loadColleges(true);
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), loading ->
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE));

        viewModel.getColleges().observe(getViewLifecycleOwner(), colleges -> {
            adapter.setData(colleges);
            tvCount.setText(colleges.size() + " colleges");
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty())
                Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).show();
        });
    }
}