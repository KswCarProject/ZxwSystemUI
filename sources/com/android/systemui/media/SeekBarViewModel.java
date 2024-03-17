package com.android.systemui.media;

import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.SeekBar;
import androidx.core.view.GestureDetectorCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.util.concurrency.RepeatableExecutor;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SeekBarViewModel.kt */
public final class SeekBarViewModel {
    @NotNull
    public Progress _data = new Progress(false, false, false, false, (Integer) null, 0);
    @NotNull
    public final MutableLiveData<Progress> _progress;
    @NotNull
    public final RepeatableExecutor bgExecutor;
    @NotNull
    public SeekBarViewModel$callback$1 callback;
    @Nullable
    public Runnable cancel;
    @Nullable
    public MediaController controller;
    @Nullable
    public EnabledChangeListener enabledChangeListener;
    public boolean isFalseSeek;
    public boolean listening;
    public Function0<Unit> logSeek;
    @Nullable
    public PlaybackState playbackState;
    public boolean scrubbing;
    @Nullable
    public ScrubbingChangeListener scrubbingChangeListener;

    /* compiled from: SeekBarViewModel.kt */
    public interface EnabledChangeListener {
        void onEnabledChanged(boolean z);
    }

    /* compiled from: SeekBarViewModel.kt */
    public interface ScrubbingChangeListener {
        void onScrubbingChanged(boolean z);
    }

    public SeekBarViewModel(@NotNull RepeatableExecutor repeatableExecutor) {
        this.bgExecutor = repeatableExecutor;
        MutableLiveData<Progress> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.postValue(this._data);
        this._progress = mutableLiveData;
        this.callback = new SeekBarViewModel$callback$1(this);
        this.listening = true;
    }

    public final void set_data(Progress progress) {
        EnabledChangeListener enabledChangeListener2;
        boolean z = progress.getEnabled() != this._data.getEnabled();
        this._data = progress;
        if (z && (enabledChangeListener2 = this.enabledChangeListener) != null) {
            enabledChangeListener2.onEnabledChanged(progress.getEnabled());
        }
        this._progress.postValue(progress);
    }

    @NotNull
    public final LiveData<Progress> getProgress() {
        return this._progress;
    }

    public final void setController(MediaController mediaController) {
        MediaController mediaController2 = this.controller;
        MediaSession.Token token = null;
        MediaSession.Token sessionToken = mediaController2 == null ? null : mediaController2.getSessionToken();
        if (mediaController != null) {
            token = mediaController.getSessionToken();
        }
        if (!Intrinsics.areEqual((Object) sessionToken, (Object) token)) {
            MediaController mediaController3 = this.controller;
            if (mediaController3 != null) {
                mediaController3.unregisterCallback(this.callback);
            }
            if (mediaController != null) {
                mediaController.registerCallback(this.callback);
            }
            this.controller = mediaController;
        }
    }

    public final void setListening(boolean z) {
        this.bgExecutor.execute(new SeekBarViewModel$listening$1(this, z));
    }

    public final void setScrubbing(boolean z) {
        if (this.scrubbing != z) {
            this.scrubbing = z;
            checkIfPollingNeeded();
            ScrubbingChangeListener scrubbingChangeListener2 = this.scrubbingChangeListener;
            if (scrubbingChangeListener2 != null) {
                scrubbingChangeListener2.onScrubbingChanged(z);
            }
            set_data(Progress.copy$default(this._data, false, false, false, z, (Integer) null, 0, 55, (Object) null));
        }
    }

    @NotNull
    public final Function0<Unit> getLogSeek() {
        Function0<Unit> function0 = this.logSeek;
        if (function0 != null) {
            return function0;
        }
        return null;
    }

    public final void setLogSeek(@NotNull Function0<Unit> function0) {
        this.logSeek = function0;
    }

    public final void onSeekStarting() {
        this.bgExecutor.execute(new SeekBarViewModel$onSeekStarting$1(this));
    }

    public final void onSeekProgress(long j) {
        this.bgExecutor.execute(new SeekBarViewModel$onSeekProgress$1(this, j));
    }

