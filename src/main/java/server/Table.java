package server;

import java.util.ArrayList;
import java.util.List;

public class Table {
    List<Card>  table = new ArrayList<Card>();
    public void check_table(List<Card> Table, Client client)
    {
        Print print = new Print();
        for (Card card: Table)
        {
            print.ServerToOne(card.number + " " + card.suite, client);
        }
    }
    public void add(Card card) {this.table.add(card);}
    public void clean(){this.table.clear();}
}
