package com.android.settingslib;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.utils.BuildCompatUtils;

public class RestrictedPreferenceHelper {
    public String mAttrUserRestriction;
    public final Context mContext;
    public boolean mDisabledByAdmin;
    public boolean mDisabledByAppOps;
    public boolean mDisabledSummary;
    public RestrictedLockUtils.EnforcedAdmin mEnforcedAdmin;
    public final Preference mPreference;
    public String packageName;
    public int uid;

    public RestrictedPreferenceHelper(Context context, Preference preference, AttributeSet attributeSet, String str, int i) {
        CharSequence charSequence;
        String str2;
        this.mAttrUserRestriction = null;
        boolean z = false;
        this.mDisabledSummary = false;
        this.mContext = context;
        this.mPreference = preference;
        this.packageName = str;
        this.uid = i;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.RestrictedPreference);
            TypedValue peekValue = obtainStyledAttributes.peekValue(R$styleable.RestrictedPreference_userRestriction);
            if (peekValue == null || peekValue.type != 3) {
                charSequence = null;
            } else {
                int i2 = peekValue.resourceId;
                charSequence = i2 != 0 ? context.getText(i2) : peekValue.string;
            }
            if (charSequence == null) {
                str2 = null;
            } else {
                str2 = charSequence.toString();
            }
            this.mAttrUserRestriction = str2;
            if (RestrictedLockUtilsInternal.hasBaseUserRestriction(context, str2, UserHandle.myUserId())) {
                this.mAttrUserRestriction = null;
                return;
            }
            TypedValue peekValue2 = obtainStyledAttributes.peekValue(R$styleable.RestrictedPreference_useAdminDisabledSummary);
            if (peekValue2 != null) {
                if (peekValue2.type == 18 && peekValue2.data != 0) {
                    z = true;
                }
                this.mDisabledSummary = z;
            }
        }
    }

    public RestrictedPreferenceHelper(Context context, Preference preference, AttributeSet attributeSet) {
        this(context, preference, attributeSet, (String) null, -1);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        TextView textView;
        String str;
        if (this.mDisabledByAdmin || this.mDisabledByAppOps) {
            preferenceViewHolder.itemView.setEnabled(true);
        }
        if (this.mDisabledSummary && (textView = (TextView) preferenceViewHolder.findViewById(16908304)) != null) {
            if (BuildCompatUtils.isAtLeastT()) {
                str = getDisabledByAdminUpdatableString();
            } else {
                str = this.mContext.getString(R$string.disabled_by_admin_summary_text);
            }
            if (this.mDisabledByAdmin) {
                textView.setText(str);
            } else if (this.mDisabledByAppOps) {
                textView.setText(R$string.disabled_by_app_ops_text);
            } else if (TextUtils.equals(str, textView.getText())) {
                textView.setText((CharSequence) null);
            }
        }
    }

    public final String getDisabledByAdminUpdatableString() {
        return ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class)).getResources().getString("Settings.CONTROLLED_BY_ADMIN_SUMMARY", new RestrictedPreferenceHelper$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getDisabledByAdminUpdatableString$0() {
        return this.mContext.getString(R$string.disabled_by_admin_summary_text);
    }

    public void useAdminDisabledSummary(boolean z) {
        this.mDisabledSummary = z;
    }

    public boolean performClick() {
        if (this.mDisabledByAdmin) {
            RestrictedLockUtils.sendShowAdminSupportDetailsIntent(this.mContext, this.mEnforcedAdmin);
            return true;
        } else if (!this.mDisabledByAppOps) {
            return false;
        } else {
            RestrictedLockUtilsInternal.sendShowRestrictedSettingDialogIntent(this.mContext, this.packageName, this.uid);
            return true;
        }
    }

    public void onAttachedToHierarchy() {
        String str = this.mAttrUserRestriction;
        if (str != null) {
            checkRestrictionAndSetDisabled(str, UserHandle.myUserId());
        }
    }

    public void checkRestrictionAndSetDisabled(String str, int i) {
        setDisabledByAdmin(RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, str, i));
    }

    public boolean setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        boolean z = enforcedAdmin != null;
        this.mEnforcedAdmin = enforcedAdmin;
        if (this.mDisabledByAdmin == z) {
            return false;
        }
        this.mDisabledByAdmin = z;
        updateDisabledState();
        return true;
    }

    public boolean setDisabledByAppOps(boolean z) {
        if (this.mDisabledByAppOps == z) {
            return false;
        }
        this.mDisabledByAppOps = z;
        updateDisabledState();
        return true;
    }

    public boolean isDisabledByAdmin() {
        return this.mDisabledByAdmin;
    }

    public boolean isDisabledByAppOps() {
        return this.mDisabledByAppOps;
    }

    public final void updateDisabledState() {
        Preference preference = this.mPreference;
        boolean z = true;
        if (!(preference instanceof RestrictedTopLevelPreference)) {
            preference.setEnabled(!this.mDisabledByAdmin && !this.mDisabledByAppOps);
        }
        Preference preference2 = this.mPreference;
        if (preference2 instanceof PrimarySwitchPreference) {
            PrimarySwitchPreference primarySwitchPreference = (PrimarySwitchPreference) preference2;
            if (this.mDisabledByAdmin || this.mDisabledByAppOps) {
                z = false;
            }
            primarySwitchPreference.setSwitchEnabled(z);
        }
    }
}
