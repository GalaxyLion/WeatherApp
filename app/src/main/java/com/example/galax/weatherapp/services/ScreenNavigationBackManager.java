package com.example.galax.weatherapp.services;

import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;

import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.base.BaseActivity;
import com.example.galax.weatherapp.services.events.BackPressEvent;
import com.example.galax.weatherapp.services.events.TryExitActivityEvent;
import com.example.galax.weatherapp.services.events.TryNavigateBackEvent;
import com.squareup.otto.Subscribe;


import timber.log.Timber;

public class ScreenNavigationBackManager implements BackNavigator {

    private BaseActivity activity;
    private boolean couldNavigateBack = true;
    private boolean doubleBackToExitPressedOnce = false;
    private static final int TIME_OUT = 2000;
    private Handler handler;

    public ScreenNavigationBackManager(BaseActivity activity) {
        this.activity = activity;
        handler = new Handler(Looper.getMainLooper());
    }

    public boolean isCouldNavigateBack() {
        return couldNavigateBack;
    }

    public void setCouldNavigateBack(boolean couldNavigateBack) {
        this.couldNavigateBack = couldNavigateBack;
    }

    public void navigateBack(){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Timber.d("pop fragment from backstack");

            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
            String fragmentName = backEntry.getName();
            fragmentManager.popBackStackImmediate(fragmentName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            tryExitActivity();
        }
    }

    public void tryExitActivity() {
        activity.hideKeyboard();
        if (activity.isTaskRoot()) {
            if (doubleBackToExitPressedOnce){
                exit();
            }else {
                doubleBackToExitPressedOnce = true;
                Snackbar.make(activity.findViewById(R.id.content_frame), R.string.back_message, Snackbar.LENGTH_LONG)
                        .show();
                handler.postDelayed(() -> doubleBackToExitPressedOnce = false,TIME_OUT);
            }
        }else {
            exit();
        }
    }

    private void exit() {
        activity.finish();
        activity.freeMemory();
    }

    @Subscribe
    public void onEvent(BackPressEvent event){
        if (couldNavigateBack){
            navigateBack();
        }else {
            activity.getBus().post(new TryNavigateBackEvent());
        }
    }

    @Subscribe
    public void onEvent(TryExitActivityEvent event){
        tryExitActivity();
    }
}
