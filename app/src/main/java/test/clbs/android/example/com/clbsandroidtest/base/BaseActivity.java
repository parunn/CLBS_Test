package test.clbs.android.example.com.clbsandroidtest.base;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

import test.clbs.android.example.com.clbsandroidtest.R;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public class BaseActivity extends AppCompatActivity implements BaseActivityListener {

    @Override
    public void addPage(Fragment page) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.nestedScrollView, page);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void setResult(Fragment page, Bundle bundle) {

    }
}