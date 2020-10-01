import redhatExample.VertxServer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.ext.web.client.*;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.UnknownHostException;




@ExtendWith(VertxExtension.class)
public class VertxServerTest extends AbstractVerticle{

    @BeforeAll
    static void setUp() throws UnknownHostException{
        VertxServer s = new VertxServer();
        s.start();
    }

    @Test
    public void testGet(){
        Vertx vertx = Vertx.vertx();
        WebClient client = WebClient.create(vertx);

        System.out.println("cus");
        System.out.println(true);

        client.get(8088,"localhost","/").send(hand->{
            if (hand.succeeded()){
                System.out.println("jo");
            }
            else{
                System.out.println("ne");
            }

        });


    }

   /* @Test
    public void failTest(){
        System.out.println("FailTest - Ok");
    }
    */
}