package server;

import common.Card;

public class GameManager {
    Deck    deck = new Deck();
    GameManager(){}
    String bet_suite;
    int bet_number = 0;
    int pass = 0;
    boolean bet_turn = false;
    boolean play_turn = false;
    boolean timer = true;
    Print   print = new Print();
    public void f_run(ClientManager clientManager)
    {
        clientManager.lclient.get(0).starter = true;
        deck.create();
        deck.distribution(clientManager.lclient);
        print.ServerToAll("BET STARTS\n", clientManager);
        print.ServerToAll("Joueur " + (clientManager.lclient.get(0).id) + " must bet\n", clientManager);
        bet_turn = false;
    }

    public void d_run(ClientManager clientManager)
    {
        print.ServerToAll("Joueur " + (clientManager.getClientByBegin().id) + " begin\n", clientManager);
    }
     public void bet(ClientManager clientManager)
    {
        bet_turn = true;
        if (timer) {
                         if (ServerHandler.bet == 2)
                            print.ServerToAll("Joueur " + (clientManager.lclient.get(1).id) + " must bet\n", clientManager);
                        else if (ServerHandler.bet == 3) {
                             print.ServerToAll("BET FINISHED\n", clientManager);
                             bet_turn = false;
                             play_turn = true;
                         }
                    //print.ServerToAll("3Joueur " + clientManager.lclient.get(2).id + " must bet\n", clientManager);
                        else if (ServerHandler.bet == 4)
                            print.ServerToAll("Joueur " + clientManager.lclient.get(3).id + " must bet\n", clientManager);
                    timer = false;
                }
    }
    public void check_hand(Client clt)
    {
        for (Card card: clt.hand)
        {
            print.ServerToOne(card.number + " " +  card.suite, clt);
        }
    }
    public void check_bet(String number, String suite, ClientManager clientManager)
    {

        if (Integer.parseInt(number) > bet_number)
        {
            bet_number = Integer.parseInt(number);
            bet_suite = suite;
            ServerHandler.bet++;
            timer = true;
        }
        else
            print.ServerToOne("Higher pls\n", clientManager.lclient.get(ServerHandler.bet - 2));
    }
}
