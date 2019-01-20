package translatedemo.com.translatedemo.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.activity.FeedBackActivity;
import translatedemo.com.translatedemo.activity.HelpCenterActivity;
import translatedemo.com.translatedemo.activity.MenberCenterActivity;
import translatedemo.com.translatedemo.activity.MyCollectionActivity;
import translatedemo.com.translatedemo.activity.MycouponActivity;
import translatedemo.com.translatedemo.activity.NoticeActivity;
import translatedemo.com.translatedemo.activity.OffLineActivity;
import translatedemo.com.translatedemo.activity.SetingActivty;
import translatedemo.com.translatedemo.activity.UserinfoActivity;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.base.BaseFragment;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.eventbus.UpdateUserEvent;
import translatedemo.com.translatedemo.util.UIUtils;

/**
 * Created by oldwang on 2018/12/30 0030.
 */

public class UserinfoFragment extends BaseFragment {
    @BindView(R.id.headimaege)
    ImageView headimaege;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.sex_image)
    ImageView sex_image;
    @BindView(R.id.vip_image)
    ImageView vip_iamge;


    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_user);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        updateUserdata();
    }

    private void updateUserdata(){
        LoginBean user = BaseActivity.getuser();
        if(!TextUtils.isEmpty(user.headBigImage)){
            UIUtils.loadImageViewRoud(mContext,user.headBigImage,headimaege,UIUtils.dip2px(40));
        }
        if(!TextUtils.isEmpty(user.headSmallImage)){
            UIUtils.loadImageViewRoud(mContext,user.headBigImage,headimaege,UIUtils.dip2px(40));
        }
        if(!TextUtils.isEmpty(user.nickName)){
            name.setText(user.nickName);
        }else{
            name.setText(mContext.getResources().getString(R.string.userinfo_text_noadd));
        }
        if(!TextUtils.isEmpty(user.phone)){
            phone.setText(UIUtils.getphone(user.phone));
        }else{
            phone.setText(mContext.getResources().getString(R.string.userinfo_text_nobading));
        }

        if(!TextUtils.isEmpty(user.sex)){
            switch (Integer.parseInt(user.sex)){
                case 1:
                    sex_image.setVisibility(View.VISIBLE);
                    sex_image.setImageResource(R.mipmap.nan);
                    break;
                case 2:
                    sex_image.setVisibility(View.VISIBLE);
                    sex_image.setImageResource(R.mipmap.nv);
                    break;
                default:
                    sex_image.setVisibility(View.GONE);
                        break;

            }
        }else{
            sex_image.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(user.isMember+"")&&user.isMember>=0){
            switch (user.isMember){
                case 0:
                    vip_iamge.setImageResource(R.mipmap.huiyuan1);
                    break;
                case 1:
                    vip_iamge.setImageResource(R.mipmap.huiyuan);
                    break;
                    default:
                        vip_iamge.setVisibility(View.GONE);
                        break;
            }
        }else{
            vip_iamge.setVisibility(View.GONE);
        }


    }


    /**
     * 编辑个人资料
     */
    @OnClick(R.id.userinfo_lin_usemessage)
    public void gotoUserinfo(){
        UserinfoActivity.startactivity(mContext);
    }

    /**
     * 会员中心
     */
    @OnClick(R.id.vipcenter)
    public void gotovipcenter(){
        MenberCenterActivity.startactivity(mContext);
    }
    /**
     * 我的收藏
     */
    @OnClick(R.id.mycollection)
    public void gotomycollection(){
        MyCollectionActivity.startactivity(mContext);
    }

    /**
     * 优惠券
     */
    @OnClick(R.id.mycoupon)
    public void gotoMycouPon(){
        MycouponActivity.startactivity(mContext);
    }

    /**
     * 离线数据
     */
    @OnClick(R.id.offline_linearlayout)
    public void gotoOffLine(){
        OffLineActivity.startactivity(mContext);
    }
    /**
     * 通知公告
     */
    @OnClick(R.id.notice_linearlayout)
    public void gotoNotice(){
        NoticeActivity.startactivity(mContext);
    }

    /**
     * 意见反馈
     */
    @OnClick(R.id.layout_feedback)
    public void gotoFeedBackActivity(){
        FeedBackActivity.startactivity(mContext);
    }
    /**
     * 设置
     */
    @OnClick(R.id.layout_seting)
    public void gotoSeting(){
        SetingActivty.startactivity(mContext);
    }


    /**
     * 帮助
     * @param uodate
     */
    @OnClick(R.id.help_center)
    public void gotohelpcenter(){
        HelpCenterActivity.startactivity(mContext);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uodateuser( UpdateUserEvent uodate){
        updateUserdata();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
