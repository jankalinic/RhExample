import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import redhatExample.VertxServer;

import static org.fest.assertions.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
public class VertxServerTest{

    private WebClient client;
    private VertxServer  vertxServer = new VertxServer();

    @BeforeEach
    public void verticleDeploy(Vertx vertx,VertxTestContext testContext) {

        vertx.deployVerticle(VertxServer.class.getName(), testContext.completing());
        client = WebClient.create(vertx);
    }


    @Test
    public void testHttpMethods(VertxTestContext testContext){

        HttpMethod[] httpMethods = {HttpMethod.GET,HttpMethod.SEARCH,HttpMethod.PATCH,HttpMethod.POST,HttpMethod.REPORT};
        Checkpoint responsesReceived = testContext.checkpoint();

        for(HttpMethod httpMethodName : httpMethods)
        {
            client  .request(httpMethodName, 8088, "localhost", "/")
                    .send(ar -> {
                if (ar.failed() && httpMethodName != HttpMethod.GET) {
                    responsesReceived.flag();
                }
                else if (ar.succeeded() && httpMethodName == HttpMethod.GET){
                    responsesReceived.flag();
                }
                else{
                    testContext.failNow(ar.cause().getCause());
                }
            });
        }
    }
    @Disabled
    @Test
    public void portTest(VertxTestContext testContext){

        int[] ports ={1,136,8088,445,8080,250};
        Checkpoint responsesReceived = testContext.checkpoint(ports.length);
        for(int testedPort: ports)
        {

            client  .get(testedPort,"localhost","/")
                    .send(ar -> {
                        if (ar.failed() && testedPort != 8088) {
                            responsesReceived.flag();
                        }
                        else if (ar.succeeded() && testedPort == 8088){
                            responsesReceived.flag();
                        }
                        else{
                            testContext.failNow(ar.cause());
                        }
                    });
            client.close();
        }

    }
    @Test
    public void statusCodeTest(VertxTestContext testContext){
        Checkpoint checkpoint = testContext.checkpoint(10);
        for (int rep = 0; rep < 10; rep++) {
            client.get(8088, "localhost", "/")
                    .send(ar -> {
                         if (ar.result().statusCode() == 200) {
                            checkpoint.flag();

                        } else {
                            testContext.failNow(ar.cause());
                        }
                    });
        }
    }

    @Test
    public void emptyUUIDTest(){
        assertThat(vertxServer.getUniqueID()).isNotEmpty();
    }
    @Test
    public void emptyHostnameTest(){
        assertThat(vertxServer.getCurrentHostname()).isNotEmpty();
    }
    @AfterEach
    public void tearDown(Vertx vertx,VertxTestContext testContext) {
        client.close();
        vertx.close(testContext.completing());
    }


}
