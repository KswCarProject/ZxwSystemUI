package com.android.systemui.statusbar.notification.stack;

import android.content.res.Resources;
import android.util.Log;
import com.android.systemui.R$dimen;
import com.android.systemui.R$integer;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.util.ConvenienceExtensionsKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.Delegates;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequenceBuilderKt;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationStackSizeCalculator.kt */
public final class NotificationStackSizeCalculator {
    public static final /* synthetic */ KProperty<Object>[] $$delegatedProperties;
    @NotNull
    public final ReadWriteProperty dividerHeight$delegate;
    @NotNull
    public final LockscreenShadeTransitionController lockscreenShadeTransitionController;
    @NotNull
    public final ReadWriteProperty maxKeyguardNotifications$delegate;
    @NotNull
    public final Resources resources;
    @NotNull
    public final SysuiStatusBarStateController statusBarStateController;

    public final int infiniteIfNegative(int i) {
        if (i < 0) {
            return Integer.MAX_VALUE;
        }
        return i;
    }

    public NotificationStackSizeCalculator(@NotNull SysuiStatusBarStateController sysuiStatusBarStateController, @NotNull LockscreenShadeTransitionController lockscreenShadeTransitionController2, @NotNull Resources resources2) {
        this.statusBarStateController = sysuiStatusBarStateController;
        this.lockscreenShadeTransitionController = lockscreenShadeTransitionController2;
        this.resources = resources2;
        Delegates delegates = Delegates.INSTANCE;
        this.maxKeyguardNotifications$delegate = delegates.notNull();
        this.dividerHeight$delegate = delegates.notNull();
        updateResources();
    }

    static {
        Class<NotificationStackSizeCalculator> cls = NotificationStackSizeCalculator.class;
        $$delegatedProperties = new KProperty[]{Reflection.mutableProperty1(new MutablePropertyReference1Impl(cls, "maxKeyguardNotifications", "getMaxKeyguardNotifications()I", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(cls, "dividerHeight", "getDividerHeight()F", 0))};
    }

    public final int getMaxKeyguardNotifications() {
        return ((Number) this.maxKeyguardNotifications$delegate.getValue(this, $$delegatedProperties[0])).intValue();
    }

    public final void setMaxKeyguardNotifications(int i) {
        this.maxKeyguardNotifications$delegate.setValue(this, $$delegatedProperties[0], Integer.valueOf(i));
    }

    public final float getDividerHeight() {
        return ((Number) this.dividerHeight$delegate.getValue(this, $$delegatedProperties[1])).floatValue();
    }

    public final void setDividerHeight(float f) {
        this.dividerHeight$delegate.setValue(this, $$delegatedProperties[1], Float.valueOf(f));
    }

    public final boolean canStackFitInSpace(StackHeight stackHeight, float f, float f2) {
        float component1 = stackHeight.component1();
        float component2 = stackHeight.component2();
        boolean z = true;
        if (component2 == 0.0f) {
            if (component1 > f) {
                z = false;
            }
            if (NotificationStackSizeCalculatorKt.DEBUG) {
                Log.d("NotifStackSizeCalc", "canStackFitInSpace[" + z + "] = notificationsHeight[" + component1 + "] <= spaceForNotifications[" + f + ']');
            }
        } else {
            if (component1 + component2 > f + f2) {
                z = false;
            }
            if (NotificationStackSizeCalculatorKt.DEBUG) {
                Log.d("NotifStackSizeCalc", "canStackFitInSpace[" + z + "] = (notificationsHeight[" + component1 + "] + shelfHeightWithSpaceBefore[" + component2 + "]) <= (spaceForNotifications[" + f + "]  + spaceForShelf[" + f2 + "])");
            }
        }
        return z;
    }

    /* compiled from: NotificationStackSizeCalculator.kt */
    public static final class StackHeight {
        public final float notificationsHeight;
        public final float shelfHeightWithSpaceBefore;

        public final float component1() {
            return this.notificationsHeight;
        }

        public final float component2() {
            return this.shelfHeightWithSpaceBefore;
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof StackHeight)) {
                return false;
            }
            StackHeight stackHeight = (StackHeight) obj;
            return Intrinsics.areEqual((Object) Float.valueOf(this.notificationsHeight), (Object) Float.valueOf(stackHeight.notificationsHeight)) && Intrinsics.areEqual((Object) Float.valueOf(this.shelfHeightWithSpaceBefore), (Object) Float.valueOf(stackHeight.shelfHeightWithSpaceBefore));
        }

        public int hashCode() {
            return (Float.hashCode(this.notificationsHeight) * 31) + Float.hashCode(this.shelfHeightWithSpaceBefore);
        }

        @NotNull
        public String toString() {
            return "StackHeight(notificationsHeight=" + this.notificationsHeight + ", shelfHeightWithSpaceBefore=" + this.shelfHeightWithSpaceBefore + ')';
        }

