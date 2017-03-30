package com.wxws.myticket;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    @SmallTest
    public void test_case(){
        final int expected = 5 ;
        final int reality = 5;
        assertEquals(expected,reality);
    }
}