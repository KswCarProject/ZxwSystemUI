package com.android.systemui.statusbar.events;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.internal.annotations.GuardedBy;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsChangedListener;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.leak.RotationUtils;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PrivacyDotViewController.kt */
public final class PrivacyDotViewController {
    @NotNull
    public final SystemStatusAnimationScheduler animationScheduler;
    public View bl;
    public View br;
    @Nullable
    public Runnable cancelRunnable;
    @NotNull
    public final ConfigurationController configurationController;
    @NotNull
    public final StatusBarContentInsetsProvider contentInsetsProvider;
    @NotNull
    public ViewState currentViewState;
    @NotNull
    public final Object lock = new Object();
    @NotNull
    public final Executor mainExecutor;
    @GuardedBy({"lock"})
    @NotNull
    public ViewState nextViewState;
    @Nullable
    public ShowingListener showingListener;
    @NotNull
    public final StatusBarStateController stateController;
    @NotNull
    public final SystemStatusAnimationCallback systemStatusAnimationCallback;
    public View tl;
    public View tr;
    @Nullable
    public DelayableExecutor uiExecutor;

    /* compiled from: PrivacyDotViewController.kt */
    public interface ShowingListener {
        void onPrivacyDotHidden(@Nullable View view);

        void onPrivacyDotShown(@Nullable View view);
    }

    public final int rotatedCorner(int i, int i2) {
        int i3 = i - i2;
        return i3 < 0 ? i3 + 4 : i3;
    }

    public PrivacyDotViewController(@NotNull Executor executor, @NotNull StatusBarStateController statusBarStateController, @NotNull ConfigurationController configurationController2, @NotNull StatusBarContentInsetsProvider statusBarContentInsetsProvider, @NotNull SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        StatusBarStateController statusBarStateController2 = statusBarStateController;
        ConfigurationController configurationController3 = configurationController2;
        StatusBarContentInsetsProvider statusBarContentInsetsProvider2 = statusBarContentInsetsProvider;
        this.mainExecutor = executor;
        this.stateController = statusBarStateController2;
        this.configurationController = configurationController3;
        this.contentInsetsProvider = statusBarContentInsetsProvider2;
        this.animationScheduler = systemStatusAnimationScheduler;
        ViewState viewState = r4;
        ViewState viewState2 = new ViewState(false, false, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8191, (DefaultConstructorMarker) null);
        ViewState viewState3 = viewState;
        this.currentViewState = viewState3;
        this.nextViewState = ViewState.copy$default(viewState3, false, false, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8191, (Object) null);
        statusBarContentInsetsProvider2.addCallback((StatusBarContentInsetsChangedListener) new StatusBarContentInsetsChangedListener(this) {
            public final /* synthetic */ PrivacyDotViewController this$0;

            {
                this.this$0 = r1;
            }

            public void onStatusBarContentInsetsChanged() {
                PrivacyDotViewControllerKt.dlog("onStatusBarContentInsetsChanged: ");
                this.this$0.setNewLayoutRects();
            }
        });
        configurationController3.addCallback(new ConfigurationController.ConfigurationListener(this) {
            public final /* synthetic */ PrivacyDotViewController this$0;

            {
                this.this$0 = r1;
            }

            public void onLayoutDirectionChanged(boolean z) {
                DelayableExecutor access$getUiExecutor$p = this.this$0.uiExecutor;
                if (access$getUiExecutor$p != null) {
                    access$getUiExecutor$p.execute(new PrivacyDotViewController$2$onLayoutDirectionChanged$1(this.this$0, this, z));
                }
            }
        });
        statusBarStateController2.addCallback(new StatusBarStateController.StateListener(this) {
            public final /* synthetic */ PrivacyDotViewController this$0;

            {
                this.this$0 = r1;
            }

            public void onExpandedChanged(boolean z) {
                this.this$0.updateStatusBarState();
            }

            public void onStateChanged(int i) {
                this.this$0.updateStatusBarState();
            }
        });
        this.systemStatusAnimationCallback = new PrivacyDotViewController$systemStatusAnimationCallback$1(this);
    }

