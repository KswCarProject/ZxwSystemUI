package com.android.systemui.qs;

import android.os.Bundle;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.systemui.R$id;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.qs.carrier.QSCarrierGroup;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.statusbar.policy.VariableDateView;
import com.android.systemui.statusbar.policy.VariableDateViewController;
import com.android.systemui.util.ViewController;
import java.util.List;
import java.util.Objects;

public class QuickStatusBarHeaderController extends ViewController<QuickStatusBarHeader> implements ChipVisibilityListener {
    public final BatteryMeterViewController mBatteryMeterViewController;
    public final Clock mClockView;
    public SysuiColorExtractor mColorExtractor;
    public final DemoModeController mDemoModeController;
    public final DemoMode mDemoModeReceiver;
    public final FeatureFlags mFeatureFlags;
    public final StatusIconContainer mIconContainer;
    public final StatusBarIconController.TintedIconManager mIconManager;
    public final StatusBarContentInsetsProvider mInsetsProvider;
    public boolean mListening;
    public ColorExtractor.OnColorsChangedListener mOnColorsChangedListener;
    public final HeaderPrivacyIconsController mPrivacyIconsController;
    public final QSCarrierGroupController mQSCarrierGroupController;
    public final QSExpansionPathInterpolator mQSExpansionPathInterpolator;
    public final QuickQSPanelController mQuickQSPanelController;
    public final StatusBarIconController mStatusBarIconController;
    public final VariableDateViewController mVariableDateViewControllerClockDateView;
    public final VariableDateViewController mVariableDateViewControllerDateView;

    public QuickStatusBarHeaderController(QuickStatusBarHeader quickStatusBarHeader, HeaderPrivacyIconsController headerPrivacyIconsController, StatusBarIconController statusBarIconController, DemoModeController demoModeController, QuickQSPanelController quickQSPanelController, QSCarrierGroupController.Builder builder, SysuiColorExtractor sysuiColorExtractor, QSExpansionPathInterpolator qSExpansionPathInterpolator, FeatureFlags featureFlags, VariableDateViewController.Factory factory, BatteryMeterViewController batteryMeterViewController, StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        super(quickStatusBarHeader);
        this.mPrivacyIconsController = headerPrivacyIconsController;
        this.mStatusBarIconController = statusBarIconController;
        this.mDemoModeController = demoModeController;
        this.mQuickQSPanelController = quickQSPanelController;
        this.mQSExpansionPathInterpolator = qSExpansionPathInterpolator;
        this.mFeatureFlags = featureFlags;
        this.mBatteryMeterViewController = batteryMeterViewController;
        this.mInsetsProvider = statusBarContentInsetsProvider;
        this.mQSCarrierGroupController = builder.setQSCarrierGroup((QSCarrierGroup) ((QuickStatusBarHeader) this.mView).findViewById(R$id.carrier_group)).build();
        Clock clock = (Clock) ((QuickStatusBarHeader) this.mView).findViewById(R$id.clock);
        this.mClockView = clock;
        StatusIconContainer statusIconContainer = (StatusIconContainer) ((QuickStatusBarHeader) this.mView).findViewById(R$id.statusIcons);
        this.mIconContainer = statusIconContainer;
        this.mVariableDateViewControllerDateView = factory.create((VariableDateView) ((QuickStatusBarHeader) this.mView).requireViewById(R$id.date));
        this.mVariableDateViewControllerClockDateView = factory.create((VariableDateView) ((QuickStatusBarHeader) this.mView).requireViewById(R$id.date_clock));
        this.mIconManager = new StatusBarIconController.TintedIconManager(statusIconContainer, featureFlags);
        this.mDemoModeReceiver = new ClockDemoModeReceiver(clock);
        this.mColorExtractor = sysuiColorExtractor;
        QuickStatusBarHeaderController$$ExternalSyntheticLambda0 quickStatusBarHeaderController$$ExternalSyntheticLambda0 = new QuickStatusBarHeaderController$$ExternalSyntheticLambda0(this);
        this.mOnColorsChangedListener = quickStatusBarHeaderController$$ExternalSyntheticLambda0;
        this.mColorExtractor.addOnColorsChangedListener(quickStatusBarHeaderController$$ExternalSyntheticLambda0);
        batteryMeterViewController.ignoreTunerUpdates();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ColorExtractor colorExtractor, int i) {
        this.mClockView.onColorsChanged(this.mColorExtractor.getNeutralColors().supportsDarkText());
    }

