package com.android.wm.shell;

import android.app.ResourcesManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.hardware.display.DisplayManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.SparseArray;
import android.view.Display;
import android.view.SurfaceControl;
import android.window.DisplayAreaAppearedInfo;
import android.window.DisplayAreaInfo;
import android.window.DisplayAreaOrganizer;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class RootTaskDisplayAreaOrganizer extends DisplayAreaOrganizer {
    public static final String TAG = RootTaskDisplayAreaOrganizer.class.getSimpleName();
    public final Context mContext;
    public final SparseArray<DisplayAreaContext> mDisplayAreaContexts = new SparseArray<>();
    public final SparseArray<DisplayAreaInfo> mDisplayAreasInfo = new SparseArray<>();
    public final SparseArray<SurfaceControl> mLeashes = new SparseArray<>();
    public final SparseArray<ArrayList<RootTaskDisplayAreaListener>> mListeners = new SparseArray<>();

    public interface RootTaskDisplayAreaListener {
        void onDisplayAreaAppeared(DisplayAreaInfo displayAreaInfo) {
        }

        void onDisplayAreaInfoChanged(DisplayAreaInfo displayAreaInfo) {
        }

        void onDisplayAreaVanished(DisplayAreaInfo displayAreaInfo) {
        }
    }

    public RootTaskDisplayAreaOrganizer(Executor executor, Context context) {
        super(executor);
        this.mContext = context;
        List registerOrganizer = registerOrganizer(1);
        for (int size = registerOrganizer.size() - 1; size >= 0; size--) {
            onDisplayAreaAppeared(((DisplayAreaAppearedInfo) registerOrganizer.get(size)).getDisplayAreaInfo(), ((DisplayAreaAppearedInfo) registerOrganizer.get(size)).getLeash());
        }
    }

    public void attachToDisplayArea(int i, SurfaceControl.Builder builder) {
        builder.setParent(this.mLeashes.get(i));
    }

    public void onDisplayAreaAppeared(DisplayAreaInfo displayAreaInfo, SurfaceControl surfaceControl) {
        if (displayAreaInfo.featureId == 1) {
            int i = displayAreaInfo.displayId;
            if (this.mDisplayAreasInfo.get(i) == null) {
                this.mDisplayAreasInfo.put(i, displayAreaInfo);
                this.mLeashes.put(i, surfaceControl);
                ArrayList arrayList = this.mListeners.get(i);
                if (arrayList != null) {
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        ((RootTaskDisplayAreaListener) arrayList.get(size)).onDisplayAreaAppeared(displayAreaInfo);
                    }
                }
                applyConfigChangesToContext(displayAreaInfo);
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
            this.mLeashes.get(i).release();
            this.mLeashes.remove(i);
            ArrayList arrayList = this.mListeners.get(i);
            if (arrayList != null) {
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    ((RootTaskDisplayAreaListener) arrayList.get(size)).onDisplayAreaVanished(displayAreaInfo);
                }
            }
            this.mDisplayAreaContexts.remove(i);
            return;
        }
        throw new IllegalArgumentException("onDisplayAreaVanished() Unknown DA displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
    }

    public void onDisplayAreaInfoChanged(DisplayAreaInfo displayAreaInfo) {
        int i = displayAreaInfo.displayId;
        if (this.mDisplayAreasInfo.get(i) != null) {
            this.mDisplayAreasInfo.put(i, displayAreaInfo);
            ArrayList arrayList = this.mListeners.get(i);
            if (arrayList != null) {
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    ((RootTaskDisplayAreaListener) arrayList.get(size)).onDisplayAreaInfoChanged(displayAreaInfo);
                }
            }
            applyConfigChangesToContext(displayAreaInfo);
            return;
        }
        throw new IllegalArgumentException("onDisplayAreaInfoChanged() Unknown DA displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
    }

    public final void applyConfigChangesToContext(DisplayAreaInfo displayAreaInfo) {
        int i = displayAreaInfo.displayId;
        Display display = ((DisplayManager) this.mContext.getSystemService(DisplayManager.class)).getDisplay(i);
        if (display != null) {
            DisplayAreaContext displayAreaContext = this.mDisplayAreaContexts.get(i);
            if (displayAreaContext == null) {
                displayAreaContext = new DisplayAreaContext(this.mContext, display);
                this.mDisplayAreaContexts.put(i, displayAreaContext);
            }
            displayAreaContext.updateConfigurationChanges(displayAreaInfo.configuration);
        } else if (ShellProtoLogCache.WM_SHELL_TASK_ORG_enabled) {
            long j = (long) i;
            ShellProtoLogImpl.w(ShellProtoLogGroup.WM_SHELL_TASK_ORG, -581313329, 1, (String) null, Long.valueOf(j));
        }
    }

    public String toString() {
        return TAG + "#" + this.mDisplayAreasInfo.size();
    }

    public static class DisplayAreaContext extends ContextWrapper {
        public final ResourcesManager mResourcesManager = ResourcesManager.getInstance();
        public final IBinder mToken;

        public DisplayAreaContext(Context context, Display display) {
            super((Context) null);
            Binder binder = new Binder();
            this.mToken = binder;
            attachBaseContext(context.createTokenContext(binder, display));
        }

        public final void updateConfigurationChanges(Configuration configuration) {
            if (getResources().getConfiguration().diff(configuration) != 0) {
                this.mResourcesManager.updateResourcesForActivity(this.mToken, configuration, getDisplayId());
            }
        }
    }
}
