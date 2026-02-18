package com.admission.counsellor.model;

/**
 * All constant dropdown values used in the Student Form.
 * Update these lists to exactly match what your Spring Boot backend accepts.
 */
public class AppConstants {

    // Matches your "category" field
    public static final String[] CATEGORIES = {
            "OPEN", "OBC", "SC", "ST", "NT1", "NT2", "NT3", "VJ", "EWS", "TFWS"
    };

    // Matches your "gender" field
    public static final String[] GENDERS = {
            "GENERAL", "LADIES"
    };

    // Matches your "admissionType" field
    public static final String[] ADMISSION_TYPES = {
            "STATE", "HOME", "OTHER"
    };

    // Matches your "round" field
    public static final Integer[] ROUNDS = { 1, 2, 3, 4 };

    // Common Maharashtra engineering branches
    public static final String[] BRANCHES = {
            "Computer Science and Engineering",
            "Information Technology",
            "Electronics and Telecommunication Engineering",
            "Mechanical Engineering",
            "Civil Engineering",
            "Electrical Engineering",
            "Artificial Intelligence and Data Science",
            "Chemical Engineering",
            "Production Engineering",
            "Instrumentation Engineering"
    };

    // Maharashtra districts
    public static final String[] DISTRICTS = {
            "Pune", "Mumbai", "Thane", "Nashik", "Aurangabad",
            "Nagpur", "Kolhapur", "Solapur", "Amravati",
            "Nanded", "Sangli", "Satara", "Raigad", "Ahmednagar"
    };
}