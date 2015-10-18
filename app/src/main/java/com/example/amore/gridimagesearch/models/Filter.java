package com.example.amore.gridimagesearch.models;

import java.io.Serializable;

/**
 * Created by amore on 10/18/15.
 */
public class Filter implements Serializable{
    public String filterSize;
    public String filterColor;
    public String filterType;
    public String filterSite;

    public Filter(String filterSize, String filterColor, String filterType, String filterSite) {
        this.filterSize = filterSize;
        this.filterColor = filterColor;
        this.filterType = filterType;
        this.filterSite = filterSite;
    }
}
