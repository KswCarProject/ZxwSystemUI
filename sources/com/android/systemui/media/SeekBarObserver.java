package com.android.systemui.media;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.widget.SeekBar;
import androidx.lifecycle.Observer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$dimen;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.media.SeekBarViewModel;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SeekBarObserver.kt */
public class SeekBarObserver implements Observer<SeekBarViewModel.Progress> {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final int RESET_ANIMATION_DURATION_MS = 750;
    public static final int RESET_ANIMATION_THRESHOLD_MS = 250;
    @NotNull
    public final MediaViewHolder holder;
    public final int seekBarDisabledHeight;
    public final int seekBarDisabledVerticalPadding;
    public final int seekBarEnabledMaxHeight;
    public final int seekBarEnabledVerticalPadding;
    @Nullable
    public Animator seekBarResetAnimator;

    public SeekBarObserver(@NotNull MediaViewHolder mediaViewHolder) {
        this.holder = mediaViewHolder;
        this.seekBarEnabledMaxHeight = mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_enabled_seekbar_height);
        this.seekBarDisabledHeight = mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_disabled_seekbar_height);
        this.seekBarEnabledVerticalPadding = mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_session_enabled_seekbar_vertical_padding);
        this.seekBarDisabledVerticalPadding = mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_session_disabled_seekbar_vertical_padding);
        float dimensionPixelSize = (float) mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_seekbar_progress_wavelength);
        float dimensionPixelSize2 = (float) mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_seekbar_progress_amplitude);
        float dimensionPixelSize3 = (float) mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_seekbar_progress_phase);
        float dimensionPixelSize4 = (float) mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_seekbar_progress_stroke_width);
        Drawable progressDrawable = mediaViewHolder.getSeekBar().getProgressDrawable();
        SquigglyProgress squigglyProgress = progressDrawable instanceof SquigglyProgress ? (SquigglyProgress) progressDrawable : null;
        if (squigglyProgress != null) {
            squigglyProgress.setWaveLength(dimensionPixelSize);
            squigglyProgress.setLineAmplitude(dimensionPixelSize2);
            squigglyProgress.setPhaseSpeed(dimensionPixelSize3);
            squigglyProgress.setStrokeWidth(dimensionPixelSize4);
        }
    }

    /* compiled from: SeekBarObserver.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    @Nullable
    public final Animator getSeekBarResetAnimator() {
        return this.seekBarResetAnimator;
    }

    public final void setSeekBarResetAnimator(@Nullable Animator animator) {
        this.seekBarResetAnimator = animator;
    }

    public void onChanged(@NotNull SeekBarViewModel.Progress progress) {
        Drawable progressDrawable = this.holder.getSeekBar().getProgressDrawable();
        SquigglyProgress squigglyProgress = progressDrawable instanceof SquigglyProgress ? (SquigglyProgress) progressDrawable : null;
        if (!progress.getEnabled()) {
            if (this.holder.getSeekBar().getMaxHeight() != this.seekBarDisabledHeight) {
                this.holder.getSeekBar().setMaxHeight(this.seekBarDisabledHeight);
                setVerticalPadding(this.seekBarDisabledVerticalPadding);
            }
            this.holder.getSeekBar().setEnabled(false);
            if (squigglyProgress != null) {
                squigglyProgress.setAnimate(false);
            }
            this.holder.getSeekBar().getThumb().setAlpha(0);
            this.holder.getSeekBar().setProgress(0);
            this.holder.getSeekBar().setContentDescription("");
            this.holder.getScrubbingElapsedTimeView().setText("");
            this.holder.getScrubbingTotalTimeView().setText("");
            return;
        }
        this.holder.getSeekBar().getThumb().setAlpha(progress.getSeekAvailable() ? 255 : 0);
        this.holder.getSeekBar().setEnabled(progress.getSeekAvailable());
        if (squigglyProgress != null) {
            squigglyProgress.setAnimate(progress.getPlaying() && !progress.getScrubbing());
        }
        if (squigglyProgress != null) {
            squigglyProgress.setTransitionEnabled(!progress.getSeekAvailable());
        }
        if (this.holder.getSeekBar().getMaxHeight() != this.seekBarEnabledMaxHeight) {
            this.holder.getSeekBar().setMaxHeight(this.seekBarEnabledMaxHeight);
            setVerticalPadding(this.seekBarEnabledVerticalPadding);
        }
        this.holder.getSeekBar().setMax(progress.getDuration());
        String formatElapsedTime = DateUtils.formatElapsedTime(((long) progress.getDuration()) / 1000);
        if (progress.getScrubbing()) {
            this.holder.getScrubbingTotalTimeView().setText(formatElapsedTime);
        }
        Integer elapsedTime = progress.getElapsedTime();
        if (elapsedTime != null) {
            int intValue = elapsedTime.intValue();
            if (!progress.getScrubbing()) {
                Animator seekBarResetAnimator2 = getSeekBarResetAnimator();
                if (!(seekBarResetAnimator2 == null ? false : seekBarResetAnimator2.isRunning())) {
                    int i = RESET_ANIMATION_THRESHOLD_MS;
                    if (intValue > i || this.holder.getSeekBar().getProgress() <= i) {
                        this.holder.getSeekBar().setProgress(intValue);
                    } else {
                        Animator buildResetAnimator = buildResetAnimator(intValue);
                        buildResetAnimator.start();
                        setSeekBarResetAnimator(buildResetAnimator);
                    }
                }
            }
            String formatElapsedTime2 = DateUtils.formatElapsedTime(((long) intValue) / 1000);
            if (progress.getScrubbing()) {
                this.holder.getScrubbingElapsedTimeView().setText(formatElapsedTime2);
            }
            this.holder.getSeekBar().setContentDescription(this.holder.getSeekBar().getContext().getString(R$string.controls_media_seekbar_description, new Object[]{formatElapsedTime2, formatElapsedTime}));
        }
    }

    @NotNull
    @VisibleForTesting
    public Animator buildResetAnimator(int i) {
        SeekBar seekBar = this.holder.getSeekBar();
        int i2 = RESET_ANIMATION_DURATION_MS;
        ObjectAnimator ofInt = ObjectAnimator.ofInt(seekBar, "progress", new int[]{this.holder.getSeekBar().getProgress(), i + i2});
        ofInt.setAutoCancel(true);
        ofInt.setDuration((long) i2);
        ofInt.setInterpolator(Interpolators.EMPHASIZED);
        return ofInt;
    }

    public final void setVerticalPadding(int i) {
        this.holder.getSeekBar().setPadding(this.holder.getSeekBar().getPaddingLeft(), i, this.holder.getSeekBar().getPaddingRight(), this.holder.getSeekBar().getPaddingBottom());
    }
}
