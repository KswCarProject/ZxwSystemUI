package com.android.systemui.plugins;

import android.app.PendingIntent;
import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.app.smartspace.uitemplatedata.TapAction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.plugins.annotations.ProvidesInterface;
import java.util.List;

@ProvidesInterface(action = "com.android.systemui.action.PLUGIN_BC_SMARTSPACE_DATA", version = 1)
public interface BcSmartspaceDataPlugin extends Plugin {
    public static final String ACTION = "com.android.systemui.action.PLUGIN_BC_SMARTSPACE_DATA";
    public static final String TAG = "BcSmartspaceDataPlugin";
    public static final int VERSION = 1;

    public interface SmartspaceEventNotifier {
        void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent);
    }

    public interface SmartspaceTargetListener {
        void onSmartspaceTargetsUpdated(List<? extends Parcelable> list);
    }

    public interface SmartspaceView {
        int getCurrentCardTopPadding();

        int getSelectedPage();

        void registerDataProvider(BcSmartspaceDataPlugin bcSmartspaceDataPlugin);

        void setDnd(Drawable drawable, String str);

        void setDozeAmount(float f);

        void setFalsingManager(FalsingManager falsingManager);

        void setIntentStarter(IntentStarter intentStarter);

        void setIsDreaming(boolean z);

        void setMediaTarget(SmartspaceTarget smartspaceTarget);

        void setNextAlarm(Drawable drawable, String str);

        void setPrimaryTextColor(int i);
    }

    void addOnAttachStateChangeListener(View.OnAttachStateChangeListener onAttachStateChangeListener) {
    }

    SmartspaceView getView(ViewGroup viewGroup) {
        return null;
    }

    void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent) {
    }

    void onTargetsAvailable(List<SmartspaceTarget> list);

    void registerListener(SmartspaceTargetListener smartspaceTargetListener);

    void registerSmartspaceEventNotifier(SmartspaceEventNotifier smartspaceEventNotifier) {
    }

    void unregisterListener(SmartspaceTargetListener smartspaceTargetListener);

    public interface IntentStarter {
        void startIntent(View view, Intent intent, boolean z);

        void startPendingIntent(PendingIntent pendingIntent, boolean z);

        void startFromAction(SmartspaceAction smartspaceAction, View view, boolean z) {
            try {
                if (smartspaceAction.getIntent() != null) {
                    startIntent(view, smartspaceAction.getIntent(), z);
                } else if (smartspaceAction.getPendingIntent() != null) {
                    startPendingIntent(smartspaceAction.getPendingIntent(), z);
                }
            } catch (ActivityNotFoundException e) {
                Log.w(BcSmartspaceDataPlugin.TAG, "Could not launch intent for action: " + smartspaceAction, e);
            }
        }

        void startFromAction(TapAction tapAction, View view, boolean z) {
            try {
                if (tapAction.getIntent() != null) {
                    startIntent(view, tapAction.getIntent(), z);
                } else if (tapAction.getPendingIntent() != null) {
                    startPendingIntent(tapAction.getPendingIntent(), z);
                }
            } catch (ActivityNotFoundException e) {
                Log.w(BcSmartspaceDataPlugin.TAG, "Could not launch intent for action: " + tapAction, e);
            }
        }
    }
}
