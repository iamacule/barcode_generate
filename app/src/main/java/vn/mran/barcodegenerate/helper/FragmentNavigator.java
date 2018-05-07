package vn.mran.barcodegenerate.helper;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Administrator on 06.07.2015.
 */
public class FragmentNavigator {
    private static final int NO_RES_SET = 0;
    @NonNull
    protected final FragmentManager mFragmentManager;
    @IdRes
    protected final int mDefaultContainer;

    private int mDefaultInAnim;
    private int mDefaultOutAnim;
    private int mDefeaultPopInAnim;
    private int mDefeaultPopOutAnim;

    private onStackChanged onStackChanged;
    private String mRootFragmentTag;

    /**
     * This constructor should be only called once per
     *
     * @param fragmentManager  Your FragmentManger
     * @param defaultContainer Your container id where the fragments should be placed
     * @param defaultInAnim    Your default animation for the "In Animation" it will be added on each
     *                         transaction.
     * @param defaultOutAnim   Your default animation for the "Out Animation" it will be added on each
     *                         transaction.
     */
    public FragmentNavigator(@NonNull final FragmentManager fragmentManager
            , @IdRes final int defaultContainer
            , @AnimRes final int defaultInAnim
            , @AnimRes final int defaultOutAnim
            , @AnimRes final int defaultPopInAnim
            , @AnimRes final int defaultPopOutAnim) {
        mFragmentManager = fragmentManager;
        mDefaultContainer = defaultContainer;
        mDefaultInAnim = defaultInAnim;
        mDefaultOutAnim = defaultOutAnim;
        mDefeaultPopInAnim = defaultPopInAnim;
        mDefeaultPopOutAnim = defaultPopOutAnim;
        mFragmentManager
                .addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (onStackChanged!=null)
                            onStackChanged.onChanged(getActiveFragment());
                    }
                });
    }

    public void setOnStackChanged(FragmentNavigator.onStackChanged onStackChanged) {
        this.onStackChanged = onStackChanged;
    }

    /**
     * @return the current active fragment. If no fragment is active it return null.
     */
    public Fragment getActiveFragment() {
        String tag;
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            tag = mRootFragmentTag;
        } else {
            tag = mFragmentManager
                    .getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1).getName();
        }
        return mFragmentManager.findFragmentByTag(tag);
    }

    /**
     * Pushes the fragment, and add it to the history (BackStack) if you have set
     * a default animation it will be added to the transaction.
     *
     * @param fragment The fragment which will be added
     */
    public void goTo(final Fragment fragment) {
        if (mDefeaultPopInAnim == 0 && mDefeaultPopOutAnim == 0)
            goTo(fragment, mDefaultInAnim, mDefaultOutAnim);
        else
            goTo(fragment, mDefaultInAnim, mDefaultOutAnim, mDefeaultPopInAnim, mDefeaultPopOutAnim);
    }

    /**
     * Pushes the fragment, and add it to the history (BackStack) with the specific animation.
     * You have to set both animation.
     *
     * @param fragment The fragment which will be added
     * @param inAnim   Your dismissDialog "In Animation"
     * @param outAnim  Your dismissDialog "Out Animation"
     */
    public void goTo(final Fragment fragment, @AnimRes int inAnim, @AnimRes int outAnim) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.addToBackStack(getTag(fragment));
        if (inAnim != NO_RES_SET && outAnim != NO_RES_SET) {
            transaction.setCustomAnimations(inAnim, outAnim);
        }
        transaction.add(mDefaultContainer, fragment, getTag(fragment));
        transaction.commit();
        mFragmentManager.executePendingTransactions();
    }

    /**
     * Pushes the fragment, and add it to the history (BackStack) with the specific animation.
     * You have to set both animation.
     *
     * @param fragment   The fragment which will be added
     * @param inAnim     Your dismissDialog "In Animation"
     * @param outAnim    Your dismissDialog "Out Animation"
     * @param popInAnim  Your dismissDialog "Pop In Animation"
     * @param popOutAnim Your dismissDialog "Pop Out Animation"
     */
    public void goTo(final Fragment fragment, @AnimRes int inAnim, @AnimRes int outAnim, @AnimRes int popInAnim, @AnimRes int popOutAnim) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.addToBackStack(getTag(fragment));
        if (inAnim != NO_RES_SET && outAnim != NO_RES_SET) {
            transaction.setCustomAnimations(inAnim, outAnim, popInAnim, popOutAnim);
        }
        transaction.add(mDefaultContainer, fragment, getTag(fragment));
        transaction.commit();
        mFragmentManager.executePendingTransactions();
    }

    /**
     * This is just a helper method which returns the simple name of the fragment.
     *
     * @param fragment That get added to the history (BackStack)
     * @return The simple name of the given fragment
     */
    protected String getTag(final Fragment fragment) {
        return fragment.getClass().getSimpleName();
    }

    /**
     * Set the new root fragment. If there is any entry in the history (BackStack) it will be
     * deleted.
     *
     * @param rootFragment The new root fragment
     */
    public void setRootFragment(final Fragment rootFragment) {
        if (getSize() > 0) {
            this.clearHistory();
        }
        mRootFragmentTag = getTag(rootFragment);
        this.replaceFragment(rootFragment);
    }

    /**
     * Replace the current fragment with the given one, without to add it to backstack. So when the
     * users navigates away from the given fragment it will not appaer in the history.
     *
     * @param fragment The new fragment
     */
    private void replaceFragment(final Fragment fragment) {
        mFragmentManager.beginTransaction()
                .replace(mDefaultContainer, fragment, getTag(fragment))
                .commit();
        mFragmentManager.executePendingTransactions();
    }

    /**
     * Goes one entry back in the history
     */
    public void goOneBack() {
        mFragmentManager.popBackStackImmediate();
    }

    /**
     * Get prevideo fragment on back stack
     * @return
     */
    public Fragment getPreviousFragment() {
        return mFragmentManager.getFragments().get(mFragmentManager.getBackStackEntryCount() - 1);
    }

    /**
     * @return The current size of the history (BackStack)
     */
    public int getSize() {
        return mFragmentManager.getBackStackEntryCount();
    }

    /**
     * Goes the whole history back until to the first fragment in the history. It would be the same if
     * the user would click so many times the back button until he reach the first fragment of the
     * app.
     */
    public void goToRoot() {
        while (getSize() >= 1) {
            goOneBack();
        }
    }

    /**
     * Clears the whole history so it will no BackStack entry there any more.
     */
    public void clearHistory() {
        //noinspection StatementWithEmptyBody - it works as designed
        while (mFragmentManager.popBackStackImmediate()) {
            ;
        }
    }

    public interface onStackChanged {

        void onChanged(Fragment fragment);
    }
}
