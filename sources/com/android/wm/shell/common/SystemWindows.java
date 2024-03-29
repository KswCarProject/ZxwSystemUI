package com.android.wm.shell.common;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Region;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.MergedConfiguration;
import android.util.Slog;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.IScrollCaptureResponseListener;
import android.view.IWindow;
import android.view.IWindowManager;
import android.view.IWindowSession;
import android.view.IWindowSessionCallback;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.ScrollCaptureResponse;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import android.window.ClientWindowFrames;
import com.android.internal.os.IResultReceiver;
import com.android.wm.shell.common.DisplayController;
import java.util.HashMap;

public class SystemWindows {
    public final DisplayController mDisplayController;
    public final DisplayController.OnDisplaysChangedListener mDisplayListener;
    public final SparseArray<PerDisplay> mPerDisplay = new SparseArray<>();
    public IWindowSession mSession;
    public final HashMap<View, SurfaceControlViewHost> mViewRoots = new HashMap<>();
    public final IWindowManager mWmService;

    public SystemWindows(DisplayController displayController, IWindowManager iWindowManager) {
        AnonymousClass1 r0 = new DisplayController.OnDisplaysChangedListener() {
            public void onDisplayAdded(int i) {
            }

            public void onDisplayRemoved(int i) {
            }

            public void onDisplayConfigurationChanged(int i, Configuration configuration) {
                PerDisplay perDisplay = (PerDisplay) SystemWindows.this.mPerDisplay.get(i);
                if (perDisplay != null) {
                    perDisplay.updateConfiguration(configuration);
                }
            }
        };
        this.mDisplayListener = r0;
        this.mWmService = iWindowManager;
        this.mDisplayController = displayController;
        displayController.addDisplayWindowListener(r0);
        try {
            this.mSession = iWindowManager.openSession(new IWindowSessionCallback.Stub() {
                public void onAnimatorScaleChanged(float f) {
                }
            });
        } catch (RemoteException e) {
            Slog.e("SystemWindows", "Unable to create layer", e);
        }
    }

    public void addView(View view, WindowManager.LayoutParams layoutParams, int i, int i2) {
        PerDisplay perDisplay = this.mPerDisplay.get(i);
        if (perDisplay == null) {
            perDisplay = new PerDisplay(i);
            this.mPerDisplay.put(i, perDisplay);
        }
        perDisplay.addView(view, layoutParams, i2);
    }

    public void removeView(View view) {
        this.mViewRoots.remove(view).release();
    }

    public void updateViewLayout(View view, ViewGroup.LayoutParams layoutParams) {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewRoots.get(view);
        if (surfaceControlViewHost != null && (layoutParams instanceof WindowManager.LayoutParams)) {
            view.setLayoutParams(layoutParams);
            surfaceControlViewHost.relayout((WindowManager.LayoutParams) layoutParams);
        }
    }

    public void setShellRootAccessibilityWindow(int i, int i2, View view) {
        PerDisplay perDisplay = this.mPerDisplay.get(i);
        if (perDisplay != null) {
            perDisplay.setShellRootAccessibilityWindow(i2, view);
        }
    }

    public void setTouchableRegion(View view, Region region) {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewRoots.get(view);
        if (surfaceControlViewHost != null) {
            SysUiWindowManager windowlessWM = surfaceControlViewHost.getWindowlessWM();
            if (windowlessWM instanceof SysUiWindowManager) {
                windowlessWM.setTouchableRegionForWindow(view, region);
            }
        }
    }

    public SurfaceControl getViewSurface(View view) {
        for (int i = 0; i < this.mPerDisplay.size(); i++) {
            for (int i2 = 0; i2 < this.mPerDisplay.valueAt(i).mWwms.size(); i2++) {
                SurfaceControl surfaceControlForWindow = ((SysUiWindowManager) this.mPerDisplay.valueAt(i).mWwms.valueAt(i2)).getSurfaceControlForWindow(view);
                if (surfaceControlForWindow != null) {
                    return surfaceControlForWindow;
                }
            }
        }
        return null;
    }

    public IBinder getFocusGrantToken(View view) {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewRoots.get(view);
        if (surfaceControlViewHost != null) {
            return surfaceControlViewHost.getFocusGrantToken();
        }
        Slog.e("SystemWindows", "Couldn't get focus grant token since view does not exist in SystemWindow:" + view);
        return null;
    }

    public class PerDisplay {
        public final int mDisplayId;
        public final SparseArray<SysUiWindowManager> mWwms = new SparseArray<>();

        public PerDisplay(int i) {
            this.mDisplayId = i;
        }

        public void addView(View view, WindowManager.LayoutParams layoutParams, int i) {
            SysUiWindowManager addRoot = addRoot(i);
            if (addRoot == null) {
                Slog.e("SystemWindows", "Unable to create systemui root");
                return;
            }
            SurfaceControlViewHost surfaceControlViewHost = new SurfaceControlViewHost(view.getContext(), SystemWindows.this.mDisplayController.getDisplay(this.mDisplayId), addRoot, true);
            layoutParams.flags |= 16777216;
            surfaceControlViewHost.setView(view, layoutParams);
            SystemWindows.this.mViewRoots.put(view, surfaceControlViewHost);
            setShellRootAccessibilityWindow(i, view);
        }

