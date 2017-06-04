package test.clbs.android.example.com.clbsandroidtest.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public class BaseFragment extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            this.activityCallback = (BaseActivityListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement BaseActivityListener");
        }
    }

    protected BaseActivityListener activityCallback;
}