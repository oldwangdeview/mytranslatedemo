package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseRecycleAdapter;
import translatedemo.com.translatedemo.bean.CollectionListbean;
import translatedemo.com.translatedemo.bean.ListBean_information;
import translatedemo.com.translatedemo.help.RecycleViewHolder;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/23 0023.
 */

public class CollectionAdpater  extends BaseRecycleAdapter<CollectionListbean> {
    TextView content;
    View view;
    ListOnclickLister mlister;
    public CollectionAdpater(Context context, List<CollectionListbean> datas) {
        super(context, datas, R.layout.collection_item);
    }

    @Override
    protected void setData(RecycleViewHolder holder, CollectionListbean s, final int position) {

        content = holder.getItemView(R.id.content);
        view = holder.getItemView(R.id.view);

        content.setText(s.content);
        if(position!=mDatas.size()-1){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

    }

    public void setlistOnclickLister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

}
