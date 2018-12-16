package com.example.galax.weatherapp.base.dialogs.events;

/**
 * Created by max_ermakov on 3/18/18.
 */

public class SetDialogCancelableEvent {
    public boolean cancelable;

    public SetDialogCancelableEvent(boolean cancelable) {
        this.cancelable = cancelable;
    }
}
