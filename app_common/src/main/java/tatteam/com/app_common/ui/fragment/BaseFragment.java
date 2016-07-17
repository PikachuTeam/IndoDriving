package tatteam.com.app_common.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tatteam.com.app_common.ui.activity.BaseActivity;
import tatteam.com.app_common.ui.drawable.FractionFrameLayout;

/**
 * Created by ThanhNH-Mac on 2/10/16.
 */
public abstract class BaseFragment extends Fragment {


    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResIdContentView(), container, false);
        this.onCreateContentView(rootView, savedInstanceState);

        if (!(rootView instanceof FractionFrameLayout)) {
            FractionFrameLayout fractionFrameLayout = new FractionFrameLayout(getActivity());
            fractionFrameLayout.addView(rootView);
            return fractionFrameLayout;
        }
        return rootView;
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            getBaseActivity().onCloseActivity();
        }
    }

    public void runOnUIThread(Runnable runnable) {
        if (getBaseActivity() != null) {
            getBaseActivity().runOnUiThread(runnable);
        }
    }

    public Object getHolder(String key) {
        return getBaseActivity().getHolder(key);
    }

    public void putHolder(String key, Object value) {
        getBaseActivity().putHolder(key, value);
    }

    public boolean containHolder(String key) {
        return getBaseActivity().containHolder(key);
    }

    public void removeHolder(String key) {
        getBaseActivity().removeHolder(key);
    }

    public void clearAllHolder() {
        getBaseActivity().clearAllHolder();
    }

    public BaseFragment getCurrentFragment() {
        return getBaseActivity().getCurrentFragment();
    }

    public BaseFragment findFragment(String fragmentTag) {
        return getBaseActivity().findFragment(fragmentTag);
    }

    public void popFragment() {
        getBaseActivity().popFragment();
    }

    public void popToFragment(String transactionTag) {
        getBaseActivity().popToFragment(transactionTag);
    }

    public void popToFirstFragment() {
        getBaseActivity().popToFirstFragment();
    }

    public void replaceFragment(BaseFragment fragment, String fragmentTag, boolean needCommitAllowingStateLoss) {
        replaceFragment(fragment, fragmentTag, fragmentTag, needCommitAllowingStateLoss);
    }

    public void replaceFragment(BaseFragment fragment, String fragmentTag) {
        replaceFragment(fragment, fragmentTag, fragmentTag, false);
    }

    public void replaceFragment(BaseFragment fragment, String fragmentTag, String transactionTag) {
        replaceFragment(fragment, fragmentTag, transactionTag, false);
    }

    public void replaceFragment(BaseFragment fragment, String fragmentTag, String transactionTag, boolean needCommitAllowingStateLoss) {
        getBaseActivity().replaceFragment(fragment, fragmentTag, transactionTag, needCommitAllowingStateLoss);
    }

    public void replaceFragmentWithCustomAnimation(BaseFragment fragment, String fragmentTag,
                                                   int enter, int exit, int popEnter, int popExit) {
        getBaseActivity().replaceFragmentWithCustomAnimation(
                fragment, fragmentTag, fragmentTag, enter, exit, popEnter, popExit);
    }

    public void replaceFragmentWithCustomAnimation(BaseFragment fragment, String fragmentTag,
                                                   int enter, int exit) {
        getBaseActivity().replaceFragmentWithCustomAnimation(
                fragment, fragmentTag, fragmentTag, enter, exit);
    }

    public void replaceFragmentWithOutAnimation(BaseFragment fragment, String fragmentTag) {
        getBaseActivity().replaceFragmentWithOutAnimation(fragment, fragmentTag, fragmentTag);
    }

    protected abstract int getLayoutResIdContentView();

    protected abstract void onCreateContentView(View rootView, Bundle savedInstanceState);

}
