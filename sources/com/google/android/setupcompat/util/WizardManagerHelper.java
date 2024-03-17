package com.google.android.setupcompat.util;

import android.content.Intent;

public final class WizardManagerHelper {
    public static final String ACTION_NEXT = "com.android.wizard.NEXT";
    public static final String EXTRA_ACTION_ID = "actionId";
    public static final String EXTRA_SCRIPT_URI = "scriptUri";
    public static final String EXTRA_WIZARD_BUNDLE = "wizardBundle";

    public static boolean isAnySetupWizard(Intent intent) {
        if (intent == null) {
            return false;
        }
        return intent.getBooleanExtra("isSetupFlow", false);
    }
}
