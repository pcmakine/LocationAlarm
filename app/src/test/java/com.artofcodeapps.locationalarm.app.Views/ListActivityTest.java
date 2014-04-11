package test.java.views;

import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.junit.Test;
import android.app.Activity;
import android.widget.TextView;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Robolectric;
import com.artofcodeapps.locationalarm.app.Views.ListActivity;

import dalvik.annotation.TestTargetClass;


/**
 * Created by Pete on 11.4.2014.
 */
@RunWith(RobolectricTestRunner.class)
public class ListActivityTest {
    @Test
    public void testInstantiation() {

        Activity activity = new Activity();

        TextView tv = new TextView(activity);
        tv.setText("e84");

        assertEquals("e84", tv.getText());
    }

    @RunWith(RobolectricTestRunner.class)
    @Test
    public void testIfLasEntryInListGivesCorrectResultForTheLastOne(){
        ListActivity a = (ListActivity) Robolectric.buildActivity(ListActivity.class).create().get();
        boolean result = a.lastEntryInList(2, 1);
        assertEquals(true, result);
    }
}