    public final void setNextViewState(ViewState viewState) {
        this.nextViewState = viewState;
        scheduleUpdate();
    }

    public final Sequence<View> getViews() {
        View view = this.tl;
        if (view == null) {
            return SequencesKt__SequencesKt.sequenceOf(new View[0]);
        }
        View[] viewArr = new View[4];
        View view2 = null;
        if (view == null) {
            view = null;
        }
        viewArr[0] = view;
        View view3 = this.tr;
        if (view3 == null) {
            view3 = null;
        }
        viewArr[1] = view3;
        View view4 = this.br;
        if (view4 == null) {
            view4 = null;
        }
        viewArr[2] = view4;
        View view5 = this.bl;
        if (view5 != null) {
            view2 = view5;
        }
        viewArr[3] = view2;
        return SequencesKt__SequencesKt.sequenceOf(viewArr);
    }

    public final void setUiExecutor(@NotNull DelayableExecutor delayableExecutor) {
        this.uiExecutor = delayableExecutor;
    }

    public final void setShowingListener(@Nullable ShowingListener showingListener2) {
        this.showingListener = showingListener2;
    }

    public final void setQsExpanded(boolean z) {
        PrivacyDotViewControllerKt.dlog(Intrinsics.stringPlus("setQsExpanded ", Boolean.valueOf(z)));
        synchronized (this.lock) {
            setNextViewState(ViewState.copy$default(this.nextViewState, false, false, false, z, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8183, (Object) null));
            Unit unit = Unit.INSTANCE;
        }
    }

