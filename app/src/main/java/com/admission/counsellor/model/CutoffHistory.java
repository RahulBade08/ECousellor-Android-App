package com.admission.counsellor.model;

import com.google.gson.annotations.SerializedName;

public class CutoffHistory {

    @SerializedName("year")
    private int year;

    @SerializedName("category")
    private String category;

    @SerializedName("cutoffRank")
    private int cutoffRank;

    @SerializedName("cutoffPercentile")
    private double cutoffPercentile;

    // Getters
    public int getYear() { return year; }
    public String getCategory() { return category; }
    public int getCutoffRank() { return cutoffRank; }
    public double getCutoffPercentile() { return cutoffPercentile; }
}