package org.isel.jingle.dto;

public class SearchArtistDto {
    private final ResultsDto results;

    public SearchArtistDto(ResultsDto results) {
        this.results = results;
    }

    public ResultsDto getResults() {
        return results;
    }
}