    public final void onSeekFalse() {
        this.bgExecutor.execute(new SeekBarViewModel$onSeekFalse$1(this));
    }

    public final void onSeek(long j) {
        this.bgExecutor.execute(new SeekBarViewModel$onSeek$1(this, j));
    }

    public final void updateController(@Nullable MediaController mediaController) {
        int i;
        boolean z;
        boolean z2;
        setController(mediaController);
        MediaController mediaController2 = this.controller;
        Integer num = null;
        this.playbackState = mediaController2 == null ? null : mediaController2.getPlaybackState();
        MediaController mediaController3 = this.controller;
        MediaMetadata metadata = mediaController3 == null ? null : mediaController3.getMetadata();
        PlaybackState playbackState2 = this.playbackState;
        boolean z3 = ((playbackState2 == null ? 0 : playbackState2.getActions()) & 256) != 0;
        PlaybackState playbackState3 = this.playbackState;
        if (playbackState3 != null) {
            num = Integer.valueOf((int) playbackState3.getPosition());
        }
        Integer num2 = num;
        if (metadata == null) {
            i = 0;
        } else {
            i = (int) metadata.getLong("android.media.metadata.DURATION");
        }
        PlaybackState playbackState4 = this.playbackState;
        boolean isPlayingState = NotificationMediaManager.isPlayingState(playbackState4 == null ? 0 : playbackState4.getState());
        PlaybackState playbackState5 = this.playbackState;
        if (playbackState5 != null) {
            if (playbackState5 != null && playbackState5.getState() == 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!z2 && i > 0) {
                z = true;
                set_data(new Progress(z, z3, isPlayingState, this.scrubbing, num2, i));
                checkIfPollingNeeded();
            }
        }
        z = false;
        set_data(new Progress(z, z3, isPlayingState, this.scrubbing, num2, i));
        checkIfPollingNeeded();
    }

    public final void clearController() {
        this.bgExecutor.execute(new SeekBarViewModel$clearController$1(this));
    }

    public final void onDestroy() {
        this.bgExecutor.execute(new SeekBarViewModel$onDestroy$1(this));
    }

    public final void checkPlaybackPosition() {
        int duration = this._data.getDuration();
        PlaybackState playbackState2 = this.playbackState;
        Integer valueOf = playbackState2 == null ? null : Integer.valueOf((int) SeekBarViewModelKt.computePosition(playbackState2, (long) duration));
        if (valueOf != null && !Intrinsics.areEqual((Object) this._data.getElapsedTime(), (Object) valueOf)) {
            set_data(Progress.copy$default(this._data, false, false, false, false, valueOf, 0, 47, (Object) null));
        }
    }

    public final void checkIfPollingNeeded() {
        boolean z = false;
        if (this.listening && !this.scrubbing) {
            PlaybackState playbackState2 = this.playbackState;
            if (playbackState2 == null ? false : SeekBarViewModelKt.isInMotion(playbackState2)) {
                z = true;
            }
        }
        if (!z) {
            Runnable runnable = this.cancel;
            if (runnable != null) {
                runnable.run();
            }
            this.cancel = null;
        } else if (this.cancel == null) {
            this.cancel = this.bgExecutor.executeRepeatedly(new SeekBarViewModel$checkIfPollingNeeded$1(this), 0, 100);
        }
    }

    @NotNull
    public final SeekBar.OnSeekBarChangeListener getSeekBarListener() {
        return new SeekBarChangeListener(this);
    }

    public final void attachTouchHandlers(@NotNull SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(getSeekBarListener());
        seekBar.setOnTouchListener(new SeekBarTouchListener(this, seekBar));
    }

    public final void setScrubbingChangeListener(@NotNull ScrubbingChangeListener scrubbingChangeListener2) {
        this.scrubbingChangeListener = scrubbingChangeListener2;
    }

    public final void removeScrubbingChangeListener(@NotNull ScrubbingChangeListener scrubbingChangeListener2) {
        if (Intrinsics.areEqual((Object) scrubbingChangeListener2, (Object) this.scrubbingChangeListener)) {
            this.scrubbingChangeListener = null;
        }
    }

