package com.android.keyguard.dagger;

import android.view.ViewGroup;
import com.android.keyguard.KeyguardHostViewController;

public interface KeyguardBouncerComponent {

    public interface Factory {
        KeyguardBouncerComponent create(ViewGroup viewGroup);
    }

    KeyguardHostViewController getKeyguardHostViewController();
}
