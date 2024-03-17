package androidx.fragment.app.strictmode;

import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

public final class WrongFragmentContainerViolation extends Violation {
    private final ViewGroup mContainer;

    public WrongFragmentContainerViolation(Fragment fragment, ViewGroup viewGroup) {
        super(fragment);
        this.mContainer = viewGroup;
    }

    public String getMessage() {
        return "Attempting to add fragment " + this.mFragment + " to container " + this.mContainer + " which is not a FragmentContainerView";
    }
}
