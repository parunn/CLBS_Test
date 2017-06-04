package test.clbs.android.example.com.clbsandroidtest.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public interface BaseActivityListener {

    public void addPage(Fragment page);
    public void setResult(Fragment page, Bundle bundle);
}
