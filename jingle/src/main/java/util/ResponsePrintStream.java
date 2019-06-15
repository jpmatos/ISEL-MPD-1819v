package util;

import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class ResponsePrintStream extends PrintStream {
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
