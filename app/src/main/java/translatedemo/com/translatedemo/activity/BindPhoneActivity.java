package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/4 0004.
 */

public class BindPhoneActivity extends BaseActivity {
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.bindphone_inputeditext_phone)
    EditText bindphone_inputeditext_phone;//手机号
    @BindView(R.id.bindphone_inoputeditext_code)
    EditText bindphone_inoputeditext_code;//验证码


    @Override
    protected void initView() {
        setContentView(R.layout.activity_bindphone);
    }

    @Override
    protected void initData() {
        super.initData();
        title_name.setText(getResources().getString(R.string.bindphone_text_titlename));
        iv_back_activity_text.setVisibility(View.VISIBLE);
    }
    @OnClick({R.id.iv_back_activity_basepersoninfo,R.id.iv_back_activity_text})
    public void overActivity(){
        MainActivity.startactivity(this,0);
        finish();
    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.bindphone_text_getcode)
    public void getcode(){

    }


    /**
     * 绑定手机号
     */
    @OnClick(R.id.bindphone_btn_confirm)
    public void bandphone(){
        String phone = bindphone_inputeditext_phone.getText().toString().trim();
        String code = bindphone_inoputeditext_code.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.makeText(getResources().getString(R.string.login_error_notphonenumber));
            return;
        }
        if(!UIUtils.isPhoneNumber(phone)){
            ToastUtils.makeText(getResources().getString(R.string.login_error_isnotphone));
            return;
        }
        if(TextUtils.isEmpty(code)){
            ToastUtils.makeText(getResources().getString(R.string.login_error_notcode));
            return;
        }
        MainActivity.startactivity(this,0);

    }

    public static void statrtactivity(Context mContext){
        Intent mintent = new Intent(mContext,BindPhoneActivity.class);
        mContext.startActivity(mintent);
    }
}
