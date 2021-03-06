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
import translatedemo.com.translatedemo.help.RecycleViewHolder;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/14 0014.
 */

public class MyCuponAdpater  extends BaseRecycleAdapter<GetCouponListBean> {

    TextView title;
    TextView zf;
    TextView moeny;
    TextView tj;
    TextView rq;
    private int type = 0;

    ListOnclickLister mlister;
    public MyCuponAdpater(Context context, List<GetCouponListBean> datas,int type) {
        super(context, datas, R.layout.mycupon_item);
        this.type = type;
    }

    @Override
    protected void setData(RecycleViewHolder holder, GetCouponListBean s, final int position) {

        title = holder.getItemView(R.id.title);
        zf = holder.getItemView(R.id.zf);
        moeny = holder.getItemView(R.id.money);
        tj = holder.getItemView(R.id.tiaojian);
        rq = holder.getItemView(R.id.time);

        if(!TextUtils.isEmpty(s.name)){
            title.setText(s.name);
        }
        if(type==0){
            zf.setTextColor(mContext.getResources().getColor(R.color.c_f84752));
            moeny.setTextColor(mContext.getResources().getColor(R.color.c_f84752));
            title.setTextColor(mContext.getResources().getColor(R.color.c_000000));

        }else{
            zf.setTextColor(mContext.getResources().getColor(R.color.c_ff808080));
            moeny.setTextColor(mContext.getResources().getColor(R.color.c_ff808080));
            title.setTextColor(mContext.getResources().getColor(R.color.c_ff808080));

        }
        if(!TextUtils.isEmpty(s.reducePrice+"")){
            moeny.setText(s.reducePrice+"");
        }
        if(!TextUtils.isEmpty(s.fullPrice+"")){
            String text = mContext.getResources().getString(R.string.getcoupon_text_tj);
            text = text.replace("%",s.fullPrice+"");
            tj.setText(text);
        }
        if(!TextUtils.isEmpty(s.endTime)){
            rq.setText(UIUtils.gettime(s.endTime));
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