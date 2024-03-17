package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.Configuration;
import android.util.IndentingPrintWriter;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.LargeScreenUtils;
import java.io.PrintWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AbstractLockscreenShadeTransitionController.kt */
public abstract class AbstractLockscreenShadeTransitionController implements Dumpable {
    @NotNull
    public final Context context;
    public float dragDownAmount;
    public boolean useSplitShade;

    public abstract void dump(@NotNull IndentingPrintWriter indentingPrintWriter);

    public abstract void onDragDownAmountChanged(float f);

    public abstract void updateResources();

    public AbstractLockscreenShadeTransitionController(@NotNull Context context2, @NotNull ConfigurationController configurationController, @NotNull DumpManager dumpManager) {
        this.context = context2;
        updateResourcesInternal();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            public final /* synthetic */ AbstractLockscreenShadeTransitionController this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(@Nullable Configuration configuration) {
                this.this$0.updateResourcesInternal();
            }
        });
        dumpManager.registerDumpable(this);
    }

    @NotNull
    public final Context getContext() {
        return this.context;
    }

    public final boolean getUseSplitShade() {
        return this.useSplitShade;
    }

    public final float getDragDownAmount() {
        return this.dragDownAmount;
    }

    public final void setDragDownAmount(float f) {
        if (!(f == this.dragDownAmount)) {
            this.dragDownAmount = f;
            onDragDownAmountChanged(f);
        }
    }

    public final void updateResourcesInternal() {
        this.useSplitShade = LargeScreenUtils.shouldUseSplitNotificationShade(this.context.getResources());
        updateResources();
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        dump(new IndentingPrintWriter(printWriter, "  "));
    }
}
