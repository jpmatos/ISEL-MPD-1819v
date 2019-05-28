package util.req;

import java.util.concurrent.CompletableFuture;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

public class AsyncHttpRequest implements AsyncRequest {
    final AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();

    @Override
    public CompletableFuture<String> getLines(String url) {
        return asyncHttpClient
                .prepareGet(url)
                .execute()
                .toCompletableFuture()
                .thenApply(Response::getResponseBody);
    }
}
