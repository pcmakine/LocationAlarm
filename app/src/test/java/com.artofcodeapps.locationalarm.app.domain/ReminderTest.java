package com.artofcodeapps.locationalarm.app.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Pete on 14.4.2014.
 */
public class ReminderTest {

    public ReminderTest(){

    }


    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testReminderHasContentWorksForStringContainingOnlySpaces() {
        Reminder reminder = new Reminder("          ");
        assertEquals(reminder.hasContent(), false);
    }
}
