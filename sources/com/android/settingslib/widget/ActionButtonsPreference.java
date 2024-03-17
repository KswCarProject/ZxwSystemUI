package com.android.settingslib.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.utils.BuildCompatUtils;
import java.util.ArrayList;
import java.util.List;

public class ActionButtonsPreference extends Preference {
    public static final boolean mIsAtLeastS = BuildCompatUtils.isAtLeastS();
    public final List<Drawable> mBtnBackgroundStyle1 = new ArrayList(1);
    public final List<Drawable> mBtnBackgroundStyle2 = new ArrayList(2);
    public final List<Drawable> mBtnBackgroundStyle3 = new ArrayList(3);
    public final List<Drawable> mBtnBackgroundStyle4 = new ArrayList(4);
    public final ButtonInfo mButton1Info = new ButtonInfo();
    public final ButtonInfo mButton2Info = new ButtonInfo();
    public final ButtonInfo mButton3Info = new ButtonInfo();
    public final ButtonInfo mButton4Info = new ButtonInfo();
    public View mDivider1;
    public View mDivider2;
    public View mDivider3;
    public final List<ButtonInfo> mVisibleButtonInfos = new ArrayList(4);

    public ActionButtonsPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public final void init() {
        setLayoutResource(R$layout.settingslib_action_buttons);
        setSelectable(false);
        Resources resources = getContext().getResources();
        fetchDrawableArray(this.mBtnBackgroundStyle1, resources.obtainTypedArray(R$array.background_style1));
        fetchDrawableArray(this.mBtnBackgroundStyle2, resources.obtainTypedArray(R$array.background_style2));
        fetchDrawableArray(this.mBtnBackgroundStyle3, resources.obtainTypedArray(R$array.background_style3));
        fetchDrawableArray(this.mBtnBackgroundStyle4, resources.obtainTypedArray(R$array.background_style4));
    }

    public final void fetchDrawableArray(List<Drawable> list, TypedArray typedArray) {
        for (int i = 0; i < typedArray.length(); i++) {
            list.add(getContext().getDrawable(typedArray.getResourceId(i, 0)));
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        boolean z = mIsAtLeastS;
        preferenceViewHolder.setDividerAllowedAbove(!z);
        preferenceViewHolder.setDividerAllowedBelow(!z);
        this.mButton1Info.mButton = (Button) preferenceViewHolder.findViewById(R$id.button1);
        this.mButton2Info.mButton = (Button) preferenceViewHolder.findViewById(R$id.button2);
        this.mButton3Info.mButton = (Button) preferenceViewHolder.findViewById(R$id.button3);
        this.mButton4Info.mButton = (Button) preferenceViewHolder.findViewById(R$id.button4);
        this.mDivider1 = preferenceViewHolder.findViewById(R$id.divider1);
        this.mDivider2 = preferenceViewHolder.findViewById(R$id.divider2);
        this.mDivider3 = preferenceViewHolder.findViewById(R$id.divider3);
        this.mButton1Info.setUpButton();
        this.mButton2Info.setUpButton();
        this.mButton3Info.setUpButton();
        this.mButton4Info.setUpButton();
        if (!this.mVisibleButtonInfos.isEmpty()) {
            this.mVisibleButtonInfos.clear();
        }
        updateLayout();
    }

    public void notifyChanged() {
        super.notifyChanged();
        if (!this.mVisibleButtonInfos.isEmpty()) {
            this.mVisibleButtonInfos.clear();
            updateLayout();
        }
    }

    public final void updateLayout() {
        if (this.mButton1Info.isVisible() && mIsAtLeastS) {
            this.mVisibleButtonInfos.add(this.mButton1Info);
        }
        if (this.mButton2Info.isVisible() && mIsAtLeastS) {
            this.mVisibleButtonInfos.add(this.mButton2Info);
        }
        if (this.mButton3Info.isVisible() && mIsAtLeastS) {
            this.mVisibleButtonInfos.add(this.mButton3Info);
        }
        if (this.mButton4Info.isVisible() && mIsAtLeastS) {
            this.mVisibleButtonInfos.add(this.mButton4Info);
        }
        boolean z = getContext().getResources().getConfiguration().getLayoutDirection() == 1;
        int size = this.mVisibleButtonInfos.size();
        if (size != 1) {
            if (size != 2) {
                if (size != 3) {
                    if (size != 4) {
                        Log.e("ActionButtonPreference", "No visible buttons info, skip background settings.");
                    } else if (z) {
                        setupRtlBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle4);
                    } else {
                        setupBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle4);
                    }
                } else if (z) {
                    setupRtlBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle3);
                } else {
                    setupBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle3);
                }
            } else if (z) {
                setupRtlBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle2);
            } else {
                setupBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle2);
            }
        } else if (z) {
            setupRtlBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle1);
        } else {
            setupBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle1);
        }
        setupDivider1();
        setupDivider2();
        setupDivider3();
    }

    public final void setupBackgrounds(List<ButtonInfo> list, List<Drawable> list2) {
        for (int i = 0; i < list2.size(); i++) {
            list.get(i).mButton.setBackground(list2.get(i));
        }
    }

    public final void setupRtlBackgrounds(List<ButtonInfo> list, List<Drawable> list2) {
        for (int size = list2.size() - 1; size >= 0; size--) {
            list.get((list2.size() - 1) - size).mButton.setBackground(list2.get(size));
        }
    }

    public final void setupDivider1() {
        if (this.mDivider1 != null && this.mButton1Info.isVisible() && this.mButton2Info.isVisible()) {
            this.mDivider1.setVisibility(0);
        }
    }

    public final void setupDivider2() {
        if (this.mDivider2 != null && this.mButton3Info.isVisible()) {
            if (this.mButton1Info.isVisible() || this.mButton2Info.isVisible()) {
                this.mDivider2.setVisibility(0);
            }
        }
    }

    public final void setupDivider3() {
        if (this.mDivider3 != null && this.mVisibleButtonInfos.size() > 1 && this.mButton4Info.isVisible()) {
            this.mDivider3.setVisibility(0);
        }
    }

    public static class ButtonInfo {
        public Button mButton;
        public Drawable mIcon;
        public boolean mIsEnabled = true;
        public boolean mIsVisible = true;
        public View.OnClickListener mListener;
        public CharSequence mText;

        public void setUpButton() {
            this.mButton.setText(this.mText);
            this.mButton.setOnClickListener(this.mListener);
            this.mButton.setEnabled(this.mIsEnabled);
            this.mButton.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, this.mIcon, (Drawable) null, (Drawable) null);
            if (shouldBeVisible()) {
                this.mButton.setVisibility(0);
            } else {
                this.mButton.setVisibility(8);
            }
        }

        public boolean isVisible() {
            return this.mButton.getVisibility() == 0;
        }

        public final boolean shouldBeVisible() {
            return this.mIsVisible && (!TextUtils.isEmpty(this.mText) || this.mIcon != null);
        }
    }
}
