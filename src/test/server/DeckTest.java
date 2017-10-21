package server;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DeckTest {
    @Test
    public void create() throws Exception {
        Deck deck = new Deck();

        deck.create();
        assertEquals(32, deck.deck.size());
    }

    @Test
    public void distribution() throws Exception {
        Deck deck = new Deck();
        EmbeddedChannel channel = new EmbeddedChannel(new StringDecoder(StandardCharsets.UTF_8));
        List<Client> lclient = new ArrayList<Client>();
        lclient.add(new Client(0, true, channel));

        deck.create();
        deck.distribution(lclient);
        assertEquals(8, lclient.get(0).hand.size());
    }

}