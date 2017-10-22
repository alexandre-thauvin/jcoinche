package server;


public class GameManager {
    Deck    deck = new Deck();
    GameManager(){}
    private String bet_suite;
    private boolean bet_turn = false;
    boolean play_turn = false;
    boolean timer = true;
    private Print   print = new Print();
    public void f_run(ClientManager clientManager)
    {
        clientManager.lclient.get(0).starter = true;
        deck.create();
        deck.distribution(clientManager.lclient);
        print.ServerToAll("BET STARTS\n", clientManager);
        print.ServerToAll("Joueur " + (clientManager.lclient.get(0).id) + " must bet\n", clientManager);
        print.ServerToOne("Usage: bet <mise> <suite>\n", clientManager.lclient.get(0));
        bet_turn = false;
    }

    public void d_run(ClientManager clientManager)
    {
        print.ServerToAll("Player " + (clientManager.getClientByBegin().id) + " begin\n", clientManager);
        
    }
     public void bet(ClientManager clientManager)
    {
        if (timer) {
                         if (ServerHandler.getBet() == 2) {
                             print.ServerToAll("Player " + (clientManager.lclient.get(1).id) + " must bet\n", clientManager);
                             print.ServerToOne("Usage: bet <value> <suite>\n", clientManager.lclient.get(1));

                         }
                        else if (ServerHandler.getBet() == 3) {
                             print.ServerToAll("BET FINISHED\n", clientManager);
                             bet_turn = false;
                             play_turn = true;
                         }
                    //print.ServerToAll("Player " + clientManager.lclient.get(2).id + " must bet\n", clientManager);
                        else if (ServerHandler.getBet() == 4)
                            print.ServerToAll("Player " + clientManager.lclient.get(3).id + " must bet\n", clientManager);
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
    public boolean check_bet(String number, String suite, ClientManager clientManager)
    {
        if (Integer.parseInt(number) > ServerHandler.bet_number && Integer.parseInt(number) <= 160 && Integer.parseInt(number) >= 80)
        {
            ServerHandler.bet_number = Integer.parseInt(number);
            bet_suite = suite;
            for (Client clt: clientManager.lclient)
            {
                if (clt.starter)
                    clt.starter = false;
            }
            clientManager.lclient.get(ServerHandler.getBet() - 1).starter = true;
            ServerHandler.bet++;
            ServerHandler.indexPlayer++;
            ServerHandler.table.atout = suite;
            timer = true;
        }
        else {
            print.ServerToOne("The bet must be between " + ServerHandler.bet_number + " and 160\n", clientManager.lclient.get(ServerHandler.bet - 1));
            return false;
        }
        return true;
    }
}
