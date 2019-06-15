package org.isel.jingle.web.View;

import htmlflow.HtmlView;
import htmlflow.StaticHtml;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.vertx.core.http.HttpServerResponse;
import org.isel.jingle.model.Album;
import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Html;
import org.xmlet.htmlapifaster.Table;
import org.xmlet.htmlapifaster.Tbody;
import util.ResponsePrintStream;

public class AlbumsView implements View<Observable<Album>> {

    @Override
    public void write(HttpServerResponse resp, Observable<Album> model) {
        resp.setChunked(true); // response.putHeader("Transfer-Encoding", "chunked");
        resp.putHeader("content-type", "text/html");

        model.subscribeWith(new Observer<Album>() {
            Tbody<Table<Body<Html<HtmlView>>>> tbody;

            @Override
            public void onSubscribe(Disposable d) {
                tbody = writeHeader(resp);
            }

            @Override
            public void onNext(Album album) {
                writeTableRow(tbody, album);
            }

            @Override
            public void onError(Throwable e) {
                closeAll(tbody, resp);
            }

            @Override
            public void onComplete() {
                closeAll(tbody, resp);
            }
        });
    }

    @Override
    public void write(HttpServerResponse resp) {
        throw new UnsupportedOperationException("This view requires a Model. You should invoke write(resp, model) instead!");
    }

    private static Tbody<Table<Body<Html<HtmlView>>>> writeHeader(HttpServerResponse resp) {
        return StaticHtml.view(new ResponsePrintStream(resp))
                .html()
                .head()
                .title()
                .text("Albums")
                .__()// title
                .__()// head
                .body()
                .h1()
                .text("Albums")
                .__()// h1
                .table()
                .thead()
                .tr()
                .th().text("Name").__()
                .th().text("Tracks").__()
                .__() // tr
                .__() // thead
                .tbody();
    }

    private static void writeTableRow(Tbody<Table<Body<Html<HtmlView>>>> tbody, Album album) {
        tbody
                .tr()
                .td()
                .text(album.getName())
                .__() // td
                .td()
                .a()
                .attrHref(getAlbumTracksLink(album.getMbid()))
                .text("Tracks")
                .__() // a
                .__();// td
    }

    private static String getAlbumTracksLink(String mbid) {
        return "/albums/" + mbid + "/tracks";
    }
}
