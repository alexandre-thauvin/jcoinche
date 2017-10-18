package server;

public class GameManager {
    Deck    deck = new Deck();
    GameManager(){}

    public void run(ClientManager clientManager)
    {
        deck.create();
        deck.distribution(clientManager.lclient);
    }
}
