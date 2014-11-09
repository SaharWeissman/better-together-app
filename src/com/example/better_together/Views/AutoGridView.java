package com.example.better_together.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * Created by ssdd on 11/7/14.
 */
public class AutoGridView extends GridView {
    private static final String TAG = AutoGridView.class.getName();
    private int mNumColumnsID;
    private int mNumColumns;
    private int previousFirstVisible;

    public AutoGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public AutoGridView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(attrs);
    }

    public AutoGridView(Context context){
        super(context);
    }

    /**
     * set the mNumColumns according to the given attribute set
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        int count = attrs.getAttributeCount();
        if(count > 0){
            for(int i =0; i < count; i++){
                String name = attrs.getAttributeName(i);
                if(name != null && name.equals("numColumns")){
                    this.mNumColumnsID = attrs.getAttributeResourceValue(i,1);
                    updateColumns();
                    break;
                }
            }
        }
        Log.d(TAG, "num columns set to: " + mNumColumns);
    }

    private void updateColumns() {
        this.mNumColumns = getContext().getResources().getInteger(mNumColumnsID);
    }

    @Override
    public void setNumColumns(int numColumns) {
        this.mNumColumns = numColumns;
        super.setNumColumns(numColumns);
        setSelection(previousFirstVisible);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setHeights();
    }

    private void setHeights() {
        ListAdapter adapter = getAdapter();
        if(adapter != null ){
            for(int i = 0; i < getChildCount(); i+= mNumColumns){
                //determine the max height for this row
                int maxHeight = 0;
                for(int j = 0; j < i + mNumColumns; j++){
                    View view = getChildAt(j);
                    if(view != null && view.getHeight() > maxHeight){
                        maxHeight = view.getHeight();
                    }
                }

                if(maxHeight > 0){
                    for(int j = i; j < i+mNumColumns;j++){
                        View view = getChildAt(j);
                        if(view!=null && view.getHeight() != maxHeight){
                            view.setMinimumHeight(maxHeight);
                        }
                    }
                }
            }
        }
    }
}
