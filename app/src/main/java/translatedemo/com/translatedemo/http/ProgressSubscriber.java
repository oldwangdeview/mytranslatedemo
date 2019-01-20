package translatedemo.com.translatedemo.http;


import android.content.Context;
import android.util.Log;

import com.google.gson.JsonSyntaxException;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import translatedemo.com.translatedemo.bean.StatusCode;
import translatedemo.com.translatedemo.util.NetUtils;


/**
 * Created by helin on 2016/10/10 15:49.
 */

public abstract class ProgressSubscriber<T> implements Observer<StatusCode<T>> , ProgressCancelListener{

    private Context mContext;
    private Disposable mD;

    public ProgressSubscriber(Context context) {
        mContext = context;
    }
    @Override
    public void onSubscribe(Disposable d) {

        mD = d;
    }

    @Override
    public void onNext(StatusCode<T> tStatusCode) {
        _onNext(tStatusCode);
    }

    @Override
    public void onError(Throwable e) {
        Log.e("message",e.getMessage());
        if (!NetUtils.NetwrokIsUseful(mContext)) { //这里自行替换判断网络的代码
            _onError("网络不可用");
        } else if (e instanceof ApiException) {
            _onError(e.getMessage());
        } else if(e instanceof JsonSyntaxException){
            _onError("解析失败，请稍后再试...");
        }else{
            _onError("请求失败，请稍后再试...");
        }
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onCancelProgress() {
        if (!this.mD.isDisposed()) {
            this.mD.dispose();
        }
    }
    protected abstract void _onNext(StatusCode<T> tStatusCode);
    protected abstract void _onError(String message);
}
