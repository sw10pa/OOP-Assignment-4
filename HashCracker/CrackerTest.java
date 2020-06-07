package HashCracker;

import java.io.*;
import java.security.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CrackerTest {

    private OutputStream os;

    @BeforeEach
    public void SetUp() {
        os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
    }

    @Test
    public void noArgsTest() throws InterruptedException, NoSuchAlgorithmException {
        Cracker.main(new String[]{});
        assertEquals("Program Arguments:\n" +
                "\tGeneration Mode: password\n" +
                "\tCracking Mode: hash-value length [num-workers]\n\r\n", os.toString());
    }

    @Test
    public void generationModeShortTest() throws InterruptedException, NoSuchAlgorithmException {
        Cracker.main(new String[]{"a"});
        assertEquals("86f7e437faa5a7fce15d1ddcb9eaeaea377667b8\r\n", os.toString());
    }

    @Test
    public void generationModeMediumTest() throws InterruptedException, NoSuchAlgorithmException {
        Cracker.main(new String[]{"fm"});
        assertEquals("adeb6f2a18fe33af368d91b09587b68e3abcb9a7\r\n", os.toString());
    }

    @Test
    public void generationModeLongTest() throws InterruptedException, NoSuchAlgorithmException {
        Cracker.main(new String[]{"xyz"});
        assertEquals("66b27417d37e024c46526c2f6d358a754fc552f3\r\n", os.toString());
    }

    @Test
    public void crackingModeSingleThreadShortTest() throws InterruptedException, NoSuchAlgorithmException {
        Cracker.main(new String[]{"86f7e437faa5a7fce15d1ddcb9eaeaea377667b8", "1"});
        assertEquals("a\r\nall done\r\n", os.toString());
    }

    @Test
    public void crackingModeSingleThreadMediumTest() throws InterruptedException, NoSuchAlgorithmException {
        Cracker.main(new String[]{"adeb6f2a18fe33af368d91b09587b68e3abcb9a7", "2"});
        assertEquals("fm\r\nall done\r\n", os.toString());
    }

    @Test
    public void crackingModeSingleThreadLongTest() throws InterruptedException, NoSuchAlgorithmException {
        Cracker.main(new String[]{"66b27417d37e024c46526c2f6d358a754fc552f3", "3"});
        assertEquals("xyz\r\nall done\r\n", os.toString());
    }

    @Test
    public void crackingModeMultipleThreadsShortTest() throws InterruptedException, NoSuchAlgorithmException {
        Cracker.main(new String[]{"86f7e437faa5a7fce15d1ddcb9eaeaea377667b8", "1", "10"});
        assertEquals("a\r\nall done\r\n", os.toString());
    }

    @Test
    public void crackingModeMultipleThreadsMediumTest() throws InterruptedException, NoSuchAlgorithmException {
        Cracker.main(new String[]{"adeb6f2a18fe33af368d91b09587b68e3abcb9a7", "2", "25"});
        assertEquals("fm\r\nall done\r\n", os.toString());
    }

    @Test
    public void crackingModeMultipleThreadsLongTest() throws InterruptedException, NoSuchAlgorithmException {
        Cracker.main(new String[]{"66b27417d37e024c46526c2f6d358a754fc552f3", "3", "50"});
        assertEquals("xyz\r\nall done\r\n", os.toString());
    }

}
