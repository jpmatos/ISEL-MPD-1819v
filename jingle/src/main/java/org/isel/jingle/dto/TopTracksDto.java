package org.isel.jingle.dto;

import com.google.gson.annotations.SerializedName;

public class TopTracksDto {
    private final TrackRankDto[] track;

    @SerializedName("@attr")
    private final TopTracksAttrDto attr;

    public TopTracksDto(TrackRankDto[] track, TopTracksAttrDto attr) {
        this.track = track;
        this.attr = attr;
    }

    public TrackRankDto[] getTrack() {
        return track;
    }

    public TopTracksAttrDto getAttr() {
        return attr;
    }
}
