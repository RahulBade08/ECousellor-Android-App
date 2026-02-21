package com.admission.counsellor.adapter;

import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admission.counsellor.R;
import com.admission.counsellor.model.CollegeResult;
import com.admission.counsellor.model.ShortlistRequest;
import com.admission.counsellor.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollegeResultAdapter extends RecyclerView.Adapter<CollegeResultAdapter.ViewHolder> {

    public interface OnCutoffClickListener {
        void onClick(CollegeResult result);
    }

    // Student's own search context — set by CollegeResultsFragment before showing results
    public double studentPercentile   = 0;
    public String studentCategory     = "OPEN";
    public String studentGender       = "GENERAL";
    public String studentAdmissionType= "STATE";

    private List<CollegeResult> data = new ArrayList<>();
    // Track which college+branch combos are shortlisted in this session
    private final Set<String> shortlistedKeys = new HashSet<>();

    private final OnCutoffClickListener listener;

    public CollegeResultAdapter(OnCutoffClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<CollegeResult> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_college_result, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() { return data.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView    tvName, tvBranch, tvLocation, tvUniversity, tvCutoff, tvCategory;
        Chip        chipAutonomous, chipAided;
        MaterialButton btnCutoff;
        ImageButton btnShortlist;

        ViewHolder(View itemView) {
            super(itemView);
            tvName         = itemView.findViewById(R.id.tv_college_name);
            tvBranch       = itemView.findViewById(R.id.tv_branch);
            tvLocation     = itemView.findViewById(R.id.tv_location);
            tvUniversity   = itemView.findViewById(R.id.tv_university);
            tvCutoff       = itemView.findViewById(R.id.tv_cutoff_rank);
            tvCategory     = itemView.findViewById(R.id.tv_category);
            chipAutonomous = itemView.findViewById(R.id.chip_autonomous);
            chipAided      = itemView.findViewById(R.id.chip_aided);
            btnCutoff      = itemView.findViewById(R.id.btn_view_cutoff);
            btnShortlist   = itemView.findViewById(R.id.btn_shortlist);
        }

        void bind(CollegeResult result) {
            tvName.setText(result.getCollegeName());
            tvBranch.setText(result.getBranch());
            tvLocation.setText(result.getDistrict());
            tvUniversity.setText(result.getUniversity());

            // Cutoff + risk
            String cutoffText = "";
            if (result.getCutoffPercentile() != null)
                cutoffText = "Cutoff: " + result.getCutoffPercentile() + "%";
            if (result.getRisk() != null)
                cutoffText += "  |  " + result.getRisk();
            tvCutoff.setText(cutoffText);

            // Confidence + probability
            String infoText = "";
            if (result.getConfidence() != null) infoText = result.getConfidence();
            if (result.getProbability() > 0)
                infoText += "  ·  " + Math.round(result.getProbability() * 100) + "% chance";
            tvCategory.setText(infoText);

            chipAutonomous.setVisibility(
                    Boolean.TRUE.equals(result.getIsAutonomous()) ? View.VISIBLE : View.GONE);
            chipAided.setText(result.isAided() ? "Aided" : "Unaided");

            btnCutoff.setVisibility(View.GONE);

            // ── Shortlist button ──────────────────────────────────────────────
            String key = result.getCollegeCode() + "_" + result.getCourseName();
            boolean alreadyShortlisted = shortlistedKeys.contains(key);

            btnShortlist.setImageResource(alreadyShortlisted
                    ? android.R.drawable.btn_star_big_on
                    : android.R.drawable.btn_star_big_off);

            btnShortlist.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos == RecyclerView.NO_ID) return;

                boolean nowShortlisted = !shortlistedKeys.contains(key);

                if (nowShortlisted) {
                    shortlistedKeys.add(key);
                    btnShortlist.setImageResource(android.R.drawable.btn_star_big_on);
                    sendShortlistEvent(result);
                } else {
                    shortlistedKeys.remove(key);
                    btnShortlist.setImageResource(android.R.drawable.btn_star_big_off);
                }
            });
        }

        // ── Fire shortlist event to counselling backend ───────────────────────
        private void sendShortlistEvent(CollegeResult result) {
            String capCode = derivedCapCategoryCode(
                    studentCategory, studentGender, studentAdmissionType);

            ShortlistRequest req = new ShortlistRequest(
                    result.getCollegeCode(),
                    result.getCourseName(),     // courseCode — using courseName as key
                    result.getCourseName(),
                    studentPercentile > 0 ? studentPercentile : null,
                    studentCategory,
                    studentGender,
                    studentAdmissionType,
                    capCode
            );

            RetrofitClient.getInstance()
                    .getApiService()
                    .recordShortlist(req)
                    .enqueue(new Callback<Void>() {
                        @Override public void onResponse(Call<Void> c, Response<Void> r) {}
                        @Override public void onFailure(Call<Void> c, Throwable t) {}
                    });
        }
    }

    // ── Derive cap category code (mirrors backend logic) ──────────────────────
    private String derivedCapCategoryCode(String category, String gender, String admType) {
        if ("EWS".equals(category) || "TFWS".equals(category)) return category;
        String prefix = "LADIES".equalsIgnoreCase(gender) ? "L" : "G";
        String suffix = "HOME".equalsIgnoreCase(admType)  ? "H"
                : "OTHER".equalsIgnoreCase(admType) ? "O" : "S";
        return prefix + normalizeCategory(category) + suffix;
    }

    private String normalizeCategory(String cat) {
        if (cat == null) return "OPEN";
        switch (cat.toUpperCase().trim()) {
            case "OPEN":                    return "OPEN";
            case "OBC":                     return "OBC";
            case "SC":                      return "SC";
            case "ST":                      return "ST";
            case "NT1": case "NT-1": case "VJ": return "NT1";
            case "NT2": case "NT-2":            return "NT2";
            case "NT3": case "NT-3":            return "NT3";
            default: return cat.toUpperCase().trim();
        }
    }
}