package com.android.systemui.wallet.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import com.android.systemui.R$dimen;
import com.android.systemui.R$layout;
import java.util.Collections;
import java.util.List;

public class WalletCardCarousel extends RecyclerView {
    public float mCardCenterToScreenCenterDistancePx;
    public float mCardEdgeToCenterDistance;
    public int mCardHeightPx;
    public int mCardMarginPx;
    public OnCardScrollListener mCardScrollListener;
    public int mCardWidthPx;
    public int mCenteredAdapterPosition;
    public float mCornerRadiusPx;
    public float mEdgeToCenterDistance;
    public int mExpectedViewWidth;
    public OnSelectionListener mSelectionListener;
    public final Rect mSystemGestureExclusionZone;
    public int mTotalCardWidth;
    public final WalletCardCarouselAdapter mWalletCardCarouselAdapter;

    public interface OnCardScrollListener {
        void onCardScroll(WalletCardViewInfo walletCardViewInfo, WalletCardViewInfo walletCardViewInfo2, float f);
    }

    public interface OnSelectionListener {
        void onCardClicked(WalletCardViewInfo walletCardViewInfo);

        void onCardSelected(WalletCardViewInfo walletCardViewInfo);

        void onUncenteredClick(int i);

        void queryWalletCards();
    }

    public WalletCardCarousel(Context context) {
        this(context, (AttributeSet) null);
    }

