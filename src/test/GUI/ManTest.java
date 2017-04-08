package test.GUI;

import GUI.Man;
import deprecated.People;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by slavik on 07.04.17.
 */
public class ManTest {
    private Man people;

    @Before
    public void setUp() throws Exception {
        people = new People("neiw");
        people.setAge((byte) 20);
    }

    @Test
    public void setName() throws Exception {
        assertFalse(people.setName("39l"));
        assertTrue(people.setName("лаl"));
        assertFalse(people.setName("_JKll"));
        assertTrue(people.setName("JKll"));
        assertFalse(people.setName("JK ll"));
    }


    @Test
    public void getName() throws Exception {
        assertEquals("neiw",people.getName());
        assertEquals("",new People("").getName());
    }

    @Test
    public void setAge(){
        assertFalse(people.setAge((byte) -1));
        assertTrue(people.setAge((byte) 'k'));
        assertTrue(people.setAge((byte) 0));
    }

    @Test(expected = AssertionError.class)
    public void setAgeBigNumber(){
        assertTrue(people.setAge((byte) 409));
    }

    @Test
    public void getAge() throws Exception {
        assertEquals(20,people.getAge());

    }

}