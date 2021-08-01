package com.loicteillard.easytabs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import loic.teillard.easytabs.R;

public class EasyTabs extends LinearLayout {

    public static final int SEP_DEFAULT_COLOR = Color.parseColor("#b7b7b7");

    public static final int INDICATOR_TEXT = 0;
    public static final int INDICATOR_VALUE = 1;
    public static final int INDICATOR_MATCH_PARENT = 2;

    private View mIndicator;
    private boolean mSeparatorsEnabled;
    private boolean mIndicatorsEnabled;
    private boolean mBoldForSelected;
    private ArrayList<View> mTabs;
    private int mSelectedColor, mUnselectedColor, mSeparatorColor;
    private int mSeparatorWidth, mSeparatorSize;
    private ViewPager mViewPager;
    protected ViewPager.OnPageChangeListener mOnPageChangeListener;
    private int mDefaultTab;
    private PagerListener mPagerListener;

    // ---------------------------------------------------------------------------------------------------------------------

    public interface PagerListener {
        void onTabSelected(int position);
    }

    // ---------------------------------------------------------------------------------------------------------------------

    public EasyTabs(Context context) {
        super(context);
        initialize(null);
    }

    public EasyTabs(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public EasyTabs(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    // ---------------------------------------------------------------------------------------------------------------------

    private void initialize(AttributeSet attrs) {

        // Read custom values
        TypedArray attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.EasyTabsAttrs, 0, 0);
        initAttributesArray(attrsArray);
        attrsArray.recycle();

        // Prepare current viewgroup layout
        setOrientation(VERTICAL);
        LinearLayout.LayoutParams lParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lParams);
    }

    // ---------------------------------------------------------------------------------------------------------------------

    private void populate() {
        // Prepare layout for tabs
        LinearLayout.LayoutParams lParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final LinearLayout layoutTabs = new LinearLayout(getContext());
        layoutTabs.setOrientation(HORIZONTAL);
        layoutTabs.setLayoutParams(lParams);

        // Add childs views
        for (int i = 0; i < getChildCount(); i++) {

            View view = getChildAt(i);

            if (view instanceof LinearLayout) {
                LinearLayout linear = (LinearLayout) view;
                // TextView textView = (TextView) view;
                addTab(prepareLinearTab(linear), i);
            }
        }

        // Clear views (childs can have only one parent)
        removeAllViews();

        // Create custom stuff
        mIndicator = createIndicator();

        // Add tabs items
        for (View view : getTabs()) {
            layoutTabs.addView(view);
            if (mSeparatorsEnabled) layoutTabs.addView(createSeparator());
        }

        // Add views
        addView(layoutTabs);

        // At the end, add views to the main viewgroup
        if (mIndicatorsEnabled) addView(mIndicator);

        // Listener to change state
        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchLayoutState(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        getViewPager().clearOnPageChangeListeners();
        getViewPager().addOnPageChangeListener(mOnPageChangeListener);

        // Initial state on the default item
        switchLayoutState(mDefaultTab);
    }

    // ---------------------------------------------------------------------------------------------------------------------

    public ViewPager getViewPager() {
        if (mViewPager == null)
            throw new IllegalStateException("No ViewPager found, please add a viewpager as a child of the layout !");
        return mViewPager;
    }

    // ---------------------------------------------------------------------------------------------------------------------

    public PagerAdapter getPagerAdapter() {
        if (mViewPager == null)
            throw new IllegalStateException("No ViewPager found, please set one !");
        if (mViewPager.getAdapter() == null)
            throw new IllegalStateException("No Adapter found for this viewpager, please set one !");
        if (mViewPager.getAdapter().getCount() != getTabs().size())
            throw new IllegalStateException("Adapter must have the same number of items than tabs !");
        return mViewPager.getAdapter();
    }

    // ---------------------------------------------------------------------------------------------------------------------

    public void setViewPager(ViewPager viewPager) {
        setViewPager(viewPager, 0);
    }

    // ---------------------------------------------------------------------------------------------------------------------

    public void setViewPager(ViewPager viewPager, int defaultTab) {
        mViewPager = viewPager;
        mDefaultTab = defaultTab;
        populate();
    }

    // ---------------------------------------------------------------------------------------------------------------------

    public void setPagerListener(PagerListener pagerListener) {
        mPagerListener = pagerListener;
    }

    // ---------------------------------------------------------------------------------------------------------------------

    private void initAttributesArray(TypedArray attrsArray) {

        if (attrsArray == null) return;

        mSelectedColor = attrsArray.getColor(R.styleable.EasyTabsAttrs_etab_selected_color, Color.BLACK);
        mUnselectedColor = attrsArray.getColor(R.styleable.EasyTabsAttrs_etab_unselected_color, Color.BLACK);
        mSeparatorSize = attrsArray.getInt(R.styleable.EasyTabsAttrs_etab_indicator_size, INDICATOR_TEXT);
        mSeparatorWidth = attrsArray.getDimensionPixelSize(R.styleable.EasyTabsAttrs_etab_indicator_width, 0);
        mSeparatorsEnabled = attrsArray.getBoolean(R.styleable.EasyTabsAttrs_etab_separators, false);
        mIndicatorsEnabled = attrsArray.getBoolean(R.styleable.EasyTabsAttrs_etab_indicators, true);
        mBoldForSelected = attrsArray.getBoolean(R.styleable.EasyTabsAttrs_etab_bold_for_selected, false);
        mSeparatorColor = attrsArray.getColor(R.styleable.EasyTabsAttrs_etab_separator_color, SEP_DEFAULT_COLOR);
    }

    // ---------------------------------------------------------------------------------------------------------------------

    private void switchLayoutState(int selected) {

        int selectedColor = mSelectedColor;
        int unselectedColor = mUnselectedColor;

        for (int i = 0; i < getPagerAdapter().getCount(); i++) {
            View view = getTabs().get(i);
            if (view instanceof LinearLayout) {
                final LinearLayout tab = (LinearLayout) getTabs().get(i);
                if (view instanceof EasyTabLayout) {
                    EasyTabLayout easyTabTextView = (EasyTabLayout) tab;
                    if (easyTabTextView.getSelectedColor() != 0)
                        selectedColor = easyTabTextView.getSelectedColor();
                    if (easyTabTextView.getUnselectedColor() != 0)
                        unselectedColor = easyTabTextView.getUnselectedColor();
                }

                ImageView img = (ImageView) tab.getChildAt(0);
                TextView text = (TextView) tab.getChildAt(1);

                text.setTextColor((i == selected) ? selectedColor : unselectedColor);
                img.setColorFilter((i == selected) ? selectedColor : unselectedColor);
                if (i == selected) mIndicator.setBackgroundColor(selectedColor);
                text.setTypeface(null, i == selected && mBoldForSelected ? Typeface.BOLD : Typeface.NORMAL);

                if (i == selected) {
                    tab.post(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (mIndicator.getMeasuredWidth() > 0)
                                        mIndicator.animate().translationX(tab.getX()).setDuration(200);
                                    else mIndicator.setTranslationX(tab.getX());
                                    int padding = 0;
                                    int tabWidth = tab.getMeasuredWidth();
                                    padding = tabWidth;
                                    ETUtils.setDimensionLayout(mIndicator, padding, -1);
                                    ETUtils.setMarginsLayout(mIndicator, (tabWidth - padding) >> 1, -1, (tabWidth - padding) >> 1, -1);
                                }
                            }
                    );
                }
            }
        }

