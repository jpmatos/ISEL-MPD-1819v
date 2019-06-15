package util.req;

import java.util.concurrent.CompletableFuture;

public interface AsyncRequest extends AutoCloseable {
    public CompletableFuture<String> getLines(String path);
}
