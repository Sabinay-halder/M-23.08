package com.widevision.pregnantwoman.baby;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.widevision.pregnantwoman.R;
import com.widevision.pregnantwoman.kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.OnWheelChangedListener;
import com.widevision.pregnantwoman.kankan.wheel.widget.view.WheelView;
import com.widevision.pregnantwoman.model.HideKeyFragment;
import com.widevision.pregnantwoman.util.Constant;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BabyHealthTips extends HideKeyFragment {


    @Bind(R.id.nameEdt)
    TextView nameET;
    @Bind(R.id.tipsEt)
    MaterialEditText tipsEt;
    @Bind(R.id.web)
    WebView mWebView;
    private int selected_value=0;
    private Typeface fontLight, fontBold;
    private List<String> category;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.baby_health_tips, container, false);
        ButterKnife.bind(this, view);
        setupUI(view);
        fontLight = Typeface.createFromAsset(getActivity().getAssets(), "Oregon LDO Medium.ttf");
        fontBold = Typeface.createFromAsset(getActivity().getAssets(), "Oregon LDO Black.ttf");
        /*Name Set*/

        nameET.setText(Constant.babyName);
        mWebView.loadUrl("file:///android_asset/HTML_BABY/Food-Tips.html");
        tipsEt.setText("Food-Tips");
        try {
            category = getListFromAssets();
            tipsEt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPicker(category);
                }
            });
            tipsEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        setPicker(category);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    private List<String> getListFromAssets() throws IOException {
        AssetManager assetManager = getActivity().getAssets();
        String[] files = assetManager.list("HTML_MOTHER");
        List<String> list = Arrays.asList(files);
        if (list.contains("element")) {
            list.remove("element");
        }
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).replace(".html", ""));
        }
        return list;
    }

    public void setPicker(final List<String> strArr) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogAnimation);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.picker_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        final WheelView country = (WheelView) dialog.findViewById(R.id.cityWheel);
        country.setVisibleItems(3);
        final CountryAdapterSecond adapter = new CountryAdapterSecond(getActivity(), strArr);
        country.setViewAdapter(adapter);

        LinearLayout titleLayout = (LinearLayout) dialog.findViewById(R.id.pickerTitleLayout);
        ImageView doneButton = (ImageView) dialog.findViewById(R.id.pickerDone);
        ImageView closeButton = (ImageView) dialog.findViewById(R.id.pickerClose);
        final TextView textView = (TextView) dialog.findViewById(R.id.selectedTxt);
        titleLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.heading));
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String category = strArr.get(selected_value);
                tipsEt.setText(category);
                mWebView.loadUrl("file:///android_asset/HTML_BABY/" + category + ".html");
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
                textView.setText(strArr.get(selected_value));
                textView.setTextColor(Color.parseColor("#ffffff"));
                adapter.notifyDataChangedEvent();
            }
        });

        country.setCurrentItem(selected_value);
        textView.setText(strArr.get(selected_value));
        textView.setTextColor(Color.parseColor("#ffffff"));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    /**
     * Adapter for picker
     */
    private class CountryAdapterSecond extends AbstractWheelTextAdapter {
        List<String> countries;

        /**
         * Constructor
         */
        protected CountryAdapterSecond(Context context, List<String> countries) {
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
                img.setTextColor(Color.parseColor("#000023"));
            } else {
                img.setTypeface(fontLight);
                img.setTextColor(Color.parseColor("#A1242E76"));
            }
            img.setText(countries.get(index));
            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries.get(index);
        }
    }
}
