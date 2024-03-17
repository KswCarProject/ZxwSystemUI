package com.android.systemui.media;

import android.content.Context;
import android.os.Trace;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.systemui.R$xml;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.animation.MeasurementInput;
import com.android.systemui.util.animation.MeasurementOutput;
import com.android.systemui.util.animation.TransitionLayout;
import com.android.systemui.util.animation.TransitionLayoutController;
import com.android.systemui.util.animation.TransitionViewState;
import com.android.systemui.util.animation.WidgetState;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaViewController.kt */
public final class MediaViewController {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final long GUTS_ANIMATION_DURATION = 500;
    public boolean animateNextStateChange;
    public long animationDelay;
    public long animationDuration;
    @NotNull
    public final ConstraintSet collapsedLayout;
    @NotNull
    public final ConfigurationController configurationController;
    @NotNull
    public final MediaViewController$configurationListener$1 configurationListener;
    @NotNull
    public final Context context;
    public int currentEndLocation;
    public int currentHeight;
    public int currentStartLocation;
    public float currentTransitionProgress;
    public int currentWidth;
    @NotNull
    public final ConstraintSet expandedLayout;
    public boolean firstRefresh = true;
    public boolean isGutsVisible;
    @NotNull
    public final TransitionLayoutController layoutController;
    @NotNull
    public final MediaViewLogger logger;
    @NotNull
    public final MeasurementOutput measurement;
    @NotNull
    public final MediaHostStatesManager mediaHostStatesManager;
    public Function0<Unit> sizeChangedListener;
    @NotNull
    public final MediaHostStatesManager.Callback stateCallback;
    @NotNull
    public final CacheKey tmpKey;
    @NotNull
    public final TransitionViewState tmpState;
    @NotNull
    public final TransitionViewState tmpState2;
    @NotNull
    public final TransitionViewState tmpState3;
    @Nullable
    public TransitionLayout transitionLayout;
    @NotNull
    public TYPE type;
    @NotNull
    public final Map<CacheKey, TransitionViewState> viewStates;

    /* compiled from: MediaViewController.kt */
    public enum TYPE {
        PLAYER,
        RECOMMENDATION
    }

