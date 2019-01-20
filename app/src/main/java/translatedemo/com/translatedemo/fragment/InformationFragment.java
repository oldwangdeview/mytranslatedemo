package translatedemo.com.translatedemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.activity.UserinfoActivity;
import translatedemo.com.translatedemo.adpater.ChoiceLangvageAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.base.BaseFragment;
import translatedemo.com.translatedemo.bean.InformationBean;
import translatedemo.com.translatedemo.bean.ListBean_information;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.contans.Contans;
import translatedemo.com.translatedemo.http.HttpUtil;
import translatedemo.com.translatedemo.http.ProgressSubscriber;
import translatedemo.com.translatedemo.http.RxHelper;
import translatedemo.com.translatedemo.interfice.ListOnclickLister;
import translatedemo.com.translatedemo.rxjava.ApiUtils;
import translatedemo.com.translatedemo.rxjava.TranslateApi;
import translatedemo.com.translatedemo.rxjava.TranslateApiUtils;
import translatedemo.com.translatedemo.util.LoadingDialogUtils;
import translatedemo.com.translatedemo.util.LogUntil;
import translatedemo.com.translatedemo.util.ToastUtils;
import translatedemo.com.translatedemo.util.UIUtils;
import translatedemo.com.translatedemo.view.ChoiceDucationDialog;
import translatedemo.com.translatedemo.view.ChoiceLangageDialog;

/**
 * Created by oldwang on 2018/12/30 0030.
 */

public class InformationFragment extends BaseFragment {

    @BindView(R.id.id_cb)
    ConvenientBanner mConvenientBanner;
    @BindView(R.id.choice_text1)
    TextView Choice_text1;
    @BindView(R.id.choice_text2)
    TextView Choice_text2;
    @BindArray(R.array.translate_choiceimage)
    String[] choicedata;

    @BindView(R.id.input_editet)
    EditText input_editet;
    private Dialog mLoadingDialog;
    private ChoiceLangageDialog choicelangage1,choicelangage2;
    private ChoiceLangvageAdpater chiceadpater1,chiceadpater2;

    private String inoputtexttype = "";
    private String outputexttype = "" ;
    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_information);
    }

    @Override
    protected void initData() {
        super.initData();
        chiceadpater1 = new ChoiceLangvageAdpater(mContext,choicedata);
        chiceadpater2 = new  ChoiceLangvageAdpater(mContext,choicedata);
        chiceadpater1.steonitemclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                Choice_text1.setText(choicedata[position]);
                if(choicelangage1!=null){
                    choicelangage1.dismiss();

                }
                switch (position){
                    case 0:
                        inoputtexttype = "bo";
                        break;
                    case 1:
                        inoputtexttype = "zh-cn";
                        break;
                    case 2:
                        inoputtexttype = "en";
                        break;
                    case 3:
                        inoputtexttype = "zh-cn";
                        break;
                }
            }
        });
        chiceadpater2.steonitemclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                Choice_text2.setText(choicedata[position]);
                if(choicelangage2!=null){
                    choicelangage2.dismiss();

                }
                switch (position){
                    case 0:
                        outputexttype = "bo";
                        break;
                    case 1:
                        outputexttype = "zh-cn";
                        break;
                    case 2:
                        outputexttype = "en";
                        break;
                    case 3:
                        outputexttype = "zh-cn";
                        break;
                }
            }
        });
        input_editet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                   String content = input_editet.getText().toString().trim();
                   if(!TextUtils.isEmpty(content)&&!TextUtils.isEmpty(outputexttype)){
                       translatedata(content,outputexttype);
                   }
            }
        });
        getbannerdata();
    }

    @OnClick({R.id.choice_text1,R.id.choice_text2})
    public void choice_text(View v){

        switch (v.getId()){
            case R.id.choice_text1:

                if (choicelangage1==null&&chiceadpater1!=null) {

                    choicelangage1 = new ChoiceLangageDialog.Builder(mContext).setCanCancel(chiceadpater1).create();
                }
                if (!choicelangage1.isShowing()){

                    choicelangage1.show();
                }
                break;
            case R.id.choice_text2:

                if (choicelangage2==null&&chiceadpater2!=null) {

                    choicelangage2 = new ChoiceLangageDialog.Builder(mContext).setCanCancel(chiceadpater2).create();
                }
                if (!choicelangage2.isShowing()){

                    choicelangage2.show();
                }

                break;
        }
    }


    private void getbannerdata(){

        Observable observable =
                ApiUtils.getApi().getinformationlist(BaseActivity.getLanguetype(mContext),1, Contans.cow,BaseActivity.getuser().id+"",1,"2")
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
                        mConvenientBanner.setPages(new CBViewHolderCreator<InformationFragment.MImageViewHolder>() {
                            @Override
                            public InformationFragment.MImageViewHolder createHolder() {
                                return new InformationFragment.MImageViewHolder();
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

     private void translatedata(String content,String outputexttype){
         Observable observable =
                 TranslateApiUtils.getApi().translateconttent(outputexttype,content)
                         .compose(RxHelper.getObservaleTransformer())
                         .subscribeOn(AndroidSchedulers.mainThread());

         HttpUtil.getInstance().toSubscribe(observable, new ProgressSubscriber<String>(mContext) {
             @Override
             protected void _onNext(StatusCode<String> stringStatusCode) {
                 new LogUntil(mContext,TAG+"zixunmessage",new Gson().toJson(stringStatusCode));



             }

             @Override
             protected void _onError(String message) {

                 ToastUtils.makeText(message);


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
            Log.e("imageurl",data.image);
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
}