        public SysUiWindowManager addRoot(int i) {
            SurfaceControl surfaceControl;
            SysUiWindowManager sysUiWindowManager = this.mWwms.get(i);
            if (sysUiWindowManager != null) {
                return sysUiWindowManager;
            }
            ContainerWindow containerWindow = new ContainerWindow();
            try {
                surfaceControl = SystemWindows.this.mWmService.addShellRoot(this.mDisplayId, containerWindow, i);
            } catch (RemoteException unused) {
                surfaceControl = null;
            }
            if (surfaceControl == null) {
                Slog.e("SystemWindows", "Unable to get root surfacecontrol for systemui");
                return null;
            }
            SysUiWindowManager sysUiWindowManager2 = new SysUiWindowManager(this.mDisplayId, SystemWindows.this.mDisplayController.getDisplayContext(this.mDisplayId), surfaceControl, containerWindow);
            this.mWwms.put(i, sysUiWindowManager2);
            return sysUiWindowManager2;
        }

        public void setShellRootAccessibilityWindow(int i, View view) {
            if (this.mWwms.get(i) != null) {
                try {
                    SystemWindows.this.mWmService.setShellRootAccessibilityWindow(this.mDisplayId, i, view != null ? ((SurfaceControlViewHost) SystemWindows.this.mViewRoots.get(view)).getWindowToken() : null);
                } catch (RemoteException e) {
                    Slog.e("SystemWindows", "Error setting accessibility window for " + this.mDisplayId + ":" + i, e);
                }
            }
        }

        public void updateConfiguration(Configuration configuration) {
            for (int i = 0; i < this.mWwms.size(); i++) {
                this.mWwms.valueAt(i).updateConfiguration(configuration);
            }
        }
    }

    public class SysUiWindowManager extends WindowlessWindowManager {
        public ContainerWindow mContainerWindow;
        public final int mDisplayId;
        public final HashMap<IBinder, SurfaceControl> mLeashForWindow = new HashMap<>();

        public SysUiWindowManager(int i, Context context, SurfaceControl surfaceControl, ContainerWindow containerWindow) {
            super(context.getResources().getConfiguration(), surfaceControl, (IBinder) null);
            this.mContainerWindow = containerWindow;
            this.mDisplayId = i;
        }

        public void updateConfiguration(Configuration configuration) {
            setConfiguration(configuration);
        }

        public SurfaceControl getSurfaceControlForWindow(View view) {
            SurfaceControl surfaceControl;
            synchronized (this) {
                surfaceControl = this.mLeashForWindow.get(getWindowBinder(view));
            }
            return surfaceControl;
        }

        public void attachToParentSurface(IWindow iWindow, SurfaceControl.Builder builder) {
            SurfaceControl build = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName("SystemWindowLeash").setHidden(false).setParent(this.mRootSurface).setCallsite("SysUiWIndowManager#attachToParentSurface").build();
            synchronized (this) {
                this.mLeashForWindow.put(iWindow.asBinder(), build);
            }
            builder.setParent(build);
        }

        public void remove(IWindow iWindow) throws RemoteException {
            SystemWindows.super.remove(iWindow);
            synchronized (this) {
                IBinder asBinder = iWindow.asBinder();
                new SurfaceControl.Transaction().remove(this.mLeashForWindow.get(asBinder)).apply();
                this.mLeashForWindow.remove(asBinder);
            }
        }

        public void setTouchableRegionForWindow(View view, Region region) {
            IBinder windowToken = view.getWindowToken();
            if (windowToken != null) {
                setTouchRegion(windowToken, region);
            }
        }
    }

    public static class ContainerWindow extends IWindow.Stub {
        public void closeSystemDialogs(String str) {
        }

        public void dispatchAppVisibility(boolean z) {
        }

        public void dispatchDragEvent(DragEvent dragEvent) {
        }

        public void dispatchGetNewSurface() {
        }

        public void dispatchWallpaperCommand(String str, int i, int i2, int i3, Bundle bundle, boolean z) {
        }

        public void dispatchWallpaperOffsets(float f, float f2, float f3, float f4, float f5, boolean z) {
        }

        public void dispatchWindowShown() {
        }

        public void executeCommand(String str, String str2, ParcelFileDescriptor parcelFileDescriptor) {
        }

        public void hideInsets(int i, boolean z) {
        }

        public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
        }

        public void moved(int i, int i2) {
        }

        public void requestAppKeyboardShortcuts(IResultReceiver iResultReceiver, int i) {
        }

        public void resized(ClientWindowFrames clientWindowFrames, boolean z, MergedConfiguration mergedConfiguration, InsetsState insetsState, boolean z2, boolean z3, int i, int i2, int i3) {
        }

        public void showInsets(int i, boolean z) {
        }

        public void updatePointerIcon(float f, float f2) {
        }

        public void requestScrollCapture(IScrollCaptureResponseListener iScrollCaptureResponseListener) {
            try {
                iScrollCaptureResponseListener.onScrollCaptureResponse(new ScrollCaptureResponse.Builder().setDescription("Not Implemented").build());
            } catch (RemoteException unused) {
            }
        }
    }
}
