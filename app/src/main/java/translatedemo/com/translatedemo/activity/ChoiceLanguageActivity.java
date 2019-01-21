package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.util.PreferencesUtils;

/**
 * Created by Administrator on 2018/12/26 0026.
 */

public class ChoiceLanguageActivity extends BaseActivity{
    @Override
    protected void initView() {
        setContentView(R.layout.activity_choicelanguage);
    }

    @OnClick({R.id.chunaese,R.id.zangwen})
    public void onclick(View view){
        SharedPreferences preferences = getSharedPreferences("language", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
      switch (view.getId()){
          case R.id.chunaese:
              editor.putString("language", "zh");

              break;
          case R.id.zangwen:
              editor.putString("language", "zh-hant");
              break;
      }
        editor.apply();
      if(BaseActivity.getuser()!=null){
          MainActivity.startactivity(this,1);
      }else {
          LoginActivity.startactivity(this);
      }
        finish();
    }

    @Override
    protected void initData() {
        super.initData();
        SharedPreferences preferences = this.getSharedPreferences("language", Context.MODE_PRIVATE);
        String selectedLanguage = preferences.getString("language", "");
        if(!TextUtils.isEmpty(selectedLanguage)){
          if(!TextUtils.isEmpty(PreferencesUtils.getInstance().getString(BaseActivity.LOGINUSER,""))){
              MainActivity.startactivity(this,1);
              finish();
          }else {
              LoginActivity.startactivity(this);
              finish();
          }
        }


    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent( mContext,ChoiceLanguageActivity.class);
        mContext.startActivity(mIntent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateactionbar();
    }

}
