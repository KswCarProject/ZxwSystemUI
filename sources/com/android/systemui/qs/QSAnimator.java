package com.android.systemui.qs;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.PagedTileLayout;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.TouchAnimator;
import com.android.systemui.qs.tileimpl.HeightOverrideable;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.animation.UniqueObjectHostView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;

public class QSAnimator implements QSHost.Callback, PagedTileLayout.PageListener, TouchAnimator.Listener, View.OnLayoutChangeListener, View.OnAttachStateChangeListener, TunerService.Tunable {
    public TouchAnimator mAllPagesDelayedAnimator;
    public final ArrayList<View> mAllViews = new ArrayList<>();
    public boolean mAllowFancy;
    public final ArrayList<View> mAnimatedQsViews = new ArrayList<>();
    public TouchAnimator mBrightnessAnimator;
    public int mCurrentPage = 0;
    public final Executor mExecutor;
    public TouchAnimator mFirstPageAnimator;
    public boolean mFullRows;
    public final QSTileHost mHost;
    public float mLastPosition;
    public int mLastQQSTileHeight;
    public boolean mNeedsAnimatorUpdate = false;
    public final TouchAnimator.Listener mNonFirstPageListener = new TouchAnimator.ListenerAdapter() {
        public void onAnimationAtEnd() {
            QSAnimator.this.mQuickQsPanel.setVisibility(4);
        }

        public void onAnimationStarted() {
            QSAnimator.this.mQuickQsPanel.setVisibility(0);
        }
    };
    public final SparseArray<Pair<HeightExpansionAnimator, TouchAnimator>> mNonFirstPageQSAnimators = new SparseArray<>();
    public TouchAnimator mNonfirstPageAlphaAnimator;
    public int mNumQuickTiles;
    public boolean mOnFirstPage = true;
    public boolean mOnKeyguard;
    public HeightExpansionAnimator mOtherFirstPageTilesHeightAnimator;
    public PagedTileLayout mPagedLayout;
    public TouchAnimator mQQSFooterActionsAnimator;
    public HeightExpansionAnimator mQQSTileHeightAnimator;
    public int mQQSTop;
    public TouchAnimator mQQSTranslationYAnimator;
    public final QSExpansionPathInterpolator mQSExpansionPathInterpolator;
    public TouchAnimator mQSTileLayoutTranslatorAnimator;
    public final QS mQs;
    public final QSPanelController mQsPanelController;
    public final QuickQSPanelController mQuickQSPanelController;
    public final QuickQSPanel mQuickQsPanel;
    public final QuickStatusBarHeader mQuickStatusBarHeader;
    public boolean mShowCollapsedOnKeyguard;
    public int[] mTmpLoc1 = new int[2];
    public int[] mTmpLoc2 = new int[2];
    public boolean mTranslateWhileExpanding;
    public TouchAnimator mTranslationXAnimator;
    public TouchAnimator mTranslationYAnimator;
    public final TunerService mTunerService;
    public final Runnable mUpdateAnimators = new QSAnimator$$ExternalSyntheticLambda0(this);

    public QSAnimator(QS qs, QuickQSPanel quickQSPanel, QuickStatusBarHeader quickStatusBarHeader, QSPanelController qSPanelController, QuickQSPanelController quickQSPanelController, QSTileHost qSTileHost, Executor executor, TunerService tunerService, QSExpansionPathInterpolator qSExpansionPathInterpolator) {
        this.mQs = qs;
        this.mQuickQsPanel = quickQSPanel;
        this.mQsPanelController = qSPanelController;
        this.mQuickQSPanelController = quickQSPanelController;
        this.mQuickStatusBarHeader = quickStatusBarHeader;
        this.mHost = qSTileHost;
        this.mExecutor = executor;
        this.mTunerService = tunerService;
        this.mQSExpansionPathInterpolator = qSExpansionPathInterpolator;
        qSTileHost.addCallback(this);
        qSPanelController.addOnAttachStateChangeListener(this);
        qs.getView().addOnLayoutChangeListener(this);
        if (qSPanelController.isAttachedToWindow()) {
            onViewAttachedToWindow((View) null);
        }
        QSPanel.QSTileLayout tileLayout = qSPanelController.getTileLayout();
        if (tileLayout instanceof PagedTileLayout) {
            this.mPagedLayout = (PagedTileLayout) tileLayout;
        } else {
            Log.w("QSAnimator", "QS Not using page layout");
        }
        qSPanelController.setPageListener(this);
    }

    public void onRtlChanged() {
        updateAnimators();
    }

    public void requestAnimatorUpdate() {
        this.mNeedsAnimatorUpdate = true;
    }

    public void setOnKeyguard(boolean z) {
        this.mOnKeyguard = z;
        updateQQSVisibility();
        if (this.mOnKeyguard) {
            clearAnimationState();
        }
    }

