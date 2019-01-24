package translatedemo.com.translatedemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.xiaweizi.cornerslibrary.CornersProperty;
import com.xiaweizi.cornerslibrary.RoundCornersTransformation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.activity.LoginActivity;
import translatedemo.com.translatedemo.activity.MainActivity;
import translatedemo.com.translatedemo.activity.NoticeDetailActivity;
import translatedemo.com.translatedemo.activity.TranslateActivity;
import translatedemo.com.translatedemo.adpater.TranslateAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.base.BaseFragment;
import translatedemo.com.translatedemo.bean.InformationBean;
import translatedemo.com.translatedemo.bean.ListBean_information;
import translatedemo.com.translatedemo.bean.LoginBean;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.eventbus.OverMainactivty;
import translatedemo.com.translatedemo.eventbus.UpdataInfomation;
import translatedemo.com.translatedemo.eventbus.UpdateMainIndex;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.PreferencesUtils;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;
import translatedemo.com.translatedemo.view.LoadingPagerHead;
import translatedemo.com.translatedemo.view.YRecycleview;

/**
 * Created by oldwang on 2018/12/28 0028.
 */

public class TranslateFragment extends BaseFragment {



    @BindView(R.id.yrecycleview_)
    YRecycleview yrecycleview_;
    private LoadingPagerHead mLoadingPagerHead;
    private View mHeadView;
    private TranslateAdpater madpater;
    public List<ListBean_information> list = new ArrayList<>();
    public ConvenientBanner mConvenientBanner;
    private View headview;
    @Override
    public View initView(Context context) {
        mLoadingPagerHead = new LoadingPagerHead(context) {
            @Override
            protected void close() {

            }

            @Override
            protected View createSuccessView() {
                return UIUtils.inflate(mContext, R.layout.fragment_translate);
            }

            @Override
            protected void reLoading() {
                retry(false);

            }
        };

        mHeadView = UIUtils.inflate(context, R.layout.headview_translatefragemt);
        mConvenientBanner = mHeadView.findViewById(R.id.id_cb);

        return mLoadingPagerHead;
    }


    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        yrecycleview_.addHeadView(mHeadView);
        mHeadView.findViewById(R.id.myhome_line_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new UpdateMainIndex(3));
            }
        });
        mHeadView.findViewById(R.id.myhome_line_top_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new UpdateMainIndex(3));
            }
        });
        madpater = new TranslateAdpater(mContext, list);
        mLoadingPagerHead.showPagerView(Contans.STATE_SUCCEED);
        yrecycleview_.setLayoutManager(new LinearLayoutManager(mContext));
        yrecycleview_.setItemAnimator(new DefaultItemAnimator());
        yrecycleview_.setRefreshAndLoadMoreListener(mlister);
        yrecycleview_.setAdapter(madpater);
        madpater.setlistOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                if(list.get(position).type==0) {
                    NoticeDetailActivity.startactivity(mContext, list.get(position));
                }else{
                    UIUtils.openWebUrl(mContext,list.get(position).url);
                }
            }
        });
        getdata();
        retry(false);
    }
    private Dialog mLoadingDialog;
    private int contentpage = 1;
    private void retry(final boolean type){
        if(type){
            contentpage++;
        }else{
            contentpage = 1;
        }


        Observable observable =
                ApiUtils.getApi().getinformationlist(BaseActivity.getLanguetype(mContext),contentpage,Contans.cow,BaseActivity.getuser().id+"",0,"")
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<InformationBean>(mContext) {
            @Override
            protected void _onNext(StatusCode<InformationBean> stringStatusCode) {
                new LogUntil(mContext,TAG+"zixunmessage",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                mLoadingPagerHead.showPagerView(Contans.STATE_SUCCEED);
                if(stringStatusCode.getCode()==0&&stringStatusCode.getData()!=null) {
                    if (!type) {
                        list.clear();
                        yrecycleview_.setReFreshComplete();
                    }else{
                        yrecycleview_.setloadMoreComplete();
                    }
                    list.addAll(stringStatusCode.getData().list);
                    madpater.notifyDataSetChanged();
                }
            }

            @Override
            protected void _onError(String message) {
                if (!type) {
                    yrecycleview_.setReFreshComplete();
                }else{
                    yrecycleview_.setloadMoreComplete();
                }
                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                mLoadingPagerHead.showPagerView(Contans.STATE_ERROR);
            }
        }, "", lifecycleSubject, false, true);
    }


    private void getdata(){
         Log.e("user",new Gson().toJson(BaseActivity.getuser()));
        Observable observable =
                ApiUtils.getApi().getinformationlist(BaseActivity.getLanguetype(mContext),contentpage,Contans.cow,BaseActivity.getuser().id+"",1,"1")
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<InformationBean>(mContext) {
            @Override
            protected void _onNext(StatusCode<InformationBean> stringStatusCode) {
                new LogUntil(mContext,TAG+"zixunmessage",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getData()!=null&&stringStatusCode.getData().list.size()>0) {
                    List<ListBean_information> adsBeans = stringStatusCode.getData().list;
                    if (adsBeans != null && adsBeans.size() > 0) {
                        mConvenientBanner.setPages(new CBViewHolderCreator<ImageViewHolder>() {
                            @Override
                            public ImageViewHolder createHolder() {
                                return new ImageViewHolder();
                            }
                        }, adsBeans)
                                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL); //设置指示器的方向水平  居中
                    }
                    if (adsBeans.size() == 1) {
                        mConvenientBanner.setPointViewVisible(false);
                    } else {
                        mConvenientBanner.setPointViewVisible(true);
                    }
                }

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);


    }


    public class ImageViewHolder implements Holder<ListBean_information> {
        private View mhandeview;
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            mhandeview = UIUtils.inflate(mContext,R.layout.banner_item);
            return mhandeview;
        }

        @Override
        public void UpdateUI(Context context, int position, ListBean_information data) {
            imageView = mhandeview.findViewById(R.id.image);

            final  String weburl = data.url;
            UIUtils.loadImageViewRoud(mContext,data.image,imageView,UIUtils.dip2px(15));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   UIUtils.openWebUrl(mContext,weburl);
                }
            });
        }
    }


    /**
     * 刷新监听
     */
    YRecycleview.OnRefreshAndLoadMoreListener mlister = new YRecycleview.OnRefreshAndLoadMoreListener() {
        @Override
        public void onRefresh() {
            retry(false);
        }

        @Override
        public void onLoadMore() {
            retry(true);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mConvenientBanner.startTurning(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        mConvenientBanner.stopTurning();
    }

    /**
     * 刷新数据
     * @param
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishMainactivity(UpdataInfomation update){
        retry(false);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


//    /**
//     * 跳转到翻译
//     */
//    @OnClick({R.id.myhome_line_top,R.id.myhome_line_top_1})
//    public void gotoTranslateActivity(){
//        EventBus.getDefault().post(new UpdateMainIndex(3));
//    }
}
