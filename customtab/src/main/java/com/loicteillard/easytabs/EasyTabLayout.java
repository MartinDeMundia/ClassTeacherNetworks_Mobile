package com.loicteillard.easytabs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import loic.teillard.easytabs.R;

public class EasyTabLayout extends LinearLayout {

    private int mSelectedColor, mUnselectedColor;

    // ---------------------------------------------------------------------------------------------------------------------

    public EasyTabLayout(Context context) {
        super(context);
        initialize(null);
    }

    public EasyTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public EasyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    // ---------------------------------------------------------------------------------------------------------------------

    private void initialize(AttributeSet attrs) {

        // Read custom values
        TypedArray attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.EasyTabsAttrs, 0, 0);
        initAttributesArray(attrsArray);
        attrsArray.recycle();
    }

    // ---------------------------------------------------------------------------------------------------------------------

    private void initAttributesArray(TypedArray attrsArray) {

        if (attrsArray == null) return;

        mSelectedColor = attrsArray.getColor(R.styleable.EasyTabsAttrs_etab_selected_color, 0);
        mUnselectedColor = attrsArray.getColor(R.styleable.EasyTabsAttrs_etab_unselected_color, 0);
    }

    // ---------------------------------------------------------------------------------------------------------------------


    public int getSelectedColor() {
        return mSelectedColor;
    }

    public int getUnselectedColor() {
        return mUnselectedColor;
    }

    // ---------------------------------------------------------------------------------------------------------------------
}
