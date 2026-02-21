package com.admission.counsellor.model;

import com.google.gson.annotations.SerializedName;

public class ViewEventRequest {

    @SerializedName("collegeCode")
    private String collegeCode;

    @SerializedName("courseCode")
    private String courseCode;          // nullable — pass null for college-level view

    @SerializedName("studentPercentile")
    private Double studentPercentile;

    @SerializedName("category")
    private String category;

    @SerializedName("gender")
    private String gender;

    @SerializedName("admissionType")
    private String admissionType;

    public ViewEventRequest(String collegeCode, String courseCode,
                            Double studentPercentile, String category,
                            String gender, String admissionType) {
        this.collegeCode       = collegeCode;
        this.courseCode        = courseCode;
        this.studentPercentile = studentPercentile;
        this.category          = category;
        this.gender            = gender;
        this.admissionType     = admissionType;
    }

    public String getCollegeCode()         { return collegeCode; }
    public String getCourseCode()          { return courseCode; }
    public Double getStudentPercentile()   { return studentPercentile; }
    public String getCategory()            { return category; }
    public String getGender()              { return gender; }
    public String getAdmissionType()       { return admissionType; }
}
