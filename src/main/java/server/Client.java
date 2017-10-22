package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class Client {
    Channel ctx;
    int id;
    List<Card> hand = new ArrayList<Card>();
    boolean inGame;
    boolean starter;
    int     scoreTurn = 0;
    int     scoreParty = 0;
    int     team;
    boolean winFolds = false;
    boolean contrat = false;

    Client (int id, boolean inGame, Channel ctx) {
        this.ctx = ctx;
        this.inGame = inGame;
        this.id = id;
        if (this.id == 1 || this.id == 3)
            team = 1;
        else
            team = 2;
        this.starter = false;
        this.scoreTurn = 0;
    }

}
