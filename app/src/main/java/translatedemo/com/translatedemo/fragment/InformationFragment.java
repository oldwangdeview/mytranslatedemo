package translatedemo.com.translatedemo.fragment;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
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

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.activity.PeopleTranslateActivity;
import translatedemo.com.translatedemo.activity.UserinfoActivity;
import translatedemo.com.translatedemo.adpater.ChoiceLangvageAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.base.BaseFragment;
import translatedemo.com.translatedemo.bean.CollectionListbean;
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
    @BindArray(R.array.main_translate)
    String[] myOrderTitles;
    @BindView(R.id.data)
    LinearLayout data;
    private Dialog mLoadingDialog;
    private ChoiceLangageDialog choicelangage1,choicelangage2;
    private ChoiceLangvageAdpater chiceadpater1,chiceadpater2;
    private View tanslaterequest;
    private String inoputtexttype = "";
    private String outputexttype = "" ;
    private TextView content;
    private ImageView shoucangimage;

    @Override
    public View initView(Context context) {
        return UIUtils.inflate(mContext, R.layout.fragment_information);
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        chiceadpater1 = new ChoiceLangvageAdpater(mContext,choicedata);
        chiceadpater2 = new  ChoiceLangvageAdpater(mContext,choicedata);
        tanslaterequest = UIUtils.inflate(mContext,R.layout.layout_translate_request);
        shoucangimage = tanslaterequest.findViewById(R.id.shouc_image);
        content = tanslaterequest.findViewById(R.id.text);
        tanslaterequest.findViewById(R.id.shouc_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionDictionary(input_editet.getText().toString().trim());
            }
        });
        tanslaterequest.findViewById(R.id.fuzhi_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
                    String text = content.getText().toString().trim();
                    ClipData mClipData = ClipData.newPlainText("Label", text);
// 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    ToastUtils.makeText("已复制");
                }catch (Exception e){

                }
            }
        });
        tanslaterequest.findViewById(R.id.pople_translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeopleTranslateActivity.startactivity(mContext);
            }
        });

        chiceadpater1.steonitemclicklister(new ListOnclickLister() {
            @Override
            public void onclick(View v, int position) {
                Choice_text1.setText(choicedata[position]);
                if(choicelangage1!=null){
                    choicelangage1.dismiss();
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
                   try {
                       content = new String(content.getBytes(), "utf-8");
                   }catch (Exception e){

                   }
                   String inputdata = Choice_text1.getText().toString().trim();
                   String outputdata = Choice_text2.getText().toString().trim();
                   if(!TextUtils.isEmpty(content)&&!TextUtils.isEmpty(inputdata)&&!TextUtils.isEmpty(outputdata)){
                       translatedata(URLEncoder.encode(content),getoutputtype(inputdata,outputdata));
                   }else{
                       data.removeAllViews();
                   }
            }
        });
        if(BaseActivity.getuser().isMember==0) {
            getbannerdata();
        }
    }

    private String getoutputtype(String input,String outpout){
        String type = "";
        if(!TextUtils.isEmpty(input)&&!TextUtils.isEmpty(outpout)){
            type = getlangvagetype(input)+"_"+getlangvagetype(outpout);
        }
        return type;
    }

    private int clickindex(String input,String outpout){
           String data = input.replace("语","")+outpout.replace("语","");
           for(int i =0;i<myOrderTitles.length;i++){
               if(data.equals(myOrderTitles[i])){
                   return i+1;
               }
           }
           return -1;
    }
    private String getlangvagetype(String data){
        String outputexttype = "";
        for(int i=0;i<choicedata.length;i++){
            if(data.equals(choicedata[i])){
                switch (i){
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
            }else{
                continue;
            }
        }
        return outputexttype;
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

     private void translatedata(final String content,final String outputexttype){
         new LogUntil(mContext,TAG+"content",content);
         new LogUntil(mContext,TAG+"outputexttype",outputexttype);
//         if (mLoadingDialog == null) {
//             mLoadingDialog = LoadingDialogUtils.createLoadingDialog(mContext, "");
//         }
//         LoadingDialogUtils.show(mLoadingDialog);
         new Thread(){
             @Override
             public void run() {
                 super.run();
                 try {
                     okPost(outputexttype,content);
                 }catch (Exception e){
                     new LogUntil(mContext,TAG+"content",e.getMessage());
                 }

             }
         }.start();

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

    private String mdata = "";
    Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.arg1==101){
                 mdata = msg.obj+"";
                data.removeAllViews();

                new LogUntil(mContext,TAG+"zixunmessage_xxx",mdata);
                if(mdata.indexOf("[ERR]")>=0){

                }else{
                    content.setText(mdata);
                    data.addView(tanslaterequest);
                }
            }
        }
    };
    @Override
    public void onPause() {
        super.onPause();
        mConvenientBanner.stopTurning();
    }
     private void okPost(String outputtype, String data) throws IOException {
        String path = "https://nmt.xmu.edu.cn/nmt"+"?lang="+outputtype+"&src="+data;

      OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Message msg = new Message();
                msg.obj = string;
                msg.arg1 = 101;
                mhandler.sendMessage(msg);
            }
        });
//        return response.body().string();
    }

    /**
     * 收藏
     */

   private String minputdata = "";
    private void collectionDictionary(String inoutdata){
        if(minputdata.equals(inoutdata)){
            return;
        }else{
            minputdata = inoutdata;
        }
        Observable observable =
                ApiUtils.getApi().collectionDictionary(BaseActivity.getLanguetype(mContext),BaseActivity.getuser().id+"",clickindex(Choice_text1.getText().toString().trim(),Choice_text1.getText().toString().trim()),inoutdata,mdata,"","0")
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
                shoucangimage.setImageResource(R.mipmap.shoucang2);

            }

            @Override
            protected void _onError(String message) {

                ToastUtils.makeText(message);
                LoadingDialogUtils.closeDialog(mLoadingDialog);

            }
        }, "", lifecycleSubject, false, true);
    }

    @OnClick(R.id.qieh)
    public void qieh(){
        String text1 = Choice_text1.getText().toString().trim();
        String text2 = Choice_text2.getText().toString().trim();
        Choice_text1.setText(text2);
        Choice_text2.setText(text1);
        input_editet.setText("");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setdata(CollectionListbean mdata) {
        input_editet.setText(mdata.content);
    }
}
