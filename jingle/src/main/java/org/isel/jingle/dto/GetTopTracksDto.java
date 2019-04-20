package org.isel.jingle.dto;

public class GetTopTracksDto {
    private final TopTracksDto tracks;

    public GetTopTracksDto(TopTracksDto tracks) {
        this.tracks = tracks;
    }

    public TopTracksDto getTracks() {
        return tracks;
    }
}
