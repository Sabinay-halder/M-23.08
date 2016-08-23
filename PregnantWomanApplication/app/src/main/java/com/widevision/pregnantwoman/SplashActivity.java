package com.widevision.pregnantwoman;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;

import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.mother.ReminderService;
import com.widevision.pregnantwoman.util.Constant;


public class SplashActivity extends AppCompatActivity {
    private final static int MSG_CONTINUE = 1234;
    private final static long DELAY = 2000;


    @Override
    protected void onCreate(Bundle args) {
        super.onCreate(args);
        setContentView(R.layout.activity_splash);
        Display display = getWindowManager().getDefaultDisplay();
        Constant.height = display.getHeight();
        Constant.width = display.getWidth();
        mHandler.sendEmptyMessageDelayed(MSG_CONTINUE, DELAY);
        ActiveAndroidDBHelper.getInstance().orderByDate();
        if (Constant.isMyServiceRunning(ReminderService.class, SplashActivity.this)) {
           // stopService(new Intent(SplashActivity.this, ReminderService.class));
        } else {
            startService(new Intent(SplashActivity.this, ReminderService.class));
        }

    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(MSG_CONTINUE);
        super.onDestroy();
    }

    private void _continue() {
        startActivity(new Intent(this, HomeActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private final Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_CONTINUE:
                    _continue();
                    break;
            }
        }
    };
}
