package server;


import java.util.ArrayList;
import java.util.List;

public class Deck {
    Deck(){};
    List<Card>  deck = new ArrayList<Card>();
    public void         create()
    {
        for (int suite = 0; suite < 4; suite++) {
            for (int number = 0; number < 8; number++)
                deck.add(new Card(suite, number));
        }
    }
    public void         distribution(List<Client> lclient)
    {
        int     rand;
        for (Client clt: lclient) {
            if (clt.inGame) {
                for (int i = 0; i < 8 ; i++) {
                    rand = (int)(Math.random() * deck.size());
                    clt.hand.add(deck.get(rand));
                    clt.ctx.writeAndFlush("[DISTRIBUTION]Player " + clt.id + ": " + deck.get(rand).number + " " + deck.get(rand).suite + '\n');
                    deck.remove(rand);
                }
            }

        }
    }
}
