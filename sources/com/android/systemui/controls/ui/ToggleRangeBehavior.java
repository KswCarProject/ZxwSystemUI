package com.android.systemui.controls.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.RangeTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import android.service.controls.templates.ToggleRangeTemplate;
import android.util.Log;
import android.util.MathUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import java.util.Arrays;
import java.util.IllegalFormatException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ToggleRangeBehavior.kt */
public final class ToggleRangeBehavior implements Behavior {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public Drawable clipLayer;
    public int colorOffset;
    public Context context;
    public Control control;
    @NotNull
    public String currentRangeValue = "";
    @NotNull
    public CharSequence currentStatusText = "";
    public ControlViewHolder cvh;
    public boolean isChecked;
    public boolean isToggleable;
    @Nullable
    public ValueAnimator rangeAnimator;
    public RangeTemplate rangeTemplate;
    public String templateId;

    @NotNull
    public final Drawable getClipLayer() {
        Drawable drawable = this.clipLayer;
        if (drawable != null) {
            return drawable;
        }
        return null;
    }

    public final void setClipLayer(@NotNull Drawable drawable) {
        this.clipLayer = drawable;
    }

    @NotNull
    public final String getTemplateId() {
        String str = this.templateId;
        if (str != null) {
            return str;
        }
        return null;
    }

    public final void setTemplateId(@NotNull String str) {
        this.templateId = str;
    }

    @NotNull
    public final Control getControl() {
        Control control2 = this.control;
        if (control2 != null) {
            return control2;
        }
        return null;
    }

    public final void setControl(@NotNull Control control2) {
        this.control = control2;
    }

    @NotNull
    public final ControlViewHolder getCvh() {
        ControlViewHolder controlViewHolder = this.cvh;
        if (controlViewHolder != null) {
            return controlViewHolder;
        }
        return null;
    }

    public final void setCvh(@NotNull ControlViewHolder controlViewHolder) {
        this.cvh = controlViewHolder;
    }

    @NotNull
    public final RangeTemplate getRangeTemplate() {
        RangeTemplate rangeTemplate2 = this.rangeTemplate;
        if (rangeTemplate2 != null) {
            return rangeTemplate2;
        }
        return null;
    }

    public final void setRangeTemplate(@NotNull RangeTemplate rangeTemplate2) {
        this.rangeTemplate = rangeTemplate2;
    }

    @NotNull
    public final Context getContext() {
        Context context2 = this.context;
        if (context2 != null) {
            return context2;
        }
        return null;
    }

    public final void setContext(@NotNull Context context2) {
        this.context = context2;
    }

    public final boolean isChecked() {
        return this.isChecked;
    }

    public final boolean isToggleable() {
        return this.isToggleable;
    }

