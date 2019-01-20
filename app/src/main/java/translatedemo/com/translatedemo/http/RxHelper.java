package translatedemo.com.translatedemo.http;




import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import translatedemo.com.translatedemo.base.ActivityLifeCycleEvent;
import translatedemo.com.translatedemo.bean.StatusCode;


/**
 * Created by helin on 2016/11/9 17:02.
 */

public class RxHelper {

    /**
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<StatusCode<T>, StatusCode<T>> handleResult(final ActivityLifeCycleEvent event,
                                                                                       final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject) {
        return new ObservableTransformer<StatusCode<T>, StatusCode<T>>() {
            @Override
            public ObservableSource<StatusCode<T>> apply(Observable<StatusCode<T>> tObservable) {
                Observable<ActivityLifeCycleEvent> compareLifecycleObservable =
                        lifecycleSubject.filter(new Predicate<ActivityLifeCycleEvent>() {
                            @Override
                            public boolean test(ActivityLifeCycleEvent activityLifeCycleEvent) throws Exception {


                                return activityLifeCycleEvent.equals(event);
                            }
                        }).firstElement().toObservable();
                return tObservable.flatMap(new Function<StatusCode<T>, ObservableSource<StatusCode<T>>>() {
                    @Override
                    public ObservableSource<StatusCode<T>> apply(StatusCode<T> result) throws Exception {
                       if (result.getCode() == 0) {
                           return createData(result);
                      }/*else if (result.getNumber()==2001){
                           return createData(result);
                       }*/else {
                           return Observable.error(new ApiException(result.getCode(),result));
                       }
                    }
                }).takeUntil(compareLifecycleObservable)
                        .subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread());

            }
        };
    }

    /**
     *
     *
     */

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<StatusCode<T>> createData(final StatusCode<T> data) {
        return Observable.create(new ObservableOnSubscribe<StatusCode<T>>() {
            @Override
            public void subscribe(ObservableEmitter<StatusCode<T>> emitter) throws Exception {
                try {
                    emitter.onNext(data);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }


        });

    }

    /**
     * 切换线程
     * @return
     */
    public static ObservableTransformer getObservaleTransformer(){

        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {


                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };




    }


}
