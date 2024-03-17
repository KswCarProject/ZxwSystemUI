package com.android.wm.shell.pip.tv;

import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Annotation;
import android.text.SpannableString;
import android.text.SpannedString;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.wm.shell.R;
import com.android.wm.shell.pip.PipUtils;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TvPipMenuView extends FrameLayout implements View.OnClickListener {
    public final LinearLayout mActionButtonsContainer;
    public final List<TvPipMenuActionButton> mAdditionalButtons;
    public final ImageView mArrowDown;
    public final ImageView mArrowLeft;
    public final ImageView mArrowRight;
    public final ImageView mArrowUp;
    public boolean mButtonMenuIsVisible;
    public final TvPipMenuActionButton mCloseButton;
    public Rect mCurrentPipBounds;
    public final View mEduTextContainerView;
    public final int mEduTextFadeExitAnimationDurationMs;
    public int mEduTextHeight;
    public final int mEduTextSlideExitAnimationDurationMs;
    public final TextView mEduTextView;
    public final TvPipMenuActionButton mExpandButton;
    public View mFocusedButton;
    public final HorizontalScrollView mHorizontalScrollView;
    public Listener mListener;
    public final View mMenuFrameView;
    public boolean mMoveMenuIsVisible;
    public final View mPipFrameView;
    public final int mPipMenuBorderWidth;
    public final int mPipMenuFadeAnimationDuration;
    public final int mPipMenuOuterSpace;
    public final View mPipView;
    public final int mResizeAnimationDuration;
    public final ScrollView mScrollView;
    public boolean mSwitchingOrientation;

    public interface Listener {
        void onBackPress();

        void onCloseButtonClick();

        void onEnterMoveMode();

        boolean onExitMoveMode();

        void onFullscreenButtonClick();

        boolean onPipMovement(int i);

        void onToggleExpandedMode();
    }

    public final boolean checkGravity(int i, int i2) {
        return (i & i2) == i2;
    }

    public TvPipMenuView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TvPipMenuView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TvPipMenuView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public TvPipMenuView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mAdditionalButtons = new ArrayList();
        FrameLayout.inflate(context, R.layout.tv_pip_menu, this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.tv_pip_menu_action_buttons);
        this.mActionButtonsContainer = linearLayout;
        linearLayout.findViewById(R.id.tv_pip_menu_fullscreen_button).setOnClickListener(this);
        TvPipMenuActionButton tvPipMenuActionButton = (TvPipMenuActionButton) linearLayout.findViewById(R.id.tv_pip_menu_close_button);
        this.mCloseButton = tvPipMenuActionButton;
        tvPipMenuActionButton.setOnClickListener(this);
        tvPipMenuActionButton.setIsCustomCloseAction(true);
        linearLayout.findViewById(R.id.tv_pip_menu_move_button).setOnClickListener(this);
        TvPipMenuActionButton tvPipMenuActionButton2 = (TvPipMenuActionButton) findViewById(R.id.tv_pip_menu_expand_button);
        this.mExpandButton = tvPipMenuActionButton2;
        tvPipMenuActionButton2.setOnClickListener(this);
        this.mScrollView = (ScrollView) findViewById(R.id.tv_pip_menu_scroll);
        this.mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.tv_pip_menu_horizontal_scroll);
        this.mMenuFrameView = findViewById(R.id.tv_pip_menu_frame);
        this.mPipFrameView = findViewById(R.id.tv_pip_border);
        this.mPipView = findViewById(R.id.tv_pip);
        this.mArrowUp = (ImageView) findViewById(R.id.tv_pip_menu_arrow_up);
        this.mArrowRight = (ImageView) findViewById(R.id.tv_pip_menu_arrow_right);
        this.mArrowDown = (ImageView) findViewById(R.id.tv_pip_menu_arrow_down);
        this.mArrowLeft = (ImageView) findViewById(R.id.tv_pip_menu_arrow_left);
        this.mEduTextView = (TextView) findViewById(R.id.tv_pip_menu_edu_text);
        this.mEduTextContainerView = findViewById(R.id.tv_pip_menu_edu_text_container);
        this.mResizeAnimationDuration = context.getResources().getInteger(R.integer.config_pipResizeAnimationDuration);
        this.mPipMenuFadeAnimationDuration = context.getResources().getInteger(R.integer.pip_menu_fade_animation_duration);
        this.mPipMenuOuterSpace = context.getResources().getDimensionPixelSize(R.dimen.pip_menu_outer_space);
        this.mPipMenuBorderWidth = context.getResources().getDimensionPixelSize(R.dimen.pip_menu_border_width);
        this.mEduTextHeight = context.getResources().getDimensionPixelSize(R.dimen.pip_menu_edu_text_view_height);
        this.mEduTextFadeExitAnimationDurationMs = context.getResources().getInteger(R.integer.pip_edu_text_view_exit_animation_duration_ms);
        this.mEduTextSlideExitAnimationDurationMs = context.getResources().getInteger(R.integer.pip_edu_text_window_exit_animation_duration_ms);
        initEduText();
    }

    public void initEduText() {
        SpannedString spannedString = (SpannedString) getResources().getText(R.string.pip_edu_text);
        SpannableString spannableString = new SpannableString(spannedString);
        Arrays.stream((Annotation[]) spannedString.getSpans(0, spannedString.length(), Annotation.class)).findFirst().ifPresent(new TvPipMenuView$$ExternalSyntheticLambda0(this, spannableString, spannedString));
        this.mEduTextView.setText(spannableString);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initEduText$0(SpannableString spannableString, SpannedString spannedString, Annotation annotation) {
        Drawable drawable = getResources().getDrawable(R.drawable.home_icon, this.mContext.getTheme());
        if (drawable != null) {
            drawable.mutate();
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            spannableString.setSpan(new CenteredImageSpan(drawable), spannedString.getSpanStart(annotation), spannedString.getSpanEnd(annotation), 33);
        }
    }

    public void setEduTextActive(boolean z) {
        this.mEduTextView.setSelected(z);
    }

    public void hideEduText() {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.mEduTextHeight, 0});
        ofInt.setDuration((long) this.mEduTextSlideExitAnimationDurationMs);
        ofInt.setInterpolator(TvPipInterpolators.BROWSE);
        ofInt.addUpdateListener(new TvPipMenuView$$ExternalSyntheticLambda1(this));
        this.mEduTextView.animate().alpha(0.0f).setInterpolator(TvPipInterpolators.EXIT).setDuration((long) this.mEduTextFadeExitAnimationDurationMs).withEndAction(new TvPipMenuView$$ExternalSyntheticLambda2(this)).start();
        ofInt.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$hideEduText$1(ValueAnimator valueAnimator) {
        this.mEduTextHeight = ((Integer) valueAnimator.getAnimatedValue()).intValue();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$hideEduText$2() {
        this.mEduTextContainerView.setVisibility(8);
    }

    public void onPipTransitionStarted(Rect rect) {
        Rect rect2 = this.mCurrentPipBounds;
        if (!(rect2 == null || rect == null || !PipUtils.aspectRatioChanged(((float) rect2.width()) / ((float) this.mCurrentPipBounds.height()), ((float) rect.width()) / ((float) rect.height())))) {
            this.mPipView.animate().alpha(1.0f).setInterpolator(TvPipInterpolators.EXIT).setDuration((long) (this.mResizeAnimationDuration / 2)).start();
        }
        boolean z = (rect.height() > rect.width()) != (this.mActionButtonsContainer.getOrientation() == 1);
        if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -205253086, 12, (String) null, "TvPipMenuView", Boolean.valueOf(z));
        }
        if (z) {
            if (this.mButtonMenuIsVisible) {
                this.mSwitchingOrientation = true;
                this.mActionButtonsContainer.animate().alpha(0.0f).setInterpolator(TvPipInterpolators.EXIT).setDuration((long) (this.mResizeAnimationDuration / 2)).withEndAction(new TvPipMenuView$$ExternalSyntheticLambda5(this, rect));
                return;
            }
            changeButtonScrollOrientation(rect);
            updateButtonGravity(rect);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onPipTransitionStarted$3(Rect rect) {
        changeButtonScrollOrientation(rect);
        updateButtonGravity(rect);
    }

    public void onPipTransitionFinished() {
        if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1855178708, 0, (String) null, "TvPipMenuView");
        }
        ViewPropertyAnimator duration = this.mPipView.animate().alpha(0.0f).setDuration((long) (this.mResizeAnimationDuration / 2));
        Interpolator interpolator = TvPipInterpolators.ENTER;
        duration.setInterpolator(interpolator).start();
        if (this.mSwitchingOrientation) {
            this.mActionButtonsContainer.animate().alpha(1.0f).setInterpolator(interpolator).setDuration((long) (this.mResizeAnimationDuration / 2));
        } else {
            refocusPreviousButton();
        }
        this.mSwitchingOrientation = false;
    }

    public void updateBounds(Rect rect) {
        if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
            String valueOf = String.valueOf(rect.width());
            String valueOf2 = String.valueOf(rect.height());
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -408772810, 0, (String) null, "TvPipMenuView", valueOf, valueOf2);
        }
        this.mCurrentPipBounds = rect;
        if (!this.mSwitchingOrientation) {
            updateButtonGravity(rect);
        }
        updatePipFrameBounds();
    }

    public final void changeButtonScrollOrientation(Rect rect) {
        int i = rect.height() > rect.width() ? 1 : 0;
        ViewGroup viewGroup = i != 0 ? this.mHorizontalScrollView : this.mScrollView;
        ViewGroup viewGroup2 = i != 0 ? this.mScrollView : this.mHorizontalScrollView;
        if (viewGroup.getChildCount() == 1) {
            if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 2009175096, 0, (String) null, "TvPipMenuView");
            }
            viewGroup.removeView(this.mActionButtonsContainer);
            viewGroup.setVisibility(8);
            this.mActionButtonsContainer.setOrientation(i);
            viewGroup2.addView(this.mActionButtonsContainer);
            viewGroup2.setVisibility(0);
            View view = this.mFocusedButton;
            if (view != null) {
                view.requestFocus();
            }
        }
    }

    public final void updateButtonGravity(Rect rect) {
        boolean z = rect.height() > rect.width();
        int max = Math.max(this.mActionButtonsContainer.getHeight(), this.mActionButtonsContainer.getWidth());
        if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1698307807, 0, (String) null, "TvPipMenuView", String.valueOf(this.mActionButtonsContainer.getWidth()), String.valueOf(this.mActionButtonsContainer.getHeight()));
        }
        boolean z2 = !z ? max < rect.width() : max < rect.height();
        int i = z2 ? 17 : z ? 1 : 16;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mActionButtonsContainer.getLayoutParams();
        layoutParams.gravity = i;
        this.mActionButtonsContainer.setLayoutParams(layoutParams);
        if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -824684158, 60, (String) null, "TvPipMenuView", Boolean.valueOf(z), Boolean.valueOf(z2), String.valueOf(Gravity.toString(i)));
        }
    }

    public final void refocusPreviousButton() {
        Rect rect;
        if (!this.mMoveMenuIsVisible && (rect = this.mCurrentPipBounds) != null && this.mFocusedButton != null) {
            boolean z = rect.height() > this.mCurrentPipBounds.width();
            if (!this.mFocusedButton.hasFocus()) {
                if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 560551991, 0, (String) null, "TvPipMenuView", String.valueOf(this.mFocusedButton));
                }
                this.mFocusedButton.requestFocus();
            } else if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, -521298281, 0, (String) null, "TvPipMenuView", String.valueOf(this.mFocusedButton));
            }
            Rect rect2 = new Rect();
            Rect rect3 = new Rect();
            if (z) {
                this.mScrollView.getDrawingRect(rect3);
            } else {
                this.mHorizontalScrollView.getDrawingRect(rect3);
            }
            this.mFocusedButton.getHitRect(rect2);
            if (!rect3.contains(rect2)) {
                if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 2090322089, 0, (String) null, "TvPipMenuView");
                }
                if (z) {
                    this.mScrollView.smoothScrollTo((int) this.mFocusedButton.getX(), (int) this.mFocusedButton.getY());
                } else {
                    this.mHorizontalScrollView.smoothScrollTo((int) this.mFocusedButton.getX(), (int) this.mFocusedButton.getY());
                }
            } else if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 557294652, 0, (String) null, "TvPipMenuView");
            }
        }
    }

    public Rect getPipMenuContainerBounds(Rect rect) {
        Rect rect2 = new Rect(rect);
        int i = this.mPipMenuOuterSpace;
        rect2.inset(-i, -i);
        rect2.bottom += this.mEduTextHeight;
        return rect2;
    }

    public final void updatePipFrameBounds() {
        ViewGroup.LayoutParams layoutParams = this.mPipFrameView.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = this.mCurrentPipBounds.width() + (this.mPipMenuBorderWidth * 2);
            layoutParams.height = this.mCurrentPipBounds.height() + (this.mPipMenuBorderWidth * 2);
            this.mPipFrameView.setLayoutParams(layoutParams);
        }
        ViewGroup.LayoutParams layoutParams2 = this.mPipView.getLayoutParams();
        if (layoutParams2 != null) {
            layoutParams2.width = this.mCurrentPipBounds.width();
            layoutParams2.height = this.mCurrentPipBounds.height();
            this.mPipView.setLayoutParams(layoutParams2);
        }
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public void setExpandedModeEnabled(boolean z) {
        this.mExpandButton.setVisibility(z ? 0 : 8);
    }

    public void setIsExpanded(boolean z) {
        this.mExpandButton.setImageResource(z ? R.drawable.pip_ic_collapse : R.drawable.pip_ic_expand);
        this.mExpandButton.setTextAndDescription(z ? R.string.pip_collapse : R.string.pip_expand);
    }

    public void showMoveMenu(int i) {
        this.mButtonMenuIsVisible = false;
        this.mMoveMenuIsVisible = true;
        showButtonsMenu(false);
        showMovementHints(i);
        setFrameHighlighted(true);
    }

    public void showButtonsMenu() {
        this.mButtonMenuIsVisible = true;
        this.mMoveMenuIsVisible = false;
        showButtonsMenu(true);
        hideMovementHints();
        setFrameHighlighted(true);
        if (this.mFocusedButton == null) {
            this.mFocusedButton = this.mActionButtonsContainer.getChildAt(1);
            this.mScrollView.scrollTo(0, 0);
            this.mHorizontalScrollView.scrollTo(isLayoutRtl() ? this.mActionButtonsContainer.getWidth() : 0, 0);
        }
        refocusPreviousButton();
    }

    public void hideAllUserControls() {
        if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
            ShellProtoLogImpl.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 2076292245, 0, (String) null, "TvPipMenuView");
        }
        this.mFocusedButton = null;
        this.mButtonMenuIsVisible = false;
        this.mMoveMenuIsVisible = false;
        showButtonsMenu(false);
        hideMovementHints();
        setFrameHighlighted(false);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (!z) {
            hideAllUserControls();
        }
    }

    public final void animateAlphaTo(float f, View view) {
        if (view.getAlpha() != f) {
            view.animate().alpha(f).setInterpolator(f == 0.0f ? TvPipInterpolators.EXIT : TvPipInterpolators.ENTER).setDuration((long) this.mPipMenuFadeAnimationDuration).withStartAction(new TvPipMenuView$$ExternalSyntheticLambda3(f, view)).withEndAction(new TvPipMenuView$$ExternalSyntheticLambda4(f, view));
        }
    }

    public static /* synthetic */ void lambda$animateAlphaTo$4(float f, View view) {
        if (f != 0.0f) {
            view.setVisibility(0);
        }
    }

    public static /* synthetic */ void lambda$animateAlphaTo$5(float f, View view) {
        if (f == 0.0f) {
            view.setVisibility(8);
        }
    }

    public void setAdditionalActions(List<RemoteAction> list, RemoteAction remoteAction, Handler handler) {
        if (remoteAction != null) {
            setActionForButton(remoteAction, this.mCloseButton, handler);
        } else {
            this.mCloseButton.setTextAndDescription(R.string.pip_close);
            this.mCloseButton.setImageResource(R.drawable.pip_ic_close_white);
        }
        this.mCloseButton.setIsCustomCloseAction(remoteAction != null);
        this.mCloseButton.setEnabled(true);
        int size = list.size();
        int size2 = this.mAdditionalButtons.size();
        if (size > size2) {
            while (size > size2) {
                TvPipMenuActionButton tvPipMenuActionButton = new TvPipMenuActionButton(this.mContext);
                tvPipMenuActionButton.setOnClickListener(this);
                this.mActionButtonsContainer.addView(tvPipMenuActionButton, size2 + 3);
                this.mAdditionalButtons.add(tvPipMenuActionButton);
                size2++;
            }
        } else if (size < size2) {
            while (size < size2) {
                View view = this.mAdditionalButtons.get(size2 - 1);
                view.setVisibility(8);
                view.setTag((Object) null);
                size2--;
            }
        }
        for (int i = 0; i < size; i++) {
            RemoteAction remoteAction2 = list.get(i);
            TvPipMenuActionButton tvPipMenuActionButton2 = this.mAdditionalButtons.get(i);
            if (PipUtils.remoteActionsMatch(remoteAction2, remoteAction)) {
                tvPipMenuActionButton2.setVisibility(8);
            } else {
                setActionForButton(remoteAction2, tvPipMenuActionButton2, handler);
            }
        }
        Rect rect = this.mCurrentPipBounds;
        if (rect != null) {
            updateButtonGravity(rect);
            refocusPreviousButton();
        }
    }

    public final void setActionForButton(RemoteAction remoteAction, TvPipMenuActionButton tvPipMenuActionButton, Handler handler) {
        tvPipMenuActionButton.setVisibility(0);
        if (remoteAction.getContentDescription().length() > 0) {
            tvPipMenuActionButton.setTextAndDescription(remoteAction.getContentDescription());
        } else {
            tvPipMenuActionButton.setTextAndDescription(remoteAction.getTitle());
        }
        tvPipMenuActionButton.setEnabled(remoteAction.isEnabled());
        tvPipMenuActionButton.setTag(remoteAction);
        remoteAction.getIcon().loadDrawableAsync(this.mContext, new TvPipMenuView$$ExternalSyntheticLambda6(tvPipMenuActionButton), handler);
    }

    public void onClick(View view) {
        if (this.mListener != null) {
            int id = view.getId();
            if (id == R.id.tv_pip_menu_fullscreen_button) {
                this.mListener.onFullscreenButtonClick();
            } else if (id == R.id.tv_pip_menu_move_button) {
                this.mListener.onEnterMoveMode();
            } else if (id == R.id.tv_pip_menu_close_button) {
                this.mListener.onCloseButtonClick();
            } else if (id == R.id.tv_pip_menu_expand_button) {
                this.mListener.onToggleExpandedMode();
            } else {
                RemoteAction remoteAction = (RemoteAction) view.getTag();
                if (remoteAction != null) {
                    try {
                        remoteAction.getActionIntent().send();
                    } catch (PendingIntent.CanceledException e) {
                        if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                            String valueOf = String.valueOf(e);
                            ShellProtoLogImpl.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1855434335, 0, (String) null, "TvPipMenuView", valueOf);
                        }
                    }
                } else if (ShellProtoLogCache.WM_SHELL_PICTURE_IN_PICTURE_enabled) {
                    ShellProtoLogImpl.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, 1408810920, 0, (String) null, "TvPipMenuView");
                }
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (this.mListener != null && keyEvent.getAction() == 1) {
            if (!this.mMoveMenuIsVisible) {
                this.mFocusedButton = this.mActionButtonsContainer.getFocusedChild();
            }
            int keyCode = keyEvent.getKeyCode();
            if (keyCode != 4) {
                if (keyCode != 66) {
                    switch (keyCode) {
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                            if (this.mListener.onPipMovement(keyEvent.getKeyCode()) || super.dispatchKeyEvent(keyEvent)) {
                                return true;
                            }
                            return false;
                        case 23:
                            break;
                    }
                }
                if (this.mListener.onExitMoveMode() || super.dispatchKeyEvent(keyEvent)) {
                    return true;
                }
                return false;
            }
            this.mListener.onBackPress();
            return true;
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    public void showMovementHints(int i) {
        float f = 1.0f;
        animateAlphaTo(checkGravity(i, 80) ? 1.0f : 0.0f, this.mArrowUp);
        animateAlphaTo(checkGravity(i, 48) ? 1.0f : 0.0f, this.mArrowDown);
        animateAlphaTo(checkGravity(i, 5) ? 1.0f : 0.0f, this.mArrowLeft);
        if (!checkGravity(i, 3)) {
            f = 0.0f;
        }
        animateAlphaTo(f, this.mArrowRight);
    }

    public void hideMovementHints() {
        animateAlphaTo(0.0f, this.mArrowUp);
        animateAlphaTo(0.0f, this.mArrowRight);
        animateAlphaTo(0.0f, this.mArrowDown);
        animateAlphaTo(0.0f, this.mArrowLeft);
    }

    public void showButtonsMenu(boolean z) {
        if (z) {
            this.mActionButtonsContainer.setVisibility(0);
            refocusPreviousButton();
        }
        animateAlphaTo(z ? 1.0f : 0.0f, this.mActionButtonsContainer);
    }

    public final void setFrameHighlighted(boolean z) {
        this.mMenuFrameView.setActivated(z);
    }
}
