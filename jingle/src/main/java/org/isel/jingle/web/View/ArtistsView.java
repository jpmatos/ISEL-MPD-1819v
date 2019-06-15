package org.isel.jingle.web.View;

import htmlflow.HtmlView;
import htmlflow.StaticHtml;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.vertx.core.http.HttpServerResponse;
import org.isel.jingle.model.Artist;
import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Html;
import org.xmlet.htmlapifaster.Table;
import org.xmlet.htmlapifaster.Tbody;
import util.ResponsePrintStream;

public class ArtistsView implements View<Observable<Artist>>{

    @Override
    public void write(HttpServerResponse resp, Observable<Artist> model) {
        resp.setChunked(true); // response.putHeader("Transfer-Encoding", "chunked");
        resp.putHeader("content-type", "text/html");

        model.subscribeWith(new Observer<Artist>() {
            Tbody<Table<Body<Html<HtmlView>>>> tbody;
            @Override
            public void onSubscribe(Disposable d) {
                tbody = writeHeader(resp);
            }

            @Override
            public void onNext(Artist artist) {
                writeTableRow(tbody, artist);
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
                .text("Artists")
                .__()// title
                .__()// head
                .body()
                .h1()
                .text("Artists")
                .__()// h1
                .table()
                .thead()
                .tr()
                .th().text("Name").__()
                .th().text("Albums").__()
                .th().text("Tracks").__()
                .__()
                .__()
                .tbody();
    }

    private static void writeTableRow(Tbody<Table<Body<Html<HtmlView>>>> tbody, Artist artist) {
        tbody
                .tr()
                .td()
                .text(artist.getName())
                .__() // td
                .td()
                .a()
                .attrHref(getArtistAlbumsLink(artist.getMbid()))
                .text("Albums")
                .__() // a
                .__() // td
                .td()
                .a()
                .attrHref(getArtistTracksLink(artist.getMbid()))
                .text("Tracks")
                .__() // a
                .__();// td
    }

    private static String getArtistTracksLink(String mbid) {
        return "/artists/" + mbid + "/tracks";
    }

    private static String getArtistAlbumsLink(String mbid) {
        return "/artists/" + mbid + "/albums";
    }
}
