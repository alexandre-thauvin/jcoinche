package server;

import common.Card;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class Client {
    Client (int id, boolean inGame, ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.inGame = inGame;
        this.id = id;
    }
    ChannelHandlerContext ctx;
    int id;
    List<Card> hand = new ArrayList<Card>();
    boolean inGame;
}
