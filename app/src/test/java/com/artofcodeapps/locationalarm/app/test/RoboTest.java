package com.artofcodeapps.locationalarm.app.test;

/**
 * Created by Pete on 26.4.2014.
 */
import com.artofcodeapps.locationalarm.app.Views.EditAddActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
public class RoboTest {
    private final ActivityController<EditAddActivity> controller = Robolectric.buildActivity(EditAddActivity.class);

    @Test
    public void testTrueIsTrue() throws Exception {
        assertEquals(true, true);
    }

    @Test
    public void testActivityCreation(){
        EditAddActivity activity = controller.create().start().resume().get();
        assertEquals(true, activity != null);
    }
}