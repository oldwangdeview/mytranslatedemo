package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.eventbus.OverMainactivty;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.UIUtils;
import translatedemo.com.translatedemo.view.TipsDialog;

/**
 * Created by oldwang on 2019/1/11 0011.
 */

public class SetingActivty extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_seting);
    }
    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(SetingActivty.this,false);
        updateactionbar();
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.seting_text_titlename));
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,SetingActivty.class);
        mContext.startActivity(mIntent);
    }

    /**
     * 切换语言
     */
    @OnClick(R.id.seting_layout_choicelanguage)
    public void gotoChoicelanguage(){
        SharedPreferences preferences = getSharedPreferences("language", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", "");
        editor.apply();
        EventBus.getDefault().post(new OverMainactivty());
        ChoiceLanguageActivity.startactivity(this);
//        finish();
    }

    /**
     * 安全中心
     */
    @OnClick(R.id.seting_layout_safecenter)
    public void gotoSafetyCenterActivty(){
        SafetyCenterActivity.startactivity(this);
    }

    /**
     * 关于我们
     */
    @OnClick(R.id.seting_layout_aboutus)
    public void gotoAboutusActivty(){
        AboutUsActivty.startactivity(this);
    }


    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }
    TipsDialog mdialog;
    @OnClick(R.id.rejest_btn)
    public void loginout(){
        if(mdialog==null) {
            mdialog = new TipsDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.help))
                    .setContentView(getResources().getString(R.string.dialog_message))
                    .setLeftButton(getResources().getString(R.string.translate_text_quxiao), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mdialog.dismiss();
                        }
                    })
                    .setRightButton(getResources().getString(R.string.translate_text_qued), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseActivity.user=null;
                            PreferencesUtils.getInstance().putString(BaseActivity.LOGINUSER,"");
                            EventBus.getDefault().post(new OverMainactivty());
                            LoginActivity.startactivity(SetingActivty.this);
                            finish();
                        }
                    })
                    .build();
        }
        mdialog.show();

    }




}