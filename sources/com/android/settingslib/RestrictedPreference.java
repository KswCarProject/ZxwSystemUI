package com.android.settingslib;

import android.content.Context;
import android.util.AttributeSet;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.widget.TwoTargetPreference;

public class RestrictedPreference extends TwoTargetPreference {
    public RestrictedPreferenceHelper mHelper;

    public RestrictedPreference(Context context, AttributeSet attributeSet, int i, int i2, String str, int i3) {
        super(context, attributeSet, i, i2);
        this.mHelper = new RestrictedPreferenceHelper(context, this, attributeSet, str, i3);
    }

    public RestrictedPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        this(context, attributeSet, i, i2, (String) null, -1);
    }

    public RestrictedPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public RestrictedPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R$attr.preferenceStyle, 16842894));
    }

    public RestrictedPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mHelper.onBindViewHolder(preferenceViewHolder);
    }

    public void performClick() {
        if (!this.mHelper.performClick()) {
            super.performClick();
        }
    }

    public void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        this.mHelper.onAttachedToHierarchy();
        super.onAttachedToHierarchy(preferenceManager);
    }

    public void setEnabled(boolean z) {
        if (!z || !isDisabledByAdmin()) {
            super.setEnabled(z);
        } else {
            this.mHelper.setDisabledByAdmin((RestrictedLockUtils.EnforcedAdmin) null);
        }
    }

    public boolean isDisabledByAdmin() {
        return this.mHelper.isDisabledByAdmin();
    }
}
