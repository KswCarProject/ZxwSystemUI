package com.android.wm.shell;

import android.util.SparseArray;
import android.view.SurfaceControl;
import android.window.DisplayAreaAppearedInfo;
import android.window.DisplayAreaInfo;
import android.window.DisplayAreaOrganizer;
import java.util.List;
import java.util.concurrent.Executor;

public class RootDisplayAreaOrganizer extends DisplayAreaOrganizer {
    public static final String TAG = RootDisplayAreaOrganizer.class.getSimpleName();
    public final SparseArray<DisplayAreaInfo> mDisplayAreasInfo = new SparseArray<>();
    public final SparseArray<SurfaceControl> mLeashes = new SparseArray<>();

    public RootDisplayAreaOrganizer(Executor executor) {
        super(executor);
        List registerOrganizer = registerOrganizer(0);
        for (int size = registerOrganizer.size() - 1; size >= 0; size--) {
            onDisplayAreaAppeared(((DisplayAreaAppearedInfo) registerOrganizer.get(size)).getDisplayAreaInfo(), ((DisplayAreaAppearedInfo) registerOrganizer.get(size)).getLeash());
        }
    }

    public void attachToDisplayArea(int i, SurfaceControl.Builder builder) {
        SurfaceControl surfaceControl = this.mLeashes.get(i);
        if (surfaceControl != null) {
            builder.setParent(surfaceControl);
        }
    }

    public void onDisplayAreaAppeared(DisplayAreaInfo displayAreaInfo, SurfaceControl surfaceControl) {
        if (displayAreaInfo.featureId == 0) {
            int i = displayAreaInfo.displayId;
            if (this.mDisplayAreasInfo.get(i) == null) {
                this.mDisplayAreasInfo.put(i, displayAreaInfo);
                this.mLeashes.put(i, surfaceControl);
                return;
            }
            throw new IllegalArgumentException("Duplicate DA for displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
        }
        throw new IllegalArgumentException("Unknown feature: " + displayAreaInfo.featureId + "displayAreaInfo:" + displayAreaInfo);
    }

    public void onDisplayAreaVanished(DisplayAreaInfo displayAreaInfo) {
        int i = displayAreaInfo.displayId;
        if (this.mDisplayAreasInfo.get(i) != null) {
            this.mDisplayAreasInfo.remove(i);
            return;
        }
        throw new IllegalArgumentException("onDisplayAreaVanished() Unknown DA displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
    }

    public void onDisplayAreaInfoChanged(DisplayAreaInfo displayAreaInfo) {
        int i = displayAreaInfo.displayId;
        if (this.mDisplayAreasInfo.get(i) != null) {
            this.mDisplayAreasInfo.put(i, displayAreaInfo);
            return;
        }
        throw new IllegalArgumentException("onDisplayAreaInfoChanged() Unknown DA displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
    }

    public String toString() {
        return TAG + "#" + this.mDisplayAreasInfo.size();
    }
}
