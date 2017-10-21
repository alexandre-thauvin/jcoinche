package server;

import org.junit.Test;

import static org.junit.Assert.*;

public class TableTest {
    @Test
    public void add() throws Exception {
        Card card = new Card(0, 0);
        Table table = new Table();

        table.add_card(card);
        assertEquals(card, table.table.get(0));
    }

    @Test
    public void clear() throws Exception {
        Card card = new Card(0, 0);
        Table table = new Table();

        table.add_card(card);
        table.clean_table();
        assertEquals(0, table.table.size());
    }
}