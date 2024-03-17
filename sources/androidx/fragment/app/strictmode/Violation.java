package androidx.fragment.app.strictmode;

import androidx.fragment.app.Fragment;

public abstract class Violation extends RuntimeException {
    public final Fragment mFragment;

    public Violation(Fragment fragment) {
        this.mFragment = fragment;
    }

    public Fragment getFragment() {
        return this.mFragment;
    }
}
