package com.admission.counsellor.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class College {

    @SerializedName("id")
    private long id;

    @SerializedName("collegeCode")
    private String collegeCode;

    @SerializedName("collegeName")
    private String collegeName;

    @SerializedName("university")
    private String university;

    @SerializedName("district")
    private String district;

    @SerializedName("autonomous")
    private boolean autonomous;

    @SerializedName("aided")
    private boolean aided;

    @SerializedName("courses")
    private List<Course> courses;

    @SerializedName("website")
    private String website;

    @SerializedName("phone")
    private String phone;

    @SerializedName("address")
    private String address;

    // ── Getters ──────────────────────────────────────
    public long getId() { return id; }
    public String getCollegeCode() { return collegeCode; }
    public String getCollegeName() { return collegeName; }
    public String getUniversity() { return university; }
    public String getDistrict() { return district; }
    public boolean isAutonomous() { return autonomous; }
    public boolean isAided() { return aided; }
    public List<Course> getCourses() { return courses; }
    public String getWebsite() { return website; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    // ── Inner class: Course ───────────────────────────
    public static class Course {

        @SerializedName("branchCode")
        private String branchCode;

        @SerializedName("branchName")
        private String branchName;

        @SerializedName("intake")
        private int intake;

        public String getBranchCode() { return branchCode; }
        public String getBranchName() { return branchName; }
        public int getIntake() { return intake; }
    }
}