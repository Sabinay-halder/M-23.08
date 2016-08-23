package com.widevision.pregnantwoman.kankan.wheel.widget.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.kankan.wheel.widget.MyListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Created by Mercury-five on 11/12/15.
 */
public class PickerView {

    private Activity activity;
    private String[] strArr;
    private int selected_value;
    private Typeface fontLight, fontBold;
    private int largeFontSize = 22, smallFontSize = 18;
    private MyListener listener;
    final Dialog dialog;
    public PickerView(Activity activity, String[] list, int selectedIndex, MyListener listener) {
        this.activity = activity;
        this.strArr = list;
        this.selected_value = selectedIndex;
        this.listener = listener;
        fontLight = Typeface.createFromAsset(activity.getAssets(), "Oregon LDO Medium.ttf");
        fontBold = Typeface.createFromAsset(activity.getAssets(), "Oregon LDO Black.ttf");
        dialog = new Dialog(activity, R.style.DialogAnimation);
    }

    public void setPicker() {

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.picker_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        final WheelView country = (WheelView) dialog.findViewById(R.id.cityWheel);
        country.setVisibleItems(3);
        final CountryAdapterSecond adapter = new CountryAdapterSecond(activity, strArr);
        country.setViewAdapter(adapter);

        LinearLayout titleLayout = (LinearLayout) dialog.findViewById(R.id.pickerTitleLayout);
        ImageView doneButton = (ImageView) dialog.findViewById(R.id.pickerDone);
        ImageView closeButton = (ImageView) dialog.findViewById(R.id.pickerClose);
        final TextView textView = (TextView) dialog.findViewById(R.id.selectedTxt);
        titleLayout.setBackgroundColor(activity.getResources().getColor(R.color.heading));
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                String category = strArr[selected_value];
                listener.onSet(category);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        country.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selected_value = newValue;
                textView.setText(strArr[selected_value]);
                textView.setTextColor(Color.parseColor("#ffffff"));
                adapter.notifyDataChangedEvent();
            }
        });
        selected_value = 0;
        country.setCurrentItem(selected_value);
        textView.setText(strArr[selected_value]);
        textView.setTextColor(Color.parseColor("#ffffff"));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    /**
     * Adapter for picker
     */
    private class CountryAdapterSecond extends AbstractWheelTextAdapter {
        String[] countries;

        /**
         * Constructor
         */
        protected CountryAdapterSecond(Context context, String[] countries) {
            super(context, R.layout.row_wheel_layout, NO_RESOURCE);
            this.countries = countries;
            setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            TextView img = (TextView) view.findViewById(R.id.country_name);
            if (selected_value == index) {
                img.setTypeface(fontBold);
                img.setTextSize(TypedValue.COMPLEX_UNIT_SP, largeFontSize);
                img.setTextColor(Color.parseColor("#000023"));
            } else {
                img.setTypeface(fontLight);
                img.setTextSize(TypedValue.COMPLEX_UNIT_SP, smallFontSize);
                img.setTextColor(Color.parseColor("#A1242E76"));
            }
            img.setText(countries[index]);
            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }
}
