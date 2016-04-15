package org.openedu.www.test;

import tw.openedu.mobile.BuildConfig;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyTests extends BaseTestCase {

    @Test
    public void testGetDisplayVersionName() throws Exception {
        String name = BuildConfig.VERSION_NAME;
        assertTrue("failed to read versionName, found=" + name,
                name != null && !name.isEmpty());
    }
}