    public final void setEnabledChangeListener(@NotNull EnabledChangeListener enabledChangeListener2) {
        this.enabledChangeListener = enabledChangeListener2;
    }

    public final void removeEnabledChangeListener(@NotNull EnabledChangeListener enabledChangeListener2) {
        if (Intrinsics.areEqual((Object) enabledChangeListener2, (Object) this.enabledChangeListener)) {
            this.enabledChangeListener = null;
        }
    }

    /* compiled from: SeekBarViewModel.kt */
    public static final class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @NotNull
        public final SeekBarViewModel viewModel;

        public SeekBarChangeListener(@NotNull SeekBarViewModel seekBarViewModel) {
            this.viewModel = seekBarViewModel;
        }

        public void onProgressChanged(@NotNull SeekBar seekBar, int i, boolean z) {
            if (z) {
                this.viewModel.onSeekProgress((long) i);
            }
        }

        public void onStartTrackingTouch(@NotNull SeekBar seekBar) {
            this.viewModel.onSeekStarting();
        }

        public void onStopTrackingTouch(@NotNull SeekBar seekBar) {
            this.viewModel.onSeek((long) seekBar.getProgress());
        }
    }

    /* compiled from: SeekBarViewModel.kt */
    public static final class SeekBarTouchListener implements View.OnTouchListener, GestureDetector.OnGestureListener {
        @NotNull
        public final SeekBar bar;
        @NotNull
        public final GestureDetectorCompat detector;
        public final int flingVelocity;
        public boolean shouldGoToSeekBar;
        @NotNull
        public final SeekBarViewModel viewModel;

        public void onLongPress(@NotNull MotionEvent motionEvent) {
        }

        public void onShowPress(@NotNull MotionEvent motionEvent) {
        }

        public SeekBarTouchListener(@NotNull SeekBarViewModel seekBarViewModel, @NotNull SeekBar seekBar) {
            this.viewModel = seekBarViewModel;
            this.bar = seekBar;
            this.detector = new GestureDetectorCompat(seekBar.getContext(), this);
            this.flingVelocity = ViewConfiguration.get(seekBar.getContext()).getScaledMinimumFlingVelocity() * 10;
        }

        public boolean onTouch(@NotNull View view, @NotNull MotionEvent motionEvent) {
            if (!Intrinsics.areEqual((Object) view, (Object) this.bar)) {
                return false;
            }
            this.detector.onTouchEvent(motionEvent);
            return !this.shouldGoToSeekBar;
        }

        public boolean onDown(@NotNull MotionEvent motionEvent) {
            double d;
            double d2;
            ViewParent parent;
            int paddingLeft = this.bar.getPaddingLeft();
            int paddingRight = this.bar.getPaddingRight();
            int progress = this.bar.getProgress();
            int max = this.bar.getMax() - this.bar.getMin();
            double min = max > 0 ? ((double) (progress - this.bar.getMin())) / ((double) max) : 0.0d;
            int width = (this.bar.getWidth() - paddingLeft) - paddingRight;
            if (this.bar.isLayoutRtl()) {
                d2 = (double) paddingLeft;
                d = ((double) width) * (((double) 1) - min);
            } else {
                d2 = (double) paddingLeft;
                d = ((double) width) * min;
            }
            double d3 = d2 + d;
            long height = (long) (this.bar.getHeight() / 2);
            int round = (int) (Math.round(d3) - height);
            int round2 = (int) (Math.round(d3) + height);
            int round3 = Math.round(motionEvent.getX());
            boolean z = round3 >= round && round3 <= round2;
            this.shouldGoToSeekBar = z;
            if (z && (parent = this.bar.getParent()) != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            return this.shouldGoToSeekBar;
        }

        public boolean onSingleTapUp(@NotNull MotionEvent motionEvent) {
            this.shouldGoToSeekBar = true;
            return true;
        }

        public boolean onScroll(@NotNull MotionEvent motionEvent, @NotNull MotionEvent motionEvent2, float f, float f2) {
            return this.shouldGoToSeekBar;
        }

        public boolean onFling(@NotNull MotionEvent motionEvent, @NotNull MotionEvent motionEvent2, float f, float f2) {
            if (Math.abs(f) > ((float) this.flingVelocity) || Math.abs(f2) > ((float) this.flingVelocity)) {
                this.viewModel.onSeekFalse();
            }
            return this.shouldGoToSeekBar;
        }
    }

    /* compiled from: SeekBarViewModel.kt */
    public static final class Progress {
        public final int duration;
        @Nullable
        public final Integer elapsedTime;
        public final boolean enabled;
        public final boolean playing;
        public final boolean scrubbing;
        public final boolean seekAvailable;

        public static /* synthetic */ Progress copy$default(Progress progress, boolean z, boolean z2, boolean z3, boolean z4, Integer num, int i, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                z = progress.enabled;
            }
            if ((i2 & 2) != 0) {
                z2 = progress.seekAvailable;
            }
            boolean z5 = z2;
            if ((i2 & 4) != 0) {
                z3 = progress.playing;
            }
            boolean z6 = z3;
            if ((i2 & 8) != 0) {
                z4 = progress.scrubbing;
            }
            boolean z7 = z4;
            if ((i2 & 16) != 0) {
                num = progress.elapsedTime;
            }
            Integer num2 = num;
            if ((i2 & 32) != 0) {
                i = progress.duration;
            }
            return progress.copy(z, z5, z6, z7, num2, i);
        }

        @NotNull
        public final Progress copy(boolean z, boolean z2, boolean z3, boolean z4, @Nullable Integer num, int i) {
            return new Progress(z, z2, z3, z4, num, i);
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Progress)) {
                return false;
            }
            Progress progress = (Progress) obj;
            return this.enabled == progress.enabled && this.seekAvailable == progress.seekAvailable && this.playing == progress.playing && this.scrubbing == progress.scrubbing && Intrinsics.areEqual((Object) this.elapsedTime, (Object) progress.elapsedTime) && this.duration == progress.duration;
        }

        public int hashCode() {
            boolean z = this.enabled;
            boolean z2 = true;
            if (z) {
                z = true;
            }
            int i = (z ? 1 : 0) * true;
            boolean z3 = this.seekAvailable;
            if (z3) {
                z3 = true;
            }
            int i2 = (i + (z3 ? 1 : 0)) * 31;
            boolean z4 = this.playing;
            if (z4) {
                z4 = true;
            }
            int i3 = (i2 + (z4 ? 1 : 0)) * 31;
            boolean z5 = this.scrubbing;
            if (!z5) {
                z2 = z5;
            }
            int i4 = (i3 + (z2 ? 1 : 0)) * 31;
            Integer num = this.elapsedTime;
            return ((i4 + (num == null ? 0 : num.hashCode())) * 31) + Integer.hashCode(this.duration);
        }

        @NotNull
        public String toString() {
            return "Progress(enabled=" + this.enabled + ", seekAvailable=" + this.seekAvailable + ", playing=" + this.playing + ", scrubbing=" + this.scrubbing + ", elapsedTime=" + this.elapsedTime + ", duration=" + this.duration + ')';
        }

        public Progress(boolean z, boolean z2, boolean z3, boolean z4, @Nullable Integer num, int i) {
            this.enabled = z;
            this.seekAvailable = z2;
            this.playing = z3;
            this.scrubbing = z4;
            this.elapsedTime = num;
            this.duration = i;
        }

        public final boolean getEnabled() {
            return this.enabled;
        }

        public final boolean getSeekAvailable() {
            return this.seekAvailable;
        }

        public final boolean getPlaying() {
            return this.playing;
        }

        public final boolean getScrubbing() {
            return this.scrubbing;
        }

        @Nullable
        public final Integer getElapsedTime() {
            return this.elapsedTime;
        }

        public final int getDuration() {
            return this.duration;
        }
    }
}
