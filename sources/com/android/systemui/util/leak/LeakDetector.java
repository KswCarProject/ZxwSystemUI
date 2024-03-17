package com.android.systemui.util.leak;

import android.os.Build;
import android.util.IndentingPrintWriter;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import java.io.PrintWriter;
import java.util.Collection;

public class LeakDetector implements Dumpable {
    public static final boolean ENABLED = Build.IS_DEBUGGABLE;
    public final TrackedCollections mTrackedCollections;
    public final TrackedGarbage mTrackedGarbage;
    public final TrackedObjects mTrackedObjects;

    @VisibleForTesting
    public LeakDetector(TrackedCollections trackedCollections, TrackedGarbage trackedGarbage, TrackedObjects trackedObjects, DumpManager dumpManager) {
        this.mTrackedCollections = trackedCollections;
        this.mTrackedGarbage = trackedGarbage;
        this.mTrackedObjects = trackedObjects;
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
    }

    public <T> void trackInstance(T t) {
        TrackedObjects trackedObjects = this.mTrackedObjects;
        if (trackedObjects != null) {
            trackedObjects.track(t);
        }
    }

    public <T> void trackCollection(Collection<T> collection, String str) {
        TrackedCollections trackedCollections = this.mTrackedCollections;
        if (trackedCollections != null) {
            trackedCollections.track(collection, str);
        }
    }

    public void trackGarbage(Object obj) {
        TrackedGarbage trackedGarbage = this.mTrackedGarbage;
        if (trackedGarbage != null) {
            trackedGarbage.track(obj);
        }
    }

    public TrackedGarbage getTrackedGarbage() {
        return this.mTrackedGarbage;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("SYSUI LEAK DETECTOR");
        indentingPrintWriter.increaseIndent();
        if (this.mTrackedCollections == null || this.mTrackedGarbage == null) {
            indentingPrintWriter.println("disabled");
        } else {
            indentingPrintWriter.println("TrackedCollections:");
            indentingPrintWriter.increaseIndent();
            this.mTrackedCollections.dump(indentingPrintWriter, new LeakDetector$$ExternalSyntheticLambda0());
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.println("TrackedObjects:");
            indentingPrintWriter.increaseIndent();
            this.mTrackedCollections.dump(indentingPrintWriter, new LeakDetector$$ExternalSyntheticLambda1());
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.print("TrackedGarbage:");
            indentingPrintWriter.increaseIndent();
            this.mTrackedGarbage.dump(indentingPrintWriter);
            indentingPrintWriter.decreaseIndent();
        }
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println();
    }

    public static /* synthetic */ boolean lambda$dump$0(Collection collection) {
        return !TrackedObjects.isTrackedObject(collection);
    }
}
