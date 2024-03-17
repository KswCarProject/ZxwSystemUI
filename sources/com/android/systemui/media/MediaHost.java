package com.android.systemui.media;

import android.graphics.Rect;
import android.util.ArraySet;
import com.android.systemui.util.animation.DisappearParameters;
import com.android.systemui.util.animation.MeasurementInput;
import com.android.systemui.util.animation.UniqueObjectHostView;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaHost.kt */
public final class MediaHost implements MediaHostState {
    @NotNull
    public final Rect currentBounds = new Rect();
    @NotNull
    public final Rect currentClipping = new Rect();
    public UniqueObjectHostView hostView;
    public boolean inited;
    @NotNull
    public final MediaHost$listener$1 listener = new MediaHost$listener$1(this);
    public boolean listeningToMediaData;
    public int location = -1;
    @NotNull
    public final MediaDataManager mediaDataManager;
    @NotNull
    public final MediaHierarchyManager mediaHierarchyManager;
    @NotNull
    public final MediaHostStatesManager mediaHostStatesManager;
    @NotNull
    public final MediaHostStateHolder state;
    @NotNull
    public final int[] tmpLocationOnScreen = {0, 0};
    @NotNull
    public ArraySet<Function1<Boolean, Unit>> visibleChangedListeners = new ArraySet<>();

    @NotNull
    public MediaHostState copy() {
        return this.state.copy();
    }

    @NotNull
    public DisappearParameters getDisappearParameters() {
        return this.state.getDisappearParameters();
    }

    public float getExpansion() {
        return this.state.getExpansion();
    }

    public boolean getFalsingProtectionNeeded() {
        return this.state.getFalsingProtectionNeeded();
    }

    @Nullable
    public MeasurementInput getMeasurementInput() {
        return this.state.getMeasurementInput();
    }

    public boolean getShowsOnlyActiveMedia() {
        return this.state.getShowsOnlyActiveMedia();
    }

    public boolean getVisible() {
        return this.state.getVisible();
    }

    public void setDisappearParameters(@NotNull DisappearParameters disappearParameters) {
        this.state.setDisappearParameters(disappearParameters);
    }

    public void setExpansion(float f) {
        this.state.setExpansion(f);
    }

    public void setFalsingProtectionNeeded(boolean z) {
        this.state.setFalsingProtectionNeeded(z);
    }

    public void setShowsOnlyActiveMedia(boolean z) {
        this.state.setShowsOnlyActiveMedia(z);
    }

    public MediaHost(@NotNull MediaHostStateHolder mediaHostStateHolder, @NotNull MediaHierarchyManager mediaHierarchyManager2, @NotNull MediaDataManager mediaDataManager2, @NotNull MediaHostStatesManager mediaHostStatesManager2) {
        this.state = mediaHostStateHolder;
        this.mediaHierarchyManager = mediaHierarchyManager2;
        this.mediaDataManager = mediaDataManager2;
        this.mediaHostStatesManager = mediaHostStatesManager2;
    }

    @NotNull
    public final UniqueObjectHostView getHostView() {
        UniqueObjectHostView uniqueObjectHostView = this.hostView;
        if (uniqueObjectHostView != null) {
            return uniqueObjectHostView;
        }
        return null;
    }

    public final void setHostView(@NotNull UniqueObjectHostView uniqueObjectHostView) {
        this.hostView = uniqueObjectHostView;
    }

    public final int getLocation() {
        return this.location;
    }

    @NotNull
    public final Rect getCurrentBounds() {
        getHostView().getLocationOnScreen(this.tmpLocationOnScreen);
        int i = 0;
        int paddingLeft = this.tmpLocationOnScreen[0] + getHostView().getPaddingLeft();
        int paddingTop = this.tmpLocationOnScreen[1] + getHostView().getPaddingTop();
        int width = (this.tmpLocationOnScreen[0] + getHostView().getWidth()) - getHostView().getPaddingRight();
        int height = (this.tmpLocationOnScreen[1] + getHostView().getHeight()) - getHostView().getPaddingBottom();
        if (width < paddingLeft) {
            paddingLeft = 0;
            width = 0;
        }
        if (height < paddingTop) {
            height = 0;
        } else {
            i = paddingTop;
        }
        this.currentBounds.set(paddingLeft, i, width, height);
        return this.currentBounds;
    }

    @NotNull
    public final Rect getCurrentClipping() {
        return this.currentClipping;
    }

    public final void addVisibilityChangeListener(@NotNull Function1<? super Boolean, Unit> function1) {
        this.visibleChangedListeners.add(function1);
    }

    public final void removeVisibilityChangeListener(@NotNull Function1<? super Boolean, Unit> function1) {
        this.visibleChangedListeners.remove(function1);
    }

    public final void init(int i) {
        if (!this.inited) {
            this.inited = true;
            this.location = i;
            setHostView(this.mediaHierarchyManager.register(this));
            setListeningToMediaData(true);
            getHostView().addOnAttachStateChangeListener(new MediaHost$init$1(this));
            getHostView().setMeasurementManager(new MediaHost$init$2(this, i));
            this.state.setChangedListener(new MediaHost$init$3(this, i));
            updateViewVisibility();
        }
    }

    public final void setListeningToMediaData(boolean z) {
        if (z != this.listeningToMediaData) {
            this.listeningToMediaData = z;
            if (z) {
                this.mediaDataManager.addListener(this.listener);
            } else {
                this.mediaDataManager.removeListener(this.listener);
            }
        }
    }

