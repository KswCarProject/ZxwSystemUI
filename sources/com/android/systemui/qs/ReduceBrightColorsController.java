package com.android.systemui.qs;

import android.content.Context;
import android.database.ContentObserver;
import android.hardware.display.ColorDisplayManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.provider.Settings;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.Iterator;

public class ReduceBrightColorsController implements CallbackController<Listener> {
    public final ContentObserver mContentObserver;
    public UserTracker.Callback mCurrentUserTrackerCallback;
    public final Handler mHandler;
    public final ArrayList<Listener> mListeners = new ArrayList<>();
    public final ColorDisplayManager mManager;
    public final SecureSettings mSecureSettings;
    public final UserTracker mUserTracker;

    public interface Listener {
        void onActivated(boolean z) {
        }
    }

    public ReduceBrightColorsController(UserTracker userTracker, Handler handler, ColorDisplayManager colorDisplayManager, SecureSettings secureSettings) {
        this.mManager = colorDisplayManager;
        this.mUserTracker = userTracker;
        this.mHandler = handler;
        this.mSecureSettings = secureSettings;
        this.mContentObserver = new ContentObserver(handler) {
            public void onChange(boolean z, Uri uri) {
                String str;
                super.onChange(z, uri);
                if (uri == null) {
                    str = null;
                } else {
                    str = uri.getLastPathSegment();
                }
                synchronized (ReduceBrightColorsController.this.mListeners) {
                    if (str != null) {
                        if (ReduceBrightColorsController.this.mListeners.size() != 0 && str.equals("reduce_bright_colors_activated")) {
                            Iterator it = ReduceBrightColorsController.this.mListeners.iterator();
                            while (it.hasNext()) {
                                ((Listener) it.next()).onActivated(ReduceBrightColorsController.this.mManager.isReduceBrightColorsActivated());
                            }
                        }
                    }
                }
            }
        };
        AnonymousClass2 r4 = new UserTracker.Callback() {
            public void onUserChanged(int i, Context context) {
                synchronized (ReduceBrightColorsController.this.mListeners) {
                    if (ReduceBrightColorsController.this.mListeners.size() > 0) {
                        ReduceBrightColorsController.this.mSecureSettings.unregisterContentObserver(ReduceBrightColorsController.this.mContentObserver);
                        ReduceBrightColorsController.this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("reduce_bright_colors_activated"), false, ReduceBrightColorsController.this.mContentObserver, i);
                    }
                }
            }
        };
        this.mCurrentUserTrackerCallback = r4;
        userTracker.addCallback(r4, new HandlerExecutor(handler));
    }

    public void addCallback(Listener listener) {
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(listener)) {
                this.mListeners.add(listener);
                if (this.mListeners.size() == 1) {
                    this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("reduce_bright_colors_activated"), false, this.mContentObserver, this.mUserTracker.getUserId());
                }
            }
        }
    }

    public void removeCallback(Listener listener) {
        synchronized (this.mListeners) {
            if (this.mListeners.remove(listener) && this.mListeners.size() == 0) {
                this.mSecureSettings.unregisterContentObserver(this.mContentObserver);
            }
        }
    }

    public boolean isReduceBrightColorsActivated() {
        return this.mManager.isReduceBrightColorsActivated();
    }

    public void setReduceBrightColorsActivated(boolean z) {
        this.mManager.setReduceBrightColorsActivated(z);
    }
}
