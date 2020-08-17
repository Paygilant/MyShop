package com.paygilant.myshop;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.SdkSuppress;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import android.util.Log;

import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.File;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 19)
public class ExampleInstrumentedTest {

    private UiDevice mDevice;

    @Before
    public void before() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

//        assertThat(mDevice, notNullValue());
        // Start from the home screen
        mDevice.pressHome();
    }

    @org.junit.Test
    public void test() throws InterruptedException {
        openApp("com.paygilant.deviceidetification");

        takeScreenshot("screenshot-1.png");
        for (int i =0 ;i<4;i++){
            Thread.sleep(5000);
            allowPermissionsIfNeeded();
        }

        UiObject2 editText = waitForObject(By.res("com.paygilant.deviceidetification:id/editTextConnect"));
        takeScreenshot("screenshot-2.png");
//        String uuid = Settings.Secure.getString(InstrumentationRegistry.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        editText.setText(Build.MODEL+"_TEST");
        UiObject2 protectObject = waitForObject(By.text("CONNECT"));
        protectObject.click();
        takeScreenshot("screenshot-3.png");
        Thread.sleep(5000);
        protectObject = waitForObject(By.text("BUY ONLINE"));
        protectObject.click();
        Thread.sleep(5000);
        onView(withId(R.id.gridView1)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        Thread.sleep(5000);

        protectObject = waitForObject(By.text("CONTINUE SHOPPING"));
        protectObject.click();
        Thread.sleep(5000);
        onView(withId(R.id.gridView1)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        Thread.sleep(5000);
        protectObject = waitForObject(By.text("CONTINUE SHOPPING"));

        protectObject.click();
        Thread.sleep(5000);
        onView(withId(R.id.gridView1)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        Thread.sleep(5000);
        protectObject = waitForObject(By.text("CONTINUE SHOPPING"));

        protectObject.click();
        Thread.sleep(5000);
        onView(withId(R.id.gridView1)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
        Thread.sleep(5000);
    }



    private void allowPermissionsIfNeeded()  {
        if (Build.VERSION.SDK_INT >= 23) {
            UiObject allowPermissions = mDevice.findObject(new UiSelector().text("ALLOW"));
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click();
                } catch (UiObjectNotFoundException e) {
                    Log.e("TEST" ,"There is no permissions dialog to interact with ");
                }
            }
        }
    }
    private void openApp(String packageName) {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private UiObject2 waitForObject(BySelector selector) throws InterruptedException {
        UiObject2 object = null;
        int timeout = 30000;
        int delay = 1000;
        long time = System.currentTimeMillis();
        while (object == null) {
            object = mDevice.findObject(selector);
            Thread.sleep(delay);
            if (System.currentTimeMillis() - timeout > time) {
                fail();
            }
        }
        return object;
    }

    private void takeScreenshot(String name) {
        Log.d("TEST", "takeScreenshot");
        String dir = String.format("/storage/emulated/0/DCIM"+InstrumentationRegistry.getContext().getPackageName()+File.separator+ "test-screenshots");
        File theDir = new File(dir);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
        mDevice.takeScreenshot(new File(String.format( dir+File.separator+ name)));
        Log.d("TEST" ,"Path:"+dir+File.separator+name);

    }
}
