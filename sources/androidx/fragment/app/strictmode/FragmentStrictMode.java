package androidx.fragment.app.strictmode;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressLint({"SyntheticAccessor"})
public final class FragmentStrictMode {
    public static Policy defaultPolicy = Policy.LAX;

    public enum Flag {
        PENALTY_LOG,
        PENALTY_DEATH,
        DETECT_FRAGMENT_REUSE,
        DETECT_FRAGMENT_TAG_USAGE,
        DETECT_RETAIN_INSTANCE_USAGE,
        DETECT_SET_USER_VISIBLE_HINT,
        DETECT_TARGET_FRAGMENT_USAGE,
        DETECT_WRONG_FRAGMENT_CONTAINER
    }

    public interface OnViolationListener {
        void onViolation(Violation violation);
    }

    public static final class Policy {
        public static final Policy LAX = new Policy(new HashSet(), (OnViolationListener) null, new HashMap());
        public final Map<Class<? extends Fragment>, Set<Class<? extends Violation>>> mAllowedViolations;
        public final Set<Flag> mFlags;
        public final OnViolationListener mListener;

        public Policy(Set<Flag> set, OnViolationListener onViolationListener, Map<Class<? extends Fragment>, Set<Class<? extends Violation>>> map) {
            this.mFlags = new HashSet(set);
            this.mListener = onViolationListener;
            HashMap hashMap = new HashMap();
            for (Map.Entry next : map.entrySet()) {
                hashMap.put((Class) next.getKey(), new HashSet((Collection) next.getValue()));
            }
            this.mAllowedViolations = hashMap;
        }
    }

    public static Policy getNearestPolicy(Fragment fragment) {
        while (fragment != null) {
            if (fragment.isAdded()) {
                FragmentManager parentFragmentManager = fragment.getParentFragmentManager();
                if (parentFragmentManager.getStrictModePolicy() != null) {
                    return parentFragmentManager.getStrictModePolicy();
                }
            }
            fragment = fragment.getParentFragment();
        }
        return defaultPolicy;
    }

    public static void onFragmentReuse(Fragment fragment, String str) {
        FragmentReuseViolation fragmentReuseViolation = new FragmentReuseViolation(fragment, str);
        logIfDebuggingEnabled(fragmentReuseViolation);
        Policy nearestPolicy = getNearestPolicy(fragment);
        if (nearestPolicy.mFlags.contains(Flag.DETECT_FRAGMENT_REUSE) && shouldHandlePolicyViolation(nearestPolicy, fragment.getClass(), FragmentReuseViolation.class)) {
            handlePolicyViolation(nearestPolicy, fragmentReuseViolation);
        }
    }

    public static void onFragmentTagUsage(Fragment fragment, ViewGroup viewGroup) {
        FragmentTagUsageViolation fragmentTagUsageViolation = new FragmentTagUsageViolation(fragment, viewGroup);
        logIfDebuggingEnabled(fragmentTagUsageViolation);
        Policy nearestPolicy = getNearestPolicy(fragment);
        if (nearestPolicy.mFlags.contains(Flag.DETECT_FRAGMENT_TAG_USAGE) && shouldHandlePolicyViolation(nearestPolicy, fragment.getClass(), FragmentTagUsageViolation.class)) {
            handlePolicyViolation(nearestPolicy, fragmentTagUsageViolation);
        }
    }

    public static void onGetTargetFragmentUsage(Fragment fragment) {
        GetTargetFragmentUsageViolation getTargetFragmentUsageViolation = new GetTargetFragmentUsageViolation(fragment);
        logIfDebuggingEnabled(getTargetFragmentUsageViolation);
        Policy nearestPolicy = getNearestPolicy(fragment);
        if (nearestPolicy.mFlags.contains(Flag.DETECT_TARGET_FRAGMENT_USAGE) && shouldHandlePolicyViolation(nearestPolicy, fragment.getClass(), GetTargetFragmentUsageViolation.class)) {
            handlePolicyViolation(nearestPolicy, getTargetFragmentUsageViolation);
        }
    }

    public static void onWrongFragmentContainer(Fragment fragment, ViewGroup viewGroup) {
        WrongFragmentContainerViolation wrongFragmentContainerViolation = new WrongFragmentContainerViolation(fragment, viewGroup);
        logIfDebuggingEnabled(wrongFragmentContainerViolation);
        Policy nearestPolicy = getNearestPolicy(fragment);
        if (nearestPolicy.mFlags.contains(Flag.DETECT_WRONG_FRAGMENT_CONTAINER) && shouldHandlePolicyViolation(nearestPolicy, fragment.getClass(), WrongFragmentContainerViolation.class)) {
            handlePolicyViolation(nearestPolicy, wrongFragmentContainerViolation);
        }
    }

    public static void onPolicyViolation(Violation violation) {
        logIfDebuggingEnabled(violation);
        Fragment fragment = violation.getFragment();
        Policy nearestPolicy = getNearestPolicy(fragment);
        if (shouldHandlePolicyViolation(nearestPolicy, fragment.getClass(), violation.getClass())) {
            handlePolicyViolation(nearestPolicy, violation);
        }
    }

    public static void logIfDebuggingEnabled(Violation violation) {
        if (FragmentManager.isLoggingEnabled(3)) {
            Log.d("FragmentManager", "StrictMode violation in " + violation.getFragment().getClass().getName(), violation);
        }
    }

    public static boolean shouldHandlePolicyViolation(Policy policy, Class<? extends Fragment> cls, Class<? extends Violation> cls2) {
        Set set = (Set) policy.mAllowedViolations.get(cls);
        if (set == null) {
            return true;
        }
        if (cls2.getSuperclass() == Violation.class || !set.contains(cls2.getSuperclass())) {
            return !set.contains(cls2);
        }
        return false;
    }

    public static void handlePolicyViolation(final Policy policy, final Violation violation) {
        Fragment fragment = violation.getFragment();
        final String name = fragment.getClass().getName();
        if (policy.mFlags.contains(Flag.PENALTY_LOG)) {
            Log.d("FragmentStrictMode", "Policy violation in " + name, violation);
        }
        if (policy.mListener != null) {
            runOnHostThread(fragment, new Runnable() {
                public void run() {
                    Policy.this.mListener.onViolation(violation);
                }
            });
        }
        if (policy.mFlags.contains(Flag.PENALTY_DEATH)) {
            runOnHostThread(fragment, new Runnable() {
                public void run() {
                    Log.e("FragmentStrictMode", "Policy violation with PENALTY_DEATH in " + name, violation);
                    throw violation;
                }
            });
        }
    }

    public static void runOnHostThread(Fragment fragment, Runnable runnable) {
        if (fragment.isAdded()) {
            Handler handler = fragment.getParentFragmentManager().getHost().getHandler();
            if (handler.getLooper() == Looper.myLooper()) {
                runnable.run();
            } else {
                handler.post(runnable);
            }
        } else {
            runnable.run();
        }
    }
}
