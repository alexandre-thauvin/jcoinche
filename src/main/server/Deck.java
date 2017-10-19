package server;


import common.Card;

import java.util.ArrayList;
import java.util.List;

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
        int     n_rand;
        for (Client clt: lclient) {
            if (clt.inGame) {
                for (int i = 0; i < 8 ; i++) {
                    n_rand = (int)(Math.random() * deck.size());
                    clt.hand.add(deck.get(n_rand));
                    clt.ctx.writeAndFlush("[DISTRIBUTION]Player " + clt.id + ": " + deck.get(n_rand).number + " " + deck.get(n_rand).suite + '\n');
                    deck.remove(n_rand);
                }
            }

        }
    }
}
