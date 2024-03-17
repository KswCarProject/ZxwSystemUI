package com.android.systemui.qrcodescanner.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.DeviceConfig;
import android.util.Log;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;

public class QRCodeScannerController implements CallbackController<Callback> {
    public final ArrayList<Callback> mCallbacks = new ArrayList<>();
    public ComponentName mComponentName = null;
    public final boolean mConfigEnableLockScreenButton;
    public final Context mContext;
    public AtomicInteger mDefaultQRCodeScannerChangeEvents = new AtomicInteger(0);
    public final DeviceConfigProxy mDeviceConfigProxy;
    public final Executor mExecutor;
    public Intent mIntent = null;
    public Boolean mIsCameraAvailable = null;
    public DeviceConfig.OnPropertiesChangedListener mOnDefaultQRCodeScannerChangedListener = null;
    public String mQRCodeScannerActivity = null;
    public boolean mQRCodeScannerEnabled;
    public AtomicInteger mQRCodeScannerPreferenceChangeEvents = new AtomicInteger(0);
    public HashMap<Integer, ContentObserver> mQRCodeScannerPreferenceObserver = new HashMap<>();
    public final SecureSettings mSecureSettings;
    public UserTracker.Callback mUserChangedListener = null;
    public final UserTracker mUserTracker;

    public interface Callback {
        void onQRCodeScannerActivityChanged() {
        }

        void onQRCodeScannerPreferenceChanged() {
        }
    }

    public QRCodeScannerController(Context context, Executor executor, SecureSettings secureSettings, DeviceConfigProxy deviceConfigProxy, UserTracker userTracker) {
        this.mContext = context;
        this.mExecutor = executor;
        this.mSecureSettings = secureSettings;
        this.mDeviceConfigProxy = deviceConfigProxy;
        this.mUserTracker = userTracker;
        this.mConfigEnableLockScreenButton = context.getResources().getBoolean(17891336);
    }

    public void addCallback(@NotNull Callback callback) {
        if (isCameraAvailable()) {
            synchronized (this.mCallbacks) {
                this.mCallbacks.add(callback);
            }
        }
    }

