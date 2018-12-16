package com.example.galax.weatherapp.base.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.example.galax.weatherapp.base.BaseActivity;
import com.example.galax.weatherapp.base.dialogs.events.HideDialogEvent;
import com.example.galax.weatherapp.base.dialogs.events.SetDialogCancelableEvent;
import com.example.galax.weatherapp.base.dialogs.events.ShowDialogEvent;
import com.squareup.otto.Subscribe;


import timber.log.Timber;



public class DialogShower {
    private BaseActivity context;
    private BaseDialog dialog;

    public DialogShower(BaseActivity context) {
        this.context = context;
    }

    public  void showDialog(View view) {
        dialog = new BaseDialog(context);
        dialog.setCanceledOnTouchOutside(true);
        BaseDialogView dialogView = new BaseDialogView(context);

        final ViewGroup contentContainer = dialogView.getContentContainer();
        contentContainer.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(context,dialog);
            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(dialogView);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                contentContainer.removeAllViews();
                context.dialogDismissed();
            }
        });
        dialog.show();
    }

    @Subscribe
    public void onEvent(ShowDialogEvent showDialogEvent){
        if (dialog==null||!dialog.isShowing()) {
            showDialog(showDialogEvent.view);
        }else {
            dialog.dismiss();
            showDialog(showDialogEvent.view);
        }
    }

    @Subscribe
    public void onEvent(HideDialogEvent event){
        hideDialog();
    }

    @Subscribe
    public void onEvent(SetDialogCancelableEvent event){
        if (dialog!=null){
            dialog.setCancelable(event.cancelable);
        }
    }

    public void hideDialog() {
        context.hideKeyboard();

        try {
            if ((this.dialog != null) && this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
            Timber.e(e.getLocalizedMessage());
        } catch (final Exception e) {
            // Handle or log or ignore
            Timber.e(e.getLocalizedMessage());
        } finally {
            this.dialog = null;
        }
    }

    public static void hideSoftKeyboard(Context activity, BaseDialog dialog) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (dialog!=null) {
            if (dialog.getCurrentFocus()!=null){
                inputMethodManager.hideSoftInputFromWindow(
                        dialog.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }
}
