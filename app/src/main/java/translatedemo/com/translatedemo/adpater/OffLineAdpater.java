package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseRecycleAdapter;
import translatedemo.com.translatedemo.bean.DictionaryBean;
import translatedemo.com.translatedemo.bean.GetCouponListBean;
import translatedemo.com.translatedemo.help.RecycleViewHolder;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/16 0016.
 */

public class OffLineAdpater extends BaseRecycleAdapter<DictionaryBean> {


    TextView textView;
    Button btn;
    ListOnclickLister mlister;
    public OffLineAdpater(Context context, List<DictionaryBean> datas) {
        super(context, datas, R.layout.offline_item);
    }

    @Override
    protected void setData(RecycleViewHolder holder, DictionaryBean s, final int position) {

        textView = holder.getItemView(R.id.text);
        btn= holder.getItemView(R.id.btn);
        textView .setText(s.name);
        if(s.islode){
            btn.setTextColor(mContext.getResources().getColor(R.color.c_999999));
             btn.setBackgroundResource(R.drawable.offlineitem_btn_shape1);
        }else{
            btn.setTextColor(mContext.getResources().getColor(R.color.c_ffffff));
            btn.setBackgroundResource(R.drawable.offlineitem_btn_shape);
            holder.getItemView(R.id.btn)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (mlister != null) {
                                mlister.onclick(v, position);
                            }

                        }
                    });
        }



    }

    public void setlistOnclickLister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

}
