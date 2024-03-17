package com.android.systemui.statusbar.phone;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.widget.ImageView;
import androidx.appcompat.R$styleable;
import com.android.systemui.R$color;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DarkIconDispatcherImpl implements SysuiDarkIconDispatcher, LightBarTransitionsController.DarkIntensityApplier {
    public float mDarkIntensity;
    public int mDarkModeIconColorSingleTone;
    public int mIconTint = -1;
    public int mLightModeIconColorSingleTone;
    public final ArrayMap<Object, DarkIconDispatcher.DarkReceiver> mReceivers = new ArrayMap<>();
    public final ArrayList<Rect> mTintAreas = new ArrayList<>();
    public final LightBarTransitionsController mTransitionsController;

    public int getTintAnimationDuration() {
        return R$styleable.AppCompatTheme_windowFixedHeightMajor;
    }

    public DarkIconDispatcherImpl(Context context, LightBarTransitionsController.Factory factory, DumpManager dumpManager) {
        this.mDarkModeIconColorSingleTone = context.getColor(R$color.dark_mode_icon_color_single_tone);
        this.mLightModeIconColorSingleTone = context.getColor(R$color.light_mode_icon_color_single_tone);
        this.mTransitionsController = factory.create(this);
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
    }

    public LightBarTransitionsController getTransitionsController() {
        return this.mTransitionsController;
    }

    public void addDarkReceiver(DarkIconDispatcher.DarkReceiver darkReceiver) {
        this.mReceivers.put(darkReceiver, darkReceiver);
        darkReceiver.onDarkChanged(this.mTintAreas, this.mDarkIntensity, this.mIconTint);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addDarkReceiver$0(ImageView imageView, ArrayList arrayList, float f, int i) {
        imageView.setImageTintList(ColorStateList.valueOf(DarkIconDispatcher.getTint(this.mTintAreas, imageView, this.mIconTint)));
    }

    public void addDarkReceiver(ImageView imageView) {
        DarkIconDispatcherImpl$$ExternalSyntheticLambda0 darkIconDispatcherImpl$$ExternalSyntheticLambda0 = new DarkIconDispatcherImpl$$ExternalSyntheticLambda0(this, imageView);
        this.mReceivers.put(imageView, darkIconDispatcherImpl$$ExternalSyntheticLambda0);
        darkIconDispatcherImpl$$ExternalSyntheticLambda0.onDarkChanged(this.mTintAreas, this.mDarkIntensity, this.mIconTint);
    }

    public void removeDarkReceiver(DarkIconDispatcher.DarkReceiver darkReceiver) {
        this.mReceivers.remove(darkReceiver);
    }

    public void removeDarkReceiver(ImageView imageView) {
        this.mReceivers.remove(imageView);
    }

    public void applyDark(DarkIconDispatcher.DarkReceiver darkReceiver) {
        this.mReceivers.get(darkReceiver).onDarkChanged(this.mTintAreas, this.mDarkIntensity, this.mIconTint);
    }

    public void setIconsDarkArea(ArrayList<Rect> arrayList) {
        if (arrayList != null || !this.mTintAreas.isEmpty()) {
            this.mTintAreas.clear();
            if (arrayList != null) {
                this.mTintAreas.addAll(arrayList);
            }
            applyIconTint();
        }
    }

    public void applyDarkIntensity(float f) {
        this.mDarkIntensity = f;
        this.mIconTint = ((Integer) ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(this.mLightModeIconColorSingleTone), Integer.valueOf(this.mDarkModeIconColorSingleTone))).intValue();
        applyIconTint();
    }

    public final void applyIconTint() {
        for (int i = 0; i < this.mReceivers.size(); i++) {
            this.mReceivers.valueAt(i).onDarkChanged(this.mTintAreas, this.mDarkIntensity, this.mIconTint);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("DarkIconDispatcher: ");
        printWriter.println("  mIconTint: 0x" + Integer.toHexString(this.mIconTint));
        printWriter.println("  mDarkIntensity: " + this.mDarkIntensity + "f");
        StringBuilder sb = new StringBuilder();
        sb.append("  mTintAreas: ");
        sb.append(this.mTintAreas);
        printWriter.println(sb.toString());
    }
}
