package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.R$plurals;
import com.android.systemui.qs.FgsManagerController;
import java.util.concurrent.Executor;

public class QSFgsManagerFooter implements View.OnClickListener, FgsManagerController.OnDialogDismissedListener, FgsManagerController.OnNumberOfPackagesChangedListener {
    public final ImageView mCollapsedDotView;
    public final Context mContext;
    public final ImageView mDotView;
    public final Executor mExecutor;
    public final FgsManagerController mFgsManagerController;
    public final TextView mFooterText;
    public boolean mIsInitialized = false;
    public final Executor mMainExecutor;
    public int mNumPackages;
    public final View mNumberContainer;
    public final TextView mNumberView;
    public final View mRootView;
    public final View mTextContainer;
    public VisibilityChangedDispatcher$OnVisibilityChangedListener mVisibilityChangedListener;

    public QSFgsManagerFooter(View view, Executor executor, Executor executor2, FgsManagerController fgsManagerController) {
        this.mRootView = view;
        this.mFooterText = (TextView) view.findViewById(R$id.footer_text);
        this.mTextContainer = view.findViewById(R$id.fgs_text_container);
        this.mNumberContainer = view.findViewById(R$id.fgs_number_container);
        this.mNumberView = (TextView) view.findViewById(R$id.fgs_number);
        this.mDotView = (ImageView) view.findViewById(R$id.fgs_new);
        this.mCollapsedDotView = (ImageView) view.findViewById(R$id.fgs_collapsed_new);
        this.mContext = view.getContext();
        this.mMainExecutor = executor;
        this.mExecutor = executor2;
        this.mFgsManagerController = fgsManagerController;
    }

    public void setCollapsed(boolean z) {
        this.mTextContainer.setVisibility(8);
        this.mNumberContainer.setVisibility(8);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mRootView.getLayoutParams();
        layoutParams.width = -2;
        layoutParams.weight = 0.0f;
        this.mRootView.setLayoutParams(layoutParams);
    }

    public void init() {
        if (!this.mIsInitialized) {
            this.mFgsManagerController.init();
            this.mIsInitialized = true;
        }
    }

    public void setListening(boolean z) {
        if (z) {
            this.mFgsManagerController.addOnDialogDismissedListener(this);
            this.mFgsManagerController.addOnNumberOfPackagesChangedListener(this);
            this.mNumPackages = this.mFgsManagerController.getNumRunningPackages();
            refreshState();
            return;
        }
        this.mFgsManagerController.removeOnDialogDismissedListener(this);
        this.mFgsManagerController.removeOnNumberOfPackagesChangedListener(this);
    }

    public void setOnVisibilityChangedListener(VisibilityChangedDispatcher$OnVisibilityChangedListener visibilityChangedDispatcher$OnVisibilityChangedListener) {
        this.mVisibilityChangedListener = visibilityChangedDispatcher$OnVisibilityChangedListener;
    }

    public void onClick(View view) {
        this.mFgsManagerController.showDialog(this.mRootView);
    }

    public void refreshState() {
        this.mExecutor.execute(new QSFgsManagerFooter$$ExternalSyntheticLambda0(this));
    }

    public View getView() {
        return this.mRootView;
    }

    public void handleRefreshState() {
        this.mMainExecutor.execute(new QSFgsManagerFooter$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleRefreshState$0() {
        Resources resources = this.mContext.getResources();
        int i = R$plurals.fgs_manager_footer_label;
        int i2 = this.mNumPackages;
        int i3 = 0;
        String quantityString = resources.getQuantityString(i, i2, new Object[]{Integer.valueOf(i2)});
        this.mFooterText.setText(quantityString);
        this.mNumberView.setText(Integer.toString(this.mNumPackages));
        this.mNumberView.setContentDescription(quantityString);
        if (this.mFgsManagerController.shouldUpdateFooterVisibility()) {
            this.mRootView.setVisibility((this.mNumPackages <= 0 || !this.mFgsManagerController.isAvailable()) ? 8 : 0);
            if (!this.mFgsManagerController.getShowFooterDot() || !this.mFgsManagerController.getChangesSinceDialog()) {
                i3 = 8;
            }
            this.mDotView.setVisibility(i3);
            this.mCollapsedDotView.setVisibility(i3);
            VisibilityChangedDispatcher$OnVisibilityChangedListener visibilityChangedDispatcher$OnVisibilityChangedListener = this.mVisibilityChangedListener;
            if (visibilityChangedDispatcher$OnVisibilityChangedListener != null) {
                visibilityChangedDispatcher$OnVisibilityChangedListener.onVisibilityChanged(this.mRootView.getVisibility());
            }
        }
    }

    public void onDialogDismissed() {
        refreshState();
    }

    public void onNumberOfPackagesChanged(int i) {
        this.mNumPackages = i;
        refreshState();
    }
}
