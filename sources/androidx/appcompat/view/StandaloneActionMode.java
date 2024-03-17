package androidx.appcompat.view;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.ActionBarContextView;
import java.lang.ref.WeakReference;

public class StandaloneActionMode extends ActionMode implements MenuBuilder.Callback {
    public ActionMode.Callback mCallback;
    public Context mContext;
    public ActionBarContextView mContextView;
    public WeakReference<View> mCustomView;
    public boolean mFinished;
    public boolean mFocusable;
    public MenuBuilder mMenu;

    public StandaloneActionMode(Context context, ActionBarContextView actionBarContextView, ActionMode.Callback callback, boolean z) {
        this.mContext = context;
        this.mContextView = actionBarContextView;
        this.mCallback = callback;
        MenuBuilder defaultShowAsAction = new MenuBuilder(actionBarContextView.getContext()).setDefaultShowAsAction(1);
        this.mMenu = defaultShowAsAction;
        defaultShowAsAction.setCallback(this);
        this.mFocusable = z;
    }

    public void setTitle(CharSequence charSequence) {
        this.mContextView.setTitle(charSequence);
    }

    public void setSubtitle(CharSequence charSequence) {
        this.mContextView.setSubtitle(charSequence);
    }

    public void setTitle(int i) {
        setTitle((CharSequence) this.mContext.getString(i));
    }

    public void setSubtitle(int i) {
        setSubtitle((CharSequence) this.mContext.getString(i));
    }

    public void setTitleOptionalHint(boolean z) {
        super.setTitleOptionalHint(z);
        this.mContextView.setTitleOptional(z);
    }

    public boolean isTitleOptional() {
        return this.mContextView.isTitleOptional();
    }

    public void setCustomView(View view) {
        this.mContextView.setCustomView(view);
        this.mCustomView = view != null ? new WeakReference<>(view) : null;
    }

    public void invalidate() {
        this.mCallback.onPrepareActionMode(this, this.mMenu);
    }

    public void finish() {
        if (!this.mFinished) {
            this.mFinished = true;
            this.mCallback.onDestroyActionMode(this);
        }
    }

    public Menu getMenu() {
        return this.mMenu;
    }

    public CharSequence getTitle() {
        return this.mContextView.getTitle();
    }

    public CharSequence getSubtitle() {
        return this.mContextView.getSubtitle();
    }

    public View getCustomView() {
        WeakReference<View> weakReference = this.mCustomView;
        if (weakReference != null) {
            return (View) weakReference.get();
        }
        return null;
    }

    public MenuInflater getMenuInflater() {
        return new SupportMenuInflater(this.mContextView.getContext());
    }

    public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        return this.mCallback.onActionItemClicked(this, menuItem);
    }

    public void onMenuModeChange(MenuBuilder menuBuilder) {
        invalidate();
        this.mContextView.showOverflowMenu();
    }
}
