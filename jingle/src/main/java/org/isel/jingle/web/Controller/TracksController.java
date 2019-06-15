package org.isel.jingle.web.Controller;

import io.reactivex.Observable;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.isel.jingle.JingleService;
import org.isel.jingle.model.Track;
import org.isel.jingle.web.View.TracksView;

public class TracksController implements AutoCloseable{
    final TracksView view = new TracksView();
    final JingleService service = new JingleService();

    public TracksController(Router router) {
        router.route("/albums/:id/tracks").handler(this::albumTracksHandler);
        router.route("/artists/:id/tracks").handler(this::artistTracksHandler);
    }

    private void albumTracksHandler(RoutingContext ctx) {
        // This handler gets called for each request
        // that arrives on /album/:id/tracks path

        String mbid = ctx.request().getParam("id");

        Observable<Track> albumTracks = service.getAlbumTracks(mbid);

        HttpServerResponse response = ctx.response();
        view.write(response, albumTracks);
    }

    private void artistTracksHandler(RoutingContext ctx) {
        // This handler gets called for each request
        // that arrives on /artists/:id/tracks path

        String mbid = ctx.request().getParam("id");
        String limit = ctx.request().getParam("limit");
        if(limit == null) limit = "10";
        if(limit.equals("-1")) limit = String.valueOf(Integer.MAX_VALUE);

        Observable<Track> artistTracks = service.getTracks(mbid).take(Integer.valueOf(limit));

        HttpServerResponse response = ctx.response();
        view.write(response, artistTracks);
    }

    @Override
    public void close() throws Exception {
        // weather.close();
    }
}