    /* compiled from: ToggleRangeBehavior.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    public void initialize(@NotNull ControlViewHolder controlViewHolder) {
        setCvh(controlViewHolder);
        setContext(controlViewHolder.getContext());
        ToggleRangeGestureListener toggleRangeGestureListener = new ToggleRangeGestureListener(controlViewHolder.getLayout());
        controlViewHolder.getLayout().setOnTouchListener(new ToggleRangeBehavior$initialize$1(new GestureDetector(getContext(), toggleRangeGestureListener), toggleRangeGestureListener, this));
    }

    public final void setup(ToggleRangeTemplate toggleRangeTemplate) {
        setRangeTemplate(toggleRangeTemplate.getRange());
        this.isToggleable = true;
        this.isChecked = toggleRangeTemplate.isChecked();
    }

    public final void setup(RangeTemplate rangeTemplate2) {
        setRangeTemplate(rangeTemplate2);
        this.isChecked = !(getRangeTemplate().getCurrentValue() == getRangeTemplate().getMinValue());
    }

    public final boolean setupTemplate(ControlTemplate controlTemplate) {
        if (controlTemplate instanceof ToggleRangeTemplate) {
            setup((ToggleRangeTemplate) controlTemplate);
            return true;
        } else if (controlTemplate instanceof RangeTemplate) {
            setup((RangeTemplate) controlTemplate);
            return true;
        } else if (controlTemplate instanceof TemperatureControlTemplate) {
            return setupTemplate(((TemperatureControlTemplate) controlTemplate).getTemplate());
        } else {
            Log.e("ControlsUiController", Intrinsics.stringPlus("Unsupported template type: ", controlTemplate));
            return false;
        }
    }

    public void bind(@NotNull ControlWithState controlWithState, int i) {
        Control control2 = controlWithState.getControl();
        Intrinsics.checkNotNull(control2);
        setControl(control2);
        this.colorOffset = i;
        this.currentStatusText = getControl().getStatusText();
        getCvh().getLayout().setOnLongClickListener((View.OnLongClickListener) null);
        Drawable background = getCvh().getLayout().getBackground();
        if (background != null) {
            setClipLayer(((LayerDrawable) background).findDrawableByLayerId(R$id.clip_layer));
            ControlTemplate controlTemplate = getControl().getControlTemplate();
            if (setupTemplate(controlTemplate)) {
                setTemplateId(controlTemplate.getTemplateId());
                updateRange(rangeToLevelValue(getRangeTemplate().getCurrentValue()), this.isChecked, false);
                ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), this.isChecked, i, false, 4, (Object) null);
                getCvh().getLayout().setAccessibilityDelegate(new ToggleRangeBehavior$bind$1(this));
                return;
            }
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }

    public final void beginUpdateRange() {
        getCvh().setUserInteractionInProgress(true);
        getCvh().setStatusTextSize((float) getContext().getResources().getDimensionPixelSize(R$dimen.control_status_expanded));
    }

    public final void updateRange(int i, boolean z, boolean z2) {
        int max = Math.max(0, Math.min(10000, i));
        if (getClipLayer().getLevel() == 0 && max > 0) {
            getCvh().applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(z, this.colorOffset, false);
        }
        ValueAnimator valueAnimator = this.rangeAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (z2) {
            boolean z3 = max == 0 || max == 10000;
            if (getClipLayer().getLevel() != max) {
                getCvh().getControlActionCoordinator().drag(z3);
                getClipLayer().setLevel(max);
            }
        } else if (max != getClipLayer().getLevel()) {
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{getCvh().getClipLayer().getLevel(), max});
            ofInt.addUpdateListener(new ToggleRangeBehavior$updateRange$1$1(this));
            ofInt.addListener(new ToggleRangeBehavior$updateRange$1$2(this));
            ofInt.setDuration(700);
            ofInt.setInterpolator(Interpolators.CONTROL_STATE);
            ofInt.start();
            this.rangeAnimator = ofInt;
        }
        if (z) {
            this.currentRangeValue = format(getRangeTemplate().getFormatString().toString(), "%.1f", levelToRangeValue(max));
            if (z2) {
                getCvh().setStatusText(this.currentRangeValue, true);
                return;
            }
            ControlViewHolder cvh2 = getCvh();
            ControlViewHolder.setStatusText$default(cvh2, this.currentStatusText + ' ' + this.currentRangeValue, false, 2, (Object) null);
            return;
        }
        ControlViewHolder.setStatusText$default(getCvh(), this.currentStatusText, false, 2, (Object) null);
    }

    public final String format(String str, String str2, float f) {
        try {
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            return String.format(str, Arrays.copyOf(new Object[]{Float.valueOf(findNearestStep(f))}, 1));
        } catch (IllegalFormatException e) {
            Log.w("ControlsUiController", "Illegal format in range template", e);
            if (Intrinsics.areEqual((Object) str2, (Object) "")) {
                return "";
            }
            return format(str2, "", f);
        }
    }

    public final float levelToRangeValue(int i) {
        return MathUtils.constrainedMap(getRangeTemplate().getMinValue(), getRangeTemplate().getMaxValue(), 0.0f, 10000.0f, (float) i);
    }

    public final int rangeToLevelValue(float f) {
        return (int) MathUtils.constrainedMap(0.0f, 10000.0f, getRangeTemplate().getMinValue(), getRangeTemplate().getMaxValue(), f);
    }

    public final void endUpdateRange() {
        getCvh().setStatusTextSize((float) getContext().getResources().getDimensionPixelSize(R$dimen.control_status_normal));
        ControlViewHolder cvh2 = getCvh();
        cvh2.setStatusText(this.currentStatusText + ' ' + this.currentRangeValue, true);
        getCvh().getControlActionCoordinator().setValue(getCvh(), getRangeTemplate().getTemplateId(), findNearestStep(levelToRangeValue(getClipLayer().getLevel())));
        getCvh().setUserInteractionInProgress(false);
    }

    public final float findNearestStep(float f) {
        float minValue = getRangeTemplate().getMinValue();
        float f2 = Float.MAX_VALUE;
        while (minValue <= getRangeTemplate().getMaxValue()) {
            float abs = Math.abs(f - minValue);
            if (abs >= f2) {
                return minValue - getRangeTemplate().getStepValue();
            }
            minValue += getRangeTemplate().getStepValue();
            f2 = abs;
        }
        return getRangeTemplate().getMaxValue();
    }

    /* compiled from: ToggleRangeBehavior.kt */
    public final class ToggleRangeGestureListener extends GestureDetector.SimpleOnGestureListener {
        public boolean isDragging;
        @NotNull
        public final View v;

        public boolean onDown(@NotNull MotionEvent motionEvent) {
            return true;
        }

        public ToggleRangeGestureListener(@NotNull View view) {
            this.v = view;
        }

        public final boolean isDragging() {
            return this.isDragging;
        }

        public final void setDragging(boolean z) {
            this.isDragging = z;
        }

        public void onLongPress(@NotNull MotionEvent motionEvent) {
            if (!this.isDragging) {
                ToggleRangeBehavior.this.getCvh().getControlActionCoordinator().longPress(ToggleRangeBehavior.this.getCvh());
            }
        }

        public boolean onScroll(@NotNull MotionEvent motionEvent, @NotNull MotionEvent motionEvent2, float f, float f2) {
            if (!this.isDragging) {
                this.v.getParent().requestDisallowInterceptTouchEvent(true);
                ToggleRangeBehavior.this.beginUpdateRange();
                this.isDragging = true;
            }
            ToggleRangeBehavior toggleRangeBehavior = ToggleRangeBehavior.this;
            toggleRangeBehavior.updateRange(toggleRangeBehavior.getClipLayer().getLevel() + ((int) (((float) 10000) * ((-f) / ((float) this.v.getWidth())))), true, true);
            return true;
        }

        public boolean onSingleTapUp(@NotNull MotionEvent motionEvent) {
            if (!ToggleRangeBehavior.this.isToggleable()) {
                return false;
            }
            ToggleRangeBehavior.this.getCvh().getControlActionCoordinator().toggle(ToggleRangeBehavior.this.getCvh(), ToggleRangeBehavior.this.getTemplateId(), ToggleRangeBehavior.this.isChecked());
            return true;
        }
    }
}
