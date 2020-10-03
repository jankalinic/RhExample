
import io.vertx.core.buffer.Buffer;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.core.Vertx;


import io.vertx.ext.unit.TestContext;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.junit5.VertxExtension;



import redhatExample.VertxServer;

@ExtendWith(VertxExtension.class)
public class VertxServerTest{


    int port = 8088;
    String host = "localhost";
    String uri = "/";

    @BeforeEach
    public void setUp(Vertx vertx,VertxTestContext testContext) {
        System.out.println("Running @BeforeAll setUp now");


        vertx.deployVerticle(VertxServer.class.getName(), testContext.completing());

    }

    //@Ignore
    @Test
    public void testGet(Vertx vertx,VertxTestContext testContext) {
        System.out.println("Running @Test testGet now");


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
                    testContext.completeNow();
                });
    }

    @AfterEach
    public void tearDown(Vertx vertx,VertxTestContext testContext) {
        System.out.println("tearDown Vertx");
        vertx.close(testContext.completing());
    }


}