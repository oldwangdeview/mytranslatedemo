package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseRecycleAdapter;
import translatedemo.com.translatedemo.bean.GetCouponListBean;
import translatedemo.com.translatedemo.bean.NoticeListBean;
import translatedemo.com.translatedemo.help.RecycleViewHolder;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/14 0014.
 */

public class NoticeAdpater extends BaseRecycleAdapter<NoticeListBean> {


    TextView title;
    TextView time;
    ListOnclickLister mlister;
    public NoticeAdpater(Context context, List<NoticeListBean> datas) {
        super(context, datas, R.layout.notice_item);
    }

    @Override
    protected void setData(RecycleViewHolder holder, NoticeListBean s, final int position) {


        title = holder.getItemView(R.id.title);
        time = holder.getItemView(R.id.time);

        if(!TextUtils.isEmpty(s.field)){
            title.setText(s.field);
        }
        if(!TextUtils.isEmpty(s.createTime)){
            time.setText(UIUtils.gettime(s.createTime));
        }



        holder.getView()
                .setOnClickListener(new View.OnClickListener() {
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
