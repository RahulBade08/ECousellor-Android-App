package com.admission.counsellor.ui.student;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.admission.counsellor.R;
import com.admission.counsellor.model.CutoffHistory;
import com.admission.counsellor.viewmodel.StudentViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.*;

public class CutoffChartFragment extends Fragment {

    private StudentViewModel viewModel;
    private LineChart lineChart;
    private ProgressBar progressBar;

    // Predefined colors for different categories
    private final int[] CHART_COLORS = {
            Color.parseColor("#1976D2"),  // Blue  - OPEN
            Color.parseColor("#E53935"),  // Red   - OBC
            Color.parseColor("#43A047"),  // Green - SC
            Color.parseColor("#FB8C00"),  // Orange- ST
            Color.parseColor("#8E24AA")   // Purple- Others
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cutoff_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel    = new ViewModelProvider(requireActivity()).get(StudentViewModel.class);
        lineChart    = view.findViewById(R.id.line_chart);
        progressBar  = view.findViewById(R.id.progress_bar);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        // Get arguments passed from CollegeResultsFragment
        String collegeCode = getArguments() != null ? getArguments().getString("collegeCode", "") : "";
        String collegeName = getArguments() != null ? getArguments().getString("collegeName", "") : "";
        String branch      = getArguments() != null ? getArguments().getString("branch", "") : "";

        TextView tvSubtitle = view.findViewById(R.id.tv_subtitle);
        tvSubtitle.setText(collegeName + " • " + branch);

        setupChart();
        observeChartData();

        viewModel.loadCutoffHistory(collegeCode, branch);
    }

    private void setupChart() {
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.getLegend().setEnabled(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setGranularity(1f);
    }

    private void observeChartData() {
        progressBar.setVisibility(View.VISIBLE);

        viewModel.getCutoffHistory().observe(getViewLifecycleOwner(), data -> {
            progressBar.setVisibility(View.GONE);
            if (data == null || data.isEmpty()) {
                renderChart(getDemoData()); // Show demo data if API has no data
            } else {
                renderChart(data);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            progressBar.setVisibility(View.GONE);
            renderChart(getDemoData()); // Fallback gracefully
        });
    }

    private void renderChart(List<CutoffHistory> data) {
        // Group records by category
        Map<String, List<CutoffHistory>> byCategory = new LinkedHashMap<>();
        for (CutoffHistory h : data) {
            if (!byCategory.containsKey(h.getCategory())) {
                byCategory.put(h.getCategory(), new ArrayList<>());
            }
            byCategory.get(h.getCategory()).add(h);
        }

        // Build sorted year labels
        Set<Integer> yearSet = new TreeSet<>();
        for (CutoffHistory h : data) yearSet.add(h.getYear());
        List<Integer> years = new ArrayList<>(yearSet);
        String[] yearLabels = new String[years.size()];
        for (int i = 0; i < years.size(); i++) yearLabels[i] = String.valueOf(years.get(i));

        // Build one LineDataSet per category
        List<ILineDataSet> dataSets = new ArrayList<>();
        int colorIndex = 0;
        for (Map.Entry<String, List<CutoffHistory>> entry : byCategory.entrySet()) {
            List<CutoffHistory> catData = entry.getValue();
            catData.sort(Comparator.comparingInt(CutoffHistory::getYear));

            List<Entry> entries = new ArrayList<>();
            for (CutoffHistory h : catData) {
                int xIndex = years.indexOf(h.getYear());
                entries.add(new Entry(xIndex, h.getCutoffRank()));
            }

            LineDataSet dataSet = new LineDataSet(entries, entry.getKey());
            int color = CHART_COLORS[colorIndex % CHART_COLORS.length];
            dataSet.setColor(color);
            dataSet.setCircleColor(color);
            dataSet.setLineWidth(2f);
            dataSet.setCircleRadius(4f);
            dataSet.setDrawValues(true);
            dataSets.add(dataSet);
            colorIndex++;
        }

        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(yearLabels));
        lineChart.setData(new LineData(dataSets));
        lineChart.invalidate(); // Refresh chart
    }

    /** Returns demo data so chart is not blank during development */
    private List<CutoffHistory> getDemoData() {
        List<CutoffHistory> demo = new ArrayList<>();
        // Will be populated via reflection-style or just shown as placeholder
        // In real use, your backend will populate this
        return demo;
    }
}