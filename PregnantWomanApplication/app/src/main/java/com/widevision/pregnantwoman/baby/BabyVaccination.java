package com.widevision.pregnantwoman.baby;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.widevision.pregnantwoman.Bean.VaccineBean;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.database.ActiveAndroidDBHelper;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BabyVaccination extends HideKeyFragment {


    @Bind(R.id.babyName)
    TextView nameET;
    @Bind(R.id.vaccinationList)
    ListView mVaccinationListView;

    private ActiveAndroidDBHelper helper;
    private ArrayList<VaccineBean> mVaccineList;
    private ArrayList<VaccineBean> mVaccineStatusList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.baby_vaccination, container, false);
        ButterKnife.bind(this, view);
        helper = ActiveAndroidDBHelper.getInstance();
        mVaccineList = helper.getVaccineList();
        mVaccineStatusList = helper.getVaccineStatusList("" + Constant.babyId);
        nameET.setText(Constant.babyName);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int i = 0; i < mVaccineList.size(); i++) {
            mVaccineStatusList.get(i).get_status();
            VaccineBean bean = new VaccineBean();
            bean.set_age(mVaccineList.get(i).get_age());
            bean.set_vaccineName(mVaccineList.get(i).get_vaccineName());
            bean.set_id(mVaccineList.get(i).get_id());
            bean.set_status(mVaccineStatusList.get(i).get_status());
            mVaccineList.set(i, bean);
        }

        VaccinationAdapter adapter = new VaccinationAdapter();
        mVaccinationListView.setAdapter(adapter);
    }


    class VaccinationAdapter extends BaseAdapter {

        private ViewHolder holder;
        private LayoutInflater inflater;

        public VaccinationAdapter() {
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mVaccineList.size();
        }

        @Override
        public Object getItem(int position) {
            return mVaccineList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.vaccination_list_row, parent, false);
                holder.vaccineNameTxt = (TextView) convertView.findViewById(R.id.vaccinationTxt);
                holder.dateTxt = (TextView) convertView.findViewById(R.id.dateTxt);
                holder.doneImg = (ImageView) convertView.findViewById(R.id.doneImg);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final VaccineBean item = mVaccineList.get(position);
            holder.vaccineNameTxt.setText(item.get_vaccineName());
            holder.dateTxt.setText(item.get_age());
            if (item.get_status().trim().equals("TRUE")) {
                holder.doneImg.setImageResource(R.drawable.done);
            } else {
                holder.doneImg.setImageResource(R.drawable.not_done);
            }
            holder.doneImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVaccineList.get(position).set_status("TRUE");
                    notifyDataSetChanged();
                    helper.updateBabayvaccineStatus("" + Constant.babyId, "" + mVaccineList.get(position).get_id(), "TRUE");
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView vaccineNameTxt, dateTxt;
            ImageView doneImg;
        }

    }
}
