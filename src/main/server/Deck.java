package server;


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
        Random mix = new Random();
        for (Client clt: lclient) {
            if (clt.game) {
                rand = mix.nextInt(32);
                clt.hand.add(deck.get(rand));
                deck.remove(rand);
            }

        }
    }
}
