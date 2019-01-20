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

/**
 * Created by oldwang on 2019/1/11 0011.
 */

public class AboutUsActivty  extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_aboutus);
    }
    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(AboutUsActivty.this,false);
        updateactionbar();
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.myuserinfo_text_titlename));
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,AboutUsActivty.class);
        mContext.startActivity(mIntent);
    }
    @OnClick(R.id.iv_back_activity_text)
    public void finishactivity(){
        finish();
    }
}
