package org.isel.jingle.web.View;

import htmlflow.HtmlView;
import io.vertx.core.http.HttpServerResponse;
import org.xmlet.htmlapifaster.Body;
import org.xmlet.htmlapifaster.Html;
import org.xmlet.htmlapifaster.Table;
import org.xmlet.htmlapifaster.Tbody;

public interface View<T> {

    void write(HttpServerResponse resp, T model);

    void write(HttpServerResponse resp);

    default void closeAll(Tbody<Table<Body<Html<HtmlView>>>> tbody, HttpServerResponse resp) {
        tbody
                .__() // tbody
                .__() // table
                .p()
                .small()
                .text("Default limit to 10. Use query 'limit=' for more results.")
                .__() // p
                .__() // body
                .__();// html
        resp.end();
    }
}
