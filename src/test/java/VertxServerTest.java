import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.unit.junit.Repeat;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import redhatExample.VertxServer;

import static org.fest.assertions.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
public class VertxServerTest{
   /* @Override
    void handleException(Throwable t) {
        super.handleException(t);
        Handler<Throwable> handler;
        synchronized (this) {
            handler = exceptionHandler;
            if (handler == null || endFuture.isComplete()) {
                log.error(t);
                return;
            }
        }
        // Might be called from non vertx thread
        context.emit(t, handler);
        endPromise.tryFail(t);
    }{*/


    private static Vertx vertx = Vertx.vertx();
    private static WebClient client= WebClient.create(vertx);
    private VertxServer vertxServer = new VertxServer();

    @BeforeEach
    public void verticleDeploy(Vertx vertx,VertxTestContext testContext) {
        assertThat(vertx.deploymentIDs()).isEmpty();
        vertx.deployVerticle(VertxServer.class.getName(), new DeploymentOptions().setWorker(true), testContext.completing());
    }

    @AfterEach
    public void tearDown(Vertx vertx,VertxTestContext testContext) {
        assertThat(vertx.deploymentIDs()).isNotEmpty();
        vertx.close(testContext.succeeding(response -> testContext.completeNow()));
    }

    @AfterAll
    public static void endTest(){
        client.close();
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

                                testContext.failNow(ar.cause());
                            }
                        });
            }
    }

    @ParameterizedTest
    @ValueSource(ints ={1,136,8088,445,8080,250})
    public void portTest(int testedPort,VertxTestContext testContext){
        client  .get(testedPort,"localhost","/")
                .send(ar -> {
                    if (ar.failed() && testedPort != 8088) {

                        testContext.completeNow();
                    }
                    else if (ar.succeeded() && testedPort == 8088){
                        testContext.completeNow();
                    }
                    else{
                        testContext.failNow(ar.cause());
                    }
                });

    }

    @Test
    @Repeat(5)
    public void statusCodeTest(VertxTestContext testContext){

        client.get(8088, "localhost", "/")
                .send(ar -> {
                    if (ar.result().statusCode() == 200) {
                        testContext.completeNow();

                    } else {
                        testContext.failNow(ar.cause());
                    }
                });

    }
    @Test
    public void emptyUUIDTest(){
        assertThat(vertxServer.getUniqueID()).isNotEmpty();
    }
    @Test
    public void emptyHostnameTest(){
        assertThat(vertxServer.getCurrentHostname()).isNotEmpty();
    }

}
