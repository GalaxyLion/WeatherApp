package com.example.galax.weatherapp.screen.settings;

import android.view.View;
import android.widget.TextView;

import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.base.App;
import com.jakewharton.rxbinding2.view.RxView;

import io.paperdb.Paper;
import io.reactivex.Observable;

public class SettingsView implements SettingsContract.SettingsView {
    private View root;
    private View unitBtn;
    private TextView titleTemp;
    private TextView subtitleTemp;
    private TextView unitTextTemp;
    private View aboutBtn;


    public SettingsView(View root) {
        this.root = root;
        initView();
    }

    private void initView() {
        unitBtn = root.findViewById(R.id.unit_temp);
        titleTemp = root.findViewById(R.id.title);
        subtitleTemp = root.findViewById(R.id.sub_title);
        aboutBtn = root.findViewById(R.id.about);
        unitTextTemp = root.findViewById(R.id.unit_text);

    }

    @Override
    public Observable<Object> temperatureUnitBtnAction() {
        return RxView.clicks(unitBtn);
    }

    @Override
    public Observable<Object> aboutBtnAction() {
        return RxView.clicks(aboutBtn);
    }


    @Override
    public void setTempUnit(String unit, String subUnit) {
        subtitleTemp.setText(unit);
        unitTextTemp.setText(subUnit);
    }

}
