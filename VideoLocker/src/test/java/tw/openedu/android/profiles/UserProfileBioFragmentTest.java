package tw.openedu.android.profiles;

import android.databinding.DataBindingUtil;

import org.assertj.core.api.Assertions;
import tw.openedu.android.databinding.FragmentUserProfileBinding;
import tw.openedu.android.databinding.FragmentUserProfileBioBinding;
import tw.openedu.android.view.PresenterFragmentTest;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.verify;

public class UserProfileBioFragmentTest extends PresenterFragmentTest<UserProfileBioFragment, UserProfileBioPresenter, UserProfileBioPresenter.ViewInterface> {

    FragmentUserProfileBioBinding binding;

    @Before
    public void before() {
        startFragment(UserProfileBioFragment.newInstance());
        binding = DataBindingUtil.getBinding(fragment.getView());
        Assertions.assertThat(binding).isNotNull();
    }
    @Test
    public void click_onParentalConsentEditProfileButton_callsEditProfile() {
        binding.parentalConsentEditProfileButton.performClick();
        verify(presenter).onEditProfile();
    }

    @Test
    public void click_onIncompleteEditProfileButton_callsEditProfile() {
        binding.incompleteEditProfileButton.performClick();
        verify(presenter).onEditProfile();
    }

}
