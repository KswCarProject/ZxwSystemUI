package com.android.settingslib.deviceinfo;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateUtils;
import androidx.preference.Preference;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.lang.ref.WeakReference;

public abstract class AbstractUptimePreferenceController extends AbstractPreferenceController implements LifecycleObserver, OnStart, OnStop {
    public static final String KEY_UPTIME = "up_time";
    public Handler mHandler;
    public Preference mUptime;

    public void onStart() {
        getHandler().sendEmptyMessage(500);
    }

    public void onStop() {
        getHandler().removeMessages(500);
    }

    public final Handler getHandler() {
        if (this.mHandler == null) {
            this.mHandler = new MyHandler(this);
        }
        return this.mHandler;
    }

    public final void updateTimes() {
        this.mUptime.setSummary((CharSequence) DateUtils.formatElapsedTime(SystemClock.elapsedRealtime() / 1000));
    }

    public static class MyHandler extends Handler {
        public WeakReference<AbstractUptimePreferenceController> mStatus;

        public MyHandler(AbstractUptimePreferenceController abstractUptimePreferenceController) {
            this.mStatus = new WeakReference<>(abstractUptimePreferenceController);
        }

        public void handleMessage(Message message) {
            AbstractUptimePreferenceController abstractUptimePreferenceController = (AbstractUptimePreferenceController) this.mStatus.get();
            if (abstractUptimePreferenceController != null) {
                if (message.what == 500) {
                    abstractUptimePreferenceController.updateTimes();
                    sendEmptyMessageDelayed(500, 1000);
                    return;
                }
                throw new IllegalStateException("Unknown message " + message.what);
            }
        }
    }
}
