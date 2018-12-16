package com.example.galax.weatherapp.base.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Simple wrapper with logging capabilities.
 * Intended to help to localize rare but regular crashes during dismissal.
 */
public class BaseDialog extends android.app.Dialog {

    private final String TAG = getClass().getSimpleName();

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG , " onCreate()" + (savedInstanceState != null ? " recreating" : ""));

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        Log.d(TAG , " onStart()");

        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG , " onStop()");

        super.onStop();
    }

    @Override
    public void onAttachedToWindow() {
        Log.d(TAG , " onAttachedToWindow()");

        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        Log.d(TAG , " onDetachedFromWindow()");

        super.onDetachedFromWindow();
    }

    @Override
    public void dismiss() {
        Log.d(TAG , " dismiss()");
        super.dismiss();
    }
}
