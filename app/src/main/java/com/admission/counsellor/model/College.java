package com.admission.counsellor.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class College {

    @SerializedName("collegeId")
    private long collegeId;

    @SerializedName("collegeCode")
    private String collegeCode;

    @SerializedName("collegeName")
    private String collegeName;

    @SerializedName("courseUniversity")
    private String courseUniversity;

    @SerializedName("fundingType")
    private String fundingType;

    @SerializedName("isAutonomous")
    private Boolean isAutonomous;

    @SerializedName("minorityStatus")
    private String minorityStatus;

    @SerializedName("totalIntake")
    private Integer totalIntake;

    @SerializedName("address")
    private String address;

    @SerializedName("region")
    private String region;

    @SerializedName("district")
    private String district;

    @SerializedName("courses")
    private List<CourseDTO> courses;

    // Getters
    public long getId()                  { return collegeId; }
    public long getCollegeId()           { return collegeId; }
    public String getCollegeCode()       { return collegeCode; }
    public String getCollegeName()       { return collegeName; }
    public String getUniversity()        { return courseUniversity; }
    public String getCourseUniversity()  { return courseUniversity; }
    public String getFundingType()       { return fundingType; }
    public Boolean getIsAutonomous()     { return isAutonomous != null && isAutonomous; }
    public boolean isAutonomous()        { return isAutonomous != null && isAutonomous; }
    public String getMinorityStatus()    { return minorityStatus; }
    public Integer getTotalIntake()      { return totalIntake; }
    public String getAddress()           { return address; }
    public String getRegion()            { return region; }
    public String getDistrict()          { return district; }
    public List<CourseDTO> getCourses()  { return courses; }

    // Convenience - isAided based on fundingType
    public boolean isAided() {
        return fundingType != null && fundingType.toLowerCase().contains("aided");
    }

    // Nested CourseDTO matching backend CollegeDTO.CourseDTO
    public static class CourseDTO {

        @SerializedName("courseId")
        private Long courseId;

        @SerializedName("courseCode")
        private String courseCode;

        @SerializedName("courseName")
        private String courseName;

        @SerializedName("courseStatus")
        private String courseStatus;

        @SerializedName("intake")
        private Integer intake;

        @SerializedName("university")
        private String university;

        @SerializedName("isAutonomous")
        private Boolean isAutonomous;

        @SerializedName("minorityStatus")
        private String minorityStatus;

        @SerializedName("shift")
        private String shift;

        @SerializedName("accreditation")
        private String accreditation;

        @SerializedName("gender")
        private String gender;

        public Long getCourseId()         { return courseId; }
        public String getCourseCode()     { return courseCode; }
        public String getCourseName()     { return courseName; }
        public String getCourseStatus()   { return courseStatus; }
        public Integer getIntake()        { return intake; }
        public String getUniversity()     { return university; }
        public Boolean getIsAutonomous()  { return isAutonomous; }
        public String getMinorityStatus() { return minorityStatus; }
        public String getShift()          { return shift; }
        public String getAccreditation()  { return accreditation; }
        public String getGender()         { return gender; }

        // Aliases used by existing adapter code
        public String getBranchName()     { return courseName; }
        public String getBranchCode()     { return courseCode; }
    }
}