package com.android.systemui.dreams.complication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.touch.TouchInsetManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComplicationLayoutEngine implements Complication.VisibilityController {
    public final HashMap<ComplicationId, ViewEntry> mEntries = new HashMap<>();
    public final int mFadeInDuration;
    public final int mFadeOutDuration;
    public final ConstraintLayout mLayout;
    public final int mMargin;
    public final HashMap<Integer, PositionGroup> mPositions = new HashMap<>();
    public final TouchInsetManager.TouchInsetSession mSession;
    public ViewPropertyAnimator mViewPropertyAnimator;

    public static class ViewEntry implements Comparable<ViewEntry> {
        public final int mCategory;
        public final Parent mParent;
        public final TouchInsetManager.TouchInsetSession mTouchInsetSession;
        public final View mView;

        public interface Parent {
            void removeEntry(ViewEntry viewEntry);
        }

        public final View getView() {
            return this.mView;
        }

        public void applyLayoutParams(View view) {
            throw null;
        }

        public void remove() {
            this.mParent.removeEntry(this);
            ((ViewGroup) this.mView.getParent()).removeView(this.mView);
            this.mTouchInsetSession.removeViewFromTracking(this.mView);
        }

        public int compareTo(ViewEntry viewEntry) {
            int i = viewEntry.mCategory;
            int i2 = this.mCategory;
            if (i != i2) {
                return i2 == 2 ? 1 : -1;
            }
            throw null;
        }

        public static class Builder {
            public final int mCategory;
            public int mMargin;
            public final TouchInsetManager.TouchInsetSession mTouchSession;
            public final View mView;

            public Builder(View view, TouchInsetManager.TouchInsetSession touchInsetSession, ComplicationLayoutParams complicationLayoutParams, int i) {
                this.mView = view;
                this.mCategory = i;
                this.mTouchSession = touchInsetSession;
            }

            public Builder setMargin(int i) {
                this.mMargin = i;
                return this;
            }
        }
    }

    public static class PositionGroup implements DirectionGroup.Parent {
        public final HashMap<Integer, DirectionGroup> mDirectionGroups = new HashMap<>();

        public void onEntriesChanged() {
            updateViews();
        }

        public final void updateViews() {
            ViewEntry viewEntry = null;
            for (DirectionGroup head : this.mDirectionGroups.values()) {
                ViewEntry head2 = head.getHead();
                if (viewEntry == null || (head2 != null && head2.compareTo(viewEntry) > 0)) {
                    viewEntry = head2;
                }
            }
            if (viewEntry != null) {
                for (DirectionGroup updateViews : this.mDirectionGroups.values()) {
                    updateViews.updateViews(viewEntry.getView());
                }
            }
        }

        public final ArrayList<ViewEntry> getViews() {
            ArrayList<ViewEntry> arrayList = new ArrayList<>();
            for (DirectionGroup r1 : this.mDirectionGroups.values()) {
                arrayList.addAll(r1.getViews());
            }
            return arrayList;
        }
    }

    public static class DirectionGroup implements ViewEntry.Parent {
        public final Parent mParent;
        public final ArrayList<ViewEntry> mViews;

        public interface Parent {
            void onEntriesChanged();
        }

        public ViewEntry getHead() {
            if (this.mViews.isEmpty()) {
                return null;
            }
            return this.mViews.get(0);
        }

        public void removeEntry(ViewEntry viewEntry) {
            this.mViews.remove(viewEntry);
            this.mParent.onEntriesChanged();
        }

        public void updateViews(View view) {
            Iterator<ViewEntry> it = this.mViews.iterator();
            while (it.hasNext()) {
                ViewEntry next = it.next();
                next.applyLayoutParams(view);
                view = next.getView();
            }
        }

        public final List<ViewEntry> getViews() {
            return this.mViews;
        }
    }

    public ComplicationLayoutEngine(ConstraintLayout constraintLayout, int i, TouchInsetManager.TouchInsetSession touchInsetSession, int i2, int i3) {
        this.mLayout = constraintLayout;
        this.mMargin = i;
        this.mSession = touchInsetSession;
        this.mFadeInDuration = i2;
        this.mFadeOutDuration = i3;
    }

    public void setVisibility(final int i, boolean z) {
        boolean z2 = i == 0;
        ViewPropertyAnimator viewPropertyAnimator = this.mViewPropertyAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        if (z2) {
            this.mLayout.setVisibility(0);
        }
        this.mViewPropertyAnimator = this.mLayout.animate().alpha(z2 ? 1.0f : 0.0f).setDuration((long) (z2 ? this.mFadeInDuration : this.mFadeOutDuration)).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ComplicationLayoutEngine.this.mLayout.setVisibility(i);
            }
        });
    }

    public void addComplication(ComplicationId complicationId, View view, ComplicationLayoutParams complicationLayoutParams, int i) {
        Log.d("ComplicationLayoutEngine", "engine: " + this + " addComplication");
        if (this.mEntries.containsKey(complicationId)) {
            removeComplication(complicationId);
        }
        new ViewEntry.Builder(view, this.mSession, complicationLayoutParams, i).setMargin(this.mMargin);
        throw null;
    }

    public boolean removeComplication(ComplicationId complicationId) {
        ViewEntry remove = this.mEntries.remove(complicationId);
        if (remove == null) {
            Log.e("ComplicationLayoutEngine", "could not find id:" + complicationId);
            return false;
        }
        remove.remove();
        return true;
    }

    public List<View> getViewsAtPosition(int i) {
        return (List) this.mPositions.entrySet().stream().filter(new ComplicationLayoutEngine$$ExternalSyntheticLambda0(i)).flatMap(new ComplicationLayoutEngine$$ExternalSyntheticLambda1()).map(new ComplicationLayoutEngine$$ExternalSyntheticLambda2()).collect(Collectors.toList());
    }

    public static /* synthetic */ boolean lambda$getViewsAtPosition$0(int i, Map.Entry entry) {
        return (((Integer) entry.getKey()).intValue() & i) == i;
    }
}
