package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class Client {
    Client (int id, boolean inGame, Channel ctx) {
        this.ctx = ctx;
        this.inGame = inGame;
        this.id = id;
        this.starter = false;
        this.score = 0;
    }
    Channel ctx;
    int id;
    List<Card> hand = new ArrayList<Card>();
    boolean inGame;
    boolean starter;
    int     score;
}
