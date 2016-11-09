package tw.openedu.android.test.http;

import tw.openedu.android.model.api.ProfileModel;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SocialLoginTests extends HttpBaseTestCase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testGetProfile() throws Exception {
        ProfileModel profile = api.getProfile();
        assertNotNull(profile);
        assertNotNull("profile.email cannot be null", profile.email);
        print("finished getProfile");
    }
}
