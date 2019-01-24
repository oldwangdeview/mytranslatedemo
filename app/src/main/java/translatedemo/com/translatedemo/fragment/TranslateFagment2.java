package translatedemo.com.translatedemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.activity.FeedBackActivity;
import translatedemo.com.translatedemo.activity.Look_imageActivity;
import translatedemo.com.translatedemo.activity.OffLineActivity;
import translatedemo.com.translatedemo.adpater.TanslateTitleAdpater;
import translatedemo.com.translatedemo.adpater.TranslateBottomAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.base.BaseFragment;
import translatedemo.com.translatedemo.bean.CollectionListbean;
import translatedemo.com.translatedemo.bean.DictionaryBean;
import translatedemo.com.translatedemo.bean.InformationBean;
import translatedemo.com.translatedemo.bean.ListBean_information;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.bean.TranslateBean;
import translatedemo.com.translatedemo.contans.Contans;
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

/**
 * Created by oldwang on 2019/1/16 0016.
 */

public class TranslateFagment2  extends BaseFragment {


    @BindView(R.id.titlerecyclerview)
    RecyclerView titlerecyclerview;
    @BindArray(R.array.main_translate)
    String[] myOrderTitles;
    @BindView(R.id.bottom_recyclerview)
    RecyclerView bottom_recyclerview;
    @BindView(R.id.id_cb)
    ConvenientBanner mConvenientBanner;
    @BindView(R.id.translate_linyout)
    LinearLayout translate_linyout;
    @BindView(R.id.input_editext_titl)
    EditText input_editext_titl;
    @BindView(R.id.title_btn)
    TextView title_btn;
    @BindView(R.id.input_text)
    EditText input_text;
    @BindView(R.id.translate_iamge)
    ImageView translate_iamge;
    @BindView(R.id.shouc_image)
    ImageView shouc_image;

