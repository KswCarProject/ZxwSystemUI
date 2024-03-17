package androidx.fragment.app.strictmode;

import androidx.fragment.app.Fragment;

public final class GetTargetFragmentUsageViolation extends TargetFragmentUsageViolation {
    public GetTargetFragmentUsageViolation(Fragment fragment) {
        super(fragment);
    }

    public String getMessage() {
        return "Attempting to get target fragment from fragment " + this.mFragment;
    }
}
