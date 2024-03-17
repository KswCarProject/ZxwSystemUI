package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.IndentingPrintWriter;
import android.view.View;
import com.android.systemui.R$color;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.notification.stack.ExpandableViewState;
import com.android.systemui.statusbar.notification.stack.ViewState;
import com.android.systemui.util.DumpUtilsKt;
import java.io.PrintWriter;

public class FooterView extends StackScrollerDecorView {
    public FooterViewButton mClearAllButton;
    public FooterViewButton mManageButton;
    public String mManageNotificationHistoryText;
    public String mManageNotificationText;
    public boolean mShowHistory;

    public FooterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public View findContentView() {
        return findViewById(R$id.content);
    }

    public View findSecondaryView() {
        return findViewById(R$id.dismiss_text);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        super.dump(asIndenting, strArr);
        DumpUtilsKt.withIncreasedIndent(asIndenting, new FooterView$$ExternalSyntheticLambda0(this, asIndenting));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$dump$0(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("visibility: " + DumpUtilsKt.visibilityString(getVisibility()));
        indentingPrintWriter.println("manageButton showHistory: " + this.mShowHistory);
        indentingPrintWriter.println("manageButton visibility: " + DumpUtilsKt.visibilityString(this.mClearAllButton.getVisibility()));
        indentingPrintWriter.println("dismissButton visibility: " + DumpUtilsKt.visibilityString(this.mClearAllButton.getVisibility()));
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mClearAllButton = (FooterViewButton) findSecondaryView();
        this.mManageButton = (FooterViewButton) findViewById(R$id.manage_text);
        updateResources();
        updateText();
    }

    public void setManageButtonClickListener(View.OnClickListener onClickListener) {
        this.mManageButton.setOnClickListener(onClickListener);
    }

    public void setClearAllButtonClickListener(View.OnClickListener onClickListener) {
        this.mClearAllButton.setOnClickListener(onClickListener);
    }

    public boolean isOnEmptySpace(float f, float f2) {
        return f < this.mContent.getX() || f > this.mContent.getX() + ((float) this.mContent.getWidth()) || f2 < this.mContent.getY() || f2 > this.mContent.getY() + ((float) this.mContent.getHeight());
    }

    public void showHistory(boolean z) {
        if (this.mShowHistory != z) {
            this.mShowHistory = z;
            updateText();
        }
    }

    public final void updateText() {
        if (this.mShowHistory) {
            this.mManageButton.setText(this.mManageNotificationHistoryText);
            this.mManageButton.setContentDescription(this.mManageNotificationHistoryText);
            return;
        }
        this.mManageButton.setText(this.mManageNotificationText);
        this.mManageButton.setContentDescription(this.mManageNotificationText);
    }

    public boolean isHistoryShown() {
        return this.mShowHistory;
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateColors();
        this.mClearAllButton.setText(R$string.clear_all_notifications_text);
        this.mClearAllButton.setContentDescription(this.mContext.getString(R$string.accessibility_clear_all));
        updateResources();
        updateText();
    }

    public void updateColors() {
        Resources.Theme theme = this.mContext.getTheme();
        int color = getResources().getColor(R$color.notif_pill_text, theme);
        FooterViewButton footerViewButton = this.mClearAllButton;
        int i = R$drawable.notif_footer_btn_background;
        footerViewButton.setBackground(theme.getDrawable(i));
        this.mClearAllButton.setTextColor(color);
        this.mManageButton.setBackground(theme.getDrawable(i));
        this.mManageButton.setTextColor(color);
    }

    public final void updateResources() {
        this.mManageNotificationText = getContext().getString(R$string.manage_notifications_text);
        this.mManageNotificationHistoryText = getContext().getString(R$string.manage_notifications_history_text);
    }

    public ExpandableViewState createExpandableViewState() {
        return new FooterViewState();
    }

    public class FooterViewState extends ExpandableViewState {
        public boolean hideContent;

        public FooterViewState() {
        }

        public void copyFrom(ViewState viewState) {
            super.copyFrom(viewState);
            if (viewState instanceof FooterViewState) {
                this.hideContent = ((FooterViewState) viewState).hideContent;
            }
        }

        public void applyToView(View view) {
            super.applyToView(view);
            if (view instanceof FooterView) {
                ((FooterView) view).setContentVisible(!this.hideContent);
            }
        }
    }
}
