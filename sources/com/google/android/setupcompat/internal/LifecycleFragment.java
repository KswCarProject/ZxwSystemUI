package com.google.android.setupcompat.internal;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.PersistableBundle;
import android.util.Log;
import com.google.android.setupcompat.logging.CustomEvent;
import com.google.android.setupcompat.logging.MetricKey;
import com.google.android.setupcompat.logging.SetupMetricsLogger;
import com.google.android.setupcompat.util.WizardManagerHelper;
import java.util.concurrent.TimeUnit;

public class LifecycleFragment extends Fragment {
    public static final String LOG_TAG = LifecycleFragment.class.getSimpleName();
    public long durationInNanos = 0;
    public MetricKey metricKey;
    public long startInNanos;

    public LifecycleFragment() {
        setRetainInstance(true);
    }

    public static LifecycleFragment attachNow(Activity activity) {
        if (WizardManagerHelper.isAnySetupWizard(activity.getIntent())) {
            SetupCompatServiceInvoker.get(activity.getApplicationContext()).bindBack(LayoutBindBackHelper.getScreenName(activity), LayoutBindBackHelper.getExtraBundle(activity));
            FragmentManager fragmentManager = activity.getFragmentManager();
            if (fragmentManager != null && !fragmentManager.isDestroyed()) {
                Fragment findFragmentByTag = fragmentManager.findFragmentByTag("lifecycle_monitor");
                if (findFragmentByTag == null) {
                    LifecycleFragment lifecycleFragment = new LifecycleFragment();
                    try {
                        fragmentManager.beginTransaction().add(lifecycleFragment, "lifecycle_monitor").commitNow();
                        findFragmentByTag = lifecycleFragment;
                    } catch (IllegalStateException e) {
                        String str = LOG_TAG;
                        Log.e(str, "Error occurred when attach to Activity:" + activity.getComponentName(), e);
                    }
                } else if (!(findFragmentByTag instanceof LifecycleFragment)) {
                    String str2 = LOG_TAG;
                    Log.wtf(str2, activity.getClass().getSimpleName() + " Incorrect instance on lifecycle fragment.");
                    return null;
                }
                return (LifecycleFragment) findFragmentByTag;
            }
        }
        return null;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.metricKey = MetricKey.get("ScreenDuration", getActivity());
    }

    public void onDetach() {
        super.onDetach();
        SetupMetricsLogger.logDuration(getActivity(), this.metricKey, TimeUnit.NANOSECONDS.toMillis(this.durationInNanos));
    }

    public void onResume() {
        super.onResume();
        this.startInNanos = ClockProvider.timeInNanos();
        logScreenResume();
    }

    public void onPause() {
        super.onPause();
        this.durationInNanos += ClockProvider.timeInNanos() - this.startInNanos;
    }

    public final void logScreenResume() {
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putLong("onScreenResume", System.nanoTime());
        SetupMetricsLogger.logCustomEvent(getActivity(), CustomEvent.create(MetricKey.get("ScreenActivity", getActivity()), persistableBundle));
    }
}