    public void removeCallback(@NotNull Callback callback) {
        if (isCameraAvailable()) {
            synchronized (this.mCallbacks) {
                this.mCallbacks.remove(callback);
            }
        }
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r2.mIntent;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isEnabledForLockScreenButton() {
        /*
            r2 = this;
            boolean r0 = r2.mQRCodeScannerEnabled
            if (r0 == 0) goto L_0x0014
            android.content.Intent r0 = r2.mIntent
            if (r0 == 0) goto L_0x0014
            boolean r1 = r2.mConfigEnableLockScreenButton
            if (r1 == 0) goto L_0x0014
            boolean r2 = r2.isActivityCallable(r0)
            if (r2 == 0) goto L_0x0014
            r2 = 1
            goto L_0x0015
        L_0x0014:
            r2 = 0
        L_0x0015:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qrcodescanner.controller.QRCodeScannerController.isEnabledForLockScreenButton():boolean");
    }

    public boolean isEnabledForQuickSettings() {
        Intent intent = this.mIntent;
        return intent != null && isActivityCallable(intent);
    }

    public void registerQRCodeScannerChangeObservers(int... iArr) {
        if (isCameraAvailable()) {
            for (int i : iArr) {
                if (i == 0) {
                    this.mDefaultQRCodeScannerChangeEvents.incrementAndGet();
                    registerDefaultQRCodeScannerObserver();
                } else if (i != 1) {
                    Log.e("QRCodeScannerController", "Unrecognised event: " + i);
                } else {
                    this.mQRCodeScannerPreferenceChangeEvents.incrementAndGet();
                    registerQRCodePreferenceObserver();
                    registerUserChangeObservers();
                }
            }
        }
    }

    public void unregisterQRCodeScannerChangeObservers(int... iArr) {
        if (isCameraAvailable()) {
            for (int i : iArr) {
                if (i != 0) {
                    if (i != 1) {
                        Log.e("QRCodeScannerController", "Unrecognised event: " + i);
                    } else if (this.mUserTracker != null && this.mQRCodeScannerPreferenceChangeEvents.decrementAndGet() == 0) {
                        unregisterQRCodePreferenceObserver();
                        unregisterUserChangeObservers();
                    }
                } else if (this.mOnDefaultQRCodeScannerChangedListener != null && this.mDefaultQRCodeScannerChangeEvents.decrementAndGet() == 0) {
                    unregisterDefaultQRCodeScannerObserver();
                }
            }
        }
    }

    public boolean isCameraAvailable() {
        if (this.mIsCameraAvailable == null) {
            this.mIsCameraAvailable = Boolean.valueOf(this.mContext.getPackageManager().hasSystemFeature("android.hardware.camera"));
        }
        return this.mIsCameraAvailable.booleanValue();
    }

    public final void updateQRCodeScannerPreferenceDetails(boolean z) {
        if (this.mConfigEnableLockScreenButton) {
            boolean z2 = this.mQRCodeScannerEnabled;
            boolean z3 = false;
            if (this.mSecureSettings.getIntForUser("lock_screen_show_qr_code_scanner", 0, this.mUserTracker.getUserId()) != 0) {
                z3 = true;
            }
            this.mQRCodeScannerEnabled = z3;
            if (z) {
                this.mSecureSettings.putStringForUser("show_qr_code_scanner_setting", this.mQRCodeScannerActivity, this.mUserTracker.getUserId());
            }
            if (!Objects.equals(Boolean.valueOf(this.mQRCodeScannerEnabled), Boolean.valueOf(z2))) {
                notifyQRCodeScannerPreferenceChanged();
            }
        }
    }

    public final String getDefaultScannerActivity() {
        return this.mContext.getResources().getString(17039942);
    }

    /* renamed from: updateQRCodeScannerActivityDetails */
    public final void lambda$registerDefaultQRCodeScannerObserver$3() {
        ComponentName componentName;
        String string = this.mDeviceConfigProxy.getString("systemui", "default_qr_code_scanner", "");
        if (Objects.equals(string, "")) {
            string = getDefaultScannerActivity();
        }
        String str = this.mQRCodeScannerActivity;
        Intent intent = new Intent();
        if (string != null) {
            componentName = ComponentName.unflattenFromString(string);
            intent.setComponent(componentName);
            intent.addFlags(335544320);
        } else {
            componentName = null;
        }
        if (isActivityAvailable(intent)) {
            this.mQRCodeScannerActivity = string;
            this.mComponentName = componentName;
            this.mIntent = intent;
        } else {
            this.mQRCodeScannerActivity = null;
            this.mComponentName = null;
            this.mIntent = null;
        }
        if (!Objects.equals(this.mQRCodeScannerActivity, str)) {
            notifyQRCodeScannerActivityChanged();
        }
    }

    public final boolean isActivityAvailable(Intent intent) {
        if (intent.getComponent() == null) {
            return false;
        }
        return !this.mContext.getPackageManager().queryIntentActivities(intent, 537698816).isEmpty();
    }

    public final boolean isActivityCallable(Intent intent) {
        if (intent.getComponent() == null) {
            return false;
        }
        return !this.mContext.getPackageManager().queryIntentActivities(intent, 819200).isEmpty();
    }

    public final void unregisterUserChangeObservers() {
        this.mUserTracker.removeCallback(this.mUserChangedListener);
        this.mUserChangedListener = null;
        this.mQRCodeScannerEnabled = false;
    }

    public final void unregisterQRCodePreferenceObserver() {
        if (this.mConfigEnableLockScreenButton) {
            this.mQRCodeScannerPreferenceObserver.forEach(new QRCodeScannerController$$ExternalSyntheticLambda0(this));
            this.mQRCodeScannerPreferenceObserver = new HashMap<>();
            this.mSecureSettings.putStringForUser("show_qr_code_scanner_setting", (String) null, this.mUserTracker.getUserId());
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterQRCodePreferenceObserver$0(Integer num, ContentObserver contentObserver) {
        this.mSecureSettings.unregisterContentObserver(contentObserver);
    }

    public final void unregisterDefaultQRCodeScannerObserver() {
        this.mDeviceConfigProxy.removeOnPropertiesChangedListener(this.mOnDefaultQRCodeScannerChangedListener);
        this.mOnDefaultQRCodeScannerChangedListener = null;
        this.mQRCodeScannerActivity = null;
        this.mIntent = null;
        this.mComponentName = null;
    }

    public final void notifyQRCodeScannerActivityChanged() {
        ArrayList arrayList;
        synchronized (this.mCallbacks) {
            arrayList = (ArrayList) this.mCallbacks.clone();
        }
        arrayList.forEach(new QRCodeScannerController$$ExternalSyntheticLambda5());
    }

    public final void notifyQRCodeScannerPreferenceChanged() {
        ArrayList arrayList;
        synchronized (this.mCallbacks) {
            arrayList = (ArrayList) this.mCallbacks.clone();
        }
        arrayList.forEach(new QRCodeScannerController$$ExternalSyntheticLambda4());
    }

    public final void registerDefaultQRCodeScannerObserver() {
        if (this.mOnDefaultQRCodeScannerChangedListener == null) {
            this.mExecutor.execute(new QRCodeScannerController$$ExternalSyntheticLambda1(this));
            QRCodeScannerController$$ExternalSyntheticLambda2 qRCodeScannerController$$ExternalSyntheticLambda2 = new QRCodeScannerController$$ExternalSyntheticLambda2(this);
            this.mOnDefaultQRCodeScannerChangedListener = qRCodeScannerController$$ExternalSyntheticLambda2;
            this.mDeviceConfigProxy.addOnPropertiesChangedListener("systemui", this.mExecutor, qRCodeScannerController$$ExternalSyntheticLambda2);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerDefaultQRCodeScannerObserver$4(DeviceConfig.Properties properties) {
        if ("systemui".equals(properties.getNamespace()) && properties.getKeyset().contains("default_qr_code_scanner")) {
            lambda$registerDefaultQRCodeScannerObserver$3();
            updateQRCodeScannerPreferenceDetails(true);
        }
    }

    public final void registerQRCodePreferenceObserver() {
        if (this.mConfigEnableLockScreenButton) {
            int userId = this.mUserTracker.getUserId();
            if (this.mQRCodeScannerPreferenceObserver.getOrDefault(Integer.valueOf(userId), (Object) null) == null) {
                this.mExecutor.execute(new QRCodeScannerController$$ExternalSyntheticLambda3(this));
                this.mQRCodeScannerPreferenceObserver.put(Integer.valueOf(userId), new ContentObserver((Handler) null) {
                    public void onChange(boolean z) {
                        QRCodeScannerController.this.mExecutor.execute(new QRCodeScannerController$1$$ExternalSyntheticLambda0(this));
                    }

                    /* access modifiers changed from: private */
                    public /* synthetic */ void lambda$onChange$0() {
                        QRCodeScannerController.this.updateQRCodeScannerPreferenceDetails(false);
                    }
                });
                SecureSettings secureSettings = this.mSecureSettings;
                secureSettings.registerContentObserverForUser(secureSettings.getUriFor("lock_screen_show_qr_code_scanner"), false, this.mQRCodeScannerPreferenceObserver.get(Integer.valueOf(userId)), userId);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$registerQRCodePreferenceObserver$5() {
        updateQRCodeScannerPreferenceDetails(true);
    }

    public final void registerUserChangeObservers() {
        if (this.mUserChangedListener == null) {
            AnonymousClass2 r0 = new UserTracker.Callback() {
                public void onUserChanged(int i, Context context) {
                    QRCodeScannerController.this.registerQRCodePreferenceObserver();
                    QRCodeScannerController.this.updateQRCodeScannerPreferenceDetails(true);
                }
            };
            this.mUserChangedListener = r0;
            this.mUserTracker.addCallback(r0, this.mExecutor);
        }
    }
}
