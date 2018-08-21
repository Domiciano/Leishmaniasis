package i2t.cideim.leishmaniasis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import i2t.cideim.R;

/**
 * Created by Leonardo.
 * Splash screen at the beginning of the app.
 */

public class SplashScreenActivity extends Activity {

    private long splashDelay = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent().setClass(
                        SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, splashDelay);
    }
}
