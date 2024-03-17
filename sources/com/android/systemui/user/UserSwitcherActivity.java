package com.android.systemui.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.constraintlayout.helper.widget.Flow;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.LifecycleActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UserSwitcherActivity.kt */
public final class UserSwitcherActivity extends LifecycleActivity {
    @NotNull
    public final UserSwitcherActivity$adapter$1 adapter;
    public View addButton;
    @NotNull
    public List<UserSwitcherController.UserRecord> addUserRecords = new ArrayList();
    @NotNull
    public final BroadcastDispatcher broadcastDispatcher;
    public BroadcastReceiver broadcastReceiver;
    @NotNull
    public final FalsingManager falsingManager;
    @NotNull
    public final LayoutInflater layoutInflater;
    @NotNull
    public final UserSwitcherController.UserRecord manageUserRecord = new UserSwitcherController.UserRecord((UserInfo) null, (Bitmap) null, false, false, false, false, false, false);
    public ViewGroup parent;
    @Nullable
    public UserSwitcherPopupMenu popupMenu;
    @NotNull
    public final UserManager userManager;
    @NotNull
    public final UserTracker.Callback userSwitchedCallback = new UserSwitcherActivity$userSwitchedCallback$1(this);
    @NotNull
    public final UserSwitcherController userSwitcherController;
    @NotNull
    public final UserTracker userTracker;

