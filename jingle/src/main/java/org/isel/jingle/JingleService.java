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

import io.reactivex.Completable;
import org.isel.jingle.dto.AlbumDto;
import org.isel.jingle.dto.ArtistDto;
import org.isel.jingle.dto.TrackDto;
import org.isel.jingle.dto.TrackRankDto;
import org.isel.jingle.model.Album;
import org.isel.jingle.model.Artist;
import org.isel.jingle.model.Track;
import org.isel.jingle.model.TrackRank;
import util.StreamUtils;
import util.req.AsyncHttpRequest;
import io.reactivex.Observable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Stream.*;


public class JingleService {

    final LastfmWebApi api;

    public JingleService(LastfmWebApi api) {
        this.api = api;
    }

    public JingleService() {
        this(new LastfmWebApi(new AsyncHttpRequest()));
    }

    public Observable<Artist> searchArtist(String name) {

        Stream<CompletableFuture<ArtistDto[]>> cfstream = iterate(1, i -> i + 1)
                .map(i -> api.searchArtist(name, i));


        return Observable.fromIterable(cfstream::iterator)
                .flatMap(Observable::fromFuture)
                .takeWhile(arr -> arr.length != 0)
                .flatMap(Observable::fromArray)
                .map(artist -> new Artist(
                        artist.getName(),
                        artist.getListeners(),
                        artist.getMbid(),
                        artist.getUrl(),
                        artist.getImage()[0].toString(),
                        getAlbums(artist.getMbid()),
                        getTracks(artist.getMbid())
                ));

//        Stream<Integer> indexes = iterate(1, i -> i + 1);
//        Stream<ArtistDto[]> map = indexes.map(i -> api.searchArtist(name, i));
//        Stream<ArtistDto[]> artistDtos = map.takeWhile(arr -> arr.length != 0);
//        Stream<ArtistDto> flattenedArtists = artistDtos.flatMap(Stream::of);
//        return flattenedArtists.map(artist -> new Artist(
//                        artist.getName(),
//                        artist.getListeners(),
//                        artist.getMbid(),
//                        artist.getUrl(),
//                        artist.getImage()[0].toString(),
//                        () -> getAlbums(artist.getMbid()),
//                        () -> getTracks(artist.getMbid())
//                )
//        );
    }

    private Observable<Album> getAlbums(String artistMbid) {

        Stream<CompletableFuture<AlbumDto[]>> cfstream = iterate(1, i -> i + 1)
                .map(i -> api.getAlbums(artistMbid, i));

        return Observable.fromIterable(cfstream::iterator)
                .flatMap(Observable::fromFuture)
                .takeWhile(arr -> arr.length != 0)
                .flatMap(Observable::fromArray)
                .map(album -> new Album(
                        album.getName(),
                        album.getPlaycount(),
                        album.getMbid(),
                        album.getUrl(),
                        album.getImage()[0].toString(),
                        getAlbumTracks(album.getMbid()))
                );

//        Stream<Integer> indexes = iterate(1, i -> i + 1);
//            Stream<AlbumDto[]> albums = indexes.map(i -> api.getAlbums(artistMbid, i));
//            Stream<AlbumDto[]> albumDtos = albums.takeWhile(arr -> arr.length != 0);
//            Stream<AlbumDto> flattenedAlbums = albumDtos.flatMap(Stream::of);
//            return flattenedAlbums.map(album -> new Album(
//                            album.getName(),
//                            album.getPlaycount(),
//                            album.getMbid(),
//                            album.getUrl(),
//                            album.getImage()[0].toString(),
//                            getAlbumTracks(album.getMbid())
//            );
    }

    private Observable<Track> getAlbumTracks(String albumMbid) {
        return Observable.fromFuture(api.getAlbumInfo(albumMbid))
                .flatMap(Observable::fromArray)
                .map(track -> new Track(
                        track.getName(),
                        track.getUrl(),
                        track.getDuration()
                ));
    }
//
    private Observable<Track> getTracks(String artistMbid) {
        return getAlbums(artistMbid)
                .filter(album -> album.getMbid() != null)
                .flatMap(Album::getTracks);
    }
//
//    public Stream<TrackRank> getTopTracks(String country){
//        Stream<Integer> indexes = iterate(1, i -> i + 1);
//        Stream<TrackRankDto[]> topTracksPages = indexes.map(i -> api.getTopTracks(country, i));
//        Stream<TrackRankDto[]> topTracksPagesLimited = topTracksPages.takeWhile(arr -> arr.length != 0);
//        Stream<TrackRankDto> topTracks = topTracksPagesLimited.flatMap(Stream::of);
//        return topTracks.map(topTrack -> new TrackRank(
//                topTrack.getName(),
//                topTrack.getUrl(),
//                topTrack.getDuration(),
//                topTrack.getAttr().getRank() + ((topTrack.getPage()-1) * 50) + 1
//        ));
//    }
//
//    public Stream<TrackRank> getTracksRank(String artistsMbId, String country){
//        Stream<Track> tracks = getTracks(artistsMbId);
//        Stream<TrackRank> top100Tracks = getTopTracks(country).takeWhile(track -> track.getRank() <= 100); //.limit(100)
//        Supplier<Stream<TrackRank>> sup = StreamUtils.merge(
//                () -> tracks,
//                () -> top100Tracks,
//                (track, trackRank) -> track.getName().equals(trackRank.getName()),
//                (track, trackRank) -> trackRank != null ? trackRank : new TrackRank(
//                        track.getName(),
//                        track.getUrl(),
//                        track.getDuration(),
//                        0)
//                );
//        return sup.get();
//    }
}
