package com.admission.counsellor.model;

import com.google.gson.annotations.SerializedName;

public class ShortlistRequest {

    @SerializedName("collegeCode")
    private String collegeCode;

    @SerializedName("courseCode")
    private String courseCode;

    @SerializedName("courseName")
    private String courseName;

    @SerializedName("studentPercentile")
    private Double studentPercentile;

    @SerializedName("category")
    private String category;

    @SerializedName("gender")
    private String gender;

    @SerializedName("admissionType")
    private String admissionType;

    @SerializedName("capCategoryCode")
    private String capCategoryCode;

    public ShortlistRequest(String collegeCode, String courseCode, String courseName,
                            Double studentPercentile, String category, String gender,
                            String admissionType, String capCategoryCode) {
        this.collegeCode       = collegeCode;
        this.courseCode        = courseCode;
        this.courseName        = courseName;
        this.studentPercentile = studentPercentile;
        this.category          = category;
        this.gender            = gender;
        this.admissionType     = admissionType;
        this.capCategoryCode   = capCategoryCode;
    }

    public String getCollegeCode()       { return collegeCode; }
    public String getCourseCode()        { return courseCode; }
    public String getCourseName()        { return courseName; }
    public Double getStudentPercentile() { return studentPercentile; }
    public String getCategory()          { return category; }
    public String getGender()            { return gender; }
    public String getAdmissionType()     { return admissionType; }
    public String getCapCategoryCode()   { return capCategoryCode; }
}
