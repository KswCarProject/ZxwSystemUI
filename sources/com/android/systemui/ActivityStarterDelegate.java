package com.android.systemui;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.UserHandle;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.Lazy;
import java.util.Optional;

public class ActivityStarterDelegate implements ActivityStarter {
    public Lazy<Optional<CentralSurfaces>> mActualStarterOptionalLazy;

    public ActivityStarterDelegate(Lazy<Optional<CentralSurfaces>> lazy) {
        this.mActualStarterOptionalLazy = lazy;
    }

    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda4(pendingIntent));
    }

    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda9(pendingIntent, runnable));
    }

    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable, View view) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda11(pendingIntent, runnable, view));
    }

    public void startPendingIntentDismissingKeyguard(PendingIntent pendingIntent, Runnable runnable, ActivityLaunchAnimator.Controller controller) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda0(pendingIntent, runnable, controller));
    }

    public void startActivity(Intent intent, boolean z, boolean z2, int i) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda5(intent, z, z2, i));
    }

    public void startActivity(Intent intent, boolean z) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda2(intent, z));
    }

    public void startActivity(Intent intent, boolean z, ActivityLaunchAnimator.Controller controller, boolean z2) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda14(intent, z, controller, z2));
    }

    public void startActivity(Intent intent, boolean z, ActivityLaunchAnimator.Controller controller, boolean z2, UserHandle userHandle) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda15(intent, z, controller, z2, userHandle));
    }

    public void startActivity(Intent intent, boolean z, boolean z2) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda1(intent, z, z2));
    }

    public void startActivity(Intent intent, boolean z, ActivityStarter.Callback callback) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda8(intent, z, callback));
    }

    public void postStartActivityDismissingKeyguard(Intent intent, int i) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda10(intent, i));
    }

    public void postStartActivityDismissingKeyguard(Intent intent, int i, ActivityLaunchAnimator.Controller controller) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda7(intent, i, controller));
    }

    public void postStartActivityDismissingKeyguard(PendingIntent pendingIntent) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda13(pendingIntent));
    }

    public void postStartActivityDismissingKeyguard(PendingIntent pendingIntent, ActivityLaunchAnimator.Controller controller) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda3(pendingIntent, controller));
    }

    public void postQSRunnableDismissingKeyguard(Runnable runnable) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda6(runnable));
    }

    public void dismissKeyguardThenExecute(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z) {
        this.mActualStarterOptionalLazy.get().ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda12(onDismissAction, runnable, z));
    }
}
