package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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

public class GetCouponAdpater extends BaseRecycleAdapter<GetCouponListBean> {

    TextView title;
    TextView zf;
    TextView moeny;
    TextView tj;
    TextView rq;
    Button btn;

    ListOnclickLister mlister;
    public GetCouponAdpater(Context context, List<GetCouponListBean> datas) {
        super(context, datas, R.layout.getcoupon_item);
    }

    @Override
    protected void setData(RecycleViewHolder holder, GetCouponListBean s, final int position) {

        title = holder.getItemView(R.id.title);
        zf = holder.getItemView(R.id.zf);
        moeny = holder.getItemView(R.id.money);
        tj = holder.getItemView(R.id.tiaojian);
        rq = holder.getItemView(R.id.time);
        btn = holder.getItemView(R.id.btn);
        if(!TextUtils.isEmpty(s.name)){
            title.setText(s.name);
        }
        if(s.isReceive==0){
            zf.setTextColor(mContext.getResources().getColor(R.color.c_f84752));
            moeny.setTextColor(mContext.getResources().getColor(R.color.c_f84752));
            title.setTextColor(mContext.getResources().getColor(R.color.c_000000));
            btn.setBackgroundResource(R.drawable.getcoupom_btn_shape1);
            btn.setText(mContext.getResources().getString(R.string.getcoupon_text_get));
        }else{
            zf.setTextColor(mContext.getResources().getColor(R.color.c_ff808080));
            moeny.setTextColor(mContext.getResources().getColor(R.color.c_ff808080));
            title.setTextColor(mContext.getResources().getColor(R.color.c_ff808080));
            btn.setBackgroundResource(R.drawable.getcoupom_btn_shape2);
            btn.setText(mContext.getResources().getString(R.string.getcoupon_text_getover));
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


        holder.getItemView(R.id.btn)
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
