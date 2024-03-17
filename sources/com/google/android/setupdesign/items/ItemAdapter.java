package com.google.android.setupdesign.items;

import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.google.android.setupdesign.items.ItemHierarchy;

public class ItemAdapter extends BaseAdapter implements ItemHierarchy.Observer {
    public final ItemHierarchy itemHierarchy;
    public final ViewTypes viewTypes = new ViewTypes();

    public long getItemId(int i) {
        return (long) i;
    }

    public ItemAdapter(ItemHierarchy itemHierarchy2) {
        this.itemHierarchy = itemHierarchy2;
        itemHierarchy2.registerObserver(this);
        refreshViewTypes();
    }

    public int getCount() {
        return this.itemHierarchy.getCount();
    }

    public IItem getItem(int i) {
        return this.itemHierarchy.getItemAt(i);
    }

    public int getItemViewType(int i) {
        return this.viewTypes.get(getItem(i).getLayoutResource());
    }

    public int getViewTypeCount() {
        return this.viewTypes.size();
    }

    public final void refreshViewTypes() {
        for (int i = 0; i < getCount(); i++) {
            this.viewTypes.add(getItem(i).getLayoutResource());
        }
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        IItem item = getItem(i);
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(item.getLayoutResource(), viewGroup, false);
        }
        item.onBindView(view);
        return view;
    }

    public void onChanged(ItemHierarchy itemHierarchy2) {
        refreshViewTypes();
        notifyDataSetChanged();
    }

    public void onItemRangeChanged(ItemHierarchy itemHierarchy2, int i, int i2) {
        onChanged(itemHierarchy2);
    }

    public void onItemRangeInserted(ItemHierarchy itemHierarchy2, int i, int i2) {
        onChanged(itemHierarchy2);
    }

    public boolean isEnabled(int i) {
        return getItem(i).isEnabled();
    }

    public static class ViewTypes {
        public int nextPosition;
        public final SparseIntArray positionMap;

        public ViewTypes() {
            this.positionMap = new SparseIntArray();
            this.nextPosition = 0;
        }

        public int add(int i) {
            if (this.positionMap.indexOfKey(i) < 0) {
                this.positionMap.put(i, this.nextPosition);
                this.nextPosition++;
            }
            return this.positionMap.get(i);
        }

        public int size() {
            return this.positionMap.size();
        }

        public int get(int i) {
            return this.positionMap.get(i);
        }
    }
}
