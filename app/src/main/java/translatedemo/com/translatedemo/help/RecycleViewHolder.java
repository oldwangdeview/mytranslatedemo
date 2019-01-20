package translatedemo.com.translatedemo.help;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Author yichao
 * Time  2018/1/15 14:22
 * Dest  ${TODO}
 */

public class RecycleViewHolder extends RecyclerView.ViewHolder {
    public View mItemView;
    private SparseArray<View> mViews;

    public RecycleViewHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<View>();
        mItemView = itemView;
    }
    //通过控件的id获取对应的控件，如果没有则加入mViews;记住 <T extends View> T 这种用法
    public <T extends View> T getItemView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }
    public View getView(){
        return mItemView;
    }
}
