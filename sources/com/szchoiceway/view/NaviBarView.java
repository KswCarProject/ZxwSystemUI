package com.szchoiceway.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class NaviBarView extends RelativeLayout {
    public static String packName = "com.szchoiceway.customerui";
    public boolean mIsCustomerResource;
    public View mMenuView;
    public int taskId;
    public View vHome;
    public View vRecents;
    public View vReturn;
    public View vShowApp;

    public NaviBarView(Context context) {
        this(context, (AttributeSet) null, 0, 0);
    }

    public NaviBarView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public NaviBarView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public NaviBarView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mIsCustomerResource = false;
        this.vRecents = null;
        this.vHome = null;
        this.vReturn = null;
        this.vShowApp = null;
        this.taskId = 0;
        createView();
    }

    public final void createView() {
        Object systemService;
        try {
            Context createPackageContext = getContext().createPackageContext(packName, 3);
            if (!(createPackageContext == null || (systemService = createPackageContext.getSystemService("layout_inflater")) == null)) {
                LayoutInflater layoutInflater = (LayoutInflater) systemService;
                Resources resources = createPackageContext.getResources();
                int identifier = resources.getIdentifier("layout_navi_bar_zxw", "layout", packName);
                if (identifier != 0) {
                    View inflate = layoutInflater.inflate(resources.getLayout(identifier), this);
                    this.mMenuView = inflate;
                    if (inflate != null) {
                        bindCustomerUIView(createPackageContext, inflate);
                    }
                } else {
                    Log.e("SysUINaviBarView", "layout_navi_bar_zxw.xml not found! please check your layout xml.");
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
        Log.i("SysUINaviBarView", "NaviBarView createView");
    }

    public final void bindCustomerUIView(Context context, View view) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("btnTask", "id", packName);
        if (identifier != 0) {
            this.vRecents = view.findViewById(identifier);
            Log.i("SysUINaviBarView", "bindCustomerUIView btnTask id = " + identifier + "vRecents = " + this.vRecents.getClass());
        }
        int identifier2 = resources.getIdentifier("btnHome", "id", packName);
        if (identifier2 != 0) {
            this.vHome = view.findViewById(identifier2);
            Log.i("SysUINaviBarView", "bindCustomerUIView btnHome id = " + identifier2 + "vHome = " + this.vHome.getClass());
        }
        int identifier3 = resources.getIdentifier("btnExit", "id", packName);
        if (identifier3 != 0) {
            this.vReturn = view.findViewById(identifier3);
            Log.i("SysUINaviBarView", "bindCustomerUIView vReturn id = " + identifier3 + "vReturn = " + this.vReturn.getClass());
        }
        int identifier4 = resources.getIdentifier("btnShowApp", "id", packName);
        if (identifier4 != 0) {
            this.vShowApp = view.findViewById(identifier4);
            Log.i("SysUINaviBarView", "bindCustomerUIView vReturn id = " + identifier4 + "vShowApp = " + this.vShowApp.getClass());
        }
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
