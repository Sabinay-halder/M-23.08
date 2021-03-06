package com.widevision.pregnantwoman.baby;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.widevision.pregnantwoman.MainFragmentActivityBaby;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.database.BabyInfoTable;
import com.widevision.pregnantwoman.util.Constant;

import java.util.List;

/**
 * Created by mercury-two on 22/8/15.
 */

public class BabyNameAdapter extends BaseAdapter {

    Activity content;
    LayoutInflater inflat;
    List<BabyInfoTable> babynamebean;

    public BabyNameAdapter(Activity content, List<BabyInfoTable> babynamebean) {
        this.content = content;
        this.babynamebean = babynamebean;

        inflat = (LayoutInflater) content
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return babynamebean.size();
    }

    @Override
    public Object getItem(int position) {
        return babynamebean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = inflat.inflate(R.layout.baby_listdata, null);

        TextView name = (TextView) view.findViewById(R.id.txtbaby1);
        ImageView image = (ImageView) view.findViewById(R.id.imgbaby1);
        final BabyInfoTable be = babynamebean.get(position);

        name.setText(be.name + " " + be.lastName);
        image.setBackgroundResource(R.drawable.baby_icon_popup);

        return view;
    }


    //     /* Dialog Set */
    public static void setBabysDialog(final Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.baby_namelist_dialog);
        dialog.getWindow().setLayout(Constant.width - 20, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        intialised widget
        ListView babyList = (ListView) dialog.findViewById(R.id.lvbaby);
        LinearLayout addNew = (LinearLayout) dialog.findViewById(R.id.LLladdnew);
        ImageView cancleBttn = (ImageView) dialog.findViewById(R.id.babydialocancle);
        TextView txtaddnew = (TextView) dialog.findViewById(R.id.txtaddnew);

//        Fetch value
        ActiveAndroidDBHelper helper = ActiveAndroidDBHelper.getInstance();
        final List<BabyInfoTable> babyinfo = helper.babyDetail();

        if (!(babyinfo.size() < 4)) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Constant.height / 3);
            layoutParams.addRule(RelativeLayout.BELOW, R.id.title);
            babyList.setLayoutParams(layoutParams);
        }

//        set Adapter
        BabyNameAdapter babyadap = new BabyNameAdapter(activity, babyinfo);
        babyList.setAdapter(babyadap);

//          Item select
        babyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dialog.dismiss();
                Constant.type = 2;
                Intent in = new Intent(activity, MainFragmentActivityBaby.class);
                long id = babyinfo.get(position).getId();
                Constant.babyName = babyinfo.get(position).name + " " + babyinfo.get(position).lastName;
                Constant.babyId = id;
                String namena = babyinfo.get(position).name;
                String date = babyinfo.get(position).dobbaby;
                Constant.babyDOB = date;
                String gender = babyinfo.get(position).genderbaby;
                Constant.gender = gender;
                activity.startActivity(in);
                activity.finish();
            }
        });

        txtaddnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent positveActivity = new Intent(activity, BabyHomeActivity.class);
                activity.startActivity(positveActivity);
                activity.finish();
            }
        });

        cancleBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

