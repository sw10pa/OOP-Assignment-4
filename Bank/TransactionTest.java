package Bank;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    public void constructorTest() {
        Transaction trans;

        trans = new Transaction(-1, 0, 1);
        assertEquals("from:-1 to:0 amt:1", trans.toString());

        trans = new Transaction(1, -1, 0);
        assertEquals("from:1 to:-1 amt:0", trans.toString());

        trans = new Transaction(0, 1, -1);
        assertEquals("from:0 to:1 amt:-1", trans.toString());
    }

    @Test
    public void getFromAccountTest() {
        Transaction trans;

        trans = new Transaction(-1, 0, 1);
        assertEquals(-1, trans.getFromAccount());

        trans = new Transaction(1, -1, 0);
        assertEquals(1, trans.getFromAccount());

        trans = new Transaction(0, 1, -1);
        assertEquals(0, trans.getFromAccount());
    }

    @Test
    public void getToAccountTest() {
        Transaction trans;

        trans = new Transaction(-1, 0, 1);
        assertEquals(0, trans.getToAccount());

        trans = new Transaction(1, -1, 0);
        assertEquals(-1, trans.getToAccount());

        trans = new Transaction(0, 1, -1);
        assertEquals(1, trans.getToAccount());
    }

    @Test
    public void getAmountTest() {
        Transaction trans;

        trans = new Transaction(-1, 0, 1);
        assertEquals(1, trans.getAmount());

        trans = new Transaction(1, -1, 0);
        assertEquals(0, trans.getAmount());

        trans = new Transaction(0, 1, -1);
        assertEquals(-1, trans.getAmount());
    }

}
