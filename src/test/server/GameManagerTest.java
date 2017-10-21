package server;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class GameManagerTest {
    @Test
    public void bet() throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(new StringDecoder(StandardCharsets.UTF_8));
        GameManager gameManager = new GameManager();
        Client clt = new Client(0, true, channel);
        ClientManager clientManager = new ClientManager();
        clientManager.add(clt);
        gameManager.timer = true;
        gameManager.bet(clientManager);

        assertEquals(false, gameManager.timer);
    }

}