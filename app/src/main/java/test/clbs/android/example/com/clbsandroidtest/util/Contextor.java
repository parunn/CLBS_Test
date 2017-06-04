package test.clbs.android.example.com.clbsandroidtest.util;

import android.content.Context;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public class Contextor {
    private static Contextor instance;

    public static Contextor getInstance() {
        if (instance == null)
            instance = new Contextor();
        return instance;
    }

    private Context mContext;

    private Contextor() {

    }

    public void init(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }



}
