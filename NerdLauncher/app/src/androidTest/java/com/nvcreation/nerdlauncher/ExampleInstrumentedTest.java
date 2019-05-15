/*
 *
 *  * App Name : Nerd Launcher (Based on Big Nerd Ranch Guide)
 *  * Created by Jinisha Vora | Email: jinishatanna@gmail.com / jinishavora15@gmail.com | Domain: nvcreation.com
 *  * Copyright (c) 2019. All Rights Reserved.
 *  * Last Modified: 5/8/19 5:26 PM
 *
 *
 */

package com.nvcreation.nerdlauncher;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.nvcreation.nerdlauncher", appContext.getPackageName());
    }
}
