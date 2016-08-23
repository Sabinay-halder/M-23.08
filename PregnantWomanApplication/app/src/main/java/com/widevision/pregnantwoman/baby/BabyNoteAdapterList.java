/*
package com.widevision.pregnantwoman.babby;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.widevision.pregnantwoman.Bean.BabyDBBean;
import com.widevision.pregnantwoman.R;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by mercury-two on 7/9/15.
 *//*

public class BabyNoteAdapterList {


    */
/*Adapter set For FoodTable*//*

    public static class FoodAdapter extends BaseAdapter {

        Activity content;
        LayoutInflater inflat;
        List<BabyDBBean> babyfoodlist = new ArrayList<BabyDBBean>();

        public FoodAdapter(Activity content , List<BabyDBBean> babyfoodlist) {
            this.content = content;
            this.babyfoodlist = babyfoodlist;

            inflat = (LayoutInflater) content
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return babyfoodlist.size();
        }

        @Override
        public Object getItem(int position) {
            return babyfoodlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View viewfood, ViewGroup viewGroup) {

            viewfood = inflat.inflate(R.layout.baby_note_list, null);

            TextView categoryfood = (TextView) viewfood.findViewById(R.id.txtbaby1);
            TextView datefood = (TextView) viewfood.findViewById(R.id.txtbaby2);
            TextView describfood = (TextView) viewfood.findViewById(R.id.txtbaby3);
            BabyDBBean be = babyfoodlist.get(position);

            categoryfood.setText(be.getCategoryfood());
            datefood.setText(be.getDatefood());
            describfood.setText(be.getNotetitlefood());

            return viewfood;
        }
    }

    */
/*Adapter set For Height-Weight*//*

    public static class HeiWeiAdapter extends BaseAdapter {

        Activity content;
        LayoutInflater inflat;
        List<BabyDBBean> babyheiweilist = new ArrayList<BabyDBBean>();

        public HeiWeiAdapter(Activity content , List<BabyDBBean> babyheiweilist ) {
            this.content = content;
            this.babyheiweilist = babyheiweilist;

            inflat = (LayoutInflater) content
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return babyheiweilist.size();
        }

        @Override
        public Object getItem(int position) {
            return babyheiweilist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View viewHei, ViewGroup viewGroup) {

            viewHei = inflat.inflate(R.layout.baby_note_list, null);

            TextView categoryhei = (TextView) viewHei.findViewById(R.id.txtbaby1);
            TextView datehei = (TextView) viewHei.findViewById(R.id.txtbaby2);
            TextView describhei = (TextView) viewHei.findViewById(R.id.txtbaby3);
            BabyDBBean be = babyheiweilist.get(position);

            categoryhei.setText("HeiWei");
            datehei.setText(be.getDateheiwei());
            describhei.setText(be.getNotetitleheiwei());
            return viewHei;
        }
    }

    */
/*Adapter set For SleepTable*//*

    public static class SleepAdapter extends BaseAdapter {

        Activity content;
        LayoutInflater inflat;
        List<BabyDBBean> babySleeplist = new ArrayList<BabyDBBean>();

        public SleepAdapter(Activity content , List<BabyDBBean> babySleeplist) {
            this.content = content;
            this.babySleeplist = babySleeplist;

            inflat = (LayoutInflater) content
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return babySleeplist.size();
        }

        @Override
        public Object getItem(int position) {
            return babySleeplist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View viewsleep, ViewGroup viewGroup) {

            viewsleep = inflat.inflate(R.layout.baby_note_list, null);

            TextView categorySleep = (TextView) viewsleep.findViewById(R.id.txtbaby1);
            TextView dateSleep = (TextView) viewsleep.findViewById(R.id.txtbaby2);
            TextView describSleep = (TextView) viewsleep.findViewById(R.id.txtbaby3);
            BabyDBBean be = babySleeplist.get(position);

            categorySleep.setText("Sleep");
            dateSleep.setText(be.getDatesleep());
            describSleep.setText(be.getNotedetailsleep());

            return viewsleep;
        }
    }



}
*/
