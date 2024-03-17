package com.android.systemui.statusbar.phone;

import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.app.IActivityTaskManager;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.quickaccesswallet.GetWalletCardsError;
import android.service.quickaccesswallet.GetWalletCardsResponse;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.EmergencyCarrierArea;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.Dependency;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.camera.CameraIntents;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.doze.util.BurnInHelperKt;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.IntentButtonProvider;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import com.android.systemui.statusbar.KeyguardAffordanceView;
import com.android.systemui.statusbar.policy.AccessibilityController;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.FlashlightController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.PreviewInflater;
import com.android.systemui.tuner.LockscreenFragment;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import java.util.List;
import java.util.concurrent.Executor;

public class KeyguardBottomAreaView extends FrameLayout implements View.OnClickListener, KeyguardStateController.Callback, AccessibilityController.AccessibilityStateChangedCallback {
    public static final Intent PHONE_INTENT = new Intent("android.intent.action.DIAL");
    public AccessibilityController mAccessibilityController;
    public View.AccessibilityDelegate mAccessibilityDelegate;
    public ActivityIntentHelper mActivityIntentHelper;
    public ActivityStarter mActivityStarter;
    public KeyguardAffordanceHelper mAffordanceHelper;
    public View mAmbientIndicationArea;
    public int mBurnInXOffset;
    public int mBurnInYOffset;
    public View mCameraPreview;
    public WalletCardRetriever mCardRetriever;
    public CentralSurfaces mCentralSurfaces;
    public boolean mControlServicesAvailable;
    public ImageView mControlsButton;
    public ControlsComponent mControlsComponent;
    public float mDarkAmount;
    public final BroadcastReceiver mDevicePolicyReceiver;
    public boolean mDozing;
    public EmergencyCarrierArea mEmergencyCarrierArea;
    public FalsingManager mFalsingManager;
    public FlashlightController mFlashlightController;
    public boolean mHasCard;
    public ViewGroup mIndicationArea;
    public int mIndicationBottomMargin;
    public int mIndicationPadding;
    public TextView mIndicationText;
    public TextView mIndicationTextBottom;
    public KeyguardStateController mKeyguardStateController;
    public KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public KeyguardAffordanceView mLeftAffordanceView;
    public Drawable mLeftAssistIcon;
    public IntentButtonProvider.IntentButton mLeftButton;
    public String mLeftButtonStr;
    public ExtensionController.Extension<IntentButtonProvider.IntentButton> mLeftExtension;
    public boolean mLeftIsVoiceAssist;
    public View mLeftPreview;
    public ControlsListingController.ControlsListingCallback mListingCallback;
    public ViewGroup mOverlayContainer;
    public ViewGroup mPreviewContainer;
    public PreviewInflater mPreviewInflater;
    public boolean mPrewarmBound;
    public final ServiceConnection mPrewarmConnection;
    public Messenger mPrewarmMessenger;
    public ImageView mQRCodeScannerButton;
    public QRCodeScannerController mQRCodeScannerController;
    public QuickAccessWalletController mQuickAccessWalletController;
    public KeyguardAffordanceView mRightAffordanceView;
    public IntentButtonProvider.IntentButton mRightButton;
    public String mRightButtonStr;
    public ExtensionController.Extension<IntentButtonProvider.IntentButton> mRightExtension;
    public final boolean mShowCameraAffordance;
    public final boolean mShowLeftAffordance;
    public final KeyguardUpdateMonitorCallback mUpdateMonitorCallback;
    public boolean mUserSetupComplete;
    public ImageView mWalletButton;

    public static boolean isSuccessfulLaunch(int i) {
        return i == 0 || i == 3 || i == 2;
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    public KeyguardBottomAreaView(Context context) {
        this(context, (AttributeSet) null);
    }

    public KeyguardBottomAreaView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyguardBottomAreaView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public KeyguardBottomAreaView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mHasCard = false;
        this.mCardRetriever = new WalletCardRetriever();
        this.mControlServicesAvailable = false;
        this.mPrewarmConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                KeyguardBottomAreaView.this.mPrewarmMessenger = new Messenger(iBinder);
            }

