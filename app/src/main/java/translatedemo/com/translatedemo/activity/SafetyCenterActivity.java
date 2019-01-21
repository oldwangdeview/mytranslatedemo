package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.eventbus.UpdateUserEvent;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/11 0011.
 * 安全中心
 */

public class SafetyCenterActivity extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.phonenum)
    TextView phonenum;
    @BindView(R.id.bangdingqq)
    TextView bangdingqq;
    @BindView(R.id.bangdingwechart)
    TextView bangdingwechat;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_safecenter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(SafetyCenterActivity.this,false);
        updateactionbar();
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.safecenter_text_titlename));
        getuserdata();
    }
    private void getuserdata(){
        LoginBean user = BaseActivity.getuser();
        if(!TextUtils.isEmpty(user.phone)){
            phonenum.setText(UIUtils.getphone(user.phone));
        }else{
            phonenum.setText(mcontent.getResources().getString(R.string.userinfo_text_nobading));
        }
        if(!TextUtils.isEmpty(user.qqOpenid)){
            bangdingqq.setText("已绑定");
            bangdingqq.setTextColor(mcontent.getResources().getColor(R.color.c_6495ED));
//  c_6495ED
        }else{
            bangdingqq.setText("未绑定");
            bangdingqq.setTextColor(mcontent.getResources().getColor(R.color.c_999999));
        }
        if(!TextUtils.isEmpty(user.weixinOpenid)){
            bangdingwechat.setText("已绑定");
            bangdingwechat.setTextColor(mcontent.getResources().getColor(R.color.c_6495ED));
        }else{
            bangdingwechat.setText("未绑定");
            bangdingwechat.setTextColor(mcontent.getResources().getColor(R.color.c_999999));

        }

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uodateuser( UpdateUserEvent uodate){
        getuserdata();
    }


    @OnClick(R.id.replace_phone_layout)
    public void gotoreplacephone(){
        ReplacePhoneActivity.startactivity(this);
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,SafetyCenterActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
