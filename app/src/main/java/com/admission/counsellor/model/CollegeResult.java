package com.admission.counsellor.model;

import com.google.gson.annotations.SerializedName;

public class CollegeResult {

    @SerializedName("collegeName")
    private String collegeName;

    @SerializedName("collegeCode")
    private String collegeCode;

    @SerializedName("courseName")
    private String courseName;

    @SerializedName("cutoffPercentile")
    private Double cutoffPercentile;

    @SerializedName("round")
    private Integer round;

    @SerializedName("risk")
    private String risk;

    @SerializedName("probability")
    private double probability;

    @SerializedName("confidence")
    private String confidence;

    @SerializedName("district")
    private String district;

    @SerializedName("region")
    private String region;

    @SerializedName("address")
    private String address;

    @SerializedName("fundingType")
    private String fundingType;

    @SerializedName("isAutonomous")
    private Boolean isAutonomous;

    @SerializedName("intake")
    private Integer intake;

    // Getters
    public String getCollegeName()      { return collegeName; }
    public String getCollegeCode()      { return collegeCode; }
    public String getCourseName()       { return courseName; }
    public Double getCutoffPercentile() { return cutoffPercentile; }
    public Integer getRound()           { return round; }
    public String getRisk()             { return risk; }
    public double getProbability()      { return probability; }
    public String getConfidence()       { return confidence; }
    public String getDistrict()         { return district; }
    public String getRegion()           { return region; }
    public String getAddress()          { return address; }
    public String getFundingType()      { return fundingType; }
    public Boolean getIsAutonomous()    { return isAutonomous; }
    public Integer getIntake()          { return intake; }

    // Convenience helpers used by adapter
    public String getBranch()           { return courseName; }
    public String getUniversity()       { return region; }
    public boolean isAided() {
        return fundingType != null && fundingType.toLowerCase().contains("aided");
    }
}