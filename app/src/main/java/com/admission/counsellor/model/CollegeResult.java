package com.admission.counsellor.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents one recommended college row returned by the Spring Boot backend.
 * Adjust field names below to match your actual API response JSON keys.
 */
public class CollegeResult {

    @SerializedName("collegeCode")
    private String collegeCode;

    @SerializedName("collegeName")
    private String collegeName;

    @SerializedName("branch")
    private String branch;

    @SerializedName("district")
    private String district;

    @SerializedName("university")
    private String university;

    @SerializedName("cutoffPercentile")
    private double cutoffPercentile;

    @SerializedName("cutoffRank")
    private int cutoffRank;

    @SerializedName("intake")
    private int intake;

    @SerializedName("autonomous")
    private boolean autonomous;

    @SerializedName("aided")
    private boolean aided;

    @SerializedName("category")
    private String category;

    @SerializedName("gender")
    private String gender;

    // ── Getters ──────────────────────────────────────
    public String getCollegeCode() { return collegeCode; }
    public String getCollegeName() { return collegeName; }
    public String getBranch() { return branch; }
    public String getDistrict() { return district; }
    public String getUniversity() { return university; }
    public double getCutoffPercentile() { return cutoffPercentile; }
    public int getCutoffRank() { return cutoffRank; }
    public int getIntake() { return intake; }
    public boolean isAutonomous() { return autonomous; }
    public boolean isAided() { return aided; }
    public String getCategory() { return category; }
    public String getGender() { return gender; }
}