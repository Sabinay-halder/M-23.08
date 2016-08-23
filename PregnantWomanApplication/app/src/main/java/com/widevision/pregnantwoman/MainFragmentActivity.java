package com.widevision.pregnantwoman;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.database.AppointmentRecordTable;
import com.widevision.pregnantwoman.model.HideKeyActionBarActivity;
import com.widevision.pregnantwoman.mother.AddNoteFragment;
import com.widevision.pregnantwoman.mother.AppointmentListActivity;
import com.widevision.pregnantwoman.mother.AppointmentRequestActivity;
import com.widevision.pregnantwoman.mother.BloodPresureChart;
import com.widevision.pregnantwoman.mother.EditMotherProfileActivity;
import com.widevision.pregnantwoman.mother.HelthParameterActivityWomen;
import com.widevision.pregnantwoman.mother.NotesListActivity;
import com.widevision.pregnantwoman.mother.TipsFragment;
import com.widevision.pregnantwoman.mother.WeightChart;
import com.widevision.pregnantwoman.mother.WomanMainActivity;
import com.widevision.pregnantwoman.util.Constant;
import com.widevision.pregnantwoman.util.ObjectSerializer;
import com.widevision.pregnantwoman.util.PreferenceConnector;
import com.widevision.pregnantwoman.util.ToolbarActionItemTarget;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;


