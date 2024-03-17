package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import com.android.settingslib.Utils;
import com.android.systemui.Dumpable;
import com.android.systemui.R$id;
import com.android.systemui.R$xml;
import com.android.systemui.animation.ShadeInterpolation;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.qs.ChipVisibilityListener;
import com.android.systemui.qs.HeaderPrivacyIconsController;
import com.android.systemui.qs.carrier.QSCarrierGroup;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.io.PrintWriter;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParser;

/* compiled from: LargeScreenShadeHeaderController.kt */
public final class LargeScreenShadeHeaderController implements Dumpable {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int HEADER_TRANSITION_ID = R$id.header_transition;
    public static final int LARGE_SCREEN_HEADER_CONSTRAINT = R$id.large_screen_header_constraint;
    public static final int LARGE_SCREEN_HEADER_TRANSITION_ID = R$id.large_screen_header_transition;
    public static final int QQS_HEADER_CONSTRAINT = R$id.qqs_header_constraint;
    public static final int QS_HEADER_CONSTRAINT = R$id.qs_header_constraint;
    public boolean active;
    @NotNull
    public final List<String> carrierIconSlots;
    @NotNull
    public final ChipVisibilityListener chipVisibilityListener;
    @NotNull
    public final TextView clock;
    public final boolean combinedHeaders;
    @NotNull
    public final ConfigurationController configurationController;
    @NotNull
    public final TextView date;
    @NotNull
    public final View header;
    @NotNull
    public final StatusIconContainer iconContainer;
    @NotNull
    public final StatusBarIconController.TintedIconManager iconManager;
    @NotNull
    public final HeaderPrivacyIconsController privacyIconsController;
    @NotNull
    public final QSCarrierGroup qsCarrierGroup;
    @NotNull
    public final QSCarrierGroupController qsCarrierGroupController;
    public boolean qsDisabled;
    public float qsExpandedFraction = -1.0f;
    public int qsScrollY;
    public boolean shadeExpanded;
    public float shadeExpandedFraction = -1.0f;
    @NotNull
    public final StatusBarIconController statusBarIconController;
    public boolean visible;

