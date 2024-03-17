package com.android.systemui.privacy;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: PrivacyDialog.kt */
public final class PrivacyDialog extends SystemUIDialog {
    @NotNull
    public final View.OnClickListener clickListener;
    @NotNull
    public final List<WeakReference<OnDialogDismissed>> dismissListeners = new ArrayList();
    @NotNull
    public final AtomicBoolean dismissed = new AtomicBoolean(false);
    @NotNull
    public final String enterpriseText;
    public final int iconColorSolid = Utils.getColorAttrDefaultColor(getContext(), 16843827);
    @NotNull
    public final List<PrivacyElement> list;
    public final String phonecall;
    public ViewGroup rootView;

    /* compiled from: PrivacyDialog.kt */
    public interface OnDialogDismissed {
        void onDialogDismissed();
    }

    /* compiled from: PrivacyDialog.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[PrivacyType.values().length];
            iArr[PrivacyType.TYPE_LOCATION.ordinal()] = 1;
            iArr[PrivacyType.TYPE_CAMERA.ordinal()] = 2;
            iArr[PrivacyType.TYPE_MICROPHONE.ordinal()] = 3;
            iArr[PrivacyType.TYPE_MEDIA_PROJECTION.ordinal()] = 4;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public PrivacyDialog(@NotNull Context context, @NotNull List<PrivacyElement> list2, @NotNull Function4<? super String, ? super Integer, ? super CharSequence, ? super Intent, Unit> function4) {
        super(context, R$style.PrivacyDialog);
        this.list = list2;
        this.enterpriseText = Intrinsics.stringPlus(" ", context.getString(R$string.ongoing_privacy_dialog_enterprise));
        this.phonecall = context.getString(R$string.ongoing_privacy_dialog_phonecall);
        this.clickListener = new PrivacyDialog$clickListener$1(function4);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        if (window != null) {
            window.getAttributes().setFitInsetsTypes(window.getAttributes().getFitInsetsTypes() | WindowInsets.Type.statusBars());
            window.getAttributes().receiveInsetsIgnoringZOrder = true;
            window.setGravity(49);
        }
        setTitle(R$string.ongoing_privacy_dialog_a11y_title);
        setContentView(R$layout.privacy_dialog);
        this.rootView = (ViewGroup) requireViewById(R$id.root);
        for (PrivacyElement privacyElement : this.list) {
            ViewGroup viewGroup = this.rootView;
            if (viewGroup == null) {
                viewGroup = null;
            }
            viewGroup.addView(createView(privacyElement));
        }
    }

    public final void addOnDismissListener(@NotNull OnDialogDismissed onDialogDismissed) {
        if (this.dismissed.get()) {
            onDialogDismissed.onDialogDismissed();
        } else {
            this.dismissListeners.add(new WeakReference(onDialogDismissed));
        }
    }

    public void onStop() {
        super.onStop();
        this.dismissed.set(true);
        Iterator<WeakReference<OnDialogDismissed>> it = this.dismissListeners.iterator();
        while (it.hasNext()) {
            it.remove();
            OnDialogDismissed onDialogDismissed = (OnDialogDismissed) it.next().get();
            if (onDialogDismissed != null) {
                onDialogDismissed.onDialogDismissed();
            }
        }
    }

    public final View createView(PrivacyElement privacyElement) {
        LayoutInflater from = LayoutInflater.from(getContext());
        int i = R$layout.privacy_dialog_item;
        ViewGroup viewGroup = this.rootView;
        if (viewGroup == null) {
            viewGroup = null;
        }
        View inflate = from.inflate(i, viewGroup, false);
        if (inflate != null) {
            ViewGroup viewGroup2 = (ViewGroup) inflate;
            LayerDrawable drawableForType = getDrawableForType(privacyElement.getType());
            int i2 = R$id.icon;
            drawableForType.findDrawableByLayerId(i2).setTint(this.iconColorSolid);
            ImageView imageView = (ImageView) viewGroup2.requireViewById(i2);
            imageView.setImageDrawable(drawableForType);
            imageView.setContentDescription(privacyElement.getType().getName(imageView.getContext()));
            int stringIdForState = getStringIdForState(privacyElement.getActive());
            CharSequence applicationName = privacyElement.getPhoneCall() ? this.phonecall : privacyElement.getApplicationName();
            if (privacyElement.getEnterprise()) {
                applicationName = TextUtils.concat(new CharSequence[]{applicationName, this.enterpriseText});
            }
            ((TextView) viewGroup2.requireViewById(R$id.text)).setText(getFinalText(getContext().getString(stringIdForState, new Object[]{applicationName}), privacyElement.getAttributionLabel(), privacyElement.getProxyLabel()));
            if (privacyElement.getPhoneCall()) {
                viewGroup2.requireViewById(R$id.chevron).setVisibility(8);
            }
            viewGroup2.setTag(privacyElement);
            if (!privacyElement.getPhoneCall()) {
                viewGroup2.setOnClickListener(this.clickListener);
            }
            return viewGroup2;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    public final CharSequence getFinalText(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        String str;
        if (charSequence2 != null && charSequence3 != null) {
            str = getContext().getString(R$string.ongoing_privacy_dialog_attribution_proxy_label, new Object[]{charSequence2, charSequence3});
        } else if (charSequence2 != null) {
            str = getContext().getString(R$string.ongoing_privacy_dialog_attribution_label, new Object[]{charSequence2});
        } else if (charSequence3 != null) {
            str = getContext().getString(R$string.ongoing_privacy_dialog_attribution_text, new Object[]{charSequence3});
        } else {
            str = null;
        }
        if (str == null) {
            return charSequence;
        }
        return TextUtils.concat(new CharSequence[]{charSequence, " ", str});
    }

    public final int getStringIdForState(boolean z) {
        if (z) {
            return R$string.ongoing_privacy_dialog_using_op;
        }
        return R$string.ongoing_privacy_dialog_recent_op;
    }

    public final LayerDrawable getDrawableForType(PrivacyType privacyType) {
        int i;
        Context context = getContext();
        int i2 = WhenMappings.$EnumSwitchMapping$0[privacyType.ordinal()];
        if (i2 == 1) {
            i = R$drawable.privacy_item_circle_location;
        } else if (i2 == 2) {
            i = R$drawable.privacy_item_circle_camera;
        } else if (i2 == 3) {
            i = R$drawable.privacy_item_circle_microphone;
        } else if (i2 == 4) {
            i = R$drawable.privacy_item_circle_media_projection;
        } else {
            throw new NoWhenBranchMatchedException();
        }
        Drawable drawable = context.getDrawable(i);
        if (drawable != null) {
            return (LayerDrawable) drawable;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }

    /* compiled from: PrivacyDialog.kt */
    public static final class PrivacyElement {
        public final boolean active;
        @NotNull
        public final CharSequence applicationName;
        @Nullable
        public final CharSequence attributionLabel;
        @Nullable
        public final CharSequence attributionTag;
        @NotNull
        public final StringBuilder builder;
        public final boolean enterprise;
        public final long lastActiveTimestamp;
        @Nullable
        public final Intent navigationIntent;
        @NotNull
        public final String packageName;
        @NotNull
        public final CharSequence permGroupName;
        public final boolean phoneCall;
        @Nullable
        public final CharSequence proxyLabel;
        @NotNull
        public final PrivacyType type;
        public final int userId;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PrivacyElement)) {
                return false;
            }
            PrivacyElement privacyElement = (PrivacyElement) obj;
            return this.type == privacyElement.type && Intrinsics.areEqual((Object) this.packageName, (Object) privacyElement.packageName) && this.userId == privacyElement.userId && Intrinsics.areEqual((Object) this.applicationName, (Object) privacyElement.applicationName) && Intrinsics.areEqual((Object) this.attributionTag, (Object) privacyElement.attributionTag) && Intrinsics.areEqual((Object) this.attributionLabel, (Object) privacyElement.attributionLabel) && Intrinsics.areEqual((Object) this.proxyLabel, (Object) privacyElement.proxyLabel) && this.lastActiveTimestamp == privacyElement.lastActiveTimestamp && this.active == privacyElement.active && this.enterprise == privacyElement.enterprise && this.phoneCall == privacyElement.phoneCall && Intrinsics.areEqual((Object) this.permGroupName, (Object) privacyElement.permGroupName) && Intrinsics.areEqual((Object) this.navigationIntent, (Object) privacyElement.navigationIntent);
        }

        public int hashCode() {
            int hashCode = ((((((this.type.hashCode() * 31) + this.packageName.hashCode()) * 31) + Integer.hashCode(this.userId)) * 31) + this.applicationName.hashCode()) * 31;
            CharSequence charSequence = this.attributionTag;
            int i = 0;
            int hashCode2 = (hashCode + (charSequence == null ? 0 : charSequence.hashCode())) * 31;
            CharSequence charSequence2 = this.attributionLabel;
            int hashCode3 = (hashCode2 + (charSequence2 == null ? 0 : charSequence2.hashCode())) * 31;
            CharSequence charSequence3 = this.proxyLabel;
            int hashCode4 = (((hashCode3 + (charSequence3 == null ? 0 : charSequence3.hashCode())) * 31) + Long.hashCode(this.lastActiveTimestamp)) * 31;
            boolean z = this.active;
            boolean z2 = true;
            if (z) {
                z = true;
            }
            int i2 = (hashCode4 + (z ? 1 : 0)) * 31;
            boolean z3 = this.enterprise;
            if (z3) {
                z3 = true;
            }
            int i3 = (i2 + (z3 ? 1 : 0)) * 31;
            boolean z4 = this.phoneCall;
            if (!z4) {
                z2 = z4;
            }
            int hashCode5 = (((i3 + (z2 ? 1 : 0)) * 31) + this.permGroupName.hashCode()) * 31;
            Intent intent = this.navigationIntent;
            if (intent != null) {
                i = intent.hashCode();
            }
            return hashCode5 + i;
        }

        public PrivacyElement(@NotNull PrivacyType privacyType, @NotNull String str, int i, @NotNull CharSequence charSequence, @Nullable CharSequence charSequence2, @Nullable CharSequence charSequence3, @Nullable CharSequence charSequence4, long j, boolean z, boolean z2, boolean z3, @NotNull CharSequence charSequence5, @Nullable Intent intent) {
            String str2 = str;
            CharSequence charSequence6 = charSequence;
            CharSequence charSequence7 = charSequence2;
            CharSequence charSequence8 = charSequence3;
            CharSequence charSequence9 = charSequence4;
            boolean z4 = z;
            boolean z5 = z2;
            boolean z6 = z3;
            CharSequence charSequence10 = charSequence5;
            Intent intent2 = intent;
            this.type = privacyType;
            this.packageName = str2;
            this.userId = i;
            this.applicationName = charSequence6;
            this.attributionTag = charSequence7;
            this.attributionLabel = charSequence8;
            this.proxyLabel = charSequence9;
            this.lastActiveTimestamp = j;
            this.active = z4;
            this.enterprise = z5;
            this.phoneCall = z6;
            this.permGroupName = charSequence10;
            this.navigationIntent = intent2;
            StringBuilder sb = new StringBuilder("PrivacyElement(");
            this.builder = sb;
            sb.append(Intrinsics.stringPlus("type=", privacyType.getLogName()));
            sb.append(Intrinsics.stringPlus(", packageName=", str2));
            sb.append(Intrinsics.stringPlus(", userId=", Integer.valueOf(i)));
            sb.append(Intrinsics.stringPlus(", appName=", charSequence6));
            if (charSequence7 != null) {
                sb.append(Intrinsics.stringPlus(", attributionTag=", charSequence7));
            }
            if (charSequence8 != null) {
                sb.append(Intrinsics.stringPlus(", attributionLabel=", charSequence8));
            }
            if (charSequence9 != null) {
                sb.append(Intrinsics.stringPlus(", proxyLabel=", charSequence9));
            }
            sb.append(Intrinsics.stringPlus(", lastActive=", Long.valueOf(j)));
            if (z4) {
                sb.append(", active");
            }
            if (z5) {
                sb.append(", enterprise");
            }
            if (z6) {
                sb.append(", phoneCall");
            }
            sb.append(", permGroupName=" + charSequence10 + ')');
            if (intent2 != null) {
                sb.append(Intrinsics.stringPlus(", navigationIntent=", intent2));
            }
        }

        @NotNull
        public final PrivacyType getType() {
            return this.type;
        }

        @NotNull
        public final String getPackageName() {
            return this.packageName;
        }

        public final int getUserId() {
            return this.userId;
        }

        @NotNull
        public final CharSequence getApplicationName() {
            return this.applicationName;
        }

        @Nullable
        public final CharSequence getAttributionTag() {
            return this.attributionTag;
        }

        @Nullable
        public final CharSequence getAttributionLabel() {
            return this.attributionLabel;
        }

        @Nullable
        public final CharSequence getProxyLabel() {
            return this.proxyLabel;
        }

        public final long getLastActiveTimestamp() {
            return this.lastActiveTimestamp;
        }

        public final boolean getActive() {
            return this.active;
        }

        public final boolean getEnterprise() {
            return this.enterprise;
        }

        public final boolean getPhoneCall() {
            return this.phoneCall;
        }

        @Nullable
        public final Intent getNavigationIntent() {
            return this.navigationIntent;
        }

        @NotNull
        public String toString() {
            return this.builder.toString();
        }
    }
}
