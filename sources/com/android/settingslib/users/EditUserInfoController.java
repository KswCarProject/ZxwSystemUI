package com.android.settingslib.users;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import com.android.internal.util.UserIcons;
import com.android.settingslib.R$id;
import com.android.settingslib.R$layout;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.drawable.CircleFramedDrawable;
import java.io.File;
import java.util.function.BiConsumer;

public class EditUserInfoController {
    public Dialog mEditUserInfoDialog;
    public EditUserPhotoController mEditUserPhotoController;
    public final String mFileAuthority;
    public Drawable mSavedDrawable;
    public Bitmap mSavedPhoto;
    public boolean mWaitingForActivityResult = false;

    public EditUserInfoController(String str) {
        this.mFileAuthority = str;
    }

    public final void clear() {
        EditUserPhotoController editUserPhotoController = this.mEditUserPhotoController;
        if (editUserPhotoController != null) {
            editUserPhotoController.removeNewUserPhotoBitmapFile();
        }
        this.mEditUserInfoDialog = null;
        this.mSavedPhoto = null;
        this.mSavedDrawable = null;
    }

    public void onRestoreInstanceState(Bundle bundle) {
        String string = bundle.getString("pending_photo");
        if (string != null) {
            this.mSavedPhoto = EditUserPhotoController.loadNewUserPhotoBitmap(new File(string));
        }
        this.mWaitingForActivityResult = bundle.getBoolean("awaiting_result", false);
    }

    public void onSaveInstanceState(Bundle bundle) {
        EditUserPhotoController editUserPhotoController;
        File saveNewUserPhotoBitmap;
        if (!(this.mEditUserInfoDialog == null || (editUserPhotoController = this.mEditUserPhotoController) == null || (saveNewUserPhotoBitmap = editUserPhotoController.saveNewUserPhotoBitmap()) == null)) {
            bundle.putString("pending_photo", saveNewUserPhotoBitmap.getPath());
        }
        bundle.putBoolean("awaiting_result", this.mWaitingForActivityResult);
    }

    public void startingActivityForResult() {
        this.mWaitingForActivityResult = true;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        this.mWaitingForActivityResult = false;
        EditUserPhotoController editUserPhotoController = this.mEditUserPhotoController;
        if (editUserPhotoController != null && this.mEditUserInfoDialog != null) {
            editUserPhotoController.onActivityResult(i, i2, intent);
        }
    }

    public Dialog createDialog(Activity activity, ActivityStarter activityStarter, Drawable drawable, String str, String str2, BiConsumer<String, Drawable> biConsumer, Runnable runnable) {
        Drawable drawable2;
        Activity activity2 = activity;
        View inflate = LayoutInflater.from(activity).inflate(R$layout.edit_user_info_dialog_content, (ViewGroup) null);
        EditText editText = (EditText) inflate.findViewById(R$id.user_name);
        String str3 = str;
        editText.setText(str);
        ImageView imageView = (ImageView) inflate.findViewById(R$id.user_photo);
        if (drawable != null) {
            drawable2 = drawable;
        } else {
            drawable2 = UserIcons.getDefaultUserIcon(activity.getResources(), -10000, false);
        }
        imageView.setImageDrawable(getUserIcon(activity, drawable2));
        if (isChangePhotoRestrictedByBase(activity)) {
            inflate.findViewById(R$id.add_a_photo_icon).setVisibility(8);
        } else {
            RestrictedLockUtils.EnforcedAdmin changePhotoAdminRestriction = getChangePhotoAdminRestriction(activity);
            if (changePhotoAdminRestriction != null) {
                imageView.setOnClickListener(new EditUserInfoController$$ExternalSyntheticLambda0(activity, changePhotoAdminRestriction));
            } else {
                ActivityStarter activityStarter2 = activityStarter;
                this.mEditUserPhotoController = createEditUserPhotoController(activity, activityStarter, imageView);
            }
        }
        Dialog buildDialog = buildDialog(activity, inflate, editText, drawable, str, str2, biConsumer, runnable);
        this.mEditUserInfoDialog = buildDialog;
        buildDialog.getWindow().setSoftInputMode(4);
        return this.mEditUserInfoDialog;
    }

    public final Drawable getUserIcon(Activity activity, Drawable drawable) {
        Bitmap bitmap = this.mSavedPhoto;
        if (bitmap == null) {
            return drawable;
        }
        CircleFramedDrawable instance = CircleFramedDrawable.getInstance(activity, bitmap);
        this.mSavedDrawable = instance;
        return instance;
    }

    public final Dialog buildDialog(Activity activity, View view, EditText editText, Drawable drawable, String str, String str2, BiConsumer<String, Drawable> biConsumer, Runnable runnable) {
        return new AlertDialog.Builder(activity).setTitle(str2).setView(view).setCancelable(true).setPositiveButton(17039370, new EditUserInfoController$$ExternalSyntheticLambda1(this, drawable, editText, str, biConsumer)).setNegativeButton(17039360, new EditUserInfoController$$ExternalSyntheticLambda2(this, runnable)).setOnCancelListener(new EditUserInfoController$$ExternalSyntheticLambda3(this, runnable)).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$buildDialog$1(Drawable drawable, EditText editText, String str, BiConsumer biConsumer, DialogInterface dialogInterface, int i) {
        EditUserPhotoController editUserPhotoController = this.mEditUserPhotoController;
        Drawable newUserPhotoDrawable = editUserPhotoController != null ? editUserPhotoController.getNewUserPhotoDrawable() : null;
        if (newUserPhotoDrawable != null) {
            drawable = newUserPhotoDrawable;
        }
        String trim = editText.getText().toString().trim();
        if (!trim.isEmpty()) {
            str = trim;
        }
        clear();
        if (biConsumer != null) {
            biConsumer.accept(str, drawable);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$buildDialog$2(Runnable runnable, DialogInterface dialogInterface, int i) {
        clear();
        if (runnable != null) {
            runnable.run();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$buildDialog$3(Runnable runnable, DialogInterface dialogInterface) {
        clear();
        if (runnable != null) {
            runnable.run();
        }
    }

    public boolean isChangePhotoRestrictedByBase(Context context) {
        return RestrictedLockUtilsInternal.hasBaseUserRestriction(context, "no_set_user_icon", UserHandle.myUserId());
    }

    public RestrictedLockUtils.EnforcedAdmin getChangePhotoAdminRestriction(Context context) {
        return RestrictedLockUtilsInternal.checkIfRestrictionEnforced(context, "no_set_user_icon", UserHandle.myUserId());
    }

    public EditUserPhotoController createEditUserPhotoController(Activity activity, ActivityStarter activityStarter, ImageView imageView) {
        return new EditUserPhotoController(activity, activityStarter, imageView, this.mSavedPhoto, this.mSavedDrawable, this.mFileAuthority);
    }
}
