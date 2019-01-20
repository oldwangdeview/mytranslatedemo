package translatedemo.com.translatedemo.http;




import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.PublishSubject;
import translatedemo.com.translatedemo.base.ActivityLifeCycleEvent;
import translatedemo.com.translatedemo.bean.StatusCode;

/**
 *
 *
 */

public class HttpUtil {

    /**
     * 构造方法私有
     */
    private HttpUtil() {

    }

    /**
     * 在访问HttpMethods时创建单例
     */
    private static class SingletonHolder {
        private static final HttpUtil INSTANCE = new HttpUtil();
    }

    /**
     * 获取单例
     */
    public static HttpUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 添加线程管理并订阅
     * @param ob  处罚
     * @param subscriber  处理
     * @param cacheKey 缓存kay
     * @param lifecycleSubject 生命周期
     * @param isSave 是否缓存
     * @param forceRefresh 是否强制刷新
     */
    public void toSubscribe(Observable ob, final ProgressSubscriber subscriber, String cacheKey, final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject, boolean isSave, boolean forceRefresh) {
        //数据预处理
        ObservableTransformer<StatusCode<Object>, StatusCode<Object>> result = RxHelper.handleResult(ActivityLifeCycleEvent.DESTROY,lifecycleSubject);
        Observable observable = ob.compose(result);
              /*  .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        //显示Dialog和一些其他操作
                        subscriber.showProgressDialog();
                    }
                });*/
        RetrofitCache.load(cacheKey,observable,isSave,forceRefresh).subscribe(subscriber);
    }
}
