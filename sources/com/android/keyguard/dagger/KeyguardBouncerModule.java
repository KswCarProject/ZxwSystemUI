package com.android.keyguard.dagger;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.keyguard.KeyguardHostView;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityViewFlipper;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;

public interface KeyguardBouncerModule {
    static KeyguardHostView providesKeyguardHostView(ViewGroup viewGroup, LayoutInflater layoutInflater) {
        KeyguardHostView keyguardHostView = (KeyguardHostView) layoutInflater.inflate(R$layout.keyguard_host_view, viewGroup, false);
        viewGroup.addView(keyguardHostView);
        return keyguardHostView;
    }

    static KeyguardSecurityContainer providesKeyguardSecurityContainer(KeyguardHostView keyguardHostView) {
        return (KeyguardSecurityContainer) keyguardHostView.findViewById(R$id.keyguard_security_container);
    }

    static KeyguardSecurityViewFlipper providesKeyguardSecurityViewFlipper(KeyguardSecurityContainer keyguardSecurityContainer) {
        return (KeyguardSecurityViewFlipper) keyguardSecurityContainer.findViewById(R$id.view_flipper);
    }
}
