package test.clbs.android.example.com.clbsandroidtest;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import test.clbs.android.example.com.clbsandroidtest.base.BaseActivityListener;
import test.clbs.android.example.com.clbsandroidtest.databinding.ActivityHomeBinding;
import test.clbs.android.example.com.clbsandroidtest.map.MapSearchResultFragment;
import test.clbs.android.example.com.clbsandroidtest.map.ListViewSearchResultFragment;
import test.clbs.android.example.com.clbsandroidtest.util.BottomNavigationViewUtil;
import test.clbs.android.example.com.clbsandroidtest.util.Contextor;

public class HomeActivity extends AppCompatActivity implements BaseActivityListener{

    //Global Variable
    ActivityHomeBinding binding; //Data Binding Object

    BottomNavigationView bottomNavigationView;
    DrawerLayout rootLayout;

    Fragment previousFragment;
    Fragment currentFragment;

    Fragment mapSearchFragment;
    Fragment mapSearchResultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Binding Mapping
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        initial();
    }

    //Initial
    private void initial() {

        Contextor.getInstance().init(this.getApplicationContext());
        initBottomNavigationView();
        initFragment();

        replaceFragment(this.mapSearchResultFragment);
    }

    //Initial Replace Fragment
    private void initFragment() {
        this.mapSearchFragment = new MapSearchResultFragment();
        this.mapSearchResultFragment = new ListViewSearchResultFragment();
    }

    private void initBottomNavigationView() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        rootLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        BottomNavigationViewUtil.disableShiftMode(this.binding.bottomNavigation);

        this.binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.tab01:
                        replaceFragment(mapSearchResultFragment);
                        break;
                    case R.id.tab02:
                        replaceFragment(mapSearchFragment);
                        break;
                }
                return false;
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.nestedScrollView, fragment);
        transaction.addToBackStack(null);

        transaction.commit();

        this.previousFragment = (this.currentFragment == null) ? fragment : this.currentFragment;
        this.currentFragment = fragment;
    }

    @Override
    public void addPage(Fragment page) {

    }

    @Override
    public void setResult(Fragment page, Bundle bundle) {

    }


}
