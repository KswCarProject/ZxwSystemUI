package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.util.ContrastColorUtil;
import com.android.wm.shell.R;
import com.android.wm.shell.bubbles.BadgedImageView;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: BubbleOverflowContainerView */
public class BubbleOverflowAdapter extends RecyclerView.Adapter<ViewHolder> {
    public List<Bubble> mBubbles;
    public Context mContext;
    public BubblePositioner mPositioner;
    public Consumer<Bubble> mPromoteBubbleFromOverflow;

    public BubbleOverflowAdapter(Context context, List<Bubble> list, Consumer<Bubble> consumer, BubblePositioner bubblePositioner) {
        this.mContext = context;
        this.mBubbles = list;
        this.mPromoteBubbleFromOverflow = consumer;
        this.mPositioner = bubblePositioner;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bubble_overflow_view, viewGroup, false);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{16844002, 16842806});
        int ensureTextContrast = ContrastColorUtil.ensureTextContrast(obtainStyledAttributes.getColor(1, -16777216), obtainStyledAttributes.getColor(0, -1), true);
        obtainStyledAttributes.recycle();
        ((TextView) linearLayout.findViewById(R.id.bubble_view_name)).setTextColor(ensureTextContrast);
        return new ViewHolder(linearLayout, this.mPositioner);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CharSequence charSequence;
        Bubble bubble = this.mBubbles.get(i);
        viewHolder.iconView.setRenderedBubble(bubble);
        viewHolder.iconView.removeDotSuppressionFlag(BadgedImageView.SuppressionFlag.FLYOUT_VISIBLE);
        viewHolder.iconView.setOnClickListener(new BubbleOverflowAdapter$$ExternalSyntheticLambda0(this, bubble));
        String title = bubble.getTitle();
        if (title == null) {
            title = this.mContext.getResources().getString(R.string.notification_bubble_title);
        }
        viewHolder.iconView.setContentDescription(this.mContext.getResources().getString(R.string.bubble_content_description_single, new Object[]{title, bubble.getAppName()}));
        viewHolder.iconView.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, BubbleOverflowAdapter.this.mContext.getResources().getString(R.string.bubble_accessibility_action_add_back)));
            }
        });
        if (bubble.getShortcutInfo() != null) {
            charSequence = bubble.getShortcutInfo().getLabel();
        } else {
            charSequence = bubble.getAppName();
        }
        viewHolder.textView.setText(charSequence);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(Bubble bubble, View view) {
        this.mBubbles.remove(bubble);
        notifyDataSetChanged();
        this.mPromoteBubbleFromOverflow.accept(bubble);
    }

    public int getItemCount() {
        return this.mBubbles.size();
    }

    /* compiled from: BubbleOverflowContainerView */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public BadgedImageView iconView;
        public TextView textView;

        public ViewHolder(LinearLayout linearLayout, BubblePositioner bubblePositioner) {
            super(linearLayout);
            BadgedImageView badgedImageView = (BadgedImageView) linearLayout.findViewById(R.id.bubble_view);
            this.iconView = badgedImageView;
            badgedImageView.initialize(bubblePositioner);
            this.textView = (TextView) linearLayout.findViewById(R.id.bubble_view_name);
        }
    }
}
