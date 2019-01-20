package translatedemo.com.translatedemo.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import translatedemo.com.translatedemo.R;


/**
 * Author wangshif
 * Time  2017/12/11 17:14
 * Dest  个人信息基类
 */

public abstract class BasePersonActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout mFrameLayout;
    protected TextView mTvSmalTitle,mTvTitle;
    protected ImageView mIvBack;
    protected View mViewline;
    protected RelativeLayout mRlHeadRoot;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_baseperson);
        mFrameLayout = findViewById(R.id.fl_content_activity_baseperson);
        View mContentView=getContentView(this);
        mFrameLayout.addView(mContentView);
        mTvSmalTitle = findViewById(R.id.tv_small_title_layout_head);
        mTvTitle = findViewById(R.id.tv_title_activity_baseperson);
        mIvBack = findViewById(R.id.iv_back_activity_basepersoninfo);
        mViewline = findViewById(R.id.view_activity_baseperson);

    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mIvBack.setOnClickListener(this);
        mTvSmalTitle.setOnClickListener(this);
    }

    protected abstract View getContentView(Context context) ;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_activity_basepersoninfo:
                returnMainActivity(v);
                finish();
                break;
            case R.id.tv_small_title_layout_head:
                save(v);
                break;
        }
    }

    /**
     * 返回主页
     * @param v
     */
    protected  void returnMainActivity(View v){};

    /**
     * 保存
     * @param v
     */
    protected void save(View v) {
    }
}
