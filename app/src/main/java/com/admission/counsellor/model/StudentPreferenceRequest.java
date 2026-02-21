package com.admission.counsellor.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StudentPreferenceRequest {

    @SerializedName("percentile")
    private double percentile;

    @SerializedName("category")
    private String category;

    @SerializedName("gender")
    private String gender;

    @SerializedName("admissionType")
    private String admissionType;

    @SerializedName("round")
    private int round;

    // Group labels selected by user (e.g. "Computer Science").
    // The BACKEND expands these to exact DB course names via BranchGroups.expand().
    // Android just sends what the user selected — no client-side expansion needed.
    @SerializedName("branches")
    private List<String> branches;

    // Exact district names (e.g. "Pune", "Mumbai City")
    @SerializedName("districts")
    private List<String> districts;

    public StudentPreferenceRequest(double percentile, String category, String gender,
                                    String admissionType, int round,
                                    List<String> branches, List<String> districts) {
        this.percentile    = percentile;
        this.category      = category;
        this.gender        = gender;
        this.admissionType = admissionType;
        this.round         = round;
        this.branches      = branches;   // sent as group labels directly
        this.districts     = districts;
    }

    public double       getPercentile()    { return percentile; }
    public String       getCategory()      { return category; }
    public String       getGender()        { return gender; }
    public String       getAdmissionType() { return admissionType; }
    public int          getRound()         { return round; }
    public List<String> getBranches()      { return branches; }
    public List<String> getDistricts()     { return districts; }

    public void setPercentile(double v)       { this.percentile = v; }
    public void setCategory(String v)         { this.category = v; }
    public void setGender(String v)           { this.gender = v; }
    public void setAdmissionType(String v)    { this.admissionType = v; }
    public void setRound(int v)               { this.round = v; }
    public void setBranches(List<String> v)   { this.branches = v; }
    public void setDistricts(List<String> v)  { this.districts = v; }
}