package server;


public class GameManager {
    Deck    deck = new Deck();
    GameManager(){}
    boolean timer = true;
    private Print   print = new Print();
    public void fRun(ClientManager clientManager)
    {
        clientManager.lclient.get(0).starter = true;
        deck.create();
        deck.distribution(clientManager.lclient);
        print.ServerToAll("BET STARTS\n", clientManager);
        print.ServerToAll("Player " + (clientManager.lclient.get(0).id) + " must bet\n", clientManager);
        print.ServerToOne("Usage: bet <mise> <suite>\n", clientManager.lclient.get(0));
    }

     public void bet(ClientManager clientManager)
    {
        if (timer) {
            if (ServerHandler.getBet() == 2) {
                print.ServerToAll("Player " + (clientManager.lclient.get(1).id) + " must bet\n", clientManager);
                print.ServerToOne("Usage: bet <value> <suite>\n", clientManager.lclient.get(1));

            } else if (ServerHandler.getBet() == 3) {
                print.ServerToAll("Player " + clientManager.lclient.get(2).id + " must bet\n", clientManager);

            } else if (ServerHandler.getBet() == 4) {
                print.ServerToAll("Player " + clientManager.lclient.get(3).id + " must bet\n", clientManager);

            } else if (ServerHandler.bet == 5) {
                print.ServerToAll("BET FINISHED\n", clientManager);
                print.ServerToAll("Player " + (clientManager.lclient.get(0).id) + " begin\n", clientManager);
                ServerHandler.indexPlayer = 0;
            }
            timer = false;
        }
    }
    public void checkHand(Client clt)
    {
        print.ServerToOne("YOUR HAND\n", clt);
        for (Card card: clt.hand)
        {
            print.ServerToOne(card.number + " " +  card.suite + '\n', clt);
        }
    }
    public boolean checkBet(String number, String suite, ClientManager clientManager)
    {
        if (Integer.parseInt(number) > ServerHandler.bet_number && Integer.parseInt(number) <= 160 && Integer.parseInt(number) >= 80)
        {
            ServerHandler.bet_number = Integer.parseInt(number);
            for (Client clt: clientManager.lclient)
            {
                if (clt.starter)
                    clt.starter = false;
            }
            clientManager.lclient.get(ServerHandler.getBet() - 1).starter = true;
            clientManager.lclient.get(ServerHandler.getBet() - 1).contrat = true;
            if (ServerHandler.bet - 1 == 0 || ServerHandler.bet - 1 == 2) {
                clientManager.lclient.get(0).contrat = true;
                clientManager.lclient.get(2).contrat = true;
            }
            else
            {
                clientManager.lclient.get(1).contrat = true;
                clientManager.lclient.get(3).contrat = true;
            }
            ServerHandler.bet++;
            if (ServerHandler.indexPlayer == 3)
                ServerHandler.indexPlayer = 0;
            else
                ServerHandler.indexPlayer++;
            ServerHandler.table.atout = suite;
            ServerHandler.table.contrat = Integer.parseInt(number);
            timer = true;
        }
        else {
            print.ServerToOne("The bet must be between " + ServerHandler.bet_number + " and 160\n", clientManager.lclient.get(ServerHandler.bet - 1));
            return false;
        }
        return true;
    }
}
