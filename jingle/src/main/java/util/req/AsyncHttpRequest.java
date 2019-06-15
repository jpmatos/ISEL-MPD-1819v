package util.req;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

import java.util.concurrent.CompletableFuture;

public class AsyncHttpRequest implements AsyncRequest {
    final AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();
    private final AsyncCompletionHandlerBaseCount handlerBase;

    public AsyncHttpRequest(AsyncCompletionHandlerBaseCount handlerBase) {
        this.handlerBase = handlerBase;
    }

    @Override
    public CompletableFuture<String> getLines(String path) {
        return asyncHttpClient
                .prepareGet(path)
                .execute(handlerBase)
                .toCompletableFuture()
                .thenApply(Response::getResponseBody);
    }

    @Override
    public void close() throws Exception {
        asyncHttpClient.close();
    }
}
