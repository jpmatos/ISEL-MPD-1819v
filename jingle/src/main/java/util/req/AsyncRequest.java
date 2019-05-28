package util.req;

import java.util.concurrent.CompletableFuture;

public interface AsyncRequest {
    public CompletableFuture<String> getLines(String path);
}
