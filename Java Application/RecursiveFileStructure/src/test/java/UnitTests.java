
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTests {

    Main m = new Main();
    @Test
    public void readAFileFromResourceNotNull() throws URISyntaxException {
        File fileToRead = m.getFile("TestFile1.txt");
        assertNotNull(fileToRead);
    }
    @Test
    public void readAFileFromResourceNotNull_fileTwo() throws URISyntaxException {
        File fileToRead = m.getFile("TestFile2.txt");
        assertNotNull(fileToRead);
    }
    @Test
    public void printFileOne() throws URISyntaxException {
        File fileToRead = m.getFile("TestFile1.txt");
        try {
            Main.printFile(fileToRead);
        }
        catch (Exception e) {
            Assert.fail("Exception " + e);
        }

    }
    @Test
    public void printFileTwo() throws URISyntaxException {
        File fileToRead = m.getFile("TestFile2.txt");
        try {
            Main.printFile(fileToRead);
        }
        catch (Exception e) {
            Assert.fail("Exception " + e);
        }

    }
    @Test
    public void getIndentsTest1() {
        assertEquals(1,m.getIndent("    test"));
    }
    @Test
    public void getIndentsTest2() {
        assertEquals(0,m.getIndent("test"));
    }
    @Test
    public void getIndentsTest3() {
        assertEquals(3,m.getIndent("            test"));
    }
    @Test
    public void removeIndents1() {
       assertEquals("hello",m.removeIndent("    hello"));
    }
    @Test
    public void removeIndents2() {
        assertEquals("hello",m.removeIndent("                       hello"));
    }
    @Test
    public void removeIndents3() {
        assertEquals("hello",m.removeIndent("hello"));
    }
}
