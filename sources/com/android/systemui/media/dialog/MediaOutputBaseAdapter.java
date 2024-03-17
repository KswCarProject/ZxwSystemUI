package com.android.systemui.media.dialog;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.WallpaperColors;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settingslib.Utils;
import com.android.settingslib.media.MediaDevice;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import java.util.List;

public abstract class MediaOutputBaseAdapter extends RecyclerView.Adapter<MediaDeviceBaseViewHolder> {
    public Context mContext;
    public final MediaOutputController mController;
    public int mCurrentActivePosition = -1;
    public View mHolderView;
    public boolean mIsDragging = false;
    public boolean mIsInitVolumeFirstTime = true;
    public int mMargin;

    public MediaOutputBaseAdapter(MediaOutputController mediaOutputController) {
        this.mController = mediaOutputController;
    }

    public MediaDeviceBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        this.mContext = context;
        this.mMargin = context.getResources().getDimensionPixelSize(R$dimen.media_output_dialog_list_margin);
        this.mHolderView = LayoutInflater.from(this.mContext).inflate(R$layout.media_output_list_item, viewGroup, false);
        return null;
    }

    public void updateColorScheme(WallpaperColors wallpaperColors, boolean z) {
        this.mController.setCurrentColorScheme(wallpaperColors, z);
    }

    public CharSequence getItemTitle(MediaDevice mediaDevice) {
        return mediaDevice.getName();
    }

    public boolean isCurrentlyConnected(MediaDevice mediaDevice) {
        if (TextUtils.equals(mediaDevice.getId(), this.mController.getCurrentConnectedMediaDevice().getId())) {
            return true;
        }
        if (this.mController.getSelectedMediaDevice().size() != 1 || !isDeviceIncluded(this.mController.getSelectedMediaDevice(), mediaDevice)) {
            return false;
        }
        return true;
    }

    public boolean isDeviceIncluded(List<MediaDevice> list, MediaDevice mediaDevice) {
        for (MediaDevice id : list) {
            if (TextUtils.equals(id.getId(), mediaDevice.getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isDragging() {
        return this.mIsDragging;
    }

    public int getCurrentActivePosition() {
        return this.mCurrentActivePosition;
    }

    public MediaOutputController getController() {
        return this.mController;
    }

    public abstract class MediaDeviceBaseViewHolder extends RecyclerView.ViewHolder {
        public final CheckBox mCheckBox;
        public final LinearLayout mContainerLayout;
        public ValueAnimator mCornerAnimator;
        public String mDeviceId;
        public final LinearLayout mEndTouchArea;
        public final FrameLayout mItemLayout;
        public final ProgressBar mProgressBar;
        public final MediaOutputSeekbar mSeekBar;
        public final ImageView mStatusIcon;
        public final TextView mSubTitleText;
        public final ImageView mTitleIcon;
        public final TextView mTitleText;
        public final LinearLayout mTwoLineLayout;
        public final TextView mTwoLineTitleText;
        public ValueAnimator mVolumeAnimator;

        public static /* synthetic */ boolean lambda$disableSeekBar$2(View view, MotionEvent motionEvent) {
            return true;
        }

        public abstract void onBind(int i, boolean z, boolean z2);

        public MediaDeviceBaseViewHolder(View view) {
            super(view);
            this.mContainerLayout = (LinearLayout) view.requireViewById(R$id.device_container);
            this.mItemLayout = (FrameLayout) view.requireViewById(R$id.item_layout);
            this.mTitleText = (TextView) view.requireViewById(R$id.title);
            this.mSubTitleText = (TextView) view.requireViewById(R$id.subtitle);
            this.mTwoLineLayout = (LinearLayout) view.requireViewById(R$id.two_line_layout);
            this.mTwoLineTitleText = (TextView) view.requireViewById(R$id.two_line_title);
            this.mTitleIcon = (ImageView) view.requireViewById(R$id.title_icon);
            this.mProgressBar = (ProgressBar) view.requireViewById(R$id.volume_indeterminate_progress);
            this.mSeekBar = (MediaOutputSeekbar) view.requireViewById(R$id.volume_seekbar);
            this.mStatusIcon = (ImageView) view.requireViewById(R$id.media_output_item_status);
            this.mCheckBox = (CheckBox) view.requireViewById(R$id.check_box);
            this.mEndTouchArea = (LinearLayout) view.requireViewById(R$id.end_action_area);
            initAnimator();
        }

        public void onBind(MediaDevice mediaDevice, boolean z, boolean z2, int i) {
            this.mDeviceId = mediaDevice.getId();
        }

        public void setSingleLineLayout(CharSequence charSequence, boolean z) {
            setSingleLineLayout(charSequence, z, false, false, false);
        }

        public void setSingleLineLayout(CharSequence charSequence, boolean z, boolean z2, boolean z3, boolean z4) {
            int i;
            Drawable drawable;
            int i2;
            int i3 = 8;
            this.mTwoLineLayout.setVisibility(8);
            boolean z5 = z2 || z3;
            if (!this.mCornerAnimator.isRunning()) {
                if (z2) {
                    drawable = MediaOutputBaseAdapter.this.mContext.getDrawable(R$drawable.media_output_item_background_active).mutate();
                } else {
                    drawable = MediaOutputBaseAdapter.this.mContext.getDrawable(R$drawable.media_output_item_background).mutate();
                }
                if (z5) {
                    i2 = MediaOutputBaseAdapter.this.mController.getColorConnectedItemBackground();
                } else {
                    i2 = MediaOutputBaseAdapter.this.mController.getColorItemBackground();
                }
                drawable.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.SRC_IN));
                this.mItemLayout.setBackground(drawable);
                if (z2) {
                    ((GradientDrawable) ((ClipDrawable) ((LayerDrawable) this.mSeekBar.getProgressDrawable()).findDrawableByLayerId(16908301)).getDrawable()).setCornerRadius(MediaOutputBaseAdapter.this.mController.getActiveRadius());
                }
            } else {
                Drawable background = this.mItemLayout.getBackground();
                if (z5) {
                    i = MediaOutputBaseAdapter.this.mController.getColorConnectedItemBackground();
                } else {
                    i = MediaOutputBaseAdapter.this.mController.getColorItemBackground();
                }
                background.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
            }
            this.mProgressBar.setVisibility(z3 ? 0 : 8);
            this.mSeekBar.setAlpha(1.0f);
            this.mSeekBar.setVisibility(z2 ? 0 : 8);
            if (!z2) {
                this.mSeekBar.resetVolume();
            }
            ImageView imageView = this.mStatusIcon;
            if (z4) {
                i3 = 0;
            }
            imageView.setVisibility(i3);
            this.mTitleText.setText(charSequence);
            this.mTitleText.setVisibility(0);
        }

        public void setTwoLineLayout(MediaDevice mediaDevice, boolean z, boolean z2, boolean z3, boolean z4) {
            setTwoLineLayout(mediaDevice, (CharSequence) null, z, z2, z3, z4, false);
        }

        public void setTwoLineLayout(MediaDevice mediaDevice, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
            setTwoLineLayout(mediaDevice, (CharSequence) null, z, z2, z3, z4, z5);
        }

        public void setTwoLineLayout(CharSequence charSequence, boolean z, boolean z2, boolean z3, boolean z4) {
            setTwoLineLayout((MediaDevice) null, charSequence, z, z2, z3, z4, false);
        }

        public final void setTwoLineLayout(MediaDevice mediaDevice, CharSequence charSequence, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
            int i = 8;
            this.mTitleText.setVisibility(8);
            this.mTwoLineLayout.setVisibility(0);
            this.mStatusIcon.setVisibility(z5 ? 0 : 8);
            this.mSeekBar.setAlpha(1.0f);
            this.mSeekBar.setVisibility(z2 ? 0 : 8);
            Drawable mutate = MediaOutputBaseAdapter.this.mContext.getDrawable(R$drawable.media_output_item_background).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(MediaOutputBaseAdapter.this.mController.getColorItemBackground(), PorterDuff.Mode.SRC_IN));
            this.mItemLayout.setBackground(mutate);
            this.mProgressBar.setVisibility(z3 ? 0 : 8);
            TextView textView = this.mSubTitleText;
            if (z4) {
                i = 0;
            }
            textView.setVisibility(i);
            this.mTwoLineTitleText.setTranslationY(0.0f);
            if (mediaDevice == null) {
                this.mTwoLineTitleText.setText(charSequence);
            } else {
                this.mTwoLineTitleText.setText(MediaOutputBaseAdapter.this.getItemTitle(mediaDevice));
            }
            if (z) {
                this.mTwoLineTitleText.setTypeface(Typeface.create(MediaOutputBaseAdapter.this.mContext.getString(17039986), 0));
            } else {
                this.mTwoLineTitleText.setTypeface(Typeface.create(MediaOutputBaseAdapter.this.mContext.getString(17039985), 0));
            }
        }

        public void initSeekbar(final MediaDevice mediaDevice, boolean z) {
            if (!MediaOutputBaseAdapter.this.mController.isVolumeControlEnabled(mediaDevice)) {
                disableSeekBar();
            }
            this.mSeekBar.setMaxVolume(mediaDevice.getMaxVolume());
            int currentVolume = mediaDevice.getCurrentVolume();
            if (this.mSeekBar.getVolume() != currentVolume) {
                if (z && !MediaOutputBaseAdapter.this.mIsInitVolumeFirstTime) {
                    animateCornerAndVolume(this.mSeekBar.getProgress(), MediaOutputSeekbar.scaleVolumeToProgress(currentVolume));
                } else if (!this.mVolumeAnimator.isStarted()) {
                    this.mSeekBar.setVolume(currentVolume);
                } else {
                    endAnimateCornerAndVolume();
                    this.mSeekBar.setVolume(currentVolume);
                }
            }
            if (MediaOutputBaseAdapter.this.mIsInitVolumeFirstTime) {
                MediaOutputBaseAdapter.this.mIsInitVolumeFirstTime = false;
            }
            this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    int scaleProgressToVolume;
                    if (mediaDevice != null && z && (scaleProgressToVolume = MediaOutputSeekbar.scaleProgressToVolume(i)) != mediaDevice.getCurrentVolume()) {
                        MediaOutputBaseAdapter.this.mController.adjustVolume(mediaDevice, scaleProgressToVolume);
                    }
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    MediaOutputBaseAdapter.this.mIsDragging = true;
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    MediaOutputBaseAdapter.this.mIsDragging = false;
                }
            });
        }

        public void initMutingExpectedDevice() {
            disableSeekBar();
            Drawable mutate = MediaOutputBaseAdapter.this.mContext.getDrawable(R$drawable.media_output_item_background_active).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(MediaOutputBaseAdapter.this.mController.getColorConnectedItemBackground(), PorterDuff.Mode.SRC_IN));
            this.mItemLayout.setBackground(mutate);
        }

        public void initSessionSeekbar() {
            disableSeekBar();
            this.mSeekBar.setMax(MediaOutputBaseAdapter.this.mController.getSessionVolumeMax());
            this.mSeekBar.setMin(0);
            int sessionVolume = MediaOutputBaseAdapter.this.mController.getSessionVolume();
            if (this.mSeekBar.getProgress() != sessionVolume) {
                this.mSeekBar.setProgress(sessionVolume, true);
            }
            this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    if (z) {
                        MediaOutputBaseAdapter.this.mController.adjustSessionVolume(i);
                    }
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    MediaOutputBaseAdapter.this.mIsDragging = true;
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    MediaOutputBaseAdapter.this.mIsDragging = false;
                }
            });
        }

        public final void animateCornerAndVolume(int i, int i2) {
            this.mCornerAnimator.addUpdateListener(new MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda3((GradientDrawable) this.mItemLayout.getBackground(), (GradientDrawable) ((ClipDrawable) ((LayerDrawable) this.mSeekBar.getProgressDrawable()).findDrawableByLayerId(16908301)).getDrawable()));
            this.mVolumeAnimator.setIntValues(new int[]{i, i2});
            this.mVolumeAnimator.start();
            this.mCornerAnimator.start();
        }

        public static /* synthetic */ void lambda$animateCornerAndVolume$0(GradientDrawable gradientDrawable, GradientDrawable gradientDrawable2, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            gradientDrawable.setCornerRadius(floatValue);
            gradientDrawable2.setCornerRadius(floatValue);
        }

        public final void endAnimateCornerAndVolume() {
            this.mVolumeAnimator.end();
            this.mCornerAnimator.end();
        }

        public final void initAnimator() {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{MediaOutputBaseAdapter.this.mController.getInactiveRadius(), MediaOutputBaseAdapter.this.mController.getActiveRadius()});
            this.mCornerAnimator = ofFloat;
            ofFloat.setDuration(500);
            this.mCornerAnimator.setInterpolator(new LinearInterpolator());
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[0]);
            this.mVolumeAnimator = ofInt;
            ofInt.addUpdateListener(new MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda0(this));
            this.mVolumeAnimator.setDuration(500);
            this.mVolumeAnimator.setInterpolator(new LinearInterpolator());
            this.mVolumeAnimator.addListener(new Animator.AnimatorListener() {
                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                    MediaDeviceBaseViewHolder.this.mSeekBar.setEnabled(false);
                }

                public void onAnimationEnd(Animator animator) {
                    MediaDeviceBaseViewHolder.this.mSeekBar.setEnabled(true);
                }

                public void onAnimationCancel(Animator animator) {
                    MediaDeviceBaseViewHolder.this.mSeekBar.setEnabled(true);
                }
            });
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$initAnimator$1(ValueAnimator valueAnimator) {
            this.mSeekBar.setProgress(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }

        public Drawable getSpeakerDrawable() {
            Drawable mutate = MediaOutputBaseAdapter.this.mContext.getDrawable(R$drawable.ic_speaker_group_black_24dp).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(Utils.getColorStateListDefaultColor(MediaOutputBaseAdapter.this.mContext, R$color.media_dialog_item_main_content), PorterDuff.Mode.SRC_IN));
            return mutate;
        }

        public final void disableSeekBar() {
            this.mSeekBar.setEnabled(false);
            this.mSeekBar.setOnTouchListener(new MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda2());
        }

        public void setUpDeviceIcon(MediaDevice mediaDevice) {
            ThreadUtils.postOnBackgroundThread(new MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda1(this, mediaDevice));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setUpDeviceIcon$4(MediaDevice mediaDevice) {
            ThreadUtils.postOnMainThread(new MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda4(this, mediaDevice, MediaOutputBaseAdapter.this.mController.getDeviceIconCompat(mediaDevice).toIcon(MediaOutputBaseAdapter.this.mContext)));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setUpDeviceIcon$3(MediaDevice mediaDevice, Icon icon) {
            if (TextUtils.equals(this.mDeviceId, mediaDevice.getId())) {
                this.mTitleIcon.setImageIcon(icon);
            }
        }
    }
}
