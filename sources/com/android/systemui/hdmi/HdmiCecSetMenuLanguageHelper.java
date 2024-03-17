package com.android.systemui.hdmi;

import com.android.internal.app.LocalePicker;
import com.android.systemui.util.settings.SecureSettings;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.Executor;

public class HdmiCecSetMenuLanguageHelper {
    public final Executor mBackgroundExecutor;
    public HashSet<String> mDenylist;
    public Locale mLocale;
    public final SecureSettings mSecureSettings;

    public HdmiCecSetMenuLanguageHelper(Executor executor, SecureSettings secureSettings) {
        Collection collection;
        this.mBackgroundExecutor = executor;
        this.mSecureSettings = secureSettings;
        String stringForUser = secureSettings.getStringForUser("hdmi_cec_set_menu_language_denylist", -2);
        if (stringForUser == null) {
            collection = Collections.EMPTY_SET;
        } else {
            collection = Arrays.asList(stringForUser.split(","));
        }
        this.mDenylist = new HashSet<>(collection);
    }

    public void setLocale(String str) {
        this.mLocale = Locale.forLanguageTag(str);
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public boolean isLocaleDenylisted() {
        return this.mDenylist.contains(this.mLocale.toLanguageTag());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$acceptLocale$0() {
        LocalePicker.updateLocale(this.mLocale);
    }

    public void acceptLocale() {
        this.mBackgroundExecutor.execute(new HdmiCecSetMenuLanguageHelper$$ExternalSyntheticLambda0(this));
    }

    public void declineLocale() {
        this.mDenylist.add(this.mLocale.toLanguageTag());
        this.mSecureSettings.putStringForUser("hdmi_cec_set_menu_language_denylist", String.join(",", this.mDenylist), -2);
    }
}