    public void onInit() {
        this.mBatteryMeterViewController.init();
    }

    public void onViewAttached() {
        List list;
        this.mPrivacyIconsController.onParentVisible();
        this.mPrivacyIconsController.setChipVisibilityListener(this);
        this.mIconContainer.setShouldRestrictIcons(false);
        this.mStatusBarIconController.addIconGroup(this.mIconManager);
        ((QuickStatusBarHeader) this.mView).setIsSingleCarrier(this.mQSCarrierGroupController.isSingleCarrier());
        QSCarrierGroupController qSCarrierGroupController = this.mQSCarrierGroupController;
        QuickStatusBarHeader quickStatusBarHeader = (QuickStatusBarHeader) this.mView;
        Objects.requireNonNull(quickStatusBarHeader);
        qSCarrierGroupController.setOnSingleCarrierChangedListener(new QuickStatusBarHeaderController$$ExternalSyntheticLambda1(quickStatusBarHeader));
        if (this.mFeatureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS)) {
            list = List.of(getResources().getString(17041576), getResources().getString(17041559));
        } else {
            list = List.of(getResources().getString(17041573));
        }
        ((QuickStatusBarHeader) this.mView).onAttach(this.mIconManager, this.mQSExpansionPathInterpolator, list, this.mInsetsProvider, this.mFeatureFlags.isEnabled(Flags.COMBINED_QS_HEADERS));
        this.mDemoModeController.addCallback(this.mDemoModeReceiver);
        this.mVariableDateViewControllerDateView.init();
        this.mVariableDateViewControllerClockDateView.init();
    }

    public void onViewDetached() {
        this.mColorExtractor.removeOnColorsChangedListener(this.mOnColorsChangedListener);
        this.mPrivacyIconsController.onParentInvisible();
        this.mStatusBarIconController.removeIconGroup(this.mIconManager);
        this.mQSCarrierGroupController.setOnSingleCarrierChangedListener((QSCarrierGroupController.OnSingleCarrierChangedListener) null);
        this.mDemoModeController.removeCallback(this.mDemoModeReceiver);
        setListening(false);
    }

    public void setListening(boolean z) {
        this.mQSCarrierGroupController.setListening(z);
        if (z != this.mListening) {
            this.mListening = z;
            this.mQuickQSPanelController.setListening(z);
            if (this.mQuickQSPanelController.switchTileLayout(false)) {
                ((QuickStatusBarHeader) this.mView).updateResources();
            }
            if (z) {
                this.mPrivacyIconsController.startListening();
            } else {
                this.mPrivacyIconsController.stopListening();
            }
        }
    }

    public void onChipVisibilityRefreshed(boolean z) {
        ((QuickStatusBarHeader) this.mView).setChipVisibility(z);
    }

    public void setContentMargins(int i, int i2) {
        this.mQuickQSPanelController.setContentMargins(i, i2);
    }

    public static class ClockDemoModeReceiver implements DemoMode {
        public Clock mClockView;

        public List<String> demoCommands() {
            return List.of("clock");
        }

        public ClockDemoModeReceiver(Clock clock) {
            this.mClockView = clock;
        }

        public void dispatchDemoCommand(String str, Bundle bundle) {
            this.mClockView.dispatchDemoCommand(str, bundle);
        }

        public void onDemoModeStarted() {
            this.mClockView.onDemoModeStarted();
        }

        public void onDemoModeFinished() {
            this.mClockView.onDemoModeFinished();
        }
    }
}
