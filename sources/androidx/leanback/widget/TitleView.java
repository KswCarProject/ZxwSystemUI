package androidx.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.leanback.R$attr;
import androidx.leanback.R$id;
import androidx.leanback.R$layout;

public class TitleView extends FrameLayout {
    public int flags;
    public ImageView mBadgeView;
    public boolean mHasSearchListener;
    public SearchOrbView mSearchOrbView;
    public TextView mTextView;
    public final TitleViewAdapter mTitleViewAdapter;

    public TitleView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TitleView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.browseTitleViewStyle);
    }

    public TitleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.flags = 6;
        this.mHasSearchListener = false;
        this.mTitleViewAdapter = new TitleViewAdapter() {
        };
        View inflate = LayoutInflater.from(context).inflate(R$layout.lb_title_view, this);
        this.mBadgeView = (ImageView) inflate.findViewById(R$id.title_badge);
        this.mTextView = (TextView) inflate.findViewById(R$id.title_text);
        this.mSearchOrbView = (SearchOrbView) inflate.findViewById(R$id.title_orb);
        setClipToPadding(false);
        setClipChildren(false);
    }
}
