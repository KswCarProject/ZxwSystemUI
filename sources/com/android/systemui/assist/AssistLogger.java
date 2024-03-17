package com.android.systemui.assist;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import com.android.internal.app.AssistUtils;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.FrameworkStatsLog;
import com.android.keyguard.KeyguardUpdateMonitor;
import java.util.Set;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AssistLogger.kt */
public class AssistLogger {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final Set<AssistantSessionEvent> SESSION_END_EVENTS = SetsKt__SetsKt.setOf(AssistantSessionEvent.ASSISTANT_SESSION_INVOCATION_CANCELLED, AssistantSessionEvent.ASSISTANT_SESSION_CLOSE);
    @NotNull
    public final AssistUtils assistUtils;
    @NotNull
    public final Context context;
    @Nullable
    public InstanceId currentInstanceId;
    @NotNull
    public final InstanceIdSequence instanceIdSequence = new InstanceIdSequence(1048576);
    @NotNull
    public final PhoneStateMonitor phoneStateMonitor;
    @NotNull
    public final UiEventLogger uiEventLogger;

    public void reportAssistantInvocationExtraData() {
    }

    public AssistLogger(@NotNull Context context2, @NotNull UiEventLogger uiEventLogger2, @NotNull AssistUtils assistUtils2, @NotNull PhoneStateMonitor phoneStateMonitor2) {
        this.context = context2;
        this.uiEventLogger = uiEventLogger2;
        this.assistUtils = assistUtils2;
        this.phoneStateMonitor = phoneStateMonitor2;
    }

    public final void reportAssistantInvocationEventFromLegacy(int i, boolean z, @Nullable ComponentName componentName, @Nullable Integer num) {
        reportAssistantInvocationEvent(AssistantInvocationEvent.Companion.eventFromLegacyInvocationType(i, z), componentName, num == null ? null : Integer.valueOf(AssistantInvocationEvent.Companion.deviceStateFromLegacyDeviceState(num.intValue())));
    }

    public final void reportAssistantInvocationEvent(@NotNull UiEventLogger.UiEventEnum uiEventEnum, @Nullable ComponentName componentName, @Nullable Integer num) {
        int i;
        if (componentName == null) {
            componentName = getAssistantComponentForCurrentUser();
        }
        int assistantUid = getAssistantUid(componentName);
        if (num == null) {
            i = AssistantInvocationEvent.Companion.deviceStateFromLegacyDeviceState(this.phoneStateMonitor.getPhoneState());
        } else {
            i = num.intValue();
        }
        FrameworkStatsLog.write(281, uiEventEnum.getId(), assistantUid, componentName.flattenToString(), getOrCreateInstanceId().getId(), i, false);
        reportAssistantInvocationExtraData();
    }

    public final void reportAssistantSessionEvent(@NotNull UiEventLogger.UiEventEnum uiEventEnum) {
        ComponentName assistantComponentForCurrentUser = getAssistantComponentForCurrentUser();
        this.uiEventLogger.logWithInstanceId(uiEventEnum, getAssistantUid(assistantComponentForCurrentUser), assistantComponentForCurrentUser.flattenToString(), getOrCreateInstanceId());
        if (CollectionsKt___CollectionsKt.contains(SESSION_END_EVENTS, uiEventEnum)) {
            clearInstanceId();
        }
    }

    @NotNull
    public final InstanceId getOrCreateInstanceId() {
        InstanceId instanceId = this.currentInstanceId;
        if (instanceId == null) {
            instanceId = this.instanceIdSequence.newInstanceId();
        }
        this.currentInstanceId = instanceId;
        return instanceId;
    }

    public final void clearInstanceId() {
        this.currentInstanceId = null;
    }

    @NotNull
    public final ComponentName getAssistantComponentForCurrentUser() {
        return this.assistUtils.getAssistComponentForUser(KeyguardUpdateMonitor.getCurrentUser());
    }

    public final int getAssistantUid(@NotNull ComponentName componentName) {
        try {
            return this.context.getPackageManager().getApplicationInfo(componentName.getPackageName(), 0).uid;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AssistLogger", "Unable to find Assistant UID", e);
            return 0;
        }
    }

    /* compiled from: AssistLogger.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