            public void onServiceDisconnected(ComponentName componentName) {
                KeyguardBottomAreaView.this.mPrewarmMessenger = null;
            }
        };
        this.mRightButton = new DefaultRightButton();
        this.mLeftButton = new DefaultLeftButton();
        this.mListingCallback = new ControlsListingController.ControlsListingCallback() {
            public void onServicesUpdated(List<ControlsServiceInfo> list) {
                KeyguardBottomAreaView.this.post(new KeyguardBottomAreaView$2$$ExternalSyntheticLambda0(this, list));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onServicesUpdated$0(List list) {
                boolean z = !list.isEmpty();
                if (z != KeyguardBottomAreaView.this.mControlServicesAvailable) {
                    KeyguardBottomAreaView.this.mControlServicesAvailable = z;
                    KeyguardBottomAreaView.this.updateControlsVisibility();
                    KeyguardBottomAreaView.this.updateAffordanceColors();
                }
            }
        };
        this.mAccessibilityDelegate = new View.AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                String str;
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                if (view == KeyguardBottomAreaView.this.mRightAffordanceView) {
                    str = KeyguardBottomAreaView.this.getResources().getString(R$string.camera_label);
                } else if (view == KeyguardBottomAreaView.this.mLeftAffordanceView) {
                    str = KeyguardBottomAreaView.this.mLeftIsVoiceAssist ? KeyguardBottomAreaView.this.getResources().getString(R$string.voice_assist_label) : KeyguardBottomAreaView.this.getResources().getString(R$string.phone_label);
                } else {
                    str = null;
                }
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, str));
            }

            public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                if (i == 16) {
                    if (view == KeyguardBottomAreaView.this.mRightAffordanceView) {
                        KeyguardBottomAreaView.this.launchCamera("lockscreen_affordance");
                        return true;
                    } else if (view == KeyguardBottomAreaView.this.mLeftAffordanceView) {
                        KeyguardBottomAreaView.this.launchLeftAffordance();
                        return true;
                    }
                }
                return super.performAccessibilityAction(view, i, bundle);
            }
        };
        this.mDevicePolicyReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                KeyguardBottomAreaView.this.post(new Runnable() {
                    public void run() {
                        KeyguardBottomAreaView.this.updateCameraVisibility();
                    }
                });
            }
        };
        this.mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
            public void onUserSwitchComplete(int i) {
                KeyguardBottomAreaView.this.updateCameraVisibility();
            }

            public void onUserUnlocked() {
                KeyguardBottomAreaView.this.inflateCameraPreview();
                KeyguardBottomAreaView.this.updateCameraVisibility();
                KeyguardBottomAreaView.this.updateLeftAffordance();
            }
        };
        this.mShowLeftAffordance = getResources().getBoolean(R$bool.config_keyguardShowLeftAffordance);
        this.mShowCameraAffordance = getResources().getBoolean(R$bool.config_keyguardShowCameraAffordance);
    }

    public void initFrom(KeyguardBottomAreaView keyguardBottomAreaView) {
        setCentralSurfaces(keyguardBottomAreaView.mCentralSurfaces);
        if (this.mAmbientIndicationArea != null) {
            View findViewById = keyguardBottomAreaView.findViewById(R$id.ambient_indication_container);
            ((ViewGroup) findViewById.getParent()).removeView(findViewById);
            ViewGroup viewGroup = (ViewGroup) this.mAmbientIndicationArea.getParent();
            int indexOfChild = viewGroup.indexOfChild(this.mAmbientIndicationArea);
            viewGroup.removeView(this.mAmbientIndicationArea);
            viewGroup.addView(findViewById, indexOfChild);
            this.mAmbientIndicationArea = findViewById;
            dozeTimeTick();
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mPreviewInflater = new PreviewInflater(this.mContext, new LockPatternUtils(this.mContext), new ActivityIntentHelper(this.mContext));
        this.mEmergencyCarrierArea = (EmergencyCarrierArea) findViewById(R$id.keyguard_selector_fade_container);
        this.mOverlayContainer = (ViewGroup) findViewById(R$id.overlay_container);
        this.mRightAffordanceView = (KeyguardAffordanceView) findViewById(R$id.camera_button);
        this.mLeftAffordanceView = (KeyguardAffordanceView) findViewById(R$id.left_button);
        this.mWalletButton = (ImageView) findViewById(R$id.wallet_button);
        this.mQRCodeScannerButton = (ImageView) findViewById(R$id.qr_code_scanner_button);
        this.mControlsButton = (ImageView) findViewById(R$id.controls_button);
        this.mIndicationArea = (ViewGroup) findViewById(R$id.keyguard_indication_area);
        this.mAmbientIndicationArea = findViewById(R$id.ambient_indication_container);
        this.mIndicationText = (TextView) findViewById(R$id.keyguard_indication_text);
        this.mIndicationTextBottom = (TextView) findViewById(R$id.keyguard_indication_text_bottom);
        this.mIndicationBottomMargin = getResources().getDimensionPixelSize(R$dimen.keyguard_indication_margin_bottom);
        this.mBurnInYOffset = getResources().getDimensionPixelSize(R$dimen.default_burn_in_prevention_offset);
        updateCameraVisibility();
        KeyguardStateController keyguardStateController = (KeyguardStateController) Dependency.get(KeyguardStateController.class);
        this.mKeyguardStateController = keyguardStateController;
        keyguardStateController.addCallback(this);
        setClipChildren(false);
        setClipToPadding(false);
        this.mRightAffordanceView.setOnClickListener(this);
        this.mLeftAffordanceView.setOnClickListener(this);
        initAccessibility();
        this.mActivityStarter = (ActivityStarter) Dependency.get(ActivityStarter.class);
        this.mFlashlightController = (FlashlightController) Dependency.get(FlashlightController.class);
        this.mAccessibilityController = (AccessibilityController) Dependency.get(AccessibilityController.class);
        this.mActivityIntentHelper = new ActivityIntentHelper(getContext());
        this.mIndicationPadding = getResources().getDimensionPixelSize(R$dimen.keyguard_indication_area_padding);
        updateWalletVisibility();
        updateQRCodeButtonVisibility();
        updateControlsVisibility();
    }

    public void setPreviewContainer(ViewGroup viewGroup) {
        this.mPreviewContainer = viewGroup;
        inflateCameraPreview();
        updateLeftAffordance();
    }

    public void onAttachedToWindow() {
        Class<IntentButtonProvider> cls = IntentButtonProvider.class;
        Class<IntentButtonProvider.IntentButton> cls2 = IntentButtonProvider.IntentButton.class;
        Class cls3 = ExtensionController.class;
        super.onAttachedToWindow();
        this.mAccessibilityController.addStateChangedCallback(this);
        this.mRightExtension = ((ExtensionController) Dependency.get(cls3)).newExtension(cls2).withPlugin(cls, "com.android.systemui.action.PLUGIN_LOCKSCREEN_RIGHT_BUTTON", new KeyguardBottomAreaView$$ExternalSyntheticLambda0()).withTunerFactory(new LockscreenFragment.LockButtonFactory(this.mContext, "sysui_keyguard_right")).withDefault(new KeyguardBottomAreaView$$ExternalSyntheticLambda1(this)).withCallback(new KeyguardBottomAreaView$$ExternalSyntheticLambda2(this)).build();
        this.mLeftExtension = ((ExtensionController) Dependency.get(cls3)).newExtension(cls2).withPlugin(cls, "com.android.systemui.action.PLUGIN_LOCKSCREEN_LEFT_BUTTON", new KeyguardBottomAreaView$$ExternalSyntheticLambda3()).withTunerFactory(new LockscreenFragment.LockButtonFactory(this.mContext, "sysui_keyguard_left")).withDefault(new KeyguardBottomAreaView$$ExternalSyntheticLambda4(this)).withCallback(new KeyguardBottomAreaView$$ExternalSyntheticLambda5(this)).build();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
        getContext().registerReceiverAsUser(this.mDevicePolicyReceiver, UserHandle.ALL, intentFilter, (String) null, (Handler) null);
        KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        keyguardUpdateMonitor.registerCallback(this.mUpdateMonitorCallback);
        this.mKeyguardStateController.addCallback(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ IntentButtonProvider.IntentButton lambda$onAttachedToWindow$1() {
        return new DefaultRightButton();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ IntentButtonProvider.IntentButton lambda$onAttachedToWindow$4() {
        return new DefaultLeftButton();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mKeyguardStateController.removeCallback(this);
        this.mAccessibilityController.removeStateChangedCallback(this);
        this.mRightExtension.destroy();
        this.mLeftExtension.destroy();
        getContext().unregisterReceiver(this.mDevicePolicyReceiver);
        this.mKeyguardUpdateMonitor.removeCallback(this.mUpdateMonitorCallback);
        QuickAccessWalletController quickAccessWalletController = this.mQuickAccessWalletController;
        if (quickAccessWalletController != null) {
            quickAccessWalletController.unregisterWalletChangeObservers(QuickAccessWalletController.WalletChangeEvent.WALLET_PREFERENCE_CHANGE, QuickAccessWalletController.WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE);
        }
        QRCodeScannerController qRCodeScannerController = this.mQRCodeScannerController;
        if (qRCodeScannerController != null) {
            qRCodeScannerController.unregisterQRCodeScannerChangeObservers(0, 1);
        }
        ControlsComponent controlsComponent = this.mControlsComponent;
        if (controlsComponent != null) {
            controlsComponent.getControlsListingController().ifPresent(new KeyguardBottomAreaView$$ExternalSyntheticLambda6(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onDetachedFromWindow$6(ControlsListingController controlsListingController) {
        controlsListingController.removeCallback(this.mListingCallback);
    }

    public final void initAccessibility() {
        this.mLeftAffordanceView.setAccessibilityDelegate(this.mAccessibilityDelegate);
        this.mRightAffordanceView.setAccessibilityDelegate(this.mAccessibilityDelegate);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mIndicationBottomMargin = getResources().getDimensionPixelSize(R$dimen.keyguard_indication_margin_bottom);
        this.mBurnInYOffset = getResources().getDimensionPixelSize(R$dimen.default_burn_in_prevention_offset);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mIndicationArea.getLayoutParams();
        int i = marginLayoutParams.bottomMargin;
        int i2 = this.mIndicationBottomMargin;
        if (i != i2) {
            marginLayoutParams.bottomMargin = i2;
            this.mIndicationArea.setLayoutParams(marginLayoutParams);
        }
        this.mIndicationTextBottom.setTextSize(0, (float) getResources().getDimensionPixelSize(17105582));
        this.mIndicationText.setTextSize(0, (float) getResources().getDimensionPixelSize(17105582));
        ViewGroup.LayoutParams layoutParams = this.mRightAffordanceView.getLayoutParams();
        Resources resources = getResources();
        int i3 = R$dimen.keyguard_affordance_width;
        layoutParams.width = resources.getDimensionPixelSize(i3);
        Resources resources2 = getResources();
        int i4 = R$dimen.keyguard_affordance_height;
        layoutParams.height = resources2.getDimensionPixelSize(i4);
        this.mRightAffordanceView.setLayoutParams(layoutParams);
        updateRightAffordanceIcon();
        ViewGroup.LayoutParams layoutParams2 = this.mLeftAffordanceView.getLayoutParams();
        layoutParams2.width = getResources().getDimensionPixelSize(i3);
        layoutParams2.height = getResources().getDimensionPixelSize(i4);
        this.mLeftAffordanceView.setLayoutParams(layoutParams2);
        updateLeftAffordanceIcon();
        ViewGroup.LayoutParams layoutParams3 = this.mWalletButton.getLayoutParams();
        Resources resources3 = getResources();
        int i5 = R$dimen.keyguard_affordance_fixed_width;
        layoutParams3.width = resources3.getDimensionPixelSize(i5);
        Resources resources4 = getResources();
        int i6 = R$dimen.keyguard_affordance_fixed_height;
        layoutParams3.height = resources4.getDimensionPixelSize(i6);
        this.mWalletButton.setLayoutParams(layoutParams3);
        ViewGroup.LayoutParams layoutParams4 = this.mQRCodeScannerButton.getLayoutParams();
        layoutParams4.width = getResources().getDimensionPixelSize(i5);
        layoutParams4.height = getResources().getDimensionPixelSize(i6);
        this.mQRCodeScannerButton.setLayoutParams(layoutParams4);
        ViewGroup.LayoutParams layoutParams5 = this.mControlsButton.getLayoutParams();
        layoutParams5.width = getResources().getDimensionPixelSize(i5);
        layoutParams5.height = getResources().getDimensionPixelSize(i6);
        this.mControlsButton.setLayoutParams(layoutParams5);
        this.mIndicationPadding = getResources().getDimensionPixelSize(R$dimen.keyguard_indication_area_padding);
        updateWalletVisibility();
        updateQRCodeButtonVisibility();
        updateAffordanceColors();
    }

    public final void updateRightAffordanceIcon() {
        IntentButtonProvider.IntentButton.IconState icon = this.mRightButton.getIcon();
        this.mRightAffordanceView.setVisibility((this.mDozing || !icon.isVisible) ? 8 : 0);
        if (!(icon.drawable == this.mRightAffordanceView.getDrawable() && icon.tint == this.mRightAffordanceView.shouldTint())) {
            this.mRightAffordanceView.setImageDrawable(icon.drawable, icon.tint);
        }
        this.mRightAffordanceView.setContentDescription(icon.contentDescription);
    }

    public void setCentralSurfaces(CentralSurfaces centralSurfaces) {
        this.mCentralSurfaces = centralSurfaces;
        updateCameraVisibility();
    }

    public void setAffordanceHelper(KeyguardAffordanceHelper keyguardAffordanceHelper) {
        this.mAffordanceHelper = keyguardAffordanceHelper;
    }

    public void setUserSetupComplete(boolean z) {
        this.mUserSetupComplete = z;
        updateCameraVisibility();
        updateLeftAffordanceIcon();
    }

    public final Intent getCameraIntent() {
        return this.mRightButton.getIntent();
    }

    public ResolveInfo resolveCameraIntent() {
        return this.mContext.getPackageManager().resolveActivityAsUser(getCameraIntent(), 65536, KeyguardUpdateMonitor.getCurrentUser());
    }

    public final void updateCameraVisibility() {
        KeyguardAffordanceView keyguardAffordanceView = this.mRightAffordanceView;
        if (keyguardAffordanceView != null) {
            keyguardAffordanceView.setVisibility((this.mDozing || !this.mShowCameraAffordance || !this.mRightButton.getIcon().isVisible) ? 8 : 0);
        }
    }

    public final void updateLeftAffordanceIcon() {
        int i = 8;
        if (!this.mShowLeftAffordance || this.mDozing) {
            this.mLeftAffordanceView.setVisibility(8);
            return;
        }
        IntentButtonProvider.IntentButton.IconState icon = this.mLeftButton.getIcon();
        KeyguardAffordanceView keyguardAffordanceView = this.mLeftAffordanceView;
        if (icon.isVisible) {
            i = 0;
        }
        keyguardAffordanceView.setVisibility(i);
        if (!(icon.drawable == this.mLeftAffordanceView.getDrawable() && icon.tint == this.mLeftAffordanceView.shouldTint())) {
            this.mLeftAffordanceView.setImageDrawable(icon.drawable, icon.tint);
        }
        this.mLeftAffordanceView.setContentDescription(icon.contentDescription);
    }

    public final void updateWalletVisibility() {
        QuickAccessWalletController quickAccessWalletController;
        if (this.mDozing || (quickAccessWalletController = this.mQuickAccessWalletController) == null || !quickAccessWalletController.isWalletEnabled() || !this.mHasCard) {
            this.mWalletButton.setVisibility(8);
            if (this.mControlsButton.getVisibility() == 8) {
                this.mIndicationArea.setPadding(0, 0, 0, 0);
                return;
            }
            return;
        }
        this.mWalletButton.setVisibility(0);
        this.mWalletButton.setOnClickListener(new KeyguardBottomAreaView$$ExternalSyntheticLambda7(this));
        ViewGroup viewGroup = this.mIndicationArea;
        int i = this.mIndicationPadding;
        viewGroup.setPadding(i, 0, i, 0);
    }

    public final void updateControlsVisibility() {
        ControlsComponent controlsComponent = this.mControlsComponent;
        if (controlsComponent != null) {
            this.mControlsButton.setImageResource(controlsComponent.getTileImageId());
            this.mControlsButton.setContentDescription(getContext().getString(this.mControlsComponent.getTileTitleId()));
            updateAffordanceColors();
            boolean booleanValue = ((Boolean) this.mControlsComponent.getControlsController().map(new KeyguardBottomAreaView$$ExternalSyntheticLambda10()).orElse(Boolean.FALSE)).booleanValue();
            if (this.mDozing || !booleanValue || !this.mControlServicesAvailable || this.mControlsComponent.getVisibility() != ControlsComponent.Visibility.AVAILABLE) {
                this.mControlsButton.setVisibility(8);
                if (this.mWalletButton.getVisibility() == 8) {
                    this.mIndicationArea.setPadding(0, 0, 0, 0);
                    return;
                }
                return;
            }
            this.mControlsButton.setVisibility(0);
            this.mControlsButton.setOnClickListener(new KeyguardBottomAreaView$$ExternalSyntheticLambda11(this));
            ViewGroup viewGroup = this.mIndicationArea;
            int i = this.mIndicationPadding;
            viewGroup.setPadding(i, 0, i, 0);
        }
    }

    public static /* synthetic */ Boolean lambda$updateControlsVisibility$7(ControlsController controlsController) {
        return Boolean.valueOf(controlsController.getFavorites().size() > 0);
    }

    public boolean isLeftVoiceAssist() {
        return this.mLeftIsVoiceAssist;
    }

    public final boolean isPhoneVisible() {
        PackageManager packageManager = this.mContext.getPackageManager();
        if (!packageManager.hasSystemFeature("android.hardware.telephony") || packageManager.resolveActivity(PHONE_INTENT, 0) == null) {
            return false;
        }
        return true;
    }

    public void onStateChanged(boolean z, boolean z2) {
        this.mRightAffordanceView.setClickable(z2);
        this.mLeftAffordanceView.setClickable(z2);
        this.mRightAffordanceView.setFocusable(z);
        this.mLeftAffordanceView.setFocusable(z);
    }

    public void onClick(View view) {
        if (view == this.mRightAffordanceView) {
            launchCamera("lockscreen_affordance");
        } else if (view == this.mLeftAffordanceView) {
            launchLeftAffordance();
        }
    }

    public void bindCameraPrewarmService() {
        Bundle bundle;
        String string;
        ActivityInfo targetActivityInfo = this.mActivityIntentHelper.getTargetActivityInfo(getCameraIntent(), KeyguardUpdateMonitor.getCurrentUser(), true);
        if (targetActivityInfo != null && (bundle = targetActivityInfo.metaData) != null && (string = bundle.getString("android.media.still_image_camera_preview_service")) != null) {
            Intent intent = new Intent();
            intent.setClassName(targetActivityInfo.packageName, string);
            intent.setAction("android.service.media.CameraPrewarmService.ACTION_PREWARM");
            try {
                if (getContext().bindServiceAsUser(intent, this.mPrewarmConnection, 67108865, new UserHandle(-2))) {
                    this.mPrewarmBound = true;
                }
            } catch (SecurityException e) {
                Log.w("CentralSurfaces/KeyguardBottomAreaView", "Unable to bind to prewarm service package=" + targetActivityInfo.packageName + " class=" + string, e);
            }
        }
    }

    public void unbindCameraPrewarmService(boolean z) {
        if (this.mPrewarmBound) {
            Messenger messenger = this.mPrewarmMessenger;
            if (messenger != null && z) {
                try {
                    messenger.send(Message.obtain((Handler) null, 1));
                } catch (RemoteException e) {
                    Log.w("CentralSurfaces/KeyguardBottomAreaView", "Error sending camera fired message", e);
                }
            }
            this.mContext.unbindService(this.mPrewarmConnection);
            this.mPrewarmBound = false;
        }
    }

    public void launchCamera(String str) {
        final Intent cameraIntent = getCameraIntent();
        cameraIntent.putExtra("com.android.systemui.camera_launch_source", str);
        boolean wouldLaunchResolverActivity = this.mActivityIntentHelper.wouldLaunchResolverActivity(cameraIntent, KeyguardUpdateMonitor.getCurrentUser());
        if (!CameraIntents.isSecureCameraIntent(cameraIntent) || wouldLaunchResolverActivity) {
            this.mActivityStarter.startActivity(cameraIntent, false, (ActivityStarter.Callback) new ActivityStarter.Callback() {
                public void onActivityStarted(int i) {
                    KeyguardBottomAreaView.this.unbindCameraPrewarmService(KeyguardBottomAreaView.isSuccessfulLaunch(i));
                }
            });
        } else {
            AsyncTask.execute(new Runnable() {
                public void run() {
                    int i;
                    ActivityOptions makeBasic = ActivityOptions.makeBasic();
                    makeBasic.setDisallowEnterPictureInPictureWhileLaunching(true);
                    makeBasic.setRotationAnimationHint(3);
                    try {
                        IActivityTaskManager service = ActivityTaskManager.getService();
                        String basePackageName = KeyguardBottomAreaView.this.getContext().getBasePackageName();
                        String attributionTag = KeyguardBottomAreaView.this.getContext().getAttributionTag();
                        Intent intent = cameraIntent;
                        i = service.startActivityAsUser((IApplicationThread) null, basePackageName, attributionTag, intent, intent.resolveTypeIfNeeded(KeyguardBottomAreaView.this.getContext().getContentResolver()), (IBinder) null, (String) null, 0, 268435456, (ProfilerInfo) null, makeBasic.toBundle(), UserHandle.CURRENT.getIdentifier());
                    } catch (RemoteException e) {
                        Log.w("CentralSurfaces/KeyguardBottomAreaView", "Unable to start camera activity", e);
                        i = -96;
                    }
                    final boolean r0 = KeyguardBottomAreaView.isSuccessfulLaunch(i);
                    KeyguardBottomAreaView.this.post(new Runnable() {
                        public void run() {
                            KeyguardBottomAreaView.this.unbindCameraPrewarmService(r0);
                        }
                    });
                }
            });
        }
    }

    public void setDarkAmount(float f) {
        if (f != this.mDarkAmount) {
            this.mDarkAmount = f;
            dozeTimeTick();
        }
    }

    public void launchLeftAffordance() {
        if (this.mLeftIsVoiceAssist) {
            launchVoiceAssist();
        } else {
            launchPhone();
        }
    }

    @VisibleForTesting
    public void launchVoiceAssist() {
        AnonymousClass6 r1 = new Runnable() {
            public void run() {
                ((AssistManager) Dependency.get(AssistManager.class)).launchVoiceAssistFromKeyguard();
            }
        };
        if (!this.mKeyguardStateController.canDismissLockScreen()) {
            ((Executor) Dependency.get(Dependency.BACKGROUND_EXECUTOR)).execute(r1);
        } else {
            this.mCentralSurfaces.executeRunnableDismissingKeyguard(r1, (Runnable) null, !TextUtils.isEmpty(this.mRightButtonStr) && ((TunerService) Dependency.get(TunerService.class)).getValue("sysui_keyguard_right_unlock", 1) != 0, false, true);
        }
    }

    public final boolean canLaunchVoiceAssist() {
        return ((AssistManager) Dependency.get(AssistManager.class)).canVoiceAssistBeLaunchedFromKeyguard();
    }

    public final void launchPhone() {
        final TelecomManager from = TelecomManager.from(this.mContext);
        if (from.isInCall()) {
            AsyncTask.execute(new Runnable() {
                public void run() {
                    from.showInCallScreen(false);
                }
            });
            return;
        }
        boolean z = true;
        if (TextUtils.isEmpty(this.mLeftButtonStr) || ((TunerService) Dependency.get(TunerService.class)).getValue("sysui_keyguard_left_unlock", 1) == 0) {
            z = false;
        }
        this.mActivityStarter.startActivity(this.mLeftButton.getIntent(), z);
    }

    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (view == this && i == 0) {
            updateCameraVisibility();
        }
    }

    public KeyguardAffordanceView getLeftView() {
        return this.mLeftAffordanceView;
    }

    public KeyguardAffordanceView getRightView() {
        return this.mRightAffordanceView;
    }

    public View getLeftPreview() {
        return this.mLeftPreview;
    }

    public View getRightPreview() {
        return this.mCameraPreview;
    }

    public View getIndicationArea() {
        return this.mIndicationArea;
    }

    public void onUnlockedChanged() {
        updateCameraVisibility();
    }

    public void onKeyguardShowingChanged() {
        QuickAccessWalletController quickAccessWalletController;
        if (this.mKeyguardStateController.isShowing() && (quickAccessWalletController = this.mQuickAccessWalletController) != null) {
            quickAccessWalletController.queryWalletCards(this.mCardRetriever);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0024  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void inflateCameraPreview() {
        /*
            r4 = this;
            android.view.ViewGroup r0 = r4.mPreviewContainer
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            android.view.View r1 = r4.mCameraPreview
            r2 = 0
            if (r1 == 0) goto L_0x0015
            r0.removeView(r1)
            int r0 = r1.getVisibility()
            if (r0 != 0) goto L_0x0015
            r0 = 1
            goto L_0x0016
        L_0x0015:
            r0 = r2
        L_0x0016:
            com.android.systemui.statusbar.policy.PreviewInflater r1 = r4.mPreviewInflater
            android.content.Intent r3 = r4.getCameraIntent()
            android.view.View r1 = r1.inflatePreview((android.content.Intent) r3)
            r4.mCameraPreview = r1
            if (r1 == 0) goto L_0x0032
            android.view.ViewGroup r3 = r4.mPreviewContainer
            r3.addView(r1)
            android.view.View r1 = r4.mCameraPreview
            if (r0 == 0) goto L_0x002e
            goto L_0x002f
        L_0x002e:
            r2 = 4
        L_0x002f:
            r1.setVisibility(r2)
        L_0x0032:
            com.android.systemui.statusbar.phone.KeyguardAffordanceHelper r4 = r4.mAffordanceHelper
            if (r4 == 0) goto L_0x0039
            r4.updatePreviews()
        L_0x0039:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.KeyguardBottomAreaView.inflateCameraPreview():void");
    }

    public final void updateLeftPreview() {
        Class cls = AssistManager.class;
        ViewGroup viewGroup = this.mPreviewContainer;
        if (viewGroup != null) {
            View view = this.mLeftPreview;
            if (view != null) {
                viewGroup.removeView(view);
            }
            if (!this.mLeftIsVoiceAssist) {
                this.mLeftPreview = this.mPreviewInflater.inflatePreview(this.mLeftButton.getIntent());
            } else if (((AssistManager) Dependency.get(cls)).getVoiceInteractorComponentName() != null) {
                this.mLeftPreview = this.mPreviewInflater.inflatePreviewFromService(((AssistManager) Dependency.get(cls)).getVoiceInteractorComponentName());
            }
            View view2 = this.mLeftPreview;
            if (view2 != null) {
                this.mPreviewContainer.addView(view2);
                this.mLeftPreview.setVisibility(4);
            }
            KeyguardAffordanceHelper keyguardAffordanceHelper = this.mAffordanceHelper;
            if (keyguardAffordanceHelper != null) {
                keyguardAffordanceHelper.updatePreviews();
            }
        }
    }

    public void startFinishDozeAnimation() {
        long j = 0;
        if (this.mWalletButton.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mWalletButton, 0);
        }
        if (this.mQRCodeScannerButton.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mQRCodeScannerButton, 0);
        }
        if (this.mControlsButton.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mControlsButton, 0);
        }
        if (this.mLeftAffordanceView.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mLeftAffordanceView, 0);
            j = 48;
        }
        if (this.mRightAffordanceView.getVisibility() == 0) {
            startFinishDozeAnimationElement(this.mRightAffordanceView, j);
        }
    }

    public final void startFinishDozeAnimationElement(View view, long j) {
        view.setAlpha(0.0f);
        view.setTranslationY((float) (view.getHeight() / 2));
        view.animate().alpha(1.0f).translationY(0.0f).setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN).setStartDelay(j).setDuration(250);
    }

    public void updateLeftAffordance() {
        updateLeftAffordanceIcon();
        updateLeftPreview();
    }

    /* renamed from: setRightButton */
    public final void lambda$onAttachedToWindow$2(IntentButtonProvider.IntentButton intentButton) {
        this.mRightButton = intentButton;
        updateRightAffordanceIcon();
        updateCameraVisibility();
        inflateCameraPreview();
    }

    /* renamed from: setLeftButton */
    public final void lambda$onAttachedToWindow$5(IntentButtonProvider.IntentButton intentButton) {
        this.mLeftButton = intentButton;
        if (!(intentButton instanceof DefaultLeftButton)) {
            this.mLeftIsVoiceAssist = false;
        }
        updateLeftAffordance();
    }

    public void setDozing(boolean z, boolean z2) {
        this.mDozing = z;
        updateCameraVisibility();
        updateLeftAffordanceIcon();
        updateWalletVisibility();
        updateControlsVisibility();
        updateQRCodeButtonVisibility();
        if (z) {
            this.mOverlayContainer.setVisibility(4);
            this.mEmergencyCarrierArea.setVisibility(4);
            return;
        }
        this.mOverlayContainer.setVisibility(0);
        this.mEmergencyCarrierArea.setVisibility(0);
        if (z2) {
            startFinishDozeAnimation();
        }
    }

    public void dozeTimeTick() {
        float burnInOffset = (float) (BurnInHelperKt.getBurnInOffset(this.mBurnInYOffset * 2, false) - this.mBurnInYOffset);
        this.mIndicationArea.setTranslationY(this.mDarkAmount * burnInOffset);
        View view = this.mAmbientIndicationArea;
        if (view != null) {
            view.setTranslationY(burnInOffset * this.mDarkAmount);
        }
    }

    public void setAntiBurnInOffsetX(int i) {
        if (this.mBurnInXOffset != i) {
            this.mBurnInXOffset = i;
            float f = (float) i;
            this.mIndicationArea.setTranslationX(f);
            View view = this.mAmbientIndicationArea;
            if (view != null) {
                view.setTranslationX(f);
            }
        }
    }

    public void setAffordanceAlpha(float f) {
        this.mLeftAffordanceView.setAlpha(f);
        this.mRightAffordanceView.setAlpha(f);
        this.mIndicationArea.setAlpha(f);
        this.mWalletButton.setAlpha(f);
        this.mQRCodeScannerButton.setAlpha(f);
        this.mControlsButton.setAlpha(f);
        this.mEmergencyCarrierArea.setAlpha(f);
    }

    public class DefaultLeftButton implements IntentButtonProvider.IntentButton {
        public IntentButtonProvider.IntentButton.IconState mIconState;

        public DefaultLeftButton() {
            this.mIconState = new IntentButtonProvider.IntentButton.IconState();
        }

        public IntentButtonProvider.IntentButton.IconState getIcon() {
            KeyguardBottomAreaView keyguardBottomAreaView = KeyguardBottomAreaView.this;
            keyguardBottomAreaView.mLeftIsVoiceAssist = keyguardBottomAreaView.canLaunchVoiceAssist();
            boolean z = true;
            if (KeyguardBottomAreaView.this.mLeftIsVoiceAssist) {
                IntentButtonProvider.IntentButton.IconState iconState = this.mIconState;
                if (!KeyguardBottomAreaView.this.mUserSetupComplete || !KeyguardBottomAreaView.this.mShowLeftAffordance) {
                    z = false;
                }
                iconState.isVisible = z;
                if (KeyguardBottomAreaView.this.mLeftAssistIcon == null) {
                    this.mIconState.drawable = KeyguardBottomAreaView.this.mContext.getDrawable(R$drawable.ic_mic_26dp);
                } else {
                    this.mIconState.drawable = KeyguardBottomAreaView.this.mLeftAssistIcon;
                }
                this.mIconState.contentDescription = KeyguardBottomAreaView.this.mContext.getString(R$string.accessibility_voice_assist_button);
            } else {
                IntentButtonProvider.IntentButton.IconState iconState2 = this.mIconState;
                if (!KeyguardBottomAreaView.this.mUserSetupComplete || !KeyguardBottomAreaView.this.mShowLeftAffordance || !KeyguardBottomAreaView.this.isPhoneVisible()) {
                    z = false;
                }
                iconState2.isVisible = z;
                this.mIconState.drawable = KeyguardBottomAreaView.this.mContext.getDrawable(17302816);
                this.mIconState.contentDescription = KeyguardBottomAreaView.this.mContext.getString(R$string.accessibility_phone_button);
            }
            return this.mIconState;
        }

        public Intent getIntent() {
            return KeyguardBottomAreaView.PHONE_INTENT;
        }
    }

    public class DefaultRightButton implements IntentButtonProvider.IntentButton {
        public IntentButtonProvider.IntentButton.IconState mIconState;

        public DefaultRightButton() {
            this.mIconState = new IntentButtonProvider.IntentButton.IconState();
        }

        public IntentButtonProvider.IntentButton.IconState getIcon() {
            boolean z = true;
            boolean z2 = KeyguardBottomAreaView.this.mCentralSurfaces != null && !KeyguardBottomAreaView.this.mCentralSurfaces.isCameraAllowedByAdmin();
            IntentButtonProvider.IntentButton.IconState iconState = this.mIconState;
            if (z2 || !KeyguardBottomAreaView.this.mShowCameraAffordance || !KeyguardBottomAreaView.this.mUserSetupComplete || KeyguardBottomAreaView.this.resolveCameraIntent() == null) {
                z = false;
            }
            iconState.isVisible = z;
            this.mIconState.drawable = KeyguardBottomAreaView.this.mContext.getDrawable(R$drawable.ic_camera_alt_24dp);
            this.mIconState.contentDescription = KeyguardBottomAreaView.this.mContext.getString(R$string.accessibility_camera_button);
            return this.mIconState;
        }

        public Intent getIntent() {
            boolean canDismissLockScreen = KeyguardBottomAreaView.this.mKeyguardStateController.canDismissLockScreen();
            if (!KeyguardBottomAreaView.this.mKeyguardStateController.isMethodSecure() || canDismissLockScreen) {
                return CameraIntents.getInsecureCameraIntent(KeyguardBottomAreaView.this.getContext());
            }
            return CameraIntents.getSecureCameraIntent(KeyguardBottomAreaView.this.getContext());
        }
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int safeInsetBottom = windowInsets.getDisplayCutout() != null ? windowInsets.getDisplayCutout().getSafeInsetBottom() : 0;
        if (isPaddingRelative()) {
            setPaddingRelative(getPaddingStart(), getPaddingTop(), getPaddingEnd(), safeInsetBottom);
        } else {
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), safeInsetBottom);
        }
        return windowInsets;
    }

    public void setFalsingManager(FalsingManager falsingManager) {
        this.mFalsingManager = falsingManager;
    }

    public void initWallet(QuickAccessWalletController quickAccessWalletController) {
        this.mQuickAccessWalletController = quickAccessWalletController;
        quickAccessWalletController.setupWalletChangeObservers(this.mCardRetriever, QuickAccessWalletController.WalletChangeEvent.WALLET_PREFERENCE_CHANGE, QuickAccessWalletController.WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE);
        this.mQuickAccessWalletController.updateWalletPreference();
        this.mQuickAccessWalletController.queryWalletCards(this.mCardRetriever);
        updateWalletVisibility();
        updateAffordanceColors();
    }

    public void initQRCodeScanner(QRCodeScannerController qRCodeScannerController) {
        this.mQRCodeScannerController = qRCodeScannerController;
        qRCodeScannerController.registerQRCodeScannerChangeObservers(0, 1);
        updateQRCodeButtonVisibility();
        updateAffordanceColors();
    }

    public final void updateQRCodeButtonVisibility() {
        QuickAccessWalletController quickAccessWalletController = this.mQuickAccessWalletController;
        if (quickAccessWalletController == null || !quickAccessWalletController.isWalletEnabled()) {
            QRCodeScannerController qRCodeScannerController = this.mQRCodeScannerController;
            if (qRCodeScannerController == null || !qRCodeScannerController.isEnabledForLockScreenButton()) {
                this.mQRCodeScannerButton.setVisibility(8);
                if (this.mControlsButton.getVisibility() == 8) {
                    this.mIndicationArea.setPadding(0, 0, 0, 0);
                    return;
                }
                return;
            }
            this.mQRCodeScannerButton.setVisibility(0);
            this.mQRCodeScannerButton.setOnClickListener(new KeyguardBottomAreaView$$ExternalSyntheticLambda8(this));
            ViewGroup viewGroup = this.mIndicationArea;
            int i = this.mIndicationPadding;
            viewGroup.setPadding(i, 0, i, 0);
        }
    }

    public final void onQRCodeScannerClicked(View view) {
        Intent intent = this.mQRCodeScannerController.getIntent();
        if (intent != null) {
            try {
                ActivityTaskManager.getService().startActivityAsUser((IApplicationThread) null, getContext().getBasePackageName(), getContext().getAttributionTag(), intent, intent.resolveTypeIfNeeded(getContext().getContentResolver()), (IBinder) null, (String) null, 0, 268435456, (ProfilerInfo) null, (Bundle) null, UserHandle.CURRENT.getIdentifier());
            } catch (RemoteException unused) {
                Log.e("CentralSurfaces/KeyguardBottomAreaView", "Unexpected intent: " + intent + " when the QR code scanner button was clicked");
            }
        }
    }

    public final void updateAffordanceColors() {
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.mContext, 16842806);
        this.mWalletButton.getDrawable().setTint(colorAttrDefaultColor);
        this.mControlsButton.getDrawable().setTint(colorAttrDefaultColor);
        this.mQRCodeScannerButton.getDrawable().setTint(colorAttrDefaultColor);
        ColorStateList colorAttr = Utils.getColorAttr(this.mContext, 17956909);
        this.mWalletButton.setBackgroundTintList(colorAttr);
        this.mControlsButton.setBackgroundTintList(colorAttr);
        this.mQRCodeScannerButton.setBackgroundTintList(colorAttr);
    }

    public void initControls(ControlsComponent controlsComponent) {
        this.mControlsComponent = controlsComponent;
        controlsComponent.getControlsListingController().ifPresent(new KeyguardBottomAreaView$$ExternalSyntheticLambda9(this));
        updateAffordanceColors();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initControls$8(ControlsListingController controlsListingController) {
        controlsListingController.addCallback(this.mListingCallback);
    }

    public final void onWalletClick(View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mQuickAccessWalletController.startQuickAccessUiIntent(this.mActivityStarter, createLaunchAnimationController(view), this.mHasCard);
        }
    }

    public ActivityLaunchAnimator.Controller createLaunchAnimationController(View view) {
        return ActivityLaunchAnimator.Controller.fromView(view, (Integer) null);
    }

    public final void onControlsClick(View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            Intent putExtra = new Intent(this.mContext, ControlsActivity.class).addFlags(335544320).putExtra("extra_animate", true);
            ActivityLaunchAnimator.Controller controller = null;
            if (view != null) {
                controller = ActivityLaunchAnimator.Controller.fromView(view, (Integer) null);
            }
            if (this.mControlsComponent.getVisibility() == ControlsComponent.Visibility.AVAILABLE) {
                this.mActivityStarter.startActivity(putExtra, true, controller, true);
            } else {
                this.mActivityStarter.postStartActivityDismissingKeyguard(putExtra, 0, controller);
            }
        }
    }

    public class WalletCardRetriever implements QuickAccessWalletClient.OnWalletCardsRetrievedCallback {
        public WalletCardRetriever() {
        }

        public void onWalletCardsRetrieved(GetWalletCardsResponse getWalletCardsResponse) {
            KeyguardBottomAreaView.this.mHasCard = !getWalletCardsResponse.getWalletCards().isEmpty();
            KeyguardBottomAreaView.this.post(new KeyguardBottomAreaView$WalletCardRetriever$$ExternalSyntheticLambda1(this, KeyguardBottomAreaView.this.mQuickAccessWalletController.getWalletClient().getTileIcon()));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onWalletCardsRetrieved$0(Drawable drawable) {
            if (drawable != null) {
                KeyguardBottomAreaView.this.mWalletButton.setImageDrawable(drawable);
            }
            KeyguardBottomAreaView.this.updateWalletVisibility();
            KeyguardBottomAreaView.this.updateAffordanceColors();
        }

        public void onWalletCardRetrievalError(GetWalletCardsError getWalletCardsError) {
            KeyguardBottomAreaView.this.mHasCard = false;
            KeyguardBottomAreaView.this.post(new KeyguardBottomAreaView$WalletCardRetriever$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onWalletCardRetrievalError$1() {
            KeyguardBottomAreaView.this.updateWalletVisibility();
            KeyguardBottomAreaView.this.updateAffordanceColors();
        }
    }
}
