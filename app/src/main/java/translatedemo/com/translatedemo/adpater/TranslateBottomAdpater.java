package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseRecycleAdapter;
import translatedemo.com.translatedemo.bean.DictionaryBean;
import translatedemo.com.translatedemo.help.RecycleViewHolder;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;

/**
 * Created by oldwang on 2019/1/17 0017.
 */

public class TranslateBottomAdpater extends BaseRecycleAdapter<DictionaryBean> {
    TextView translate_btn;
    ListOnclickLister mlister;

    private static Map<Integer,Boolean> mclick = new HashMap<>();
    public TranslateBottomAdpater(Context context, List<DictionaryBean> datas) {
        super(context, datas, R.layout.translatetitle_btn_item);

    }

    @Override
    protected void setData(RecycleViewHolder holder, DictionaryBean s, final int position) {
        translate_btn = holder.getItemView(R.id.translate_btn);

        if(mclick.get(position)){
            translate_btn.setBackgroundResource(R.drawable.translate_bottom_btn);
            translate_btn.setTextColor(mContext.getResources().getColor(R.color.c_e94950));
        }else{
            translate_btn.setBackgroundResource(R.drawable.translate_bottom_btn1);
            translate_btn.setTextColor(mContext.getResources().getColor(R.color.c_999999));
        }
        if(s.isMemberVisible==1){
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.vip);
            translate_btn.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
        }else{

            translate_btn.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }
        translate_btn.setText(s.name+" ");
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                steallclickfalse();
                mclick.put(position,!mclick.get(position));
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
                notifyDataSetChanged();
            }
        });

    }

    public void setlistOnclickLister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

    public void updatecliclk(){
        for(int i =0;i<mDatas.size();i++){
            if(i!=0){
                mclick.put(i,false);
            }else{
                mclick.put(i,false);
            }
        }
    }
    public void steallclickfalse(){
        for(int i = 0;i<mDatas.size();i++){
            mclick.put(i,false);
        }
    }

}
