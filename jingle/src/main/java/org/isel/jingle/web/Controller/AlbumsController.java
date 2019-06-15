package org.isel.jingle.web.Controller;

import io.reactivex.Observable;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.isel.jingle.JingleService;
import org.isel.jingle.model.Album;
import org.isel.jingle.web.View.AlbumsView;

public class AlbumsController implements AutoCloseable{
    final AlbumsView view = new AlbumsView();
    final JingleService service = new JingleService();

    public AlbumsController(Router router) {
        router.route("/artists/:id/albums").handler(this::albumsHandler);
    }

    public void albumsHandler(RoutingContext ctx) {
        // This handler gets called for each request
        // that arrives on /artists/:id/albums path

        String mbid = ctx.request().getParam("id");
        String limit = ctx.request().getParam("limit");
        if(limit == null) limit = "10";

        Observable<Album> albums = service.getAlbums(mbid).take(Integer.valueOf(limit));

        HttpServerResponse response = ctx.response();
        view.write(response, albums);
    }

    @Override
    public void close() throws Exception {
        service.close();
    }
}
