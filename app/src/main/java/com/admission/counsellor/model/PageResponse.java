package com.admission.counsellor.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Maps Spring Boot's Page<T> JSON response.
 *
 * Spring Page JSON structure:
 * {
 *   "content": [...],
 *   "pageable": { "pageNumber": 0, "pageSize": 500, ... },
 *   "totalElements": 342,
 *   "totalPages": 1,
 *   "last": true,
 *   "size": 500,
 *   "number": 0,
 *   "numberOfElements": 342,
 *   "first": true,
 *   "empty": false
 * }
 *
 * BUG FIX: @SerializedName annotations must exactly match Spring's JSON field names.
 * Without these, Gson silently returns null for all fields → empty list shown in UI.
 */
public class PageResponse<T> {

    @SerializedName("content")
    private List<T> content;

    @SerializedName("totalElements")
    private long totalElements;

    @SerializedName("totalPages")
    private int totalPages;

    @SerializedName("number")
    private int number;              // current page (0-indexed)

    @SerializedName("size")
    private int size;                // page size requested

    @SerializedName("numberOfElements")
    private int numberOfElements;    // elements in this page

    @SerializedName("last")
    private boolean last;

    @SerializedName("first")
    private boolean first;

    @SerializedName("empty")
    private boolean empty;

    // ── Getters ───────────────────────────────────────────────────────────────

    public List<T> getContent()        { return content; }
    public long    getTotalElements()  { return totalElements; }
    public int     getTotalPages()     { return totalPages; }
    public int     getNumber()         { return number; }
    public int     getSize()           { return size; }
    public int     getNumberOfElements() { return numberOfElements; }
    public boolean isLast()            { return last; }
    public boolean isFirst()           { return first; }
    public boolean isEmpty()           { return empty; }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setContent(List<T> content)            { this.content = content; }
    public void setTotalElements(long totalElements)   { this.totalElements = totalElements; }
    public void setTotalPages(int totalPages)          { this.totalPages = totalPages; }
    public void setNumber(int number)                  { this.number = number; }
    public void setSize(int size)                      { this.size = size; }
    public void setNumberOfElements(int n)             { this.numberOfElements = n; }
    public void setLast(boolean last)                  { this.last = last; }
    public void setFirst(boolean first)                { this.first = first; }
    public void setEmpty(boolean empty)                { this.empty = empty; }
}
