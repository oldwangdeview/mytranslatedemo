package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.wcy.htmltext.HtmlImageLoader;
import me.wcy.htmltext.HtmlText;
import me.wcy.htmltext.OnTagClickListener;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.bean.Helpcenter_ListBean;
import translatedemo.com.translatedemo.contans.Contans;

/**
 * Created by oldwang on 2019/1/20 0020.
 */

public class HelpDetailActivity extends BaseActivity {
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.title_text_data)
    TextView title;
    private Helpcenter_ListBean mdata;
    @Override
    protected void initView() {
       setContentView(R.layout.activity_helpdetail);
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        title_name.setText(this.getResources().getString(R.string.helpcenter_text_titlename));
        mdata = (Helpcenter_ListBean)getIntent().getSerializableExtra(Contans.INTENT_DATA);
        if(!TextUtils.isEmpty(mdata.field)){
            title.setText(mdata.field);
        }
        if(!TextUtils.isEmpty(mdata.content)){

            HtmlText.from(mdata.content)
                    .setImageLoader(new HtmlImageLoader() {
                        @Override
                        public void loadImage(String url, final Callback callback) {
                            // Glide sample, you can also use other image loader
                            SimpleTarget<Bitmap> into = Glide.with(HelpDetailActivity.this)
                                    .load(url)
                                    .asBitmap()
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource,
                                                                    GlideAnimation<? super Bitmap> glideAnimation) {
                                            callback.onLoadComplete(resource);
                                        }

                                        @Override
                                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                            callback.onLoadFailed();
                                        }
                                    });
                        }

                        @Override
                        public Drawable getDefaultDrawable() {
                            return ContextCompat.getDrawable(HelpDetailActivity.this, R.mipmap.buffer);
                        }

                        @Override
                        public Drawable getErrorDrawable() {
                            return ContextCompat.getDrawable(HelpDetailActivity.this, R.mipmap.buffer);
                        }

                        @Override
                        public int getMaxWidth() {
                            return content.getMaxWidth();
                        }

                        @Override
                        public boolean fitWidth() {
                            return false;
                        }
                    })
                    .setOnTagClickListener(new OnTagClickListener() {
                        @Override
                        public void onImageClick(Context context, List<String> imageUrlList, int position) {
                            // image click
                        }

                        @Override
                        public void onLinkClick(Context context, String url) {
                            // link click
                        }
                    })
                    .into(content);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }

    public static void startactivty(Context mContext, Helpcenter_ListBean mdata){
        Intent mintent = new Intent(mContext,HelpDetailActivity.class);
        mintent.putExtra(Contans.INTENT_DATA,(Serializable) mdata);
        mContext.startActivity(mintent);
    }

}