    private List<String> titlelistdata = new ArrayList<>();
    TanslateTitleAdpater madpater;
    private int clickindex = 0;
    private Dialog mLoadingDialog;
    private List<DictionaryBean> listdata = new ArrayList<>();
    private TranslateBottomAdpater translatebottomadpater;
    private View becomeview;
    private DictionaryBean choicecd = null;
    private View translate_requestdata;
    private View momessage_view;
    private LinearLayout data;
    private boolean isshouc = false;

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.activity_translate);
    }

    @Override
    protected void initData() {
        super.initData();
        for(int i =0;i<myOrderTitles.length;i++){
            titlelistdata.add(myOrderTitles[i]);
        }
        EventBus.getDefault().register(TranslateFagment2.this);
        clickindex = PreferencesUtils.getInstance().getInt(Contans.PERFICE_TRANSLATE_TITLE_CLICK,0);
        madpater = new TanslateTitleAdpater(mContext,titlelistdata,clickindex);
        madpater.setlistOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                PreferencesUtils.getInstance().putInt(Contans.PERFICE_TRANSLATE_TITLE_CLICK,position);
                clickindex = position;
                getdictionary(position+1);
                translate_linyout.removeAllViews();
            }
        });
        getdictionary(clickindex+1);
        LinearLayoutManager mg = new LinearLayoutManager(mContext);
        mg.setOrientation(LinearLayoutManager.HORIZONTAL);
        titlerecyclerview.setLayoutManager(mg);
        titlerecyclerview.setItemAnimator(new DefaultItemAnimator());
        titlerecyclerview.setAdapter(madpater);

        LinearLayoutManager mg1 = new LinearLayoutManager(mContext);
        mg1.setOrientation(LinearLayoutManager.HORIZONTAL);
        translatebottomadpater = new TranslateBottomAdpater(mContext,listdata);
        bottom_recyclerview.setLayoutManager(mg1);
        bottom_recyclerview.setItemAnimator(new DefaultItemAnimator());
        bottom_recyclerview.setAdapter(translatebottomadpater);
        becomeview = UIUtils.inflate(mContext,R.layout.translate_becomevip_layout);
        translate_requestdata = UIUtils.inflate(mContext,R.layout.translate_tansrequest_layout);
        momessage_view = UIUtils.inflate(mContext,R.layout.translate_nomessagedata);
        data = translate_requestdata.findViewById(R.id.data);
        translatebottomadpater.setlistOnclickLister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                translate_linyout.removeAllViews();
                choicecd = listdata.get(position);
                if(listdata.get(position).isMemberVisible==1&&BaseActivity.getuser().isMember==0){
                    translate_linyout.addView(becomeview);
                }else{
                    String inputString = input_editext_titl.getText().toString().trim();
                    if(!TextUtils.isEmpty(inputString)) {
                        translatecontent(inputString,clickindex+1,choicecd.id);
                    }
                }
            }
        });

        translate_requestdata.findViewById(R.id.bottom_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedBackActivity.startactivity(mContext);
            }
        });

        momessage_view.findViewById(R.id.fanhui_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedBackActivity.startactivity(mContext);
            }
        });

        input_editext_titl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputString = input_editext_titl.getText().toString().trim();
                if(!TextUtils.isEmpty(inputString)){
                    title_btn.setText(mContext.getResources().getString(R.string.translate_text_qued));
                    translate_iamge.setVisibility(View.GONE);
                    requst_data = null;
                }else{
                    title_btn.setText(mContext.getResources().getString(R.string.translate_text_quxiao));
                }
            }
        });

        title_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title_btn.getText().equals(mContext.getResources().getString(R.string.translate_text_qued))){

                    String inputString = input_editext_titl.getText().toString().trim();
                    if(choicecd!=null){
                        input_text.setText(inputString);
                        translatecontent(inputString,clickindex+1,choicecd.id);
                    }

                }else{
                    EventBus.getDefault().post(new UpdateMainIndex(0));
                    return;
                }
            }
        });
        if(BaseActivity.getuser().isMember==0) {
            getbannerdata();
        }
    }



    private void getdictionary(int type){
        Observable observable =
                ApiUtils.getApi().getAllDictionary(BaseActivity.getuser().id+"",BaseActivity.getLanguetype(mContext),type)
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<List<DictionaryBean>>(mContext) {
            @Override
            protected void _onNext(StatusCode<List<DictionaryBean>> stringStatusCode) {
                new LogUntil(mContext,TAG+"getAllDictionary",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode.getData()!=null&&stringStatusCode.getData().size()>0){
                    listdata.clear();
                    choicecd = stringStatusCode.getData().get(0);
                    listdata.addAll(stringStatusCode.getData());
                }
                translatebottomadpater.notifyDataSetChanged();
                translatebottomadpater.updatecliclk();
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }


    private void getbannerdata(){

        Observable observable =
                ApiUtils.getApi().getinformationlist(BaseActivity.getLanguetype(mContext),1,Contans.cow,BaseActivity.getuser().id+"",1,"2")
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
                        mConvenientBanner.setPages(new CBViewHolderCreator<MImageViewHolder>() {
                            @Override
                            public MImageViewHolder createHolder() {
                                return new MImageViewHolder();
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


    /**
     * 翻译单词
     * @param contengt
     * @param type
     */

    private TranslateBean requst_data;
    private void translatecontent(String contengt,int type,int daviceid){
        Observable observable =
                ApiUtils.getApi().translateconttent(BaseActivity.getuser().id+"",BaseActivity.getLanguetype(mContext),type,contengt,daviceid)
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<TranslateBean>(mContext) {
            @Override
            protected void _onNext(StatusCode<TranslateBean> stringStatusCode) {
                new LogUntil(mContext,TAG+"translateconttent",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                if(stringStatusCode!=null&&stringStatusCode.getData()!=null){
                    if(requst_data!=null&&requst_data.id==stringStatusCode.getData().id){
                        if(isshouc){
                            shouc_image.setImageResource(R.mipmap.shoucang2);
                        }
                    }else{
                        shouc_image.setImageResource(R.mipmap.shoucang1);
                        isshouc = false;
                    }
                    requst_data = stringStatusCode.getData();
                    translate_linyout.removeAllViews();
                    data.removeAllViews();
                    String translateResult = stringStatusCode.getData().translateResult;
                    if(!TextUtils.isEmpty(translateResult)) {
                        if (translateResult.indexOf(";") > 0) {
                            String[] line = translateResult.split(";");
                            for (int i = 0; i < line.length; i++) {
                               View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                               TextView textView = textv.findViewById(R.id.text);
                               textView.setText((i+1)+". "+line[i]);
                               data.addView(textv);
                            }
                        }else{
                            View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                            TextView textView = textv.findViewById(R.id.text);
                            textView.setText("1. "+translateResult);
                            data.addView(textv);
                        }
                    }
                    translate_linyout.addView(translate_requestdata);
                    if(!TextUtils.isEmpty(requst_data.image)){
                        translate_iamge.setVisibility(View.VISIBLE);
                        requst_dataimage = requst_data.image;
                        UIUtils.loadImageView(mContext,requst_data.image,translate_iamge);
                    }
                }

            }

            @Override
            protected void _onError(String message) {
                 translate_linyout.removeAllViews();
                 TextView text1 = momessage_view.findViewById(R.id.text);
                text1.setText(message);
                translate_linyout.addView(momessage_view);
//                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }

    public class MImageViewHolder implements Holder<ListBean_information> {
        private View mhandeview;
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            mhandeview = UIUtils.inflate(mContext,R.layout.translate_banner_item);
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
    @OnClick(R.id.shouc_image)
    public void shouc(){
        if(!isshouc){
            if(requst_data!=null) {
                collectionDictionary();
            }
        }
        isshouc = true;

    }

    private void collectionDictionary(){
        Observable observable =
                ApiUtils.getApi().collectionDictionary(BaseActivity.getLanguetype(mContext),BaseActivity.getuser().id+"",clickindex+1,input_editext_titl.getText().toString().trim(),requst_data.translateResult,choicecd.id+"","1")
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

        HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<Object>(mContext) {
            @Override
            protected void _onNext(StatusCode<Object> stringStatusCode) {
                new LogUntil(mContext,TAG+"zixunmessage",new Gson().toJson(stringStatusCode));
                LoadingDialogUtils.closeDialog(mLoadingDialog);
                shouc_image.setImageResource(R.mipmap.shoucang2);

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setdata(CollectionListbean mdata){
        input_editext_titl.setText(mdata.content);
        translate_linyout.removeAllViews();
        input_text.setText(mdata.content);
        data.removeAllViews();
        isshouc = true;
        shouc_image.setImageResource(R.mipmap.shoucang2);
        String translateResult = mdata.translateContent;
        if(!TextUtils.isEmpty(translateResult)) {
            if (translateResult.indexOf(";") > 0) {
                String[] line = translateResult.split(";");
                for (int i = 0; i < line.length; i++) {
                    View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                    TextView textView = textv.findViewById(R.id.text);
                    textView.setText((i+1)+". "+line[i]);
                    data.addView(textv);
                }
            }else{
                View textv = UIUtils.inflate(mContext,R.layout.layout_text);
                TextView textView = textv.findViewById(R.id.text);
                textView.setText("1. "+translateResult);
                data.addView(textv);
            }
        }
        translate_linyout.addView(translate_requestdata);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(TranslateFagment2.this);
    }
    private String requst_dataimage = "";
    @OnClick(R.id.translate_iamge)
    public void lookimage(){
        if(!TextUtils.isEmpty(requst_dataimage))
        Look_imageActivity.startactivity(mContext,requst_dataimage);
    }
}
