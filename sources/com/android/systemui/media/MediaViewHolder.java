package com.android.systemui.media;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.Barrier;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.util.animation.TransitionLayout;
import java.util.List;
import java.util.Set;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaViewHolder.kt */
public final class MediaViewHolder {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final Set<Integer> controlsIds;
    @NotNull
    public static final Set<Integer> expandedBottomActionIds;
    @NotNull
    public static final Set<Integer> genericButtonIds;
    public final ImageButton action0;
    public final ImageButton action1;
    public final ImageButton action2;
    public final ImageButton action3;
    public final ImageButton action4;
    public final ImageButton actionNext;
    public final ImageButton actionPlayPause;
    public final ImageButton actionPrev;
    public final Barrier actionsTopBarrier;
    public final ImageView albumView;
    public final ImageView appIcon;
    public final TextView artistText;
    @NotNull
    public final GutsViewHolder gutsViewHolder;
    @NotNull
    public final TransitionLayout player;
    @NotNull
    public final TextView scrubbingElapsedTimeView;
    @NotNull
    public final TextView scrubbingTotalTimeView;
    public final ViewGroup seamless;
    public final View seamlessButton;
    public final ImageView seamlessIcon;
    public final TextView seamlessText;
    public final SeekBar seekBar;
    public final TextView titleText;

    public MediaViewHolder(@NotNull View view) {
        this.player = (TransitionLayout) view;
        this.albumView = (ImageView) view.requireViewById(R$id.album_art);
        this.appIcon = (ImageView) view.requireViewById(R$id.icon);
        this.titleText = (TextView) view.requireViewById(R$id.header_title);
        this.artistText = (TextView) view.requireViewById(R$id.header_artist);
        this.seamless = (ViewGroup) view.requireViewById(R$id.media_seamless);
        this.seamlessIcon = (ImageView) view.requireViewById(R$id.media_seamless_image);
        this.seamlessText = (TextView) view.requireViewById(R$id.media_seamless_text);
        this.seamlessButton = view.requireViewById(R$id.media_seamless_button);
        this.seekBar = (SeekBar) view.requireViewById(R$id.media_progress_bar);
        this.scrubbingElapsedTimeView = (TextView) view.requireViewById(R$id.media_scrubbing_elapsed_time);
        this.scrubbingTotalTimeView = (TextView) view.requireViewById(R$id.media_scrubbing_total_time);
        this.gutsViewHolder = new GutsViewHolder(view);
        this.actionPlayPause = (ImageButton) view.requireViewById(R$id.actionPlayPause);
        this.actionNext = (ImageButton) view.requireViewById(R$id.actionNext);
        this.actionPrev = (ImageButton) view.requireViewById(R$id.actionPrev);
        this.action0 = (ImageButton) view.requireViewById(R$id.action0);
        this.action1 = (ImageButton) view.requireViewById(R$id.action1);
        this.action2 = (ImageButton) view.requireViewById(R$id.action2);
        this.action3 = (ImageButton) view.requireViewById(R$id.action3);
        this.action4 = (ImageButton) view.requireViewById(R$id.action4);
        this.actionsTopBarrier = (Barrier) view.requireViewById(R$id.media_action_barrier_top);
    }

    @NotNull
    public final TransitionLayout getPlayer() {
        return this.player;
    }

    public final ImageView getAlbumView() {
        return this.albumView;
    }

    public final ImageView getAppIcon() {
        return this.appIcon;
    }

    public final TextView getTitleText() {
        return this.titleText;
    }

    public final TextView getArtistText() {
        return this.artistText;
    }

    public final ViewGroup getSeamless() {
        return this.seamless;
    }

    public final ImageView getSeamlessIcon() {
        return this.seamlessIcon;
    }

    public final TextView getSeamlessText() {
        return this.seamlessText;
    }

    public final View getSeamlessButton() {
        return this.seamlessButton;
    }

    public final SeekBar getSeekBar() {
        return this.seekBar;
    }

    @NotNull
    public final TextView getScrubbingElapsedTimeView() {
        return this.scrubbingElapsedTimeView;
    }

    @NotNull
    public final TextView getScrubbingTotalTimeView() {
        return this.scrubbingTotalTimeView;
    }

    @NotNull
    public final GutsViewHolder getGutsViewHolder() {
        return this.gutsViewHolder;
    }

    public final ImageButton getActionPlayPause() {
        return this.actionPlayPause;
    }

    @NotNull
    public final ImageButton getAction(int i) {
        if (i == R$id.actionPlayPause) {
            return this.actionPlayPause;
        }
        if (i == R$id.actionNext) {
            return this.actionNext;
        }
        if (i == R$id.actionPrev) {
            return this.actionPrev;
        }
        if (i == R$id.action0) {
            return this.action0;
        }
        if (i == R$id.action1) {
            return this.action1;
        }
        if (i == R$id.action2) {
            return this.action2;
        }
        if (i == R$id.action3) {
            return this.action3;
        }
        if (i == R$id.action4) {
            return this.action4;
        }
        throw new IllegalArgumentException();
    }

    @NotNull
    public final List<ImageButton> getTransparentActionButtons() {
        return CollectionsKt__CollectionsKt.listOf(this.actionNext, this.actionPrev, this.action0, this.action1, this.action2, this.action3, this.action4);
    }

    public final void marquee(boolean z, long j) {
        this.gutsViewHolder.marquee(z, j, "MediaViewHolder");
    }

    /* compiled from: MediaViewHolder.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @NotNull
        public final MediaViewHolder create(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
            View inflate = layoutInflater.inflate(R$layout.media_session_view, viewGroup, false);
            inflate.setLayerType(2, (Paint) null);
            inflate.setLayoutDirection(3);
            MediaViewHolder mediaViewHolder = new MediaViewHolder(inflate);
            mediaViewHolder.getSeekBar().setLayoutDirection(0);
            return mediaViewHolder;
        }

        @NotNull
        public final Set<Integer> getControlsIds() {
            return MediaViewHolder.controlsIds;
        }

        @NotNull
        public final Set<Integer> getGenericButtonIds() {
            return MediaViewHolder.genericButtonIds;
        }

        @NotNull
        public final Set<Integer> getExpandedBottomActionIds() {
            return MediaViewHolder.expandedBottomActionIds;
        }
    }

    static {
        int i = R$id.icon;
        int i2 = R$id.actionNext;
        int i3 = R$id.actionPrev;
        int i4 = R$id.action0;
        int i5 = R$id.action1;
        int i6 = R$id.action2;
        int i7 = R$id.action3;
        int i8 = R$id.action4;
        int i9 = R$id.media_scrubbing_elapsed_time;
        int i10 = R$id.media_scrubbing_total_time;
        controlsIds = SetsKt__SetsKt.setOf(Integer.valueOf(i), Integer.valueOf(R$id.app_name), Integer.valueOf(R$id.header_title), Integer.valueOf(R$id.header_artist), Integer.valueOf(R$id.media_seamless), Integer.valueOf(R$id.media_progress_bar), Integer.valueOf(R$id.actionPlayPause), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7), Integer.valueOf(i8), Integer.valueOf(i), Integer.valueOf(i9), Integer.valueOf(i10));
        genericButtonIds = SetsKt__SetsKt.setOf(Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7), Integer.valueOf(i8));
        expandedBottomActionIds = SetsKt__SetsKt.setOf(Integer.valueOf(i3), Integer.valueOf(i2), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7), Integer.valueOf(i8), Integer.valueOf(i9), Integer.valueOf(i10));
    }
}
