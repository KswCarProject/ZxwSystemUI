package com.android.systemui.shared.animation;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UnfoldConstantTranslateAnimator.kt */
public final class UnfoldConstantTranslateAnimator implements UnfoldTransitionProgressProvider.TransitionProgressListener {
    @NotNull
    public final UnfoldTransitionProgressProvider progressProvider;
    public ViewGroup rootView;
    public float translationMax;
    @NotNull
    public final Set<ViewIdToTranslate> viewsIdToTranslate;
    @NotNull
    public List<ViewToTranslate> viewsToTranslate = CollectionsKt__CollectionsKt.emptyList();

    public UnfoldConstantTranslateAnimator(@NotNull Set<ViewIdToTranslate> set, @NotNull UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        this.viewsIdToTranslate = set;
        this.progressProvider = unfoldTransitionProgressProvider;
    }

    public final void init(@NotNull ViewGroup viewGroup, float f) {
        this.rootView = viewGroup;
        this.translationMax = f;
        this.progressProvider.addCallback(this);
    }

    public void onTransitionStarted() {
        ViewGroup viewGroup = this.rootView;
        if (viewGroup == null) {
            viewGroup = null;
        }
        registerViewsForAnimation(viewGroup, this.viewsIdToTranslate);
    }

    public void onTransitionProgress(float f) {
        translateViews(f);
    }

    public void onTransitionFinished() {
        translateViews(1.0f);
    }

    public final void translateViews(float f) {
        View view;
        float f2 = (f - 1.0f) * this.translationMax;
        for (ViewToTranslate viewToTranslate : this.viewsToTranslate) {
            WeakReference<View> component1 = viewToTranslate.component1();
            Direction component2 = viewToTranslate.component2();
            if (viewToTranslate.component3().invoke().booleanValue() && (view = (View) component1.get()) != null) {
                view.setTranslationX(component2.getMultiplier() * f2);
            }
        }
    }

    public final void registerViewsForAnimation(ViewGroup viewGroup, Set<ViewIdToTranslate> set) {
        ViewToTranslate viewToTranslate;
        ArrayList arrayList = new ArrayList();
        for (ViewIdToTranslate viewIdToTranslate : set) {
            int component1 = viewIdToTranslate.component1();
            Direction component2 = viewIdToTranslate.component2();
            Function0<Boolean> component3 = viewIdToTranslate.component3();
            View findViewById = viewGroup.findViewById(component1);
            if (findViewById == null) {
                viewToTranslate = null;
            } else {
                viewToTranslate = new ViewToTranslate(new WeakReference(findViewById), component2, component3);
            }
            if (viewToTranslate != null) {
                arrayList.add(viewToTranslate);
            }
        }
        this.viewsToTranslate = arrayList;
    }

    /* compiled from: UnfoldConstantTranslateAnimator.kt */
    public static final class ViewIdToTranslate {
        @NotNull
        public final Direction direction;
        @NotNull
        public final Function0<Boolean> shouldBeAnimated;
        public final int viewId;

        public final int component1() {
            return this.viewId;
        }

        @NotNull
        public final Direction component2() {
            return this.direction;
        }

        @NotNull
        public final Function0<Boolean> component3() {
            return this.shouldBeAnimated;
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ViewIdToTranslate)) {
                return false;
            }
            ViewIdToTranslate viewIdToTranslate = (ViewIdToTranslate) obj;
            return this.viewId == viewIdToTranslate.viewId && this.direction == viewIdToTranslate.direction && Intrinsics.areEqual((Object) this.shouldBeAnimated, (Object) viewIdToTranslate.shouldBeAnimated);
        }

        public int hashCode() {
            return (((Integer.hashCode(this.viewId) * 31) + this.direction.hashCode()) * 31) + this.shouldBeAnimated.hashCode();
        }

        @NotNull
        public String toString() {
            return "ViewIdToTranslate(viewId=" + this.viewId + ", direction=" + this.direction + ", shouldBeAnimated=" + this.shouldBeAnimated + ')';
        }

        public ViewIdToTranslate(int i, @NotNull Direction direction2, @NotNull Function0<Boolean> function0) {
            this.viewId = i;
            this.direction = direction2;
            this.shouldBeAnimated = function0;
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ ViewIdToTranslate(int i, Direction direction2, Function0 function0, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(i, direction2, (i2 & 4) != 0 ? AnonymousClass1.INSTANCE : function0);
        }
    }

    /* compiled from: UnfoldConstantTranslateAnimator.kt */
    public static final class ViewToTranslate {
        @NotNull
        public final Direction direction;
        @NotNull
        public final Function0<Boolean> shouldBeAnimated;
        @NotNull
        public final WeakReference<View> view;

        @NotNull
        public final WeakReference<View> component1() {
            return this.view;
        }

        @NotNull
        public final Direction component2() {
            return this.direction;
        }

        @NotNull
        public final Function0<Boolean> component3() {
            return this.shouldBeAnimated;
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ViewToTranslate)) {
                return false;
            }
            ViewToTranslate viewToTranslate = (ViewToTranslate) obj;
            return Intrinsics.areEqual((Object) this.view, (Object) viewToTranslate.view) && this.direction == viewToTranslate.direction && Intrinsics.areEqual((Object) this.shouldBeAnimated, (Object) viewToTranslate.shouldBeAnimated);
        }

        public int hashCode() {
            return (((this.view.hashCode() * 31) + this.direction.hashCode()) * 31) + this.shouldBeAnimated.hashCode();
        }

        @NotNull
        public String toString() {
            return "ViewToTranslate(view=" + this.view + ", direction=" + this.direction + ", shouldBeAnimated=" + this.shouldBeAnimated + ')';
        }

        public ViewToTranslate(@NotNull WeakReference<View> weakReference, @NotNull Direction direction2, @NotNull Function0<Boolean> function0) {
            this.view = weakReference;
            this.direction = direction2;
            this.shouldBeAnimated = function0;
        }
    }

    /* compiled from: UnfoldConstantTranslateAnimator.kt */
    public enum Direction {
        LEFT(-1.0f),
        RIGHT(1.0f);
        
        private final float multiplier;

        /* access modifiers changed from: public */
        Direction(float f) {
            this.multiplier = f;
        }

        public final float getMultiplier() {
            return this.multiplier;
        }
    }
}
