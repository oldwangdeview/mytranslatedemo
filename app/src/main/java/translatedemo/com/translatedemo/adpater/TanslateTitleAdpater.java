package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseRecycleAdapter;
import translatedemo.com.translatedemo.bean.ListBean_information;
import translatedemo.com.translatedemo.help.RecycleViewHolder;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/17 0017.
 */

public class TanslateTitleAdpater extends BaseRecycleAdapter<String> {
    TextView translate_btn;
    ListOnclickLister mlister;
    private static Map<Integer,Boolean> mclick = new HashMap<>();
    public TanslateTitleAdpater(Context context, List<String> datas,int clickindex) {
        super(context, datas, R.layout.translatetitle_btn_item);
        for(int i =0;i<datas.size();i++){
            if(i!=clickindex){
                mclick.put(i,false);
            }else{
                mclick.put(i,true);
            }
        }
    }

    @Override
    protected void setData(RecycleViewHolder holder, String s, final int position) {
        translate_btn = holder.getItemView(R.id.translate_btn);

        if(mclick.get(position)){
            translate_btn.setBackgroundResource(R.drawable.tranlate_titlebtn_shape);
            translate_btn.setTextColor(mContext.getResources().getColor(R.color.c_ffffff));
        }else{
            translate_btn.setBackgroundResource(R.drawable.tranlate_titlebtn_shape1);
            translate_btn.setTextColor(mContext.getResources().getColor(R.color.c_999999));
        }
        translate_btn.setText(s);
        translate_btn.setOnClickListener(new View.OnClickListener() {
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

    public void steallclickfalse(){
        for(int i = 0;i<mDatas.size();i++){
            mclick.put(i,false);
        }
    }

}
