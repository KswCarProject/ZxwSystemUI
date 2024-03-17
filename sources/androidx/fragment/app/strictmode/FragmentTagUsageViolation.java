package androidx.fragment.app.strictmode;

import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public final class FragmentTagUsageViolation extends Violation {
    private final ViewGroup mContainer;

    public FragmentTagUsageViolation(Fragment fragment, ViewGroup viewGroup) {
        super(fragment);
        this.mContainer = viewGroup;
    }

    public String getMessage() {
        return "Attempting to use <fragment> tag to add fragment " + this.mFragment + " to container " + this.mContainer;
    }
}
