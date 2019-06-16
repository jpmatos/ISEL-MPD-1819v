package org.isel.jingle;

import io.reactivex.Observable;
import org.isel.jingle.model.Album;
import org.isel.jingle.model.Artist;
import org.isel.jingle.model.Track;
import org.isel.jingle.model.TrackRank;
import org.junit.Test;
import util.req.AsyncCompletionHandlerBaseCount;
import util.req.AsyncHttpRequest;

import static junit.framework.Assert.assertEquals;

public class JingleServiceTest {

    @Test   //passes
    public void searchHiperAndCountAllResults() {
        AsyncCompletionHandlerBaseCount asyncHandler = new AsyncCompletionHandlerBaseCount();
        JingleService service = new JingleService(new LastfmWebApi(new AsyncHttpRequest(asyncHandler)));
        Observable<Artist> artists = service.searchArtist("hiper").cache();
        assertEquals(0, asyncHandler.count);
        long count = artists.count().blockingGet().longValue();
        assertEquals(711, count);
        assertEquals(25, asyncHandler.count);
        Artist last = artists.take(count - 1).blockingFirst(); //last(artists);
        assertEquals("hiperkarma", last.getName());
        assertEquals(25, asyncHandler.count);
    }

    @Test   //passes
    public void getFirstAlbumOfMuse() {
        AsyncCompletionHandlerBaseCount asyncHandler = new AsyncCompletionHandlerBaseCount();
        JingleService service = new JingleService(new LastfmWebApi(new AsyncHttpRequest(asyncHandler)));
        Observable<Artist> artists = service.searchArtist("muse");
        assertEquals(0, asyncHandler.count);
        Artist muse = artists.blockingFirst();
        assertEquals(1, asyncHandler.count);
        Observable<Album> albums = muse.getAlbums().get();
        assertEquals(1, asyncHandler.count);
        Album first = albums.blockingFirst();
        assertEquals(2, asyncHandler.count);
        assertEquals("Black Holes and Revelations", first.getName());
    }

    @Test   //passes
    public void get111AlbumsOfMuse() {
        AsyncCompletionHandlerBaseCount asyncHandler = new AsyncCompletionHandlerBaseCount();
        JingleService service = new JingleService(new LastfmWebApi(new AsyncHttpRequest(asyncHandler)));
        Observable<Album> albums = service
                .searchArtist("muse")
                .blockingFirst()
                .getAlbums()
                .get()
                .take(111);
        assertEquals(111, albums.count().blockingGet().longValue());
        assertEquals(4, asyncHandler.count); // 1 for artist + 3 pages of albums each with 50 albums
    }

    @Test   //passes
    public void getSecondSongFromBlackHolesAlbumOfMuse() {
        AsyncCompletionHandlerBaseCount asyncHandler = new AsyncCompletionHandlerBaseCount();
        JingleService service = new JingleService(new LastfmWebApi(new AsyncHttpRequest(asyncHandler)));
        Album blackHoles = service.searchArtist("muse").blockingFirst().getAlbums().get().blockingFirst();
        assertEquals(2, asyncHandler.count); // 1 for artist + 1 page of albums
        assertEquals("Black Holes and Revelations", blackHoles.getName());
        Track song = blackHoles.getTracks().get().skip(1).blockingFirst();
        assertEquals(3, asyncHandler.count); // + 1 to getTracks
        assertEquals("Starlight", song.getName());
    }

    @Test   //passes
    public void get42thTrackOfMuse() {
        AsyncCompletionHandlerBaseCount asyncHandler = new AsyncCompletionHandlerBaseCount();
        JingleService service = new JingleService(new LastfmWebApi(new AsyncHttpRequest(asyncHandler)));
        Observable<Track> tracks = service.searchArtist("muse").blockingFirst().getTracks().get();
        assertEquals(1, asyncHandler.count); // 1 for artist + 0 for tracks because it fetches lazily
        Track track = tracks.skip(42).blockingFirst(); // + 1 to getAlbums + 4 to get tracks of first 4 albums.
        assertEquals("MK Ultra", track.getName());
        assertEquals(6, asyncHandler.count);
    }

    @Test   //passes
    public void getLastTrackOfMuseOf500() {
        AsyncCompletionHandlerBaseCount asyncHandler = new AsyncCompletionHandlerBaseCount();
        JingleService service = new JingleService(new LastfmWebApi(new AsyncHttpRequest(asyncHandler)));
        Observable<Track> tracks = service.searchArtist("muse").blockingFirst().getTracks().get().take(500);
        assertEquals(500, tracks.count().blockingGet().longValue());
        assertEquals(78, asyncHandler.count); // Each page has 50 albums => 50 requests to get their tracks. Some albums have no tracks.
    }

    @Test   //passes
    public void getTopTrack60FromSpain(){
        AsyncCompletionHandlerBaseCount asyncHandler = new AsyncCompletionHandlerBaseCount();
        JingleService service = new JingleService(new LastfmWebApi(new AsyncHttpRequest(asyncHandler)));
        Observable<TrackRank> topTracks = service.getTopTracks("Spain");
        TrackRank track = topTracks.skip(60).blockingFirst();
        assertEquals(2, asyncHandler.count);
        assertEquals("Bitter Sweet Symphony", track.getName());
        assertEquals(61, track.getRank());
    }

    @Test   // missing service
    public void getSongIntroFromDrakeAndRankInPortugal(){
        AsyncCompletionHandlerBaseCount asyncHandler = new AsyncCompletionHandlerBaseCount();
        JingleService service = new JingleService(new LastfmWebApi(new AsyncHttpRequest(asyncHandler)));
        Observable<TrackRank> drakeInPortugal = service.getTracksRank("b49b81cc-d5b7-4bdd-aadb-385df8de69a6", "Portugal");
        TrackRank trackIntro = drakeInPortugal.skip(70).blockingFirst();
        assertEquals(17, asyncHandler.count);
        assertEquals("Intro", trackIntro.getName());
        assertEquals(6, trackIntro.getRank());
    }
}