    public final void updateViewVisibility() {
        boolean z;
        MediaHostStateHolder mediaHostStateHolder = this.state;
        if (getShowsOnlyActiveMedia()) {
            z = this.mediaDataManager.hasActiveMediaOrRecommendation();
        } else {
            z = this.mediaDataManager.hasAnyMediaOrRecommendation();
        }
        mediaHostStateHolder.setVisible(z);
        int i = getVisible() ? 0 : 8;
        if (i != getHostView().getVisibility()) {
            getHostView().setVisibility(i);
            for (Function1 invoke : this.visibleChangedListeners) {
                invoke.invoke(Boolean.valueOf(getVisible()));
            }
        }
    }

    /* compiled from: MediaHost.kt */
    public static final class MediaHostStateHolder implements MediaHostState {
        @Nullable
        public Function0<Unit> changedListener;
        @NotNull
        public DisappearParameters disappearParameters = new DisappearParameters();
        public float expansion;
        public boolean falsingProtectionNeeded;
        public int lastDisappearHash = getDisappearParameters().hashCode();
        @Nullable
        public MeasurementInput measurementInput;
        public boolean showsOnlyActiveMedia;
        public boolean visible = true;

        @Nullable
        public MeasurementInput getMeasurementInput() {
            return this.measurementInput;
        }

        public void setMeasurementInput(@Nullable MeasurementInput measurementInput2) {
            boolean z = false;
            if (measurementInput2 != null && measurementInput2.equals(this.measurementInput)) {
                z = true;
            }
            if (!z) {
                this.measurementInput = measurementInput2;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        public float getExpansion() {
            return this.expansion;
        }

        public void setExpansion(float f) {
            if (!Float.valueOf(f).equals(Float.valueOf(this.expansion))) {
                this.expansion = f;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        public boolean getShowsOnlyActiveMedia() {
            return this.showsOnlyActiveMedia;
        }

        public void setShowsOnlyActiveMedia(boolean z) {
            if (!Boolean.valueOf(z).equals(Boolean.valueOf(this.showsOnlyActiveMedia))) {
                this.showsOnlyActiveMedia = z;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        public boolean getVisible() {
            return this.visible;
        }

        public void setVisible(boolean z) {
            if (this.visible != z) {
                this.visible = z;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        public boolean getFalsingProtectionNeeded() {
            return this.falsingProtectionNeeded;
        }

        public void setFalsingProtectionNeeded(boolean z) {
            if (this.falsingProtectionNeeded != z) {
                this.falsingProtectionNeeded = z;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        @NotNull
        public DisappearParameters getDisappearParameters() {
            return this.disappearParameters;
        }

        public void setDisappearParameters(@NotNull DisappearParameters disappearParameters2) {
            int hashCode = disappearParameters2.hashCode();
            if (!Integer.valueOf(this.lastDisappearHash).equals(Integer.valueOf(hashCode))) {
                this.disappearParameters = disappearParameters2;
                this.lastDisappearHash = hashCode;
                Function0<Unit> function0 = this.changedListener;
                if (function0 != null) {
                    function0.invoke();
                }
            }
        }

        public final void setChangedListener(@Nullable Function0<Unit> function0) {
            this.changedListener = function0;
        }

        @NotNull
        public MediaHostState copy() {
            MediaHostStateHolder mediaHostStateHolder = new MediaHostStateHolder();
            mediaHostStateHolder.setExpansion(getExpansion());
            mediaHostStateHolder.setShowsOnlyActiveMedia(getShowsOnlyActiveMedia());
            MeasurementInput measurementInput2 = getMeasurementInput();
            MeasurementInput measurementInput3 = null;
            if (measurementInput2 != null) {
                measurementInput3 = MeasurementInput.copy$default(measurementInput2, 0, 0, 3, (Object) null);
            }
            mediaHostStateHolder.setMeasurementInput(measurementInput3);
            mediaHostStateHolder.setVisible(getVisible());
            mediaHostStateHolder.setDisappearParameters(getDisappearParameters().deepCopy());
            mediaHostStateHolder.setFalsingProtectionNeeded(getFalsingProtectionNeeded());
            return mediaHostStateHolder;
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof MediaHostState)) {
                return false;
            }
            MediaHostState mediaHostState = (MediaHostState) obj;
            if (!Objects.equals(getMeasurementInput(), mediaHostState.getMeasurementInput())) {
                return false;
            }
            if ((getExpansion() == mediaHostState.getExpansion()) && getShowsOnlyActiveMedia() == mediaHostState.getShowsOnlyActiveMedia() && getVisible() == mediaHostState.getVisible() && getFalsingProtectionNeeded() == mediaHostState.getFalsingProtectionNeeded() && getDisappearParameters().equals(mediaHostState.getDisappearParameters())) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            MeasurementInput measurementInput2 = getMeasurementInput();
            return ((((((((((measurementInput2 == null ? 0 : measurementInput2.hashCode()) * 31) + Float.hashCode(getExpansion())) * 31) + Boolean.hashCode(getFalsingProtectionNeeded())) * 31) + Boolean.hashCode(getShowsOnlyActiveMedia())) * 31) + (getVisible() ? 1 : 2)) * 31) + getDisappearParameters().hashCode();
        }
    }
}