        public StackHeight(float f, float f2) {
            this.notificationsHeight = f;
            this.shelfHeightWithSpaceBefore = f2;
        }
    }

    public final Sequence<StackHeight> computeHeightPerNotificationLimit(NotificationStackScrollLayout notificationStackScrollLayout, float f) {
        return SequencesKt__SequenceBuilderKt.sequence(new NotificationStackSizeCalculator$computeHeightPerNotificationLimit$1(this, notificationStackScrollLayout, f, (Continuation<? super NotificationStackSizeCalculator$computeHeightPerNotificationLimit$1>) null));
    }

    public final void updateResources() {
        setMaxKeyguardNotifications(infiniteIfNegative(this.resources.getInteger(R$integer.keyguard_max_notification_count)));
        setDividerHeight(Math.max(1.0f, (float) this.resources.getDimensionPixelSize(R$dimen.notification_divider_height)));
    }

    public final Sequence<ExpandableView> getChildrenSequence(NotificationStackScrollLayout notificationStackScrollLayout) {
        return SequencesKt___SequencesKt.map(ConvenienceExtensionsKt.getChildren(notificationStackScrollLayout), NotificationStackSizeCalculator$childrenSequence$1.INSTANCE);
    }

    public final boolean onLockscreen() {
        if (this.statusBarStateController.getState() != 1) {
            return false;
        }
        if (this.lockscreenShadeTransitionController.getFractionToShade() == 0.0f) {
            return true;
        }
        return false;
    }

    public final float spaceNeeded(@NotNull ExpandableView expandableView, int i, @Nullable ExpandableView expandableView2, @NotNull NotificationStackScrollLayout notificationStackScrollLayout, boolean z) {
        int i2;
        isShowable(expandableView, z);
        if (z) {
            i2 = expandableView.getMinHeight(true);
        } else {
            i2 = expandableView.getIntrinsicHeight();
        }
        return ((float) i2) + calculateGapAndDividerHeight(notificationStackScrollLayout, expandableView2, expandableView, i);
    }

    public final boolean isShowable(ExpandableView expandableView, boolean z) {
        if (expandableView.getVisibility() == 8 || expandableView.hasNoContentHeight()) {
            return false;
        }
        if (!z) {
            return true;
        }
        if (expandableView instanceof ExpandableNotificationRow) {
            if (!canShowViewOnLockscreen(expandableView) || ((ExpandableNotificationRow) expandableView).isRemoved()) {
                return false;
            }
            return true;
        } else if (!(expandableView instanceof MediaContainerView) || ((MediaContainerView) expandableView).getIntrinsicHeight() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public final float calculateGapAndDividerHeight(NotificationStackScrollLayout notificationStackScrollLayout, ExpandableView expandableView, ExpandableView expandableView2, int i) {
        if (i == 0) {
            return 0.0f;
        }
        return notificationStackScrollLayout.calculateGapHeight(expandableView, expandableView2, i) + getDividerHeight();
    }

    public final Sequence<ExpandableView> showableChildren(NotificationStackScrollLayout notificationStackScrollLayout) {
        return SequencesKt___SequencesKt.filter(getChildrenSequence(notificationStackScrollLayout), new NotificationStackSizeCalculator$showableChildren$1(this));
    }

    public final boolean canShowViewOnLockscreen(ExpandableView expandableView) {
        if (!expandableView.hasNoContentHeight() && expandableView.getVisibility() != 8) {
            return true;
        }
        return false;
    }

    public final float computeHeight(@NotNull NotificationStackScrollLayout notificationStackScrollLayout, int i, float f) {
        if (NotificationStackSizeCalculatorKt.DEBUG) {
            Log.d("NotifStackSizeCalc", "\n");
        }
        Sequence computeHeightPerNotificationLimit = computeHeightPerNotificationLimit(notificationStackScrollLayout, f);
        StackHeight stackHeight = (StackHeight) SequencesKt___SequencesKt.elementAtOrElse(computeHeightPerNotificationLimit, i, new NotificationStackSizeCalculator$computeHeight$2(computeHeightPerNotificationLimit));
        float component1 = stackHeight.component1();
        float component2 = stackHeight.component2();
        if (NotificationStackSizeCalculatorKt.DEBUG) {
            Log.d("NotifStackSizeCalc", "computeHeight(maxNotifications=" + i + ",shelfIntrinsicHeight=" + f + ") -> " + (component1 + component2) + " = (" + component1 + " + " + component2 + ')');
        }
        return component1 + component2;
    }

    public final int computeMaxKeyguardNotifications(@NotNull NotificationStackScrollLayout notificationStackScrollLayout, float f, float f2, float f3) {
        if (NotificationStackSizeCalculatorKt.DEBUG) {
            Log.d("NotifStackSizeCalc", "\n");
        }
        Sequence<StackHeight> computeHeightPerNotificationLimit = computeHeightPerNotificationLimit(notificationStackScrollLayout, f3);
        int lastIndexWhile = lastIndexWhile(computeHeightPerNotificationLimit, new NotificationStackSizeCalculator$computeMaxKeyguardNotifications$maxNotifications$1(this, f, f2));
        if (onLockscreen()) {
            lastIndexWhile = Math.min(getMaxKeyguardNotifications(), lastIndexWhile);
        }
        int max = Math.max(0, lastIndexWhile);
        if (NotificationStackSizeCalculatorKt.DEBUG) {
            String stringPlus = NotificationStackSizeCalculatorKt.SPEW ? Intrinsics.stringPlus(" stackHeightSequence=", SequencesKt___SequencesKt.toList(computeHeightPerNotificationLimit)) : "";
            Log.d("NotifStackSizeCalc", "computeMaxKeyguardNotifications( spaceForNotifications=" + f + " spaceForShelf=" + f2 + " shelfHeight=" + f3 + ") -> " + max + stringPlus);
        }
        return max;
    }

    public final <T> int lastIndexWhile(Sequence<? extends T> sequence, Function1<? super T, Boolean> function1) {
        return SequencesKt___SequencesKt.count(SequencesKt___SequencesKt.takeWhile(sequence, function1)) - 1;
    }
}
