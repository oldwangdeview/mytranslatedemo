package translatedemo.com.translatedemo.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.PublishSubject;
import translatedemo.com.translatedemo.languageutil.LanguageUtil;

/**
 * Author yichao
 * Time  2017/11/30 9:08
 * Dest  Fragment基类
 */

public abstract class BaseFragment extends Fragment {
    public View mContentView;
    public Context mContext;
    private Unbinder mUnbinder;
    public static final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();

    protected  String TAG=this.getClass().getSimpleName();
    public BaseFragment(){}


    @Override
    public void onAttach(Context context) {
        mContext=getActivity();
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext=getActivity();
            mContentView=initView(mContext);
            mUnbinder = ButterKnife.bind(this, mContentView);
            initData();
            initEvent();
        return  mContentView;

        
    }



    /**
     * 初始化事件
     */
    protected void initEvent() {
    }

    /**
     * 设置数据
     */
    protected void initData() {
    }

    /**
     * 初始化布局
     * @param context
     * @return
     */
    public abstract View initView(Context context);

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        mUnbinder.unbind();
//        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
//    }
//
//    @Override
//    public void onPause() {
//        lifecycleSubject.onNext(ActivityLifeCycleEvent.PAUSE);
//        super.onPause();
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       /* RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);*/
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onStop();
    }
}
