package com.admission.counsellor.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Maps Spring Boot's Page<T> JSON response structure
 */
public class PageResponse<T> {

    @SerializedName("content")
    private List<T> content;

    @SerializedName("totalElements")
    private long totalElements;

    @SerializedName("totalPages")
    private int totalPages;

    @SerializedName("number")
    private int currentPage;

    @SerializedName("size")
    private int pageSize;

    @SerializedName("last")
    private boolean last;

    public List<T> getContent() { return content; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public int getCurrentPage() { return currentPage; }
    public int getPageSize() { return pageSize; }
    public boolean isLast() { return last; }
}