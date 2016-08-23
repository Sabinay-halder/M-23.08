package com.widevision.pregnantwoman;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.widevision.pregnantwoman.baby.BabyHomeActivity;
import com.widevision.pregnantwoman.baby.BabyNameAdapter;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.database.BabyInfoTable;
import com.widevision.pregnantwoman.model.HideKeyActivity;
import com.widevision.pregnantwoman.mother.WomanHomeActivity;
import com.widevision.pregnantwoman.util.PreferenceConnector;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends HideKeyActivity {

    @Bind(R.id.mother_btn)
    Button mMotherBtn;
    @Bind(R.id.baby_btn)
    Button mBabyBtn;
    private ShowcaseView showcaseView;
    private int tag = 0;
    private OnShowcaseEventListener listener = new OnShowcaseEventListener() {
        @Override
        public void onShowcaseViewHide(ShowcaseView showcaseView) {
            if (tag == 0) {
                if (PreferenceConnector.readString(HomeActivity.this, PreferenceConnector.MOTHER_BTN, "No").equals("Yes") && PreferenceConnector.readString(HomeActivity.this, PreferenceConnector.BABY_BTN, "No").equals("No")) {
                    PreferenceConnector.writeString(HomeActivity.this, PreferenceConnector.BABY_BTN, "Yes");
                    ViewTarget target = new ViewTarget(R.id.baby_btn, HomeActivity.this);
                    showcaseView = new ShowcaseView.Builder(HomeActivity.this)
                            .withHoloShowcase()
                            .setTarget(target)
                            .setContentTitle("")
                            .setContentText("Click here or create Baby Profile.")
                            .setStyle(R.style.CustomShowcaseTheme)
                            .replaceEndButton(R.layout.ok_button)
                            .setShowcaseEventListener(listener)
                            .build();
                }
            } else {
                tag = 0;
            }
        }

        @Override
        public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

        }

        @Override
        public void onShowcaseViewShow(ShowcaseView showcaseView) {

        }

        @Override
        public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ButterKnife.bind(HomeActivity.this);

        if (PreferenceConnector.readString(HomeActivity.this, PreferenceConnector.MOTHER_BTN, "No").equals("No")) {
            ViewTarget target = new ViewTarget(R.id.mother_btn, HomeActivity.this);
            showcaseView = new ShowcaseView.Builder(HomeActivity.this)
                    .withHoloShowcase()
                    .setTarget(target)
                    .setContentTitle("")
                    .setContentText("Click here or create Mother Profile.")
                    .setStyle(R.style.CustomShowcaseTheme)
                    .replaceEndButton(R.layout.ok_button)
                    .setShowcaseEventListener(listener)
                    .build();
            PreferenceConnector.writeString(HomeActivity.this, PreferenceConnector.MOTHER_BTN, "Yes");
        } else if (PreferenceConnector.readString(HomeActivity.this, PreferenceConnector.MOTHER_BTN, "No").equals("Yes") && PreferenceConnector.readString(HomeActivity.this, PreferenceConnector.BABY_BTN, "No").equals("No")) {
            ViewTarget target = new ViewTarget(R.id.baby_btn, HomeActivity.this);
            showcaseView = new ShowcaseView.Builder(HomeActivity.this)
                    .withHoloShowcase()
                    .setTarget(target)
                    .setContentTitle("")
                    .setContentText("Click here or create Baby Profile.")
                    .setStyle(R.style.CustomShowcaseTheme)
                    .replaceEndButton(R.layout.ok_button)
                    .setShowcaseEventListener(listener)
                    .build();
            PreferenceConnector.writeString(HomeActivity.this, PreferenceConnector.BABY_BTN, "Yes");
        }

        mMotherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showcaseView != null) {
                    tag = 1;
                    showcaseView.hide();
                }
                if (PreferenceConnector.readString(HomeActivity.this, PreferenceConnector.IS_FIRSTTIME_MOTHER, "Yes").equals("Yes")) {
                    startActivity(new Intent(HomeActivity.this, WomanHomeActivity.class));
                    overridePendingTransition(R.anim.push_infromright_anim, R.anim.push_out_to_left_anim);
                    finish();
                } else {
                    startActivity(new Intent(HomeActivity.this, MainFragmentActivity.class));
                    overridePendingTransition(R.anim.push_infromright_anim, R.anim.push_out_to_left_anim);
                    finish();
                }
            }
        });

        mBabyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showcaseView != null) {
                    tag = 1;
                    showcaseView.hide();
                }
                String name = PreferenceConnector.readString(HomeActivity.this, PreferenceConnector.IS_FIRSTTIME_BABY, "Yes");
                ActiveAndroidDBHelper helper = ActiveAndroidDBHelper.getInstance();
                final List<BabyInfoTable> babyinfo = helper.babyDetail();
                if (babyinfo.size() == 0) {
                    Intent positveActivity = new Intent(HomeActivity.this, BabyHomeActivity.class);
                    overridePendingTransition(R.anim.push_infromright_anim, R.anim.push_out_to_left_anim);
                    startActivity(positveActivity);
                    finish();
                } else {
                    BabyNameAdapter.setBabysDialog(HomeActivity.this, " ");
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
