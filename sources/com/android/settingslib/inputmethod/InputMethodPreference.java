package com.android.settingslib.inputmethod;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodInfo;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.PrimarySwitchPreference;
import com.android.settingslib.R$dimen;
import com.android.settingslib.R$string;

public class InputMethodPreference extends PrimarySwitchPreference implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    public static final String TAG = InputMethodPreference.class.getSimpleName();
    public AlertDialog mDialog = null;
    public final boolean mHasPriorityInSorting;
    public final InputMethodInfo mImi;
    public final InputMethodSettingValuesWrapper mInputMethodSettingValues;
    public final boolean mIsAllowedByOrganization;
    public final OnSavePreferenceListener mOnSaveListener;
    public final int mUserId;

    public interface OnSavePreferenceListener {
        void onSaveInputMethodPreference(InputMethodPreference inputMethodPreference);
    }

    @VisibleForTesting
    public InputMethodPreference(Context context, InputMethodInfo inputMethodInfo, CharSequence charSequence, boolean z, OnSavePreferenceListener onSavePreferenceListener, int i) {
        super(context);
        boolean z2 = false;
        setPersistent(false);
        this.mImi = inputMethodInfo;
        this.mIsAllowedByOrganization = z;
        this.mOnSaveListener = onSavePreferenceListener;
        setKey(inputMethodInfo.getId());
        setTitle(charSequence);
        String settingsActivity = inputMethodInfo.getSettingsActivity();
        if (TextUtils.isEmpty(settingsActivity)) {
            setIntent((Intent) null);
        } else {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setClassName(inputMethodInfo.getPackageName(), settingsActivity);
            setIntent(intent);
        }
        this.mInputMethodSettingValues = InputMethodSettingValuesWrapper.getInstance(i != UserHandle.myUserId() ? getContext().createContextAsUser(UserHandle.of(i), 0) : context);
        this.mUserId = i;
        if (inputMethodInfo.isSystem() && InputMethodAndSubtypeUtil.isValidNonAuxAsciiCapableIme(inputMethodInfo)) {
            z2 = true;
        }
        this.mHasPriorityInSorting = z2;
        setOnPreferenceClickListener(this);
        setOnPreferenceChangeListener(this);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        Switch switchR = getSwitch();
        if (switchR != null) {
            switchR.setOnClickListener(new InputMethodPreference$$ExternalSyntheticLambda0(this, switchR));
        }
        ImageView imageView = (ImageView) preferenceViewHolder.itemView.findViewById(16908294);
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R$dimen.secondary_app_icon_size);
        if (imageView != null && dimensionPixelSize > 0) {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = dimensionPixelSize;
            layoutParams.width = dimensionPixelSize;
            imageView.setLayoutParams(layoutParams);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(Switch switchR, View view) {
        if (switchR.isEnabled()) {
            switchR.setChecked(isChecked());
            callChangeListener(Boolean.valueOf(!isChecked()));
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (isChecked()) {
            setCheckedInternal(false);
            return false;
        }
        if (!this.mImi.isSystem()) {
            showSecurityWarnDialog();
        } else if (this.mImi.getServiceInfo().directBootAware || isTv()) {
            setCheckedInternal(true);
        } else if (!isTv()) {
            showDirectBootWarnDialog();
        }
        return false;
    }

    public boolean onPreferenceClick(Preference preference) {
        Context context = getContext();
        try {
            Intent intent = getIntent();
            if (intent != null) {
                context.startActivityAsUser(intent, UserHandle.of(this.mUserId));
            }
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "IME's Settings Activity Not Found", e);
            Toast.makeText(context, context.getString(R$string.failed_to_open_app_settings_toast, new Object[]{this.mImi.loadLabel(context.getPackageManager())}), 1).show();
        }
        return true;
    }

    public final void setCheckedInternal(boolean z) {
        super.setChecked(z);
        this.mOnSaveListener.onSaveInputMethodPreference(this);
        notifyChanged();
    }

    public final void showSecurityWarnDialog() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mDialog.dismiss();
        }
        Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(17039380);
        CharSequence loadLabel = this.mImi.getServiceInfo().applicationInfo.loadLabel(context.getPackageManager());
        builder.setMessage(context.getString(R$string.ime_security_warning, new Object[]{loadLabel}));
        builder.setPositiveButton(17039370, new InputMethodPreference$$ExternalSyntheticLambda3(this));
        builder.setNegativeButton(17039360, new InputMethodPreference$$ExternalSyntheticLambda4(this));
        builder.setOnCancelListener(new InputMethodPreference$$ExternalSyntheticLambda5(this));
        AlertDialog create = builder.create();
        this.mDialog = create;
        create.show();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showSecurityWarnDialog$1(DialogInterface dialogInterface, int i) {
        if (this.mImi.getServiceInfo().directBootAware || isTv()) {
            setCheckedInternal(true);
        } else {
            showDirectBootWarnDialog();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showSecurityWarnDialog$2(DialogInterface dialogInterface, int i) {
        setCheckedInternal(false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showSecurityWarnDialog$3(DialogInterface dialogInterface) {
        setCheckedInternal(false);
    }

    public final boolean isTv() {
        return (getContext().getResources().getConfiguration().uiMode & 15) == 4;
    }

    public final void showDirectBootWarnDialog() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mDialog.dismiss();
        }
        Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage(context.getText(R$string.direct_boot_unaware_dialog_message));
        builder.setPositiveButton(17039370, new InputMethodPreference$$ExternalSyntheticLambda1(this));
        builder.setNegativeButton(17039360, new InputMethodPreference$$ExternalSyntheticLambda2(this));
        AlertDialog create = builder.create();
        this.mDialog = create;
        create.show();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showDirectBootWarnDialog$4(DialogInterface dialogInterface, int i) {
        setCheckedInternal(true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showDirectBootWarnDialog$5(DialogInterface dialogInterface, int i) {
        setCheckedInternal(false);
    }
}
