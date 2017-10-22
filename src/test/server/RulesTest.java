package server;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RulesTest {
    /*
    @Test
    public void checkAll() throws Exception {
    }*/

    @Test
    public void updatePower() throws Exception {
        List<Card> lcard =  new ArrayList<Card>();
        Card card = new Card(0, 0);
        lcard.add(card);
        Rules r = new Rules();

        r.updatePower(lcard, "Spade");
        assertEquals(0, lcard.get(0).power);
    }
}