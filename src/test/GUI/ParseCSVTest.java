package test.GUI;


import GUI.ParseCSV;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by slavik on 07.04.17.
 */
public class ParseCSVTest {
    private ParseCSV parseCSV;

    @Test
    public void readFromFile() throws Exception {
        assertFalse(parseCSV.readFromFile("o"));
        assertFalse(parseCSV.readFromFile(""));
    }

    @Test
    public void readFromFileExist() throws Exception {
        assertEquals(true,parseCSV.readFromFile("objects"));
    }

    @Before
    public void setUp() throws Exception {
         parseCSV = new ParseCSV();
    }
}