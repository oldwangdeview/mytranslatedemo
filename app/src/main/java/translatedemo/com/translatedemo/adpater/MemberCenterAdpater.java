package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.activity.MenberCenterActivity;
import translatedemo.com.translatedemo.bean.MemberListBean;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/13 0013.
 */

public class MemberCenterAdpater extends BaseAdapter {
    Context mContext;
    List<MemberListBean> listdata;
    TextView month;
    TextView money;
    ListOnclickLister lister;
    public MemberCenterAdpater(Context mContext, List<MemberListBean> listdata){
        this.mContext = mContext;
        this.listdata  = listdata;
    }
    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = UIUtils.inflate(mContext, R.layout.membercenter_item);
        }
        month = convertView.findViewById(R.id.mothone);
        money = convertView.findViewById(R.id.money);
        MemberListBean  data = listdata.get(position);
        convertView.findViewById(R.id.botton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lister!=null){
                    lister.onclick(v,position);
                }
            }
        });
        if(!TextUtils.isEmpty(data.amount+"")&&data.amount!=0){
            money.setText("￥"+data.amount);
        }else{
            money.setText("");
        }
        if(!TextUtils.isEmpty(data.month+"")&&data.month!=0){
            month.setText(data.month+mContext.getResources().getString(R.string.membercenter_text_month));
        }else{
            month.setText("");
        }


        return convertView;
    }
    public void setonitemlistclicklister(ListOnclickLister lister){
        this.lister = lister;
    }
}
