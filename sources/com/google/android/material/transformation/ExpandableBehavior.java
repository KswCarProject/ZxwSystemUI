package com.google.android.material.transformation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.expandable.ExpandableWidget;
import java.util.List;

@Deprecated
public abstract class ExpandableBehavior extends CoordinatorLayout.Behavior<View> {
    public int currentState = 0;

    public abstract boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2);

    public abstract boolean onExpandedStateChange(View view, View view2, boolean z, boolean z2);

    public ExpandableBehavior() {
    }

    public ExpandableBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r3 = findExpandableWidget(r3, r4);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onLayoutChild(androidx.coordinatorlayout.widget.CoordinatorLayout r3, final android.view.View r4, int r5) {
        /*
            r2 = this;
            boolean r5 = androidx.core.view.ViewCompat.isLaidOut(r4)
            if (r5 != 0) goto L_0x002d
            com.google.android.material.expandable.ExpandableWidget r3 = r2.findExpandableWidget(r3, r4)
            if (r3 == 0) goto L_0x002d
            boolean r5 = r3.isExpanded()
            boolean r5 = r2.didStateChange(r5)
            if (r5 == 0) goto L_0x002d
            boolean r5 = r3.isExpanded()
            if (r5 == 0) goto L_0x001e
            r5 = 1
            goto L_0x001f
        L_0x001e:
            r5 = 2
        L_0x001f:
            r2.currentState = r5
            android.view.ViewTreeObserver r0 = r4.getViewTreeObserver()
            com.google.android.material.transformation.ExpandableBehavior$1 r1 = new com.google.android.material.transformation.ExpandableBehavior$1
            r1.<init>(r4, r5, r3)
            r0.addOnPreDrawListener(r1)
        L_0x002d:
            r2 = 0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.transformation.ExpandableBehavior.onLayoutChild(androidx.coordinatorlayout.widget.CoordinatorLayout, android.view.View, int):boolean");
    }

    public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
        ExpandableWidget expandableWidget = (ExpandableWidget) view2;
        if (!didStateChange(expandableWidget.isExpanded())) {
            return false;
        }
        this.currentState = expandableWidget.isExpanded() ? 1 : 2;
        return onExpandedStateChange((View) expandableWidget, view, expandableWidget.isExpanded(), true);
    }

    public ExpandableWidget findExpandableWidget(CoordinatorLayout coordinatorLayout, View view) {
        List<View> dependencies = coordinatorLayout.getDependencies(view);
        int size = dependencies.size();
        for (int i = 0; i < size; i++) {
            View view2 = dependencies.get(i);
            if (layoutDependsOn(coordinatorLayout, view, view2)) {
                return (ExpandableWidget) view2;
            }
        }
        return null;
    }

    public final boolean didStateChange(boolean z) {
        if (z) {
            int i = this.currentState;
            return i == 0 || i == 2;
        } else if (this.currentState == 1) {
            return true;
        } else {
            return false;
        }
    }
}
