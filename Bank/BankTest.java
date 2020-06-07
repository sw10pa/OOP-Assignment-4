package Bank;

import java.io.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

    private OutputStream os;

    @BeforeEach
    public void SetUp() {
        os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
    }

    @Test
    public void noArgsTest() throws InterruptedException {
        Bank.main(new String[]{});
        assertEquals("Program Arguments: transaction-file [num-workers]\r\n", os.toString());
    }

    @Test
    public void singleThreadSmallTest() throws InterruptedException {
        Bank.main(new String[]{"Bank/small.txt"});
        assertEquals("acct:0 bal:999 trans:1\r\n" +
                "acct:1 bal:1001 trans:1\r\n" +
                "acct:2 bal:999 trans:1\r\n" +
                "acct:3 bal:1001 trans:1\r\n" +
                "acct:4 bal:999 trans:1\r\n" +
                "acct:5 bal:1001 trans:1\r\n" +
                "acct:6 bal:999 trans:1\r\n" +
                "acct:7 bal:1001 trans:1\r\n" +
                "acct:8 bal:999 trans:1\r\n" +
                "acct:9 bal:1001 trans:1\r\n" +
                "acct:10 bal:999 trans:1\r\n" +
                "acct:11 bal:1001 trans:1\r\n" +
                "acct:12 bal:999 trans:1\r\n" +
                "acct:13 bal:1001 trans:1\r\n" +
                "acct:14 bal:999 trans:1\r\n" +
                "acct:15 bal:1001 trans:1\r\n" +
                "acct:16 bal:999 trans:1\r\n" +
                "acct:17 bal:1001 trans:1\r\n" +
                "acct:18 bal:999 trans:1\r\n" +
                "acct:19 bal:1001 trans:1\r\n", os.toString());
    }

    @Test
    public void singleThread5kTest() throws InterruptedException {
        Bank.main(new String[]{"Bank/5k.txt"});
        assertEquals("acct:0 bal:1000 trans:518\r\n" +
                "acct:1 bal:1000 trans:444\r\n" +
                "acct:2 bal:1000 trans:522\r\n" +
                "acct:3 bal:1000 trans:492\r\n" +
                "acct:4 bal:1000 trans:526\r\n" +
                "acct:5 bal:1000 trans:526\r\n" +
                "acct:6 bal:1000 trans:474\r\n" +
                "acct:7 bal:1000 trans:472\r\n" +
                "acct:8 bal:1000 trans:436\r\n" +
                "acct:9 bal:1000 trans:450\r\n" +
                "acct:10 bal:1000 trans:498\r\n" +
                "acct:11 bal:1000 trans:526\r\n" +
                "acct:12 bal:1000 trans:488\r\n" +
                "acct:13 bal:1000 trans:482\r\n" +
                "acct:14 bal:1000 trans:516\r\n" +
                "acct:15 bal:1000 trans:492\r\n" +
                "acct:16 bal:1000 trans:520\r\n" +
                "acct:17 bal:1000 trans:528\r\n" +
                "acct:18 bal:1000 trans:586\r\n" +
                "acct:19 bal:1000 trans:504\r\n", os.toString());
    }

    @Test
    public void singleThread100kTest() throws InterruptedException {
        Bank.main(new String[]{"Bank/100k.txt"});
        assertEquals("acct:0 bal:1000 trans:10360\r\n" +
                "acct:1 bal:1000 trans:8880\r\n" +
                "acct:2 bal:1000 trans:10440\r\n" +
                "acct:3 bal:1000 trans:9840\r\n" +
                "acct:4 bal:1000 trans:10520\r\n" +
                "acct:5 bal:1000 trans:10520\r\n" +
                "acct:6 bal:1000 trans:9480\r\n" +
                "acct:7 bal:1000 trans:9440\r\n" +
                "acct:8 bal:1000 trans:8720\r\n" +
                "acct:9 bal:1000 trans:9000\r\n" +
                "acct:10 bal:1000 trans:9960\r\n" +
                "acct:11 bal:1000 trans:10520\r\n" +
                "acct:12 bal:1000 trans:9760\r\n" +
                "acct:13 bal:1000 trans:9640\r\n" +
                "acct:14 bal:1000 trans:10320\r\n" +
                "acct:15 bal:1000 trans:9840\r\n" +
                "acct:16 bal:1000 trans:10400\r\n" +
                "acct:17 bal:1000 trans:10560\r\n" +
                "acct:18 bal:1000 trans:11720\r\n" +
                "acct:19 bal:1000 trans:10080\r\n", os.toString());
    }

    @Test
    public void multipleThreadsSmallTest() throws InterruptedException {
        Bank.main(new String[]{"Bank/small.txt", "10"});
        assertEquals("acct:0 bal:999 trans:1\r\n" +
                "acct:1 bal:1001 trans:1\r\n" +
                "acct:2 bal:999 trans:1\r\n" +
                "acct:3 bal:1001 trans:1\r\n" +
                "acct:4 bal:999 trans:1\r\n" +
                "acct:5 bal:1001 trans:1\r\n" +
                "acct:6 bal:999 trans:1\r\n" +
                "acct:7 bal:1001 trans:1\r\n" +
                "acct:8 bal:999 trans:1\r\n" +
                "acct:9 bal:1001 trans:1\r\n" +
                "acct:10 bal:999 trans:1\r\n" +
                "acct:11 bal:1001 trans:1\r\n" +
                "acct:12 bal:999 trans:1\r\n" +
                "acct:13 bal:1001 trans:1\r\n" +
                "acct:14 bal:999 trans:1\r\n" +
                "acct:15 bal:1001 trans:1\r\n" +
                "acct:16 bal:999 trans:1\r\n" +
                "acct:17 bal:1001 trans:1\r\n" +
                "acct:18 bal:999 trans:1\r\n" +
                "acct:19 bal:1001 trans:1\r\n", os.toString());
    }

    @Test
    public void multipleThreads5kTest() throws InterruptedException {
        Bank.main(new String[]{"Bank/5k.txt", "10"});
        assertEquals("acct:0 bal:1000 trans:518\r\n" +
                "acct:1 bal:1000 trans:444\r\n" +
                "acct:2 bal:1000 trans:522\r\n" +
                "acct:3 bal:1000 trans:492\r\n" +
                "acct:4 bal:1000 trans:526\r\n" +
                "acct:5 bal:1000 trans:526\r\n" +
                "acct:6 bal:1000 trans:474\r\n" +
                "acct:7 bal:1000 trans:472\r\n" +
                "acct:8 bal:1000 trans:436\r\n" +
                "acct:9 bal:1000 trans:450\r\n" +
                "acct:10 bal:1000 trans:498\r\n" +
                "acct:11 bal:1000 trans:526\r\n" +
                "acct:12 bal:1000 trans:488\r\n" +
                "acct:13 bal:1000 trans:482\r\n" +
                "acct:14 bal:1000 trans:516\r\n" +
                "acct:15 bal:1000 trans:492\r\n" +
                "acct:16 bal:1000 trans:520\r\n" +
                "acct:17 bal:1000 trans:528\r\n" +
                "acct:18 bal:1000 trans:586\r\n" +
                "acct:19 bal:1000 trans:504\r\n", os.toString());
    }

    @Test
    public void multipleThreads100kTest() throws InterruptedException {
        Bank.main(new String[]{"Bank/100k.txt", "10"});
        assertEquals("acct:0 bal:1000 trans:10360\r\n" +
                "acct:1 bal:1000 trans:8880\r\n" +
                "acct:2 bal:1000 trans:10440\r\n" +
                "acct:3 bal:1000 trans:9840\r\n" +
                "acct:4 bal:1000 trans:10520\r\n" +
                "acct:5 bal:1000 trans:10520\r\n" +
                "acct:6 bal:1000 trans:9480\r\n" +
                "acct:7 bal:1000 trans:9440\r\n" +
                "acct:8 bal:1000 trans:8720\r\n" +
                "acct:9 bal:1000 trans:9000\r\n" +
                "acct:10 bal:1000 trans:9960\r\n" +
                "acct:11 bal:1000 trans:10520\r\n" +
                "acct:12 bal:1000 trans:9760\r\n" +
                "acct:13 bal:1000 trans:9640\r\n" +
                "acct:14 bal:1000 trans:10320\r\n" +
                "acct:15 bal:1000 trans:9840\r\n" +
                "acct:16 bal:1000 trans:10400\r\n" +
                "acct:17 bal:1000 trans:10560\r\n" +
                "acct:18 bal:1000 trans:11720\r\n" +
                "acct:19 bal:1000 trans:10080\r\n", os.toString());
    }

}
