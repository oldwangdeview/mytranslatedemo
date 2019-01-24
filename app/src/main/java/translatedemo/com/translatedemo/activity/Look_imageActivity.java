package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2019/1/24 0024.
 */

public class Look_imageActivity extends BaseActivity {

    @BindView(R.id.image)
    ImageView imageView;
    @Override
    protected void initView() {
     setContentView(R.layout.activity_look_image);
    }

    @Override
    protected void initData() {
        super.initData();
        String imagepath = getIntent().getStringExtra(Contans.INTENT_DATA);
        if(!TextUtils.isEmpty(imagepath)) {
            UIUtils.loadImageView(mcontent, imagepath, imageView);
        }else{
            finish();
        }
    }

    @OnClick(R.id.image)
    public void finish(){
        finish();
    }
    public static void startactivity(Context mContext,String imagepath){
        Intent mintent = new Intent(mContext,Look_imageActivity.class);
        mintent.putExtra(Contans.INTENT_DATA,imagepath);
        mContext.startActivity(mintent);
    }
}
