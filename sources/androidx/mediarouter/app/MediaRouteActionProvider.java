package androidx.mediarouter.app;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ActionProvider;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;
import java.lang.ref.WeakReference;

public class MediaRouteActionProvider extends ActionProvider {
    public boolean mAlwaysVisible;
    public MediaRouteButton mButton;
    public final MediaRouterCallback mCallback;
    public MediaRouteDialogFactory mDialogFactory = MediaRouteDialogFactory.getDefault();
    public final MediaRouter mRouter;
    public MediaRouteSelector mSelector = MediaRouteSelector.EMPTY;

    public boolean overridesItemVisibility() {
        return true;
    }

    public MediaRouteActionProvider(Context context) {
        super(context);
        this.mRouter = MediaRouter.getInstance(context);
        this.mCallback = new MediaRouterCallback(this);
    }

    public MediaRouteButton onCreateMediaRouteButton() {
        return new MediaRouteButton(getContext());
    }

    public View onCreateActionView() {
        if (this.mButton != null) {
            Log.e("MRActionProvider", "onCreateActionView: this ActionProvider is already associated with a menu item. Don't reuse MediaRouteActionProvider instances! Abandoning the old menu item...");
        }
        MediaRouteButton onCreateMediaRouteButton = onCreateMediaRouteButton();
        this.mButton = onCreateMediaRouteButton;
        onCreateMediaRouteButton.setCheatSheetEnabled(true);
        this.mButton.setRouteSelector(this.mSelector);
        this.mButton.setAlwaysVisible(this.mAlwaysVisible);
        this.mButton.setDialogFactory(this.mDialogFactory);
        this.mButton.setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
        return this.mButton;
    }

    public boolean onPerformDefaultAction() {
        MediaRouteButton mediaRouteButton = this.mButton;
        if (mediaRouteButton != null) {
            return mediaRouteButton.showDialog();
        }
        return false;
    }

    public boolean isVisible() {
        return this.mAlwaysVisible || this.mRouter.isRouteAvailable(this.mSelector, 1);
    }

    public void refreshRoute() {
        refreshVisibility();
    }

    public static final class MediaRouterCallback extends MediaRouter.Callback {
        public final WeakReference<MediaRouteActionProvider> mProviderWeak;

        public MediaRouterCallback(MediaRouteActionProvider mediaRouteActionProvider) {
            this.mProviderWeak = new WeakReference<>(mediaRouteActionProvider);
        }

        public void onRouteAdded(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            refreshRoute(mediaRouter);
        }

        public void onRouteRemoved(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            refreshRoute(mediaRouter);
        }

        public void onRouteChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            refreshRoute(mediaRouter);
        }

        public void onProviderAdded(MediaRouter mediaRouter, MediaRouter.ProviderInfo providerInfo) {
            refreshRoute(mediaRouter);
        }

        public void onProviderRemoved(MediaRouter mediaRouter, MediaRouter.ProviderInfo providerInfo) {
            refreshRoute(mediaRouter);
        }

        public void onProviderChanged(MediaRouter mediaRouter, MediaRouter.ProviderInfo providerInfo) {
            refreshRoute(mediaRouter);
        }

        public final void refreshRoute(MediaRouter mediaRouter) {
            MediaRouteActionProvider mediaRouteActionProvider = (MediaRouteActionProvider) this.mProviderWeak.get();
            if (mediaRouteActionProvider != null) {
                mediaRouteActionProvider.refreshRoute();
            } else {
                mediaRouter.removeCallback(this);
            }
        }
    }
}