    /* compiled from: MediaViewController.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[TYPE.values().length];
            iArr[TYPE.PLAYER.ordinal()] = 1;
            iArr[TYPE.RECOMMENDATION.ordinal()] = 2;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public final void attach(@NotNull TransitionLayout transitionLayout2, @NotNull TYPE type2) {
        Trace.beginSection("MediaViewController#attach");
        try {
            updateMediaViewControllerType(type2);
            this.logger.logMediaLocation("attach", this.currentStartLocation, getCurrentEndLocation());
            this.transitionLayout = transitionLayout2;
            this.layoutController.attach(transitionLayout2);
            if (getCurrentEndLocation() != -1) {
                setCurrentState(this.currentStartLocation, getCurrentEndLocation(), this.currentTransitionProgress, true);
                Unit unit = Unit.INSTANCE;
                Trace.endSection();
            }
        } finally {
            Trace.endSection();
        }
    }

    @Nullable
    public final MeasurementOutput getMeasurementsForState(@NotNull MediaHostState mediaHostState) {
        Trace.beginSection("MediaViewController#getMeasurementsForState");
        try {
            TransitionViewState obtainViewState = obtainViewState(mediaHostState);
            if (obtainViewState == null) {
                return null;
            }
            this.measurement.setMeasuredWidth(obtainViewState.getWidth());
            this.measurement.setMeasuredHeight(obtainViewState.getHeight());
            MeasurementOutput measurementOutput = this.measurement;
            Trace.endSection();
            return measurementOutput;
        } finally {
            Trace.endSection();
        }
    }

    public final void refreshState() {
        Trace.beginSection("MediaViewController#refreshState");
        try {
            this.viewStates.clear();
            if (this.firstRefresh) {
                ensureAllMeasurements();
                this.firstRefresh = false;
            }
            setCurrentState(this.currentStartLocation, getCurrentEndLocation(), this.currentTransitionProgress, true);
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    public final void setCurrentState(int i, int i2, float f, boolean z) {
        TransitionViewState transitionViewState;
        int i3 = i;
        int i4 = i2;
        float f2 = f;
        Trace.beginSection("MediaViewController#setCurrentState");
        try {
            setCurrentEndLocation(i4);
            this.currentStartLocation = i3;
            this.currentTransitionProgress = f2;
            this.logger.logMediaLocation("setCurrentState", i3, i4);
            boolean z2 = true;
            boolean z3 = this.animateNextStateChange && !z;
            MediaHostState mediaHostState = this.mediaHostStatesManager.getMediaHostStates().get(Integer.valueOf(i2));
            if (mediaHostState != null) {
                MediaHostState mediaHostState2 = this.mediaHostStatesManager.getMediaHostStates().get(Integer.valueOf(i));
                TransitionViewState obtainViewState = obtainViewState(mediaHostState);
                if (obtainViewState == null) {
                    Trace.endSection();
                    return;
                }
                TransitionViewState updateViewStateToCarouselSize = updateViewStateToCarouselSize(obtainViewState, i4, this.tmpState2);
                Intrinsics.checkNotNull(updateViewStateToCarouselSize);
                this.layoutController.setMeasureState(updateViewStateToCarouselSize);
                this.animateNextStateChange = false;
                if (this.transitionLayout == null) {
                    Trace.endSection();
                    return;
                }
                TransitionViewState updateViewStateToCarouselSize2 = updateViewStateToCarouselSize(obtainViewState(mediaHostState2), i3, this.tmpState3);
                if (!mediaHostState.getVisible()) {
                    if (!(updateViewStateToCarouselSize2 == null || mediaHostState2 == null)) {
                        if (mediaHostState2.getVisible()) {
                            updateViewStateToCarouselSize2 = this.layoutController.getGoneState(updateViewStateToCarouselSize2, mediaHostState2.getDisappearParameters(), f2, this.tmpState);
                        }
                    }
                    transitionViewState = updateViewStateToCarouselSize;
                    this.logger.logMediaSize("setCurrentState", transitionViewState.getWidth(), transitionViewState.getHeight());
                    this.layoutController.setState(transitionViewState, z, z3, this.animationDuration, this.animationDelay);
                    Unit unit = Unit.INSTANCE;
                    Trace.endSection();
                } else if (mediaHostState2 == null || mediaHostState2.getVisible()) {
                    if (!(f2 == 1.0f)) {
                        if (updateViewStateToCarouselSize2 != null) {
                            if (f2 != 0.0f) {
                                z2 = false;
                            }
                            if (!z2) {
                                updateViewStateToCarouselSize2 = this.layoutController.getInterpolatedState(updateViewStateToCarouselSize2, updateViewStateToCarouselSize, f2, this.tmpState);
                            }
                        }
                    }
                    transitionViewState = updateViewStateToCarouselSize;
                    this.logger.logMediaSize("setCurrentState", transitionViewState.getWidth(), transitionViewState.getHeight());
                    this.layoutController.setState(transitionViewState, z, z3, this.animationDuration, this.animationDelay);
                    Unit unit2 = Unit.INSTANCE;
                    Trace.endSection();
                } else {
                    updateViewStateToCarouselSize2 = this.layoutController.getGoneState(updateViewStateToCarouselSize, mediaHostState.getDisappearParameters(), 1.0f - f2, this.tmpState);
                }
                transitionViewState = updateViewStateToCarouselSize2;
                this.logger.logMediaSize("setCurrentState", transitionViewState.getWidth(), transitionViewState.getHeight());
                this.layoutController.setState(transitionViewState, z, z3, this.animationDuration, this.animationDelay);
                Unit unit22 = Unit.INSTANCE;
                Trace.endSection();
            }
        } finally {
            Trace.endSection();
        }
    }

    public MediaViewController(@NotNull Context context2, @NotNull ConfigurationController configurationController2, @NotNull MediaHostStatesManager mediaHostStatesManager2, @NotNull MediaViewLogger mediaViewLogger) {
        this.context = context2;
        this.configurationController = configurationController2;
        this.mediaHostStatesManager = mediaHostStatesManager2;
        this.logger = mediaViewLogger;
        TransitionLayoutController transitionLayoutController = new TransitionLayoutController();
        this.layoutController = transitionLayoutController;
        this.measurement = new MeasurementOutput(0, 0);
        this.type = TYPE.PLAYER;
        this.viewStates = new LinkedHashMap();
        this.currentEndLocation = -1;
        this.currentStartLocation = -1;
        this.currentTransitionProgress = 1.0f;
        this.tmpState = new TransitionViewState();
        this.tmpState2 = new TransitionViewState();
        this.tmpState3 = new TransitionViewState();
        this.tmpKey = new CacheKey(0, 0, 0.0f, false, 15, (DefaultConstructorMarker) null);
        MediaViewController$configurationListener$1 mediaViewController$configurationListener$1 = new MediaViewController$configurationListener$1(this);
        this.configurationListener = mediaViewController$configurationListener$1;
        this.stateCallback = new MediaViewController$stateCallback$1(this);
        this.collapsedLayout = new ConstraintSet();
        this.expandedLayout = new ConstraintSet();
        mediaHostStatesManager2.addController(this);
        transitionLayoutController.setSizeChangedListener(new Function2<Integer, Integer, Unit>(this) {
            public final /* synthetic */ MediaViewController this$0;

            {
                this.this$0 = r1;
            }

