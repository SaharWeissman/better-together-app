package com.example.better_together.Views;

import com.example.better_together.Views.models.ViewItem;

/**
 * Created by ssdd on 10/27/14.
 */
public interface IViewItemClickListener {
    public boolean onListItemLongClick(ViewItem item);
    public void onListItemClick(ViewItem item);
}
