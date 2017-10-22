package server;

import java.util.ArrayList;
import java.util.List;

public class Table {
    List<Card>  table = new ArrayList<Card>();
    String      atout;
    int         contrat;
    public void checkTable(Client client)
    {
        Print print = new Print();
        for (Card card: table)
            print.ServerToOne("Table: " + card.number + " " + card.suite + "\n", client);
    }
    public void add_card(Card card) {this.table.add(card);}
    public void clean_table(){this.table.clear();}
}
