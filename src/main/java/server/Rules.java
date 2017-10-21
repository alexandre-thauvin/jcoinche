package server;

import com.sun.security.ntlm.Server;

import java.util.List;

public class Rules {
    public Rules() {
    }

    public boolean checkAll() {
        return true;
    }

    public void updatePower(List<Card> lcard, String atout) {
        for (Card card : lcard) {
            switch (card.number) {
                case "7":
                    if (atout.equals(card.suite))
                        card.power = 0;
                    break;
                case "8":
                    if (atout.equals(card.suite))
                        card.power = 0;
                    break;
                case "9":
                    if (atout.equals(card.suite))
                        card.power = 9;
                    else
                        card.power = 0;
                    break;
                case "10":
                    if (atout.equals(card.suite))
                        card.power = 5;
                    else
                        card.power = 10;
                    break;
                case "Jack":
                    if (atout.equals(card.suite))
                        card.power = 14;
                    else
                        card.power = 2;
                    break;
                case "Queen":
                    if (atout.equals(card.suite))
                        card.power = 2;
                    else
                        card.power = 3;
                    break;
                case "King":
                    if (atout.equals(card.suite))
                        card.power = 3;
                    else
                        card.power = 4;
                    break;
                case "Ace":
                    if (atout.equals(card.suite))
                        card.power = 7;
                    else
                        card.power = 19;
                    break;
            }
        }
    }

    public void countScore(List<Card> lcart) {
        int score = 0;
        for (Card card : lcart) {
            score += card.power;
        }
        for (Client clt : ServerHandler.clientManager.lclient) {
            if (clt.winTurn)
                clt.scoreTurn = score;
        }
    }

    public void resetWinTurn() {
        for (Client clt : ServerHandler.clientManager.lclient) {
            clt.winParty = false;
        }
    }
    public void checkWinTurn()
    {
        Print print = new Print();
        for (Client clt : ServerHandler.clientManager.lclient) {
            if (clt.winTurn) {
                print.ServerToAll("Team " + clt.team + "win !\n", ServerHandler.clientManager);
                break;
            }
        }
    }
    public void checkWinParty()
    {
        Print print = new Print();
        for (Client clt : ServerHandler.clientManager.lclient) {
            if (clt.winTurn) {
                for (Client cltt : ServerHandler.clientManager.lclient) {
                    if (cltt.winTurn) {
                        cltt.scoreParty = cltt.scoreTurn;
                        cltt.scoreTurn = 0;
                    }
                }
                print.ServerToAll("Team " + clt.team + "win the turn!\n", ServerHandler.clientManager);
                break;
            }
        }
    }
}
