package com.android.systemui.statusbar.notification.stack;

import android.os.Trace;
import android.util.SparseArray;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.collection.render.MediaContainerController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ConvenienceExtensionsKt;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationSectionsManager.kt */
public final class NotificationSectionsManager implements StackScrollAlgorithm.SectionProvider {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final SectionHeaderController alertingHeaderController;
    @NotNull
    public final ConfigurationController configurationController;
    @NotNull
    public final NotificationSectionsManager$configurationListener$1 configurationListener = new NotificationSectionsManager$configurationListener$1(this);
    @NotNull
    public final SectionHeaderController incomingHeaderController;
    public boolean initialized;
    @NotNull
    public final KeyguardMediaController keyguardMediaController;
    @NotNull
    public final NotificationSectionsLogger logger;
    @NotNull
    public final MediaContainerController mediaContainerController;
    @NotNull
    public final NotifPipelineFlags notifPipelineFlags;
    public NotificationStackScrollLayout parent;
    @NotNull
    public final SectionHeaderController peopleHeaderController;
    @NotNull
    public final NotificationSectionsFeatureManager sectionsFeatureManager;
    @NotNull
    public final SectionHeaderController silentHeaderController;
    @NotNull
    public final StatusBarStateController statusBarStateController;

    /* compiled from: NotificationSectionsManager.kt */
    public interface SectionUpdateState<T extends ExpandableView> {
        void adjustViewPosition();

        @Nullable
        Integer getCurrentPosition();

        @Nullable
        Integer getTargetPosition();

        void setCurrentPosition(@Nullable Integer num);

        void setTargetPosition(@Nullable Integer num);
    }

    @VisibleForTesting
    public static /* synthetic */ void getAlertingHeaderView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getIncomingHeaderView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getMediaControlsView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getPeopleHeaderView$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getSilentHeaderView$annotations() {
    }