    public void setShowCollapsedOnKeyguard(boolean z) {
        this.mShowCollapsedOnKeyguard = z;
        updateQQSVisibility();
        setCurrentPosition();
    }

    public final void setCurrentPosition() {
        setPosition(this.mLastPosition);
    }

    public final void updateQQSVisibility() {
        this.mQuickQsPanel.setVisibility((!this.mOnKeyguard || this.mShowCollapsedOnKeyguard) ? 0 : 4);
    }

    public void onViewAttachedToWindow(View view) {
        this.mTunerService.addTunable(this, "sysui_qs_fancy_anim", "sysui_qs_move_whole_rows");
    }

    public void onViewDetachedFromWindow(View view) {
        this.mHost.removeCallback(this);
        this.mTunerService.removeTunable(this);
    }

    public void onTuningChanged(String str, String str2) {
        if ("sysui_qs_fancy_anim".equals(str)) {
            boolean parseIntegerSwitch = TunerService.parseIntegerSwitch(str2, true);
            this.mAllowFancy = parseIntegerSwitch;
            if (!parseIntegerSwitch) {
                clearAnimationState();
            }
        } else if ("sysui_qs_move_whole_rows".equals(str)) {
            this.mFullRows = TunerService.parseIntegerSwitch(str2, true);
        }
        updateAnimators();
    }

    public final void addNonFirstPageAnimators(int i) {
        Pair<HeightExpansionAnimator, TouchAnimator> createSecondaryPageAnimators = createSecondaryPageAnimators(i);
        if (createSecondaryPageAnimators != null) {
            this.mNonFirstPageQSAnimators.put(i, createSecondaryPageAnimators);
        }
    }

    public void onPageChanged(boolean z, int i) {
        if (!(i == -1 || this.mCurrentPage == i)) {
            this.mCurrentPage = i;
            if (!z && !this.mNonFirstPageQSAnimators.contains(i)) {
                addNonFirstPageAnimators(i);
            }
        }
        if (this.mOnFirstPage != z) {
            if (!z) {
                clearAnimationState();
            }
            this.mOnFirstPage = z;
        }
    }

    public final void translateContent(View view, View view2, View view3, int i, int i2, int[] iArr, TouchAnimator.Builder builder, TouchAnimator.Builder builder2, TouchAnimator.Builder builder3) {
        getRelativePosition(iArr, view, view3);
        int i3 = iArr[0];
        int i4 = iArr[1];
        getRelativePosition(iArr, view2, view3);
        int i5 = iArr[0];
        int i6 = iArr[1];
        int i7 = (i5 - i3) - i;
        builder.addFloat(view, "translationX", 0.0f, (float) i7);
        builder.addFloat(view2, "translationX", (float) (-i7), 0.0f);
        int i8 = (i6 - i4) - i2;
        builder3.addFloat(view, "translationY", 0.0f, (float) i8);
        builder2.addFloat(view2, "translationY", (float) (-i8), 0.0f);
        this.mAllViews.add(view);
        this.mAllViews.add(view2);
    }

