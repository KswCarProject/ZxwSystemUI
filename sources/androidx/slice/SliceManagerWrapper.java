package androidx.slice;

import android.app.slice.SliceManager;
import android.content.Context;
import android.net.Uri;
import java.util.List;
import java.util.Set;

public class SliceManagerWrapper extends SliceManager {
    public final SliceManager mManager;

    public SliceManagerWrapper(Context context) {
        this((SliceManager) context.getSystemService(SliceManager.class));
    }

    public SliceManagerWrapper(SliceManager sliceManager) {
        this.mManager = sliceManager;
    }

    public Set<SliceSpec> getPinnedSpecs(Uri uri) {
        return SliceConvert.wrap(this.mManager.getPinnedSpecs(uri));
    }

    public List<Uri> getPinnedSlices() {
        return this.mManager.getPinnedSlices();
    }
}
