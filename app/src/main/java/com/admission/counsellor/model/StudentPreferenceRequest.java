package com.admission.counsellor.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StudentPreferenceRequest {

    @SerializedName("percentile")
    private double percentile;

    @SerializedName("category")
    private String category;        // OPEN, OBC, SC, ST, NT1, NT2, NT3, VJ, EWS

    @SerializedName("gender")
    private String gender;          // GENERAL, FEMALE

    @SerializedName("admissionType")
    private String admissionType;   // HOME, OTHER

    @SerializedName("round")
    private int round;              // 1, 2, 3

    @SerializedName("branches")
    private List<String> branches;

    @SerializedName("districts")
    private List<String> districts;

    // Constructor
    public StudentPreferenceRequest(double percentile, String category, String gender,
                                    String admissionType, int round,
                                    List<String> branches, List<String> districts) {
        this.percentile = percentile;
        this.category = category;
        this.gender = gender;
        this.admissionType = admissionType;
        this.round = round;
        this.branches = branches;
        this.districts = districts;
    }

    // Getters
    public double getPercentile() { return percentile; }
    public String getCategory() { return category; }
    public String getGender() { return gender; }
    public String getAdmissionType() { return admissionType; }
    public int getRound() { return round; }
    public List<String> getBranches() { return branches; }
    public List<String> getDistricts() { return districts; }

    // Setters
    public void setPercentile(double percentile) { this.percentile = percentile; }
    public void setCategory(String category) { this.category = category; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAdmissionType(String admissionType) { this.admissionType = admissionType; }
    public void setRound(int round) { this.round = round; }
    public void setBranches(List<String> branches) { this.branches = branches; }
    public void setDistricts(List<String> districts) { this.districts = districts; }
}