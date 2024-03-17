package com.android.systemui.media.dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import androidx.core.widget.CompoundButtonCompat;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.media.dialog.MediaOutputBaseAdapter;
import java.util.List;
import java.util.Objects;

public class MediaOutputAdapter extends MediaOutputBaseAdapter {
    public static final boolean DEBUG = Log.isLoggable("MediaOutputAdapter", 3);
    public ViewGroup mConnectedItem;
    public boolean mIncludeDynamicGroup;
    public final MediaOutputDialog mMediaOutputDialog;

    public MediaOutputAdapter(MediaOutputController mediaOutputController, MediaOutputDialog mediaOutputDialog) {
        super(mediaOutputController);
        this.mMediaOutputDialog = mediaOutputDialog;
        setHasStableIds(true);
    }

    public MediaOutputBaseAdapter.MediaDeviceBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        super.onCreateViewHolder(viewGroup, i);
        return new MediaDeviceViewHolder(this.mHolderView);
    }

    public void onBindViewHolder(MediaOutputBaseAdapter.MediaDeviceBaseViewHolder mediaDeviceBaseViewHolder, int i) {
        int size = this.mController.getMediaDevices().size();
        boolean z = false;
        if (i == size && this.mController.isZeroMode()) {
            mediaDeviceBaseViewHolder.onBind(1, false, true);
        } else if (i < size) {
            MediaDevice mediaDevice = (MediaDevice) ((List) this.mController.getMediaDevices()).get(i);
            boolean z2 = i == 0;
            if (i == size - 1) {
                z = true;
            }
            mediaDeviceBaseViewHolder.onBind(mediaDevice, z2, z, i);
        } else if (DEBUG) {
            Log.d("MediaOutputAdapter", "Incorrect position: " + i);
        }
    }

    public long getItemId(int i) {
        int size = this.mController.getMediaDevices().size();
        if (i == size && this.mController.isZeroMode()) {
            return -1;
        }
        if (i < size) {
            return (long) ((MediaDevice) ((List) this.mController.getMediaDevices()).get(i)).getId().hashCode();
        }
        if (DEBUG) {
            Log.d("MediaOutputAdapter", "Incorrect position for item id: " + i);
        }
        return (long) i;
    }

    public int getItemCount() {
        if (this.mController.isZeroMode()) {
            return this.mController.getMediaDevices().size() + 1;
        }
        return this.mController.getMediaDevices().size();
    }

    public class MediaDeviceViewHolder extends MediaOutputBaseAdapter.MediaDeviceBaseViewHolder {
        public MediaDeviceViewHolder(View view) {
            super(view);
        }

        public void onBind(MediaDevice mediaDevice, boolean z, boolean z2, int i) {
            super.onBind(mediaDevice, z, z2, i);
            boolean hasMutingExpectedDevice = MediaOutputAdapter.this.mController.hasMutingExpectedDevice();
            boolean z3 = !MediaOutputAdapter.this.mIncludeDynamicGroup && MediaOutputAdapter.this.isCurrentlyConnected(mediaDevice);
            boolean z4 = this.mSeekBar.getVisibility() == 8;
            if (z3) {
                MediaOutputAdapter.this.mConnectedItem = this.mContainerLayout;
            }
            this.mCheckBox.setVisibility(8);
            this.mStatusIcon.setVisibility(8);
            this.mEndTouchArea.setVisibility(8);
            this.mEndTouchArea.setImportantForAccessibility(2);
            MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda3 mediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda3 = null;
            this.mContainerLayout.setOnClickListener((View.OnClickListener) null);
            this.mContainerLayout.setContentDescription((CharSequence) null);
            this.mTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
            this.mSubTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
            this.mTwoLineTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
            this.mSeekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(MediaOutputAdapter.this.mController.getColorSeekbarProgress(), PorterDuff.Mode.SRC_IN));
            MediaOutputAdapter mediaOutputAdapter = MediaOutputAdapter.this;
            if (mediaOutputAdapter.mCurrentActivePosition == i) {
                mediaOutputAdapter.mCurrentActivePosition = -1;
            }
            if (mediaOutputAdapter.mController.isTransferring()) {
                if (mediaDevice.getState() != 1 || MediaOutputAdapter.this.mController.hasAdjustVolumeUserRestriction()) {
                    setUpDeviceIcon(mediaDevice);
                    setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), false);
                    return;
                }
                setUpDeviceIcon(mediaDevice);
                this.mProgressBar.getIndeterminateDrawable().setColorFilter(new PorterDuffColorFilter(MediaOutputAdapter.this.mController.getColorItemContent(), PorterDuff.Mode.SRC_IN));
                setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), true, false, true, false);
            } else if (mediaDevice.isMutingExpectedDevice() && !MediaOutputAdapter.this.mController.isCurrentConnectedDeviceRemote()) {
                this.mTitleIcon.setImageDrawable(MediaOutputAdapter.this.mContext.getDrawable(R$drawable.media_output_icon_volume));
                this.mTitleIcon.setColorFilter(MediaOutputAdapter.this.mController.getColorItemContent());
                this.mTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
                setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), true, false, false, false);
                initMutingExpectedDevice();
                MediaOutputAdapter.this.mCurrentActivePosition = i;
                this.mContainerLayout.setOnClickListener(new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda0(this, mediaDevice));
            } else if (mediaDevice.getState() == 3) {
                setUpDeviceIcon(mediaDevice);
                this.mStatusIcon.setImageDrawable(MediaOutputAdapter.this.mContext.getDrawable(R$drawable.media_output_status_failed));
                this.mStatusIcon.setColorFilter(MediaOutputAdapter.this.mController.getColorItemContent());
                setTwoLineLayout(mediaDevice, false, false, false, true, true);
                this.mSubTitleText.setText(R$string.media_output_dialog_connect_failed);
                this.mContainerLayout.setOnClickListener(new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda1(this, mediaDevice));
            } else if (mediaDevice.getState() == 5) {
                setUpDeviceIcon(mediaDevice);
                this.mProgressBar.getIndeterminateDrawable().setColorFilter(new PorterDuffColorFilter(MediaOutputAdapter.this.mController.getColorItemContent(), PorterDuff.Mode.SRC_IN));
                setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), true, false, true, false);
            } else {
                if (MediaOutputAdapter.this.mController.getSelectedMediaDevice().size() > 1) {
                    MediaOutputAdapter mediaOutputAdapter2 = MediaOutputAdapter.this;
                    if (mediaOutputAdapter2.isDeviceIncluded(mediaOutputAdapter2.mController.getSelectedMediaDevice(), mediaDevice)) {
                        MediaOutputAdapter mediaOutputAdapter3 = MediaOutputAdapter.this;
                        boolean isDeviceIncluded = mediaOutputAdapter3.isDeviceIncluded(mediaOutputAdapter3.mController.getDeselectableMediaDevice(), mediaDevice);
                        this.mTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
                        this.mTitleIcon.setImageDrawable(MediaOutputAdapter.this.mContext.getDrawable(R$drawable.media_output_icon_volume));
                        this.mTitleIcon.setColorFilter(MediaOutputAdapter.this.mController.getColorItemContent());
                        setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), true, true, false, false);
                        setUpContentDescriptionForView(this.mContainerLayout, false, mediaDevice);
                        this.mCheckBox.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
                        this.mCheckBox.setVisibility(0);
                        this.mCheckBox.setChecked(true);
                        this.mCheckBox.setOnCheckedChangeListener(isDeviceIncluded ? new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda2(this, mediaDevice) : null);
                        this.mCheckBox.setEnabled(isDeviceIncluded);
                        setCheckBoxColor(this.mCheckBox, MediaOutputAdapter.this.mController.getColorItemContent());
                        initSeekbar(mediaDevice, z4);
                        this.mEndTouchArea.setVisibility(0);
                        this.mEndTouchArea.setOnClickListener((View.OnClickListener) null);
                        LinearLayout linearLayout = this.mEndTouchArea;
                        if (isDeviceIncluded) {
                            mediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda3 = new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda3(this);
                        }
                        linearLayout.setOnClickListener(mediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda3);
                        this.mEndTouchArea.setImportantForAccessibility(1);
                        setUpContentDescriptionForView(this.mEndTouchArea, true, mediaDevice);
                        return;
                    }
                }
                if (MediaOutputAdapter.this.mController.hasAdjustVolumeUserRestriction() || !z3) {
                    MediaOutputAdapter mediaOutputAdapter4 = MediaOutputAdapter.this;
                    if (mediaOutputAdapter4.isDeviceIncluded(mediaOutputAdapter4.mController.getSelectableMediaDevice(), mediaDevice)) {
                        setUpDeviceIcon(mediaDevice);
                        this.mCheckBox.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
                        this.mCheckBox.setVisibility(0);
                        this.mCheckBox.setChecked(false);
                        this.mCheckBox.setOnCheckedChangeListener(new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda5(this, mediaDevice));
                        this.mEndTouchArea.setVisibility(0);
                        this.mContainerLayout.setOnClickListener(new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda6(this, mediaDevice));
                        setCheckBoxColor(this.mCheckBox, MediaOutputAdapter.this.mController.getColorItemContent());
                        setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), false, false, false, false);
                        return;
                    }
                    setUpDeviceIcon(mediaDevice);
                    setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), false);
                    this.mContainerLayout.setOnClickListener(new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda7(this, mediaDevice));
                } else if (!hasMutingExpectedDevice || MediaOutputAdapter.this.mController.isCurrentConnectedDeviceRemote()) {
                    this.mTitleIcon.setImageDrawable(MediaOutputAdapter.this.mContext.getDrawable(R$drawable.media_output_icon_volume));
                    this.mTitleIcon.setColorFilter(MediaOutputAdapter.this.mController.getColorItemContent());
                    this.mTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
                    setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), true, true, false, false);
                    initSeekbar(mediaDevice, z4);
                    setUpContentDescriptionForView(this.mContainerLayout, false, mediaDevice);
                    MediaOutputAdapter.this.mCurrentActivePosition = i;
                } else {
                    setUpDeviceIcon(mediaDevice);
                    setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), false);
                    this.mContainerLayout.setOnClickListener(new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda4(this));
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBind$2(MediaDevice mediaDevice, CompoundButton compoundButton, boolean z) {
            onGroupActionTriggered(false, mediaDevice);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBind$3(View view) {
            this.mCheckBox.performClick();
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBind$4(View view) {
            cancelMuteAwaitConnection();
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBind$5(MediaDevice mediaDevice, CompoundButton compoundButton, boolean z) {
            onGroupActionTriggered(true, mediaDevice);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBind$6(MediaDevice mediaDevice, View view) {
            onGroupActionTriggered(true, mediaDevice);
        }

        public void setCheckBoxColor(CheckBox checkBox, int i) {
            CompoundButtonCompat.setButtonTintList(checkBox, new ColorStateList(new int[][]{new int[]{16842912}, new int[0]}, new int[]{i, i}));
        }

        public void onBind(int i, boolean z, boolean z2) {
            if (i == 1) {
                this.mTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
                this.mCheckBox.setVisibility(8);
                setSingleLineLayout(MediaOutputAdapter.this.mContext.getText(R$string.media_output_dialog_pairing_new), false);
                this.mTitleIcon.setImageDrawable(MediaOutputAdapter.this.mContext.getDrawable(R$drawable.ic_add));
                this.mTitleIcon.setColorFilter(MediaOutputAdapter.this.mController.getColorItemContent());
                LinearLayout linearLayout = this.mContainerLayout;
                MediaOutputController mediaOutputController = MediaOutputAdapter.this.mController;
                Objects.requireNonNull(mediaOutputController);
                linearLayout.setOnClickListener(new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda8(mediaOutputController));
            }
        }

        public final void onGroupActionTriggered(boolean z, MediaDevice mediaDevice) {
            if (z) {
                MediaOutputAdapter mediaOutputAdapter = MediaOutputAdapter.this;
                if (mediaOutputAdapter.isDeviceIncluded(mediaOutputAdapter.mController.getSelectableMediaDevice(), mediaDevice)) {
                    MediaOutputAdapter.this.mController.addDeviceToPlayMedia(mediaDevice);
                    return;
                }
            }
            if (!z) {
                MediaOutputAdapter mediaOutputAdapter2 = MediaOutputAdapter.this;
                if (mediaOutputAdapter2.isDeviceIncluded(mediaOutputAdapter2.mController.getDeselectableMediaDevice(), mediaDevice)) {
                    MediaOutputAdapter.this.mController.removeDeviceFromPlayMedia(mediaDevice);
                }
            }
        }

        /* renamed from: onItemClick */
        public final void lambda$onBind$7(View view, MediaDevice mediaDevice) {
            if (!MediaOutputAdapter.this.mController.isTransferring()) {
                if (MediaOutputAdapter.this.isCurrentlyConnected(mediaDevice)) {
                    Log.d("MediaOutputAdapter", "This device is already connected! : " + mediaDevice.getName());
                    return;
                }
                MediaOutputAdapter.this.mController.setTemporaryAllowListExceptionIfNeeded(mediaDevice);
                MediaOutputAdapter mediaOutputAdapter = MediaOutputAdapter.this;
                mediaOutputAdapter.mCurrentActivePosition = -1;
                mediaOutputAdapter.mController.connectDevice(mediaDevice);
                mediaDevice.setState(1);
                MediaOutputAdapter.this.notifyDataSetChanged();
            }
        }

        public final void cancelMuteAwaitConnection() {
            MediaOutputAdapter.this.mController.cancelMuteAwaitConnection();
            MediaOutputAdapter.this.notifyDataSetChanged();
        }

        public final void setUpContentDescriptionForView(View view, boolean z, MediaDevice mediaDevice) {
            int i;
            view.setClickable(z);
            Context context = MediaOutputAdapter.this.mContext;
            if (mediaDevice.getDeviceType() == 5) {
                i = R$string.accessibility_bluetooth_name;
            } else {
                i = R$string.accessibility_cast_name;
            }
            view.setContentDescription(context.getString(i, new Object[]{mediaDevice.getName()}));
        }
    }
}