    public LargeScreenShadeHeaderController(@NotNull View view, @NotNull StatusBarIconController statusBarIconController2, @NotNull HeaderPrivacyIconsController headerPrivacyIconsController, @NotNull ConfigurationController configurationController2, @NotNull QSCarrierGroupController.Builder builder, @NotNull FeatureFlags featureFlags, @NotNull BatteryMeterViewController batteryMeterViewController, @NotNull DumpManager dumpManager) {
        List<String> list;
        this.header = view;
        this.statusBarIconController = statusBarIconController2;
        this.privacyIconsController = headerPrivacyIconsController;
        this.configurationController = configurationController2;
        this.combinedHeaders = featureFlags.isEnabled(Flags.COMBINED_QS_HEADERS);
        this.clock = (TextView) view.findViewById(R$id.clock);
        this.date = (TextView) view.findViewById(R$id.date);
        int i = R$id.carrier_group;
        this.qsCarrierGroup = (QSCarrierGroup) view.findViewById(i);
        LargeScreenShadeHeaderController$chipVisibilityListener$1 largeScreenShadeHeaderController$chipVisibilityListener$1 = new LargeScreenShadeHeaderController$chipVisibilityListener$1(this);
        this.chipVisibilityListener = largeScreenShadeHeaderController$chipVisibilityListener$1;
        if (view instanceof MotionLayout) {
            Context context = ((MotionLayout) view).getContext();
            Resources resources = ((MotionLayout) view).getResources();
            ((MotionLayout) view).getConstraintSet(QQS_HEADER_CONSTRAINT).load(context, (XmlPullParser) resources.getXml(R$xml.qqs_header));
            ((MotionLayout) view).getConstraintSet(QS_HEADER_CONSTRAINT).load(context, (XmlPullParser) resources.getXml(R$xml.qs_header));
            ((MotionLayout) view).getConstraintSet(LARGE_SCREEN_HEADER_CONSTRAINT).load(context, (XmlPullParser) resources.getXml(R$xml.large_screen_shade_header));
            headerPrivacyIconsController.setChipVisibilityListener(largeScreenShadeHeaderController$chipVisibilityListener$1);
        }
        bindConfigurationListener();
        batteryMeterViewController.init();
        BatteryMeterView batteryMeterView = (BatteryMeterView) view.findViewById(R$id.batteryRemainingIcon);
        batteryMeterViewController.ignoreTunerUpdates();
        batteryMeterView.setPercentShowMode(3);
        batteryMeterView.setVisibility(8);
        StatusIconContainer statusIconContainer = (StatusIconContainer) view.findViewById(R$id.statusIcons);
        this.iconContainer = statusIconContainer;
        statusIconContainer.setVisibility(8);
        StatusBarIconController.TintedIconManager tintedIconManager = new StatusBarIconController.TintedIconManager(statusIconContainer, featureFlags);
        this.iconManager = tintedIconManager;
        tintedIconManager.setTint(Utils.getColorAttrDefaultColor(view.getContext(), 16842806));
        if (featureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS)) {
            list = CollectionsKt__CollectionsKt.listOf(view.getContext().getString(17041576), view.getContext().getString(17041559));
        } else {
            list = CollectionsKt__CollectionsJVMKt.listOf(view.getContext().getString(17041573));
        }
        this.carrierIconSlots = list;
        this.qsCarrierGroupController = builder.setQSCarrierGroup((QSCarrierGroup) view.findViewById(i)).build();
        dumpManager.registerDumpable(this);
        updateVisibility();
        updateConstraints();
    }

    /* compiled from: LargeScreenShadeHeaderController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final String stateToString(int i) {
            if (i == LargeScreenShadeHeaderController.QQS_HEADER_CONSTRAINT) {
                return "QQS Header";
            }
            if (i == LargeScreenShadeHeaderController.QS_HEADER_CONSTRAINT) {
                return "QS Header";
            }
            return i == LargeScreenShadeHeaderController.LARGE_SCREEN_HEADER_CONSTRAINT ? "Large Screen Header" : "Unknown state";
        }
    }

    public final void setVisible(boolean z) {
        if (this.visible != z) {
            this.visible = z;
            updateListeners();
        }
    }

    public final void setShadeExpanded(boolean z) {
        if (this.shadeExpanded != z) {
            this.shadeExpanded = z;
            onShadeExpandedChanged();
        }
    }

    public final void setActive(boolean z) {
        if (this.active != z) {
            this.active = z;
            onHeaderStateChanged();
        }
    }

    public final void setShadeExpandedFraction(float f) {
        if (this.visible) {
            if (!(this.shadeExpandedFraction == f)) {
                this.header.setAlpha(ShadeInterpolation.getContentAlpha(f));
                this.shadeExpandedFraction = f;
            }
        }
    }

    public final void setQsExpandedFraction(float f) {
        if (this.visible) {
            if (!(this.qsExpandedFraction == f)) {
                this.qsExpandedFraction = f;
                updateVisibility();
                updatePosition();
            }
        }
    }

    public final void setQsScrollY(int i) {
        if (this.qsScrollY != i) {
            this.qsScrollY = i;
            updateScrollY();
        }
    }

    public final void disable(int i, int i2, boolean z) {
        boolean z2 = true;
        if ((i2 & 1) == 0) {
            z2 = false;
        }
        if (z2 != this.qsDisabled) {
            this.qsDisabled = z2;
            updateVisibility();
        }
    }

    public final void updateScrollY() {
        if (!this.active && this.combinedHeaders) {
            this.header.setScrollY(this.qsScrollY);
        }
    }

    public final void bindConfigurationListener() {
        this.configurationController.addCallback(new LargeScreenShadeHeaderController$bindConfigurationListener$listener$1(this));
    }

    public final void onShadeExpandedChanged() {
        if (this.shadeExpanded) {
            this.privacyIconsController.startListening();
        } else {
            this.privacyIconsController.stopListening();
        }
        updateVisibility();
        updatePosition();
    }

    public final void onHeaderStateChanged() {
        if (this.active || this.combinedHeaders) {
            this.privacyIconsController.onParentVisible();
        } else {
            this.privacyIconsController.onParentInvisible();
        }
        updateVisibility();
        updateConstraints();
    }

    public final void updateVisibility() {
        int i;
        boolean z = false;
        if ((this.active || this.combinedHeaders) && !this.qsDisabled) {
            i = this.shadeExpanded ? 0 : 4;
        } else {
            i = 8;
        }
        if (this.header.getVisibility() != i) {
            this.header.setVisibility(i);
            if (i == 0) {
                z = true;
            }
            setVisible(z);
        }
    }

    public final void updateConstraints() {
        if (this.combinedHeaders) {
            View view = this.header;
            MotionLayout motionLayout = (MotionLayout) view;
            if (this.active) {
                ((MotionLayout) view).setTransition(LARGE_SCREEN_HEADER_TRANSITION_ID);
                return;
            }
            ((MotionLayout) view).setTransition(HEADER_TRANSITION_ID);
            ((MotionLayout) this.header).transitionToStart();
            updatePosition();
            updateScrollY();
        }
    }

    public final void updatePosition() {
        View view = this.header;
        if ((view instanceof MotionLayout) && !this.active && this.visible) {
            ((MotionLayout) view).setProgress(this.qsExpandedFraction);
        }
    }

    public final void updateListeners() {
        this.qsCarrierGroupController.setListening(this.visible);
        if (this.visible) {
            updateSingleCarrier(this.qsCarrierGroupController.isSingleCarrier());
            this.qsCarrierGroupController.setOnSingleCarrierChangedListener(new LargeScreenShadeHeaderController$updateListeners$1(this));
            this.statusBarIconController.addIconGroup(this.iconManager);
            return;
        }
        this.qsCarrierGroupController.setOnSingleCarrierChangedListener((QSCarrierGroupController.OnSingleCarrierChangedListener) null);
        this.statusBarIconController.removeIconGroup(this.iconManager);
    }

    public final void updateSingleCarrier(boolean z) {
        if (z) {
            this.iconContainer.removeIgnoredSlots(this.carrierIconSlots);
        } else {
            this.iconContainer.addIgnoredSlots(this.carrierIconSlots);
        }
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("visible: ", Boolean.valueOf(this.visible)));
        printWriter.println(Intrinsics.stringPlus("shadeExpanded: ", Boolean.valueOf(this.shadeExpanded)));
        printWriter.println(Intrinsics.stringPlus("shadeExpandedFraction: ", Float.valueOf(this.shadeExpandedFraction)));
        printWriter.println(Intrinsics.stringPlus("active: ", Boolean.valueOf(this.active)));
        printWriter.println(Intrinsics.stringPlus("qsExpandedFraction: ", Float.valueOf(this.qsExpandedFraction)));
        printWriter.println(Intrinsics.stringPlus("qsScrollY: ", Integer.valueOf(this.qsScrollY)));
        if (this.combinedHeaders) {
            View view = this.header;
            MotionLayout motionLayout = (MotionLayout) view;
            printWriter.println(Intrinsics.stringPlus("currentState: ", Companion.stateToString(((MotionLayout) view).getCurrentState())));
        }
    }
}
