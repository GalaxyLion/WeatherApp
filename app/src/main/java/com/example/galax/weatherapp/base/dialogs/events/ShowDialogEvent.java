package com.example.galax.weatherapp.base.dialogs.events;

import android.view.View;

/**
 * Created by bohdan on 23.09.16.
 */
public class ShowDialogEvent {
    public View view;

    public ShowDialogEvent(View view) {
        this.view = view;
    }
}
