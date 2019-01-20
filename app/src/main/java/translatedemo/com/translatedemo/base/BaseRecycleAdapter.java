package translatedemo.com.translatedemo.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import translatedemo.com.translatedemo.help.RecycleViewHolder;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Author yichao
 * Time  2017/12/8 11:57
 * Dest
 */
public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<RecycleViewHolder> {
  protected Context mContext;
  protected List<T> mDatas;
  protected int mLayoutId;

  public BaseRecycleAdapter(Context context, List<T> datas,int layoutId) {
    mContext = context;
    mDatas = datas;
    mLayoutId=layoutId;
  }

  @Override
  public int getItemViewType(int position) {
    return super.getItemViewType(position);
  }

  @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View inflate = UIUtils.inflate(mContext, mLayoutId, parent, false);

      return new RecycleViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {
        setData(holder,mDatas.get(position),position);
    }

    /**
     * 设置数据
     * @param holder
     * @param t
     * @param position
     */
    protected abstract void setData(RecycleViewHolder holder, T t, int position) ;

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
/**获取数据*/
  public List<T> getDatas() {
    return mDatas;
  }
}
