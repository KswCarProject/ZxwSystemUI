package androidx.mediarouter.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.os.BuildCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.mediarouter.R$attr;
import androidx.mediarouter.R$string;
import androidx.mediarouter.R$styleable;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;
import androidx.mediarouter.media.MediaRouterParams;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.util.ArrayList;
import java.util.List;

public class MediaRouteButton extends View {
    public static final int[] CHECKABLE_STATE_SET = {16842911};
    public static final int[] CHECKED_STATE_SET = {16842912};
    public static ConnectivityReceiver sConnectivityReceiver;
    public static final SparseArray<Drawable.ConstantState> sRemoteIndicatorCache = new SparseArray<>(2);
    public boolean mAlwaysVisible;
    public boolean mAttachedToWindow;
    public ColorStateList mButtonTint;
    public final MediaRouterCallback mCallback;
    public boolean mCheatSheetEnabled;
    public int mConnectionState;
    public MediaRouteDialogFactory mDialogFactory;
    public boolean mIsFixedIcon;
    public int mLastConnectionState;
    public int mMinHeight;
    public int mMinWidth;
    public Drawable mRemoteIndicator;
    public RemoteIndicatorLoader mRemoteIndicatorLoader;
    public int mRemoteIndicatorResIdToLoad;
    public final MediaRouter mRouter;
    public MediaRouteSelector mSelector;
    public int mVisibility;

    public MediaRouteButton(Context context) {
        this(context, (AttributeSet) null);
    }

