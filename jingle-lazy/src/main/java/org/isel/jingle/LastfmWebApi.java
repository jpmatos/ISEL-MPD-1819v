/*
 * GNU General Public License v3.0
 *
 * Copyright (c) 2019, Miguel Gamboa (gamboa.pt)
 *
 *   All rights granted under this License are granted for the term of
 * copyright on the Program, and are irrevocable provided the stated
 * conditions are met.  This License explicitly affirms your unlimited
 * permission to run the unmodified Program.  The output from running a
 * covered work is covered by this License only if the output, given its
 * content, constitutes a covered work.  This License acknowledges your
 * rights of fair use or other equivalent, as provided by copyright law.
 *
 *   You may make, run and propagate covered works that you do not
 * convey, without conditions so long as your license otherwise remains
 * in force.  You may convey covered works to others for the sole purpose
 * of having them make modifications exclusively for you, or provide you
 * with facilities for running those works, provided that you comply with
 * the terms of this License in conveying all material for which you do
 * not control copyright.  Those thus making or running the covered works
 * for you must do so exclusively on your behalf, under your direction
 * and control, on terms that prohibit them from making any copies of
 * your copyrighted material outside their relationship with you.
 *
 *   Conveying under any other circumstances is permitted solely under
 * the conditions stated below.  Sublicensing is not allowed; section 10
 * makes it unnecessary.
 *
 */

package org.isel.jingle;

import com.google.gson.Gson;
import org.isel.jingle.dto.*;
import org.isel.jingle.util.req.Request;


public class LastfmWebApi {
    private static final String LASTFM_API_KEY = "a7e0132cafc3d836c175c7c71c09641f";
    private static final String LASTFM_HOST = "http://ws.audioscrobbler.com/2.0/";
    private static final String LASTFM_SEARCH = LASTFM_HOST
                                                    + "?method=artist.search&format=json&artist=%s&page=%d&api_key="
                                                    + LASTFM_API_KEY;

    private static final String LASTFM_GET_ALBUMS = LASTFM_HOST
                                                    + "?method=artist.gettopalbums&format=json&mbid=%s&page=%d&api_key="
                                                    + LASTFM_API_KEY;

    private static final String LASTFM_GET_ALBUM_INFO = LASTFM_HOST
                                                    + "?method=album.getinfo&format=json&mbid=%s&api_key="
                                                    + LASTFM_API_KEY;
    private final Request request;
    protected final Gson gson;

    public LastfmWebApi(Request request) {
        this(request, new Gson());
    }

    public LastfmWebApi(Request request, Gson gson) {
        this.request = request;
        this.gson = gson;
    }

    public ArtistDto[] searchArtist(String name, int page) {
        String path = String.format(LASTFM_SEARCH, name, page);
        Iterable<String> lines = request.getLines(path);
        String body = String.join("", lines);
        SearchArtistDto dto = gson.fromJson(body, SearchArtistDto.class);
        return dto.getResults().getArtistmatches().getArtist();
    }
    public AlbumDto[] getAlbums(String artistMbid, int page) {
        String path = String.format(LASTFM_GET_ALBUMS, artistMbid, page);
        Iterable<String> lines = request.getLines(path);
        String body = String.join("", lines);
        GetAlbumDto dto = gson.fromJson(body, GetAlbumDto.class);
        return dto.getTopalbums().getAlbum();
    }
    public TrackDto[] getAlbumInfo(String albumMbid){
        String path = String.format(LASTFM_GET_ALBUM_INFO, albumMbid);
        Iterable<String> lines = request.getLines(path);
        String body = String.join("", lines);
        GetAlbumInfoDto dto = gson.fromJson(body, GetAlbumInfoDto.class);
        return dto.getAlbum().getTracks().getTrack();
    }
}
