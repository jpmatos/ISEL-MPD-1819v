package org.isel.jingle.web.Controller;

import io.reactivex.Observable;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.isel.jingle.JingleService;
import org.isel.jingle.model.Artist;
import org.isel.jingle.web.View.ArtistsView;

public class ArtistsController implements AutoCloseable{
    final ArtistsView view = new ArtistsView();
    final JingleService service = new JingleService();

    public ArtistsController(Router router) {
        router.route("/artists").handler(this::artistsHandler);
    }

    public void artistsHandler(RoutingContext ctx) {
        // This handler gets called for each request
        // that arrives on /artists path

        String name = ctx.request().getParam("name");
        String limit = ctx.request().getParam("limit");
        if(limit == null) limit = "10";

        Observable<Artist> artists = service.searchArtist(name).take(Integer.valueOf(limit));

        HttpServerResponse response = ctx.response();
        view.write(response, artists);
    }

    @Override
    public void close() throws Exception {
        // weather.close();
    }
}
