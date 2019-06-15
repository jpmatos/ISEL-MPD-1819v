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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

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
                /* TO DO !!! */
            }

            @Override
            public void onComplete() {
                tbody
                        .__() // tbody
                        .__() // table
                        .p()
                        .small()
                        .text("Default limit to 10. Use query 'limit=' for more results.")
                        .__()
                        .__() // body
                        .__();// html
                resp.end();
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
                .__()
                .__()
                .tbody();
    }

    private static void writeTableRow(Tbody<Table<Body<Html<HtmlView>>>> tbody, Album album) {
        tbody
                .tr()
                .td()
                .text(album.getName())
                .__()
                .td()
                .a()
                .attrHref(getAlbumTracksLink(album.getMbid()))
                .text("Tracks")
                .__()
                .__(); // td
//                .td()
//                .text(artist.getTracks().get())
//                .__(); // td
    }

    private static String getAlbumTracksLink(String mbid) {
        return "/albums/" + mbid + "/tracks";
    }

    private static class ResponsePrintStream extends PrintStream {
        /**
         * We may improve this with a Buffer.
         * For now we just want to see the effect of sending
         * char by char to the browser !!!
         */
        public ResponsePrintStream(HttpServerResponse resp) {
            super(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    char c = (char) b;
                    resp.write(String.valueOf(c));
                }
            });
        }
    }
}