    public MediaRouteButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.mediaRouteButtonStyle);
    }

    public MediaRouteButton(Context context, AttributeSet attributeSet, int i) {
        super(MediaRouterThemeHelper.createThemedButtonContext(context), attributeSet, i);
        Drawable.ConstantState constantState;
        this.mSelector = MediaRouteSelector.EMPTY;
        this.mDialogFactory = MediaRouteDialogFactory.getDefault();
        this.mVisibility = 0;
        Context context2 = getContext();
        int[] iArr = R$styleable.MediaRouteButton;
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, iArr, i, 0);
        ViewCompat.saveAttributeDataForStyleable(this, context2, iArr, attributeSet, obtainStyledAttributes, i, 0);
        if (isInEditMode()) {
            this.mRouter = null;
            this.mCallback = null;
            this.mRemoteIndicator = AppCompatResources.getDrawable(context2, obtainStyledAttributes.getResourceId(R$styleable.MediaRouteButton_externalRouteEnabledDrawableStatic, 0));
            return;
        }
        MediaRouter instance = MediaRouter.getInstance(context2);
        this.mRouter = instance;
        this.mCallback = new MediaRouterCallback();
        MediaRouter.RouteInfo selectedRoute = instance.getSelectedRoute();
        int connectionState = selectedRoute.isDefaultOrBluetooth() ^ true ? selectedRoute.getConnectionState() : 0;
        this.mConnectionState = connectionState;
        this.mLastConnectionState = connectionState;
        if (sConnectivityReceiver == null) {
            sConnectivityReceiver = new ConnectivityReceiver(context2.getApplicationContext());
        }
        this.mButtonTint = obtainStyledAttributes.getColorStateList(R$styleable.MediaRouteButton_mediaRouteButtonTint);
        this.mMinWidth = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MediaRouteButton_android_minWidth, 0);
        this.mMinHeight = obtainStyledAttributes.getDimensionPixelSize(R$styleable.MediaRouteButton_android_minHeight, 0);
        int resourceId = obtainStyledAttributes.getResourceId(R$styleable.MediaRouteButton_externalRouteEnabledDrawableStatic, 0);
        this.mRemoteIndicatorResIdToLoad = obtainStyledAttributes.getResourceId(R$styleable.MediaRouteButton_externalRouteEnabledDrawable, 0);
        obtainStyledAttributes.recycle();
        int i2 = this.mRemoteIndicatorResIdToLoad;
        if (!(i2 == 0 || (constantState = sRemoteIndicatorCache.get(i2)) == null)) {
            setRemoteIndicatorDrawable(constantState.newDrawable());
        }
        if (this.mRemoteIndicator == null) {
            if (resourceId != 0) {
                Drawable.ConstantState constantState2 = sRemoteIndicatorCache.get(resourceId);
                if (constantState2 != null) {
                    setRemoteIndicatorDrawableInternal(constantState2.newDrawable());
                } else {
                    RemoteIndicatorLoader remoteIndicatorLoader = new RemoteIndicatorLoader(resourceId, getContext());
                    this.mRemoteIndicatorLoader = remoteIndicatorLoader;
                    remoteIndicatorLoader.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
                }
            } else {
                loadRemoteIndicatorIfNeeded();
            }
        }
        updateContentDescription();
        setClickable(true);
    }

    public void setRouteSelector(MediaRouteSelector mediaRouteSelector) {
        if (mediaRouteSelector == null) {
            throw new IllegalArgumentException("selector must not be null");
        } else if (!this.mSelector.equals(mediaRouteSelector)) {
            if (this.mAttachedToWindow) {
                if (!this.mSelector.isEmpty()) {
                    this.mRouter.removeCallback(this.mCallback);
                }
                if (!mediaRouteSelector.isEmpty()) {
                    this.mRouter.addCallback(mediaRouteSelector, this.mCallback);
                }
            }
            this.mSelector = mediaRouteSelector;
            refreshRoute();
        }
    }

    public void setDialogFactory(MediaRouteDialogFactory mediaRouteDialogFactory) {
        if (mediaRouteDialogFactory != null) {
            this.mDialogFactory = mediaRouteDialogFactory;
            return;
        }
        throw new IllegalArgumentException("factory must not be null");
    }

    public boolean showDialog() {
        if (!this.mAttachedToWindow) {
            return false;
        }
        MediaRouterParams routerParams = this.mRouter.getRouterParams();
        if (routerParams == null) {
            return showDialogForType(1);
        }
        if (!routerParams.isOutputSwitcherEnabled() || !MediaRouter.isMediaTransferEnabled() || !showOutputSwitcher()) {
            return showDialogForType(routerParams.getDialogType());
        }
        return true;
    }

    public final boolean showDialogForType(int i) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            if (this.mRouter.getSelectedRoute().isDefaultOrBluetooth()) {
                if (fragmentManager.findFragmentByTag("android.support.v7.mediarouter:MediaRouteChooserDialogFragment") != null) {
                    Log.w("MediaRouteButton", "showDialog(): Route chooser dialog already showing!");
                    return false;
                }
                MediaRouteChooserDialogFragment onCreateChooserDialogFragment = this.mDialogFactory.onCreateChooserDialogFragment();
                onCreateChooserDialogFragment.setRouteSelector(this.mSelector);
                if (i == 2) {
                    onCreateChooserDialogFragment.setUseDynamicGroup(true);
                }
                FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
                beginTransaction.add(onCreateChooserDialogFragment, "android.support.v7.mediarouter:MediaRouteChooserDialogFragment");
                beginTransaction.commitAllowingStateLoss();
            } else if (fragmentManager.findFragmentByTag("android.support.v7.mediarouter:MediaRouteControllerDialogFragment") != null) {
                Log.w("MediaRouteButton", "showDialog(): Route controller dialog already showing!");
                return false;
            } else {
                MediaRouteControllerDialogFragment onCreateControllerDialogFragment = this.mDialogFactory.onCreateControllerDialogFragment();
                onCreateControllerDialogFragment.setRouteSelector(this.mSelector);
                if (i == 2) {
                    onCreateControllerDialogFragment.setUseDynamicGroup(true);
                }
                FragmentTransaction beginTransaction2 = fragmentManager.beginTransaction();
                beginTransaction2.add(onCreateControllerDialogFragment, "android.support.v7.mediarouter:MediaRouteControllerDialogFragment");
                beginTransaction2.commitAllowingStateLoss();
            }
            return true;
        }
        throw new IllegalStateException("The activity must be a subclass of FragmentActivity");
    }

    public final boolean showOutputSwitcher() {
        if (!BuildCompat.isAtLeastS()) {
            return false;
        }
        boolean showOutputSwitcherForAndroidSAndAbove = showOutputSwitcherForAndroidSAndAbove();
        return !showOutputSwitcherForAndroidSAndAbove ? showOutputSwitcherForAndroidR() : showOutputSwitcherForAndroidSAndAbove;
    }

    public final boolean showOutputSwitcherForAndroidR() {
        ApplicationInfo applicationInfo;
        Context context = getContext();
        Intent putExtra = new Intent().setAction("com.android.settings.panel.action.MEDIA_OUTPUT").putExtra("com.android.settings.panel.extra.PACKAGE_NAME", context.getPackageName()).putExtra("key_media_session_token", this.mRouter.getMediaSessionToken());
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivities(putExtra, 0)) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo != null && (applicationInfo = activityInfo.applicationInfo) != null && (applicationInfo.flags & 129) != 0) {
                context.startActivity(putExtra);
                return true;
            }
        }
        return false;
    }

    public final boolean showOutputSwitcherForAndroidSAndAbove() {
        ApplicationInfo applicationInfo;
        Context context = getContext();
        Intent putExtra = new Intent().setAction("com.android.systemui.action.LAUNCH_MEDIA_OUTPUT_DIALOG").setPackage(ThemeOverlayApplier.SYSUI_PACKAGE).putExtra("package_name", context.getPackageName()).putExtra("key_media_session_token", this.mRouter.getMediaSessionToken());
        for (ResolveInfo resolveInfo : context.getPackageManager().queryBroadcastReceivers(putExtra, 0)) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo != null && (applicationInfo = activityInfo.applicationInfo) != null && (applicationInfo.flags & 129) != 0) {
                context.sendBroadcast(putExtra);
                return true;
            }
        }
        return false;
    }

    public final FragmentManager getFragmentManager() {
        Activity activity = getActivity();
        if (activity instanceof FragmentActivity) {
            return ((FragmentActivity) activity).getSupportFragmentManager();
        }
        return null;
    }

    public final Activity getActivity() {
        for (Context context = getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
        }
        return null;
    }

    public void setCheatSheetEnabled(boolean z) {
        if (z != this.mCheatSheetEnabled) {
            this.mCheatSheetEnabled = z;
            updateContentDescription();
        }
    }

    public boolean performClick() {
        boolean performClick = super.performClick();
        if (!performClick) {
            playSoundEffect(0);
        }
        loadRemoteIndicatorIfNeeded();
        if (showDialog() || performClick) {
            return true;
        }
        return false;
    }

    public int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 1);
        if (this.mRouter == null || this.mIsFixedIcon) {
            return onCreateDrawableState;
        }
        int i2 = this.mConnectionState;
        if (i2 == 1) {
            View.mergeDrawableStates(onCreateDrawableState, CHECKABLE_STATE_SET);
        } else if (i2 == 2) {
            View.mergeDrawableStates(onCreateDrawableState, CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }

    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mRemoteIndicator != null) {
            this.mRemoteIndicator.setState(getDrawableState());
            if (this.mRemoteIndicator.getCurrent() instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = (AnimationDrawable) this.mRemoteIndicator.getCurrent();
                int i = this.mConnectionState;
                if (i == 1 || this.mLastConnectionState != i) {
                    if (!animationDrawable.isRunning()) {
                        animationDrawable.start();
                    }
                } else if (i == 2 && !animationDrawable.isRunning()) {
                    animationDrawable.selectDrawable(animationDrawable.getNumberOfFrames() - 1);
                }
            }
            invalidate();
        }
        this.mLastConnectionState = this.mConnectionState;
    }

    public void setRemoteIndicatorDrawable(Drawable drawable) {
        this.mRemoteIndicatorResIdToLoad = 0;
        setRemoteIndicatorDrawableInternal(drawable);
    }

    public void setAlwaysVisible(boolean z) {
        if (z != this.mAlwaysVisible) {
            this.mAlwaysVisible = z;
            refreshVisibility();
            refreshRoute();
        }
    }

    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mRemoteIndicator;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mRemoteIndicator;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    public void setVisibility(int i) {
        this.mVisibility = i;
        refreshVisibility();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mAttachedToWindow = true;
            if (!this.mSelector.isEmpty()) {
                this.mRouter.addCallback(this.mSelector, this.mCallback);
            }
            refreshRoute();
            sConnectivityReceiver.registerReceiver(this);
        }
    }

    public void onDetachedFromWindow() {
        if (!isInEditMode()) {
            this.mAttachedToWindow = false;
            if (!this.mSelector.isEmpty()) {
                this.mRouter.removeCallback(this.mCallback);
            }
            sConnectivityReceiver.unregisterReceiver(this);
        }
        super.onDetachedFromWindow();
    }

    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int i3 = this.mMinWidth;
        Drawable drawable = this.mRemoteIndicator;
        int i4 = 0;
        int max = Math.max(i3, drawable != null ? drawable.getIntrinsicWidth() + getPaddingLeft() + getPaddingRight() : 0);
        int i5 = this.mMinHeight;
        Drawable drawable2 = this.mRemoteIndicator;
        if (drawable2 != null) {
            i4 = drawable2.getIntrinsicHeight() + getPaddingTop() + getPaddingBottom();
        }
        int max2 = Math.max(i5, i4);
        if (mode == Integer.MIN_VALUE) {
            size = Math.min(size, max);
        } else if (mode != 1073741824) {
            size = max;
        }
        if (mode2 == Integer.MIN_VALUE) {
            size2 = Math.min(size2, max2);
        } else if (mode2 != 1073741824) {
            size2 = max2;
        }
        setMeasuredDimension(size, size2);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mRemoteIndicator != null) {
            int paddingLeft = getPaddingLeft();
            int width = getWidth() - getPaddingRight();
            int paddingTop = getPaddingTop();
            int height = getHeight() - getPaddingBottom();
            int intrinsicWidth = this.mRemoteIndicator.getIntrinsicWidth();
            int intrinsicHeight = this.mRemoteIndicator.getIntrinsicHeight();
            int i = paddingLeft + (((width - paddingLeft) - intrinsicWidth) / 2);
            int i2 = paddingTop + (((height - paddingTop) - intrinsicHeight) / 2);
            this.mRemoteIndicator.setBounds(i, i2, intrinsicWidth + i, intrinsicHeight + i2);
            this.mRemoteIndicator.draw(canvas);
        }
    }

    public final void loadRemoteIndicatorIfNeeded() {
        if (this.mRemoteIndicatorResIdToLoad > 0) {
            RemoteIndicatorLoader remoteIndicatorLoader = this.mRemoteIndicatorLoader;
            if (remoteIndicatorLoader != null) {
                remoteIndicatorLoader.cancel(false);
            }
            RemoteIndicatorLoader remoteIndicatorLoader2 = new RemoteIndicatorLoader(this.mRemoteIndicatorResIdToLoad, getContext());
            this.mRemoteIndicatorLoader = remoteIndicatorLoader2;
            this.mRemoteIndicatorResIdToLoad = 0;
            remoteIndicatorLoader2.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
        }
    }

    public void setRemoteIndicatorDrawableInternal(Drawable drawable) {
        RemoteIndicatorLoader remoteIndicatorLoader = this.mRemoteIndicatorLoader;
        if (remoteIndicatorLoader != null) {
            remoteIndicatorLoader.cancel(false);
        }
        Drawable drawable2 = this.mRemoteIndicator;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback) null);
            unscheduleDrawable(this.mRemoteIndicator);
        }
        if (drawable != null) {
            if (this.mButtonTint != null) {
                drawable = DrawableCompat.wrap(drawable.mutate());
                DrawableCompat.setTintList(drawable, this.mButtonTint);
            }
            drawable.setCallback(this);
            drawable.setState(getDrawableState());
            drawable.setVisible(getVisibility() == 0, false);
        }
        this.mRemoteIndicator = drawable;
        refreshDrawableState();
    }

    public void refreshVisibility() {
        int i;
        if (this.mVisibility != 0 || this.mAlwaysVisible || sConnectivityReceiver.isConnected()) {
            i = this.mVisibility;
        } else {
            i = 4;
        }
        super.setVisibility(i);
        Drawable drawable = this.mRemoteIndicator;
        if (drawable != null) {
            drawable.setVisible(getVisibility() == 0, false);
        }
    }

    public void refreshRoute() {
        MediaRouter.RouteInfo selectedRoute = this.mRouter.getSelectedRoute();
        boolean z = true;
        boolean z2 = !selectedRoute.isDefaultOrBluetooth();
        int connectionState = z2 ? selectedRoute.getConnectionState() : 0;
        if (this.mConnectionState != connectionState) {
            this.mConnectionState = connectionState;
            updateContentDescription();
            refreshDrawableState();
        }
        if (connectionState == 1) {
            loadRemoteIndicatorIfNeeded();
        }
        if (this.mAttachedToWindow) {
            if (!this.mAlwaysVisible && !z2 && !this.mRouter.isRouteAvailable(this.mSelector, 1)) {
                z = false;
            }
            setEnabled(z);
        }
    }

    public final void updateContentDescription() {
        int i;
        int i2 = this.mConnectionState;
        if (i2 == 1) {
            i = R$string.mr_cast_button_connecting;
        } else if (i2 != 2) {
            i = R$string.mr_cast_button_disconnected;
        } else {
            i = R$string.mr_cast_button_connected;
        }
        String string = getContext().getString(i);
        setContentDescription(string);
        if (!this.mCheatSheetEnabled || TextUtils.isEmpty(string)) {
            string = null;
        }
        TooltipCompat.setTooltipText(this, string);
    }

    public final class MediaRouterCallback extends MediaRouter.Callback {
        public MediaRouterCallback() {
        }

        public void onRouteAdded(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            MediaRouteButton.this.refreshRoute();
        }

        public void onRouteRemoved(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            MediaRouteButton.this.refreshRoute();
        }

        public void onRouteChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            MediaRouteButton.this.refreshRoute();
        }

        public void onRouteSelected(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            MediaRouteButton.this.refreshRoute();
        }

        public void onRouteUnselected(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            MediaRouteButton.this.refreshRoute();
        }

        public void onProviderAdded(MediaRouter mediaRouter, MediaRouter.ProviderInfo providerInfo) {
            MediaRouteButton.this.refreshRoute();
        }

        public void onProviderRemoved(MediaRouter mediaRouter, MediaRouter.ProviderInfo providerInfo) {
            MediaRouteButton.this.refreshRoute();
        }

        public void onProviderChanged(MediaRouter mediaRouter, MediaRouter.ProviderInfo providerInfo) {
            MediaRouteButton.this.refreshRoute();
        }

        public void onRouterParamsChanged(MediaRouter mediaRouter, MediaRouterParams mediaRouterParams) {
            boolean z = mediaRouterParams != null ? mediaRouterParams.getExtras().getBoolean("androidx.mediarouter.media.MediaRouterParams.FIXED_CAST_ICON") : false;
            MediaRouteButton mediaRouteButton = MediaRouteButton.this;
            if (mediaRouteButton.mIsFixedIcon != z) {
                mediaRouteButton.mIsFixedIcon = z;
                mediaRouteButton.refreshDrawableState();
            }
        }
    }

    public final class RemoteIndicatorLoader extends AsyncTask<Void, Void, Drawable> {
        public final Context mContext;
        public final int mResId;

        public RemoteIndicatorLoader(int i, Context context) {
            this.mResId = i;
            this.mContext = context;
        }

        public Drawable doInBackground(Void... voidArr) {
            if (MediaRouteButton.sRemoteIndicatorCache.get(this.mResId) == null) {
                return AppCompatResources.getDrawable(this.mContext, this.mResId);
            }
            return null;
        }

        public void onPostExecute(Drawable drawable) {
            if (drawable != null) {
                cacheAndReset(drawable);
            } else {
                Drawable.ConstantState constantState = MediaRouteButton.sRemoteIndicatorCache.get(this.mResId);
                if (constantState != null) {
                    drawable = constantState.newDrawable();
                }
                MediaRouteButton.this.mRemoteIndicatorLoader = null;
            }
            MediaRouteButton.this.setRemoteIndicatorDrawableInternal(drawable);
        }

        public void onCancelled(Drawable drawable) {
            cacheAndReset(drawable);
        }

        public final void cacheAndReset(Drawable drawable) {
            if (drawable != null) {
                MediaRouteButton.sRemoteIndicatorCache.put(this.mResId, drawable.getConstantState());
            }
            MediaRouteButton.this.mRemoteIndicatorLoader = null;
        }
    }

    public static final class ConnectivityReceiver extends BroadcastReceiver {
        public List<MediaRouteButton> mButtons;
        public final Context mContext;
        public boolean mIsConnected = true;

        public ConnectivityReceiver(Context context) {
            this.mContext = context;
            this.mButtons = new ArrayList();
        }

        public void registerReceiver(MediaRouteButton mediaRouteButton) {
            if (this.mButtons.size() == 0) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                this.mContext.registerReceiver(this, intentFilter);
            }
            this.mButtons.add(mediaRouteButton);
        }

        public void unregisterReceiver(MediaRouteButton mediaRouteButton) {
            this.mButtons.remove(mediaRouteButton);
            if (this.mButtons.size() == 0) {
                this.mContext.unregisterReceiver(this);
            }
        }

        public boolean isConnected() {
            return this.mIsConnected;
        }

        public void onReceive(Context context, Intent intent) {
            boolean z;
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction()) && this.mIsConnected != (!intent.getBooleanExtra("noConnectivity", false))) {
                this.mIsConnected = z;
                for (MediaRouteButton refreshVisibility : this.mButtons) {
                    refreshVisibility.refreshVisibility();
                }
            }
        }
    }
}
