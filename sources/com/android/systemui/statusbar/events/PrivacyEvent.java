package com.android.systemui.statusbar.events;

import android.content.Context;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyItem;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StatusEvent.kt */
public final class PrivacyEvent implements StatusEvent {
    public final boolean forceVisible;
    public final int priority;
    @Nullable
    public OngoingPrivacyChip privacyChip;
    @NotNull
    public List<PrivacyItem> privacyItems;
    public final boolean showAnimation;
    @NotNull
    public final Function1<Context, BackgroundAnimatableView> viewCreator;

    public PrivacyEvent() {
        this(false, 1, (DefaultConstructorMarker) null);
    }

    public PrivacyEvent(boolean z) {
        this.showAnimation = z;
        this.priority = 100;
        this.forceVisible = true;
        this.privacyItems = CollectionsKt__CollectionsKt.emptyList();
        this.viewCreator = new PrivacyEvent$viewCreator$1(this);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ PrivacyEvent(boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? true : z);
    }

    public boolean getShowAnimation() {
        return this.showAnimation;
    }

    public int getPriority() {
        return this.priority;
    }

    public boolean getForceVisible() {
        return this.forceVisible;
    }

    @NotNull
    public final List<PrivacyItem> getPrivacyItems() {
        return this.privacyItems;
    }

    public final void setPrivacyItems(@NotNull List<PrivacyItem> list) {
        this.privacyItems = list;
    }

    @NotNull
    public Function1<Context, BackgroundAnimatableView> getViewCreator() {
        return this.viewCreator;
    }

    @NotNull
    public String toString() {
        return PrivacyEvent.class.getSimpleName() + "(forceVisible=" + getForceVisible() + ", privacyItems=" + this.privacyItems + ')';
    }

    public boolean shouldUpdateFromEvent(@Nullable StatusEvent statusEvent) {
        return (statusEvent instanceof PrivacyEvent) && !Intrinsics.areEqual((Object) ((PrivacyEvent) statusEvent).privacyItems, (Object) this.privacyItems);
    }

    public void updateFromEvent(@Nullable StatusEvent statusEvent) {
        if (statusEvent instanceof PrivacyEvent) {
            PrivacyEvent privacyEvent = (PrivacyEvent) statusEvent;
            this.privacyItems = privacyEvent.privacyItems;
            OngoingPrivacyChip ongoingPrivacyChip = this.privacyChip;
            if (ongoingPrivacyChip != null) {
                ongoingPrivacyChip.setPrivacyList(privacyEvent.privacyItems);
            }
        }
    }
}
