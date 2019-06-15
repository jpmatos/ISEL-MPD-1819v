package org.isel.jingle.web.View;

import io.vertx.core.http.HttpServerResponse;

public interface View<T> {

    void write(HttpServerResponse resp, T model);

    void write(HttpServerResponse resp);
}
