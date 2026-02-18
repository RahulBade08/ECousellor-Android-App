package com.admission.counsellor.adapter;

import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admission.counsellor.R;
import com.admission.counsellor.model.CollegeResult;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class CollegeResultAdapter extends RecyclerView.Adapter<CollegeResultAdapter.ViewHolder> {

    public interface OnCutoffClickListener {
        void onClick(CollegeResult result);
    }

    private List<CollegeResult> data = new ArrayList<>();
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
        TextView tvName, tvBranch, tvLocation, tvUniversity, tvCutoff, tvCategory;
        Chip chipAutonomous, chipAided;
        MaterialButton btnCutoff;

        ViewHolder(View itemView) {
            super(itemView);
            tvName        = itemView.findViewById(R.id.tv_college_name);
            tvBranch      = itemView.findViewById(R.id.tv_branch);
            tvLocation    = itemView.findViewById(R.id.tv_location);
            tvUniversity  = itemView.findViewById(R.id.tv_university);
            tvCutoff      = itemView.findViewById(R.id.tv_cutoff_rank);
            tvCategory    = itemView.findViewById(R.id.tv_category);
            chipAutonomous = itemView.findViewById(R.id.chip_autonomous);
            chipAided     = itemView.findViewById(R.id.chip_aided);
            btnCutoff     = itemView.findViewById(R.id.btn_view_cutoff);
        }

        void bind(CollegeResult result) {
            tvName.setText(result.getCollegeName());
            tvBranch.setText(result.getBranch());
            tvLocation.setText(result.getDistrict());
            tvUniversity.setText(result.getUniversity());
            tvCutoff.setText("Cutoff Rank: " + result.getCutoffRank()
                    + "  |  " + result.getCutoffPercentile() + "%");
            tvCategory.setText(result.getCategory() + " · " + result.getGender());

            chipAutonomous.setVisibility(result.isAutonomous() ? View.VISIBLE : View.GONE);
            chipAided.setText(result.isAided() ? "Aided" : "Unaided");

            btnCutoff.setOnClickListener(v -> listener.onClick(result));
        }
    }
}