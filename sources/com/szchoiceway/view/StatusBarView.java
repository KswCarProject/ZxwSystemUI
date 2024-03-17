package com.szchoiceway.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class StatusBarView extends RelativeLayout {
    public static String packName = "com.szchoiceway.customerui";
    public boolean mIsCustomerResource;
    public View mMenuView;
    public int taskId;
    public View vHome;
    public View vRecents;
    public View vReturn;

    public StatusBarView(Context context) {
        this(context, (AttributeSet) null, 0, 0);
    }

    public StatusBarView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public StatusBarView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public StatusBarView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mIsCustomerResource = false;
        this.vRecents = null;
        this.vHome = null;
        this.vReturn = null;
        this.taskId = 0;
        createView();
    }

    public final void createView() {
        Object systemService;
        try {
            Context createPackageContext = getContext().createPackageContext(packName, 3);
            if (!(createPackageContext == null || (systemService = createPackageContext.getSystemService("layout_inflater")) == null)) {
                Resources resources = createPackageContext.getResources();
                View inflate = ((LayoutInflater) systemService).inflate(resources.getLayout(resources.getIdentifier("layout_status_bar_zxw", "layout", packName)), this);
                this.mMenuView = inflate;
                if (inflate != null) {
                    bindCustomerUIView(createPackageContext, inflate);
                }
            }
        } catch (Exception e) {
            this.mMenuView = null;
            e.printStackTrace();
        }
        if (this.mMenuView == null) {
            setVisibility(8);
        } else {
            this.mIsCustomerResource = true;
        }
        Log.i("SysUIStatusBarView", "StatusBarView createView");
    }

    public final void bindCustomerUIView(Context context, View view) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("btnTask", "id", packName);
        if (identifier != 0) {
            this.vRecents = view.findViewById(identifier);
            Log.i("SysUIStatusBarView", "bindCustomerUIView btnTask id = " + identifier + "vRecents = " + this.vRecents.getClass());
        }
        int identifier2 = resources.getIdentifier("btnHome", "id", packName);
        if (identifier2 != 0) {
            this.vHome = view.findViewById(identifier2);
            Log.i("SysUIStatusBarView", "bindCustomerUIView btnHome id = " + identifier2 + "vHome = " + this.vHome.getClass());
        }
        int identifier3 = resources.getIdentifier("btnExit", "id", packName);
        if (identifier3 != 0) {
            this.vReturn = view.findViewById(identifier3);
            Log.i("SysUIStatusBarView", "bindCustomerUIView vReturn id = " + identifier3 + "vReturn = " + this.vReturn.getClass());
        }
    }

    public View getRecentView() {
        return this.vRecents;
    }

    public View getHomeView() {
        return this.vHome;
    }

    public View getReturnView() {
        return this.vReturn;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void onDetachedFromWindow() {
        this.mIsCustomerResource = false;
        super.onDetachedFromWindow();
    }

    public boolean isCustomerRes() {
        return this.mIsCustomerResource;
    }
}
