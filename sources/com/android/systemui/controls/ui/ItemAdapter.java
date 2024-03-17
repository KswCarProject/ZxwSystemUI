package com.android.systemui.controls.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$id;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlsUiControllerImpl.kt */
public final class ItemAdapter extends ArrayAdapter<SelectionItem> {
    public final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
    @NotNull
    public final Context parentContext;
    public final int resource;

    public ItemAdapter(@NotNull Context context, int i) {
        super(context, i);
        this.parentContext = context;
        this.resource = i;
    }

    @NotNull
    public View getView(int i, @Nullable View view, @NotNull ViewGroup viewGroup) {
        SelectionItem selectionItem = (SelectionItem) getItem(i);
        if (view == null) {
            view = this.layoutInflater.inflate(this.resource, viewGroup, false);
        }
        ((TextView) view.requireViewById(R$id.controls_spinner_item)).setText(selectionItem.getTitle());
        ((ImageView) view.requireViewById(R$id.app_icon)).setImageDrawable(selectionItem.getIcon());
        return view;
    }
}