    public final void logShadeContents() {
        Trace.beginSection("NotifSectionsManager.logShadeContents");
        try {
            NotificationStackScrollLayout notificationStackScrollLayout = this.parent;
            if (notificationStackScrollLayout == null) {
                notificationStackScrollLayout = null;
            }
            int i = 0;
            for (View next : ConvenienceExtensionsKt.getChildren(notificationStackScrollLayout)) {
                int i2 = i + 1;
                if (i < 0) {
                    CollectionsKt__CollectionsKt.throwIndexOverflow();
                }
                logShadeChild(i, next);
                i = i2;
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:63:0x011e, code lost:
        if ((r3.getVisibility() == 8) == false) goto L_0x0120;
     */
    /* JADX WARNING: Removed duplicated region for block: B:102:0x0181 A[Catch:{ all -> 0x0292 }] */
    /* JADX WARNING: Removed duplicated region for block: B:103:0x0182 A[Catch:{ all -> 0x0292 }] */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x019b A[Catch:{ all -> 0x0292 }] */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x019e A[Catch:{ all -> 0x0292 }] */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x01c2 A[Catch:{ all -> 0x0292 }, LOOP:0: B:35:0x009d->B:122:0x01c2, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x01fe A[Catch:{ all -> 0x0292 }] */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x0212 A[Catch:{ all -> 0x0292 }] */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x0226 A[Catch:{ all -> 0x0292 }] */
    /* JADX WARNING: Removed duplicated region for block: B:160:0x0238 A[Catch:{ all -> 0x0292 }] */
    /* JADX WARNING: Removed duplicated region for block: B:161:0x0239 A[Catch:{ all -> 0x0292 }] */
    /* JADX WARNING: Removed duplicated region for block: B:168:0x025c A[Catch:{ all -> 0x0292 }, LOOP:2: B:166:0x0256->B:168:0x025c, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:171:0x027d A[Catch:{ all -> 0x0292 }] */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x027e A[Catch:{ all -> 0x0292 }] */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x01c1 A[EDGE_INSN: B:183:0x01c1->B:121:0x01c1 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0152 A[Catch:{ all -> 0x0292 }] */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0165 A[ADDED_TO_REGION, Catch:{ all -> 0x0292 }] */
    @org.jetbrains.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final kotlin.Unit updateSectionBoundaries(@org.jetbrains.annotations.NotNull java.lang.String r23) {
        /*
            r22 = this;
            r0 = r22
            java.lang.String r1 = "NotifSectionsManager.update"
            android.os.Trace.beginSection(r1)
            com.android.systemui.statusbar.notification.NotifPipelineFlags r1 = r0.notifPipelineFlags     // Catch:{ all -> 0x0292 }
            r1.checkLegacyPipelineEnabled()     // Catch:{ all -> 0x0292 }
            boolean r1 = r22.isUsingMultipleSections()     // Catch:{ all -> 0x0292 }
            if (r1 != 0) goto L_0x0014
            goto L_0x028c
        L_0x0014:
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r1 = r0.logger     // Catch:{ all -> 0x0292 }
            r2 = r23
            r1.logStartSectionUpdate(r2)     // Catch:{ all -> 0x0292 }
            com.android.systemui.plugins.statusbar.StatusBarStateController r1 = r0.statusBarStateController     // Catch:{ all -> 0x0292 }
            int r1 = r1.getState()     // Catch:{ all -> 0x0292 }
            r8 = 0
            r9 = 1
            if (r1 == r9) goto L_0x0027
            r10 = r9
            goto L_0x0028
        L_0x0027:
            r10 = r8
        L_0x0028:
            com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager r1 = r0.sectionsFeatureManager     // Catch:{ all -> 0x0292 }
            boolean r11 = r1.isMediaControlsEnabled()     // Catch:{ all -> 0x0292 }
            com.android.systemui.statusbar.notification.stack.MediaContainerView r1 = r22.getMediaControlsView()     // Catch:{ all -> 0x0292 }
            if (r1 != 0) goto L_0x0036
            r13 = 0
            goto L_0x003b
        L_0x0036:
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r1 = r0.expandableViewHeaderState(r1)     // Catch:{ all -> 0x0292 }
            r13 = r1
        L_0x003b:
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r1 = r22.getIncomingHeaderView()     // Catch:{ all -> 0x0292 }
            if (r1 != 0) goto L_0x0043
            r14 = 0
            goto L_0x0048
        L_0x0043:
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r1 = r0.decorViewHeaderState(r1)     // Catch:{ all -> 0x0292 }
            r14 = r1
        L_0x0048:
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r1 = r22.getPeopleHeaderView()     // Catch:{ all -> 0x0292 }
            if (r1 != 0) goto L_0x0050
            r15 = 0
            goto L_0x0055
        L_0x0050:
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r1 = r0.decorViewHeaderState(r1)     // Catch:{ all -> 0x0292 }
            r15 = r1
        L_0x0055:
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r1 = r22.getAlertingHeaderView()     // Catch:{ all -> 0x0292 }
            if (r1 != 0) goto L_0x005e
            r16 = 0
            goto L_0x0064
        L_0x005e:
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r1 = r0.decorViewHeaderState(r1)     // Catch:{ all -> 0x0292 }
            r16 = r1
        L_0x0064:
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r1 = r22.getSilentHeaderView()     // Catch:{ all -> 0x0292 }
            if (r1 != 0) goto L_0x006c
            r7 = 0
            goto L_0x0071
        L_0x006c:
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r1 = r0.decorViewHeaderState(r1)     // Catch:{ all -> 0x0292 }
            r7 = r1
        L_0x0071:
            r1 = 5
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState[] r1 = new com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState[r1]     // Catch:{ all -> 0x0292 }
            r1[r8] = r13     // Catch:{ all -> 0x0292 }
            r1[r9] = r14     // Catch:{ all -> 0x0292 }
            r6 = 2
            r1[r6] = r15     // Catch:{ all -> 0x0292 }
            r2 = 3
            r1[r2] = r16     // Catch:{ all -> 0x0292 }
            r5 = 4
            r1[r5] = r7     // Catch:{ all -> 0x0292 }
            kotlin.sequences.Sequence r1 = kotlin.sequences.SequencesKt__SequencesKt.sequenceOf(r1)     // Catch:{ all -> 0x0292 }
            kotlin.sequences.Sequence r4 = kotlin.sequences.SequencesKt___SequencesKt.filterNotNull(r1)     // Catch:{ all -> 0x0292 }
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r1 = r0.parent     // Catch:{ all -> 0x0292 }
            if (r1 != 0) goto L_0x008e
            r1 = 0
        L_0x008e:
            int r1 = r1.getChildCount()     // Catch:{ all -> 0x0292 }
            int r1 = r1 - r9
            r3 = -1
            if (r3 > r1) goto L_0x01ca
            r2 = r1
            r18 = r8
            r19 = r18
            r17 = 0
        L_0x009d:
            int r1 = r2 + -1
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r3 = r0.parent     // Catch:{ all -> 0x0292 }
            if (r3 != 0) goto L_0x00a4
            r3 = 0
        L_0x00a4:
            android.view.View r3 = r3.getChildAt(r2)     // Catch:{ all -> 0x0292 }
            if (r3 != 0) goto L_0x00b2
            r12 = r1
            r20 = r2
            r1 = r3
            r8 = r4
            r21 = r7
            goto L_0x0106
        L_0x00b2:
            r0.logShadeChild(r2, r3)     // Catch:{ all -> 0x0292 }
            r12 = r1
            r1 = r22
            r20 = r2
            r2 = r13
            r23 = r3
            r8 = -1
            r3 = r14
            r8 = r4
            r4 = r15
            r5 = r16
            r6 = r7
            r21 = r7
            r7 = r23
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r1 = m2573updateSectionBoundaries$lambda16$getSectionState(r1, r2, r3, r4, r5, r6, r7)     // Catch:{ all -> 0x0292 }
            if (r1 != 0) goto L_0x00cf
            goto L_0x0104
        L_0x00cf:
            java.lang.Integer r2 = java.lang.Integer.valueOf(r20)     // Catch:{ all -> 0x0292 }
            r1.setCurrentPosition(r2)     // Catch:{ all -> 0x0292 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$updateSectionBoundaries$1$1$1$1 r2 = new com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$updateSectionBoundaries$1$1$1$1     // Catch:{ all -> 0x0292 }
            r2.<init>(r1)     // Catch:{ all -> 0x0292 }
            kotlin.sequences.Sequence r1 = com.android.systemui.util.ConvenienceExtensionsKt.takeUntil(r8, r2)     // Catch:{ all -> 0x0292 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0292 }
        L_0x00e3:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x0292 }
            if (r2 == 0) goto L_0x0104
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x0292 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r2 = (com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState) r2     // Catch:{ all -> 0x0292 }
            java.lang.Integer r3 = r2.getTargetPosition()     // Catch:{ all -> 0x0292 }
            if (r3 != 0) goto L_0x00f7
            r3 = 0
            goto L_0x0100
        L_0x00f7:
            int r3 = r3.intValue()     // Catch:{ all -> 0x0292 }
            int r3 = r3 - r9
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x0292 }
        L_0x0100:
            r2.setTargetPosition(r3)     // Catch:{ all -> 0x0292 }
            goto L_0x00e3
        L_0x0104:
            r1 = r23
        L_0x0106:
            boolean r2 = r1 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow     // Catch:{ all -> 0x0292 }
            if (r2 == 0) goto L_0x010e
            r3 = r1
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r3 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r3     // Catch:{ all -> 0x0292 }
            goto L_0x010f
        L_0x010e:
            r3 = 0
        L_0x010f:
            if (r3 != 0) goto L_0x0113
        L_0x0111:
            r3 = 0
            goto L_0x0120
        L_0x0113:
            int r2 = r3.getVisibility()     // Catch:{ all -> 0x0292 }
            r4 = 8
            if (r2 != r4) goto L_0x011d
            r2 = r9
            goto L_0x011e
        L_0x011d:
            r2 = 0
        L_0x011e:
            if (r2 != 0) goto L_0x0111
        L_0x0120:
            if (r18 != 0) goto L_0x014e
            if (r17 != 0) goto L_0x0126
            r2 = 0
            goto L_0x0148
        L_0x0126:
            int r2 = r17.intValue()     // Catch:{ all -> 0x0292 }
            if (r3 != 0) goto L_0x012e
        L_0x012c:
            r2 = 0
            goto L_0x0142
        L_0x012e:
            com.android.systemui.statusbar.notification.collection.NotificationEntry r4 = r3.getEntry()     // Catch:{ all -> 0x0292 }
            if (r4 != 0) goto L_0x0135
            goto L_0x012c
        L_0x0135:
            int r4 = r4.getBucket()     // Catch:{ all -> 0x0292 }
            if (r2 >= r4) goto L_0x013d
            r2 = r9
            goto L_0x013e
        L_0x013d:
            r2 = 0
        L_0x013e:
            java.lang.Boolean r2 = java.lang.Boolean.valueOf(r2)     // Catch:{ all -> 0x0292 }
        L_0x0142:
            java.lang.Boolean r4 = java.lang.Boolean.TRUE     // Catch:{ all -> 0x0292 }
            boolean r2 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2, (java.lang.Object) r4)     // Catch:{ all -> 0x0292 }
        L_0x0148:
            if (r2 == 0) goto L_0x014b
            goto L_0x014e
        L_0x014b:
            r18 = 0
            goto L_0x0150
        L_0x014e:
            r18 = r9
        L_0x0150:
            if (r18 == 0) goto L_0x0162
            if (r3 != 0) goto L_0x0156
            r2 = 0
            goto L_0x015a
        L_0x0156:
            com.android.systemui.statusbar.notification.collection.NotificationEntry r2 = r3.getEntry()     // Catch:{ all -> 0x0292 }
        L_0x015a:
            if (r2 != 0) goto L_0x015d
            goto L_0x0162
        L_0x015d:
            r4 = 2
            r2.setBucket(r4)     // Catch:{ all -> 0x0292 }
            goto L_0x0163
        L_0x0162:
            r4 = 2
        L_0x0163:
            if (r17 == 0) goto L_0x0179
            if (r1 == 0) goto L_0x0177
            if (r3 == 0) goto L_0x0179
            com.android.systemui.statusbar.notification.collection.NotificationEntry r1 = r3.getEntry()     // Catch:{ all -> 0x0292 }
            int r1 = r1.getBucket()     // Catch:{ all -> 0x0292 }
            int r2 = r17.intValue()     // Catch:{ all -> 0x0292 }
            if (r2 == r1) goto L_0x0179
        L_0x0177:
            r1 = r9
            goto L_0x017a
        L_0x0179:
            r1 = 0
        L_0x017a:
            if (r1 == 0) goto L_0x0197
            if (r10 == 0) goto L_0x0197
            r1 = 6
            if (r17 != 0) goto L_0x0182
            goto L_0x0197
        L_0x0182:
            int r2 = r17.intValue()     // Catch:{ all -> 0x0292 }
            if (r2 != r1) goto L_0x0197
            r1 = r21
            if (r1 != 0) goto L_0x018d
            goto L_0x0199
        L_0x018d:
            int r2 = r20 + 1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x0292 }
            r1.setTargetPosition(r2)     // Catch:{ all -> 0x0292 }
            goto L_0x0199
        L_0x0197:
            r1 = r21
        L_0x0199:
            if (r3 != 0) goto L_0x019e
            r2 = -1
            r5 = 4
            goto L_0x01bf
        L_0x019e:
            if (r19 != 0) goto L_0x01af
            com.android.systemui.statusbar.notification.collection.NotificationEntry r2 = r3.getEntry()     // Catch:{ all -> 0x0292 }
            int r2 = r2.getBucket()     // Catch:{ all -> 0x0292 }
            r5 = 4
            if (r2 != r5) goto L_0x01ac
            goto L_0x01b0
        L_0x01ac:
            r19 = 0
            goto L_0x01b2
        L_0x01af:
            r5 = 4
        L_0x01b0:
            r19 = r9
        L_0x01b2:
            com.android.systemui.statusbar.notification.collection.NotificationEntry r2 = r3.getEntry()     // Catch:{ all -> 0x0292 }
            int r2 = r2.getBucket()     // Catch:{ all -> 0x0292 }
            java.lang.Integer r17 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x0292 }
            r2 = -1
        L_0x01bf:
            if (r2 <= r12) goto L_0x01c2
            goto L_0x01ce
        L_0x01c2:
            r7 = r1
            r3 = r2
            r6 = r4
            r4 = r8
            r2 = r12
            r8 = 0
            goto L_0x009d
        L_0x01ca:
            r2 = r3
            r8 = r4
            r4 = r6
            r1 = r7
        L_0x01ce:
            if (r13 != 0) goto L_0x01d1
            goto L_0x01dd
        L_0x01d1:
            if (r11 == 0) goto L_0x01d9
            r3 = 0
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x0292 }
            goto L_0x01da
        L_0x01d9:
            r3 = 0
        L_0x01da:
            r13.setTargetPosition(r3)     // Catch:{ all -> 0x0292 }
        L_0x01dd:
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r3 = r0.logger     // Catch:{ all -> 0x0292 }
            java.lang.String r5 = "New header target positions:"
            r3.logStr(r5)     // Catch:{ all -> 0x0292 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r3 = r0.logger     // Catch:{ all -> 0x0292 }
            if (r13 != 0) goto L_0x01ea
        L_0x01e8:
            r5 = r2
            goto L_0x01f5
        L_0x01ea:
            java.lang.Integer r5 = r13.getTargetPosition()     // Catch:{ all -> 0x0292 }
            if (r5 != 0) goto L_0x01f1
            goto L_0x01e8
        L_0x01f1:
            int r5 = r5.intValue()     // Catch:{ all -> 0x0292 }
        L_0x01f5:
            r3.logMediaControls(r5)     // Catch:{ all -> 0x0292 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r3 = r0.logger     // Catch:{ all -> 0x0292 }
            if (r14 != 0) goto L_0x01fe
        L_0x01fc:
            r5 = r2
            goto L_0x0209
        L_0x01fe:
            java.lang.Integer r5 = r14.getTargetPosition()     // Catch:{ all -> 0x0292 }
            if (r5 != 0) goto L_0x0205
            goto L_0x01fc
        L_0x0205:
            int r5 = r5.intValue()     // Catch:{ all -> 0x0292 }
        L_0x0209:
            r3.logIncomingHeader(r5)     // Catch:{ all -> 0x0292 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r3 = r0.logger     // Catch:{ all -> 0x0292 }
            if (r15 != 0) goto L_0x0212
        L_0x0210:
            r5 = r2
            goto L_0x021d
        L_0x0212:
            java.lang.Integer r5 = r15.getTargetPosition()     // Catch:{ all -> 0x0292 }
            if (r5 != 0) goto L_0x0219
            goto L_0x0210
        L_0x0219:
            int r5 = r5.intValue()     // Catch:{ all -> 0x0292 }
        L_0x021d:
            r3.logConversationsHeader(r5)     // Catch:{ all -> 0x0292 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r3 = r0.logger     // Catch:{ all -> 0x0292 }
            if (r16 != 0) goto L_0x0226
        L_0x0224:
            r5 = r2
            goto L_0x0231
        L_0x0226:
            java.lang.Integer r5 = r16.getTargetPosition()     // Catch:{ all -> 0x0292 }
            if (r5 != 0) goto L_0x022d
            goto L_0x0224
        L_0x022d:
            int r5 = r5.intValue()     // Catch:{ all -> 0x0292 }
        L_0x0231:
            r3.logAlertingHeader(r5)     // Catch:{ all -> 0x0292 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r3 = r0.logger     // Catch:{ all -> 0x0292 }
            if (r1 != 0) goto L_0x0239
            goto L_0x0245
        L_0x0239:
            java.lang.Integer r1 = r1.getTargetPosition()     // Catch:{ all -> 0x0292 }
            if (r1 != 0) goto L_0x0240
            goto L_0x0245
        L_0x0240:
            int r1 = r1.intValue()     // Catch:{ all -> 0x0292 }
            r2 = r1
        L_0x0245:
            r3.logSilentHeader(r2)     // Catch:{ all -> 0x0292 }
            java.lang.Iterable r1 = kotlin.sequences.SequencesKt___SequencesKt.asIterable(r8)     // Catch:{ all -> 0x0292 }
            java.util.List r1 = kotlin.collections.CollectionsKt___CollectionsKt.reversed(r1)     // Catch:{ all -> 0x0292 }
            java.lang.Iterable r1 = (java.lang.Iterable) r1     // Catch:{ all -> 0x0292 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0292 }
        L_0x0256:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x0292 }
            if (r2 == 0) goto L_0x0266
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x0292 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsManager$SectionUpdateState r2 = (com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.SectionUpdateState) r2     // Catch:{ all -> 0x0292 }
            r2.adjustViewPosition()     // Catch:{ all -> 0x0292 }
            goto L_0x0256
        L_0x0266:
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r1 = r0.logger     // Catch:{ all -> 0x0292 }
            java.lang.String r2 = "Final order:"
            r1.logStr(r2)     // Catch:{ all -> 0x0292 }
            r22.logShadeContents()     // Catch:{ all -> 0x0292 }
            com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger r1 = r0.logger     // Catch:{ all -> 0x0292 }
            java.lang.String r2 = "Section boundary update complete"
            r1.logStr(r2)     // Catch:{ all -> 0x0292 }
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r1 = r22.getSilentHeaderView()     // Catch:{ all -> 0x0292 }
            if (r1 != 0) goto L_0x027e
            goto L_0x028c
        L_0x027e:
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r0 = r0.parent     // Catch:{ all -> 0x0292 }
            if (r0 != 0) goto L_0x0284
            r12 = 0
            goto L_0x0285
        L_0x0284:
            r12 = r0
        L_0x0285:
            boolean r0 = r12.hasActiveClearableNotifications(r4)     // Catch:{ all -> 0x0292 }
            r1.setClearSectionButtonEnabled(r0)     // Catch:{ all -> 0x0292 }
        L_0x028c:
            kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0292 }
            android.os.Trace.endSection()
            return r0
        L_0x0292:
            r0 = move-exception
            android.os.Trace.endSection()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.NotificationSectionsManager.updateSectionBoundaries(java.lang.String):kotlin.Unit");
    }

    public NotificationSectionsManager(@NotNull StatusBarStateController statusBarStateController2, @NotNull ConfigurationController configurationController2, @NotNull KeyguardMediaController keyguardMediaController2, @NotNull NotificationSectionsFeatureManager notificationSectionsFeatureManager, @NotNull NotificationSectionsLogger notificationSectionsLogger, @NotNull NotifPipelineFlags notifPipelineFlags2, @NotNull MediaContainerController mediaContainerController2, @NotNull SectionHeaderController sectionHeaderController, @NotNull SectionHeaderController sectionHeaderController2, @NotNull SectionHeaderController sectionHeaderController3, @NotNull SectionHeaderController sectionHeaderController4) {
        this.statusBarStateController = statusBarStateController2;
        this.configurationController = configurationController2;
        this.keyguardMediaController = keyguardMediaController2;
        this.sectionsFeatureManager = notificationSectionsFeatureManager;
        this.logger = notificationSectionsLogger;
        this.notifPipelineFlags = notifPipelineFlags2;
        this.mediaContainerController = mediaContainerController2;
        this.incomingHeaderController = sectionHeaderController;
        this.peopleHeaderController = sectionHeaderController2;
        this.alertingHeaderController = sectionHeaderController3;
        this.silentHeaderController = sectionHeaderController4;
    }

    @Nullable
    public final SectionHeaderView getSilentHeaderView() {
        return this.silentHeaderController.getHeaderView();
    }

    @Nullable
    public final SectionHeaderView getAlertingHeaderView() {
        return this.alertingHeaderController.getHeaderView();
    }

    @Nullable
    public final SectionHeaderView getIncomingHeaderView() {
        return this.incomingHeaderController.getHeaderView();
    }

    @Nullable
    public final SectionHeaderView getPeopleHeaderView() {
        return this.peopleHeaderController.getHeaderView();
    }

    @Nullable
    public final MediaContainerView getMediaControlsView() {
        return this.mediaContainerController.getMediaContainerView();
    }

    public final void initialize(@NotNull NotificationStackScrollLayout notificationStackScrollLayout) {
        if (!this.initialized) {
            this.initialized = true;
            this.parent = notificationStackScrollLayout;
            reinflateViews();
            this.configurationController.addCallback(this.configurationListener);
            return;
        }
        throw new IllegalStateException("NotificationSectionsManager already initialized".toString());
    }

    @NotNull
    public final NotificationSection[] createSectionsForBuckets() {
        int[] notificationBuckets = this.sectionsFeatureManager.getNotificationBuckets();
        ArrayList arrayList = new ArrayList(notificationBuckets.length);
        int length = notificationBuckets.length;
        int i = 0;
        while (i < length) {
            int i2 = notificationBuckets[i];
            i++;
            NotificationStackScrollLayout notificationStackScrollLayout = this.parent;
            if (notificationStackScrollLayout == null) {
                notificationStackScrollLayout = null;
            }
            arrayList.add(new NotificationSection(notificationStackScrollLayout, i2));
        }
        Object[] array = arrayList.toArray(new NotificationSection[0]);
        if (array != null) {
            return (NotificationSection[]) array;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
    }

    public final void reinflateViews() {
        SectionHeaderController sectionHeaderController = this.silentHeaderController;
        NotificationStackScrollLayout notificationStackScrollLayout = this.parent;
        NotificationStackScrollLayout notificationStackScrollLayout2 = null;
        if (notificationStackScrollLayout == null) {
            notificationStackScrollLayout = null;
        }
        sectionHeaderController.reinflateView(notificationStackScrollLayout);
        SectionHeaderController sectionHeaderController2 = this.alertingHeaderController;
        NotificationStackScrollLayout notificationStackScrollLayout3 = this.parent;
        if (notificationStackScrollLayout3 == null) {
            notificationStackScrollLayout3 = null;
        }
        sectionHeaderController2.reinflateView(notificationStackScrollLayout3);
        SectionHeaderController sectionHeaderController3 = this.peopleHeaderController;
        NotificationStackScrollLayout notificationStackScrollLayout4 = this.parent;
        if (notificationStackScrollLayout4 == null) {
            notificationStackScrollLayout4 = null;
        }
        sectionHeaderController3.reinflateView(notificationStackScrollLayout4);
        SectionHeaderController sectionHeaderController4 = this.incomingHeaderController;
        NotificationStackScrollLayout notificationStackScrollLayout5 = this.parent;
        if (notificationStackScrollLayout5 == null) {
            notificationStackScrollLayout5 = null;
        }
        sectionHeaderController4.reinflateView(notificationStackScrollLayout5);
        MediaContainerController mediaContainerController2 = this.mediaContainerController;
        NotificationStackScrollLayout notificationStackScrollLayout6 = this.parent;
        if (notificationStackScrollLayout6 != null) {
            notificationStackScrollLayout2 = notificationStackScrollLayout6;
        }
        mediaContainerController2.reinflateView(notificationStackScrollLayout2);
        this.keyguardMediaController.attachSinglePaneContainer(getMediaControlsView());
    }

    public boolean beginsSection(@NotNull View view, @Nullable View view2) {
        return view == getSilentHeaderView() || view == getMediaControlsView() || view == getPeopleHeaderView() || view == getAlertingHeaderView() || view == getIncomingHeaderView() || !Intrinsics.areEqual((Object) getBucket(view), (Object) getBucket(view2));
    }

    public final Integer getBucket(View view) {
        if (view == getSilentHeaderView()) {
            return 6;
        }
        if (view == getIncomingHeaderView()) {
            return 2;
        }
        if (view == getMediaControlsView()) {
            return 1;
        }
        if (view == getPeopleHeaderView()) {
            return 4;
        }
        if (view == getAlertingHeaderView()) {
            return 5;
        }
        if (view instanceof ExpandableNotificationRow) {
            return Integer.valueOf(((ExpandableNotificationRow) view).getEntry().getBucket());
        }
        return null;
    }

    public final void logShadeChild(int i, View view) {
        if (view == getIncomingHeaderView()) {
            this.logger.logIncomingHeader(i);
        } else if (view == getMediaControlsView()) {
            this.logger.logMediaControls(i);
        } else if (view == getPeopleHeaderView()) {
            this.logger.logConversationsHeader(i);
        } else if (view == getAlertingHeaderView()) {
            this.logger.logAlertingHeader(i);
        } else if (view == getSilentHeaderView()) {
            this.logger.logSilentHeader(i);
        } else if (!(view instanceof ExpandableNotificationRow)) {
            this.logger.logOther(i, view.getClass());
        } else {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            boolean isHeadsUp = expandableNotificationRow.isHeadsUp();
            int bucket = expandableNotificationRow.getEntry().getBucket();
            if (bucket == 2) {
                this.logger.logHeadsUp(i, isHeadsUp);
            } else if (bucket == 4) {
                this.logger.logConversation(i, isHeadsUp);
            } else if (bucket == 5) {
                this.logger.logAlerting(i, isHeadsUp);
            } else if (bucket == 6) {
                this.logger.logSilent(i, isHeadsUp);
            }
        }
    }

    public final boolean isUsingMultipleSections() {
        return this.sectionsFeatureManager.getNumberOfBuckets() > 1;
    }

    @VisibleForTesting
    @Nullable
    public final Unit updateSectionBoundaries() {
        return updateSectionBoundaries("test");
    }

    public final <T extends ExpandableView> SectionUpdateState<T> expandableViewHeaderState(T t) {
        return new NotificationSectionsManager$expandableViewHeaderState$1(t, this);
    }

    public final <T extends StackScrollerDecorView> SectionUpdateState<T> decorViewHeaderState(T t) {
        this.notifPipelineFlags.checkLegacyPipelineEnabled();
        return new NotificationSectionsManager$decorViewHeaderState$1(expandableViewHeaderState(t), t);
    }

    /* renamed from: updateSectionBoundaries$lambda-16$getSectionState  reason: not valid java name */
    public static final SectionUpdateState<ExpandableView> m2573updateSectionBoundaries$lambda16$getSectionState(NotificationSectionsManager notificationSectionsManager, SectionUpdateState<MediaContainerView> sectionUpdateState, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState2, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState3, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState4, SectionUpdateState<? extends SectionHeaderView> sectionUpdateState5, View view) {
        if (view == notificationSectionsManager.getMediaControlsView()) {
            return sectionUpdateState;
        }
        if (view == notificationSectionsManager.getIncomingHeaderView()) {
            return sectionUpdateState2;
        }
        if (view == notificationSectionsManager.getPeopleHeaderView()) {
            return sectionUpdateState3;
        }
        if (view == notificationSectionsManager.getAlertingHeaderView()) {
            return sectionUpdateState4;
        }
        if (view == notificationSectionsManager.getSilentHeaderView()) {
            return sectionUpdateState5;
        }
        return null;
    }

    /* compiled from: NotificationSectionsManager.kt */
    public static abstract class SectionBounds {
        public /* synthetic */ SectionBounds(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public SectionBounds() {
        }

        /* compiled from: NotificationSectionsManager.kt */
        public static final class Many extends SectionBounds {
            @NotNull
            public final ExpandableView first;
            @NotNull
            public final ExpandableView last;

            public static /* synthetic */ Many copy$default(Many many, ExpandableView expandableView, ExpandableView expandableView2, int i, Object obj) {
                if ((i & 1) != 0) {
                    expandableView = many.first;
                }
                if ((i & 2) != 0) {
                    expandableView2 = many.last;
                }
                return many.copy(expandableView, expandableView2);
            }

            @NotNull
            public final Many copy(@NotNull ExpandableView expandableView, @NotNull ExpandableView expandableView2) {
                return new Many(expandableView, expandableView2);
            }

            public boolean equals(@Nullable Object obj) {
                if (this == obj) {
                    return true;
                }
                if (!(obj instanceof Many)) {
                    return false;
                }
                Many many = (Many) obj;
                return Intrinsics.areEqual((Object) this.first, (Object) many.first) && Intrinsics.areEqual((Object) this.last, (Object) many.last);
            }

            public int hashCode() {
                return (this.first.hashCode() * 31) + this.last.hashCode();
            }

            @NotNull
            public String toString() {
                return "Many(first=" + this.first + ", last=" + this.last + ')';
            }

            @NotNull
            public final ExpandableView getFirst() {
                return this.first;
            }

            @NotNull
            public final ExpandableView getLast() {
                return this.last;
            }

            public Many(@NotNull ExpandableView expandableView, @NotNull ExpandableView expandableView2) {
                super((DefaultConstructorMarker) null);
                this.first = expandableView;
                this.last = expandableView2;
            }
        }

        /* compiled from: NotificationSectionsManager.kt */
        public static final class One extends SectionBounds {
            @NotNull
            public final ExpandableView lone;

            public boolean equals(@Nullable Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof One) && Intrinsics.areEqual((Object) this.lone, (Object) ((One) obj).lone);
            }

            public int hashCode() {
                return this.lone.hashCode();
            }

            @NotNull
            public String toString() {
                return "One(lone=" + this.lone + ')';
            }

            public One(@NotNull ExpandableView expandableView) {
                super((DefaultConstructorMarker) null);
                this.lone = expandableView;
            }

            @NotNull
            public final ExpandableView getLone() {
                return this.lone;
            }
        }

        /* compiled from: NotificationSectionsManager.kt */
        public static final class None extends SectionBounds {
            @NotNull
            public static final None INSTANCE = new None();

            public None() {
                super((DefaultConstructorMarker) null);
            }
        }

        @NotNull
        public final SectionBounds addNotif(@NotNull ExpandableView expandableView) {
            if (this instanceof None) {
                return new One(expandableView);
            }
            if (this instanceof One) {
                return new Many(((One) this).getLone(), expandableView);
            }
            if (this instanceof Many) {
                return Many.copy$default((Many) this, (ExpandableView) null, expandableView, 1, (Object) null);
            }
            throw new NoWhenBranchMatchedException();
        }

        public final boolean updateSection(@NotNull NotificationSection notificationSection) {
            if (this instanceof None) {
                return setFirstAndLastVisibleChildren(notificationSection, (ExpandableView) null, (ExpandableView) null);
            }
            if (this instanceof One) {
                One one = (One) this;
                return setFirstAndLastVisibleChildren(notificationSection, one.getLone(), one.getLone());
            } else if (this instanceof Many) {
                Many many = (Many) this;
                return setFirstAndLastVisibleChildren(notificationSection, many.getFirst(), many.getLast());
            } else {
                throw new NoWhenBranchMatchedException();
            }
        }

        public final boolean setFirstAndLastVisibleChildren(NotificationSection notificationSection, ExpandableView expandableView, ExpandableView expandableView2) {
            return notificationSection.setFirstVisibleChild(expandableView) || notificationSection.setLastVisibleChild(expandableView2);
        }
    }

    public final boolean updateFirstAndLastViewsForAllSections(@NotNull NotificationSection[] notificationSectionArr, @NotNull List<? extends ExpandableView> list) {
        SparseArray sparseArray;
        NotificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1 notificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1 = new NotificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1(CollectionsKt___CollectionsKt.asSequence(list), this);
        SectionBounds.None none = SectionBounds.None.INSTANCE;
        int length = notificationSectionArr.length;
        if (length < 0) {
            sparseArray = new SparseArray();
        } else {
            sparseArray = new SparseArray(length);
        }
        Iterator sourceIterator = notificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1.sourceIterator();
        while (sourceIterator.hasNext()) {
            Object next = sourceIterator.next();
            int intValue = ((Number) notificationSectionsManager$updateFirstAndLastViewsForAllSections$$inlined$groupingBy$1.keyOf(next)).intValue();
            Object obj = sparseArray.get(intValue);
            if (obj == null) {
                obj = none;
            }
            sparseArray.put(intValue, ((SectionBounds) obj).addNotif((ExpandableView) next));
        }
        int length2 = notificationSectionArr.length;
        int i = 0;
        boolean z = false;
        while (i < length2) {
            NotificationSection notificationSection = notificationSectionArr[i];
            i++;
            SectionBounds sectionBounds = (SectionBounds) sparseArray.get(notificationSection.getBucket());
            if (sectionBounds == null) {
                sectionBounds = SectionBounds.None.INSTANCE;
            }
            z = sectionBounds.updateSection(notificationSection) || z;
        }
        return z;
    }

    public final void setHeaderForegroundColor(int i) {
        SectionHeaderView peopleHeaderView = getPeopleHeaderView();
        if (peopleHeaderView != null) {
            peopleHeaderView.setForegroundColor(i);
        }
        SectionHeaderView silentHeaderView = getSilentHeaderView();
        if (silentHeaderView != null) {
            silentHeaderView.setForegroundColor(i);
        }
        SectionHeaderView alertingHeaderView = getAlertingHeaderView();
        if (alertingHeaderView != null) {
            alertingHeaderView.setForegroundColor(i);
        }
    }

    /* compiled from: NotificationSectionsManager.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
