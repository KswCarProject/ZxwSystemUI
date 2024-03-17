package com.android.systemui.volume;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.media.AudioSystem;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.Trace;
import android.os.VibrationEffect;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewPropertyAnimator;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.graphics.drawable.BackgroundBlurDrawable;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.settingslib.Utils;
import com.android.settingslib.volume.Util;
import com.android.systemui.Prefs;
import com.android.systemui.R$bool;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.AlphaTintDrawableWrapper;
import com.android.systemui.util.RoundedCornerProgressDrawable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class VolumeDialogImpl implements VolumeDialog, ConfigurationController.ConfigurationListener, ViewTreeObserver.OnComputeInternalInsetsListener {
    public static final String TAG = Util.logTag(VolumeDialogImpl.class);
    public final Accessibility mAccessibility = new Accessibility();
    public final AccessibilityManagerWrapper mAccessibilityMgr;
    public int mActiveStream;
    public final ActivityManager mActivityManager;
    public final ActivityStarter mActivityStarter;
    public final ValueAnimator mAnimateUpBackgroundToMatchDrawer = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
    public boolean mAutomute = true;
    public final boolean mChangeVolumeRowTintWhenInactive;
    public boolean mConfigChanged = false;
    public ConfigurableTexts mConfigurableTexts;
    public final ConfigurationController mConfigurationController;
    public final Context mContext;
    public final VolumeDialogController mController;
    public final VolumeDialogController.Callbacks mControllerCallbackH = new VolumeDialogController.Callbacks() {
        public void onShowRequested(int i, boolean z, int i2) {
            VolumeDialogImpl.this.showH(i, z, i2);
        }

        public void onDismissRequested(int i) {
            VolumeDialogImpl.this.dismissH(i);
        }

        public void onScreenOff() {
            VolumeDialogImpl.this.dismissH(4);
        }

        public void onStateChanged(VolumeDialogController.State state) {
            VolumeDialogImpl.this.onStateChangedH(state);
        }

        public void onLayoutDirectionChanged(int i) {
            VolumeDialogImpl.this.mDialogView.setLayoutDirection(i);
        }

        public void onConfigurationChanged() {
            VolumeDialogImpl.this.mDialog.dismiss();
            VolumeDialogImpl.this.mConfigChanged = true;
        }

        public void onShowVibrateHint() {
            if (VolumeDialogImpl.this.mSilentMode) {
                VolumeDialogImpl.this.mController.setRingerMode(0, false);
            }
        }

        public void onShowSilentHint() {
            if (VolumeDialogImpl.this.mSilentMode) {
                VolumeDialogImpl.this.mController.setRingerMode(2, false);
            }
        }

        public void onShowSafetyWarning(int i) {
            VolumeDialogImpl.this.showSafetyWarningH(i);
        }

        public void onAccessibilityModeChanged(Boolean bool) {
            VolumeDialogImpl.this.mShowA11yStream = bool == null ? false : bool.booleanValue();
            VolumeRow r3 = VolumeDialogImpl.this.getActiveRow();
            if (VolumeDialogImpl.this.mShowA11yStream || 10 != r3.stream) {
                VolumeDialogImpl.this.updateRowsH(r3);
            } else {
                VolumeDialogImpl.this.dismissH(7);
            }
        }

        public void onCaptionComponentStateChanged(Boolean bool, Boolean bool2) {
            VolumeDialogImpl.this.updateODICaptionsH(bool.booleanValue(), bool2.booleanValue());
        }
    };
    public Consumer<Boolean> mCrossWindowBlurEnabledListener;
    public final DeviceProvisionedController mDeviceProvisionedController;
    public CustomDialog mDialog;
    public int mDialogCornerRadius;
    public final int mDialogHideAnimationDurationMs;
    public ViewGroup mDialogRowsView;
    public BackgroundBlurDrawable mDialogRowsViewBackground;
    public ViewGroup mDialogRowsViewContainer;
    public final int mDialogShowAnimationDurationMs;
    public ViewGroup mDialogView;
    public int mDialogWidth;
    public final SparseBooleanArray mDynamic = new SparseBooleanArray();
    public final H mHandler = new H();
    public boolean mHasSeenODICaptionsTooltip;
    public boolean mHovering = false;
    public final InteractionJankMonitor mInteractionJankMonitor;
    public boolean mIsAnimatingDismiss = false;
    public boolean mIsRingerDrawerOpen = false;
    public final KeyguardManager mKeyguard;
    public final MediaOutputDialogFactory mMediaOutputDialogFactory;
    public CaptionsToggleImageButton mODICaptionsIcon;
    public View mODICaptionsTooltipView = null;
    public ViewStub mODICaptionsTooltipViewStub;
    public ViewGroup mODICaptionsView;
    public int mPrevActiveStream;
    public ViewGroup mRinger;
    public View mRingerAndDrawerContainer;
    public Drawable mRingerAndDrawerContainerBackground;
    public int mRingerCount;
    public float mRingerDrawerClosedAmount = 1.0f;
    public ViewGroup mRingerDrawerContainer;
    public ImageView mRingerDrawerIconAnimatingDeselected;
    public ImageView mRingerDrawerIconAnimatingSelected;
    public final ValueAnimator mRingerDrawerIconColorAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
    public int mRingerDrawerItemSize;
    public ViewGroup mRingerDrawerMute;
    public ImageView mRingerDrawerMuteIcon;
    public ViewGroup mRingerDrawerNewSelectionBg;
    public ViewGroup mRingerDrawerNormal;
    public ImageView mRingerDrawerNormalIcon;
    public ViewGroup mRingerDrawerVibrate;
    public ImageView mRingerDrawerVibrateIcon;
    public ImageButton mRingerIcon;
    public int mRingerRowsPadding;
    public final List<VolumeRow> mRows = new ArrayList();
    public SafetyWarningDialog mSafetyWarning;
    public final Object mSafetyWarningLock = new Object();
    public ViewGroup mSelectedRingerContainer;
    public ImageView mSelectedRingerIcon;
    public ImageButton mSettingsIcon;
    public View mSettingsView;
    public boolean mShowA11yStream;
    public final boolean mShowActiveStreamOnly;
    public final boolean mShowLowMediaVolumeIcon;
    public boolean mShowVibrate;
    public boolean mShowing;
    public boolean mSilentMode = true;
    public VolumeDialogController.State mState;
    public View mTopContainer;
    public final Region mTouchableRegion = new Region();
    public final boolean mUseBackgroundBlur;
    public Window mWindow;
    public FrameLayout mZenIcon;

    public VolumeDialogImpl(Context context, VolumeDialogController volumeDialogController, AccessibilityManagerWrapper accessibilityManagerWrapper, DeviceProvisionedController deviceProvisionedController, ConfigurationController configurationController, MediaOutputDialogFactory mediaOutputDialogFactory, ActivityStarter activityStarter, InteractionJankMonitor interactionJankMonitor) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R$style.volume_dialog_theme);
        this.mContext = contextThemeWrapper;
        this.mController = volumeDialogController;
        this.mKeyguard = (KeyguardManager) contextThemeWrapper.getSystemService("keyguard");
        this.mActivityManager = (ActivityManager) contextThemeWrapper.getSystemService("activity");
        this.mAccessibilityMgr = accessibilityManagerWrapper;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mConfigurationController = configurationController;
        this.mMediaOutputDialogFactory = mediaOutputDialogFactory;
        this.mActivityStarter = activityStarter;
        this.mShowActiveStreamOnly = showActiveStreamOnly();
        this.mHasSeenODICaptionsTooltip = Prefs.getBoolean(context, "HasSeenODICaptionsTooltip", false);
        this.mShowLowMediaVolumeIcon = contextThemeWrapper.getResources().getBoolean(R$bool.config_showLowMediaVolumeIcon);
        this.mChangeVolumeRowTintWhenInactive = contextThemeWrapper.getResources().getBoolean(R$bool.config_changeVolumeRowTintWhenInactive);
        this.mDialogShowAnimationDurationMs = contextThemeWrapper.getResources().getInteger(R$integer.config_dialogShowAnimationDurationMs);
        this.mDialogHideAnimationDurationMs = contextThemeWrapper.getResources().getInteger(R$integer.config_dialogHideAnimationDurationMs);
        boolean z = contextThemeWrapper.getResources().getBoolean(R$bool.config_volumeDialogUseBackgroundBlur);
        this.mUseBackgroundBlur = z;
        this.mInteractionJankMonitor = interactionJankMonitor;
        if (z) {
            this.mCrossWindowBlurEnabledListener = new VolumeDialogImpl$$ExternalSyntheticLambda0(this, contextThemeWrapper.getColor(R$color.volume_dialog_background_color_above_blur), contextThemeWrapper.getColor(R$color.volume_dialog_background_color));
        }
        initDimens();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, Boolean bool) {
        BackgroundBlurDrawable backgroundBlurDrawable = this.mDialogRowsViewBackground;
        if (!bool.booleanValue()) {
            i = i2;
        }
        backgroundBlurDrawable.setColor(i);
        this.mDialogRowsView.invalidate();
    }

    public void onUiModeChanged() {
        this.mContext.getTheme().applyStyle(this.mContext.getThemeResId(), true);
    }

    public void init(int i, VolumeDialog.Callback callback) {
        initDialog(this.mActivityManager.getLockTaskModeState());
        this.mAccessibility.init();
        this.mController.addCallback(this.mControllerCallbackH, this.mHandler);
        this.mController.getState();
        this.mConfigurationController.addCallback(this);
    }

    public void destroy() {
        this.mController.removeCallback(this.mControllerCallbackH);
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.mConfigurationController.removeCallback(this);
    }

    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        internalInsetsInfo.setTouchableInsets(3);
        this.mTouchableRegion.setEmpty();
        for (int i = 0; i < this.mDialogView.getChildCount(); i++) {
            unionViewBoundstoTouchableRegion(this.mDialogView.getChildAt(i));
        }
        View view = this.mODICaptionsTooltipView;
        if (view != null && view.getVisibility() == 0) {
            unionViewBoundstoTouchableRegion(this.mODICaptionsTooltipView);
        }
        internalInsetsInfo.touchableRegion.set(this.mTouchableRegion);
    }

    public final void unionViewBoundstoTouchableRegion(View view) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        float f = (float) iArr[0];
        float f2 = (float) iArr[1];
        if (view == this.mTopContainer && !this.mIsRingerDrawerOpen) {
            if (!isLandscape()) {
                f2 += (float) getRingerDrawerOpenExtraSize();
            } else {
                f += (float) getRingerDrawerOpenExtraSize();
            }
        }
        this.mTouchableRegion.op((int) f, (int) f2, iArr[0] + view.getWidth(), iArr[1] + view.getHeight(), Region.Op.UNION);
    }

    public final void initDialog(int i) {
        this.mDialog = new CustomDialog(this.mContext);
        initDimens();
        this.mConfigurableTexts = new ConfigurableTexts(this.mContext);
        this.mHovering = false;
        this.mShowing = false;
        Window window = this.mDialog.getWindow();
        this.mWindow = window;
        window.requestFeature(1);
        this.mWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.mWindow.clearFlags(65538);
        this.mWindow.addFlags(17563688);
        this.mWindow.addPrivateFlags(536870912);
        this.mWindow.setType(2020);
        this.mWindow.setWindowAnimations(16973828);
        WindowManager.LayoutParams attributes = this.mWindow.getAttributes();
        attributes.format = -3;
        attributes.setTitle(VolumeDialogImpl.class.getSimpleName());
        attributes.windowAnimations = -1;
        attributes.gravity = this.mContext.getResources().getInteger(R$integer.volume_dialog_gravity);
        this.mWindow.setAttributes(attributes);
        this.mWindow.setLayout(-2, -2);
        this.mDialog.setContentView(R$layout.volume_dialog);
        ViewGroup viewGroup = (ViewGroup) this.mDialog.findViewById(R$id.volume_dialog);
        this.mDialogView = viewGroup;
        viewGroup.setAlpha(0.0f);
        this.mDialog.setCanceledOnTouchOutside(true);
        this.mDialog.setOnShowListener(new VolumeDialogImpl$$ExternalSyntheticLambda2(this));
        this.mDialog.setOnDismissListener(new VolumeDialogImpl$$ExternalSyntheticLambda3(this));
        this.mDialogView.setOnHoverListener(new VolumeDialogImpl$$ExternalSyntheticLambda4(this));
        this.mDialogRowsView = (ViewGroup) this.mDialog.findViewById(R$id.volume_dialog_rows);
        if (this.mUseBackgroundBlur) {
            this.mDialogView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                public void onViewAttachedToWindow(View view) {
                    VolumeDialogImpl.this.mWindow.getWindowManager().addCrossWindowBlurEnabledListener(VolumeDialogImpl.this.mCrossWindowBlurEnabledListener);
                    VolumeDialogImpl.this.mDialogRowsViewBackground = view.getViewRootImpl().createBackgroundBlurDrawable();
                    Resources resources = VolumeDialogImpl.this.mContext.getResources();
                    VolumeDialogImpl.this.mDialogRowsViewBackground.setCornerRadius((float) VolumeDialogImpl.this.mContext.getResources().getDimensionPixelSize(Utils.getThemeAttr(VolumeDialogImpl.this.mContext, 16844145)));
                    VolumeDialogImpl.this.mDialogRowsViewBackground.setBlurRadius(resources.getDimensionPixelSize(R$dimen.volume_dialog_background_blur_radius));
                    VolumeDialogImpl.this.mDialogRowsView.setBackground(VolumeDialogImpl.this.mDialogRowsViewBackground);
                }

                public void onViewDetachedFromWindow(View view) {
                    VolumeDialogImpl.this.mWindow.getWindowManager().removeCrossWindowBlurEnabledListener(VolumeDialogImpl.this.mCrossWindowBlurEnabledListener);
                }
            });
        }
        this.mDialogRowsViewContainer = (ViewGroup) this.mDialogView.findViewById(R$id.volume_dialog_rows_container);
        this.mTopContainer = this.mDialogView.findViewById(R$id.volume_dialog_top_container);
        View findViewById = this.mDialogView.findViewById(R$id.volume_ringer_and_drawer_container);
        this.mRingerAndDrawerContainer = findViewById;
        if (findViewById != null) {
            if (isLandscape()) {
                View view = this.mRingerAndDrawerContainer;
                view.setPadding(view.getPaddingLeft(), this.mRingerAndDrawerContainer.getPaddingTop(), this.mRingerAndDrawerContainer.getPaddingRight(), this.mRingerRowsPadding);
                this.mRingerAndDrawerContainer.setBackgroundDrawable(this.mContext.getDrawable(R$drawable.volume_background_top_rounded));
            }
            this.mRingerAndDrawerContainer.post(new VolumeDialogImpl$$ExternalSyntheticLambda5(this));
        }
        ViewGroup viewGroup2 = (ViewGroup) this.mDialog.findViewById(R$id.ringer);
        this.mRinger = viewGroup2;
        if (viewGroup2 != null) {
            this.mRingerIcon = (ImageButton) viewGroup2.findViewById(R$id.ringer_icon);
            this.mZenIcon = (FrameLayout) this.mRinger.findViewById(R$id.dnd_icon);
        }
        this.mSelectedRingerIcon = (ImageView) this.mDialog.findViewById(R$id.volume_new_ringer_active_icon);
        this.mSelectedRingerContainer = (ViewGroup) this.mDialog.findViewById(R$id.volume_new_ringer_active_icon_container);
        this.mRingerDrawerMute = (ViewGroup) this.mDialog.findViewById(R$id.volume_drawer_mute);
        this.mRingerDrawerNormal = (ViewGroup) this.mDialog.findViewById(R$id.volume_drawer_normal);
        this.mRingerDrawerVibrate = (ViewGroup) this.mDialog.findViewById(R$id.volume_drawer_vibrate);
        this.mRingerDrawerMuteIcon = (ImageView) this.mDialog.findViewById(R$id.volume_drawer_mute_icon);
        this.mRingerDrawerVibrateIcon = (ImageView) this.mDialog.findViewById(R$id.volume_drawer_vibrate_icon);
        this.mRingerDrawerNormalIcon = (ImageView) this.mDialog.findViewById(R$id.volume_drawer_normal_icon);
        this.mRingerDrawerNewSelectionBg = (ViewGroup) this.mDialog.findViewById(R$id.volume_drawer_selection_background);
        setupRingerDrawer();
        ViewGroup viewGroup3 = (ViewGroup) this.mDialog.findViewById(R$id.odi_captions);
        this.mODICaptionsView = viewGroup3;
        if (viewGroup3 != null) {
            this.mODICaptionsIcon = (CaptionsToggleImageButton) viewGroup3.findViewById(R$id.odi_captions_icon);
        }
        ViewStub viewStub = (ViewStub) this.mDialog.findViewById(R$id.odi_captions_tooltip_stub);
        this.mODICaptionsTooltipViewStub = viewStub;
        if (this.mHasSeenODICaptionsTooltip && viewStub != null) {
            this.mDialogView.removeView(viewStub);
            this.mODICaptionsTooltipViewStub = null;
        }
        this.mSettingsView = this.mDialog.findViewById(R$id.settings_container);
        this.mSettingsIcon = (ImageButton) this.mDialog.findViewById(R$id.settings);
        if (this.mRows.isEmpty()) {
            if (!AudioSystem.isSingleVolume(this.mContext)) {
                int i2 = R$drawable.ic_volume_accessibility;
                addRow(10, i2, i2, true, false);
            }
            addRow(3, R$drawable.ic_volume_media, R$drawable.ic_volume_media_mute, true, true);
            if (!AudioSystem.isSingleVolume(this.mContext)) {
                addRow(2, R$drawable.ic_volume_ringer, R$drawable.ic_volume_ringer_mute, true, false);
                addRow(4, R$drawable.ic_alarm, R$drawable.ic_volume_alarm_mute, true, false);
                addRow(0, 17302816, 17302816, false, false);
                int i3 = R$drawable.ic_volume_bt_sco;
                addRow(6, i3, i3, false, false);
                addRow(1, R$drawable.ic_volume_system, R$drawable.ic_volume_system_mute, false, false);
            }
        } else {
            addExistingRows();
        }
        updateRowsH(getActiveRow());
        initRingerH();
        initSettingsH(i);
        initODICaptionsH();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initDialog$2(DialogInterface dialogInterface) {
        this.mDialogView.getViewTreeObserver().addOnComputeInternalInsetsListener(this);
        if (!shouldSlideInVolumeTray()) {
            ViewGroup viewGroup = this.mDialogView;
            viewGroup.setTranslationX(((float) viewGroup.getWidth()) / 2.0f);
        }
        this.mDialogView.setAlpha(0.0f);
        this.mDialogView.animate().alpha(1.0f).translationX(0.0f).setDuration((long) this.mDialogShowAnimationDurationMs).setListener(getJankListener(getDialogView(), "show", 3000)).setInterpolator(new SystemUIInterpolators$LogDecelerateInterpolator()).withEndAction(new VolumeDialogImpl$$ExternalSyntheticLambda7(this)).start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initDialog$1() {
        ImageButton imageButton;
        if (!Prefs.getBoolean(this.mContext, "TouchedRingerToggle", false) && (imageButton = this.mRingerIcon) != null) {
            imageButton.postOnAnimationDelayed(getSinglePressFor(imageButton), 1500);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initDialog$3(DialogInterface dialogInterface) {
        this.mDialogView.getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$initDialog$4(View view, MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        this.mHovering = actionMasked == 9 || actionMasked == 7;
        rescheduleTimeoutH();
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initDialog$5() {
        LayerDrawable layerDrawable = (LayerDrawable) this.mRingerAndDrawerContainer.getBackground();
        if (layerDrawable != null && layerDrawable.getNumberOfLayers() > 0) {
            this.mRingerAndDrawerContainerBackground = layerDrawable.getDrawable(0);
            updateBackgroundForDrawerClosedAmount();
            setTopContainerBackgroundDrawable();
        }
    }

    public final void initDimens() {
        this.mDialogWidth = this.mContext.getResources().getDimensionPixelSize(R$dimen.volume_dialog_panel_width);
        this.mDialogCornerRadius = this.mContext.getResources().getDimensionPixelSize(R$dimen.volume_dialog_panel_width_half);
        this.mRingerDrawerItemSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.volume_ringer_drawer_item_size);
        this.mRingerRowsPadding = this.mContext.getResources().getDimensionPixelSize(R$dimen.volume_dialog_ringer_rows_padding);
        boolean hasVibrator = this.mController.hasVibrator();
        this.mShowVibrate = hasVibrator;
        this.mRingerCount = hasVibrator ? 3 : 2;
    }

    public ViewGroup getDialogView() {
        return this.mDialogView;
    }

    public final int getAlphaAttr(int i) {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{i});
        float f = obtainStyledAttributes.getFloat(0, 0.0f);
        obtainStyledAttributes.recycle();
        return (int) (f * 255.0f);
    }

    public final boolean shouldSlideInVolumeTray() {
        return this.mContext.getDisplay().getRotation() != 0;
    }

    public final boolean isLandscape() {
        return this.mContext.getResources().getConfiguration().orientation == 2;
    }

    public final boolean isRtl() {
        return this.mContext.getResources().getConfiguration().getLayoutDirection() == 1;
    }

    public void setStreamImportant(int i, boolean z) {
        this.mHandler.obtainMessage(5, i, z ? 1 : 0).sendToTarget();
    }

    public void setAutomute(boolean z) {
        if (this.mAutomute != z) {
            this.mAutomute = z;
            this.mHandler.sendEmptyMessage(4);
        }
    }

    public void setSilentMode(boolean z) {
        if (this.mSilentMode != z) {
            this.mSilentMode = z;
            this.mHandler.sendEmptyMessage(4);
        }
    }

    public final void addRow(int i, int i2, int i3, boolean z, boolean z2) {
        addRow(i, i2, i3, z, z2, false);
    }

    public final void addRow(int i, int i2, int i3, boolean z, boolean z2, boolean z3) {
        if (D.BUG) {
            String str = TAG;
            Slog.d(str, "Adding row for stream " + i);
        }
        VolumeRow volumeRow = new VolumeRow();
        initRow(volumeRow, i, i2, i3, z, z2);
        this.mDialogRowsView.addView(volumeRow.view);
        this.mRows.add(volumeRow);
    }

    public final void addExistingRows() {
        int size = this.mRows.size();
        for (int i = 0; i < size; i++) {
            VolumeRow volumeRow = this.mRows.get(i);
            initRow(volumeRow, volumeRow.stream, volumeRow.iconRes, volumeRow.iconMuteRes, volumeRow.important, volumeRow.defaultStream);
            this.mDialogRowsView.addView(volumeRow.view);
            updateVolumeRowH(volumeRow);
        }
    }

    public final VolumeRow getActiveRow() {
        for (VolumeRow next : this.mRows) {
            if (next.stream == this.mActiveStream) {
                return next;
            }
        }
        for (VolumeRow next2 : this.mRows) {
            if (next2.stream == 3) {
                return next2;
            }
        }
        return this.mRows.get(0);
    }

    public final VolumeRow findRow(int i) {
        for (VolumeRow next : this.mRows) {
            if (next.stream == i) {
                return next;
            }
        }
        return null;
    }

    public static int getImpliedLevel(SeekBar seekBar, int i) {
        int max = seekBar.getMax();
        int i2 = max / 100;
        int i3 = i2 - 1;
        if (i == 0) {
            return 0;
        }
        return i == max ? i2 : ((int) ((((float) i) / ((float) max)) * ((float) i3))) + 1;
    }

    @SuppressLint({"InflateParams"})
    public final void initRow(VolumeRow volumeRow, int i, int i2, int i3, boolean z, boolean z2) {
        volumeRow.stream = i;
        volumeRow.iconRes = i2;
        volumeRow.iconMuteRes = i3;
        volumeRow.important = z;
        volumeRow.defaultStream = z2;
        AlphaTintDrawableWrapper alphaTintDrawableWrapper = null;
        volumeRow.view = this.mDialog.getLayoutInflater().inflate(R$layout.volume_dialog_row, (ViewGroup) null);
        volumeRow.view.setId(volumeRow.stream);
        volumeRow.view.setTag(volumeRow);
        volumeRow.header = (TextView) volumeRow.view.findViewById(R$id.volume_row_header);
        volumeRow.header.setId(volumeRow.stream * 20);
        if (i == 10) {
            volumeRow.header.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        }
        volumeRow.dndIcon = (FrameLayout) volumeRow.view.findViewById(R$id.dnd_icon);
        volumeRow.slider = (SeekBar) volumeRow.view.findViewById(R$id.volume_row_slider);
        volumeRow.slider.setOnSeekBarChangeListener(new VolumeSeekBarChangeListener(volumeRow));
        volumeRow.number = (TextView) volumeRow.view.findViewById(R$id.volume_number);
        volumeRow.anim = null;
        LayerDrawable layerDrawable = (LayerDrawable) this.mContext.getDrawable(R$drawable.volume_row_seekbar);
        LayerDrawable layerDrawable2 = (LayerDrawable) ((RoundedCornerProgressDrawable) layerDrawable.findDrawableByLayerId(16908301)).getDrawable();
        volumeRow.sliderProgressSolid = layerDrawable2.findDrawableByLayerId(R$id.volume_seekbar_progress_solid);
        Drawable findDrawableByLayerId = layerDrawable2.findDrawableByLayerId(R$id.volume_seekbar_progress_icon);
        if (findDrawableByLayerId != null) {
            alphaTintDrawableWrapper = (AlphaTintDrawableWrapper) ((RotateDrawable) findDrawableByLayerId).getDrawable();
        }
        volumeRow.sliderProgressIcon = alphaTintDrawableWrapper;
        volumeRow.slider.setProgressDrawable(layerDrawable);
        volumeRow.icon = (ImageButton) volumeRow.view.findViewById(R$id.volume_row_icon);
        volumeRow.setIcon(i2, this.mContext.getTheme());
        if (volumeRow.icon == null) {
            return;
        }
        if (volumeRow.stream != 10) {
            volumeRow.icon.setOnClickListener(new VolumeDialogImpl$$ExternalSyntheticLambda17(this, volumeRow, i));
        } else {
            volumeRow.icon.setImportantForAccessibility(2);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initRow$6(VolumeRow volumeRow, int i, View view) {
        int i2 = 0;
        boolean z = true;
        Events.writeEvent(7, Integer.valueOf(volumeRow.stream), Integer.valueOf(volumeRow.iconState));
        this.mController.setActiveStream(volumeRow.stream);
        if (volumeRow.stream == 2) {
            boolean hasVibrator = this.mController.hasVibrator();
            if (this.mState.ringerModeInternal != 2) {
                this.mController.setRingerMode(2, false);
                if (volumeRow.ss.level == 0) {
                    this.mController.setStreamVolume(i, 1);
                }
            } else if (hasVibrator) {
                this.mController.setRingerMode(1, false);
            } else {
                if (volumeRow.ss.level != 0) {
                    z = false;
                }
                VolumeDialogController volumeDialogController = this.mController;
                if (z) {
                    i2 = volumeRow.lastAudibleLevel;
                }
                volumeDialogController.setStreamVolume(i, i2);
            }
        } else {
            if (volumeRow.ss.level == volumeRow.ss.levelMin) {
                i2 = 1;
            }
            this.mController.setStreamVolume(i, i2 != 0 ? volumeRow.lastAudibleLevel : volumeRow.ss.levelMin);
        }
        volumeRow.userAttempt = 0;
    }

    public final void setRingerMode(int i) {
        Events.writeEvent(18, Integer.valueOf(i));
        incrementManualToggleCount();
        updateRingerH();
        provideTouchFeedbackH(i);
        this.mController.setRingerMode(i, false);
        maybeShowToastH(i);
    }

    public final void setupRingerDrawer() {
        ViewGroup viewGroup = (ViewGroup) this.mDialog.findViewById(R$id.volume_drawer_container);
        this.mRingerDrawerContainer = viewGroup;
        if (viewGroup != null) {
            if (!this.mShowVibrate) {
                this.mRingerDrawerVibrate.setVisibility(8);
            }
            if (!isLandscape()) {
                ViewGroup viewGroup2 = this.mDialogView;
                viewGroup2.setPadding(viewGroup2.getPaddingLeft(), this.mDialogView.getPaddingTop(), this.mDialogView.getPaddingRight(), this.mDialogView.getPaddingBottom() + getRingerDrawerOpenExtraSize());
            } else {
                ViewGroup viewGroup3 = this.mDialogView;
                viewGroup3.setPadding(viewGroup3.getPaddingLeft() + getRingerDrawerOpenExtraSize(), this.mDialogView.getPaddingTop(), this.mDialogView.getPaddingRight(), this.mDialogView.getPaddingBottom());
            }
            ((LinearLayout) this.mRingerDrawerContainer.findViewById(R$id.volume_drawer_options)).setOrientation(isLandscape() ^ true ? 1 : 0);
            this.mSelectedRingerContainer.setOnClickListener(new VolumeDialogImpl$$ExternalSyntheticLambda10(this));
            this.mRingerDrawerVibrate.setOnClickListener(new RingerDrawerItemClickListener(1));
            this.mRingerDrawerMute.setOnClickListener(new RingerDrawerItemClickListener(0));
            this.mRingerDrawerNormal.setOnClickListener(new RingerDrawerItemClickListener(2));
            int colorAccentDefaultColor = Utils.getColorAccentDefaultColor(this.mContext);
            this.mRingerDrawerIconColorAnimator.addUpdateListener(new VolumeDialogImpl$$ExternalSyntheticLambda11(this, Utils.getColorAttrDefaultColor(this.mContext, 16844002), colorAccentDefaultColor));
            this.mRingerDrawerIconColorAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    VolumeDialogImpl.this.mRingerDrawerIconAnimatingDeselected.clearColorFilter();
                    VolumeDialogImpl.this.mRingerDrawerIconAnimatingSelected.clearColorFilter();
                }
            });
            this.mRingerDrawerIconColorAnimator.setDuration(175);
            this.mAnimateUpBackgroundToMatchDrawer.addUpdateListener(new VolumeDialogImpl$$ExternalSyntheticLambda12(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupRingerDrawer$7(View view) {
        if (this.mIsRingerDrawerOpen) {
            hideRingerDrawer();
        } else {
            showRingerDrawer();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupRingerDrawer$8(int i, int i2, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        int intValue = ((Integer) ArgbEvaluator.getInstance().evaluate(floatValue, Integer.valueOf(i), Integer.valueOf(i2))).intValue();
        int intValue2 = ((Integer) ArgbEvaluator.getInstance().evaluate(floatValue, Integer.valueOf(i2), Integer.valueOf(i))).intValue();
        this.mRingerDrawerIconAnimatingDeselected.setColorFilter(intValue);
        this.mRingerDrawerIconAnimatingSelected.setColorFilter(intValue2);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupRingerDrawer$9(ValueAnimator valueAnimator) {
        this.mRingerDrawerClosedAmount = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateBackgroundForDrawerClosedAmount();
    }

    public final ImageView getDrawerIconViewForMode(int i) {
        if (i == 1) {
            return this.mRingerDrawerVibrateIcon;
        }
        if (i == 0) {
            return this.mRingerDrawerMuteIcon;
        }
        return this.mRingerDrawerNormalIcon;
    }

    public final float getTranslationInDrawerForRingerMode(int i) {
        int i2;
        if (i == 1) {
            i2 = (-this.mRingerDrawerItemSize) * 2;
        } else if (i != 0) {
            return 0.0f;
        } else {
            i2 = -this.mRingerDrawerItemSize;
        }
        return (float) i2;
    }

    public final void showRingerDrawer() {
        if (!this.mIsRingerDrawerOpen) {
            int i = 4;
            this.mRingerDrawerVibrateIcon.setVisibility(this.mState.ringerModeInternal == 1 ? 4 : 0);
            this.mRingerDrawerMuteIcon.setVisibility(this.mState.ringerModeInternal == 0 ? 4 : 0);
            ImageView imageView = this.mRingerDrawerNormalIcon;
            if (this.mState.ringerModeInternal != 2) {
                i = 0;
            }
            imageView.setVisibility(i);
            this.mRingerDrawerNewSelectionBg.setAlpha(0.0f);
            if (!isLandscape()) {
                this.mRingerDrawerNewSelectionBg.setTranslationY(getTranslationInDrawerForRingerMode(this.mState.ringerModeInternal));
            } else {
                this.mRingerDrawerNewSelectionBg.setTranslationX(getTranslationInDrawerForRingerMode(this.mState.ringerModeInternal));
            }
            if (!isLandscape()) {
                this.mRingerDrawerContainer.setTranslationY((float) (this.mRingerDrawerItemSize * (this.mRingerCount - 1)));
            } else {
                this.mRingerDrawerContainer.setTranslationX((float) (this.mRingerDrawerItemSize * (this.mRingerCount - 1)));
            }
            this.mRingerDrawerContainer.setAlpha(0.0f);
            this.mRingerDrawerContainer.setVisibility(0);
            int i2 = this.mState.ringerModeInternal == 1 ? 175 : 250;
            ViewPropertyAnimator animate = this.mRingerDrawerContainer.animate();
            Interpolator interpolator = Interpolators.FAST_OUT_SLOW_IN;
            long j = (long) i2;
            animate.setInterpolator(interpolator).setDuration(j).setStartDelay(this.mState.ringerModeInternal == 1 ? 75 : 0).alpha(1.0f).translationX(0.0f).translationY(0.0f).start();
            this.mSelectedRingerContainer.animate().setInterpolator(interpolator).setDuration(250).withEndAction(new VolumeDialogImpl$$ExternalSyntheticLambda21(this));
            this.mAnimateUpBackgroundToMatchDrawer.setDuration(j);
            this.mAnimateUpBackgroundToMatchDrawer.setInterpolator(interpolator);
            this.mAnimateUpBackgroundToMatchDrawer.start();
            if (!isLandscape()) {
                this.mSelectedRingerContainer.animate().translationY(getTranslationInDrawerForRingerMode(this.mState.ringerModeInternal)).start();
            } else {
                this.mSelectedRingerContainer.animate().translationX(getTranslationInDrawerForRingerMode(this.mState.ringerModeInternal)).start();
            }
            this.mSelectedRingerContainer.setContentDescription(this.mContext.getString(getStringDescriptionResourceForRingerMode(this.mState.ringerModeInternal)));
            this.mIsRingerDrawerOpen = true;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showRingerDrawer$10() {
        getDrawerIconViewForMode(this.mState.ringerModeInternal).setVisibility(0);
    }

    public final void hideRingerDrawer() {
        if (this.mRingerDrawerContainer != null && this.mIsRingerDrawerOpen) {
            getDrawerIconViewForMode(this.mState.ringerModeInternal).setVisibility(4);
            this.mRingerDrawerContainer.animate().alpha(0.0f).setDuration(250).setStartDelay(0).withEndAction(new VolumeDialogImpl$$ExternalSyntheticLambda19(this));
            if (!isLandscape()) {
                this.mRingerDrawerContainer.animate().translationY((float) (this.mRingerDrawerItemSize * 2)).start();
            } else {
                this.mRingerDrawerContainer.animate().translationX((float) (this.mRingerDrawerItemSize * 2)).start();
            }
            this.mAnimateUpBackgroundToMatchDrawer.setDuration(250);
            this.mAnimateUpBackgroundToMatchDrawer.setInterpolator(Interpolators.FAST_OUT_SLOW_IN_REVERSE);
            this.mAnimateUpBackgroundToMatchDrawer.reverse();
            this.mSelectedRingerContainer.animate().translationX(0.0f).translationY(0.0f).start();
            this.mSelectedRingerContainer.setContentDescription(this.mContext.getString(R$string.volume_ringer_change));
            this.mIsRingerDrawerOpen = false;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$hideRingerDrawer$11() {
        this.mRingerDrawerContainer.setVisibility(4);
    }

    public final void initSettingsH(int i) {
        View view = this.mSettingsView;
        if (view != null) {
            view.setVisibility((!this.mDeviceProvisionedController.isCurrentUserSetup() || i != 0) ? 8 : 0);
        }
        ImageButton imageButton = this.mSettingsIcon;
        if (imageButton != null) {
            imageButton.setOnClickListener(new VolumeDialogImpl$$ExternalSyntheticLambda8(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initSettingsH$12(View view) {
        Events.writeEvent(8, new Object[0]);
        Intent intent = new Intent("android.settings.panel.action.VOLUME");
        dismissH(5);
        this.mMediaOutputDialogFactory.dismiss();
        this.mActivityStarter.startActivity(intent, true);
    }

    public void initRingerH() {
        ImageButton imageButton = this.mRingerIcon;
        if (imageButton != null) {
            imageButton.setAccessibilityLiveRegion(1);
            this.mRingerIcon.setOnClickListener(new VolumeDialogImpl$$ExternalSyntheticLambda6(this));
        }
        updateRingerH();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0023, code lost:
        if (r2 != false) goto L_0x0034;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void lambda$initRingerH$13(android.view.View r6) {
        /*
            r5 = this;
            android.content.Context r6 = r5.mContext
            java.lang.String r0 = "TouchedRingerToggle"
            r1 = 1
            com.android.systemui.Prefs.putBoolean(r6, r0, r1)
            com.android.systemui.plugins.VolumeDialogController$State r6 = r5.mState
            android.util.SparseArray<com.android.systemui.plugins.VolumeDialogController$StreamState> r6 = r6.states
            r0 = 2
            java.lang.Object r6 = r6.get(r0)
            com.android.systemui.plugins.VolumeDialogController$StreamState r6 = (com.android.systemui.plugins.VolumeDialogController.StreamState) r6
            if (r6 != 0) goto L_0x0016
            return
        L_0x0016:
            com.android.systemui.plugins.VolumeDialogController r2 = r5.mController
            boolean r2 = r2.hasVibrator()
            com.android.systemui.plugins.VolumeDialogController$State r3 = r5.mState
            int r3 = r3.ringerModeInternal
            r4 = 0
            if (r3 != r0) goto L_0x0026
            if (r2 == 0) goto L_0x0028
            goto L_0x0034
        L_0x0026:
            if (r3 != r1) goto L_0x002a
        L_0x0028:
            r1 = r4
            goto L_0x0034
        L_0x002a:
            int r6 = r6.level
            if (r6 != 0) goto L_0x0033
            com.android.systemui.plugins.VolumeDialogController r6 = r5.mController
            r6.setStreamVolume(r0, r1)
        L_0x0033:
            r1 = r0
        L_0x0034:
            r5.setRingerMode(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.volume.VolumeDialogImpl.lambda$initRingerH$13(android.view.View):void");
    }

    public final void initODICaptionsH() {
        CaptionsToggleImageButton captionsToggleImageButton = this.mODICaptionsIcon;
        if (captionsToggleImageButton != null) {
            captionsToggleImageButton.setOnConfirmedTapListener(new VolumeDialogImpl$$ExternalSyntheticLambda13(this), this.mHandler);
        }
        this.mController.getCaptionsComponentState(false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initODICaptionsH$14() {
        onCaptionIconClicked();
        Events.writeEvent(21, new Object[0]);
    }

    public final void checkODICaptionsTooltip(boolean z) {
        boolean z2 = this.mHasSeenODICaptionsTooltip;
        if (!z2 && !z && this.mODICaptionsTooltipViewStub != null) {
            this.mController.getCaptionsComponentState(true);
        } else if (z2 && z && this.mODICaptionsTooltipView != null) {
            hideCaptionsTooltip();
        }
    }

    public void showCaptionsTooltip() {
        ViewStub viewStub;
        if (!this.mHasSeenODICaptionsTooltip && (viewStub = this.mODICaptionsTooltipViewStub) != null) {
            View inflate = viewStub.inflate();
            this.mODICaptionsTooltipView = inflate;
            inflate.findViewById(R$id.dismiss).setOnClickListener(new VolumeDialogImpl$$ExternalSyntheticLambda15(this));
            this.mODICaptionsTooltipViewStub = null;
            rescheduleTimeoutH();
        }
        View view = this.mODICaptionsTooltipView;
        if (view != null) {
            view.setAlpha(0.0f);
            this.mHandler.post(new VolumeDialogImpl$$ExternalSyntheticLambda16(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showCaptionsTooltip$15(View view) {
        hideCaptionsTooltip();
        Events.writeEvent(22, new Object[0]);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showCaptionsTooltip$17() {
        int[] locationOnScreen = this.mODICaptionsTooltipView.getLocationOnScreen();
        int[] locationOnScreen2 = this.mODICaptionsIcon.getLocationOnScreen();
        this.mODICaptionsTooltipView.setTranslationY(((float) (locationOnScreen2[1] - locationOnScreen[1])) - (((float) (this.mODICaptionsTooltipView.getHeight() - this.mODICaptionsIcon.getHeight())) / 2.0f));
        this.mODICaptionsTooltipView.animate().alpha(1.0f).setStartDelay((long) this.mDialogShowAnimationDurationMs).withEndAction(new VolumeDialogImpl$$ExternalSyntheticLambda18(this)).start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showCaptionsTooltip$16() {
        if (D.BUG) {
            Log.d(TAG, "tool:checkODICaptionsTooltip() putBoolean true");
        }
        Prefs.putBoolean(this.mContext, "HasSeenODICaptionsTooltip", true);
        this.mHasSeenODICaptionsTooltip = true;
        CaptionsToggleImageButton captionsToggleImageButton = this.mODICaptionsIcon;
        if (captionsToggleImageButton != null) {
            captionsToggleImageButton.postOnAnimation(getSinglePressFor(captionsToggleImageButton));
        }
    }

    public final void hideCaptionsTooltip() {
        View view = this.mODICaptionsTooltipView;
        if (view != null && view.getVisibility() == 0) {
            this.mODICaptionsTooltipView.animate().cancel();
            this.mODICaptionsTooltipView.setAlpha(1.0f);
            this.mODICaptionsTooltipView.animate().alpha(0.0f).setStartDelay(0).setDuration((long) this.mDialogHideAnimationDurationMs).withEndAction(new VolumeDialogImpl$$ExternalSyntheticLambda14(this)).start();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$hideCaptionsTooltip$18() {
        View view = this.mODICaptionsTooltipView;
        if (view != null) {
            view.setVisibility(4);
        }
    }

    public void tryToRemoveCaptionsTooltip() {
        if (this.mHasSeenODICaptionsTooltip && this.mODICaptionsTooltipView != null) {
            ((ViewGroup) this.mDialog.findViewById(R$id.volume_dialog_container)).removeView(this.mODICaptionsTooltipView);
            this.mODICaptionsTooltipView = null;
        }
    }

    public final void updateODICaptionsH(boolean z, boolean z2) {
        ViewGroup viewGroup = this.mODICaptionsView;
        if (viewGroup != null) {
            viewGroup.setVisibility(z ? 0 : 8);
        }
        if (z) {
            updateCaptionsIcon();
            if (z2) {
                showCaptionsTooltip();
            }
        }
    }

    public final void updateCaptionsIcon() {
        boolean areCaptionsEnabled = this.mController.areCaptionsEnabled();
        if (this.mODICaptionsIcon.getCaptionsEnabled() != areCaptionsEnabled) {
            this.mHandler.post(this.mODICaptionsIcon.setCaptionsEnabled(areCaptionsEnabled));
        }
    }

    public final void onCaptionIconClicked() {
        this.mController.setCaptionsEnabled(!this.mController.areCaptionsEnabled());
        updateCaptionsIcon();
    }

    public final void incrementManualToggleCount() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Settings.Secure.putInt(contentResolver, "manual_ringer_toggle_count", Settings.Secure.getInt(contentResolver, "manual_ringer_toggle_count", 0) + 1);
    }

    public final void provideTouchFeedbackH(int i) {
        VibrationEffect vibrationEffect;
        if (i == 0) {
            vibrationEffect = VibrationEffect.get(0);
        } else if (i != 2) {
            vibrationEffect = VibrationEffect.get(1);
        } else {
            this.mController.scheduleTouchFeedback();
            vibrationEffect = null;
        }
        if (vibrationEffect != null) {
            this.mController.vibrate(vibrationEffect);
        }
    }

    public final void maybeShowToastH(int i) {
        int i2 = Prefs.getInt(this.mContext, "RingerGuidanceCount", 0);
        if (i2 <= 12) {
            String str = null;
            if (i == 0) {
                str = this.mContext.getString(17041696);
            } else if (i != 2) {
                str = this.mContext.getString(17041697);
            } else {
                VolumeDialogController.StreamState streamState = this.mState.states.get(2);
                if (streamState != null) {
                    str = this.mContext.getString(R$string.volume_dialog_ringer_guidance_ring, new Object[]{Utils.formatPercentage((long) streamState.level, (long) streamState.levelMax)});
                }
            }
            Toast.makeText(this.mContext, str, 0).show();
            Prefs.putInt(this.mContext, "RingerGuidanceCount", i2 + 1);
        }
    }

    public final Animator.AnimatorListener getJankListener(View view, String str, long j) {
        final View view2 = view;
        final String str2 = str;
        final long j2 = j;
        return new Animator.AnimatorListener() {
            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
                if (view2.isAttachedToWindow()) {
                    VolumeDialogImpl.this.mInteractionJankMonitor.begin(InteractionJankMonitor.Configuration.Builder.withView(55, view2).setTag(str2).setTimeout(j2));
                } else if (D.BUG) {
                    String r4 = VolumeDialogImpl.TAG;
                    Log.d(r4, "onAnimationStart view do not attached to window:" + view2);
                }
            }

            public void onAnimationEnd(Animator animator) {
                VolumeDialogImpl.this.mInteractionJankMonitor.end(55);
            }

            public void onAnimationCancel(Animator animator) {
                VolumeDialogImpl.this.mInteractionJankMonitor.cancel(55);
            }
        };
    }

    public final void showH(int i, boolean z, int i2) {
        Trace.beginSection("VolumeDialogImpl#showH");
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "showH r=" + Events.SHOW_REASONS[i]);
        }
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        rescheduleTimeoutH();
        if (this.mConfigChanged) {
            initDialog(i2);
            this.mConfigurableTexts.update();
            this.mConfigChanged = false;
        }
        initSettingsH(i2);
        this.mShowing = true;
        this.mIsAnimatingDismiss = false;
        this.mDialog.show();
        Events.writeEvent(0, Integer.valueOf(i), Boolean.valueOf(z));
        this.mController.notifyVisible(true);
        this.mController.getCaptionsComponentState(false);
        checkODICaptionsTooltip(false);
        updateBackgroundForDrawerClosedAmount();
        Trace.endSection();
    }

    public void rescheduleTimeoutH() {
        this.mHandler.removeMessages(2);
        int computeTimeoutH = computeTimeoutH();
        H h = this.mHandler;
        h.sendMessageDelayed(h.obtainMessage(2, 3, 0), (long) computeTimeoutH);
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "rescheduleTimeout " + computeTimeoutH + " " + Debug.getCaller());
        }
        this.mController.userActivity();
    }

    public final int computeTimeoutH() {
        if (this.mHovering) {
            return this.mAccessibilityMgr.getRecommendedTimeoutMillis(16000, 4);
        }
        if (this.mSafetyWarning != null) {
            return this.mAccessibilityMgr.getRecommendedTimeoutMillis(5000, 6);
        }
        if (this.mHasSeenODICaptionsTooltip || this.mODICaptionsTooltipView == null) {
            return this.mAccessibilityMgr.getRecommendedTimeoutMillis(3000, 4);
        }
        return this.mAccessibilityMgr.getRecommendedTimeoutMillis(5000, 6);
    }

    public void dismissH(int i) {
        Trace.beginSection("VolumeDialogImpl#dismissH");
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "mDialog.dismiss() reason: " + Events.DISMISS_REASONS[i] + " from: " + Debug.getCaller());
        }
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(1);
        if (!this.mIsAnimatingDismiss) {
            this.mIsAnimatingDismiss = true;
            this.mDialogView.animate().cancel();
            if (this.mShowing) {
                this.mShowing = false;
                Events.writeEvent(1, Integer.valueOf(i));
            }
            this.mDialogView.setTranslationX(0.0f);
            this.mDialogView.setAlpha(1.0f);
            ViewPropertyAnimator withEndAction = this.mDialogView.animate().alpha(0.0f).setDuration((long) this.mDialogHideAnimationDurationMs).setInterpolator(new SystemUIInterpolators$LogAccelerateInterpolator()).withEndAction(new VolumeDialogImpl$$ExternalSyntheticLambda1(this));
            if (!shouldSlideInVolumeTray()) {
                withEndAction.translationX(((float) this.mDialogView.getWidth()) / 2.0f);
            }
            withEndAction.setListener(getJankListener(getDialogView(), "dismiss", (long) this.mDialogHideAnimationDurationMs)).start();
            checkODICaptionsTooltip(true);
            this.mController.notifyVisible(false);
            synchronized (this.mSafetyWarningLock) {
                if (this.mSafetyWarning != null) {
                    if (D.BUG) {
                        Log.d(TAG, "SafetyWarning dismissed");
                    }
                    this.mSafetyWarning.dismiss();
                }
            }
            Trace.endSection();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissH$20() {
        this.mHandler.postDelayed(new VolumeDialogImpl$$ExternalSyntheticLambda9(this), 50);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$dismissH$19() {
        this.mDialog.dismiss();
        tryToRemoveCaptionsTooltip();
        this.mIsAnimatingDismiss = false;
        hideRingerDrawer();
    }

    public final boolean showActiveStreamOnly() {
        return this.mContext.getPackageManager().hasSystemFeature("android.software.leanback") || this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.television");
    }

    public final boolean shouldBeVisibleH(VolumeRow volumeRow, VolumeRow volumeRow2) {
        if (volumeRow.stream == volumeRow2.stream) {
            return true;
        }
        if (this.mShowActiveStreamOnly) {
            return false;
        }
        if (volumeRow.stream == 10) {
            return this.mShowA11yStream;
        }
        if (volumeRow2.stream == 10 && volumeRow.stream == this.mPrevActiveStream) {
            return true;
        }
        if (!volumeRow.defaultStream) {
            return false;
        }
        if (volumeRow2.stream == 2 || volumeRow2.stream == 4 || volumeRow2.stream == 0 || volumeRow2.stream == 10 || this.mDynamic.get(volumeRow2.stream)) {
            return true;
        }
        return false;
    }

    public final void updateRowsH(VolumeRow volumeRow) {
        int min;
        Trace.beginSection("VolumeDialogImpl#updateRowsH");
        if (D.BUG) {
            Log.d(TAG, "updateRowsH");
        }
        if (!this.mShowing) {
            trimObsoleteH();
        }
        int i = !isRtl() ? -1 : 32767;
        Iterator<VolumeRow> it = this.mRows.iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            VolumeRow next = it.next();
            if (next == volumeRow) {
                z = true;
            }
            boolean shouldBeVisibleH = shouldBeVisibleH(next, volumeRow);
            Util.setVisOrGone(next.view, shouldBeVisibleH);
            if (shouldBeVisibleH && this.mRingerAndDrawerContainerBackground != null) {
                if (!isRtl()) {
                    min = Math.max(i, this.mDialogRowsView.indexOfChild(next.view));
                } else {
                    min = Math.min(i, this.mDialogRowsView.indexOfChild(next.view));
                }
                ViewGroup.LayoutParams layoutParams = next.view.getLayoutParams();
                if (layoutParams instanceof LinearLayout.LayoutParams) {
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
                    if (!isRtl()) {
                        layoutParams2.setMarginEnd(this.mRingerRowsPadding);
                    } else {
                        layoutParams2.setMarginStart(this.mRingerRowsPadding);
                    }
                }
                next.view.setBackgroundDrawable(this.mContext.getDrawable(R$drawable.volume_row_rounded_background));
            }
            if (next.view.isShown()) {
                updateVolumeRowTintH(next, z);
            }
        }
        if (i > -1 && i < 32767) {
            View childAt = this.mDialogRowsView.getChildAt(i);
            ViewGroup.LayoutParams layoutParams3 = childAt.getLayoutParams();
            if (layoutParams3 instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) layoutParams3;
                layoutParams4.setMarginStart(0);
                layoutParams4.setMarginEnd(0);
                childAt.setBackgroundColor(0);
            }
        }
        updateBackgroundForDrawerClosedAmount();
        Trace.endSection();
    }

    public void updateRingerH() {
        VolumeDialogController.State state;
        VolumeDialogController.StreamState streamState;
        if (this.mRinger != null && (state = this.mState) != null && (streamState = state.states.get(2)) != null) {
            VolumeDialogController.State state2 = this.mState;
            int i = state2.zenMode;
            boolean z = false;
            boolean z2 = i == 3 || i == 2 || (i == 1 && state2.disallowRinger);
            enableRingerViewsH(!z2);
            int i2 = this.mState.ringerModeInternal;
            if (i2 == 0) {
                ImageButton imageButton = this.mRingerIcon;
                int i3 = R$drawable.ic_volume_ringer_mute;
                imageButton.setImageResource(i3);
                this.mSelectedRingerIcon.setImageResource(i3);
                this.mRingerIcon.setTag(2);
                addAccessibilityDescription(this.mRingerIcon, 0, this.mContext.getString(R$string.volume_ringer_hint_unmute));
            } else if (i2 != 1) {
                if ((this.mAutomute && streamState.level == 0) || streamState.muted) {
                    z = true;
                }
                if (z2 || !z) {
                    ImageButton imageButton2 = this.mRingerIcon;
                    int i4 = R$drawable.ic_volume_ringer;
                    imageButton2.setImageResource(i4);
                    this.mSelectedRingerIcon.setImageResource(i4);
                    if (this.mController.hasVibrator()) {
                        addAccessibilityDescription(this.mRingerIcon, 2, this.mContext.getString(R$string.volume_ringer_hint_vibrate));
                    } else {
                        addAccessibilityDescription(this.mRingerIcon, 2, this.mContext.getString(R$string.volume_ringer_hint_mute));
                    }
                    this.mRingerIcon.setTag(1);
                    return;
                }
                ImageButton imageButton3 = this.mRingerIcon;
                int i5 = R$drawable.ic_volume_ringer_mute;
                imageButton3.setImageResource(i5);
                this.mSelectedRingerIcon.setImageResource(i5);
                addAccessibilityDescription(this.mRingerIcon, 2, this.mContext.getString(R$string.volume_ringer_hint_unmute));
                this.mRingerIcon.setTag(2);
            } else {
                ImageButton imageButton4 = this.mRingerIcon;
                int i6 = R$drawable.ic_volume_ringer_vibrate;
                imageButton4.setImageResource(i6);
                this.mSelectedRingerIcon.setImageResource(i6);
                addAccessibilityDescription(this.mRingerIcon, 1, this.mContext.getString(R$string.volume_ringer_hint_mute));
                this.mRingerIcon.setTag(3);
            }
        }
    }

    public final void addAccessibilityDescription(View view, int i, final String str) {
        view.setContentDescription(this.mContext.getString(getStringDescriptionResourceForRingerMode(i)));
        view.setAccessibilityDelegate(new View.AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, str));
            }
        });
    }

    public final int getStringDescriptionResourceForRingerMode(int i) {
        if (i == 0) {
            return R$string.volume_ringer_status_silent;
        }
        if (i != 1) {
            return R$string.volume_ringer_status_normal;
        }
        return R$string.volume_ringer_status_vibrate;
    }

    public final void enableVolumeRowViewsH(VolumeRow volumeRow, boolean z) {
        volumeRow.dndIcon.setVisibility(z ^ true ? 0 : 8);
    }

    public final void enableRingerViewsH(boolean z) {
        ImageButton imageButton = this.mRingerIcon;
        if (imageButton != null) {
            imageButton.setEnabled(z);
        }
        FrameLayout frameLayout = this.mZenIcon;
        if (frameLayout != null) {
            frameLayout.setVisibility(z ? 8 : 0);
        }
    }

    public final void trimObsoleteH() {
        if (D.BUG) {
            Log.d(TAG, "trimObsoleteH");
        }
        for (int size = this.mRows.size() - 1; size >= 0; size--) {
            VolumeRow volumeRow = this.mRows.get(size);
            if (volumeRow.ss != null && volumeRow.ss.dynamic && !this.mDynamic.get(volumeRow.stream)) {
                this.mRows.remove(size);
                this.mDialogRowsView.removeView(volumeRow.view);
                this.mConfigurableTexts.remove(volumeRow.header);
            }
        }
    }

    public void onStateChangedH(VolumeDialogController.State state) {
        int i;
        int i2;
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "onStateChangedH() state: " + state.toString());
        }
        VolumeDialogController.State state2 = this.mState;
        if (!(state2 == null || state == null || (i = state2.ringerModeInternal) == -1 || i == (i2 = state.ringerModeInternal) || i2 != 1)) {
            this.mController.vibrate(VibrationEffect.get(5));
        }
        this.mState = state;
        this.mDynamic.clear();
        for (int i3 = 0; i3 < state.states.size(); i3++) {
            int keyAt = state.states.keyAt(i3);
            if (state.states.valueAt(i3).dynamic) {
                this.mDynamic.put(keyAt, true);
                if (findRow(keyAt) == null) {
                    addRow(keyAt, R$drawable.ic_volume_remote, R$drawable.ic_volume_remote_mute, true, false, true);
                }
            }
        }
        int i4 = this.mActiveStream;
        int i5 = state.activeStream;
        if (i4 != i5) {
            this.mPrevActiveStream = i4;
            this.mActiveStream = i5;
            updateRowsH(getActiveRow());
            if (this.mShowing) {
                rescheduleTimeoutH();
            }
        }
        for (VolumeRow updateVolumeRowH : this.mRows) {
            updateVolumeRowH(updateVolumeRowH);
        }
        updateRingerH();
        this.mWindow.setTitle(composeWindowTitle());
    }

    public CharSequence composeWindowTitle() {
        return this.mContext.getString(R$string.volume_dialog_title, new Object[]{getStreamLabelH(getActiveRow().ss)});
    }

    public final void updateVolumeRowH(VolumeRow volumeRow) {
        VolumeDialogController.StreamState streamState;
        boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        VolumeRow volumeRow2 = volumeRow;
        if (D.BUG) {
            Log.i(TAG, "updateVolumeRowH s=" + volumeRow.stream);
        }
        VolumeDialogController.State state = this.mState;
        if (state != null && (streamState = state.states.get(volumeRow.stream)) != null) {
            volumeRow2.ss = streamState;
            int i5 = streamState.level;
            if (i5 > 0) {
                volumeRow2.lastAudibleLevel = i5;
            }
            if (streamState.level == volumeRow.requestedLevel) {
                volumeRow2.requestedLevel = -1;
            }
            int i6 = 0;
            boolean z2 = volumeRow.stream == 10;
            int i7 = 2;
            boolean z3 = volumeRow.stream == 2;
            boolean z4 = volumeRow.stream == 1;
            boolean z5 = volumeRow.stream == 4;
            boolean z6 = volumeRow.stream == 3;
            boolean z7 = z3 && this.mState.ringerModeInternal == 1;
            boolean z8 = z3 && this.mState.ringerModeInternal == 0;
            VolumeDialogController.State state2 = this.mState;
            int i8 = state2.zenMode;
            boolean z9 = i8 == 1;
            boolean z10 = i8 == 3;
            boolean z11 = i8 == 2;
            if (!z10 ? !z11 ? !z9 || ((!z5 || !state2.disallowAlarms) && ((!z6 || !state2.disallowMedia) && ((!z3 || !state2.disallowRinger) && (!z4 || !state2.disallowSystem)))) : !z3 && !z4 && !z5 && !z6 : !z3 && !z4) {
                z = false;
            } else {
                z = true;
            }
            int i9 = streamState.levelMax * 100;
            if (i9 != volumeRow.slider.getMax()) {
                volumeRow.slider.setMax(i9);
            }
            int i10 = streamState.levelMin * 100;
            if (i10 != volumeRow.slider.getMin()) {
                volumeRow.slider.setMin(i10);
            }
            Util.setText(volumeRow.header, getStreamLabelH(streamState));
            volumeRow.slider.setContentDescription(volumeRow.header.getText());
            this.mConfigurableTexts.add(volumeRow.header, streamState.name);
            boolean z12 = (this.mAutomute || streamState.muteSupported) && !z;
            if (z7) {
                i = R$drawable.ic_volume_ringer_vibrate;
            } else if (z8 || z) {
                i = volumeRow.iconMuteRes;
            } else if (streamState.routedToBluetooth) {
                i = isStreamMuted(streamState) ? R$drawable.ic_volume_media_bt_mute : R$drawable.ic_volume_media_bt;
            } else {
                i = isStreamMuted(streamState) ? streamState.muted ? R$drawable.ic_volume_media_off : volumeRow.iconMuteRes : (!this.mShowLowMediaVolumeIcon || streamState.level * 2 >= streamState.levelMax + streamState.levelMin) ? volumeRow.iconRes : R$drawable.ic_volume_media_low;
            }
            volumeRow2.setIcon(i, this.mContext.getTheme());
            if (i == R$drawable.ic_volume_ringer_vibrate) {
                i7 = 3;
            } else if (!(i == R$drawable.ic_volume_media_bt_mute || i == volumeRow.iconMuteRes)) {
                i7 = (i == R$drawable.ic_volume_media_bt || i == volumeRow.iconRes || i == R$drawable.ic_volume_media_low) ? 1 : 0;
            }
            volumeRow2.iconState = i7;
            if (volumeRow.icon != null) {
                if (!z12) {
                    volumeRow.icon.setContentDescription(getStreamLabelH(streamState));
                } else if (z3) {
                    if (z7) {
                        volumeRow.icon.setContentDescription(this.mContext.getString(R$string.volume_stream_content_description_unmute, new Object[]{getStreamLabelH(streamState)}));
                    } else if (this.mController.hasVibrator()) {
                        ImageButton r3 = volumeRow.icon;
                        Context context = this.mContext;
                        if (this.mShowA11yStream) {
                            i4 = R$string.volume_stream_content_description_vibrate_a11y;
                        } else {
                            i4 = R$string.volume_stream_content_description_vibrate;
                        }
                        r3.setContentDescription(context.getString(i4, new Object[]{getStreamLabelH(streamState)}));
                    } else {
                        ImageButton r32 = volumeRow.icon;
                        Context context2 = this.mContext;
                        if (this.mShowA11yStream) {
                            i3 = R$string.volume_stream_content_description_mute_a11y;
                        } else {
                            i3 = R$string.volume_stream_content_description_mute;
                        }
                        r32.setContentDescription(context2.getString(i3, new Object[]{getStreamLabelH(streamState)}));
                    }
                } else if (z2) {
                    volumeRow.icon.setContentDescription(getStreamLabelH(streamState));
                } else if (streamState.muted || (this.mAutomute && streamState.level == 0)) {
                    volumeRow.icon.setContentDescription(this.mContext.getString(R$string.volume_stream_content_description_unmute, new Object[]{getStreamLabelH(streamState)}));
                } else {
                    ImageButton r33 = volumeRow.icon;
                    Context context3 = this.mContext;
                    if (this.mShowA11yStream) {
                        i2 = R$string.volume_stream_content_description_mute_a11y;
                    } else {
                        i2 = R$string.volume_stream_content_description_mute;
                    }
                    r33.setContentDescription(context3.getString(i2, new Object[]{getStreamLabelH(streamState)}));
                }
            }
            if (z) {
                volumeRow2.tracking = false;
            }
            enableVolumeRowViewsH(volumeRow2, !z);
            boolean z13 = !z;
            if (!volumeRow.ss.muted || z3 || z) {
                i6 = volumeRow.ss.level;
            }
            Trace.beginSection("VolumeDialogImpl#updateVolumeRowSliderH");
            updateVolumeRowSliderH(volumeRow2, z13, i6);
            Trace.endSection();
            if (volumeRow.number != null) {
                volumeRow.number.setText(Integer.toString(i6));
            }
        }
    }

    public final boolean isStreamMuted(VolumeDialogController.StreamState streamState) {
        return (this.mAutomute && streamState.level == 0) || streamState.muted;
    }

    public final void updateVolumeRowTintH(VolumeRow volumeRow, boolean z) {
        ColorStateList colorStateList;
        int i;
        if (z) {
            volumeRow.slider.requestFocus();
        }
        boolean z2 = z && volumeRow.slider.isEnabled();
        if (z2 || this.mChangeVolumeRowTintWhenInactive) {
            if (z2) {
                colorStateList = Utils.getColorAccent(this.mContext);
            } else {
                colorStateList = Utils.getColorAttr(this.mContext, 17956902);
            }
            if (z2) {
                i = Color.alpha(colorStateList.getDefaultColor());
            } else {
                i = getAlphaAttr(16844115);
            }
            ColorStateList colorAttr = Utils.getColorAttr(this.mContext, 16844002);
            ColorStateList colorAttr2 = Utils.getColorAttr(this.mContext, 17957103);
            volumeRow.sliderProgressSolid.setTintList(colorStateList);
            if (volumeRow.sliderProgressIcon != null) {
                volumeRow.sliderProgressIcon.setTintList(colorAttr);
            }
            if (volumeRow.icon != null) {
                volumeRow.icon.setImageTintList(colorAttr2);
                volumeRow.icon.setImageAlpha(i);
            }
            if (volumeRow.number != null) {
                volumeRow.number.setTextColor(colorStateList);
                volumeRow.number.setAlpha((float) i);
            }
        }
    }

    public final void updateVolumeRowSliderH(VolumeRow volumeRow, boolean z, int i) {
        int i2;
        volumeRow.slider.setEnabled(z);
        updateVolumeRowTintH(volumeRow, volumeRow.stream == this.mActiveStream);
        if (!volumeRow.tracking) {
            int progress = volumeRow.slider.getProgress();
            int impliedLevel = getImpliedLevel(volumeRow.slider, progress);
            boolean z2 = volumeRow.view.getVisibility() == 0;
            boolean z3 = SystemClock.uptimeMillis() - volumeRow.userAttempt < 1000;
            this.mHandler.removeMessages(3, volumeRow);
            boolean z4 = this.mShowing;
            if (z4 && z2 && z3) {
                if (D.BUG) {
                    Log.d(TAG, "inGracePeriod");
                }
                H h = this.mHandler;
                h.sendMessageAtTime(h.obtainMessage(3, volumeRow), volumeRow.userAttempt + 1000);
            } else if ((i == impliedLevel && z4 && z2) || progress == (i2 = i * 100)) {
            } else {
                if (!z4 || !z2) {
                    if (volumeRow.anim != null) {
                        volumeRow.anim.cancel();
                    }
                    volumeRow.slider.setProgress(i2, true);
                } else if (volumeRow.anim == null || !volumeRow.anim.isRunning() || volumeRow.animTargetProgress != i2) {
                    if (volumeRow.anim == null) {
                        volumeRow.anim = ObjectAnimator.ofInt(volumeRow.slider, "progress", new int[]{progress, i2});
                        volumeRow.anim.setInterpolator(new DecelerateInterpolator());
                    } else {
                        volumeRow.anim.cancel();
                        volumeRow.anim.setIntValues(new int[]{progress, i2});
                    }
                    volumeRow.animTargetProgress = i2;
                    volumeRow.anim.setDuration(80);
                    volumeRow.anim.addListener(getJankListener(volumeRow.view, "update", 80));
                    volumeRow.anim.start();
                }
            }
        }
    }

    public final void recheckH(VolumeRow volumeRow) {
        if (volumeRow == null) {
            if (D.BUG) {
                Log.d(TAG, "recheckH ALL");
            }
            trimObsoleteH();
            for (VolumeRow updateVolumeRowH : this.mRows) {
                updateVolumeRowH(updateVolumeRowH);
            }
            return;
        }
        if (D.BUG) {
            String str = TAG;
            Log.d(str, "recheckH " + volumeRow.stream);
        }
        updateVolumeRowH(volumeRow);
    }

    public final void setStreamImportantH(int i, boolean z) {
        for (VolumeRow next : this.mRows) {
            if (next.stream == i) {
                next.important = z;
                return;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0024, code lost:
        recheckH((com.android.systemui.volume.VolumeDialogImpl.VolumeRow) null);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void showSafetyWarningH(int r4) {
        /*
            r3 = this;
            r4 = r4 & 1025(0x401, float:1.436E-42)
            if (r4 != 0) goto L_0x0008
            boolean r4 = r3.mShowing
            if (r4 == 0) goto L_0x0028
        L_0x0008:
            java.lang.Object r4 = r3.mSafetyWarningLock
            monitor-enter(r4)
            com.android.systemui.volume.SafetyWarningDialog r0 = r3.mSafetyWarning     // Catch:{ all -> 0x002c }
            if (r0 == 0) goto L_0x0011
            monitor-exit(r4)     // Catch:{ all -> 0x002c }
            return
        L_0x0011:
            com.android.systemui.volume.VolumeDialogImpl$5 r0 = new com.android.systemui.volume.VolumeDialogImpl$5     // Catch:{ all -> 0x002c }
            android.content.Context r1 = r3.mContext     // Catch:{ all -> 0x002c }
            com.android.systemui.plugins.VolumeDialogController r2 = r3.mController     // Catch:{ all -> 0x002c }
            android.media.AudioManager r2 = r2.getAudioManager()     // Catch:{ all -> 0x002c }
            r0.<init>(r1, r2)     // Catch:{ all -> 0x002c }
            r3.mSafetyWarning = r0     // Catch:{ all -> 0x002c }
            r0.show()     // Catch:{ all -> 0x002c }
            monitor-exit(r4)     // Catch:{ all -> 0x002c }
            r4 = 0
            r3.recheckH(r4)
        L_0x0028:
            r3.rescheduleTimeoutH()
            return
        L_0x002c:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x002c }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.volume.VolumeDialogImpl.showSafetyWarningH(int):void");
    }

    public final String getStreamLabelH(VolumeDialogController.StreamState streamState) {
        if (streamState == null) {
            return "";
        }
        String str = streamState.remoteLabel;
        if (str != null) {
            return str;
        }
        try {
            return this.mContext.getResources().getString(streamState.name);
        } catch (Resources.NotFoundException unused) {
            String str2 = TAG;
            Slog.e(str2, "Can't find translation for stream " + streamState);
            return "";
        }
    }

    public final Runnable getSinglePressFor(ImageButton imageButton) {
        return new VolumeDialogImpl$$ExternalSyntheticLambda20(this, imageButton);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getSinglePressFor$21(ImageButton imageButton) {
        if (imageButton != null) {
            imageButton.setPressed(true);
            imageButton.postOnAnimationDelayed(getSingleUnpressFor(imageButton), 200);
        }
    }

    public final Runnable getSingleUnpressFor(ImageButton imageButton) {
        return new VolumeDialogImpl$$ExternalSyntheticLambda22(imageButton);
    }

    public static /* synthetic */ void lambda$getSingleUnpressFor$22(ImageButton imageButton) {
        if (imageButton != null) {
            imageButton.setPressed(false);
        }
    }

    public final int getRingerDrawerOpenExtraSize() {
        return (this.mRingerCount - 1) * this.mRingerDrawerItemSize;
    }

    public final void updateBackgroundForDrawerClosedAmount() {
        Drawable drawable = this.mRingerAndDrawerContainerBackground;
        if (drawable != null) {
            Rect copyBounds = drawable.copyBounds();
            if (!isLandscape()) {
                copyBounds.top = (int) (this.mRingerDrawerClosedAmount * ((float) getRingerDrawerOpenExtraSize()));
            } else {
                copyBounds.left = (int) (this.mRingerDrawerClosedAmount * ((float) getRingerDrawerOpenExtraSize()));
            }
            this.mRingerAndDrawerContainerBackground.setBounds(copyBounds);
        }
    }

    public final void setTopContainerBackgroundDrawable() {
        int i;
        int i2;
        if (this.mTopContainer != null) {
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{new ColorDrawable(Utils.getColorAttrDefaultColor(this.mContext, 17956909))});
            int i3 = this.mDialogWidth;
            if (!isLandscape()) {
                i = this.mDialogRowsView.getHeight();
            } else {
                i = this.mDialogRowsView.getHeight() + this.mDialogCornerRadius;
            }
            layerDrawable.setLayerSize(0, i3, i);
            if (!isLandscape()) {
                i2 = this.mDialogRowsViewContainer.getTop();
            } else {
                i2 = this.mDialogRowsViewContainer.getTop() - this.mDialogCornerRadius;
            }
            layerDrawable.setLayerInsetTop(0, i2);
            layerDrawable.setLayerGravity(0, 53);
            if (isLandscape()) {
                this.mRingerAndDrawerContainer.setOutlineProvider(new ViewOutlineProvider() {
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), (float) VolumeDialogImpl.this.mDialogCornerRadius);
                    }
                });
                this.mRingerAndDrawerContainer.setClipToOutline(true);
            }
            this.mTopContainer.setBackground(layerDrawable);
        }
    }

    public final class H extends Handler {
        public H() {
            super(Looper.getMainLooper());
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                    volumeDialogImpl.showH(message.arg1, volumeDialogImpl.mKeyguard.isKeyguardLocked(), VolumeDialogImpl.this.mActivityManager.getLockTaskModeState());
                    return;
                case 2:
                    VolumeDialogImpl.this.dismissH(message.arg1);
                    return;
                case 3:
                    VolumeDialogImpl.this.recheckH((VolumeRow) message.obj);
                    return;
                case 4:
                    VolumeDialogImpl.this.recheckH((VolumeRow) null);
                    return;
                case 5:
                    VolumeDialogImpl.this.setStreamImportantH(message.arg1, message.arg2 != 0);
                    return;
                case 6:
                    VolumeDialogImpl.this.rescheduleTimeoutH();
                    return;
                case 7:
                    VolumeDialogImpl volumeDialogImpl2 = VolumeDialogImpl.this;
                    volumeDialogImpl2.onStateChangedH(volumeDialogImpl2.mState);
                    return;
                default:
                    return;
            }
        }
    }

    public final class CustomDialog extends Dialog {
        public CustomDialog(Context context) {
            super(context, R$style.volume_dialog_theme);
        }

        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            VolumeDialogImpl.this.rescheduleTimeoutH();
            return super.dispatchTouchEvent(motionEvent);
        }

        public void onStart() {
            super.setCanceledOnTouchOutside(true);
            super.onStart();
        }

        public void onStop() {
            super.onStop();
            VolumeDialogImpl.this.mHandler.sendEmptyMessage(4);
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (!VolumeDialogImpl.this.mShowing || motionEvent.getAction() != 4) {
                return false;
            }
            VolumeDialogImpl.this.dismissH(1);
            return true;
        }
    }

    public final class VolumeSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        public final VolumeRow mRow;

        public VolumeSeekBarChangeListener(VolumeRow volumeRow) {
            this.mRow = volumeRow;
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            int i2;
            if (this.mRow.ss != null) {
                if (D.BUG) {
                    String r0 = VolumeDialogImpl.TAG;
                    Log.d(r0, AudioSystem.streamToString(this.mRow.stream) + " onProgressChanged " + i + " fromUser=" + z);
                }
                if (z) {
                    if (this.mRow.ss.levelMin > 0 && i < (i2 = this.mRow.ss.levelMin * 100)) {
                        seekBar.setProgress(i2);
                        i = i2;
                    }
                    int r4 = VolumeDialogImpl.getImpliedLevel(seekBar, i);
                    if (this.mRow.ss.level != r4 || (this.mRow.ss.muted && r4 > 0)) {
                        this.mRow.userAttempt = SystemClock.uptimeMillis();
                        if (this.mRow.requestedLevel != r4) {
                            VolumeDialogImpl.this.mController.setActiveStream(this.mRow.stream);
                            VolumeDialogImpl.this.mController.setStreamVolume(this.mRow.stream, r4);
                            this.mRow.requestedLevel = r4;
                            Events.writeEvent(9, Integer.valueOf(this.mRow.stream), Integer.valueOf(r4));
                        }
                    }
                }
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            if (D.BUG) {
                String r3 = VolumeDialogImpl.TAG;
                Log.d(r3, "onStartTrackingTouch " + this.mRow.stream);
            }
            VolumeDialogImpl.this.mController.setActiveStream(this.mRow.stream);
            this.mRow.tracking = true;
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (D.BUG) {
                String r0 = VolumeDialogImpl.TAG;
                Log.d(r0, "onStopTrackingTouch " + this.mRow.stream);
            }
            this.mRow.tracking = false;
            this.mRow.userAttempt = SystemClock.uptimeMillis();
            int r5 = VolumeDialogImpl.getImpliedLevel(seekBar, seekBar.getProgress());
            Events.writeEvent(16, Integer.valueOf(this.mRow.stream), Integer.valueOf(r5));
            if (this.mRow.ss.level != r5) {
                VolumeDialogImpl.this.mHandler.sendMessageDelayed(VolumeDialogImpl.this.mHandler.obtainMessage(3, this.mRow), 1000);
            }
        }
    }

    public final class Accessibility extends View.AccessibilityDelegate {
        public Accessibility() {
        }

        public void init() {
            VolumeDialogImpl.this.mDialogView.setAccessibilityDelegate(this);
        }

        public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            accessibilityEvent.getText().add(VolumeDialogImpl.this.composeWindowTitle());
            return true;
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            VolumeDialogImpl.this.rescheduleTimeoutH();
            return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }

    public static class VolumeRow {
        public ObjectAnimator anim;
        public int animTargetProgress;
        public boolean defaultStream;
        public FrameLayout dndIcon;
        public TextView header;
        public ImageButton icon;
        public int iconMuteRes;
        public int iconRes;
        public int iconState;
        public boolean important;
        public int lastAudibleLevel;
        public TextView number;
        public int requestedLevel;
        public SeekBar slider;
        public AlphaTintDrawableWrapper sliderProgressIcon;
        public Drawable sliderProgressSolid;
        public VolumeDialogController.StreamState ss;
        public int stream;
        public boolean tracking;
        public long userAttempt;
        public View view;

        public VolumeRow() {
            this.requestedLevel = -1;
            this.lastAudibleLevel = 1;
        }

        public void setIcon(int i, Resources.Theme theme) {
            ImageButton imageButton = this.icon;
            if (imageButton != null) {
                imageButton.setImageResource(i);
            }
            AlphaTintDrawableWrapper alphaTintDrawableWrapper = this.sliderProgressIcon;
            if (alphaTintDrawableWrapper != null) {
                alphaTintDrawableWrapper.setDrawable(this.view.getResources().getDrawable(i, theme));
            }
        }
    }

    public class RingerDrawerItemClickListener implements View.OnClickListener {
        public final int mClickedRingerMode;

        public RingerDrawerItemClickListener(int i) {
            this.mClickedRingerMode = i;
        }

        public void onClick(View view) {
            if (VolumeDialogImpl.this.mIsRingerDrawerOpen) {
                VolumeDialogImpl.this.setRingerMode(this.mClickedRingerMode);
                VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                volumeDialogImpl.mRingerDrawerIconAnimatingSelected = volumeDialogImpl.getDrawerIconViewForMode(this.mClickedRingerMode);
                VolumeDialogImpl volumeDialogImpl2 = VolumeDialogImpl.this;
                volumeDialogImpl2.mRingerDrawerIconAnimatingDeselected = volumeDialogImpl2.getDrawerIconViewForMode(volumeDialogImpl2.mState.ringerModeInternal);
                VolumeDialogImpl.this.mRingerDrawerIconColorAnimator.start();
                VolumeDialogImpl.this.mSelectedRingerContainer.setVisibility(4);
                VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.setAlpha(1.0f);
                VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.animate().setInterpolator(Interpolators.ACCELERATE_DECELERATE).setDuration(175).withEndAction(new VolumeDialogImpl$RingerDrawerItemClickListener$$ExternalSyntheticLambda0(this));
                if (!VolumeDialogImpl.this.isLandscape()) {
                    VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.animate().translationY(VolumeDialogImpl.this.getTranslationInDrawerForRingerMode(this.mClickedRingerMode)).start();
                } else {
                    VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.animate().translationX(VolumeDialogImpl.this.getTranslationInDrawerForRingerMode(this.mClickedRingerMode)).start();
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onClick$0() {
            VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.setAlpha(0.0f);
            if (!VolumeDialogImpl.this.isLandscape()) {
                VolumeDialogImpl.this.mSelectedRingerContainer.setTranslationY(VolumeDialogImpl.this.getTranslationInDrawerForRingerMode(this.mClickedRingerMode));
            } else {
                VolumeDialogImpl.this.mSelectedRingerContainer.setTranslationX(VolumeDialogImpl.this.getTranslationInDrawerForRingerMode(this.mClickedRingerMode));
            }
            VolumeDialogImpl.this.mSelectedRingerContainer.setVisibility(0);
            VolumeDialogImpl.this.hideRingerDrawer();
        }
    }
}
