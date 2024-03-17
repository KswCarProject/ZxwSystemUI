package com.android.systemui.media.dialog;

import android.app.WallpaperColors;
import android.bluetooth.BluetoothLeBroadcast;
import android.bluetooth.BluetoothLeBroadcastMetadata;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.media.dialog.MediaOutputController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class MediaOutputBaseDialog extends SystemUIDialog implements MediaOutputController.Callback {
    public MediaOutputBaseAdapter mAdapter;
    public Button mAppButton;
    public ImageView mAppResourceIcon;
    public final BluetoothLeBroadcast.Callback mBroadcastCallback = new BluetoothLeBroadcast.Callback() {
        public void onPlaybackStarted(int i, int i2) {
        }

        public void onPlaybackStopped(int i, int i2) {
        }

        public void onBroadcastStarted(int i, int i2) {
            Log.d("MediaOutputDialog", "onBroadcastStarted(), reason = " + i + ", broadcastId = " + i2);
            MediaOutputBaseDialog.this.mMainThreadHandler.post(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBroadcastStarted$0() {
            MediaOutputBaseDialog.this.handleLeBroadcastStarted();
        }

        public void onBroadcastStartFailed(int i) {
            Log.d("MediaOutputDialog", "onBroadcastStartFailed(), reason = " + i);
            MediaOutputBaseDialog.this.mMainThreadHandler.postDelayed(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda2(this), 3000);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBroadcastStartFailed$1() {
            MediaOutputBaseDialog.this.handleLeBroadcastStartFailed();
        }

        public void onBroadcastMetadataChanged(int i, BluetoothLeBroadcastMetadata bluetoothLeBroadcastMetadata) {
            Log.d("MediaOutputDialog", "onBroadcastMetadataChanged(), broadcastId = " + i + ", metadata = " + bluetoothLeBroadcastMetadata);
            MediaOutputBaseDialog.this.mMainThreadHandler.post(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda6(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBroadcastMetadataChanged$2() {
            MediaOutputBaseDialog.this.handleLeBroadcastMetadataChanged();
        }

        public void onBroadcastStopped(int i, int i2) {
            Log.d("MediaOutputDialog", "onBroadcastStopped(), reason = " + i + ", broadcastId = " + i2);
            MediaOutputBaseDialog.this.mMainThreadHandler.post(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBroadcastStopped$3() {
            MediaOutputBaseDialog.this.handleLeBroadcastStopped();
        }

        public void onBroadcastStopFailed(int i) {
            Log.d("MediaOutputDialog", "onBroadcastStopFailed(), reason = " + i);
            MediaOutputBaseDialog.this.mMainThreadHandler.post(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda3(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBroadcastStopFailed$4() {
            MediaOutputBaseDialog.this.handleLeBroadcastStopFailed();
        }

        public void onBroadcastUpdated(int i, int i2) {
            Log.d("MediaOutputDialog", "onBroadcastUpdated(), reason = " + i + ", broadcastId = " + i2);
            MediaOutputBaseDialog.this.mMainThreadHandler.post(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda5(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBroadcastUpdated$5() {
            MediaOutputBaseDialog.this.handleLeBroadcastUpdated();
        }

        public void onBroadcastUpdateFailed(int i, int i2) {
            Log.d("MediaOutputDialog", "onBroadcastUpdateFailed(), reason = " + i + ", broadcastId = " + i2);
            MediaOutputBaseDialog.this.mMainThreadHandler.post(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda4(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBroadcastUpdateFailed$6() {
            MediaOutputBaseDialog.this.handleLeBroadcastUpdateFailed();
        }
    };
    public final BroadcastSender mBroadcastSender;
    public LinearLayout mCastAppLayout;
    public final Context mContext;
    public LinearLayout mDeviceListLayout;
    public final ViewTreeObserver.OnGlobalLayoutListener mDeviceListLayoutListener = new MediaOutputBaseDialog$$ExternalSyntheticLambda0(this);
    public RecyclerView mDevicesRecyclerView;
    public View mDialogView;
    public Button mDoneButton;
    public Executor mExecutor;
    public ImageView mHeaderIcon;
    public TextView mHeaderSubtitle;
    public TextView mHeaderTitle;
    public final RecyclerView.LayoutManager mLayoutManager;
    public int mListMaxHeight;
    public final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    public final MediaOutputController mMediaOutputController;
    public Button mStopButton;
    public WallpaperColors mWallpaperColors;

    public abstract Drawable getAppSourceIcon();

    public abstract IconCompat getHeaderIcon();

    public abstract int getHeaderIconRes();

    public abstract int getHeaderIconSize();

    public abstract CharSequence getHeaderSubtitle();

    public abstract CharSequence getHeaderText();

    public abstract int getStopButtonVisibility();

    public boolean isBroadcastSupported() {
        return false;
    }

    public void onHeaderIconClick() {
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.mDeviceListLayout.getHeight() > this.mListMaxHeight) {
            ViewGroup.LayoutParams layoutParams = this.mDeviceListLayout.getLayoutParams();
            layoutParams.height = this.mListMaxHeight;
            this.mDeviceListLayout.setLayoutParams(layoutParams);
        }
    }

    public class LayoutManagerWrapper extends LinearLayoutManager {
        public LayoutManagerWrapper(Context context) {
            super(context);
        }

        public void onLayoutCompleted(RecyclerView.State state) {
            super.onLayoutCompleted(state);
            MediaOutputBaseDialog.this.mMediaOutputController.setRefreshing(false);
            MediaOutputBaseDialog.this.mMediaOutputController.refreshDataSetIfNeeded();
        }
    }

    public MediaOutputBaseDialog(Context context, BroadcastSender broadcastSender, MediaOutputController mediaOutputController) {
        super(context, R$style.Theme_SystemUI_Dialog_Media);
        Context context2 = getContext();
        this.mContext = context2;
        this.mBroadcastSender = broadcastSender;
        this.mMediaOutputController = mediaOutputController;
        this.mLayoutManager = new LayoutManagerWrapper(context2);
        this.mListMaxHeight = context.getResources().getDimensionPixelSize(R$dimen.media_output_dialog_list_max_height);
        this.mExecutor = Executors.newSingleThreadExecutor();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDialogView = LayoutInflater.from(this.mContext).inflate(R$layout.media_output_dialog, (ViewGroup) null);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = 17;
        attributes.setFitInsetsTypes(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        attributes.setFitInsetsSides(WindowInsets.Side.all());
        attributes.setFitInsetsIgnoringVisibility(true);
        window.setAttributes(attributes);
        window.setContentView(this.mDialogView);
        window.setTitle(this.mContext.getString(R$string.media_output_dialog_accessibility_title));
        this.mHeaderTitle = (TextView) this.mDialogView.requireViewById(R$id.header_title);
        this.mHeaderSubtitle = (TextView) this.mDialogView.requireViewById(R$id.header_subtitle);
        this.mHeaderIcon = (ImageView) this.mDialogView.requireViewById(R$id.header_icon);
        this.mDevicesRecyclerView = (RecyclerView) this.mDialogView.requireViewById(R$id.list_result);
        this.mDeviceListLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.device_list);
        this.mDoneButton = (Button) this.mDialogView.requireViewById(R$id.done);
        this.mStopButton = (Button) this.mDialogView.requireViewById(R$id.stop);
        this.mAppButton = (Button) this.mDialogView.requireViewById(R$id.launch_app_button);
        this.mAppResourceIcon = (ImageView) this.mDialogView.requireViewById(R$id.app_source_icon);
        this.mCastAppLayout = (LinearLayout) this.mDialogView.requireViewById(R$id.cast_app_section);
        this.mDeviceListLayout.getViewTreeObserver().addOnGlobalLayoutListener(this.mDeviceListLayoutListener);
        this.mDevicesRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mDevicesRecyclerView.setAdapter(this.mAdapter);
        this.mHeaderIcon.setOnClickListener(new MediaOutputBaseDialog$$ExternalSyntheticLambda2(this));
        this.mDoneButton.setOnClickListener(new MediaOutputBaseDialog$$ExternalSyntheticLambda3(this));
        this.mStopButton.setOnClickListener(new MediaOutputBaseDialog$$ExternalSyntheticLambda4(this));
        this.mAppButton.setOnClickListener(new MediaOutputBaseDialog$$ExternalSyntheticLambda5(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        onHeaderIconClick();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(View view) {
        dismiss();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$3(View view) {
        this.mMediaOutputController.releaseSession();
        dismiss();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$4(View view) {
        this.mBroadcastSender.closeSystemDialogs();
        if (this.mMediaOutputController.getAppLaunchIntent() != null) {
            this.mContext.startActivity(this.mMediaOutputController.getAppLaunchIntent());
        }
        dismiss();
    }

    public void onStart() {
        super.onStart();
        this.mMediaOutputController.start(this);
        if (isBroadcastSupported()) {
            this.mMediaOutputController.registerLeBroadcastServiceCallBack(this.mExecutor, this.mBroadcastCallback);
        }
    }

    public void onStop() {
        super.onStop();
        if (isBroadcastSupported()) {
            this.mMediaOutputController.unregisterLeBroadcastServiceCallBack(this.mBroadcastCallback);
        }
        this.mMediaOutputController.stop();
    }

    /* renamed from: refresh */
    public void lambda$stopLeBroadcast$7() {
        refresh(false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0106  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x011d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void refresh(boolean r10) {
        /*
            r9 = this;
            com.android.systemui.media.dialog.MediaOutputController r0 = r9.mMediaOutputController
            boolean r0 = r0.isRefreshing()
            if (r0 == 0) goto L_0x0009
            return
        L_0x0009:
            com.android.systemui.media.dialog.MediaOutputController r0 = r9.mMediaOutputController
            r1 = 1
            r0.setRefreshing(r1)
            int r0 = r9.getHeaderIconRes()
            androidx.core.graphics.drawable.IconCompat r2 = r9.getHeaderIcon()
            android.graphics.drawable.Drawable r3 = r9.getAppSourceIcon()
            android.widget.LinearLayout r4 = r9.mCastAppLayout
            com.android.systemui.media.dialog.MediaOutputController r5 = r9.mMediaOutputController
            boolean r5 = r5.shouldShowLaunchSection()
            r6 = 8
            r7 = 0
            if (r5 == 0) goto L_0x002a
            r5 = r7
            goto L_0x002b
        L_0x002a:
            r5 = r6
        L_0x002b:
            r4.setVisibility(r5)
            if (r3 == 0) goto L_0x004c
            android.widget.ImageView r4 = r9.mAppResourceIcon
            r4.setImageDrawable(r3)
            android.widget.Button r4 = r9.mAppButton
            android.content.Context r5 = r9.mContext
            android.content.res.Resources r5 = r5.getResources()
            int r8 = com.android.systemui.R$dimen.media_output_dialog_app_tier_icon_size
            int r5 = r5.getDimensionPixelSize(r8)
            android.graphics.drawable.Drawable r3 = r9.resizeDrawable(r3, r5)
            r5 = 0
            r4.setCompoundDrawablesWithIntrinsicBounds(r3, r5, r5, r5)
            goto L_0x0051
        L_0x004c:
            android.widget.ImageView r3 = r9.mAppResourceIcon
            r3.setVisibility(r6)
        L_0x0051:
            if (r0 == 0) goto L_0x005e
            android.widget.ImageView r2 = r9.mHeaderIcon
            r2.setVisibility(r7)
            android.widget.ImageView r2 = r9.mHeaderIcon
            r2.setImageResource(r0)
            goto L_0x00b6
        L_0x005e:
            if (r2 == 0) goto L_0x00b1
            android.content.Context r0 = r9.mContext
            android.graphics.drawable.Icon r0 = r2.toIcon(r0)
            int r2 = r0.getType()
            if (r2 == r1) goto L_0x0078
            int r2 = r0.getType()
            r3 = 5
            if (r2 == r3) goto L_0x0078
            r9.updateButtonBackgroundColorFilter()
            r4 = r7
            goto L_0x00a6
        L_0x0078:
            android.content.Context r2 = r9.mContext
            android.content.res.Resources r2 = r2.getResources()
            android.content.res.Configuration r2 = r2.getConfiguration()
            int r2 = r2.uiMode
            r2 = r2 & 48
            r3 = 32
            if (r2 != r3) goto L_0x008c
            r2 = r1
            goto L_0x008d
        L_0x008c:
            r2 = r7
        L_0x008d:
            android.graphics.Bitmap r3 = r0.getBitmap()
            android.app.WallpaperColors r3 = android.app.WallpaperColors.fromBitmap(r3)
            android.app.WallpaperColors r4 = r9.mWallpaperColors
            boolean r4 = r3.equals(r4)
            r4 = r4 ^ r1
            if (r4 == 0) goto L_0x00a6
            com.android.systemui.media.dialog.MediaOutputBaseAdapter r5 = r9.mAdapter
            r5.updateColorScheme(r3, r2)
            r9.updateButtonBackgroundColorFilter()
        L_0x00a6:
            android.widget.ImageView r2 = r9.mHeaderIcon
            r2.setVisibility(r7)
            android.widget.ImageView r2 = r9.mHeaderIcon
            r2.setImageIcon(r0)
            goto L_0x00b7
        L_0x00b1:
            android.widget.ImageView r0 = r9.mHeaderIcon
            r0.setVisibility(r6)
        L_0x00b6:
            r4 = r7
        L_0x00b7:
            android.widget.ImageView r0 = r9.mHeaderIcon
            int r0 = r0.getVisibility()
            if (r0 != 0) goto L_0x00da
            int r0 = r9.getHeaderIconSize()
            android.content.Context r2 = r9.mContext
            android.content.res.Resources r2 = r2.getResources()
            int r3 = com.android.systemui.R$dimen.media_output_dialog_header_icon_padding
            int r2 = r2.getDimensionPixelSize(r3)
            android.widget.ImageView r3 = r9.mHeaderIcon
            android.widget.LinearLayout$LayoutParams r5 = new android.widget.LinearLayout$LayoutParams
            int r2 = r2 + r0
            r5.<init>(r2, r0)
            r3.setLayoutParams(r5)
        L_0x00da:
            android.widget.Button r0 = r9.mAppButton
            com.android.systemui.media.dialog.MediaOutputController r2 = r9.mMediaOutputController
            java.lang.String r2 = r2.getAppSourceName()
            r0.setText(r2)
            android.widget.TextView r0 = r9.mHeaderTitle
            java.lang.CharSequence r2 = r9.getHeaderText()
            r0.setText(r2)
            java.lang.CharSequence r0 = r9.getHeaderSubtitle()
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 == 0) goto L_0x0106
            android.widget.TextView r0 = r9.mHeaderSubtitle
            r0.setVisibility(r6)
            android.widget.TextView r0 = r9.mHeaderTitle
            r2 = 8388627(0x800013, float:1.175497E-38)
            r0.setGravity(r2)
            goto L_0x0115
        L_0x0106:
            android.widget.TextView r2 = r9.mHeaderSubtitle
            r2.setVisibility(r7)
            android.widget.TextView r2 = r9.mHeaderSubtitle
            r2.setText(r0)
            android.widget.TextView r0 = r9.mHeaderTitle
            r0.setGravity(r7)
        L_0x0115:
            com.android.systemui.media.dialog.MediaOutputBaseAdapter r0 = r9.mAdapter
            boolean r0 = r0.isDragging()
            if (r0 != 0) goto L_0x013c
            com.android.systemui.media.dialog.MediaOutputBaseAdapter r0 = r9.mAdapter
            int r0 = r0.getCurrentActivePosition()
            if (r4 != 0) goto L_0x0137
            if (r10 != 0) goto L_0x0137
            if (r0 < 0) goto L_0x0137
            com.android.systemui.media.dialog.MediaOutputBaseAdapter r10 = r9.mAdapter
            int r10 = r10.getItemCount()
            if (r0 >= r10) goto L_0x0137
            com.android.systemui.media.dialog.MediaOutputBaseAdapter r10 = r9.mAdapter
            r10.notifyItemChanged(r0)
            goto L_0x013c
        L_0x0137:
            com.android.systemui.media.dialog.MediaOutputBaseAdapter r10 = r9.mAdapter
            r10.notifyDataSetChanged()
        L_0x013c:
            android.widget.Button r10 = r9.mStopButton
            int r0 = r9.getStopButtonVisibility()
            r10.setVisibility(r0)
            android.widget.Button r10 = r9.mStopButton
            r10.setEnabled(r1)
            android.widget.Button r10 = r9.mStopButton
            java.lang.CharSequence r0 = r9.getStopButtonText()
            r10.setText(r0)
            android.widget.Button r10 = r9.mStopButton
            com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda6 r0 = new com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda6
            r0.<init>(r9)
            r10.setOnClickListener(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.dialog.MediaOutputBaseDialog.refresh(boolean):void");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refresh$5(View view) {
        onStopButtonClick();
    }

    public final void updateButtonBackgroundColorFilter() {
        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(this.mAdapter.getController().getColorButtonBackground(), PorterDuff.Mode.SRC_IN);
        this.mDoneButton.getBackground().setColorFilter(porterDuffColorFilter);
        this.mStopButton.getBackground().setColorFilter(porterDuffColorFilter);
        this.mDoneButton.setTextColor(this.mAdapter.getController().getColorPositiveButtonText());
    }

    public final Drawable resizeDrawable(Drawable drawable, int i) {
        Bitmap.Config config;
        if (drawable == null) {
            return null;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (drawable.getOpacity() != -1) {
            config = Bitmap.Config.ARGB_8888;
        } else {
            config = Bitmap.Config.RGB_565;
        }
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, config);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return new BitmapDrawable(this.mContext.getResources(), Bitmap.createScaledBitmap(createBitmap, i, i, false));
    }

    public void handleLeBroadcastStarted() {
        startLeBroadcastDialog();
    }

    public void handleLeBroadcastStartFailed() {
        this.mStopButton.setText(R$string.media_output_broadcast_start_failed);
        this.mStopButton.setEnabled(false);
        lambda$stopLeBroadcast$7();
    }

    public void handleLeBroadcastMetadataChanged() {
        lambda$stopLeBroadcast$7();
    }

    public void handleLeBroadcastStopped() {
        lambda$stopLeBroadcast$7();
    }

    public void handleLeBroadcastStopFailed() {
        lambda$stopLeBroadcast$7();
    }

    public void handleLeBroadcastUpdated() {
        lambda$stopLeBroadcast$7();
    }

    public void handleLeBroadcastUpdateFailed() {
        lambda$stopLeBroadcast$7();
    }

    public void startLeBroadcast() {
        this.mStopButton.setText(R$string.media_output_broadcast_starting);
        this.mStopButton.setEnabled(false);
        if (!this.mMediaOutputController.startBluetoothLeBroadcast()) {
            handleLeBroadcastStartFailed();
        }
    }

    public boolean startLeBroadcastDialogForFirstTime() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("MediaOutputDialog", 0);
        if (sharedPreferences == null || !sharedPreferences.getBoolean("PrefIsLeBroadcastFirstLaunch", true)) {
            return false;
        }
        Log.d("MediaOutputDialog", "PREF_IS_LE_BROADCAST_FIRST_LAUNCH: true");
        this.mMediaOutputController.launchLeBroadcastNotifyDialog(this.mDialogView, this.mBroadcastSender, MediaOutputController.BroadcastNotifyDialog.ACTION_FIRST_LAUNCH, new MediaOutputBaseDialog$$ExternalSyntheticLambda7(this));
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean("PrefIsLeBroadcastFirstLaunch", false);
        edit.apply();
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startLeBroadcastDialogForFirstTime$6(DialogInterface dialogInterface, int i) {
        startLeBroadcast();
    }

    public void startLeBroadcastDialog() {
        this.mMediaOutputController.launchMediaOutputBroadcastDialog(this.mDialogView, this.mBroadcastSender);
        lambda$stopLeBroadcast$7();
    }

    public void stopLeBroadcast() {
        this.mStopButton.setEnabled(false);
        if (!this.mMediaOutputController.stopBluetoothLeBroadcast()) {
            this.mMainThreadHandler.post(new MediaOutputBaseDialog$$ExternalSyntheticLambda8(this));
        }
    }

    public CharSequence getStopButtonText() {
        return this.mContext.getText(R$string.media_output_dialog_button_stop_casting);
    }

    public void onStopButtonClick() {
        this.mMediaOutputController.releaseSession();
        dismiss();
    }

    public void onMediaChanged() {
        this.mMainThreadHandler.post(new MediaOutputBaseDialog$$ExternalSyntheticLambda1(this));
    }

    public void onMediaStoppedOrPaused() {
        if (isShowing()) {
            dismiss();
        }
    }

    public void onRouteChanged() {
        this.mMainThreadHandler.post(new MediaOutputBaseDialog$$ExternalSyntheticLambda10(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onDeviceListChanged$10() {
        refresh(true);
    }

    public void onDeviceListChanged() {
        this.mMainThreadHandler.post(new MediaOutputBaseDialog$$ExternalSyntheticLambda9(this));
    }

    public void dismissDialog() {
        dismiss();
    }

    public View getDialogView() {
        return this.mDialogView;
    }
}
