package Bank;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    public void constructorTest() {
        Account acc;

        acc = new Account(-1, -1);
        assertEquals("acct:-1 bal:-1 trans:0", acc.toString());

        acc = new Account(0, 0);
        assertEquals("acct:0 bal:0 trans:0", acc.toString());

        acc = new Account(1, 1);
        assertEquals("acct:1 bal:1 trans:0", acc.toString());
    }

    @Test
    public void depositMoneyTest() {
        Account acc = new Account(0, 0);

        acc.depositMoney(-1);
        assertEquals("acct:0 bal:-1 trans:1", acc.toString());

        acc.depositMoney(0);
        assertEquals("acct:0 bal:-1 trans:2", acc.toString());

        acc.depositMoney(1);
        assertEquals("acct:0 bal:0 trans:3", acc.toString());
    }

    @Test
    public void withdrawMoneyTest() {
        Account acc = new Account(0, 0);

        acc.withdrawMoney(-1);
        assertEquals("acct:0 bal:1 trans:1", acc.toString());

        acc.withdrawMoney(0);
        assertEquals("acct:0 bal:1 trans:2", acc.toString());

        acc.withdrawMoney(1);
        assertEquals("acct:0 bal:0 trans:3", acc.toString());
    }

}
