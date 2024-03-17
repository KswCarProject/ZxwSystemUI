package com.android.systemui.tuner;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.Dependency;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$xml;
import com.android.systemui.plugins.IntentButtonProvider;
import com.android.systemui.statusbar.ScalingDrawableWrapper;
import com.android.systemui.statusbar.phone.ExpandableIndicator;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.tuner.ShortcutParser;
import com.android.systemui.tuner.TunerService;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class LockscreenFragment extends PreferenceFragment {
    public Handler mHandler;
    public final ArrayList<TunerService.Tunable> mTunables = new ArrayList<>();
    public TunerService mTunerService;

    public void onCreatePreferences(Bundle bundle, String str) {
        this.mTunerService = (TunerService) Dependency.get(TunerService.class);
        this.mHandler = new Handler();
        addPreferencesFromResource(R$xml.lockscreen_settings);
        setupGroup("sysui_keyguard_left", "sysui_keyguard_left_unlock");
        setupGroup("sysui_keyguard_right", "sysui_keyguard_right_unlock");
    }

    public void onDestroy() {
        super.onDestroy();
        this.mTunables.forEach(new LockscreenFragment$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onDestroy$0(TunerService.Tunable tunable) {
        this.mTunerService.removeTunable(tunable);
    }

    public final void setupGroup(String str, String str2) {
        addTunable(new LockscreenFragment$$ExternalSyntheticLambda1(this, (SwitchPreference) findPreference(str2), findPreference(str)), str);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupGroup$1(SwitchPreference switchPreference, Preference preference, String str, String str2) {
        switchPreference.setVisible(!TextUtils.isEmpty(str2));
        setSummary(preference, str2);
    }

    public final void setSummary(Preference preference, String str) {
        if (str == null) {
            preference.setSummary(R$string.lockscreen_none);
            return;
        }
        CharSequence charSequence = null;
        if (str.contains("::")) {
            ShortcutParser.Shortcut shortcutInfo = getShortcutInfo(getContext(), str);
            if (shortcutInfo != null) {
                charSequence = shortcutInfo.label;
            }
            preference.setSummary(charSequence);
        } else if (str.contains("/")) {
            ActivityInfo activityinfo = getActivityinfo(getContext(), str);
            if (activityinfo != null) {
                charSequence = activityinfo.loadLabel(getContext().getPackageManager());
            }
            preference.setSummary(charSequence);
        } else {
            preference.setSummary(R$string.lockscreen_none);
        }
    }

    public final void addTunable(TunerService.Tunable tunable, String... strArr) {
        this.mTunables.add(tunable);
        this.mTunerService.addTunable(tunable, strArr);
    }

    public static ActivityInfo getActivityinfo(Context context, String str) {
        try {
            return context.getPackageManager().getActivityInfo(ComponentName.unflattenFromString(str), 0);
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static ShortcutParser.Shortcut getShortcutInfo(Context context, String str) {
        return ShortcutParser.Shortcut.create(context, str);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        public final ExpandableIndicator expand;
        public final ImageView icon;
        public final TextView title;

        public Holder(View view) {
            super(view);
            this.icon = (ImageView) view.findViewById(16908294);
            this.title = (TextView) view.findViewById(16908310);
            this.expand = (ExpandableIndicator) view.findViewById(R$id.expand);
        }
    }

    public static class StaticShortcut extends Item {
        public final Context mContext;
        public final ShortcutParser.Shortcut mShortcut;

        public Boolean getExpando() {
            return null;
        }

        public Drawable getDrawable() {
            return this.mShortcut.icon.loadDrawable(this.mContext);
        }

        public String getLabel() {
            return this.mShortcut.label;
        }
    }

    public static class App extends Item {
        public final ArrayList<Item> mChildren;
        public final Context mContext;
        public boolean mExpanded;
        public final LauncherActivityInfo mInfo;

        public Drawable getDrawable() {
            return this.mInfo.getBadgedIcon(this.mContext.getResources().getConfiguration().densityDpi);
        }

        public String getLabel() {
            return this.mInfo.getLabel().toString();
        }

        public Boolean getExpando() {
            if (this.mChildren.size() != 0) {
                return Boolean.valueOf(this.mExpanded);
            }
            return null;
        }

        public void toggleExpando(Adapter adapter) {
            boolean z = !this.mExpanded;
            this.mExpanded = z;
            if (z) {
                this.mChildren.forEach(new LockscreenFragment$App$$ExternalSyntheticLambda0(this, adapter));
            } else {
                this.mChildren.forEach(new LockscreenFragment$App$$ExternalSyntheticLambda1(adapter));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$toggleExpando$0(Adapter adapter, Item item) {
            adapter.addItem(this, item);
        }
    }

    public static abstract class Item {
        public abstract Drawable getDrawable();

        public abstract Boolean getExpando();

        public abstract String getLabel();

        public void toggleExpando(Adapter adapter) {
        }

        private Item() {
        }
    }

    public static class Adapter extends RecyclerView.Adapter<Holder> {
        public final Consumer<Item> mCallback;
        public ArrayList<Item> mItems;

        public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new Holder(LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.tuner_shortcut_item, viewGroup, false));
        }

        public void onBindViewHolder(Holder holder, int i) {
            Item item = this.mItems.get(i);
            holder.icon.setImageDrawable(item.getDrawable());
            holder.title.setText(item.getLabel());
            holder.itemView.setOnClickListener(new LockscreenFragment$Adapter$$ExternalSyntheticLambda0(this, holder));
            Boolean expando = item.getExpando();
            if (expando != null) {
                holder.expand.setVisibility(0);
                holder.expand.setExpanded(expando.booleanValue());
                holder.expand.setOnClickListener(new LockscreenFragment$Adapter$$ExternalSyntheticLambda1(this, holder));
                return;
            }
            holder.expand.setVisibility(8);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$0(Holder holder, View view) {
            this.mCallback.accept(this.mItems.get(holder.getAdapterPosition()));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$1(Holder holder, View view) {
            this.mItems.get(holder.getAdapterPosition()).toggleExpando(this);
        }

        public int getItemCount() {
            return this.mItems.size();
        }

        public void remItem(Item item) {
            int indexOf = this.mItems.indexOf(item);
            this.mItems.remove(item);
            notifyItemRemoved(indexOf);
        }

        public void addItem(Item item, Item item2) {
            int indexOf = this.mItems.indexOf(item) + 1;
            this.mItems.add(indexOf, item2);
            notifyItemInserted(indexOf);
        }
    }

    public static class LockButtonFactory implements ExtensionController.TunerFactory<IntentButtonProvider.IntentButton> {
        public final Context mContext;
        public final String mKey;

        public LockButtonFactory(Context context, String str) {
            this.mContext = context;
            this.mKey = str;
        }

        public String[] keys() {
            return new String[]{this.mKey};
        }

        public IntentButtonProvider.IntentButton create(Map<String, String> map) {
            ActivityInfo activityinfo;
            String str = map.get(this.mKey);
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            if (str.contains("::")) {
                ShortcutParser.Shortcut shortcutInfo = LockscreenFragment.getShortcutInfo(this.mContext, str);
                if (shortcutInfo != null) {
                    return new ShortcutButton(this.mContext, shortcutInfo);
                }
                return null;
            } else if (!str.contains("/") || (activityinfo = LockscreenFragment.getActivityinfo(this.mContext, str)) == null) {
                return null;
            } else {
                return new ActivityButton(this.mContext, activityinfo);
            }
        }
    }

    public static class ShortcutButton implements IntentButtonProvider.IntentButton {
        public final IntentButtonProvider.IntentButton.IconState mIconState;
        public final ShortcutParser.Shortcut mShortcut;

        public ShortcutButton(Context context, ShortcutParser.Shortcut shortcut) {
            this.mShortcut = shortcut;
            IntentButtonProvider.IntentButton.IconState iconState = new IntentButtonProvider.IntentButton.IconState();
            this.mIconState = iconState;
            iconState.isVisible = true;
            iconState.drawable = shortcut.icon.loadDrawable(context).mutate();
            iconState.contentDescription = shortcut.label;
            Drawable drawable = iconState.drawable;
            iconState.drawable = new ScalingDrawableWrapper(drawable, ((float) ((int) TypedValue.applyDimension(1, 32.0f, context.getResources().getDisplayMetrics()))) / ((float) drawable.getIntrinsicWidth()));
            iconState.tint = false;
        }

        public IntentButtonProvider.IntentButton.IconState getIcon() {
            return this.mIconState;
        }

        public Intent getIntent() {
            return this.mShortcut.intent;
        }
    }

    public static class ActivityButton implements IntentButtonProvider.IntentButton {
        public final IntentButtonProvider.IntentButton.IconState mIconState;
        public final Intent mIntent;

        public ActivityButton(Context context, ActivityInfo activityInfo) {
            this.mIntent = new Intent().setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
            IntentButtonProvider.IntentButton.IconState iconState = new IntentButtonProvider.IntentButton.IconState();
            this.mIconState = iconState;
            iconState.isVisible = true;
            iconState.drawable = activityInfo.loadIcon(context.getPackageManager()).mutate();
            iconState.contentDescription = activityInfo.loadLabel(context.getPackageManager());
            Drawable drawable = iconState.drawable;
            iconState.drawable = new ScalingDrawableWrapper(drawable, ((float) ((int) TypedValue.applyDimension(1, 32.0f, context.getResources().getDisplayMetrics()))) / ((float) drawable.getIntrinsicWidth()));
            iconState.tint = false;
        }

        public IntentButtonProvider.IntentButton.IconState getIcon() {
            return this.mIconState;
        }

        public Intent getIntent() {
            return this.mIntent;
        }
    }
}
