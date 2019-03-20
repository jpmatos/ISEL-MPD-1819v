package org.isel.jingle.dto;

public class GetAlbumDto {
    private final TopAlbumsDto topalbums;

    public GetAlbumDto(TopAlbumsDto topalbums) {
        this.topalbums = topalbums;
    }

    public TopAlbumsDto getTopalbums() {
        return topalbums;
    }
}
