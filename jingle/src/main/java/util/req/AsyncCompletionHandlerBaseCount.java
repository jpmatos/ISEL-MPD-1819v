package util.req;


import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;

public class AsyncCompletionHandlerBaseCount extends AsyncCompletionHandler<Response> {
    public int count;
    @Override
    public Response onCompleted(Response response) throws Exception {
        System.out.println("Requests: " + ++count);
        return response;
    }
}