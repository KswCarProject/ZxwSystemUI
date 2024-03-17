package com.android.systemui.privacy;

import com.android.internal.logging.UiEventLogger;

/* compiled from: PrivacyDialogEvent.kt */
public enum PrivacyDialogEvent implements UiEventLogger.UiEventEnum {
    PRIVACY_DIALOG_ITEM_CLICKED_TO_APP_SETTINGS(904),
    PRIVACY_DIALOG_DISMISSED(905);
    
    private final int _id;

    /* access modifiers changed from: public */
    PrivacyDialogEvent(int i) {
        this._id = i;
    }

    public int getId() {
        return this._id;
    }
}
