package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.DucationBean;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/16 0016.
 */

public class DucationAdpater extends BaseAdapter {
    TextView name;
    View view;
    private Context mContext;
    List<DucationBean> mlistdata = new ArrayList<>();
    private ListOnclickLister monclicklister;
    public DucationAdpater(Context mContext,List<DucationBean> mlistdata){
        this.mContext = mContext;
        this.mlistdata = mlistdata;
    }
    @Override
    public int getCount() {
        return mlistdata.size();
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
            convertView = UIUtils.inflate(mContext, R.layout.item_ducation);

        }
        name = convertView.findViewById(R.id.name);
        view = convertView.findViewById(R.id.view);
        if(!TextUtils.isEmpty(mlistdata.get(position).name))
        {
            name.setText(mlistdata.get(position).name);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monclicklister!=null){
                    monclicklister.onclick(v,position);
                }
            }
        });
        if(position==mlistdata.size()-1){
            view.setVisibility(View.GONE);
        }else{
            view.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    public void setOnclickItemLister(ListOnclickLister lister){
        this.monclicklister = lister;
    }
}
