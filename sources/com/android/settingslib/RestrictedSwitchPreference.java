package com.android.settingslib;

import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreference;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.utils.BuildCompatUtils;

public class RestrictedSwitchPreference extends SwitchPreference {
    public AppOpsManager mAppOpsManager;
    public RestrictedPreferenceHelper mHelper;
    public int mIconSize;
    public CharSequence mRestrictedSwitchSummary;
    public boolean mUseAdditionalSummary;

    public RestrictedSwitchPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mUseAdditionalSummary = false;
        this.mHelper = new RestrictedPreferenceHelper(context, this, attributeSet);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.RestrictedSwitchPreference);
            TypedValue peekValue = obtainStyledAttributes.peekValue(R$styleable.RestrictedSwitchPreference_useAdditionalSummary);
            if (peekValue != null) {
                this.mUseAdditionalSummary = peekValue.type == 18 && peekValue.data != 0;
            }
            TypedValue peekValue2 = obtainStyledAttributes.peekValue(R$styleable.RestrictedSwitchPreference_restrictedSwitchSummary);
            obtainStyledAttributes.recycle();
            if (peekValue2 != null && peekValue2.type == 3) {
                int i3 = peekValue2.resourceId;
                if (i3 != 0) {
                    this.mRestrictedSwitchSummary = context.getText(i3);
                } else {
                    this.mRestrictedSwitchSummary = peekValue2.string;
                }
            }
        }
        if (this.mUseAdditionalSummary) {
            setLayoutResource(R$layout.restricted_switch_preference);
            useAdminDisabledSummary(false);
        }
    }

    public RestrictedSwitchPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public RestrictedSwitchPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R$attr.switchPreferenceStyle, 16843629));
    }

    public void setAppOps(AppOpsManager appOpsManager) {
        this.mAppOpsManager = appOpsManager;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(16908352);
        if (findViewById != null) {
            findViewById.getRootView().setFilterTouchesWhenObscured(true);
        }
        this.mHelper.onBindViewHolder(preferenceViewHolder);
        CharSequence charSequence = this.mRestrictedSwitchSummary;
        if (charSequence == null) {
            if (isChecked()) {
                charSequence = getUpdatableEnterpriseString(getContext(), "Settings.ENABLED_BY_ADMIN_SWITCH_SUMMARY", R$string.enabled_by_admin);
            } else {
                charSequence = getUpdatableEnterpriseString(getContext(), "Settings.DISABLED_BY_ADMIN_SWITCH_SUMMARY", R$string.disabled_by_admin);
            }
        }
        ImageView imageView = (ImageView) preferenceViewHolder.itemView.findViewById(16908294);
        if (this.mIconSize > 0) {
            int i = this.mIconSize;
            imageView.setLayoutParams(new LinearLayout.LayoutParams(i, i));
        }
        if (this.mUseAdditionalSummary) {
            TextView textView = (TextView) preferenceViewHolder.findViewById(R$id.additional_summary);
            if (textView == null) {
                return;
            }
            if (isDisabledByAdmin()) {
                textView.setText(charSequence);
                textView.setVisibility(0);
                return;
            }
            textView.setVisibility(8);
            return;
        }
        TextView textView2 = (TextView) preferenceViewHolder.findViewById(16908304);
        if (textView2 != null && isDisabledByAdmin()) {
            textView2.setText(charSequence);
            textView2.setVisibility(0);
        }
    }

    public static String getUpdatableEnterpriseString(Context context, String str, int i) {
        if (!BuildCompatUtils.isAtLeastT()) {
            return context.getString(i);
        }
        return ((DevicePolicyManager) context.getSystemService(DevicePolicyManager.class)).getResources().getString(str, new RestrictedSwitchPreference$$ExternalSyntheticLambda0(context, i));
    }

    public void performClick() {
        if (!this.mHelper.performClick()) {
            super.performClick();
        }
    }

    public void useAdminDisabledSummary(boolean z) {
        this.mHelper.useAdminDisabledSummary(z);
    }

    public void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        this.mHelper.onAttachedToHierarchy();
        super.onAttachedToHierarchy(preferenceManager);
    }

    public void setEnabled(boolean z) {
        boolean z2;
        boolean z3 = true;
        if (!z || !isDisabledByAdmin()) {
            z2 = false;
        } else {
            this.mHelper.setDisabledByAdmin((RestrictedLockUtils.EnforcedAdmin) null);
            z2 = true;
        }
        if (!z || !isDisabledByAppOps()) {
            z3 = z2;
        } else {
            this.mHelper.setDisabledByAppOps(false);
        }
        if (!z3) {
            super.setEnabled(z);
        }
    }

    public boolean isDisabledByAdmin() {
        return this.mHelper.isDisabledByAdmin();
    }

    public boolean isDisabledByAppOps() {
        return this.mHelper.isDisabledByAppOps();
    }
}
