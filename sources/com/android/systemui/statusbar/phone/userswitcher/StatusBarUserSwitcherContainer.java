package com.android.systemui.statusbar.phone.userswitcher;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.R$id;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StatusBarUserSwitcherContainer.kt */
public final class StatusBarUserSwitcherContainer extends LinearLayout {
    public ImageView avatar;
    public TextView text;

    public StatusBarUserSwitcherContainer(@Nullable Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @NotNull
    public final TextView getText() {
        TextView textView = this.text;
        if (textView != null) {
            return textView;
        }
        return null;
    }

    @NotNull
    public final ImageView getAvatar() {
        ImageView imageView = this.avatar;
        if (imageView != null) {
            return imageView;
        }
        return null;
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.text = (TextView) findViewById(R$id.current_user_name);
        this.avatar = (ImageView) findViewById(R$id.current_user_avatar);
    }
}