    public WalletCardCarousel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSystemGestureExclusionZone = new Rect();
        this.mCenteredAdapterPosition = -1;
        this.mEdgeToCenterDistance = Float.MAX_VALUE;
        this.mCardCenterToScreenCenterDistancePx = Float.MAX_VALUE;
        setLayoutManager(new LinearLayoutManager(context, 0, false));
        addOnScrollListener(new CardCarouselScrollListener());
        new CarouselSnapHelper().attachToRecyclerView(this);
        WalletCardCarouselAdapter walletCardCarouselAdapter = new WalletCardCarouselAdapter();
        this.mWalletCardCarouselAdapter = walletCardCarouselAdapter;
        walletCardCarouselAdapter.setHasStableIds(true);
        setAdapter(walletCardCarouselAdapter);
        ViewCompat.setAccessibilityDelegate(this, new CardCarouselAccessibilityDelegate(this));
        addItemDecoration(new DotIndicatorDecoration(getContext()));
    }

    public void setExpectedViewWidth(int i) {
        if (this.mExpectedViewWidth != i) {
            this.mExpectedViewWidth = i;
            Resources resources = getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            int round = Math.round(((float) Math.min(i, Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels))) * 0.69f);
            this.mCardWidthPx = round;
            this.mCardHeightPx = Math.round(((float) round) / 1.5909091f);
            int i2 = this.mCardWidthPx;
            this.mCornerRadiusPx = ((float) i2) * 0.035714287f;
            this.mCardMarginPx = Math.round(((float) i2) * -0.03f);
            int dimensionPixelSize = this.mCardWidthPx + (resources.getDimensionPixelSize(R$dimen.card_margin) * 2);
            this.mTotalCardWidth = dimensionPixelSize;
            this.mCardEdgeToCenterDistance = ((float) dimensionPixelSize) / 2.0f;
            updatePadding(i);
            OnSelectionListener onSelectionListener = this.mSelectionListener;
            if (onSelectionListener != null) {
                onSelectionListener.queryWalletCards();
            }
        }
    }

    public void onViewAdded(View view) {
        super.onViewAdded(view);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int i = this.mCardMarginPx;
        layoutParams.leftMargin = i;
        layoutParams.rightMargin = i;
        view.addOnLayoutChangeListener(new WalletCardCarousel$$ExternalSyntheticLambda0(this, view));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewAdded$0(View view, View view2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        updateCardView(view);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        int width = getWidth();
        if (this.mWalletCardCarouselAdapter.getItemCount() > 1 && ((double) width) < ((double) this.mTotalCardWidth) * 1.5d) {
            this.mSystemGestureExclusionZone.set(0, 0, width, getHeight());
            setSystemGestureExclusionRects(Collections.singletonList(this.mSystemGestureExclusionZone));
        }
        if (width != this.mExpectedViewWidth) {
            updatePadding(width);
        }
    }

    public void setSelectionListener(OnSelectionListener onSelectionListener) {
        this.mSelectionListener = onSelectionListener;
    }

    public void setCardScrollListener(OnCardScrollListener onCardScrollListener) {
        this.mCardScrollListener = onCardScrollListener;
    }

    public int getCardWidthPx() {
        return this.mCardWidthPx;
    }

    public int getCardHeightPx() {
        return this.mCardHeightPx;
    }

    public void resetAdapter() {
        setAdapter(this.mWalletCardCarouselAdapter);
    }

    public boolean setData(List<WalletCardViewInfo> list, int i, boolean z) {
        boolean r4 = this.mWalletCardCarouselAdapter.setData(list, z);
        scrollToPosition(i);
        WalletCardViewInfo walletCardViewInfo = list.get(i);
        this.mCardScrollListener.onCardScroll(walletCardViewInfo, walletCardViewInfo, 0.0f);
        return r4;
    }

    public void scrollToPosition(int i) {
        super.scrollToPosition(i);
        this.mSelectionListener.onCardSelected((WalletCardViewInfo) this.mWalletCardCarouselAdapter.mData.get(i));
    }

    public final void updatePadding(int i) {
        int i2;
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        int max = Math.max(0, ((i - this.mTotalCardWidth) / 2) - this.mCardMarginPx);
        setPadding(max, getPaddingTop(), max, getPaddingBottom());
        WalletCardCarouselAdapter walletCardCarouselAdapter = this.mWalletCardCarouselAdapter;
        if (walletCardCarouselAdapter != null && walletCardCarouselAdapter.getItemCount() > 0 && (i2 = this.mCenteredAdapterPosition) != -1 && (findViewHolderForAdapterPosition = findViewHolderForAdapterPosition(i2)) != null) {
            View view = findViewHolderForAdapterPosition.itemView;
            scrollBy(((view.getLeft() + view.getRight()) / 2) - ((getLeft() + getRight()) / 2), 0);
        }
    }

    public final void updateCardView(View view) {
        int i;
        CardView cardView = ((WalletCardViewHolder) view.getTag()).mCardView;
        float width = ((float) getWidth()) / 2.0f;
        float right = ((float) (view.getRight() + view.getLeft())) / 2.0f;
        float f = right - width;
        float max = Math.max(0.83f, 1.0f - Math.abs(f / ((float) view.getWidth())));
        cardView.setScaleX(max);
        cardView.setScaleY(max);
        if (right < width) {
            i = view.getRight() + this.mCardMarginPx;
        } else {
            i = view.getLeft() - this.mCardMarginPx;
        }
        if (Math.abs(f) < this.mCardCenterToScreenCenterDistancePx && getChildAdapterPosition(view) != -1) {
            this.mCenteredAdapterPosition = getChildAdapterPosition(view);
            this.mEdgeToCenterDistance = ((float) i) - width;
            this.mCardCenterToScreenCenterDistancePx = Math.abs(f);
        }
    }

    public class CardCarouselScrollListener extends RecyclerView.OnScrollListener {
        public int mOldState;

        public CardCarouselScrollListener() {
            this.mOldState = -1;
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            if (i == 0 && i != this.mOldState) {
                WalletCardCarousel.this.performHapticFeedback(1);
            }
            this.mOldState = i;
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            WalletCardCarousel walletCardCarousel = WalletCardCarousel.this;
            int i3 = -1;
            walletCardCarousel.mCenteredAdapterPosition = -1;
            walletCardCarousel.mEdgeToCenterDistance = Float.MAX_VALUE;
            walletCardCarousel.mCardCenterToScreenCenterDistancePx = Float.MAX_VALUE;
            for (int i4 = 0; i4 < WalletCardCarousel.this.getChildCount(); i4++) {
                WalletCardCarousel walletCardCarousel2 = WalletCardCarousel.this;
                walletCardCarousel2.updateCardView(walletCardCarousel2.getChildAt(i4));
            }
            WalletCardCarousel walletCardCarousel3 = WalletCardCarousel.this;
            int i5 = walletCardCarousel3.mCenteredAdapterPosition;
            if (i5 != -1 && i != 0) {
                if (walletCardCarousel3.mEdgeToCenterDistance > 0.0f) {
                    i3 = 1;
                }
                int i6 = i5 + i3;
                if (i6 >= 0 && i6 < walletCardCarousel3.mWalletCardCarouselAdapter.mData.size()) {
                    WalletCardCarousel.this.mCardScrollListener.onCardScroll((WalletCardViewInfo) WalletCardCarousel.this.mWalletCardCarouselAdapter.mData.get(WalletCardCarousel.this.mCenteredAdapterPosition), (WalletCardViewInfo) WalletCardCarousel.this.mWalletCardCarouselAdapter.mData.get(i6), Math.abs(WalletCardCarousel.this.mEdgeToCenterDistance) / WalletCardCarousel.this.mCardEdgeToCenterDistance);
                }
            }
        }
    }

    public class CarouselSnapHelper extends PagerSnapHelper {
        public CarouselSnapHelper() {
        }

        public View findSnapView(RecyclerView.LayoutManager layoutManager) {
            View findSnapView = super.findSnapView(layoutManager);
            if (findSnapView == null) {
                return null;
            }
            WalletCardViewInfo walletCardViewInfo = ((WalletCardViewHolder) findSnapView.getTag()).mCardViewInfo;
            WalletCardCarousel.this.mSelectionListener.onCardSelected(walletCardViewInfo);
            WalletCardCarousel.this.mCardScrollListener.onCardScroll(walletCardViewInfo, walletCardViewInfo, 0.0f);
            return findSnapView;
        }

        public LinearSmoothScroller createScroller(final RecyclerView.LayoutManager layoutManager) {
            return new LinearSmoothScroller(WalletCardCarousel.this.getContext()) {
                public void onTargetFound(View view, RecyclerView.State state, RecyclerView.SmoothScroller.Action action) {
                    int[] calculateDistanceToFinalSnap = CarouselSnapHelper.this.calculateDistanceToFinalSnap(layoutManager, view);
                    int i = calculateDistanceToFinalSnap[0];
                    int i2 = calculateDistanceToFinalSnap[1];
                    int calculateTimeForDeceleration = calculateTimeForDeceleration(Math.max(Math.abs(i), Math.abs(i2)));
                    if (calculateTimeForDeceleration > 0) {
                        action.update(i, i2, calculateTimeForDeceleration, this.mDecelerateInterpolator);
                    }
                }

                public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return 200.0f / ((float) displayMetrics.densityDpi);
                }

                public int calculateTimeForScrolling(int i) {
                    return Math.min(80, super.calculateTimeForScrolling(i));
                }
            };
        }
    }

    public class WalletCardCarouselAdapter extends RecyclerView.Adapter<WalletCardViewHolder> {
        public List<WalletCardViewInfo> mData;

        public WalletCardCarouselAdapter() {
            this.mData = Collections.EMPTY_LIST;
        }

        public WalletCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.wallet_card_view, viewGroup, false);
            WalletCardViewHolder walletCardViewHolder = new WalletCardViewHolder(inflate);
            CardView cardView = walletCardViewHolder.mCardView;
            cardView.setRadius(WalletCardCarousel.this.mCornerRadiusPx);
            ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
            layoutParams.width = WalletCardCarousel.this.getCardWidthPx();
            layoutParams.height = WalletCardCarousel.this.getCardHeightPx();
            inflate.setTag(walletCardViewHolder);
            return walletCardViewHolder;
        }

        public void onBindViewHolder(WalletCardViewHolder walletCardViewHolder, int i) {
            WalletCardViewInfo walletCardViewInfo = this.mData.get(i);
            walletCardViewHolder.mCardViewInfo = walletCardViewInfo;
            if (walletCardViewInfo.getCardId().isEmpty()) {
                walletCardViewHolder.mImageView.setScaleType(ImageView.ScaleType.CENTER);
            }
            walletCardViewHolder.mImageView.setImageDrawable(walletCardViewInfo.getCardDrawable());
            walletCardViewHolder.mCardView.setContentDescription(walletCardViewInfo.getContentDescription());
            walletCardViewHolder.mCardView.setOnClickListener(new WalletCardCarousel$WalletCardCarouselAdapter$$ExternalSyntheticLambda0(this, i, walletCardViewInfo));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$0(int i, WalletCardViewInfo walletCardViewInfo, View view) {
            WalletCardCarousel walletCardCarousel = WalletCardCarousel.this;
            if (i != walletCardCarousel.mCenteredAdapterPosition) {
                walletCardCarousel.mSelectionListener.onUncenteredClick(i);
            } else {
                walletCardCarousel.mSelectionListener.onCardClicked(walletCardViewInfo);
            }
        }

        public int getItemCount() {
            return this.mData.size();
        }

        public long getItemId(int i) {
            return (long) this.mData.get(i).getCardId().hashCode();
        }

        public final boolean setData(List<WalletCardViewInfo> list, boolean z) {
            List<WalletCardViewInfo> list2 = this.mData;
            this.mData = list;
            if (!z && isUiEquivalent(list2, list)) {
                return false;
            }
            notifyDataSetChanged();
            return true;
        }

        public final boolean isUiEquivalent(List<WalletCardViewInfo> list, List<WalletCardViewInfo> list2) {
            if (list.size() != list2.size()) {
                return false;
            }
            for (int i = 0; i < list2.size(); i++) {
                if (!list.get(i).isUiEquivalent(list2.get(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public class CardCarouselAccessibilityDelegate extends RecyclerViewAccessibilityDelegate {
        public CardCarouselAccessibilityDelegate(RecyclerView recyclerView) {
            super(recyclerView);
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            if (accessibilityEvent.getEventType() == 32768) {
                WalletCardCarousel walletCardCarousel = WalletCardCarousel.this;
                walletCardCarousel.scrollToPosition(walletCardCarousel.getChildAdapterPosition(view));
            }
            return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }
}
