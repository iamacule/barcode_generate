package vn.mran.barcodegenerate.mvp.presenter;

import android.app.Activity;
import android.support.v4.app.Fragment;

import vn.mran.barcodegenerate.MainActivity;
import vn.mran.barcodegenerate.R;
import vn.mran.barcodegenerate.base.BaseFragment;
import vn.mran.barcodegenerate.fragment.InputFragment;
import vn.mran.barcodegenerate.helper.FragmentNavigator;

/**
 * Created by Mr An on 20/09/2017.
 */

public class MainPresenter {
    public interface MainView {
        void moveTaskToBack();
    }

    private MainView mainView;
    private FragmentNavigator fragmentNavigator;

    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
        initFragmentNavigator();
    }

    private void initFragmentNavigator() {
        fragmentNavigator = new FragmentNavigator(((MainActivity) mainView).getSupportFragmentManager(), R.id.container, R.anim.slide_in_left, R.anim.slide_in_right,
                R.anim.slide_in_right, R.anim.slide_out_right);
        fragmentNavigator.setRootFragment(new InputFragment());
    }

    public void onBackPressed() {
        if (fragmentNavigator.getActiveFragment() instanceof InputFragment) {
            mainView.moveTaskToBack();
        } else
            fragmentNavigator.goOneBack();
    }

    public void goTo(BaseFragment baseFragment) {
        fragmentNavigator.goTo(baseFragment);
    }

    public Fragment getActiveFragment() {
        return fragmentNavigator.getActiveFragment();
    }
}
