package org.isel.jingle;

import org.isel.jingle.dto.AlbumDto;
import org.isel.jingle.dto.ArtistDto;
import org.isel.jingle.dto.TrackDto;
import org.isel.jingle.dto.TrackRankDto;
import org.junit.Test;
import util.req.AsyncCompletionHandlerBaseCount;
import util.req.AsyncHttpRequest;
import util.req.AsyncRequest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static junit.framework.Assert.assertEquals;


public class LastfmWebApiTest {
    @Test
    public void searchForArtistsNamedDavid() throws ExecutionException, InterruptedException {
        AsyncRequest req = new AsyncHttpRequest(new AsyncCompletionHandlerBaseCount());
        LastfmWebApi api = new LastfmWebApi(req);
        CompletableFuture<ArtistDto[]> artists = api.searchArtist("david", 1);
        String name = artists.get()[0].getName();
        assertEquals("David Bowie", name);
    }

    @Test
    public void getTopAlbumsFromMuse() throws ExecutionException, InterruptedException {
        AsyncRequest req = new AsyncHttpRequest(new AsyncCompletionHandlerBaseCount());
        LastfmWebApi api = new LastfmWebApi(req);
        CompletableFuture<ArtistDto[]> artists = api.searchArtist("muse", 1);
        String mbid = artists.get()[0].getMbid();
        CompletableFuture<AlbumDto[]> albums = api.getAlbums(mbid, 1);
        assertEquals("Black Holes and Revelations", albums.get()[0].getName());
    }

    @Test
    public void getStarlightFromBlackHolesAlbumOfMuse() throws ExecutionException, InterruptedException {
        AsyncRequest req = new AsyncHttpRequest(new AsyncCompletionHandlerBaseCount());
        LastfmWebApi api = new LastfmWebApi(req);
        CompletableFuture<ArtistDto[]> artistsCF = api.searchArtist("muse", 1);
        String mbid = artistsCF.get()[0].getMbid();
        AlbumDto album = api.getAlbums(mbid, 1).get()[0];
        TrackDto track = api.getAlbumInfo(album.getMbid()).get()[1];
        assertEquals("Starlight", track.getName());
    }

    @Test
    public void getTopTracksFromSpainFirstPage() throws ExecutionException, InterruptedException {
        AsyncRequest req = new AsyncHttpRequest(new AsyncCompletionHandlerBaseCount());
        LastfmWebApi api = new LastfmWebApi(req);
        CompletableFuture<TrackRankDto[]> topTracks = api.getTopTracks("Spain", 1);
        TrackRankDto firstRankingTrack = topTracks.get()[0];
        String trackName = firstRankingTrack.getName();
        int trackRank = firstRankingTrack.getAttr().getRank();

        assertEquals("The Less I Know the Better", trackName);
        assertEquals(1, trackRank + 1);
    }
}
