package com.android.wm.shell.dagger;

import android.animation.AnimationHandler;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.android.wm.shell.R;
import com.android.wm.shell.common.HandlerExecutor;
import com.android.wm.shell.common.ShellExecutor;

public abstract class WMShellConcurrencyModule {
    public static boolean enableShellMainThread(Context context) {
        return context.getResources().getBoolean(R.bool.config_enableShellMainThread);
    }

    public static Handler provideMainHandler() {
        return new Handler(Looper.getMainLooper());
    }

    public static ShellExecutor provideSysUIMainExecutor(Handler handler) {
        return new HandlerExecutor(handler);
    }

    public static HandlerThread createShellMainThread() {
        return new HandlerThread("wmshell.main", -4);
    }

    public static Handler provideShellMainHandler(Context context, HandlerThread handlerThread, Handler handler) {
        if (!enableShellMainThread(context)) {
            return handler;
        }
        if (handlerThread == null) {
            handlerThread = createShellMainThread();
            handlerThread.start();
        }
        if (Build.IS_DEBUGGABLE) {
            handlerThread.getLooper().setTraceTag(32);
            handlerThread.getLooper().setSlowLogThresholdMs(30, 30);
        }
        return Handler.createAsync(handlerThread.getLooper());
    }

    public static ShellExecutor provideShellMainExecutor(Context context, Handler handler, ShellExecutor shellExecutor) {
        return enableShellMainThread(context) ? new HandlerExecutor(handler) : shellExecutor;
    }

    public static ShellExecutor provideShellAnimationExecutor() {
        HandlerThread handlerThread = new HandlerThread("wmshell.anim", -4);
        handlerThread.start();
        if (Build.IS_DEBUGGABLE) {
            handlerThread.getLooper().setTraceTag(32);
            handlerThread.getLooper().setSlowLogThresholdMs(30, 30);
        }
        return new HandlerExecutor(Handler.createAsync(handlerThread.getLooper()));
    }

    public static ShellExecutor provideSplashScreenExecutor() {
        HandlerThread handlerThread = new HandlerThread("wmshell.splashscreen", -10);
        handlerThread.start();
        return new HandlerExecutor(handlerThread.getThreadHandler());
    }

    public static AnimationHandler provideShellMainExecutorSfVsyncAnimationHandler(ShellExecutor shellExecutor) {
        try {
            AnimationHandler animationHandler = new AnimationHandler();
            shellExecutor.executeBlocking(new WMShellConcurrencyModule$$ExternalSyntheticLambda0(animationHandler));
            return animationHandler;
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to initialize SfVsync animation handler in 1s", e);
        }
    }

    public static Handler provideSharedBackgroundHandler() {
        HandlerThread handlerThread = new HandlerThread("wmshell.background", 10);
        handlerThread.start();
        return handlerThread.getThreadHandler();
    }

    public static ShellExecutor provideSharedBackgroundExecutor(Handler handler) {
        return new HandlerExecutor(handler);
    }
}
