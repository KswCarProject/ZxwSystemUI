package com.android.systemui.qs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.ViewController;

public class QSFooterViewController extends ViewController<QSFooterView> implements QSFooter {
    public final ActivityStarter mActivityStarter;
    public final TextView mBuildText = ((TextView) ((QSFooterView) this.mView).findViewById(R$id.build));
    public final View mEditButton = ((QSFooterView) this.mView).findViewById(16908291);
    public final FalsingManager mFalsingManager;
    public final PageIndicator mPageIndicator = ((PageIndicator) ((QSFooterView) this.mView).findViewById(R$id.footer_page_indicator));
    public final QSPanelController mQsPanelController;
    public final UserTracker mUserTracker;

    public void onViewDetached() {
    }

    public QSFooterViewController(QSFooterView qSFooterView, UserTracker userTracker, FalsingManager falsingManager, ActivityStarter activityStarter, QSPanelController qSPanelController) {
        super(qSFooterView);
        this.mUserTracker = userTracker;
        this.mQsPanelController = qSPanelController;
        this.mFalsingManager = falsingManager;
        this.mActivityStarter = activityStarter;
    }

    public void onViewAttached() {
        this.mBuildText.setOnLongClickListener(new QSFooterViewController$$ExternalSyntheticLambda0(this));
        this.mEditButton.setOnClickListener(new QSFooterViewController$$ExternalSyntheticLambda1(this));
        this.mQsPanelController.setFooterPageIndicator(this.mPageIndicator);
        ((QSFooterView) this.mView).updateEverything();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$onViewAttached$0(View view) {
        CharSequence text = this.mBuildText.getText();
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        ((ClipboardManager) this.mUserTracker.getUserContext().getSystemService(ClipboardManager.class)).setPrimaryClip(ClipData.newPlainText(getResources().getString(R$string.build_number_clip_data_label), text));
        Toast.makeText(getContext(), R$string.build_number_copy_toast, 0).show();
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewAttached$2(View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mActivityStarter.postQSRunnableDismissingKeyguard(new QSFooterViewController$$ExternalSyntheticLambda2(this, view));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewAttached$1(View view) {
        this.mQsPanelController.showEdit(view);
    }

    public void setVisibility(int i) {
        ((QSFooterView) this.mView).setVisibility(i);
        this.mEditButton.setClickable(i == 0);
    }

    public void setExpanded(boolean z) {
        ((QSFooterView) this.mView).setExpanded(z);
    }

    public void setExpansion(float f) {
        ((QSFooterView) this.mView).setExpansion(f);
    }

    public void setKeyguardShowing(boolean z) {
        ((QSFooterView) this.mView).setKeyguardShowing();
    }

    public void disable(int i, int i2, boolean z) {
        ((QSFooterView) this.mView).disable(i2);
    }
}
