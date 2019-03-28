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

import org.isel.jingle.dto.AlbumDto;
import org.isel.jingle.dto.ArtistDto;
import org.isel.jingle.dto.TrackDto;
import org.isel.jingle.model.Album;
import org.isel.jingle.model.Artist;
import org.isel.jingle.model.Track;
import org.isel.jingle.util.queries.LazyQueries;
import org.isel.jingle.util.req.BaseRequest;
import org.isel.jingle.util.req.HttpRequest;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.isel.jingle.util.queries.LazyQueries.*;

public class JingleService {

    final LastfmWebApi api;

    public JingleService(LastfmWebApi api) {
        this.api = api;
    }

    public JingleService() {
        this(new LastfmWebApi(new BaseRequest(HttpRequest::openStream)));
    }

    public Iterable<Artist> searchArtist(String name) {
        Iterable<Integer> indexes = iterate(1, i -> i + 1);
        Iterable<ArtistDto[]> map = map(indexes, i -> api.searchArtist(name, i));
        Iterable<ArtistDto[]> artistDtos = takeWhile(map, arr -> arr.length != 0);
        Iterable<ArtistDto> flattenedArtists = flatMap(artistDtos, LazyQueries::from);

        return LazyQueries.map(flattenedArtists, artist -> new Artist(
                        artist.getName(),
                        artist.getListeners(),
                        artist.getMbid(),
                        artist.getUrl(),
                        artist.getImage()[0].toString(),
                        getAlbums(artist.getMbid()),
                        getTracks(artist.getMbid())
                )
        );
    }

    private Iterable<Album> getAlbums(String artistMbid) {
        return () -> {
            Iterable<Integer> indexes = iterate(1, i -> i + 1);
            Iterable<AlbumDto[]> albums = map(indexes, i -> api.getAlbums(artistMbid, i));
            Iterable<AlbumDto[]> albumDtos = takeWhile(albums, arr -> arr.length != 0);
            Iterable<AlbumDto> flattenedAlbums = flatMap(albumDtos, LazyQueries::from);
            return LazyQueries.map(flattenedAlbums, album -> new Album(
                            album.getName(),
                            album.getPlaycount(),
                            album.getMbid(),
                            album.getUrl(),
                            album.getImage()[0].toString(),
                            getAlbumTracks(album.getMbid()))
            ).iterator();
        };
    }

    private Iterable<Track> getAlbumTracks(String albumMbid) {
        return () -> map(
                        from(api.getAlbumInfo(albumMbid)),
                        track -> new Track(
                                track.getName(),
                                track.getUrl(),
                                track.getDuration()
                        )
        ).iterator();
    }

    private Iterable<Track> getTracks(String artistMbid) {
        return () -> flatMap(
                            filter(getAlbums(artistMbid), album -> album.getMbid() != null),
                            Album::getTracks
        ).iterator();
    }
}
