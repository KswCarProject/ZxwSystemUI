package com.android.wm.shell.transition;

import android.graphics.Rect;
import android.util.ArrayMap;
import android.util.RotationUtils;
import android.view.SurfaceControl;
import android.window.TransitionInfo;
import android.window.WindowContainerToken;
import com.android.wm.shell.util.CounterRotator;
import java.util.List;

public class CounterRotatorHelper {
    public final Rect mLastDisplayBounds = new Rect();
    public int mLastRotationDelta;
    public final ArrayMap<WindowContainerToken, CounterRotator> mRotatorMap = new ArrayMap<>();

    public void handleClosingChanges(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, TransitionInfo.Change change) {
        TransitionInfo transitionInfo2 = transitionInfo;
        SurfaceControl.Transaction transaction2 = transaction;
        int deltaRotation = RotationUtils.deltaRotation(change.getStartRotation(), change.getEndRotation());
        Rect endAbsBounds = change.getEndAbsBounds();
        int width = endAbsBounds.width();
        int height = endAbsBounds.height();
        this.mLastRotationDelta = deltaRotation;
        this.mLastDisplayBounds.set(endAbsBounds);
        List changes = transitionInfo.getChanges();
        int size = changes.size();
        int i = size - 1;
        while (i >= 0) {
            TransitionInfo.Change change2 = (TransitionInfo.Change) changes.get(i);
            WindowContainerToken parent = change2.getParent();
            if (Transitions.isClosingType(change2.getMode()) && TransitionInfo.isIndependent(change2, transitionInfo2) && parent != null) {
                CounterRotator counterRotator = this.mRotatorMap.get(parent);
                if (counterRotator == null) {
                    CounterRotator counterRotator2 = new CounterRotator();
                    CounterRotator counterRotator3 = counterRotator2;
                    WindowContainerToken windowContainerToken = parent;
                    counterRotator2.setup(transaction, transitionInfo2.getChange(parent).getLeash(), deltaRotation, (float) width, (float) height);
                    SurfaceControl surface = counterRotator3.getSurface();
                    if (surface != null) {
                        transaction2.setLayer(surface, (change2.getFlags() & 2) == 0 ? size - i : -1);
                    }
                    CounterRotator counterRotator4 = counterRotator3;
                    this.mRotatorMap.put(windowContainerToken, counterRotator4);
                    counterRotator = counterRotator4;
                }
                counterRotator.addChild(transaction2, change2.getLeash());
            }
            i--;
            transitionInfo2 = transitionInfo;
        }
    }

    public Rect getEndBoundsInStartRotation(TransitionInfo.Change change) {
        if (this.mLastRotationDelta == 0) {
            return change.getEndAbsBounds();
        }
        Rect rect = new Rect(change.getEndAbsBounds());
        RotationUtils.rotateBounds(rect, this.mLastDisplayBounds, this.mLastRotationDelta);
        return rect;
    }

    public void cleanUp(SurfaceControl.Transaction transaction) {
        for (int size = this.mRotatorMap.size() - 1; size >= 0; size--) {
            this.mRotatorMap.valueAt(size).cleanUp(transaction);
        }
        this.mRotatorMap.clear();
        this.mLastRotationDelta = 0;
    }
}
