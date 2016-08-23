package com.widevision.pregnantwoman.mother;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;

import com.widevision.pregnantwoman.Bean.VaccineBean;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.database.AppointmentRecordTable;
import com.widevision.pregnantwoman.database.BabyInfoTable;
import com.widevision.pregnantwoman.util.Constant;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ReminderService extends Service {

    private Timer t;
    private Timer t1;
    private ActiveAndroidDBHelper helper;
    static NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        t = new Timer();
        t1 = new Timer();
        helper = ActiveAndroidDBHelper.getInstance();
        StartTimer();
        return START_STICKY;
    }

    public void StartTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                test();
            }
        };
        t.scheduleAtFixedRate(task, 0, Constant.serviceDelayForReminder);


        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                vaccineReminder();
            }
        };
        t1.scheduleAtFixedRate(task2, 0, Constant.serviceDelayFORVaccine);
    }


    void vaccineReminder() {
        try {
            helper = ActiveAndroidDBHelper.getInstance();
            List<BabyInfoTable> list = helper.babyDetail();
            for (BabyInfoTable item : list) {
                ArrayList<VaccineBean> vaccineList = helper.getVaccineStatusList("" + item.getId());
                for (VaccineBean vaccineBean : vaccineList) {
                    DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.datePattern);
                    DateTime date = formatter.parseDateTime(vaccineBean.getVaccineDate());
                    if (date.isEqualNow()) {
                        startNotification("Baby vaccine reminder.", "" + item.getId(), "" + vaccineBean.get_vaccineId());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void test() {
        List<AppointmentRecordTable> list = helper.viewAllAppointment();
        if (list != null && list.size() != 0) {
            for (AppointmentRecordTable item : list) {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(Constant.dateTimePattern);
                DateTime dt = formatter.parseDateTime(item.date + " " + item.time);
                DateTime current = formatter.parseDateTime(Constant.getCurrentTime());
                if (!dt.isBeforeNow()) {
                    if (dt.minusDays(1).equals(current)) {
                        //show notification
                        Notify("Appointment Reminder", "you have your appointment with " + item.doctorName + " tomorrow at " + item.time, "Doctor Appointment Notification");
                    } else {
                        DateTime dateTime = formatter.parseDateTime(item.date + " " + item.time);
                        DateTime currentDateTime = formatter.parseDateTime(Constant.getCurrentTime());
                        DateTime dateTime1 = dateTime.minusHours(2);
                        if (currentDateTime.equals(dateTime1)) {
                            Notify("Appointment Reminder", "you have your appointment with " + item.doctorName + " today at " + item.time, "Doctor Appointment Notification");
                        }
                    }
                } else {
                    helper.deleteAppointment(item.getId());
                }
            }
        }

    }

    private void Notify(String notificationTitle, String notificationMessage, String title) {

        Notification notification;

        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.reminder_notification_layout);
        notificationView.setTextViewText(R.id.msg, notificationMessage);
        notificationView.setTextViewText(R.id.noti_title, notificationTitle);
        notificationView.setViewVisibility(R.id.btnLayout, View.GONE);
        //the intent that is started when the notification is clicked (works)
        Notification.Builder mNotifyBuilder = new Notification.Builder(this);
        notification = mNotifyBuilder.setSmallIcon(R.drawable.ic_launcher).build();
        //the intent that is started when the notification is clicked (works)
        Intent notificationIntent = new Intent(this, switchButtonListener.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.bigContentView = notificationView;
        notification.contentIntent = pendingNotificationIntent;
        notification.flags = Notification.FLAG_AUTO_CANCEL;

// now show notification..
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(2, notification);
    }

    private void startNotification(String _msg, String _babyId, String _vaccineId) {

        Notification notification;

        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.reminder_notification_layout);
        notificationView.setTextViewText(R.id.msg, _msg);
        //the intent that is started when the notification is clicked (works)
        Notification.Builder mNotifyBuilder = new Notification.Builder(this);
        notification = mNotifyBuilder.setSmallIcon(R.drawable.ic_launcher).build();
        //the intent that is started when the notification is clicked (works)
        Intent notificationIntent = new Intent(this, switchButtonListener.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.bigContentView = notificationView;
        notification.contentIntent = pendingNotificationIntent;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
//this is the intent that is supposed to be called when the
        //button is clicked
        Intent switchIntent = new Intent(this, switchButtonListener.class);
        switchIntent.putExtra("action", 1);
        switchIntent.putExtra("baby_id", _babyId);
        switchIntent.putExtra("vaccineId", _vaccineId);
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0,
                switchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.yesBtn, pendingSwitchIntent);

        Intent switchIntent2 = new Intent(this, switchButtonListener.class);
        switchIntent2.putExtra("action", 2);
        switchIntent2.putExtra("baby_id", _babyId);
        switchIntent2.putExtra("vaccineId", _vaccineId);
        PendingIntent pendingSwitchIntent2 = PendingIntent.getBroadcast(this, 0, switchIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationView.setOnClickPendingIntent(R.id.noBtn, pendingSwitchIntent2);

// now show notification..
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    public static class switchButtonListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            notificationManager.cancelAll();
            if (bundle != null) {
                int action = bundle.getInt("action");
                String vaccineId = bundle.getString("vaccineId");
                String baby_id = bundle.getString("baby_id");

                ActiveAndroidDBHelper helper = ActiveAndroidDBHelper.getInstance();
                switch (action) {

                    case 1:

                        helper.updateBabayvaccineStatus(baby_id, vaccineId, "TRUE");
                        break;
                    case 2:

                        helper.updateBabayvaccineStatus(baby_id, vaccineId, "FALSE");

                        break;
                    default:
                        break;
                }
            }
        }
    }
}