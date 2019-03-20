package org.isel.jingle.dto;

public class ResultsDto {
    private final ArtistMatchesDto artistmatches;

    public ResultsDto(ArtistMatchesDto artistmatches) {
        this.artistmatches = artistmatches;
    }

    public ArtistMatchesDto getArtistmatches() {
        return artistmatches;
    }
}
