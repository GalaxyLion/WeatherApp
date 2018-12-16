package com.example.galax.weatherapp.base.dialogs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.galax.weatherapp.R;


/**
 * Created by max_ermakov on 1/18/17.
 */

public class BaseDialogView extends FrameLayout {
    private static final String TAG = BaseDialogView.class.getSimpleName();

    private ViewGroup contentContainer;
    private ViewGroup actionsContainer;

    public BaseDialogView(Context context) {
        super(context);
        init();
    }

    public BaseDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BaseDialogView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.base_modal_message, this, true);
        contentContainer = view.findViewById(R.id.dialog_content_container);
    }

    public ViewGroup getContentContainer() {
        return contentContainer;
    }
}