    public UserSwitcherActivity(@NotNull UserSwitcherController userSwitcherController2, @NotNull BroadcastDispatcher broadcastDispatcher2, @NotNull LayoutInflater layoutInflater2, @NotNull FalsingManager falsingManager2, @NotNull UserManager userManager2, @NotNull UserTracker userTracker2) {
        this.userSwitcherController = userSwitcherController2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.layoutInflater = layoutInflater2;
        this.falsingManager = falsingManager2;
        this.userManager = userManager2;
        this.userTracker = userTracker2;
        this.adapter = new UserSwitcherActivity$adapter$1(this, userSwitcherController2);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.user_switcher_fullscreen);
        getWindow().getDecorView().setSystemUiVisibility(770);
        this.parent = (ViewGroup) requireViewById(R$id.user_switcher_root);
        requireViewById(R$id.cancel).setOnClickListener(new UserSwitcherActivity$onCreate$1$1(this));
        View requireViewById = requireViewById(R$id.add);
        requireViewById.setOnClickListener(new UserSwitcherActivity$onCreate$2$1(this));
        this.addButton = requireViewById;
        UserSwitcherController userSwitcherController2 = this.userSwitcherController;
        ViewGroup viewGroup = this.parent;
        ViewGroup viewGroup2 = null;
        if (viewGroup == null) {
            viewGroup = null;
        }
        userSwitcherController2.init(viewGroup);
        initBroadcastReceiver();
        ViewGroup viewGroup3 = this.parent;
        if (viewGroup3 != null) {
            viewGroup2 = viewGroup3;
        }
        viewGroup2.post(new UserSwitcherActivity$onCreate$3(this));
        this.userTracker.addCallback(this.userSwitchedCallback, getMainExecutor());
    }

    public final void showPopupMenu() {
        ArrayList arrayList = new ArrayList();
        for (UserSwitcherController.UserRecord add : this.addUserRecords) {
            arrayList.add(add);
        }
        Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
        ItemAdapter itemAdapter = new ItemAdapter(this, R$layout.user_switcher_fullscreen_popup_item, this.layoutInflater, new UserSwitcherActivity$showPopupMenu$popupMenuAdapter$1(this), new UserSwitcherActivity$showPopupMenu$popupMenuAdapter$2(this));
        ref$ObjectRef.element = itemAdapter;
        ItemAdapter itemAdapter2 = itemAdapter;
        itemAdapter.addAll(arrayList);
        UserSwitcherPopupMenu userSwitcherPopupMenu = new UserSwitcherPopupMenu(this, this.falsingManager);
        View view = this.addButton;
        if (view == null) {
            view = null;
        }
        userSwitcherPopupMenu.setAnchorView(view);
        userSwitcherPopupMenu.setAdapter((ListAdapter) ref$ObjectRef.element);
        userSwitcherPopupMenu.setOnItemClickListener(new UserSwitcherActivity$showPopupMenu$2$1(this, ref$ObjectRef, userSwitcherPopupMenu));
        userSwitcherPopupMenu.show();
        this.popupMenu = userSwitcherPopupMenu;
    }

    public final void buildUserViews() {
        ViewGroup viewGroup = this.parent;
        View view = null;
        if (viewGroup == null) {
            viewGroup = null;
        }
        int childCount = viewGroup.getChildCount();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i < childCount) {
            int i4 = i + 1;
            ViewGroup viewGroup2 = this.parent;
            if (viewGroup2 == null) {
                viewGroup2 = null;
            }
            if (Intrinsics.areEqual(viewGroup2.getChildAt(i).getTag(), (Object) "user_view")) {
                if (i2 == 0) {
                    i3 = i;
                }
                i2++;
            }
            i = i4;
        }
        ViewGroup viewGroup3 = this.parent;
        if (viewGroup3 == null) {
            viewGroup3 = null;
        }
        viewGroup3.removeViews(i3, i2);
        this.addUserRecords.clear();
        Flow flow = (Flow) requireViewById(R$id.flow);
        ViewGroup viewGroup4 = this.parent;
        if (viewGroup4 == null) {
            viewGroup4 = null;
        }
        int width = viewGroup4.getWidth();
        int maxColumns = getMaxColumns(this.adapter.getTotalUserViews());
        int dimensionPixelSize = (width - ((maxColumns - 1) * getResources().getDimensionPixelSize(R$dimen.user_switcher_fullscreen_horizontal_gap))) / maxColumns;
        flow.setMaxElementsWrap(maxColumns);
        int count = this.adapter.getCount();
        int i5 = 0;
        while (i5 < count) {
            int i6 = i5 + 1;
            UserSwitcherController.UserRecord item = this.adapter.getItem(i5);
            if (this.adapter.doNotRenderUserView(item)) {
                this.addUserRecords.add(item);
            } else {
                UserSwitcherActivity$adapter$1 userSwitcherActivity$adapter$1 = this.adapter;
                ViewGroup viewGroup5 = this.parent;
                if (viewGroup5 == null) {
                    viewGroup5 = null;
                }
                View view2 = userSwitcherActivity$adapter$1.getView(i5, (View) null, viewGroup5);
                ImageView imageView = (ImageView) view2.requireViewById(R$id.user_switcher_icon);
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                if (dimensionPixelSize < layoutParams.width) {
                    layoutParams.width = dimensionPixelSize;
                    layoutParams.height = dimensionPixelSize;
                    imageView.setLayoutParams(layoutParams);
                }
                view2.setId(View.generateViewId());
                ViewGroup viewGroup6 = this.parent;
                if (viewGroup6 == null) {
                    viewGroup6 = null;
                }
                viewGroup6.addView(view2);
                flow.addView(view2);
                view2.setOnClickListener(new UserSwitcherActivity$buildUserViews$2(this, item));
            }
            i5 = i6;
        }
        if (!this.addUserRecords.isEmpty()) {
            this.addUserRecords.add(this.manageUserRecord);
            View view3 = this.addButton;
            if (view3 != null) {
                view = view3;
            }
            view.setVisibility(0);
            return;
        }
        View view4 = this.addButton;
        if (view4 != null) {
            view = view4;
        }
        view.setVisibility(8);
    }

    public void onBackPressed() {
        finish();
    }

    public void onDestroy() {
        super.onDestroy();
        BroadcastDispatcher broadcastDispatcher2 = this.broadcastDispatcher;
        BroadcastReceiver broadcastReceiver2 = this.broadcastReceiver;
        if (broadcastReceiver2 == null) {
            broadcastReceiver2 = null;
        }
        broadcastDispatcher2.unregisterReceiver(broadcastReceiver2);
        this.userTracker.removeCallback(this.userSwitchedCallback);
    }

    public final void initBroadcastReceiver() {
        this.broadcastReceiver = new UserSwitcherActivity$initBroadcastReceiver$1(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        BroadcastDispatcher broadcastDispatcher2 = this.broadcastDispatcher;
        BroadcastReceiver broadcastReceiver2 = this.broadcastReceiver;
        if (broadcastReceiver2 == null) {
            broadcastReceiver2 = null;
        }
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher2, broadcastReceiver2, intentFilter, (Executor) null, (UserHandle) null, 0, (String) null, 60, (Object) null);
    }

    @VisibleForTesting
    public final int getMaxColumns(int i) {
        if (i < 5) {
            return 4;
        }
        return (int) Math.ceil(((double) i) / 2.0d);
    }

    /* compiled from: UserSwitcherActivity.kt */
    public static final class ItemAdapter extends ArrayAdapter<UserSwitcherController.UserRecord> {
        @NotNull
        public final Function1<UserSwitcherController.UserRecord, Drawable> iconGetter;
        @NotNull
        public final LayoutInflater layoutInflater;
        @NotNull
        public final Context parentContext;
        public final int resource;
        @NotNull
        public final Function1<UserSwitcherController.UserRecord, String> textGetter;

        @NotNull
        public final Function1<UserSwitcherController.UserRecord, String> getTextGetter() {
            return this.textGetter;
        }

        @NotNull
        public final Function1<UserSwitcherController.UserRecord, Drawable> getIconGetter() {
            return this.iconGetter;
        }

        public ItemAdapter(@NotNull Context context, int i, @NotNull LayoutInflater layoutInflater2, @NotNull Function1<? super UserSwitcherController.UserRecord, String> function1, @NotNull Function1<? super UserSwitcherController.UserRecord, ? extends Drawable> function12) {
            super(context, i);
            this.parentContext = context;
            this.resource = i;
            this.layoutInflater = layoutInflater2;
            this.textGetter = function1;
            this.iconGetter = function12;
        }

        @NotNull
        public View getView(int i, @Nullable View view, @NotNull ViewGroup viewGroup) {
            UserSwitcherController.UserRecord userRecord = (UserSwitcherController.UserRecord) getItem(i);
            if (view == null) {
                view = this.layoutInflater.inflate(this.resource, viewGroup, false);
            }
            ((ImageView) view.requireViewById(R$id.icon)).setImageDrawable(getIconGetter().invoke(userRecord));
            ((TextView) view.requireViewById(R$id.text)).setText(getTextGetter().invoke(userRecord));
            return view;
        }
    }
}