        getViewPager().removeOnPageChangeListener(mOnPageChangeListener);
        getViewPager().setCurrentItem(selected, true);
        getViewPager().addOnPageChangeListener(mOnPageChangeListener);


        if (mPagerListener != null) mPagerListener.onTabSelected(selected);

    }

    // ---------------------------------------------------------------------------------------------------------------------

    private LinearLayout prepareLinearTab(LinearLayout tab) {
        tab.setGravity(Gravity.CENTER);
        tab.setPadding(0, 0, 0, ETUtils.dpToPx(5));

        LinearLayout.LayoutParams textViewParams = new LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewParams.weight = 1f;
        textViewParams.gravity = Gravity.CENTER;
        tab.setLayoutParams(textViewParams);

        return tab;
    }

    // ---------------------------------------------------------------------------------------------------------------------

    private View createSeparator() {

        View view = new View(getContext());
        view.setBackgroundColor(mSeparatorColor);

        LinearLayout.LayoutParams params = new LayoutParams(ETUtils.dpToPx(1), ETUtils.dpToPx(25));
        params.gravity = Gravity.CENTER_VERTICAL;
        view.setLayoutParams(params);

        return view;
    }

    // ---------------------------------------------------------------------------------------------------------------------

    private View createIndicator() {
        View view = new View(getContext());
        LinearLayout.LayoutParams indicatorParams = new LayoutParams(0, ETUtils.dpToPx(3));
        indicatorParams.gravity = Gravity.TOP;
        view.setLayoutParams(indicatorParams);

        return view;
    }

    // ---------------------------------------------------------------------------------------------------------------------

    public void addTab(View view, final int index) {
        if (view == null) return;
        getTabs().add(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchLayoutState(index);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------------------------------

    public ArrayList<View> getTabs() {
        if (mTabs == null) mTabs = new ArrayList<>();
        return mTabs;
    }

    // ---------------------------------------------------------------------------------------------------------------------
}