public class MainFragmentActivity extends HideKeyActionBarActivity implements OnMenuItemClickListener, OnMenuItemLongClickListener {
    public static FragmentManager fragmentManager;
    private DialogFragment mMenuDialogFragment;
    private ActiveAndroidDBHelper helper;
    private Toolbar mToolbar;
    private ShowcaseView showcaseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        helper = ActiveAndroidDBHelper.getInstance();
        initToolbar();
        initMenuFragment();
        addFragment(new WomanMainActivity(), true, R.id.container);
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
        BitmapDrawable bd = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.menu_reminder));
        addFr.setDrawable(bd);
        MenuObject set = new MenuObject("Profile");
        BitmapDrawable sett = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.menu_setting));
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
        mToolbar.setBackgroundColor(getResources().getColor(R.color.title_color));
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolBarTextView.setText("Mother Care");
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
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else {
            goBack();
        }
    }


    public void goBack() {

        if (fragmentManager.getBackStackEntryCount() != 1) {
            fragmentManager.popBackStack();
            Fragment fragment = MainFragmentActivity.fragmentManager.findFragmentByTag(Constant.TAGFRAGMENT);
            if (fragment instanceof WomanMainActivity) {
                startActivity(new Intent(MainFragmentActivity.this, HomeActivity.class));
                overridePendingTransition(R.anim.push_infromright_anim, R.anim.push_out_to_left_anim);
                finish();
            }
        } else {
            startActivity(new Intent(MainFragmentActivity.this, HomeActivity.class));
            overridePendingTransition(R.anim.push_infromright_anim, R.anim.push_out_to_left_anim);
            finish();
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        switch (position) {
            case 1:
                startActivity(new Intent(MainFragmentActivity.this, HomeActivity.class));
                overridePendingTransition(R.anim.push_infromright_anim, R.anim.push_out_to_left_anim);
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
                    Constant.setAlert(MainFragmentActivity.this, "Please add appointement first");
                }
                break;
            case 4:
                Fragment fragment = MainFragmentActivity.fragmentManager.findFragmentByTag(Constant.TAGFRAGMENT);
                if (fragment instanceof NotesListActivity) {
                    EditMotherProfileActivity myf = new EditMotherProfileActivity();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.add(R.id.parent_noteList, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof HelthParameterActivityWomen) {
                    EditMotherProfileActivity myf = new EditMotherProfileActivity();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.add(R.id.helthparameter_parent_second, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof WomanMainActivity) {
                    EditMotherProfileActivity myf = new EditMotherProfileActivity();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.add(R.id.frame, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof AddNoteFragment) {
                    EditMotherProfileActivity myf = new EditMotherProfileActivity();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.add(R.id.parentsecond_addNote, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof AppointmentRequestActivity) {
                    EditMotherProfileActivity myf = new EditMotherProfileActivity();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.add(R.id.appointment_parent_second, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof BloodPresureChart) {
                    EditMotherProfileActivity myf = new EditMotherProfileActivity();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.add(R.id.chart_parent, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof TipsFragment) {
                    EditMotherProfileActivity myf = new EditMotherProfileActivity();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.add(R.id.tips_parent_second, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else if (fragment instanceof WeightChart) {
                    EditMotherProfileActivity myf = new EditMotherProfileActivity();
                    FragmentTransaction transaction = MainFragmentActivity.fragmentManager.beginTransaction();
                    transaction.add(R.id.chart_parent, myf, Constant.TAGFRAGMENT);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
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
                    } else if (Constant.tagForNoteFragment == 2) {
                        AppointmentListActivity AppointmentListActivity = (AppointmentListActivity) fragmentManager.findFragmentById(R.id.frame);
                        if (AppointmentListActivity != null) {
                            AppointmentListActivity.onResume();
                        }
                    }
                }
            }
        };
        return result;
    }

    void setNotification() {
        final Dialog dialog = new Dialog(MainFragmentActivity.this, R.style.DialogSlideAnimSecond);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ListView listView = (ListView) dialog.findViewById(R.id.popup_list);
        ImageView okBtn = (ImageView) dialog.findViewById(R.id.okBtn);
        TextView title = (TextView) dialog.findViewById(R.id.title);

        title.setText("Mother Reminder");
        List<AppointmentRecordTable> list = helper.viewAllAppointment();
        List<AppointmentRecordTable> newList = new ArrayList<>();
        for (AppointmentRecordTable item : list) {
            if (item.reminderFor.trim().equals("MOTHER")) {
                newList.add(item);
            }
        }
        if (newList != null && newList.size() != 0) {
            if (newList.size() > 2) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Constant.height / 3);
                listView.setLayoutParams(layoutParams);
            }
            PopUpAdapter adapter = new PopUpAdapter(MainFragmentActivity.this, newList);
            listView.setAdapter(adapter);
            dialog.show();
        } else {
            Constant.setAlert(MainFragmentActivity.this, "No appintment added.");
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    void setAlertNotification() {
        final Dialog dialog = new Dialog(MainFragmentActivity.this, R.style.DialogSlideAnimSecond);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_mother);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView firstAlertText = (TextView) dialog.findViewById(R.id.firstAlertTxt);
        TextView secondAlertText = (TextView) dialog.findViewById(R.id.secondAlertTxt);
        TextView secondAlertTitle = (TextView) dialog.findViewById(R.id.secondTitle);
        TextView thirdAlertText = (TextView) dialog.findViewById(R.id.thirdAlertTxt);
        LinearLayout firstLayout = (LinearLayout) dialog.findViewById(R.id.firstAlertLayout);
        LinearLayout secondLayout = (LinearLayout) dialog.findViewById(R.id.secondAlertLayout);
        LinearLayout thirdLayout = (LinearLayout) dialog.findViewById(R.id.thirdAlertLayout);
        ImageView okBtn = (ImageView) dialog.findViewById(R.id.okBtn);

        int tag = 0;
        secondAlertTitle.setText("Blood Pressure :-");

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        String column_min = "";
        String column_max = "";
        float mStartWeight = 0;
        ArrayList<Float> minList = new ArrayList<>();
        ArrayList<Float> maxList = new ArrayList<>();
        ArrayList<Float> listFirst;
        //      load tasks from preference
        SharedPreferences prefs = getSharedPreferences("list_file", Context.MODE_PRIVATE);
        try {
            listFirst = (ArrayList<Float>) ObjectSerializer.deserialize(prefs.getString("weight_list", ObjectSerializer.serialize(new ArrayList<Float>())));

            mStartWeight = listFirst.get(0);
            if (PreferenceConnector.readString(MainFragmentActivity.this, PreferenceConnector.TWINS, "No").equals("Yes")) {
                if (mStartWeight < 69) {
                    column_min = "twins_min_68";
                    column_max = "twins_max_68";
                } else if (mStartWeight < 82) {
                    column_min = "twins_min_81";
                    column_max = "twins_max_81";
                } else if (mStartWeight < 151) {
                    column_min = "twins_min_150";
                    column_max = "twins_max_150";
                }
            } else {
                if (mStartWeight < 51) {
                    column_min = "min_51";
                    column_max = "max_51";
                } else if (mStartWeight < 69) {
                    column_min = "min_68";
                    column_max = "max_68";
                } else if (mStartWeight < 82) {
                    column_min = "min_69";
                    column_max = "max_81";
                } else if (mStartWeight < 151) {
                    column_min = "min_150";
                    column_max = "max_150";
                }
            }
            int week = PreferenceConnector.readInteger(MainFragmentActivity.this, PreferenceConnector.WEEK, 0);
            Cursor cursor = helper.viewAll();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Float s = cursor.getFloat(cursor.getColumnIndex(column_min));
                    Float s1 = cursor.getFloat(cursor.getColumnIndex(column_max));
                    minList.add(s + mStartWeight);
                    maxList.add(s1 + mStartWeight);
                } while (cursor.moveToNext());
            }
            if (listFirst == null || listFirst.size() == 0) {
                tag = 2;
            } else if (listFirst.get(week) > maxList.get(week)) {
                float a = listFirst.get(week) - maxList.get(week);
                firstLayout.setVisibility(View.VISIBLE);
                firstAlertText.setText("Your weight is " + a + " more compare to Chart. ");
                tag = 1;
            } else if (listFirst.get(week) < minList.get(week)) {
                float a = maxList.get(week) - listFirst.get(week);
                firstAlertText.setText("Your weight is " + a + " less compare to Chart.");
                firstLayout.setVisibility(View.VISIBLE);
                tag = 1;
            } else {
                firstLayout.setVisibility(View.VISIBLE);
                firstAlertText.setText("Your weight is ideal " + listFirst.get(week));
                tag = 1;
            }

            //for blood presure
            // load tasks from preference
            ArrayList<Integer> systolic;
            ArrayList<Integer> diastolic;

            systolic = (ArrayList<Integer>) ObjectSerializer.deserialize(prefs.getString("systolic_list", ObjectSerializer.serialize(new ArrayList<Integer>())));
            diastolic = (ArrayList<Integer>) ObjectSerializer.deserialize(prefs.getString("diastolic_list", ObjectSerializer.serialize(new ArrayList<Integer>())));
            if (systolic == null || systolic.size() == 0 || diastolic == null || diastolic.size() == 0) {
                tag = 2;
            } else if (systolic.get(week) < 90 || diastolic.get(week) < 60) {
                secondLayout.setVisibility(View.VISIBLE);
                secondAlertText.setText("Your blood pressure is low.");
                tag = 1;
            } else if (systolic.get(week) < 120 || diastolic.get(week) < 80) {
                secondLayout.setVisibility(View.VISIBLE);
                secondAlertText.setText("your blood pressure is ideal.");
                tag = 1;
            } else if (systolic.get(week) < 140 || diastolic.get(week) < 90) {
                secondLayout.setVisibility(View.VISIBLE);
                secondAlertText.setText("Your blood pressure is pre-high.");
                tag = 1;
            } else if (systolic.get(week) > 140 || diastolic.get(week) > 90) {
                secondLayout.setVisibility(View.VISIBLE);
                secondAlertText.setText("Your blood pressure is high.");
                tag = 1;
            }
            if (tag == 1) {
                dialog.show();
            } else if (tag == 2) {
                Constant.setAlert(MainFragmentActivity.this, "Enter health parameter first.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Constant.setAlert(MainFragmentActivity.this, "Nothing to show.");

        }
//        dialog.show();
    }

    class PopUpAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        private List<AppointmentRecordTable> list;
        private ViewHolder holder;

        PopUpAdapter(Context context, List<AppointmentRecordTable> list) {

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
            String msg = "Your appointment is scheduled on " + list.get(i).date + " at " + list.get(i).time + " with doctor '" + list.get(i).doctorName + "'";
            holder.messageTxt.setText(msg);

            return view;
        }

        class ViewHolder {
            TextView numberTxt, messageTxt;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PreferenceConnector.readString(MainFragmentActivity.this, PreferenceConnector.TIPS_MOTHER, "No").equals("Yes") && PreferenceConnector.readString(MainFragmentActivity.this, PreferenceConnector.MENU_MOTHER, "No").equals("No")) {
            showcaseView = new ShowcaseView.Builder(this)
                    .withHoloShowcase()
                    .setTarget(new ToolbarActionItemTarget(mToolbar, R.id.context_menu))
                    .setStyle(R.style.CustomShowcaseTheme)
                    .setContentText(getResources().getString(R.string.menu_btn))
                    .replaceEndButton(R.layout.ok_button)
                    .build();
            PreferenceConnector.writeString(MainFragmentActivity.this, PreferenceConnector.MENU_MOTHER, "Yes");
            showcaseView.show();
        }
    }
}
