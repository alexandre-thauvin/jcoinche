package server;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class ClientManagerTest {
    @Test
    public void getClientById() throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(new StringDecoder(StandardCharsets.UTF_8));
        Client clt = new Client(0, true, channel);
        ClientManager clientManager = new ClientManager();
        clientManager.add(clt);

        assertEquals(clt, clientManager.getClientById(0));

    }

    @Test
    public void getClientByChannel() throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(new StringDecoder(StandardCharsets.UTF_8));
        Client clt = new Client(0, true, channel);
        ClientManager clientManager = new ClientManager();
        clientManager.add(clt);

        assertEquals(clt, clientManager.getClientByChannel(channel));
    }

    @Test
    public void getClientByBegin() throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(new StringDecoder(StandardCharsets.UTF_8));
        Client clt = new Client(0, true, channel);
        ClientManager clientManager = new ClientManager();
        clt.starter = true;
        clientManager.add(clt);

        assertEquals(clt, clientManager.getClientByBegin());
    }

    @Test
    public void add() throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(new StringDecoder(StandardCharsets.UTF_8));
        Client clt = new Client(0, true, channel);
        ClientManager clientManager = new ClientManager();
        clientManager.add(clt);

        assertEquals(1, clientManager.lclient.size());
    }

    @Test
    public void remove() throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(new StringDecoder(StandardCharsets.UTF_8));
        Client clt = new Client(0, true, channel);
        ClientManager clientManager = new ClientManager();
        clientManager.add(clt);
        clientManager.remove(clt);

        assertEquals(0, clientManager.lclient.size());
    }

}