package server;


import java.util.List;

public class Rules {
    public Rules() {
    }
    private Print print = new Print();

    public boolean checkPut(String msg)
    {
        if (checkValidCard(msg) != null) {
            ServerHandler.table.add_card(checkValidCard(msg));
            return true;
        }
        else {
            print.ServerToOne(msg, ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer));
            return false;
        }
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

    public void countScore(List<Card> lcard) {
        int score = 0;
        for (Card card : lcard) {
            score += card.power;
        }
        for (Client clt : ServerHandler.clientManager.lclient) {
            if (clt.winTurn)
                clt.scoreTurn = score;
        }
    }

    public void resetWinTurn() {
        for (Client clt : ServerHandler.clientManager.lclient) {
            clt.winTurn = false;
        }
        ServerHandler.table.clean_table();
    }
    public boolean checkWinTurn()
    {
        for (Client clt : ServerHandler.clientManager.lclient) {
            if (clt.winTurn) {
                print.ServerToAll("Team " + clt.team + "win !\n", ServerHandler.clientManager);
                resetWinTurn();
                return true;
            }
        }
        return false;
    }
    public boolean checkWinParty()
    {
        for (Client clt : ServerHandler.clientManager.lclient) {
            if (clt.winTurn) {
                for (Client cltt : ServerHandler.clientManager.lclient) {
                    if (cltt.winTurn) {
                        cltt.scoreParty += cltt.scoreTurn;
                        cltt.scoreTurn = 0;
                    }
                    if (cltt.scoreParty >= 701) {
                        print.ServerToAll("Team " + clt.team + "win the PARTY ! Congrats!\n", ServerHandler.clientManager);
                        return true;
                    }
                }
                print.ServerToAll("Team " + clt.team + "win the turn!\n", ServerHandler.clientManager);
                break;
            }
        }
        return false;
    }

    public boolean checkFolds()
    {
        if (ServerHandler.table.table.size() == 32) {
            updatePower(ServerHandler.table.table, ServerHandler.table.atout);
            countScore(ServerHandler.table.table);
            return true;
        }
        else
            return false;
    }

    public Card checkValidCard(String msg)
    {
        if (countWords(msg) == 3)
            for (Card card: ServerHandler.clientManager.lclient.get(ServerHandler.indexPlayer).hand)
            {
                if (card.number == msg.split("\\s+")[1] && card.suite == msg.split("\\s+")[2])
                    return card;
                else
                    print.ServerToAll("You don't have the card\n", ServerHandler.clientManager);
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
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }
}
