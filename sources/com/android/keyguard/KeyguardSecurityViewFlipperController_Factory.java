package com.android.keyguard;

import android.view.LayoutInflater;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardInputViewController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardSecurityViewFlipperController_Factory implements Factory<KeyguardSecurityViewFlipperController> {
    public final Provider<EmergencyButtonController.Factory> emergencyButtonControllerFactoryProvider;
    public final Provider<KeyguardInputViewController.Factory> keyguardSecurityViewControllerFactoryProvider;
    public final Provider<LayoutInflater> layoutInflaterProvider;
    public final Provider<KeyguardSecurityViewFlipper> viewProvider;

    public KeyguardSecurityViewFlipperController_Factory(Provider<KeyguardSecurityViewFlipper> provider, Provider<LayoutInflater> provider2, Provider<KeyguardInputViewController.Factory> provider3, Provider<EmergencyButtonController.Factory> provider4) {
        this.viewProvider = provider;
        this.layoutInflaterProvider = provider2;
        this.keyguardSecurityViewControllerFactoryProvider = provider3;
        this.emergencyButtonControllerFactoryProvider = provider4;
    }

    public KeyguardSecurityViewFlipperController get() {
        return newInstance(this.viewProvider.get(), this.layoutInflaterProvider.get(), this.keyguardSecurityViewControllerFactoryProvider.get(), this.emergencyButtonControllerFactoryProvider.get());
    }

    public static KeyguardSecurityViewFlipperController_Factory create(Provider<KeyguardSecurityViewFlipper> provider, Provider<LayoutInflater> provider2, Provider<KeyguardInputViewController.Factory> provider3, Provider<EmergencyButtonController.Factory> provider4) {
        return new KeyguardSecurityViewFlipperController_Factory(provider, provider2, provider3, provider4);
    }

    public static KeyguardSecurityViewFlipperController newInstance(KeyguardSecurityViewFlipper keyguardSecurityViewFlipper, LayoutInflater layoutInflater, KeyguardInputViewController.Factory factory, EmergencyButtonController.Factory factory2) {
        return new KeyguardSecurityViewFlipperController(keyguardSecurityViewFlipper, layoutInflater, factory, factory2);
    }
}
