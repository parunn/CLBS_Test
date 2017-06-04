package test.clbs.android.example.com.clbsandroidtest;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/**
 * Created by parunpichaiwong on 5/27/2017 AD.
 */

public class SplashScreenActivity extends Activity {
    Handler handler;
    Runnable runnable;
    Long delay_time;
    Long time = 3000L; 
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
      		
        handler = new Handler(); 
        
        runnable = new Runnable() { 
             public void run() { 
                 Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
             } 
        };
    }
    
    public void onResume() {
        super.onResume();
        delay_time = time;
        handler.postDelayed(runnable, delay_time);
        time = System.currentTimeMillis();
    }
    
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }
}
