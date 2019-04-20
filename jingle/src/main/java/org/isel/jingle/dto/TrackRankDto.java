package org.isel.jingle.dto;

import com.google.gson.annotations.SerializedName;

public class TrackRankDto {
    private final String name;
    private final String url;
    private final int duration;
    private int page;

    @SerializedName("@attr")
    private final TrackRankAttrDto attr;

    public TrackRankDto(String name, String url, int duration, TrackRankAttrDto attr) {
        this.name = name;
        this.url = url;
        this.duration = duration;
        this.attr = attr;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getDuration() {
        return duration;
    }

    public TrackRankAttrDto getAttr() {
        return attr;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
