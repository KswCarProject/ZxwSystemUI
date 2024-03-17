package com.android.systemui.media.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.graphics.drawable.IconCompat;
import com.android.settingslib.qrcode.QrCodeGenerator;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.media.dialog.MediaOutputController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.google.zxing.WriterException;

public class MediaOutputBroadcastDialog extends MediaOutputBaseDialog {
    public AlertDialog mAlertDialog;
    public TextView mBroadcastCode;
    public ImageView mBroadcastCodeEdit;
    public ImageView mBroadcastCodeEye;
    public TextView mBroadcastErrorMessage;
    public ViewStub mBroadcastInfoArea;
    public TextView mBroadcastName;
    public ImageView mBroadcastNameEdit;
    public ImageView mBroadcastNotify;
    public ImageView mBroadcastQrCodeView;
    public String mCurrentBroadcastCode;
    public String mCurrentBroadcastName;
    public Boolean mIsPasswordHide = Boolean.TRUE;
    public boolean mIsStopbyUpdateBroadcastCode = false;
    public int mRetryCount = 0;

    public int getHeaderIconRes() {
        return 0;
    }

    public int getStopButtonVisibility() {
        return 0;
    }

    public MediaOutputBroadcastDialog(Context context, boolean z, BroadcastSender broadcastSender, MediaOutputController mediaOutputController) {
        super(context, broadcastSender, mediaOutputController);
        this.mAdapter = new MediaOutputGroupAdapter(this.mMediaOutputController);
        if (!z) {
            getWindow().setType(2038);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initBtQrCodeUI();
    }

    public void onStart() {
        super.onStart();
        refreshUi();
    }

    public IconCompat getHeaderIcon() {
        return this.mMediaOutputController.getHeaderIcon();
    }

    public int getHeaderIconSize() {
        return this.mContext.getResources().getDimensionPixelSize(R$dimen.media_output_dialog_header_album_icon_size);
    }

    public CharSequence getHeaderText() {
        return this.mMediaOutputController.getHeaderTitle();
    }

    public CharSequence getHeaderSubtitle() {
        return this.mMediaOutputController.getHeaderSubTitle();
    }

    public Drawable getAppSourceIcon() {
        return this.mMediaOutputController.getAppSourceIcon();
    }

    public void onStopButtonClick() {
        this.mMediaOutputController.stopBluetoothLeBroadcast();
        dismiss();
    }

    public final String getBroadcastMetadataInfo(int i) {
        if (i != 0) {
            return i != 1 ? "" : this.mMediaOutputController.getBroadcastCode();
        }
        return this.mMediaOutputController.getBroadcastName();
    }

    public final void initBtQrCodeUI() {
        inflateBroadcastInfoArea();
        this.mBroadcastQrCodeView = (ImageView) getDialogView().requireViewById(R$id.qrcode_view);
        ImageView imageView = (ImageView) getDialogView().requireViewById(R$id.broadcast_info);
        this.mBroadcastNotify = imageView;
        imageView.setOnClickListener(new MediaOutputBroadcastDialog$$ExternalSyntheticLambda0(this));
        this.mBroadcastName = (TextView) getDialogView().requireViewById(R$id.broadcast_name_summary);
        ImageView imageView2 = (ImageView) getDialogView().requireViewById(R$id.broadcast_name_edit);
        this.mBroadcastNameEdit = imageView2;
        imageView2.setOnClickListener(new MediaOutputBroadcastDialog$$ExternalSyntheticLambda1(this));
        TextView textView = (TextView) getDialogView().requireViewById(R$id.broadcast_code_summary);
        this.mBroadcastCode = textView;
        textView.setTransformationMethod(PasswordTransformationMethod.getInstance());
        ImageView imageView3 = (ImageView) getDialogView().requireViewById(R$id.broadcast_code_eye);
        this.mBroadcastCodeEye = imageView3;
        imageView3.setOnClickListener(new MediaOutputBroadcastDialog$$ExternalSyntheticLambda2(this));
        ImageView imageView4 = (ImageView) getDialogView().requireViewById(R$id.broadcast_code_edit);
        this.mBroadcastCodeEdit = imageView4;
        imageView4.setOnClickListener(new MediaOutputBroadcastDialog$$ExternalSyntheticLambda3(this));
        refreshUi();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initBtQrCodeUI$0(View view) {
        this.mMediaOutputController.launchLeBroadcastNotifyDialog((View) null, (BroadcastSender) null, MediaOutputController.BroadcastNotifyDialog.ACTION_BROADCAST_INFO_ICON, (DialogInterface.OnClickListener) null);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initBtQrCodeUI$1(View view) {
        launchBroadcastUpdatedDialog(false, this.mBroadcastName.getText().toString());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initBtQrCodeUI$2(View view) {
        updateBroadcastCodeVisibility();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initBtQrCodeUI$3(View view) {
        launchBroadcastUpdatedDialog(true, this.mBroadcastCode.getText().toString());
    }

    public final void refreshUi() {
        setQrCodeView();
        this.mCurrentBroadcastName = getBroadcastMetadataInfo(0);
        this.mCurrentBroadcastCode = getBroadcastMetadataInfo(1);
        this.mBroadcastName.setText(this.mCurrentBroadcastName);
        this.mBroadcastCode.setText(this.mCurrentBroadcastCode);
    }

    public final void inflateBroadcastInfoArea() {
        ViewStub viewStub = (ViewStub) getDialogView().requireViewById(R$id.broadcast_qrcode);
        this.mBroadcastInfoArea = viewStub;
        viewStub.inflate();
    }

    public final void setQrCodeView() {
        String broadcastMetadata = getBroadcastMetadata();
        if (!broadcastMetadata.isEmpty()) {
            try {
                this.mBroadcastQrCodeView.setImageBitmap(QrCodeGenerator.encodeQrCode(broadcastMetadata, getContext().getResources().getDimensionPixelSize(R$dimen.media_output_qrcode_size)));
            } catch (WriterException e) {
                Log.e("BroadcastDialog", "Error generatirng QR code bitmap " + e);
            }
        }
    }

    public final void updateBroadcastCodeVisibility() {
        TransformationMethod transformationMethod;
        TextView textView = this.mBroadcastCode;
        if (this.mIsPasswordHide.booleanValue()) {
            transformationMethod = HideReturnsTransformationMethod.getInstance();
        } else {
            transformationMethod = PasswordTransformationMethod.getInstance();
        }
        textView.setTransformationMethod(transformationMethod);
        this.mIsPasswordHide = Boolean.valueOf(!this.mIsPasswordHide.booleanValue());
    }

    public final void launchBroadcastUpdatedDialog(boolean z, String str) {
        int i;
        View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.media_output_broadcast_update_dialog, (ViewGroup) null);
        EditText editText = (EditText) inflate.requireViewById(R$id.broadcast_edit_text);
        editText.setText(str);
        this.mBroadcastErrorMessage = (TextView) inflate.requireViewById(R$id.broadcast_error_message);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        if (z) {
            i = R$string.media_output_broadcast_code;
        } else {
            i = R$string.media_output_broadcast_name;
        }
        AlertDialog create = builder.setTitle(i).setView(inflate).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton(R$string.media_output_broadcast_dialog_save, new MediaOutputBroadcastDialog$$ExternalSyntheticLambda4(this, z, editText)).create();
        this.mAlertDialog = create;
        create.getWindow().setType(2009);
        SystemUIDialog.setShowForAllUsers(this.mAlertDialog, true);
        SystemUIDialog.registerDismissListener(this.mAlertDialog);
        this.mAlertDialog.show();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$launchBroadcastUpdatedDialog$4(boolean z, EditText editText, DialogInterface dialogInterface, int i) {
        updateBroadcastInfo(z, editText.getText().toString());
    }

    public final String getBroadcastMetadata() {
        return this.mMediaOutputController.getBroadcastMetadata();
    }

    public final void updateBroadcastInfo(boolean z, String str) {
        Button button = this.mAlertDialog.getButton(-1);
        if (button != null) {
            button.setEnabled(false);
        }
        if (z) {
            this.mIsStopbyUpdateBroadcastCode = true;
            this.mMediaOutputController.setBroadcastCode(str);
            if (!this.mMediaOutputController.stopBluetoothLeBroadcast()) {
                handleLeBroadcastStopFailed();
                return;
            }
            return;
        }
        this.mMediaOutputController.setBroadcastName(str);
        if (!this.mMediaOutputController.updateBluetoothLeBroadcast()) {
            handleLeBroadcastUpdateFailed();
        }
    }

    public void handleLeBroadcastStarted() {
        this.mRetryCount = 0;
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        refreshUi();
    }

    public void handleLeBroadcastStartFailed() {
        this.mMediaOutputController.setBroadcastCode(this.mCurrentBroadcastCode);
        this.mRetryCount++;
        handleUpdateFailedUi();
    }

    public void handleLeBroadcastMetadataChanged() {
        Log.d("BroadcastDialog", "handleLeBroadcastMetadataChanged ");
        refreshUi();
    }

    public void handleLeBroadcastUpdated() {
        this.mRetryCount = 0;
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        refreshUi();
    }

    public void handleLeBroadcastUpdateFailed() {
        this.mMediaOutputController.setBroadcastName(this.mCurrentBroadcastName);
        this.mRetryCount++;
        handleUpdateFailedUi();
    }

    public void handleLeBroadcastStopped() {
        if (this.mIsStopbyUpdateBroadcastCode) {
            this.mIsStopbyUpdateBroadcastCode = false;
            this.mRetryCount = 0;
            if (!this.mMediaOutputController.startBluetoothLeBroadcast()) {
                handleLeBroadcastStartFailed();
                return;
            }
            return;
        }
        dismiss();
    }

    public void handleLeBroadcastStopFailed() {
        this.mMediaOutputController.setBroadcastCode(this.mCurrentBroadcastCode);
        this.mRetryCount++;
        handleUpdateFailedUi();
    }

    public boolean isBroadcastSupported() {
        Log.d("BroadcastDialog", "isBroadcastSupported: " + this.mMediaOutputController.isBroadcastSupported());
        return this.mMediaOutputController.isBroadcastSupported();
    }

    public final void handleUpdateFailedUi() {
        Button button = this.mAlertDialog.getButton(-1);
        this.mBroadcastErrorMessage.setVisibility(0);
        if (this.mRetryCount < 3) {
            if (button != null) {
                button.setEnabled(true);
            }
            this.mBroadcastErrorMessage.setText(R$string.media_output_broadcast_update_error);
            return;
        }
        this.mRetryCount = 0;
        this.mBroadcastErrorMessage.setText(R$string.media_output_broadcast_last_update_error);
    }
}
