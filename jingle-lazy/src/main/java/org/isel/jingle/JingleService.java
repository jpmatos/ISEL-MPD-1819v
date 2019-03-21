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

        Iterable<ArtistDto[]> artistDtos = takeWhile(map, Objects::nonNull);

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
//        return () -> new Iterator<Artist>() {
//            private int page = 1;
//            private int index = 0;
//            private ArtistDto[] currentArray = null;
//            private Artist current = null;
//
//            @Override
//            public boolean hasNext() {
//                if(current != null) return true;
//                if(currentArray == null)
//                    currentArray = api.searchArtist(name, page);
//                if(index >= currentArray.length){
//                    page++;
//                    currentArray = api.searchArtist(name, page);
//                    index = 0;
//                    if(currentArray.length == 0)
//                        return false;
//                }
//                ArtistDto artist = currentArray[index++];
//                current = new Artist(
//                        artist.getName(),
//                        artist.getListeners(),
//                        artist.getMbid(),
//                        artist.getUrl(),
//                        artist.getImage()[0].toString(),
//                        getAlbums(artist.getMbid()),
//                        getTracks(artist.getMbid())
//                );
//                return true;
//            }
//
//            @Override
//            public Artist next() {
//                if(!hasNext()) throw new NoSuchElementException();
//                Artist aux = current;
//                current = null;
//                return aux;
//            }
//        };
    }

    private Iterable<Album> getAlbums(String artistMbid) {
        Iterable<Integer> indexes = iterate(1, i -> i + 1);

        Iterable<AlbumDto[]> albums = map(indexes, i -> api.getAlbums(artistMbid, i));

        Iterable<AlbumDto[]> albumDtos = takeWhile(albums, Objects::nonNull);

        Iterable<AlbumDto> flattenedAlbums = flatMap(albumDtos, LazyQueries::from);

        Iterable<AlbumDto> albumsWithId = filter(flattenedAlbums, album -> album.getMbid() != null);

        return LazyQueries.map(albumsWithId, album ->{
           return new Album(
                   album.getName(),
                   album.getPlaycount(),
                   album.getMbid(),
                   album.getUrl(),
                   album.getImage()[0].toString(),
                   getAlbumTracks(album.getMbid())
           );
        });

//        return () -> new Iterator<Album>() {
//            private int page = 1;
//            private int index = 0;
//            private AlbumDto[] currentArray = null;
//            private Album current = null;
//
//            @Override
//            public boolean hasNext() {
//                if(current != null) return true;
//                if(currentArray == null)
//                    currentArray = api.getAlbums(artistMbid, page);
//                if(index >= currentArray.length){
//                    page++;
//                    currentArray = api.getAlbums(artistMbid, page);
//                    if(currentArray.length == 0)
//                        return false;
//                    index = 0;
//                }
//                AlbumDto album = currentArray[index++];
//                current = new Album(
//                        album.getName(),
//                        album.getPlaycount(),
//                        album.getMbid(),
//                        album.getUrl(),
//                        album.getImage()[0].toString(),
//                        getAlbumTracks(album.getMbid())
//                );
//                return true;
//            }
//
//            @Override
//            public Album next() {
//                if(!hasNext()) throw new NoSuchElementException();
//                Album aux = current;
//                current = null;
//                return aux;
//            }
//        };
    }

    private Iterable<Track> getAlbumTracks(String albumMbid) {
        TrackDto[] albumInfo = api.getAlbumInfo(albumMbid);
        return map(from(albumInfo), track -> new Track(track.getName(), track.getUrl(), track.getDuration()));
//        return () -> new Iterator<Track>() {
//            private TrackDto[] albumInfo;
//            private int index = 0;
//            private Track current;
//
//            @Override
//            public boolean hasNext() {
//                if(albumInfo == null)
//                    albumInfo = api.getAlbumInfo(albumMbid);
//                if(current != null) return true;
//                if(index >= albumInfo.length)
//                    return false;
//                TrackDto track = albumInfo[index++];
//                current = new Track(
//                        track.getName(),
//                        track.getUrl(),
//                        track.getDuration()
//                );
//                return true;
//            }
//
//            @Override
//            public Track next() {
//                if(!hasNext()) throw new NoSuchElementException();
//                Track aux = current;
//                current = null;
//                return aux;
//            }
//        };
    }

    private Iterable<Track> getTracks(String artistMbid) {
        Iterable<Album> albums = getAlbums(artistMbid);

        Iterable<Album> albumsWithID = filter(albums, album -> album.getMbid() != null);

        return flatMap(albumsWithID, Album::getTracks);
//        return () -> new Iterator<Track>() {
//            private Iterator<Album> albums;
//            private Album album;
//            private Iterator<Track> tracks;
//            private Track track;
//            private int skipped;
//
//            @Override
//            public boolean hasNext() {
//                if(track != null) return true;
//                if(albums == null)
//                    albums = getAlbums(artistMbid).iterator();
//                if(album == null)
//                    album = albums.next();
//                if(tracks == null)
//                    tracks = album.getTracks().iterator();
//
//                if(!albums.hasNext() && !tracks.hasNext())
//                    return false;
//
//                if(!tracks.hasNext()){
//                    album = albums.next();
//                    while(album.getMbid() == null) {    //Skip Albums without an mbid
//                        album = albums.next();
//                        skipped++;
//                        System.out.println("Skipped: " + skipped);
//                    }
//                    tracks = album.getTracks().iterator();
//                    if(!tracks.hasNext())
//                        return false;
//                }
//
//                track = tracks.next();
//                return true;
//            }
//
//            @SuppressWarnings("Duplicates")
//            @Override
//            public Track next() {
//                if(!hasNext()) throw new NoSuchElementException();
//                Track aux = track;
//                track = null;
//                return aux;
//            }
//        };
    }
}
