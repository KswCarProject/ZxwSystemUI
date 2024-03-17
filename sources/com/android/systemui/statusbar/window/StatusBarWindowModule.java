package com.android.systemui.statusbar.window;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.systemui.R$layout;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarWindowModule.kt */
public abstract class StatusBarWindowModule {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    @NotNull
    public static final StatusBarWindowView providesStatusBarWindowView(@NotNull LayoutInflater layoutInflater) {
        return Companion.providesStatusBarWindowView(layoutInflater);
    }

    /* compiled from: StatusBarWindowModule.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @NotNull
        public final StatusBarWindowView providesStatusBarWindowView(@NotNull LayoutInflater layoutInflater) {
            StatusBarWindowView statusBarWindowView = (StatusBarWindowView) layoutInflater.inflate(R$layout.super_status_bar, (ViewGroup) null);
            if (statusBarWindowView != null) {
                return statusBarWindowView;
            }
            throw new IllegalStateException("R.layout.super_status_bar could not be properly inflated");
        }
    }
}
