package com.android.systemui.accessibility;

import android.content.Context;

public class AccessibilityButtonTargetsObserver extends SecureSettingsContentObserver<TargetsChangedListener> {

    public interface TargetsChangedListener {
        void onAccessibilityButtonTargetsChanged(String str);
    }

    public AccessibilityButtonTargetsObserver(Context context) {
        super(context, "accessibility_button_targets");
    }

    public void onValueChanged(TargetsChangedListener targetsChangedListener, String str) {
        targetsChangedListener.onAccessibilityButtonTargetsChanged(str);
    }

    public String getCurrentAccessibilityButtonTargets() {
        return getSettingsValue();
    }
}
