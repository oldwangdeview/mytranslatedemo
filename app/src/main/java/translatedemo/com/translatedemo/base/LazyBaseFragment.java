package translatedemo.com.translatedemo.base;

/**
 * Author yichao
 * Time  2018/4/12 14:34
 * Dest  懒加载fragment
 */

public abstract class LazyBaseFragment extends BaseFragment {
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    protected void onVisible(){
        lazyLoad();
    }

    /**
     *  懒加载
     */
    protected  void lazyLoad(){}

    /**
     * 隐藏
     */

    protected void onInvisible(){}

}