    public final void updateAnimators() {
        QSPanel.QSTileLayout qSTileLayout;
        TouchAnimator.Builder builder;
        TouchAnimator.Builder builder2;
        String str;
        TouchAnimator.Builder builder3;
        QSPanel.QSTileLayout qSTileLayout2;
        UniqueObjectHostView uniqueObjectHostView;
        String str2;
        QSPanel.QSTileLayout qSTileLayout3;
        String str3;
        TouchAnimator.Builder builder4;
        boolean z;
        TouchAnimator.Builder builder5;
        String str4;
        TouchAnimator.Builder builder6;
        QSTileView qSTileView;
        int i;
        this.mNeedsAnimatorUpdate = false;
        TouchAnimator.Builder builder7 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder8 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder9 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder10 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder11 = new TouchAnimator.Builder();
        TouchAnimator.Builder interpolator = new TouchAnimator.Builder().setInterpolator(Interpolators.ACCELERATE);
        Collection<QSTile> tiles = this.mHost.getTiles();
        clearAnimationState();
        this.mNonFirstPageQSAnimators.clear();
        this.mAllViews.clear();
        this.mAnimatedQsViews.clear();
        this.mQQSTileHeightAnimator = null;
        this.mOtherFirstPageTilesHeightAnimator = null;
        this.mNumQuickTiles = this.mQuickQsPanel.getNumQuickTiles();
        QSPanel.QSTileLayout tileLayout = this.mQsPanelController.getTileLayout();
        this.mAllViews.add((View) tileLayout);
        int heightDiff = this.mQs.getHeightDiff();
        if (!this.mTranslateWhileExpanding) {
            heightDiff = (int) (((float) heightDiff) * 0.1f);
        }
        int i2 = heightDiff;
        String str5 = "translationY";
        this.mQSTileLayoutTranslatorAnimator = new TouchAnimator.Builder().addFloat(tileLayout, str5, (float) i2, 0.0f).build();
        this.mLastQQSTileHeight = 0;
        String str6 = "alpha";
        if (this.mQsPanelController.areThereTiles()) {
            int i3 = 0;
            for (QSTile next : tiles) {
                QSTileView tileView = this.mQsPanelController.getTileView(next);
                if (tileView != null) {
                    PagedTileLayout pagedTileLayout = this.mPagedLayout;
                    if (pagedTileLayout != null && i3 >= pagedTileLayout.getNumTilesFirstPage()) {
                        break;
                    }
                    View iconView = tileView.getIcon().getIconView();
                    View view = this.mQs.getView();
                    str2 = str6;
                    if (i3 >= this.mQuickQSPanelController.getTileLayout().getNumVisibleTiles() || !this.mAllowFancy) {
                        int i4 = i3;
                        String str7 = str5;
                        qSTileView = tileView;
                        int i5 = i2;
                        qSTileLayout3 = tileLayout;
                        builder6 = interpolator;
                        TouchAnimator.Builder builder12 = builder7;
                        str4 = str2;
                        View view2 = view;
                        builder5 = builder11;
                        if (this.mFullRows) {
                            i = i4;
                            if (isIconInAnimatedRow(i)) {
                                i2 = i5;
                                z = true;
                                builder4 = builder12;
                                str3 = str7;
                                builder4.addFloat(qSTileView, str3, (float) (-i2), 0.0f);
                                this.mAllViews.add(iconView);
                            } else {
                                builder4 = builder12;
                            }
                        } else {
                            builder4 = builder12;
                            i = i4;
                        }
                        i2 = i5;
                        str3 = str7;
                        z = true;
                        SideLabelTileLayout sideLabelTileLayout = (SideLabelTileLayout) this.mQuickQsPanel.getTileLayout();
                        View view3 = view2;
                        getRelativePosition(this.mTmpLoc1, sideLabelTileLayout, view3);
                        this.mQQSTop = this.mTmpLoc1[1];
                        getRelativePosition(this.mTmpLoc2, qSTileView, view3);
                        builder8.addFloat(qSTileView, str3, (float) (-(this.mTmpLoc2[1] - (this.mTmpLoc1[1] + sideLabelTileLayout.getPhantomTopPosition(i)))), 0.0f);
                        if (this.mOtherFirstPageTilesHeightAnimator == null) {
                            this.mOtherFirstPageTilesHeightAnimator = new HeightExpansionAnimator(this, this.mLastQQSTileHeight, qSTileView.getMeasuredHeight());
                        }
                        this.mOtherFirstPageTilesHeightAnimator.addView(qSTileView);
                        qSTileView.setClipChildren(true);
                        qSTileView.setClipToPadding(true);
                        builder4.addFloat(qSTileView.getSecondaryLabel(), str4, 0.0f, 1.0f);
                        this.mAllViews.add(qSTileView.getSecondaryLabel());
                    } else {
                        QSTileView tileView2 = this.mQuickQSPanelController.getTileView(next);
                        if (tileView2 != null) {
                            getRelativePosition(this.mTmpLoc1, tileView2, view);
                            getRelativePosition(this.mTmpLoc2, tileView, view);
                            int[] iArr = this.mTmpLoc2;
                            int i6 = iArr[1];
                            int i7 = i3;
                            int[] iArr2 = this.mTmpLoc1;
                            int i8 = i6 - iArr2[1];
                            int i9 = iArr[0] - iArr2[0];
                            int offsetTranslation = i8 - this.mQuickStatusBarHeader.getOffsetTranslation();
                            int i10 = i2;
                            QSPanel.QSTileLayout qSTileLayout4 = tileLayout;
                            builder9.addFloat(tileView2, str5, 0.0f, (float) offsetTranslation);
                            builder8.addFloat(tileView, str5, (float) (-offsetTranslation), 0.0f);
                            builder10.addFloat(tileView2, "translationX", 0.0f, (float) i9);
                            builder10.addFloat(tileView, "translationX", (float) (-i9), 0.0f);
                            if (this.mQQSTileHeightAnimator == null) {
                                this.mQQSTileHeightAnimator = new HeightExpansionAnimator(this, tileView2.getMeasuredHeight(), tileView.getMeasuredHeight());
                                this.mLastQQSTileHeight = tileView2.getMeasuredHeight();
                            }
                            this.mQQSTileHeightAnimator.addView(tileView2);
                            TouchAnimator.Builder builder13 = builder7;
                            int i11 = i7;
                            int i12 = i9;
                            QSIconView icon = tileView2.getIcon();
                            int i13 = i11;
                            String str8 = str2;
                            QSIconView icon2 = tileView.getIcon();
                            String str9 = str5;
                            View view4 = view;
                            View view5 = view;
                            int i14 = i12;
                            QSTileView qSTileView2 = tileView;
                            int i15 = i8;
                            String str10 = str9;
                            qSTileLayout3 = qSTileLayout4;
                            TouchAnimator.Builder builder14 = builder10;
                            TouchAnimator.Builder builder15 = interpolator;
                            TouchAnimator.Builder builder16 = builder8;
                            TouchAnimator.Builder builder17 = builder11;
                            TouchAnimator.Builder builder18 = builder9;
                            translateContent(icon, icon2, view4, i14, i15, this.mTmpLoc1, builder14, builder16, builder18);
                            View view6 = view5;
                            translateContent(tileView2.getLabelContainer(), qSTileView2.getLabelContainer(), view6, i14, i15, this.mTmpLoc1, builder14, builder16, builder18);
                            translateContent(tileView2.getSecondaryIcon(), qSTileView2.getSecondaryIcon(), view6, i14, i15, this.mTmpLoc1, builder14, builder16, builder18);
                            str4 = str8;
                            builder6 = builder15;
                            builder6.addFloat(tileView2.getSecondaryLabel(), str4, 0.0f, 1.0f);
                            builder5 = builder17;
                            builder5.addFloat(tileView2.getSecondaryLabel(), str4, 0.0f, 0.0f);
                            qSTileView = qSTileView2;
                            this.mAnimatedQsViews.add(qSTileView);
                            this.mAllViews.add(tileView2);
                            this.mAllViews.add(tileView2.getSecondaryLabel());
                            builder4 = builder13;
                            i = i13;
                            i2 = i10;
                            str3 = str10;
                            z = true;
                        }
                    }
                    this.mAllViews.add(qSTileView);
                    i3 = i + 1;
                    builder7 = builder4;
                    interpolator = builder6;
                    str6 = str4;
                    str5 = str3;
                    builder11 = builder5;
                    boolean z2 = z;
                    tileLayout = qSTileLayout3;
                } else {
                    Log.e("QSAnimator", "tileView is null " + next.getTileSpec());
                    str2 = str6;
                }
                str6 = str2;
            }
            str = str6;
            qSTileLayout = tileLayout;
            builder3 = interpolator;
            builder2 = builder11;
            builder = builder7;
            int i16 = this.mCurrentPage;
            if (i16 != 0) {
                addNonFirstPageAnimators(i16);
            }
        } else {
            str = str6;
            qSTileLayout = tileLayout;
            builder3 = interpolator;
            builder2 = builder11;
            builder = builder7;
        }
        if (this.mAllowFancy) {
            animateBrightnessSlider(builder);
            qSTileLayout2 = qSTileLayout;
            this.mFirstPageAnimator = builder.addFloat(qSTileLayout2, str, 0.0f, 1.0f).addFloat(builder3.build(), "position", 0.0f, 1.0f).setListener(this).build();
            TouchAnimator.Builder startDelay = new TouchAnimator.Builder().setStartDelay(0.86f);
            if (!this.mQsPanelController.shouldUseHorizontalLayout() || (uniqueObjectHostView = this.mQsPanelController.mMediaHost.hostView) == null) {
                this.mQsPanelController.mMediaHost.hostView.setAlpha(1.0f);
            } else {
                startDelay.addFloat(uniqueObjectHostView, str, 0.0f, 1.0f);
            }
            this.mAllPagesDelayedAnimator = startDelay.build();
            builder8.setInterpolator(this.mQSExpansionPathInterpolator.getYInterpolator());
            builder9.setInterpolator(this.mQSExpansionPathInterpolator.getYInterpolator());
            builder10.setInterpolator(this.mQSExpansionPathInterpolator.getXInterpolator());
            if (this.mOnFirstPage) {
                this.mQQSTranslationYAnimator = builder9.build();
            }
            this.mTranslationYAnimator = builder8.build();
            this.mTranslationXAnimator = builder10.build();
            HeightExpansionAnimator heightExpansionAnimator = this.mQQSTileHeightAnimator;
            if (heightExpansionAnimator != null) {
                heightExpansionAnimator.setInterpolator(this.mQSExpansionPathInterpolator.getYInterpolator());
            }
            HeightExpansionAnimator heightExpansionAnimator2 = this.mOtherFirstPageTilesHeightAnimator;
            if (heightExpansionAnimator2 != null) {
                heightExpansionAnimator2.setInterpolator(this.mQSExpansionPathInterpolator.getYInterpolator());
            }
        } else {
            qSTileLayout2 = qSTileLayout;
        }
        this.mNonfirstPageAlphaAnimator = builder2.addFloat(this.mQuickQsPanel, str, 1.0f, 0.0f).addFloat(qSTileLayout2, str, 0.0f, 1.0f).setListener(this.mNonFirstPageListener).setEndDelay(0.9f).build();
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x015e  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0173 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.util.Pair<com.android.systemui.qs.QSAnimator.HeightExpansionAnimator, com.android.systemui.qs.TouchAnimator> createSecondaryPageAnimators(int r21) {
        /*
            r20 = this;
            r0 = r20
            r1 = r21
            com.android.systemui.qs.PagedTileLayout r2 = r0.mPagedLayout
            r3 = 0
            if (r2 != 0) goto L_0x000a
            return r3
        L_0x000a:
            com.android.systemui.qs.TouchAnimator$Builder r2 = new com.android.systemui.qs.TouchAnimator$Builder
            r2.<init>()
            com.android.systemui.qs.QSExpansionPathInterpolator r4 = r0.mQSExpansionPathInterpolator
            android.view.animation.Interpolator r4 = r4.getYInterpolator()
            com.android.systemui.qs.TouchAnimator$Builder r2 = r2.setInterpolator(r4)
            com.android.systemui.qs.TouchAnimator$Builder r4 = new com.android.systemui.qs.TouchAnimator$Builder
            r4.<init>()
            r5 = 1041865114(0x3e19999a, float:0.15)
            com.android.systemui.qs.TouchAnimator$Builder r4 = r4.setStartDelay(r5)
            r5 = 1060320051(0x3f333333, float:0.7)
            com.android.systemui.qs.TouchAnimator$Builder r4 = r4.setEndDelay(r5)
            com.android.systemui.qs.QuickQSPanel r5 = r0.mQuickQsPanel
            com.android.systemui.qs.QSPanel$QSTileLayout r5 = r5.getTileLayout()
            com.android.systemui.qs.SideLabelTileLayout r5 = (com.android.systemui.qs.SideLabelTileLayout) r5
            com.android.systemui.plugins.qs.QS r6 = r0.mQs
            android.view.View r6 = r6.getView()
            com.android.systemui.qs.PagedTileLayout r7 = r0.mPagedLayout
            java.util.List r7 = r7.getSpecsForPage(r1)
            boolean r8 = r7.isEmpty()
            if (r8 == 0) goto L_0x006c
            com.android.systemui.qs.QSPanelController r7 = r0.mQsPanelController
            com.android.systemui.qs.QSTileHost r7 = r7.getHost()
            java.util.ArrayList<java.lang.String> r7 = r7.mTileSpecs
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "Trying to create animators for empty page "
            r8.append(r9)
            r8.append(r1)
            java.lang.String r1 = ". Tiles: "
            r8.append(r1)
            r8.append(r7)
            java.lang.String r1 = r8.toString()
            java.lang.String r8 = "QSAnimator"
            android.util.Log.e(r8, r1)
        L_0x006c:
            r1 = -1
            r8 = 0
            r9 = r3
            r10 = r8
            r3 = r1
        L_0x0071:
            int r11 = r7.size()
            r12 = 2
            if (r10 >= r11) goto L_0x01ad
            com.android.systemui.qs.QSPanelController r11 = r0.mQsPanelController
            java.lang.Object r13 = r7.get(r10)
            java.lang.String r13 = (java.lang.String) r13
            com.android.systemui.plugins.qs.QSTileView r11 = r11.getTileView((java.lang.String) r13)
            int[] r13 = r0.mTmpLoc2
            r0.getRelativePosition(r13, r11, r6)
            int[] r13 = r0.mTmpLoc2
            r14 = 1
            r13 = r13[r14]
            int r15 = r0.mQQSTop
            int r16 = r5.getPhantomTopPosition(r10)
            int r15 = r15 + r16
            int r13 = r13 - r15
            float[] r15 = new float[r12]
            int r13 = -r13
            float r13 = (float) r13
            r15[r8] = r13
            r13 = 0
            r15[r14] = r13
            java.lang.String r13 = "translationY"
            r2.addFloat(r11, r13, r15)
            int r15 = r11.getMeasuredHeight()
            int r14 = r0.mLastQQSTileHeight
            int r15 = r15 - r14
            int r15 = r15 / r12
            com.android.systemui.plugins.qs.QSIconView r14 = r11.getIcon()
            float[] r8 = new float[r12]
            int r12 = -r15
            float r12 = (float) r12
            r17 = 0
            r8[r17] = r12
            r16 = 0
            r18 = 1
            r8[r18] = r16
            r2.addFloat(r14, r13, r8)
            android.view.View r8 = r11.getSecondaryIcon()
            r19 = r5
            r14 = 2
            float[] r5 = new float[r14]
            r5[r17] = r12
            r5[r18] = r16
            r2.addFloat(r8, r13, r5)
            android.view.View r5 = r11.getSecondaryLabel()
            int r5 = r5.getVisibility()
            if (r5 != 0) goto L_0x00e7
            android.view.View r5 = r11.getSecondaryLabel()
            int r5 = r5.getMeasuredHeight()
            int r5 = r5 / r14
            goto L_0x00e8
        L_0x00e7:
            r5 = 0
        L_0x00e8:
            int r15 = r15 - r5
            android.view.View r5 = r11.getLabelContainer()
            float[] r8 = new float[r14]
            int r12 = -r15
            float r12 = (float) r12
            r14 = 0
            r8[r14] = r12
            r12 = 0
            r15 = 1
            r8[r15] = r12
            r2.addFloat(r5, r13, r8)
            android.view.View r5 = r11.getSecondaryLabel()
            r8 = 3
            float[] r8 = new float[r8]
            r8 = {0, 1050253722, 1065353216} // fill-array
            java.lang.String r12 = "alpha"
            r2.addFloat(r5, r12, r8)
            android.view.View r5 = r11.getLabelContainer()
            r8 = 2
            float[] r13 = new float[r8]
            r13 = {0, 1065353216} // fill-array
            r4.addFloat(r5, r12, r13)
            com.android.systemui.plugins.qs.QSIconView r5 = r11.getIcon()
            float[] r13 = new float[r8]
            r13 = {0, 1065353216} // fill-array
            r4.addFloat(r5, r12, r13)
            android.view.View r5 = r11.getSecondaryIcon()
            float[] r13 = new float[r8]
            r13 = {0, 1065353216} // fill-array
            r4.addFloat(r5, r12, r13)
            int r5 = r11.getTop()
            if (r5 == r1) goto L_0x0138
            int r3 = r3 + 1
            r1 = r5
        L_0x0138:
            com.android.systemui.qs.QuickQSPanel r5 = r0.mQuickQsPanel
            com.android.systemui.qs.QSPanel$QSTileLayout r5 = r5.getTileLayout()
            int r5 = r5.getNumVisibleTiles()
            if (r10 < r5) goto L_0x0153
            r5 = 2
            if (r3 < r5) goto L_0x0154
            float[] r5 = new float[r3]
            int r8 = r3 + -1
            r13 = 1065353216(0x3f800000, float:1.0)
            r5[r8] = r13
            r2.addFloat(r11, r12, r5)
            goto L_0x015c
        L_0x0153:
            r5 = 2
        L_0x0154:
            float[] r5 = new float[r5]
            r5 = {1058642330, 1065353216} // fill-array
            r2.addFloat(r11, r12, r5)
        L_0x015c:
            if (r9 != 0) goto L_0x0173
            com.android.systemui.qs.QSAnimator$HeightExpansionAnimator r5 = new com.android.systemui.qs.QSAnimator$HeightExpansionAnimator
            int r8 = r0.mLastQQSTileHeight
            int r9 = r11.getMeasuredHeight()
            r5.<init>(r0, r8, r9)
            com.android.systemui.qs.QSExpansionPathInterpolator r8 = r0.mQSExpansionPathInterpolator
            android.view.animation.Interpolator r8 = r8.getYInterpolator()
            r5.setInterpolator(r8)
            r9 = r5
        L_0x0173:
            r9.addView(r11)
            r5 = 1
            r11.setClipChildren(r5)
            r11.setClipToPadding(r5)
            java.util.ArrayList<android.view.View> r5 = r0.mAllViews
            r5.add(r11)
            java.util.ArrayList<android.view.View> r5 = r0.mAllViews
            android.view.View r8 = r11.getSecondaryLabel()
            r5.add(r8)
            java.util.ArrayList<android.view.View> r5 = r0.mAllViews
            com.android.systemui.plugins.qs.QSIconView r8 = r11.getIcon()
            r5.add(r8)
            java.util.ArrayList<android.view.View> r5 = r0.mAllViews
            android.view.View r8 = r11.getSecondaryIcon()
            r5.add(r8)
            java.util.ArrayList<android.view.View> r5 = r0.mAllViews
            android.view.View r8 = r11.getLabelContainer()
            r5.add(r8)
            int r10 = r10 + 1
            r8 = r14
            r5 = r19
            goto L_0x0071
        L_0x01ad:
            com.android.systemui.qs.TouchAnimator r0 = r4.build()
            r1 = 2
            float[] r1 = new float[r1]
            r1 = {0, 1065353216} // fill-array
            java.lang.String r3 = "position"
            r2.addFloat(r0, r3, r1)
            android.util.Pair r0 = new android.util.Pair
            com.android.systemui.qs.TouchAnimator r1 = r2.build()
            r0.<init>(r9, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.QSAnimator.createSecondaryPageAnimators(int):android.util.Pair");
    }

    public final void animateBrightnessSlider(TouchAnimator.Builder builder) {
        View brightnessView = this.mQsPanelController.getBrightnessView();
        View brightnessView2 = this.mQuickQSPanelController.getBrightnessView();
        if (brightnessView2 != null && brightnessView2.getVisibility() == 0) {
            this.mAnimatedQsViews.add(brightnessView);
            this.mAllViews.add(brightnessView2);
            int relativeTranslationY = getRelativeTranslationY(brightnessView, brightnessView2);
            this.mBrightnessAnimator = new TouchAnimator.Builder().addFloat(brightnessView, "sliderScaleY", 0.3f, 1.0f).addFloat(brightnessView2, "translationY", 0.0f, (float) relativeTranslationY).build();
        } else if (brightnessView != null) {
            builder.addFloat(brightnessView, "translationY", ((float) brightnessView.getMeasuredHeight()) * 0.5f, 0.0f);
            this.mBrightnessAnimator = new TouchAnimator.Builder().addFloat(brightnessView, "alpha", 0.0f, 1.0f).addFloat(brightnessView, "sliderScaleY", 0.3f, 1.0f).setInterpolator(Interpolators.ALPHA_IN).setStartDelay(0.3f).build();
            this.mAllViews.add(brightnessView);
        } else {
            this.mBrightnessAnimator = null;
        }
    }

    public final int getRelativeTranslationY(View view, View view2) {
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        View view3 = this.mQs.getView();
        getRelativePositionInt(iArr, view, view3);
        getRelativePositionInt(iArr2, view2, view3);
        return (iArr[1] - iArr2[1]) - this.mQuickStatusBarHeader.getOffsetTranslation();
    }

    public final boolean isIconInAnimatedRow(int i) {
        PagedTileLayout pagedTileLayout = this.mPagedLayout;
        if (pagedTileLayout == null) {
            return false;
        }
        int columnCount = pagedTileLayout.getColumnCount();
        if (i < (((this.mNumQuickTiles + columnCount) - 1) / columnCount) * columnCount) {
            return true;
        }
        return false;
    }

    public final void getRelativePosition(int[] iArr, View view, View view2) {
        iArr[0] = (view.getWidth() / 2) + 0;
        iArr[1] = 0;
        getRelativePositionInt(iArr, view, view2);
    }

    public final void getRelativePositionInt(int[] iArr, View view, View view2) {
        if (view != view2 && view != null) {
            if (!isAPage(view)) {
                iArr[0] = iArr[0] + view.getLeft();
                iArr[1] = iArr[1] + view.getTop();
            }
            if (!(view instanceof PagedTileLayout)) {
                iArr[0] = iArr[0] - view.getScrollX();
                iArr[1] = iArr[1] - view.getScrollY();
            }
            getRelativePositionInt(iArr, (View) view.getParent(), view2);
        }
    }

    public final boolean isAPage(View view) {
        return view.getClass().equals(SideLabelTileLayout.class);
    }

    public void setPosition(float f) {
        if (this.mNeedsAnimatorUpdate) {
            updateAnimators();
        }
        if (this.mFirstPageAnimator != null) {
            if (this.mOnKeyguard) {
                f = this.mShowCollapsedOnKeyguard ? 0.0f : 1.0f;
            }
            this.mLastPosition = f;
            if (this.mAllowFancy) {
                if (this.mOnFirstPage) {
                    this.mQuickQsPanel.setAlpha(1.0f);
                    this.mFirstPageAnimator.setPosition(f);
                    this.mTranslationYAnimator.setPosition(f);
                    this.mTranslationXAnimator.setPosition(f);
                    HeightExpansionAnimator heightExpansionAnimator = this.mOtherFirstPageTilesHeightAnimator;
                    if (heightExpansionAnimator != null) {
                        heightExpansionAnimator.setPosition(f);
                    }
                } else {
                    this.mNonfirstPageAlphaAnimator.setPosition(f);
                }
                for (int i = 0; i < this.mNonFirstPageQSAnimators.size(); i++) {
                    Pair valueAt = this.mNonFirstPageQSAnimators.valueAt(i);
                    if (valueAt != null) {
                        ((HeightExpansionAnimator) valueAt.first).setPosition(f);
                        ((TouchAnimator) valueAt.second).setPosition(f);
                    }
                }
                HeightExpansionAnimator heightExpansionAnimator2 = this.mQQSTileHeightAnimator;
                if (heightExpansionAnimator2 != null) {
                    heightExpansionAnimator2.setPosition(f);
                }
                this.mQSTileLayoutTranslatorAnimator.setPosition(f);
                this.mQQSTranslationYAnimator.setPosition(f);
                this.mAllPagesDelayedAnimator.setPosition(f);
                TouchAnimator touchAnimator = this.mBrightnessAnimator;
                if (touchAnimator != null) {
                    touchAnimator.setPosition(f);
                }
                TouchAnimator touchAnimator2 = this.mQQSFooterActionsAnimator;
                if (touchAnimator2 != null) {
                    touchAnimator2.setPosition(f);
                }
            }
        }
    }

    public void onAnimationAtStart() {
        this.mQuickQsPanel.setVisibility(0);
    }

    public void onAnimationAtEnd() {
        this.mQuickQsPanel.setVisibility(4);
        int size = this.mAnimatedQsViews.size();
        for (int i = 0; i < size; i++) {
            this.mAnimatedQsViews.get(i).setVisibility(0);
        }
    }

    public void onAnimationStarted() {
        updateQQSVisibility();
        if (this.mOnFirstPage) {
            int size = this.mAnimatedQsViews.size();
            for (int i = 0; i < size; i++) {
                this.mAnimatedQsViews.get(i).setVisibility(4);
            }
        }
    }

    public final void clearAnimationState() {
        int size = this.mAllViews.size();
        this.mQuickQsPanel.setAlpha(0.0f);
        for (int i = 0; i < size; i++) {
            View view = this.mAllViews.get(i);
            view.setAlpha(1.0f);
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
            view.setScaleY(1.0f);
            if (view instanceof SideLabelTileLayout) {
                SideLabelTileLayout sideLabelTileLayout = (SideLabelTileLayout) view;
                sideLabelTileLayout.setClipChildren(false);
                sideLabelTileLayout.setClipToPadding(false);
            }
        }
        HeightExpansionAnimator heightExpansionAnimator = this.mQQSTileHeightAnimator;
        if (heightExpansionAnimator != null) {
            heightExpansionAnimator.resetViewsHeights();
        }
        HeightExpansionAnimator heightExpansionAnimator2 = this.mOtherFirstPageTilesHeightAnimator;
        if (heightExpansionAnimator2 != null) {
            heightExpansionAnimator2.resetViewsHeights();
        }
        for (int i2 = 0; i2 < this.mNonFirstPageQSAnimators.size(); i2++) {
            ((HeightExpansionAnimator) this.mNonFirstPageQSAnimators.valueAt(i2).first).resetViewsHeights();
        }
        int size2 = this.mAnimatedQsViews.size();
        for (int i3 = 0; i3 < size2; i3++) {
            this.mAnimatedQsViews.get(i3).setVisibility(0);
        }
    }

    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if ((i == i5 && i2 == i6 && i3 == i7 && i4 == i8) ? false : true) {
            this.mExecutor.execute(this.mUpdateAnimators);
        }
    }

    public void onTilesChanged() {
        this.mExecutor.execute(this.mUpdateAnimators);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateAnimators();
        setCurrentPosition();
    }

    public void setTranslateWhileExpanding(boolean z) {
        this.mTranslateWhileExpanding = z;
    }

    public static class HeightExpansionAnimator {
        public final ValueAnimator mAnimator;
        public final TouchAnimator.Listener mListener;
        public final ValueAnimator.AnimatorUpdateListener mUpdateListener;
        public final List<View> mViews = new ArrayList();

        public HeightExpansionAnimator(TouchAnimator.Listener listener, int i, int i2) {
            AnonymousClass1 r0 = new ValueAnimator.AnimatorUpdateListener() {
                public float mLastT = -1.0f;

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    int size = HeightExpansionAnimator.this.mViews.size();
                    int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    for (int i = 0; i < size; i++) {
                        View view = (View) HeightExpansionAnimator.this.mViews.get(i);
                        if (view instanceof HeightOverrideable) {
                            ((HeightOverrideable) view).setHeightOverride(intValue);
                        } else {
                            view.setBottom(view.getTop() + intValue);
                        }
                    }
                    if (animatedFraction == 0.0f) {
                        HeightExpansionAnimator.this.mListener.onAnimationAtStart();
                    } else if (animatedFraction == 1.0f) {
                        HeightExpansionAnimator.this.mListener.onAnimationAtEnd();
                    } else {
                        float f = this.mLastT;
                        if (f <= 0.0f || f == 1.0f) {
                            HeightExpansionAnimator.this.mListener.onAnimationStarted();
                        }
                    }
                    this.mLastT = animatedFraction;
                }
            };
            this.mUpdateListener = r0;
            this.mListener = listener;
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{i, i2});
            this.mAnimator = ofInt;
            ofInt.setRepeatCount(-1);
            ofInt.setRepeatMode(2);
            ofInt.addUpdateListener(r0);
        }

        public void addView(View view) {
            this.mViews.add(view);
        }

        public void setInterpolator(TimeInterpolator timeInterpolator) {
            this.mAnimator.setInterpolator(timeInterpolator);
        }

        public void setPosition(float f) {
            this.mAnimator.setCurrentFraction(f);
        }

        public void resetViewsHeights() {
            int size = this.mViews.size();
            for (int i = 0; i < size; i++) {
                View view = this.mViews.get(i);
                if (view instanceof HeightOverrideable) {
                    ((HeightOverrideable) view).resetOverride();
                } else {
                    view.setBottom(view.getTop() + view.getMeasuredHeight());
                }
            }
        }
    }
}
