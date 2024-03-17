package com.android.systemui.biometrics;

import android.graphics.PointF;
import com.android.systemui.R$integer;
import com.android.systemui.biometrics.UdfpsEnrollHelper;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;

public class UdfpsEnrollViewController extends UdfpsAnimationViewController<UdfpsEnrollView> {
    public final UdfpsEnrollHelper mEnrollHelper;
    public final UdfpsEnrollHelper.Listener mEnrollHelperListener = new UdfpsEnrollHelper.Listener() {
        public void onEnrollmentProgress(int i, int i2) {
            ((UdfpsEnrollView) UdfpsEnrollViewController.this.mView).onEnrollmentProgress(i, i2);
        }

        public void onEnrollmentHelp(int i, int i2) {
            ((UdfpsEnrollView) UdfpsEnrollViewController.this.mView).onEnrollmentHelp(i, i2);
        }

        public void onLastStepAcquired() {
            ((UdfpsEnrollView) UdfpsEnrollViewController.this.mView).onLastStepAcquired();
        }
    };
    public final int mEnrollProgressBarRadius;

    public String getTag() {
        return "UdfpsEnrollViewController";
    }

    public UdfpsEnrollViewController(UdfpsEnrollView udfpsEnrollView, UdfpsEnrollHelper udfpsEnrollHelper, StatusBarStateController statusBarStateController, PanelExpansionStateManager panelExpansionStateManager, SystemUIDialogManager systemUIDialogManager, DumpManager dumpManager, float f) {
        super(udfpsEnrollView, statusBarStateController, panelExpansionStateManager, systemUIDialogManager, dumpManager);
        this.mEnrollProgressBarRadius = (int) (f * ((float) getContext().getResources().getInteger(R$integer.config_udfpsEnrollProgressBar)));
        this.mEnrollHelper = udfpsEnrollHelper;
        ((UdfpsEnrollView) this.mView).setEnrollHelper(udfpsEnrollHelper);
    }

    public void onViewAttached() {
        super.onViewAttached();
        if (this.mEnrollHelper.shouldShowProgressBar()) {
            this.mEnrollHelper.setListener(this.mEnrollHelperListener);
        }
    }

    public PointF getTouchTranslation() {
        if (!this.mEnrollHelper.isGuidedEnrollmentStage()) {
            return new PointF(0.0f, 0.0f);
        }
        return this.mEnrollHelper.getNextGuidedEnrollmentPoint();
    }

    public int getPaddingX() {
        return this.mEnrollProgressBarRadius;
    }

    public int getPaddingY() {
        return this.mEnrollProgressBarRadius;
    }

    public void doAnnounceForAccessibility(String str) {
        ((UdfpsEnrollView) this.mView).announceForAccessibility(str);
    }
}
