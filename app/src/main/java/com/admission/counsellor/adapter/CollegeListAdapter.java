package com.admission.counsellor.adapter;

import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.admission.counsellor.R;
import com.admission.counsellor.model.College;

import java.util.ArrayList;
import java.util.List;

public class CollegeListAdapter extends RecyclerView.Adapter<CollegeListAdapter.ViewHolder> {

    public interface OnCollegeClickListener {
        void onClick(College college);
    }

    private List<College> data = new ArrayList<>();
    private final OnCollegeClickListener listener;

    public CollegeListAdapter(OnCollegeClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<College> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_college, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() { return data.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCode, tvDistrict, tvCourseCount;

        ViewHolder(View itemView) {
            super(itemView);
            tvName        = itemView.findViewById(R.id.tv_name);
            tvCode        = itemView.findViewById(R.id.tv_code);
            tvDistrict    = itemView.findViewById(R.id.tv_location);
            tvCourseCount = itemView.findViewById(R.id.tv_course_count);
        }

        void bind(College college) {
            tvName.setText(college.getCollegeName());
            tvCode.setText("Code: " + college.getCollegeCode());
            tvDistrict.setText(college.getDistrict() + " | " + college.getUniversity());
            int count = college.getCourses() != null ? college.getCourses().size() : 0;
            tvCourseCount.setText(count + " branches available");
            itemView.setOnClickListener(v -> listener.onClick(college));
        }
    }
}