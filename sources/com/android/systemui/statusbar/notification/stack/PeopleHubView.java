package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PeopleHubView.kt */
public final class PeopleHubView extends StackScrollerDecorView implements SwipeableView {
    public boolean canSwipe;
    public ViewGroup contents;
    public TextView label;
    public Sequence<Object> personViewAdapters;

    @Nullable
    public NotificationMenuRowPlugin createMenu() {
        return null;
    }

    @Nullable
    public View findSecondaryView() {
        return null;
    }

    public boolean hasFinishedInitialization() {
        return true;
    }

    public boolean needsClippingToShelf() {
        return true;
    }

    public PeopleHubView(@NotNull Context context, @NotNull AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        this.contents = (ViewGroup) requireViewById(R$id.people_list);
        this.label = (TextView) requireViewById(R$id.header_label);
        ViewGroup viewGroup = this.contents;
        if (viewGroup == null) {
            viewGroup = null;
        }
        this.personViewAdapters = CollectionsKt___CollectionsKt.asSequence(SequencesKt___SequencesKt.toList(SequencesKt___SequencesKt.mapNotNull(CollectionsKt___CollectionsKt.asSequence(RangesKt___RangesKt.until(0, viewGroup.getChildCount())), new PeopleHubView$onFinishInflate$1(this))));
        super.onFinishInflate();
        setVisible(true, false);
    }

    @NotNull
    public View findContentView() {
        ViewGroup viewGroup = this.contents;
        if (viewGroup == null) {
            return null;
        }
        return viewGroup;
    }

    public void resetTranslation() {
        setTranslationX(0.0f);
    }

    public void setTranslation(float f) {
        if (this.canSwipe) {
            super.setTranslation(f);
        }
    }

    public final boolean getCanSwipe() {
        return this.canSwipe;
    }

    public void applyContentTransformation(float f, float f2) {
        super.applyContentTransformation(f, f2);
        ViewGroup viewGroup = this.contents;
        if (viewGroup == null) {
            viewGroup = null;
        }
        int childCount = viewGroup.getChildCount();
        int i = 0;
        while (i < childCount) {
            int i2 = i + 1;
            ViewGroup viewGroup2 = this.contents;
            if (viewGroup2 == null) {
                viewGroup2 = null;
            }
            View childAt = viewGroup2.getChildAt(i);
            childAt.setAlpha(f);
            childAt.setTranslationY(f2);
            i = i2;
        }
    }

    /* compiled from: PeopleHubView.kt */
    public final class PersonDataListenerImpl {
        @NotNull
        public final ImageView avatarView;

        public PersonDataListenerImpl(@NotNull ImageView imageView) {
            this.avatarView = imageView;
        }
    }
}
