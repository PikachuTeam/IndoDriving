package tatteam.com.app_common.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Objects;

import tatteam.com.app_common.R;
import tatteam.com.app_common.ui.fragment.BaseFragment;

/**
 * Created by ThanhNH-Mac on 2/10/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private int fragmentContainerId;
    private HashMap<String, Object> objectHolder;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objectHolder = new HashMap<>();

        setContentView(getLayoutResIdContentView());
        fragmentContainerId = getFragmentContainerId();
        onCreateContentView();
        addFragmentContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearAllHolder();
    }

    private void addFragmentContent() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            BaseFragment fragment = getFragmentContent();
            transaction.add(fragmentContainerId, fragment, fragment.getClass().getName());
            transaction.commit();
        } else {
            popToFirstFragment();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            BaseFragment currentFragment = getCurrentFragment();
            if (currentFragment != null) {
                currentFragment.onBackPressed();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public HashMap<String, Object> getMapHolder() {
        return objectHolder;
    }

    public Object getHolder(String key) {
       return objectHolder.get(key);
    }

    public void putHolder(String key, Object value) {
        objectHolder.put(key, value);
    }

    public boolean containHolder(String key) {
        return objectHolder.containsKey(key);
    }

    public void removeHolder(String key) {
        objectHolder.remove(key);
    }

    public void clearAllHolder() {
        objectHolder.clear();
    }

    public BaseFragment getCurrentFragment() {
        BaseFragment currentFragment = (BaseFragment) getFragmentManager().findFragmentById(fragmentContainerId);
        return currentFragment;
    }

    public BaseFragment findFragment(String fragmentTag) {
        return (BaseFragment) getFragmentManager().findFragmentByTag(fragmentTag);
    }

    public void popFragment() {
        onBackPressed();
    }

    public void popToFragment(String transactionTag) {
        getFragmentManager().popBackStack(transactionTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void popToFirstFragment() {
        getFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void replaceFragment(BaseFragment fragment, String fragmentTag, String transactionTag, boolean needCommitAllowingStateLoss) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_right_enter, R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_left_enter, R.anim.fragment_slide_right_exit);
        transaction.replace(fragmentContainerId, fragment, fragmentTag).addToBackStack(transactionTag);
        if (needCommitAllowingStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    public void onCloseActivity() {
        this.finish();
    }

    protected abstract int getLayoutResIdContentView();

    protected abstract void onCreateContentView();

    protected abstract int getFragmentContainerId();

    protected abstract BaseFragment getFragmentContent();

}
