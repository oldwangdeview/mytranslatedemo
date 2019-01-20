package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;

import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/6 0006.
 */

public class TranslateActivity extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.activity_translate);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(TranslateActivity.this,false);
        updateactionbar();
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,TranslateActivity.class);
        mContext.startActivity(mIntent);

    }


}
