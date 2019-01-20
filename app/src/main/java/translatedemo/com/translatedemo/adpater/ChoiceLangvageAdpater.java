package translatedemo.com.translatedemo.adpater;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.OnClick;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/18 0018.
 */

public class ChoiceLangvageAdpater extends BaseAdapter {
    String[]data;
    Context mContext;
    TextView name;
    public ChoiceLangvageAdpater(Context mContext,String[] listdat){
        this.mContext = mContext;
        this.data = listdat;
    }
    @Override
    public int getCount() {
        return data.length;
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
        name.setText(data[position]);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mlister!=null){
                        mlister.onclick(v,position);
                    }
                }
            });


        return convertView;
    }
    ListOnclickLister mlister;
    public void steonitemclicklister(ListOnclickLister mlister){
        this.mlister = mlister;
    }
}
