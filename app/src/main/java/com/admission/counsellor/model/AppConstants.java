package com.admission.counsellor.model;

public class AppConstants {

    public static final String[] CATEGORIES = {
            "OPEN", "OBC", "SC", "ST", "NT1", "NT2", "NT3", "VJ", "EWS", "TFWS"
    };

    public static final String[] GENDERS = {
            "GENERAL", "LADIES"
    };

    public static final String[] ADMISSION_TYPES = {
            "STATE", "HOME", "OTHER"
    };

    public static final Integer[] ROUNDS = { 1, 2, 3, 4 };

    // Group labels shown in the searchable branch dropdown.
    // Backend's BranchGroups.expand() converts each label to exact DB course names.
    // e.g. "Computer Science" -> 24 exact DB names -> SQL IN query finds all colleges.
    public static final String[] BRANCHES = {
            "Computer Science",
            "Information Technology & Data Science",
            "Electronics & Communication",
            "Electrical Engineering",
            "Mechanical Engineering",
            "Civil Engineering",
            "Artificial Intelligence & ML",
            "Chemical & Petroleum Technology",
            "Textile & Fibre Technology",
            "Food & Bio Technology",
            "Production & Manufacturing",
            "Other Engineering"
    };

    // Exact district names as stored in DB.
    public static final String[] DISTRICTS = {
            "Ahmednagar", "Akola", "Amravati", "Beed", "Bhandara",
            "Buldhana", "Chandrapur", "Chhatrapati Sambhajinagar", "Dharashiv",
            "Dhule", "Jalgaon", "Jalna", "Kolhapur", "Latur",
            "Mumbai City", "Mumbai Suburban", "Nagpur", "Nanded",
            "Nandurbar", "Nashik", "Palghar", "Parbhani", "Pune",
            "Raigad", "Ratnagiri", "Sangli", "Satara", "Sindhudurg",
            "Solapur", "Thane", "Wardha", "Washim", "Yavatmal"
    };
}