package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseRecycleAdapter;
import translatedemo.com.translatedemo.bean.DictionaryBean;
import translatedemo.com.translatedemo.bean.Helpcenter_ListBean;
import translatedemo.com.translatedemo.help.RecycleViewHolder;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;

/**
 * Created by oldwang on 2019/1/20 0020.
 */

public class HelpcenterAdpater extends BaseRecycleAdapter<Helpcenter_ListBean> {

    TextView content;
    View view;
    ListOnclickLister mlister;
    public HelpcenterAdpater(Context context, List<Helpcenter_ListBean> datas) {
        super(context, datas, R.layout.item_helpitem);

    }

    @Override
    protected void setData(RecycleViewHolder holder, Helpcenter_ListBean s, final int position) {

        content = holder.getItemView(R.id.content);
        view = holder.getItemView(R.id.view);
        if(!TextUtils.isEmpty(s.field)){
            content.setText(s.field);
        }
        if(position==mDatas.size()-1){
            view.setVisibility(View.GONE);
        }else{
            view.setVisibility(View.VISIBLE);
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
