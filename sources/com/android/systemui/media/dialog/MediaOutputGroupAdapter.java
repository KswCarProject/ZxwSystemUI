package com.android.systemui.media.dialog;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.media.dialog.MediaOutputBaseAdapter;
import java.util.List;

public class MediaOutputGroupAdapter extends MediaOutputBaseAdapter {
    public static final boolean DEBUG = Log.isLoggable("MediaOutputGroupAdapter", 3);
    public final List<MediaDevice> mGroupMediaDevices;

    public MediaOutputGroupAdapter(MediaOutputController mediaOutputController) {
        super(mediaOutputController);
        this.mGroupMediaDevices = mediaOutputController.getGroupMediaDevices();
    }

    public MediaOutputBaseAdapter.MediaDeviceBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        super.onCreateViewHolder(viewGroup, i);
        return new GroupViewHolder(this.mHolderView);
    }

    public void onBindViewHolder(MediaOutputBaseAdapter.MediaDeviceBaseViewHolder mediaDeviceBaseViewHolder, int i) {
        boolean z = true;
        if (i == 0) {
            mediaDeviceBaseViewHolder.onBind(2, true, false);
            return;
        }
        int i2 = i - 1;
        int size = this.mGroupMediaDevices.size();
        if (i2 < size) {
            MediaDevice mediaDevice = this.mGroupMediaDevices.get(i2);
            if (i2 != size - 1) {
                z = false;
            }
            mediaDeviceBaseViewHolder.onBind(mediaDevice, false, z, i);
        } else if (DEBUG) {
            Log.d("MediaOutputGroupAdapter", "Incorrect position: " + i);
        }
    }

    public int getItemCount() {
        return this.mGroupMediaDevices.size() + 1;
    }

    public CharSequence getItemTitle(MediaDevice mediaDevice) {
        return super.getItemTitle(mediaDevice);
    }

    public class GroupViewHolder extends MediaOutputBaseAdapter.MediaDeviceBaseViewHolder {
        public GroupViewHolder(View view) {
            super(view);
        }

        public void onBind(MediaDevice mediaDevice, boolean z, boolean z2, int i) {
            super.onBind(mediaDevice, z, z2, i);
            this.mCheckBox.setVisibility(0);
            this.mCheckBox.setOnCheckedChangeListener(new MediaOutputGroupAdapter$GroupViewHolder$$ExternalSyntheticLambda0(this, mediaDevice));
            boolean z3 = this.mSeekBar.getVisibility() == 8;
            setTwoLineLayout(mediaDevice, false, true, false, false);
            initSeekbar(mediaDevice, z3);
            List<MediaDevice> selectedMediaDevice = MediaOutputGroupAdapter.this.mController.getSelectedMediaDevice();
            if (isDeviceIncluded(MediaOutputGroupAdapter.this.mController.getSelectableMediaDevice(), mediaDevice)) {
                this.mCheckBox.setButtonDrawable(R$drawable.ic_check_box);
                this.mCheckBox.setChecked(false);
                this.mCheckBox.setEnabled(true);
            } else if (!isDeviceIncluded(selectedMediaDevice, mediaDevice)) {
            } else {
                if (selectedMediaDevice.size() == 1 || !isDeviceIncluded(MediaOutputGroupAdapter.this.mController.getDeselectableMediaDevice(), mediaDevice)) {
                    this.mCheckBox.setButtonDrawable(getDisabledCheckboxDrawable());
                    this.mCheckBox.setChecked(true);
                    this.mCheckBox.setEnabled(false);
                    return;
                }
                this.mCheckBox.setButtonDrawable(R$drawable.ic_check_box);
                this.mCheckBox.setChecked(true);
                this.mCheckBox.setEnabled(true);
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBind$0(MediaDevice mediaDevice, CompoundButton compoundButton, boolean z) {
            onCheckBoxClicked(z, mediaDevice);
        }

        public void onBind(int i, boolean z, boolean z2) {
            if (i == 2) {
                setTwoLineLayout(MediaOutputGroupAdapter.this.mContext.getText(R$string.media_output_dialog_group), true, true, false, false);
                this.mTitleIcon.setImageDrawable(getSpeakerDrawable());
                this.mCheckBox.setVisibility(8);
                initSessionSeekbar();
            }
        }

        public final void onCheckBoxClicked(boolean z, MediaDevice mediaDevice) {
            if (z && isDeviceIncluded(MediaOutputGroupAdapter.this.mController.getSelectableMediaDevice(), mediaDevice)) {
                MediaOutputGroupAdapter.this.mController.addDeviceToPlayMedia(mediaDevice);
            } else if (!z && isDeviceIncluded(MediaOutputGroupAdapter.this.mController.getDeselectableMediaDevice(), mediaDevice)) {
                MediaOutputGroupAdapter.this.mController.removeDeviceFromPlayMedia(mediaDevice);
            }
        }

        public final Drawable getDisabledCheckboxDrawable() {
            Drawable mutate = MediaOutputGroupAdapter.this.mContext.getDrawable(R$drawable.ic_check_box_blue_24dp).mutate();
            Canvas canvas = new Canvas(Bitmap.createBitmap(mutate.getIntrinsicWidth(), mutate.getIntrinsicHeight(), Bitmap.Config.ARGB_8888));
            TypedValue typedValue = new TypedValue();
            MediaOutputGroupAdapter.this.mContext.getTheme().resolveAttribute(16842803, typedValue, true);
            mutate.setAlpha((int) (typedValue.getFloat() * 255.0f));
            mutate.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            mutate.draw(canvas);
            return mutate;
        }

        public final boolean isDeviceIncluded(List<MediaDevice> list, MediaDevice mediaDevice) {
            for (MediaDevice id : list) {
                if (TextUtils.equals(id.getId(), mediaDevice.getId())) {
                    return true;
                }
            }
            return false;
        }
    }
}
