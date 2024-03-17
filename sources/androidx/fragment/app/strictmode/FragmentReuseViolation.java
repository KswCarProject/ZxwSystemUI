package androidx.fragment.app.strictmode;

import androidx.fragment.app.Fragment;

public final class FragmentReuseViolation extends Violation {
    private final String mPreviousWho;

    public FragmentReuseViolation(Fragment fragment, String str) {
        super(fragment);
        this.mPreviousWho = str;
    }

    public String getMessage() {
        return "Attempting to reuse fragment " + this.mFragment + " with previous ID " + this.mPreviousWho;
    }
}
