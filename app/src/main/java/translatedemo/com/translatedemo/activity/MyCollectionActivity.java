package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.util.UIUtils;
import translatedemo.com.translatedemo.view.YRecycleview;

/**
 * Created by oldwang on 2019/1/7 0007.
 * 我的收藏
 */

public class MyCollectionActivity  extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.yrecycleview_)
    YRecycleview yrecycleview_;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_mycollection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(MyCollectionActivity.this,false);
        updateactionbar();
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.mycollection_text_titlename));
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,MyCollectionActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }
}
