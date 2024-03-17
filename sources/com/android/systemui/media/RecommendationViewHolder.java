package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.util.animation.TransitionLayout;
import java.util.List;
import java.util.Set;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: RecommendationViewHolder.kt */
public final class RecommendationViewHolder {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final Set<Integer> controlsIds = SetsKt__SetsKt.setOf(Integer.valueOf(R$id.recommendation_card_icon), Integer.valueOf(R$id.media_cover1), Integer.valueOf(R$id.media_cover2), Integer.valueOf(R$id.media_cover3), Integer.valueOf(R$id.media_cover1_container), Integer.valueOf(R$id.media_cover2_container), Integer.valueOf(R$id.media_cover3_container), Integer.valueOf(R$id.media_title1), Integer.valueOf(R$id.media_title2), Integer.valueOf(R$id.media_title3), Integer.valueOf(R$id.media_subtitle1), Integer.valueOf(R$id.media_subtitle2), Integer.valueOf(R$id.media_subtitle3));
    public final ImageView cardIcon;
    @NotNull
    public final GutsViewHolder gutsViewHolder;
    @NotNull
    public final List<ViewGroup> mediaCoverContainers;
    @NotNull
    public final List<ImageView> mediaCoverItems;
    @NotNull
    public final List<TextView> mediaSubtitles;
    @NotNull
    public final List<TextView> mediaTitles;
    @NotNull
    public final TransitionLayout recommendations;

    public /* synthetic */ RecommendationViewHolder(View view, DefaultConstructorMarker defaultConstructorMarker) {
        this(view);
    }

    public RecommendationViewHolder(View view) {
        TransitionLayout transitionLayout = (TransitionLayout) view;
        this.recommendations = transitionLayout;
        this.cardIcon = (ImageView) view.requireViewById(R$id.recommendation_card_icon);
        this.mediaCoverItems = CollectionsKt__CollectionsKt.listOf((ImageView) view.requireViewById(R$id.media_cover1), (ImageView) view.requireViewById(R$id.media_cover2), (ImageView) view.requireViewById(R$id.media_cover3));
        this.mediaCoverContainers = CollectionsKt__CollectionsKt.listOf((ViewGroup) view.requireViewById(R$id.media_cover1_container), (ViewGroup) view.requireViewById(R$id.media_cover2_container), (ViewGroup) view.requireViewById(R$id.media_cover3_container));
        this.mediaTitles = CollectionsKt__CollectionsKt.listOf((TextView) view.requireViewById(R$id.media_title1), (TextView) view.requireViewById(R$id.media_title2), (TextView) view.requireViewById(R$id.media_title3));
        this.mediaSubtitles = CollectionsKt__CollectionsKt.listOf((TextView) view.requireViewById(R$id.media_subtitle1), (TextView) view.requireViewById(R$id.media_subtitle2), (TextView) view.requireViewById(R$id.media_subtitle3));
        this.gutsViewHolder = new GutsViewHolder(view);
        Drawable background = transitionLayout.getBackground();
        if (background != null) {
            IlluminationDrawable illuminationDrawable = (IlluminationDrawable) background;
            for (ViewGroup registerLightSource : getMediaCoverContainers()) {
                illuminationDrawable.registerLightSource((View) registerLightSource);
            }
            illuminationDrawable.registerLightSource(getGutsViewHolder().getCancel());
            illuminationDrawable.registerLightSource((View) getGutsViewHolder().getDismiss());
            illuminationDrawable.registerLightSource((View) getGutsViewHolder().getSettings());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.media.IlluminationDrawable");
    }

    @NotNull
    public final TransitionLayout getRecommendations() {
        return this.recommendations;
    }

    public final ImageView getCardIcon() {
        return this.cardIcon;
    }

    @NotNull
    public final List<ImageView> getMediaCoverItems() {
        return this.mediaCoverItems;
    }

    @NotNull
    public final List<ViewGroup> getMediaCoverContainers() {
        return this.mediaCoverContainers;
    }

    @NotNull
    public final List<TextView> getMediaTitles() {
        return this.mediaTitles;
    }

    @NotNull
    public final List<TextView> getMediaSubtitles() {
        return this.mediaSubtitles;
    }

    @NotNull
    public final GutsViewHolder getGutsViewHolder() {
        return this.gutsViewHolder;
    }

    public final void marquee(boolean z, long j) {
        this.gutsViewHolder.marquee(z, j, "RecommendationViewHolder");
    }

    /* compiled from: RecommendationViewHolder.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        @NotNull
        public final RecommendationViewHolder create(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
            View inflate = layoutInflater.inflate(R$layout.media_smartspace_recommendations, viewGroup, false);
            inflate.setLayoutDirection(3);
            return new RecommendationViewHolder(inflate, (DefaultConstructorMarker) null);
        }

        @NotNull
        public final Set<Integer> getControlsIds() {
            return RecommendationViewHolder.controlsIds;
        }
    }
}
