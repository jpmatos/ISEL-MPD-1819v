package org.isel.jingle;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.isel.jingle.web.Controller.AlbumsController;
import org.isel.jingle.web.Controller.ArtistsController;
import org.isel.jingle.web.Controller.TracksController;

public class WebApp {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        new ArtistsController(router);    // Add Routes = an handler for each path (e.g. /time)
        new AlbumsController(router);
        new TracksController(router);
        server.requestHandler(router).listen(3000);
    }
}
