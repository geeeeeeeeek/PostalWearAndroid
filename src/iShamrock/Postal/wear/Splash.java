package iShamrock.Postal.wear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends Activity {
    private static final int SPLASH_DISPLAY_LENGTH = 1000;
    private TextView splash_title;
    private ImageView splash_logo;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

//        initCommonComponents();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an intent that will start the main activity*/
                Intent mainIntent = new Intent(Splash.this, ChooseActivityActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void initCommonComponents() {
        splash_logo = (ImageView) findViewById(R.id.splash_logo);
        splash_title = (TextView) findViewById(R.id.splash_title);

        Log.d("alpha", "" + (splash_logo.getAlpha()));
        while (splash_logo.getAlpha() > 0) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            splash_logo.setAlpha(splash_logo.getAlpha() - 0.1f);
            splash_title.setAlpha(1.0f - splash_logo.getAlpha());

        }
    }
}
