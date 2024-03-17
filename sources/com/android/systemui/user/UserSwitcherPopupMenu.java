package com.android.systemui.user;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.plugins.FalsingManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UserSwitcherPopupMenu.kt */
public final class UserSwitcherPopupMenu extends ListPopupWindow {
    @Nullable
    public ListAdapter adapter;
    @NotNull
    public final Context context;
    @NotNull
    public final FalsingManager falsingManager;
    public final Resources res;

    public UserSwitcherPopupMenu(@NotNull Context context2, @NotNull FalsingManager falsingManager2) {
        super(context2);
        this.context = context2;
        this.falsingManager = falsingManager2;
        Resources resources = context2.getResources();
        this.res = resources;
        setBackgroundDrawable(resources.getDrawable(R$drawable.bouncer_user_switcher_popup_bg, context2.getTheme()));
        setModal(false);
        setOverlapAnchor(true);
    }

    public void setAdapter(@Nullable ListAdapter listAdapter) {
        super.setAdapter(listAdapter);
        this.adapter = listAdapter;
    }

    public void show() {
        super.show();
        ListView listView = getListView();
        listView.setVerticalScrollBarEnabled(false);
        listView.setHorizontalScrollBarEnabled(false);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setAlpha(0);
        listView.setDivider(shapeDrawable);
        listView.setDividerHeight(this.res.getDimensionPixelSize(R$dimen.bouncer_user_switcher_popup_divider_height));
        int dimensionPixelSize = this.res.getDimensionPixelSize(R$dimen.bouncer_user_switcher_popup_header_height);
        listView.addHeaderView(createSpacer(dimensionPixelSize), (Object) null, false);
        listView.addFooterView(createSpacer(dimensionPixelSize), (Object) null, false);
        setWidth(findMaxWidth(listView));
        super.show();
    }

    public final int findMaxWidth(ListView listView) {
        ListAdapter listAdapter = this.adapter;
        if (listAdapter == null) {
            return 0;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) (((double) this.res.getDisplayMetrics().widthPixels) * 0.25d), Integer.MIN_VALUE);
        int count = listAdapter.getCount();
        int i = 0;
        int i2 = 0;
        while (i < count) {
            int i3 = i + 1;
            View view = listAdapter.getView(i, (View) null, listView);
            view.measure(makeMeasureSpec, 0);
            i2 = Math.max(view.getMeasuredWidth(), i2);
            i = i3;
        }
        return i2;
    }

    public final View createSpacer(int i) {
        return new UserSwitcherPopupMenu$createSpacer$1(i, this.context);
    }
}
