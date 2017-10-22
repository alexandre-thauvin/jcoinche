package server;


import java.util.List;

public class Rules {
    public Rules() {
    }
    private Print print = new Print();

    public boolean checkPut(String msg)
    {
        Card card = checkValidCard(msg);
        if (card != null) {
            if (checkPutSameSuite()) {
                if (!card.suite.equals(ServerHandler.table.table.get(0).suite)) {
                    print.ServerToOne("You must put the same suite card\n", ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer));
                    return false;
                } else {
                    card.who = ServerHandler.indexPlayer;
                    ServerHandler.table.add_card(card);
                    ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer).hand.remove(card);
                    ServerHandler.turnNumber++;
                    if (ServerHandler.indexPlayer == 3)
                        ServerHandler.indexPlayer = 0;
                    else
                        ServerHandler.indexPlayer++;
                    print.ServerToAll("Player " + (ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer).id) + " must play\n", ServerHandler.clientManager);
                    print.ServerToOne("Usage: put <value> <suite>\n", ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer));

                    return true;
                }
            }
            else {
                if (checkPutAtout()) {
                    if (card.suite.equals(ServerHandler.table.atout)) {
                        card.who = ServerHandler.indexPlayer;
                        ServerHandler.table.add_card(card);
                        ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer).hand.remove(card);
                        ServerHandler.turnNumber++;
                        ServerHandler.indexPlayer++;
                        if (ServerHandler.indexPlayer - 1 == 3)
                            ServerHandler.indexPlayer = 0;
                        print.ServerToAll("Player " + (ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer).id) + " must play\n", ServerHandler.clientManager);
                        print.ServerToOne("Usage: put <value> <suite>\n", ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer));
                        return true;
                    } else {
                        print.ServerToOne("You must put your atout card\n", ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer));
                        return false;
                    }
                }
            }
            card.who = ServerHandler.indexPlayer;
            ServerHandler.table.add_card(card);
            ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer).hand.remove(card);
            ServerHandler.turnNumber++;
            if (ServerHandler.indexPlayer == 3)
                ServerHandler.indexPlayer = 0;
            else
                ServerHandler.indexPlayer++;
            print.ServerToAll("Player " + (ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer).id) + " must play\n", ServerHandler.clientManager);
            print.ServerToOne("Usage: put <value> <suite>\n", ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer));
            return true;
        }
        return false;
    }

    public void updatePower(List<Card> lcard, String atout) {
        for (Card card : lcard) {
            switch (card.number) {
                case "7":
                        card.power = 0;
                    break;
                case "8":
                        card.power = 0;
                    break;
                case "9":
                    if (atout.equals(card.suite))
                        card.power = 14;
                    else
                        card.power = 0;
                    break;
                case "10":
                        card.power = 10;
                    break;
                case "jack":
                    if (atout.equals(card.suite))
                        card.power = 20;
                    else
                        card.power = 2;
                    break;
                case "queen":
                        card.power = 3;
                    break;
                case "king":
                        card.power = 4;
                    break;
                case "ace":
                        card.power = 11;
                    break;
            }
        }
    }

    public void countScoreFolds(List<Card> lcard) {
        int score = 0;
        for (Card card : lcard) {
            score += card.power;
        }
        for (Client clt : ServerHandler.clientManager.lclient) {
            if (clt.winFolds)
                clt.scoreTurn = score;
        }
        ServerHandler.clientManager.resetWinFolds();
        print.ServerToAll("Player " + ServerHandler.clientManager.lclient.get(0).id +  ": " + ServerHandler.clientManager.lclient.get(0).scoreTurn + " turn points", ServerHandler.clientManager);
        print.ServerToAll("Player " + ServerHandler.clientManager.lclient.get(0).id +  ": " + ServerHandler.clientManager.lclient.get(1).scoreTurn + " turn points", ServerHandler.clientManager);
        print.ServerToAll("Player " + ServerHandler.clientManager.lclient.get(0).id +  ": " + ServerHandler.clientManager.lclient.get(2).scoreTurn + " turn points", ServerHandler.clientManager);
        print.ServerToAll("Player " + ServerHandler.clientManager.lclient.get(0).id +  ": " + ServerHandler.clientManager.lclient.get(3).scoreTurn + " turn points", ServerHandler.clientManager);
    }

    public void countScoreParty()
    {
        if (checkContrat()) {
            for (Client clt : ServerHandler.clientManager.lclient) {
                if (clt.contrat)
                    clt.scoreParty += ServerHandler.table.contrat + clt.scoreTurn;
                else
                    clt.scoreParty += clt.scoreTurn;
            }
        }
        else {
            for (Client clt : ServerHandler.clientManager.lclient) {
                if (clt.contrat)
                    clt.scoreParty = 0;
                else
                    clt.scoreParty += 162 + ServerHandler.table.contrat;
            }
        }
        for (Client clt: ServerHandler.clientManager.lclient)
        {
            clt.scoreTurn = 0;
        }
        print.ServerToAll("Player " + ServerHandler.clientManager.lclient.get(0).id +  ": " + ServerHandler.clientManager.lclient.get(0).scoreParty + " party points", ServerHandler.clientManager);
        print.ServerToAll("Player " + ServerHandler.clientManager.lclient.get(0).id +  ": " + ServerHandler.clientManager.lclient.get(1).scoreParty + " party points", ServerHandler.clientManager);
        print.ServerToAll("Player " + ServerHandler.clientManager.lclient.get(0).id +  ": " + ServerHandler.clientManager.lclient.get(2).scoreParty + " party points", ServerHandler.clientManager);
        print.ServerToAll("Player " + ServerHandler.clientManager.lclient.get(0).id +  ": " + ServerHandler.clientManager.lclient.get(3).scoreParty + " party points", ServerHandler.clientManager);
    }

    public boolean checkWinParty()
    {
        for (Client cltt : ServerHandler.clientManager.lclient) {
            if (cltt.scoreParty >= 701) {
                print.ServerToAll("Team " + cltt.team + "win the PARTY ! Congrats!\n", ServerHandler.clientManager);
                return true;
            }
        }
        return false;
    }

    public boolean checkFolds()
    {
        int i;

        if (ServerHandler.table.table.size() == 4) {
            updatePower(ServerHandler.table.table, ServerHandler.table.atout);
            i = whoWinFolds();
            for (Client clt: ServerHandler.clientManager.lclient)
            {
                if (clt.team == ServerHandler.clientManager.lclient.get(i).team)
                    clt.winFolds = true;
            }
            countScoreFolds(ServerHandler.table.table);
            ServerHandler.clientManager.resetStarter();
            ServerHandler.clientManager.lclient.get(i).starter = true;
            ServerHandler.turnNumber = 0;
            return true;
        }
        else
            return false;
    }

    public Card checkValidCard(String msg)
    {
        if (countWords(msg) == 3) {
            for (Card card : ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer).hand) {
                if (card.number.equals(msg.split("\\s+")[1]) && card.suite.equals(msg.split("\\s+")[2]))
                    return card;
                else
                    print.ServerToAll("You don't have the card", ServerHandler.clientManager);
            }
        }
            else
                print.ServerToAll("Bad format of card\n", ServerHandler.clientManager);
        return null;
    }

    public static int countWords(String s){

        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetterOrDigit(s.charAt(i)) && i != endOfLine) {
                word = true;
            } else if (!Character.isLetterOrDigit(s.charAt(i)) && word) {
                wordCount++;
                word = false;
            } else if (Character.isLetterOrDigit(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }

    public boolean checkPutAtout()
    {
        for (Card card: ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer).hand)
        {
            if (card.suite.equals(ServerHandler.table.atout))
            {
                    return true;
            }
        }
        return false;
    }
    public boolean checkPutSameSuite()
    {
        for (Card card: ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer).hand)
        {
            if (card.suite.equals(ServerHandler.table.table.get(0).suite))
            {
                return true;
            }
        }
        return false;
    }

    public int  whoWinFolds()
    {
        Card tmp = new Card(0, 0);
        tmp.power = 0;
        for (Card card: ServerHandler.table.table)
        {
            if (card.power > tmp.power) {
                tmp.power = card.power;
                tmp.who = card.who;
            }
        }
        return tmp.who;
    }
    public boolean checkEndTurn()
    {
        if (ServerHandler.clientManager.lclient.get(0).hand.size() == 0) {
            print.ServerToAll("End of Turn.\n", ServerHandler.clientManager);
            return true;
        }
        else
            return false;
    }
    public boolean checkContrat() {
        for (Client clt : ServerHandler.clientManager.lclient) {
            if (clt.contrat) {
                if (clt.scoreTurn >= ServerHandler.table.contrat)
                    return true;
                else
                    return false;
            }

        }
        return true;
    }
}