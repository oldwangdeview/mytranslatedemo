package translatedemo.com.translatedemo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.subjects.PublishSubject;

/**
 * Author yichao
 * Time  2017/11/27 16:36
 * Dest  Activity的基类
 */

public abstract class BaseLoadingActivity extends AppCompatActivity {
    public String TAG=this.getClass().getSimpleName();
    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initEvent();


    }

    /**
     * 初始化事件
     */
    protected  void initEvent(){};
    /**
     * 初始化数据
     */
    protected  void initData(){};

    /**
     * 初始化界面
     */
    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);

    }
    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onStop();
    }

}
