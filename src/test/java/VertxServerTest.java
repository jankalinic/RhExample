
import io.vertx.core.buffer.Buffer;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.core.Vertx;


import io.vertx.ext.unit.TestContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.junit5.VertxExtension;
import org.junit.runner.RunWith;


import redhatExample.VertxServer;

@RunWith(VertxUnitRunner.class)
public class VertxServerTest{


    private static Vertx vertx = Vertx.vertx();

    int port = 8088;
    String host = "localhost";
    String uri = "/";

    @BeforeAll
    public static void setUp(TestContext context) {
        System.out.println("Running @BeforeAll setUp now");


        vertx.deployVerticle(VertxServer.class.getName(), context.asyncAssertSuccess());

    }

    //@Ignore
    @Test
    public void testGet(TestContext context) {
        System.out.println("Running @Test testGet now");

        Async async = context.async();
        WebClient client = WebClient.create(vertx);

        client  .get(port, host, uri)
                .send(ar -> {

                if (ar.succeeded()) {

                    HttpResponse<Buffer> response = ar.result();

                    System.out.println("Odezva status Code: " + response.statusCode());
                    System.out.println("----------SERVER RESPOND BODY--------");
                    System.out.println(response.bodyAsString());
                    System.out.println("-------------------------------------");
                }
                else {
                    System.out.println("Chyba: " + ar.cause().getMessage());
                }
                async.complete();
            });
    }

    @AfterAll
    public void tearDown(TestContext context) {
        System.out.println("tearDown Vertx");
        vertx.close(context.asyncAssertSuccess());
    }


}