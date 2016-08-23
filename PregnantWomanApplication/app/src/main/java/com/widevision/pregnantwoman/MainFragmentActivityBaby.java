package com.widevision.pregnantwoman;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.widevision.pregnantwoman.Bean.BabyHeightWeightBean;
import com.widevision.pregnantwoman.baby.AddNoteFragmentBaby;
import com.widevision.pregnantwoman.baby.BabyAppointmentRequest;
import com.widevision.pregnantwoman.baby.BabyFoodIntake;
import com.widevision.pregnantwoman.baby.BabyFoodIntakeChart;
import com.widevision.pregnantwoman.baby.BabyHealthTips;
import com.widevision.pregnantwoman.baby.BabyHeightWeight;
import com.widevision.pregnantwoman.baby.BabyHeightWeightChart;
import com.widevision.pregnantwoman.baby.BabyMainFragment;
import com.widevision.pregnantwoman.baby.BabyNotes;
import com.widevision.pregnantwoman.baby.BabyProfileUpdate;
import com.widevision.pregnantwoman.baby.BabySleepChart;
import com.widevision.pregnantwoman.baby.BabySleepRecord;
import com.widevision.pregnantwoman.baby.BabyVaccination;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.database.AppointmentRecordTable;
import com.widevision.pregnantwoman.database.BabyHeiWeiTable;
import com.widevision.pregnantwoman.database.BabyInfoTable;
import com.widevision.pregnantwoman.model.HideKeyActionBarActivity;
import com.widevision.pregnantwoman.mother.NotesListActivity;
import com.widevision.pregnantwoman.mother.WomanMainActivity;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.PreferenceConnector;
import com.widevision.pregnantwoman.util.ToolbarActionItemTarget;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by newtrainee on 7/8/15.
 */
