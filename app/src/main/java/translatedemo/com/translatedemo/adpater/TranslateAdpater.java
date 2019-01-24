package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseRecycleAdapter;
import translatedemo.com.translatedemo.bean.ListBean_information;
import translatedemo.com.translatedemo.help.RecycleViewHolder;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/4 0004.
 */

public class TranslateAdpater extends BaseRecycleAdapter<ListBean_information>{
    ImageView image;
    TextView title;
    TextView User;
    TextView time;
    LinearLayout informatin_ll;
    ImageView image_adv;
    ListOnclickLister mlister;
    public TranslateAdpater(Context context, List<ListBean_information> datas) {
        super(context, datas, R.layout.item_translate);
    }

    @Override
    protected void setData(RecycleViewHolder holder, ListBean_information s, final int position) {

        image = holder.getItemView(R.id.image);
        title = holder.getItemView(R.id.title);
        User = holder.getItemView(R.id.user);
        time = holder.getItemView(R.id.time);
        informatin_ll = holder.getItemView(R.id.informatin_ll);
        image_adv = holder.getItemView(R.id.image_adv);

        if (s.type == 0){
            informatin_ll.setVisibility(View.VISIBLE);
            image_adv.setVisibility(View.GONE);

        }else {
            informatin_ll.setVisibility(View.GONE);
            image_adv.setVisibility(View.VISIBLE);
            UIUtils.loadImageView(mContext,s.image,image_adv);
        }


        if(!TextUtils.isEmpty(s.image)){
            new LogUntil(mContext,"imageoarh",s.image);
            image.setVisibility(View.VISIBLE);
            UIUtils.loadImageView(mContext,s.image,image);
        }else {
            image.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(s.title)){
            title.setText(s.title);
        }else{
            title.setText("");
        }

        if(!TextUtils.isEmpty(s.author)){
            User.setText(s.author);
        }else{
            User.setText("");
        }

        if(!TextUtils.isEmpty(s.createTime)){
            time.setText(UIUtils.gettime(s.createTime));
        }else{
            time.setText("");
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
