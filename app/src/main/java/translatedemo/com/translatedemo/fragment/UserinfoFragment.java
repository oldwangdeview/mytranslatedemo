package translatedemo.com.translatedemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.activity.FeedBackActivity;
import translatedemo.com.translatedemo.activity.GetCouponActivty;
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
import translatedemo.com.translatedemo.bean.GetCouponListBean;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.UpdateCuponEvent;
import translatedemo.com.translatedemo.eventbus.UpdateUserEvent;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.ToastUtils;
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
    @BindView(R.id.cupon_size)
    TextView cupon_size;
    private Dialog mLoadingDialog;
    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_user);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        updateUserdata();
        getcuponsize();
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
     */
    @OnClick(R.id.help_center)
    public void gotohelpcenter(){
        HelpCenterActivity.startactivity(mContext);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void uodateuser( UpdateUserEvent uodate){
        updateUserdata();
    }

    private void getcuponsize(){
        Observable observable =
                ApiUtils.getApi().getCouponList(0, BaseActivity.getuser().id+"",BaseActivity.getLanguetype(mContext))
                        .compose(RxHelper.getObservaleTransformer())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                try {


                                    if (mLoadingDialog == null) {
                                        mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
                                    }
                                    LoadingDialogUtils.show(mLoadingDialog);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread());

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<List<GetCouponListBean>>(mContext) {
            @Override
            protected void _onNext(StatusCode<List<GetCouponListBean>> stringStatusCode) {
                new LogUntil(mContext,TAG+"getCouponList",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);

                if(stringStatusCode.getCode()==0&&stringStatusCode.getData()!=null&&stringStatusCode.getData().size()>0) {
                    cupon_size.setVisibility(View.VISIBLE);
                    cupon_size.setText(stringStatusCode.getData().size()+"");
                }else{
                    cupon_size.setVisibility(View.GONE);
                }
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
       ;
            }
        }, "", lifecycleSubject, false, true);
    }

    /**
     * 刷新领取优惠券
     * @param uodate
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updatacopo( UpdateCuponEvent uodate){
        getcuponsize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
