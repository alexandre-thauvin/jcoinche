package server;


import common.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    Deck(){};
    List<Card>  deck = new ArrayList<Card>();
    public void         create()
    {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++)
            {
                deck.add(new Card(i, j));
            }
        }
    }
    public void         distribution(List<Client> lclient)
    {
        int rand;
        int x = 0;
        Random mix = new Random();
        for (Client clt: lclient) {
            if (clt.inGame) {
                for (int i = 0; i < 8 ; i++) {
                    rand = mix.nextInt(32 - x);
                    clt.hand.add(deck.get(rand));
                    x++;

                    clt.ctx.writeAndFlush("[DISTRIB]Player " + clt.id + ": " + deck.get(rand).number + " " + deck.get(rand).suite + '\n');
                    deck.remove(rand);
                }
            }

        }
    }
}
