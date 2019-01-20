package translatedemo.com.translatedemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.adpater.MyCuponAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.base.BaseFragment;
import translatedemo.com.translatedemo.bean.GetCouponListBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;
import translatedemo.com.translatedemo.view.LoadingPagerHead;
import translatedemo.com.translatedemo.view.YRecycleview;

/**
 * Created by oldwang on 2019/1/7 0007.
 */

public class CouponFragment1  extends BaseFragment {

    @BindView(R.id.yrecycleview_)
    YRecycleview yrecycleview_;
    private LoadingPagerHead mLoadingPagerHead;
    private Dialog mLoadingDialog;
    public static final int mType = 0;
    private List<GetCouponListBean> listdata = new ArrayList<>();
    private MyCuponAdpater madpater;
    @Override
    public View initView(Context context) {
        mLoadingPagerHead = new LoadingPagerHead(context) {
            @Override
            protected void close() {

            }

            @Override
            protected View createSuccessView() {
                return UIUtils.inflate(mContext, R.layout.fragment_coupon);
            }

            @Override
            protected void reLoading() {
                retry();

            }
        };
        return mLoadingPagerHead;
    }

    @Override
    protected void initData() {
        super.initData();
        madpater = new MyCuponAdpater(mContext,listdata,mType);
        yrecycleview_.setLayoutManager(new LinearLayoutManager(mContext));
        yrecycleview_.setItemAnimator(new DefaultItemAnimator());
        yrecycleview_.setAdapter(madpater);

        yrecycleview_.setRefreshAndLoadMoreListener(new YRecycleview.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                yrecycleview_.setReFreshComplete();
                retry();
            }

            @Override
            public void onLoadMore() {
                yrecycleview_.setloadMoreComplete();
                retry();
            }
        });
        retry();
    }
    public void  retry(){

        Observable observable =
                ApiUtils.getApi().getCouponList(mType, BaseActivity.getuser().id+"",BaseActivity.getLanguetype(mContext))
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
                    mLoadingPagerHead.showPagerView(Contans.STATE_SUCCEED);
                    listdata.clear();
                    listdata.addAll(stringStatusCode.getData());
                    madpater.notifyDataSetChanged();
                }else{
                    mLoadingPagerHead.showPagerView(Contans.STATE_EMPTY);
                }
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                mLoadingPagerHead.showPagerView(Contans.STATE_ERROR);
            }
        }, "", lifecycleSubject, false, true);
    }
}
