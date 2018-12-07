package com.example.galax.weatherapp.base;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


import com.example.galax.weatherapp.services.BackNavigator;
import com.example.galax.weatherapp.services.Navigator;
import com.example.galax.weatherapp.services.ScreenNavigationBackManager;
import com.example.galax.weatherapp.services.ScreenNavigationManager;
import com.example.galax.weatherapp.services.events.BackPressEvent;
import com.squareup.otto.Bus;

import com.tbruyelle.rxpermissions2.RxPermissions;

import net.danlew.android.joda.JodaTimeAndroid;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;


public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    private Bus bus;
    private CompositeDisposable subscriptions;
    //private DialogShower dialogShower;
    private CompositeDisposable imageSubs;
    private Handler handler;
    static final int REQUEST_TAKE_PHOTO = 92;
    static final int REQUEST_LOAD_PHOTO = 93;
    private String photoPath = "";
    final RxPermissions rxPermissions = new RxPermissions(this);
    private Navigator navigator;
    private BackNavigator navigationBackManager;

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, " onStart()");
        if (subscriptions == null) {
            subscriptions = new CompositeDisposable();
        }
        imageSubs = new CompositeDisposable();
    }


    @Override
    protected void onStop() {
        Log.i(TAG, " onStop()");
        if (subscriptions!=null && !subscriptions.isDisposed()){
            subscriptions.dispose();
            subscriptions.clear();
            subscriptions = null;
        }
        if (imageSubs!=null && !imageSubs.isDisposed()){
            imageSubs.dispose();
        }
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, " onCreate()" + (savedInstanceState != null ? " recreating" : ""));
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
        bus = new Bus();
        //dialogShower = new DialogShower(this);
        navigator = new ScreenNavigationManager(this);
        navigationBackManager = new ScreenNavigationBackManager(this);
        //bus.register(dialogShower);
        bus.register(navigationBackManager);
        JodaTimeAndroid.init(this);
    }

    @Override
    public void onBackPressed() {
        bus.post(new BackPressEvent());
    }

   /* public void showInfoDialog(String message){
        View view = getLayoutInflater().inflate(R.layout.info_dialog, null);
        TextView messageLabel = view.findViewById(R.id.message);
        View okBtn = view.findViewById(R.id.ok);
        messageLabel.setText(message);
        okBtn.setOnClickListener(v -> getBus().post(new HideDialogEvent()));
        bus.post(new ShowDialogEvent(view));
    }
*/




    @Override
    protected void onResume() {
        Log.i(TAG, " onResume()");
        super.onResume();
        //checkConnectivity();
    }

    @Override
    protected void onPostResume() {
        Log.i(TAG, " onPostResume()");
        super.onPostResume();
    }


    @Override
    protected void onPause() {
        Log.i(TAG, " onPause()");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, " onDestroy()");
        //bus.unregister(dialogShower);
        //bus.unregister(navigationBackManager);
        super.onDestroy();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent() Intent=" + intent);
    }


    public Bus getBus() {
        return bus;
    }


    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public void hideKeyboard() {
        try {
            IBinder windowToken = getWindow().getDecorView().getRootView().getWindowToken();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken, 0);
        } catch (Exception e) {
            Timber.e(e.getLocalizedMessage());
        }
    }

    public void showKeyboard(EditText editText) {
        try {
            IBinder windowToken = getWindow().getDecorView().getRootView().getWindowToken();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, 0);
        } catch (Exception e) {
            Timber.e(e.getLocalizedMessage());
        }
    }
    //public abstract ActionBarContract.View getActionBarView();


    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }
    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        String p = null;
        if (cursor!=null) {
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            p = cursor.getString(column_index);
            cursor.close();
        }
        return p;
    }

    public void dialogDismissed(){
        /*if (!connectivityNotifier.isConnected()){
            isNoNetworkDialogShowing = false;

            checkInternetWithDelay(INTERVAL_SHOWING_NO_NETWORK_DIALOG);
        }*/
        //getBus().post(new DialogWasDissmisedEvent());
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public BackNavigator getNavigationBackManager() {
        return navigationBackManager;
    }
}