public class MainFragmentActivityBaby extends HideKeyActionBarActivity implements OnMenuItemClickListener,
        OnMenuItemLongClickListener {
    public static FragmentManager fragmentManager;
    private DialogFragment mMenuDialogFragment;
    private ActiveAndroidDBHelper helper;
    public static String noteTitle = "";
    public static String noteDescription = "";
    Toolbar mToolbar;
    private ShowcaseView showcaseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        helper = ActiveAndroidDBHelper.getInstance();
        initToolbar();
        initMenuFragment();


        addFragment(new BabyMainFragment(), true, R.id.container);

        // addFragment(new WomanMainActivity(), true, R.id.container);


        fragmentManager.addOnBackStackChangedListener(getListener());
        setupUI(findViewById(R.id.main_top));
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    private List<MenuObject> getMenuObjects() {

        List<MenuObject> menuObjects = new ArrayList<>();
        MenuObject close = new MenuObject();
        close.setResource(R.drawable.menu_cancel);
        MenuObject send = new MenuObject("Home");
        send.setResource(R.drawable.menu_home);
        MenuObject like = new MenuObject("Alert");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.menu_alert);
        like.setBitmap(b);
        MenuObject addFr = new MenuObject("Reminder");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.menu_reminder));
        addFr.setDrawable(bd);
        MenuObject set = new MenuObject("Profile");
        BitmapDrawable sett = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.menu_setting));
        set.setDrawable(sett);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(addFr);
        menuObjects.add(set);

        return menuObjects;
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolBarTextView.setText("Baby Care");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.baby_heading));
    }

    protected void addFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        invalidateOptionsMenu();
        String backStackName = Constant.TAGFRAGMENT;
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStackName, 0);
        if (!fragmentPopped) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(containerId, fragment, backStackName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (addToBackStack)
                transaction.addToBackStack(backStackName);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                    if (showcaseView != null) {
                        showcaseView.hide();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = MainFragmentActivityBaby.fragmentManager.findFragmentByTag(Constant.TAGFRAGMENT);
        if (!(fragment instanceof AddNoteFragmentBaby)) {
            MainFragmentActivityBaby.noteDescription = "";
            MainFragmentActivityBaby.noteTitle = "";
        }
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else {
            goBack();
//            finish();
        }
    }


    public void goBack() {
      /* if (Constant.tagFragment == 1) {
        finish();
        startActivity(new Intent(MainFragmentActivity.this, MainFragmentActivity.class));
        Constant.tagFragment = 0;
        return;
    }*/
        if (fragmentManager.getBackStackEntryCount() != 1) {
            fragmentManager.popBackStack();
            Fragment fragment = MainFragmentActivityBaby.fragmentManager.findFragmentByTag(Constant.TAGFRAGMENT);
            if (fragment instanceof WomanMainActivity) {
                startActivity(new Intent(MainFragmentActivityBaby.this, HomeActivity.class));
                finish();
            }
        } else {
            startActivity(new Intent(MainFragmentActivityBaby.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position) {
            case 1:
                startActivity(new Intent(MainFragmentActivityBaby.this, HomeActivity.class));
                finish();
                break;
            case 2:
                try {
                    setAlertNotification();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    setNotification();
                } catch (Exception e) {
                    e.printStackTrace();
                    Constant.setAlert(MainFragmentActivityBaby.this, "Please add appointement first");
                }
                break;
            case 4:

                Intent get = getIntent();
                Bundle b = get.getExtras();

                long id = 0;
                String name = "", date = "", gender = "";
                float weigh = 0, heigh = 0, circu = 0;


                Fragment fragment = MainFragmentActivityBaby.fragmentManager.findFragmentByTag(Constant.TAGFRAGMENT);
                if (fragment instanceof BabyAppointmentRequest) {
                    BabyProfileUpdate myf = new BabyProfileUpdate();

                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.add(R.id.appointment_parent_second, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof BabyFoodIntake) {
                    BabyProfileUpdate myf = new BabyProfileUpdate();

                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.add(R.id.parent_foodIntake, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof BabyFoodIntakeChart) {
                    BabyProfileUpdate myf = new BabyProfileUpdate();

                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.add(R.id.chart_parent, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof BabyHealthTips) {
                    BabyProfileUpdate myf = new BabyProfileUpdate();

                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.add(R.id.baby_healthTips, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof BabyHeightWeight) {
                    BabyProfileUpdate myf = new BabyProfileUpdate();

                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.add(R.id.baby_heightWeight, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof BabyHeightWeightChart) {
                    BabyProfileUpdate myf = new BabyProfileUpdate();

                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.add(R.id.chart_parent_heightWeight, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof BabyMainFragment) {
                    BabyProfileUpdate myf = new BabyProfileUpdate();

                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.add(R.id.framebaby, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof BabyNotes) {
                    BabyProfileUpdate myf = new BabyProfileUpdate();
                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.add(R.id.baby_notes, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof BabySleepChart) {
                    BabyProfileUpdate myf = new BabyProfileUpdate();
                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.add(R.id.chart_parent_sleep, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof BabySleepRecord) {
                    BabyProfileUpdate myf = new BabyProfileUpdate();
                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.add(R.id.baby_sleeprecord, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof BabyVaccination) {
                    BabyProfileUpdate myf = new BabyProfileUpdate();

                    FragmentTransaction transaction = MainFragmentActivityBaby.fragmentManager.beginTransaction();
                    transaction.add(R.id.baby_vaccination, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {

    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {

                fragmentManager = getSupportFragmentManager();
                if (fragmentManager != null) {
                    if (Constant.tagForNoteFragment == 1) {
                        NotesListActivity currFrag = (NotesListActivity) fragmentManager.findFragmentById(R.id.frame);
                        if (currFrag != null) {
                            currFrag.onResume();
                        }
                    }
                }
            }
        };
        return result;
    }

    void setNotification() {
        final Dialog dialog = new Dialog(MainFragmentActivityBaby.this, R.style.DialogSlideAnimSecond);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ListView listView = (ListView) dialog.findViewById(R.id.popup_list);
        ImageView okBtn = (ImageView) dialog.findViewById(R.id.okBtn);
        TextView title = (TextView) dialog.findViewById(R.id.title);

        title.setText("Baby Reminder");
        List<AppointmentRecordTable> list = helper.viewAllAppointment();
        List<AppointmentRecordTable> newList = new ArrayList<>();
        for (AppointmentRecordTable item : list) {
            if (item.reminderFor.trim().equals("BABY")) {
                newList.add(item);
            }
        }
        if (newList != null && newList.size() != 0) {
            if (newList.size() > 2) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Constant.height / 3);
                listView.setLayoutParams(layoutParams);
            }
            PopUpAdapter adapter = new PopUpAdapter(MainFragmentActivityBaby.this, newList);
            listView.setAdapter(adapter);
            dialog.show();
        } else {
            Constant.setAlert(MainFragmentActivityBaby.this, "No appointment added.");
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    void setAlertNotification() {
        final Dialog dialog = new Dialog(MainFragmentActivityBaby.this, R.style.DialogSlideAnimSecond);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_baby);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView firstAlertText = (TextView) dialog.findViewById(R.id.firstAlertTxt);
        TextView secondAlertText = (TextView) dialog.findViewById(R.id.secondAlertTxt);
        TextView thirdAlertText = (TextView) dialog.findViewById(R.id.thirdAlertTxt);
        LinearLayout firstLayout = (LinearLayout) dialog.findViewById(R.id.firstAlertLayout);
        LinearLayout secondLayout = (LinearLayout) dialog.findViewById(R.id.secondAlertLayout);
        LinearLayout thirdLayout = (LinearLayout) dialog.findViewById(R.id.thirdAlertLayout);
        ImageView okBtn = (ImageView) dialog.findViewById(R.id.okBtn);

        StringBuilder alertStr = new StringBuilder();
        int tag = 0;
        int tagAlert1 = 0;
        int tagAlert2 = 0;
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ArrayList<BabyHeightWeightBean> babbyHeightWeightList;
        int mBabyHeight;
        float mBabyWeight;

     /*get all the record from health parameter table*/
        babbyHeightWeightList = helper.getBabyHeightWeightData();
        BabyInfoTable babyInfo = helper.viewBabyById(Constant.babyId);
        List<BabyHeiWeiTable> list = helper.babyHeightInfo(Constant.babyId);
        if (list.size() != 0) {
            String dob = babyInfo.dobbaby;
            String[] d = dob.split("-");
            String l = list.get(list.size() - 1).dateofmeasure;
            mBabyHeight = list.get(list.size() - 1).heightbaby;
            mBabyWeight = list.get(list.size() - 1).weightbaby;
            String[] last = l.split("-");
            LocalDate startDate = new LocalDate(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));          //Birth date
            LocalDate endDate = new LocalDate(Integer.parseInt(last[0]), Integer.parseInt(last[1]), Integer.parseInt(last[2]));                    //Today's date
            Period period = new Period(startDate, endDate, PeriodType.yearMonthDay());
            int months = period.getMonths();
            if (babbyHeightWeightList.size() != 0) {
                int numberTag = 0;
                if (Constant.gender.trim().equals("Boy")) {
                    for (int i = 0; i < babbyHeightWeightList.size(); i++) {
                        int baby_month = 0;
                        if (babbyHeightWeightList.get(i).getAge().trim().equals("Birth")) {
                            baby_month = 1;
                        } else {
                            String text = "";
                            if (babbyHeightWeightList.get(i).getAge().contains("months")) {
                                text = babbyHeightWeightList.get(i).getAge().replaceAll("months", "");
                                baby_month = Integer.parseInt(text.trim());
                            } else if (babbyHeightWeightList.get(i).getAge().contains("Years")) {
                                text = babbyHeightWeightList.get(i).getAge().replaceAll("Years", "");
                                int v = Integer.parseInt(text.trim());
                                if (v == 4) {
                                    baby_month = 48;
                                } else if (v == 5) {
                                    baby_month = 60;
                                }
                            }
                        }
                        if ((months < baby_month)) {
                            tagAlert1 = 0;
                            tagAlert2 = 0;
                            if (mBabyWeight > babbyHeightWeightList.get(i).getBoys_Weight()) {
                                tag = 1;
                                numberTag = numberTag + 1;
                                firstAlertText.setText(" Baby weight is greater than require according to graph.");
                                firstLayout.setVisibility(View.VISIBLE);
                            } else if (mBabyWeight < babbyHeightWeightList.get(i).getBoys_Weight()) {
                                numberTag = numberTag + 1;
                                tag = 1;
                                firstAlertText.setText(" Baby weight is less than require according to graph.");
                                firstLayout.setVisibility(View.VISIBLE);
                            } else if (mBabyWeight == babbyHeightWeightList.get(i).getBoys_Weight()) {
                                tag = 1;
                                numberTag = numberTag + 1;
                                firstAlertText.setText(" Baby weight is equal to required weight.");
                                firstLayout.setVisibility(View.VISIBLE);
                            } else {
                                tagAlert1 = 1;
                            }
                            if (mBabyHeight > babbyHeightWeightList.get(i).getBoys_Height()) {
                                tag = 1;
                                numberTag = numberTag + 1;
                                secondAlertText.setText(" Baby height is greater than required height.");
                                secondLayout.setVisibility(View.VISIBLE);
                            } else if (mBabyHeight < babbyHeightWeightList.get(i).getBoys_Height()) {
                                tag = 1;
                                numberTag = numberTag + 1;
                                secondAlertText.setText("Baby height is less than required height.");
                                secondLayout.setVisibility(View.VISIBLE);
                            } else if (mBabyHeight == babbyHeightWeightList.get(i).getBoys_Height()) {
                                tag = 1;
                                numberTag = numberTag + 1;
                                secondAlertText.setText("Baby height is equal to required height.");
                                secondLayout.setVisibility(View.VISIBLE);
                            } else {
                                tagAlert2 = 1;
                            }
                            break;
                        } else {
                            tagAlert1 = 1;
                            tagAlert2 = 1;
                        }
                    }
                } else if (Constant.gender.trim().equals("Girl")) {
                    for (int i = 0; i < babbyHeightWeightList.size(); i++) {
                        int baby_month = 0;
                        if (babbyHeightWeightList.get(i).getAge().trim().equals("Birth")) {
                            baby_month = 1;
                        } else {
                            String text = "";
                            if (babbyHeightWeightList.get(i).getAge().contains("months")) {
                                text = babbyHeightWeightList.get(i).getAge().replaceAll("months", "");
                                baby_month = Integer.parseInt(text.trim());
                            } else if (babbyHeightWeightList.get(i).getAge().contains("Years")) {
                                text = babbyHeightWeightList.get(i).getAge().replaceAll("Years", "");
                                int v = Integer.parseInt(text.trim());
                                if (v == 4) {
                                    baby_month = 48;
                                } else if (v == 5) {
                                    baby_month = 60;
                                }
                            }
                        }
                        if ((months < baby_month)) {
                            tagAlert1 = 0;
                            tagAlert2 = 0;
                            if (mBabyWeight > babbyHeightWeightList.get(i).getGirls_Weight()) {
                                tag = 1;
                                numberTag = numberTag + 1;
                                firstAlertText.setText(" Baby weight is greater than required weight.");
                                firstLayout.setVisibility(View.VISIBLE);
                            } else if (mBabyWeight < babbyHeightWeightList.get(i).getGirls_Weight()) {
                                tag = 1;
                                numberTag = numberTag + 1;
                                firstAlertText.setText("Baby weight is less than required weight.");
                                firstLayout.setVisibility(View.VISIBLE);
                            } else if (mBabyWeight == babbyHeightWeightList.get(i).getGirls_Weight()) {
                                tag = 1;
                                numberTag = numberTag + 1;
                                firstAlertText.setText("Baby weight is equal to required weight.");
                                firstLayout.setVisibility(View.VISIBLE);
                            } else {
                                tagAlert1 = 1;
                            }
                            if (mBabyHeight > babbyHeightWeightList.get(i).getGirls_Height()) {
                                tag = 1;
                                numberTag = numberTag + 1;
                                secondAlertText.setText("Baby height is greater than required height.");
                                secondLayout.setVisibility(View.VISIBLE);
                            } else if (mBabyHeight < babbyHeightWeightList.get(i).getGirls_Height()) {
                                tag = 1;
                                numberTag = numberTag + 1;
                                secondAlertText.setText("Baby height is less than required height.");
                                secondLayout.setVisibility(View.VISIBLE);
                            } else if (mBabyHeight == babbyHeightWeightList.get(i).getGirls_Height()) {
                                tag = 1;
                                numberTag = numberTag + 1;
                                secondAlertText.setText("Baby height is equal to required weight.");
                                secondLayout.setVisibility(View.VISIBLE);
                            } else {
                                tagAlert2 = 1;
                            }
                            break;
                        } else {
                            tagAlert1 = 1;
                            tagAlert2 = 1;
                        }
                    }
                }
            } else {
                tagAlert1 = 1;
                tagAlert2 = 1;
            }
        } else {
            tagAlert1 = 1;
            tagAlert2 = 1;
        }
        if (tag == 1) {
            dialog.show();
        }
        if (tagAlert1 == 1 && tagAlert2 == 1) {
            Constant.setAlert(MainFragmentActivityBaby.this, "Nothing to show.");
        }
    }

    class PopUpAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Context context;
        private List<AppointmentRecordTable> list;
        private ViewHolder holder;

        PopUpAdapter(Context context, List<AppointmentRecordTable> list) {
            this.context = context;
            this.list = list;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new ViewHolder();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = inflater.inflate(R.layout.popup_list_row, viewGroup, false);
            holder.numberTxt = (TextView) view.findViewById(R.id.number);
            holder.messageTxt = (TextView) view.findViewById(R.id.msg);
            int no = i + 1;
            holder.numberTxt.setText("(" + no + ")");
            String msg = "Your appointment is scheduled on " + list.get(i).date + " at " + list.get(i).time + " with " + list.get(i).doctorName;
            holder.messageTxt.setText(msg);

            return view;
        }

        class ViewHolder {
            TextView messageTxt, numberTxt;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PreferenceConnector.readString(MainFragmentActivityBaby.this, PreferenceConnector.TIPS_BABY, "No").equals("Yes") && PreferenceConnector.readString(MainFragmentActivityBaby.this, PreferenceConnector.MENU_BABY, "No").equals("No")) {
            showcaseView = new ShowcaseView.Builder(this)
                    .withHoloShowcase()
                    .setTarget(new ToolbarActionItemTarget(mToolbar, R.id.context_menu))
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setContentText(getResources().getString(R.string.menu_btn))
                    .replaceEndButton(R.layout.ok_button)
                    .build();
            showcaseView.show();
            PreferenceConnector.writeString(MainFragmentActivityBaby.this, PreferenceConnector.MENU_BABY, "Yes");
        }
    }
}