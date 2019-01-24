package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;

/**
 * Created by oldwang on 2019/1/24 0024.
 */

public class PeopleTranslateActivity extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_peopletranslate);

    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.pople_translate_text_titlenam));
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }

    public static void startactivity(Context mContext){
        Intent mintent = new Intent(mContext,PeopleTranslateActivity.class);
        mContext.startActivity(mintent);
    }
}