    public final void setNewRotation(int i) {
        Object obj;
        int i2 = i;
        synchronized (this.lock) {
            if (i2 != this.nextViewState.getRotation()) {
                boolean layoutRtl = this.nextViewState.getLayoutRtl();
                Unit unit = Unit.INSTANCE;
                setCornerVisibilities(4);
                View selectDesignatedCorner = selectDesignatedCorner(i2, layoutRtl);
                int cornerIndex = cornerIndex(selectDesignatedCorner);
                int statusBarPaddingTop = this.contentInsetsProvider.getStatusBarPaddingTop(Integer.valueOf(i));
                Object obj2 = this.lock;
                synchronized (obj2) {
                    try {
                        obj = obj2;
                        try {
                            setNextViewState(ViewState.copy$default(this.nextViewState, false, false, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, i, statusBarPaddingTop, cornerIndex, selectDesignatedCorner, 511, (Object) null));
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        obj = obj2;
                        throw th;
                    }
                }
            }
        }
    }

    public final void hideDotView(View view, boolean z) {
        view.clearAnimation();
        if (z) {
            view.animate().setDuration(160).setInterpolator(Interpolators.ALPHA_OUT).alpha(0.0f).withEndAction(new PrivacyDotViewController$hideDotView$1(view, this)).start();
            return;
        }
        view.setVisibility(4);
        ShowingListener showingListener2 = this.showingListener;
        if (showingListener2 != null) {
            showingListener2.onPrivacyDotHidden(view);
        }
    }

    public final void showDotView(View view, boolean z) {
        ShowingListener showingListener2 = this.showingListener;
        if (showingListener2 != null) {
            showingListener2.onPrivacyDotShown(view);
        }
        view.clearAnimation();
        if (z) {
            view.setVisibility(0);
            view.setAlpha(0.0f);
            view.animate().alpha(1.0f).setDuration(160).setInterpolator(Interpolators.ALPHA_IN).start();
            return;
        }
        view.setVisibility(0);
        view.setAlpha(1.0f);
    }

    public final void updateRotations(int i, int i2) {
        for (View next : getViews()) {
            next.setPadding(0, i2, 0, 0);
            int rotatedCorner = rotatedCorner(cornerForView(next), i);
            ViewGroup.LayoutParams layoutParams = next.getLayoutParams();
            if (layoutParams != null) {
                ((FrameLayout.LayoutParams) layoutParams).gravity = PrivacyDotViewControllerKt.toGravity(rotatedCorner);
                ViewGroup.LayoutParams layoutParams2 = next.findViewById(R$id.privacy_dot).getLayoutParams();
                if (layoutParams2 != null) {
                    ((FrameLayout.LayoutParams) layoutParams2).gravity = PrivacyDotViewControllerKt.innerGravity(rotatedCorner);
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                }
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
            }
        }
    }

    public final void setCornerSizes(ViewState viewState) {
        int i;
        int i2;
        boolean layoutRtl = viewState.getLayoutRtl();
        Point point = new Point();
        View view = this.tl;
        View view2 = null;
        if (view == null) {
            view = null;
        }
        view.getContext().getDisplay().getRealSize(point);
        View view3 = this.tl;
        if (view3 == null) {
            view3 = null;
        }
        int exactRotation = RotationUtils.getExactRotation(view3.getContext());
        if (exactRotation == 1 || exactRotation == 3) {
            i = point.y;
            i2 = point.x;
        } else {
            i = point.x;
            i2 = point.y;
        }
        View view4 = this.tl;
        if (view4 == null) {
            view4 = null;
        }
        Rect contentRectForRotation = viewState.contentRectForRotation(activeRotationForCorner(view4, layoutRtl));
        View view5 = this.tl;
        if (view5 == null) {
            view5 = null;
        }
        view5.setPadding(0, viewState.getPaddingTop(), 0, 0);
        View view6 = this.tl;
        if (view6 == null) {
            view6 = null;
        }
        ViewGroup.LayoutParams layoutParams = view6.getLayoutParams();
        if (layoutParams != null) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
            layoutParams2.height = contentRectForRotation.height();
            if (layoutRtl) {
                layoutParams2.width = contentRectForRotation.left;
            } else {
                layoutParams2.width = i2 - contentRectForRotation.right;
            }
            View view7 = this.tr;
            if (view7 == null) {
                view7 = null;
            }
            Rect contentRectForRotation2 = viewState.contentRectForRotation(activeRotationForCorner(view7, layoutRtl));
            View view8 = this.tr;
            if (view8 == null) {
                view8 = null;
            }
            view8.setPadding(0, viewState.getPaddingTop(), 0, 0);
            View view9 = this.tr;
            if (view9 == null) {
                view9 = null;
            }
            ViewGroup.LayoutParams layoutParams3 = view9.getLayoutParams();
            if (layoutParams3 != null) {
                FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) layoutParams3;
                layoutParams4.height = contentRectForRotation2.height();
                if (layoutRtl) {
                    layoutParams4.width = contentRectForRotation2.left;
                } else {
                    layoutParams4.width = i - contentRectForRotation2.right;
                }
                View view10 = this.br;
                if (view10 == null) {
                    view10 = null;
                }
                Rect contentRectForRotation3 = viewState.contentRectForRotation(activeRotationForCorner(view10, layoutRtl));
                View view11 = this.br;
                if (view11 == null) {
                    view11 = null;
                }
                view11.setPadding(0, viewState.getPaddingTop(), 0, 0);
                View view12 = this.br;
                if (view12 == null) {
                    view12 = null;
                }
                ViewGroup.LayoutParams layoutParams5 = view12.getLayoutParams();
                if (layoutParams5 != null) {
                    FrameLayout.LayoutParams layoutParams6 = (FrameLayout.LayoutParams) layoutParams5;
                    layoutParams6.height = contentRectForRotation3.height();
                    if (layoutRtl) {
                        layoutParams6.width = contentRectForRotation3.left;
                    } else {
                        layoutParams6.width = i2 - contentRectForRotation3.right;
                    }
                    View view13 = this.bl;
                    if (view13 == null) {
                        view13 = null;
                    }
                    Rect contentRectForRotation4 = viewState.contentRectForRotation(activeRotationForCorner(view13, layoutRtl));
                    View view14 = this.bl;
                    if (view14 == null) {
                        view14 = null;
                    }
                    view14.setPadding(0, viewState.getPaddingTop(), 0, 0);
                    View view15 = this.bl;
                    if (view15 != null) {
                        view2 = view15;
                    }
                    ViewGroup.LayoutParams layoutParams7 = view2.getLayoutParams();
                    if (layoutParams7 != null) {
                        FrameLayout.LayoutParams layoutParams8 = (FrameLayout.LayoutParams) layoutParams7;
                        layoutParams8.height = contentRectForRotation4.height();
                        if (layoutRtl) {
                            layoutParams8.width = contentRectForRotation4.left;
                        } else {
                            layoutParams8.width = i - contentRectForRotation4.right;
                        }
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                    }
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                }
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
            }
        } else {
            throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
        }
    }

    public final View selectDesignatedCorner(int i, boolean z) {
        View view = this.tl;
        if (view == null) {
            return null;
        }
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        throw new IllegalStateException("unknown rotation");
                    } else if (z) {
                        View view2 = this.bl;
                        if (view2 != null) {
                            return view2;
                        }
                    } else if (view != null) {
                        return view;
                    }
                } else if (z) {
                    View view3 = this.br;
                    if (view3 != null) {
                        return view3;
                    }
                } else {
                    View view4 = this.bl;
                    if (view4 != null) {
                        return view4;
                    }
                }
            } else if (z) {
                View view5 = this.tr;
                if (view5 != null) {
                    return view5;
                }
            } else {
                View view6 = this.br;
                if (view6 != null) {
                    return view6;
                }
            }
        } else if (!z) {
            View view7 = this.tr;
            if (view7 != null) {
                return view7;
            }
        } else if (view != null) {
            return view;
        }
        return null;
    }

    public final void updateDesignatedCorner(View view, boolean z) {
        if (z) {
            ShowingListener showingListener2 = this.showingListener;
            if (showingListener2 != null) {
                showingListener2.onPrivacyDotShown(view);
            }
            if (view != null) {
                view.clearAnimation();
                view.setVisibility(0);
                view.setAlpha(0.0f);
                view.animate().alpha(1.0f).setDuration(300).start();
            }
        }
    }

    public final void setCornerVisibilities(int i) {
        for (View next : getViews()) {
            next.setVisibility(i);
            if (i == 0) {
                ShowingListener showingListener2 = this.showingListener;
                if (showingListener2 != null) {
                    showingListener2.onPrivacyDotShown(next);
                }
            } else {
                ShowingListener showingListener3 = this.showingListener;
                if (showingListener3 != null) {
                    showingListener3.onPrivacyDotHidden(next);
                }
            }
        }
    }

    public final int cornerForView(View view) {
        View view2 = this.tl;
        View view3 = null;
        if (view2 == null) {
            view2 = null;
        }
        if (Intrinsics.areEqual((Object) view, (Object) view2)) {
            return 0;
        }
        View view4 = this.tr;
        if (view4 == null) {
            view4 = null;
        }
        if (Intrinsics.areEqual((Object) view, (Object) view4)) {
            return 1;
        }
        View view5 = this.bl;
        if (view5 == null) {
            view5 = null;
        }
        if (Intrinsics.areEqual((Object) view, (Object) view5)) {
            return 3;
        }
        View view6 = this.br;
        if (view6 != null) {
            view3 = view6;
        }
        if (Intrinsics.areEqual((Object) view, (Object) view3)) {
            return 2;
        }
        throw new IllegalArgumentException("not a corner view");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0021, code lost:
        if (r8 != false) goto L_0x0014;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0010, code lost:
        if (r8 != false) goto L_0x0012;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int activeRotationForCorner(android.view.View r7, boolean r8) {
        /*
            r6 = this;
            android.view.View r0 = r6.tr
            r1 = 0
            if (r0 != 0) goto L_0x0006
            r0 = r1
        L_0x0006:
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r7, (java.lang.Object) r0)
            r2 = 2
            r3 = 3
            r4 = 1
            r5 = 0
            if (r0 == 0) goto L_0x0016
            if (r8 == 0) goto L_0x0014
        L_0x0012:
            r2 = r4
            goto L_0x0038
        L_0x0014:
            r2 = r5
            goto L_0x0038
        L_0x0016:
            android.view.View r0 = r6.tl
            if (r0 != 0) goto L_0x001b
            r0 = r1
        L_0x001b:
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r7, (java.lang.Object) r0)
            if (r0 == 0) goto L_0x0026
            if (r8 == 0) goto L_0x0024
            goto L_0x0014
        L_0x0024:
            r2 = r3
            goto L_0x0038
        L_0x0026:
            android.view.View r6 = r6.br
            if (r6 != 0) goto L_0x002b
            goto L_0x002c
        L_0x002b:
            r1 = r6
        L_0x002c:
            boolean r6 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r7, (java.lang.Object) r1)
            if (r6 == 0) goto L_0x0035
            if (r8 == 0) goto L_0x0012
            goto L_0x0038
        L_0x0035:
            if (r8 == 0) goto L_0x0038
            goto L_0x0024
        L_0x0038:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.events.PrivacyDotViewController.activeRotationForCorner(android.view.View, boolean):int");
    }

    public final void initialize(@NotNull View view, @NotNull View view2, @NotNull View view3, @NotNull View view4) {
        View view5 = view;
        View view6 = view2;
        View view7 = view3;
        View view8 = view4;
        View view9 = this.tl;
        if (!(view9 == null || this.tr == null || this.bl == null || this.br == null)) {
            if (view9 == null) {
                view9 = null;
            }
            if (Intrinsics.areEqual((Object) view9, (Object) view5)) {
                View view10 = this.tr;
                if (view10 == null) {
                    view10 = null;
                }
                if (Intrinsics.areEqual((Object) view10, (Object) view6)) {
                    View view11 = this.bl;
                    if (view11 == null) {
                        view11 = null;
                    }
                    if (Intrinsics.areEqual((Object) view11, (Object) view7)) {
                        View view12 = this.br;
                        if (view12 == null) {
                            view12 = null;
                        }
                        if (Intrinsics.areEqual((Object) view12, (Object) view8)) {
                            return;
                        }
                    }
                }
            }
        }
        this.tl = view5;
        this.tr = view6;
        this.bl = view7;
        this.br = view8;
        boolean isLayoutRtl = this.configurationController.isLayoutRtl();
        View selectDesignatedCorner = selectDesignatedCorner(0, isLayoutRtl);
        int cornerIndex = cornerIndex(selectDesignatedCorner);
        this.mainExecutor.execute(new PrivacyDotViewController$initialize$5(this));
        Rect statusBarContentAreaForRotation = this.contentInsetsProvider.getStatusBarContentAreaForRotation(3);
        Rect statusBarContentAreaForRotation2 = this.contentInsetsProvider.getStatusBarContentAreaForRotation(0);
        Rect statusBarContentAreaForRotation3 = this.contentInsetsProvider.getStatusBarContentAreaForRotation(1);
        Rect statusBarContentAreaForRotation4 = this.contentInsetsProvider.getStatusBarContentAreaForRotation(2);
        int statusBarPaddingTop$default = StatusBarContentInsetsProvider.getStatusBarPaddingTop$default(this.contentInsetsProvider, (Integer) null, 1, (Object) null);
        synchronized (this.lock) {
            setNextViewState(ViewState.copy$default(this.nextViewState, true, false, false, false, statusBarContentAreaForRotation2, statusBarContentAreaForRotation3, statusBarContentAreaForRotation4, statusBarContentAreaForRotation, isLayoutRtl, 0, statusBarPaddingTop$default, cornerIndex, selectDesignatedCorner, 526, (Object) null));
            Unit unit = Unit.INSTANCE;
        }
    }

    public final void updateStatusBarState() {
        synchronized (this.lock) {
            setNextViewState(ViewState.copy$default(this.nextViewState, false, false, isShadeInQs(), false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8187, (Object) null));
            Unit unit = Unit.INSTANCE;
        }
    }

    @GuardedBy({"lock"})
    public final boolean isShadeInQs() {
        return (this.stateController.isExpanded() && this.stateController.getState() == 0) || this.stateController.getState() == 2;
    }

    public final void scheduleUpdate() {
        PrivacyDotViewControllerKt.dlog("scheduleUpdate: ");
        Runnable runnable = this.cancelRunnable;
        if (runnable != null) {
            runnable.run();
        }
        DelayableExecutor delayableExecutor = this.uiExecutor;
        this.cancelRunnable = delayableExecutor == null ? null : delayableExecutor.executeDelayed(new PrivacyDotViewController$scheduleUpdate$1(this), 100);
    }

    public final void processNextViewState() {
        ViewState copy$default;
        PrivacyDotViewControllerKt.dlog("processNextViewState: ");
        synchronized (this.lock) {
            copy$default = ViewState.copy$default(this.nextViewState, false, false, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8191, (Object) null);
            Unit unit = Unit.INSTANCE;
        }
        resolveState(copy$default);
    }

    public final void resolveState(ViewState viewState) {
        PrivacyDotViewControllerKt.dlog(Intrinsics.stringPlus("resolveState ", viewState));
        if (!viewState.getViewInitialized()) {
            PrivacyDotViewControllerKt.dlog("resolveState: view is not initialized. skipping");
        } else if (Intrinsics.areEqual((Object) viewState, (Object) this.currentViewState)) {
            PrivacyDotViewControllerKt.dlog("resolveState: skipping");
        } else {
            if (viewState.getRotation() != this.currentViewState.getRotation()) {
                updateRotations(viewState.getRotation(), viewState.getPaddingTop());
            }
            if (viewState.needsLayout(this.currentViewState)) {
                setCornerSizes(viewState);
                for (View requestLayout : getViews()) {
                    requestLayout.requestLayout();
                }
            }
            if (!Intrinsics.areEqual((Object) viewState.getDesignatedCorner(), (Object) this.currentViewState.getDesignatedCorner())) {
                updateDesignatedCorner(viewState.getDesignatedCorner(), viewState.shouldShowDot());
            }
            boolean shouldShowDot = viewState.shouldShowDot();
            if (shouldShowDot != this.currentViewState.shouldShowDot()) {
                if (shouldShowDot && viewState.getDesignatedCorner() != null) {
                    showDotView(viewState.getDesignatedCorner(), true);
                } else if (!shouldShowDot && viewState.getDesignatedCorner() != null) {
                    hideDotView(viewState.getDesignatedCorner(), true);
                }
            }
            this.currentViewState = viewState;
        }
    }

    public final int cornerIndex(View view) {
        if (view != null) {
            return cornerForView(view);
        }
        return -1;
    }

    public final List<Rect> getLayoutRects() {
        return CollectionsKt__CollectionsKt.listOf(this.contentInsetsProvider.getStatusBarContentAreaForRotation(3), this.contentInsetsProvider.getStatusBarContentAreaForRotation(0), this.contentInsetsProvider.getStatusBarContentAreaForRotation(1), this.contentInsetsProvider.getStatusBarContentAreaForRotation(2));
    }

    public final void setNewLayoutRects() {
        List<Rect> layoutRects = getLayoutRects();
        synchronized (this.lock) {
            setNextViewState(ViewState.copy$default(this.nextViewState, false, false, false, false, layoutRects.get(1), layoutRects.get(2), layoutRects.get(3), layoutRects.get(0), false, 0, 0, 0, (View) null, 7951, (Object) null));
            Unit unit = Unit.INSTANCE;
        }
    }
}
