package redhatExample;

import io.vertx.core.*;
import io.vertx.core.http.*;
import io.vertx.core.AbstractVerticle;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;


public class VertxServer extends AbstractVerticle{

    private final Vertx vertx = Vertx.vertx();


    @Override
    public void start(Promise<Void> promise) throws UnknownHostException {

        String uniqueID = UUID.randomUUID().toString();
        String currentHostname =  InetAddress.getLocalHost().getHostName();

        HttpServer server = vertx.createHttpServer(new HttpServerOptions().setHost("localhost").setPort(8088));
        server.requestHandler(req -> {
            if (req.method() == HttpMethod.GET) {
                req.response().end("hello from server ID: <" + uniqueID + "> with HOSTNAME: <" + currentHostname + ">");
            }
        }).listen(8088,"localhost",futureResolver->{
            promise.complete();
        });
    }

}