            public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
                invoke(((Number) obj).intValue(), ((Number) obj2).intValue());
                return Unit.INSTANCE;
            }

            public final void invoke(int i, int i2) {
                this.this$0.setCurrentWidth(i);
                this.this$0.setCurrentHeight(i2);
                this.this$0.getSizeChangedListener().invoke();
            }
        });
        configurationController2.addCallback(mediaViewController$configurationListener$1);
    }

    /* compiled from: MediaViewController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    @NotNull
    public final Function0<Unit> getSizeChangedListener() {
        Function0<Unit> function0 = this.sizeChangedListener;
        if (function0 != null) {
            return function0;
        }
        return null;
    }

    public final void setSizeChangedListener(@NotNull Function0<Unit> function0) {
        this.sizeChangedListener = function0;
    }

    public final int getCurrentEndLocation() {
        return this.currentEndLocation;
    }

    public final void setCurrentEndLocation(int i) {
        this.currentEndLocation = i;
    }

    public final int getCurrentWidth() {
        return this.currentWidth;
    }

    public final void setCurrentWidth(int i) {
        this.currentWidth = i;
    }

    public final int getCurrentHeight() {
        return this.currentHeight;
    }

    public final void setCurrentHeight(int i) {
        this.currentHeight = i;
    }

    public final float getTranslationX() {
        TransitionLayout transitionLayout2 = this.transitionLayout;
        if (transitionLayout2 == null) {
            return 0.0f;
        }
        return transitionLayout2.getTranslationX();
    }

    public final float getTranslationY() {
        TransitionLayout transitionLayout2 = this.transitionLayout;
        if (transitionLayout2 == null) {
            return 0.0f;
        }
        return transitionLayout2.getTranslationY();
    }

    @NotNull
    public final MediaHostStatesManager.Callback getStateCallback() {
        return this.stateCallback;
    }

    @NotNull
    public final ConstraintSet getCollapsedLayout() {
        return this.collapsedLayout;
    }

    @NotNull
    public final ConstraintSet getExpandedLayout() {
        return this.expandedLayout;
    }

    public final boolean isGutsVisible() {
        return this.isGutsVisible;
    }

    public final void onDestroy() {
        this.mediaHostStatesManager.removeController(this);
        this.configurationController.removeCallback(this.configurationListener);
    }

    public final void openGuts() {
        if (!this.isGutsVisible) {
            this.isGutsVisible = true;
            animatePendingStateChange(GUTS_ANIMATION_DURATION, 0);
            setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, false);
        }
    }

    public final void closeGuts(boolean z) {
        if (this.isGutsVisible) {
            this.isGutsVisible = false;
            if (!z) {
                animatePendingStateChange(GUTS_ANIMATION_DURATION, 0);
            }
            setCurrentState(this.currentStartLocation, this.currentEndLocation, this.currentTransitionProgress, z);
        }
    }

    public final void ensureAllMeasurements() {
        for (Map.Entry<Integer, MediaHostState> value : this.mediaHostStatesManager.getMediaHostStates().entrySet()) {
            obtainViewState((MediaHostState) value.getValue());
        }
    }

    public final ConstraintSet constraintSetForExpansion(float f) {
        return f > 0.0f ? this.expandedLayout : this.collapsedLayout;
    }

    public final void setGutsViewState(TransitionViewState transitionViewState) {
        Set<Integer> set;
        int i = WhenMappings.$EnumSwitchMapping$0[this.type.ordinal()];
        if (i == 1) {
            set = MediaViewHolder.Companion.getControlsIds();
        } else if (i == 2) {
            set = RecommendationViewHolder.Companion.getControlsIds();
        } else {
            throw new NoWhenBranchMatchedException();
        }
        Set<Integer> ids = GutsViewHolder.Companion.getIds();
        Iterator it = set.iterator();
        while (true) {
            float f = 0.0f;
            if (!it.hasNext()) {
                break;
            }
            WidgetState widgetState = transitionViewState.getWidgetStates().get(Integer.valueOf(((Number) it.next()).intValue()));
            if (widgetState != null) {
                if (!isGutsVisible()) {
                    f = widgetState.getAlpha();
                }
                widgetState.setAlpha(f);
                widgetState.setGone(isGutsVisible() ? true : widgetState.getGone());
            }
        }
        for (Number intValue : ids) {
            WidgetState widgetState2 = transitionViewState.getWidgetStates().get(Integer.valueOf(intValue.intValue()));
            if (widgetState2 != null) {
                widgetState2.setAlpha(isGutsVisible() ? widgetState2.getAlpha() : 0.0f);
                widgetState2.setGone(isGutsVisible() ? widgetState2.getGone() : true);
            }
        }
    }

    public final TransitionViewState obtainViewState(MediaHostState mediaHostState) {
        if (mediaHostState == null || mediaHostState.getMeasurementInput() == null) {
            return null;
        }
        CacheKey key = getKey(mediaHostState, this.isGutsVisible, this.tmpKey);
        TransitionViewState transitionViewState = this.viewStates.get(key);
        if (transitionViewState != null) {
            return transitionViewState;
        }
        CacheKey copy$default = CacheKey.copy$default(key, 0, 0, 0.0f, false, 15, (Object) null);
        if (this.transitionLayout == null) {
            return null;
        }
        boolean z = true;
        if (!(mediaHostState.getExpansion() == 0.0f)) {
            if (mediaHostState.getExpansion() != 1.0f) {
                z = false;
            }
            if (!z) {
                MediaHostState copy = mediaHostState.copy();
                copy.setExpansion(0.0f);
                TransitionViewState obtainViewState = obtainViewState(copy);
                if (obtainViewState != null) {
                    MediaHostState copy2 = mediaHostState.copy();
                    copy2.setExpansion(1.0f);
                    TransitionViewState obtainViewState2 = obtainViewState(copy2);
                    if (obtainViewState2 != null) {
                        return TransitionLayoutController.getInterpolatedState$default(this.layoutController, obtainViewState, obtainViewState2, mediaHostState.getExpansion(), (TransitionViewState) null, 8, (Object) null);
                    }
                    throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.util.animation.TransitionViewState");
                }
                throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.util.animation.TransitionViewState");
            }
        }
        TransitionLayout transitionLayout2 = this.transitionLayout;
        Intrinsics.checkNotNull(transitionLayout2);
        MeasurementInput measurementInput = mediaHostState.getMeasurementInput();
        Intrinsics.checkNotNull(measurementInput);
        TransitionViewState calculateViewState = transitionLayout2.calculateViewState(measurementInput, constraintSetForExpansion(mediaHostState.getExpansion()), new TransitionViewState());
        setGutsViewState(calculateViewState);
        this.viewStates.put(copy$default, calculateViewState);
        return calculateViewState;
    }

    public final CacheKey getKey(MediaHostState mediaHostState, boolean z, CacheKey cacheKey) {
        MeasurementInput measurementInput = mediaHostState.getMeasurementInput();
        int i = 0;
        cacheKey.setHeightMeasureSpec(measurementInput == null ? 0 : measurementInput.getHeightMeasureSpec());
        MeasurementInput measurementInput2 = mediaHostState.getMeasurementInput();
        if (measurementInput2 != null) {
            i = measurementInput2.getWidthMeasureSpec();
        }
        cacheKey.setWidthMeasureSpec(i);
        cacheKey.setExpansion(mediaHostState.getExpansion());
        cacheKey.setGutsVisible(z);
        return cacheKey;
    }

    public final TransitionViewState updateViewStateToCarouselSize(TransitionViewState transitionViewState, int i, TransitionViewState transitionViewState2) {
        TransitionViewState copy = transitionViewState == null ? null : transitionViewState.copy(transitionViewState2);
        if (copy == null) {
            return null;
        }
        MeasurementOutput measurementOutput = this.mediaHostStatesManager.getCarouselSizes().get(Integer.valueOf(i));
        if (measurementOutput != null) {
            copy.setHeight(Math.max(measurementOutput.getMeasuredHeight(), copy.getHeight()));
            copy.setWidth(Math.max(measurementOutput.getMeasuredWidth(), copy.getWidth()));
        }
        this.logger.logMediaSize("update to carousel", copy.getWidth(), copy.getHeight());
        return copy;
    }

    public final void updateMediaViewControllerType(TYPE type2) {
        this.type = type2;
        int i = WhenMappings.$EnumSwitchMapping$0[type2.ordinal()];
        if (i == 1) {
            this.collapsedLayout.load(this.context, R$xml.media_session_collapsed);
            this.expandedLayout.load(this.context, R$xml.media_session_expanded);
        } else if (i == 2) {
            this.collapsedLayout.load(this.context, R$xml.media_recommendation_collapsed);
            this.expandedLayout.load(this.context, R$xml.media_recommendation_expanded);
        }
        refreshState();
    }

    public final TransitionViewState obtainViewStateForLocation(int i) {
        MediaHostState mediaHostState = this.mediaHostStatesManager.getMediaHostStates().get(Integer.valueOf(i));
        if (mediaHostState == null) {
            return null;
        }
        return obtainViewState(mediaHostState);
    }

    public final void onLocationPreChange(int i) {
        TransitionViewState obtainViewStateForLocation = obtainViewStateForLocation(i);
        if (obtainViewStateForLocation != null) {
            this.layoutController.setMeasureState(obtainViewStateForLocation);
        }
    }

    public final void animatePendingStateChange(long j, long j2) {
        this.animateNextStateChange = true;
        this.animationDuration = j;
        this.animationDelay = j2;
    }
}
