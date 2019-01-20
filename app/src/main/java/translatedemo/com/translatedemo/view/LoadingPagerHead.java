package translatedemo.com.translatedemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import translatedemo.com.translatedemo.R;

import translatedemo.com.translatedemo.contans.Contans;

import translatedemo.com.translatedemo.util.UIUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 先加顺序 load -->showPagerView-->createSuccessView
 *
 * @author wanghao
 *
 *在子类中 耗时操作放到 load中，然后load返回一个状态，在showPagerView中根据状态选择 显示的页面
 *如果装在是成功的。那么久显示 createSuccessView
 */
public abstract class LoadingPagerHead extends FrameLayout {

    // 加载默认的状态
    private static final int STATE_UNLOADED = 1;
    // 加载的状态
    private static final int STATE_LOADING = 2;
    // 加载失败的状态
    private static final int STATE_ERROR = 3;
    // 加载空的状态
    private static final int STATE_EMPTY = 4;
    // 加载成功的状态
    private static final int STATE_SUCCEED = 5;

    private static final int STATE_SOLDOUT=6;


    private View mLoadingView;// 转圈的view
    private View mErrorView;// 错误的view
    private View mEmptyView;// 空的view
    private View mSucceedView;// 成功的view
    private View mSoleoutView;

    private int mErr_content_layout;
    private int mtype;
    private Context mContext;

    public LoadingPagerHead(Context context) {
        super(context);
        mContext = context;
        init();
    }

    /**
     *
     * @param context
     * @param empty_content_layout  空布局
     * @param type    下架的类型    1 楼盘  2 店铺  3商品 4 共享家
     */
    public LoadingPagerHead(Context context, int empty_content_layout, int type ) {
        super(context);
        mErr_content_layout = empty_content_layout;
        mtype=type;
        mContext = context;
        init();
    }
    public LoadingPagerHead(Context context, int empty_content_layout ) {
        super(context);
        mErr_content_layout = empty_content_layout;
        mContext = context;
        init();
    }
    public LoadingPagerHead(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public LoadingPagerHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        // 初始化三个 状态的view 这个时候 三个状态的view叠加在一起了
        mLoadingView = createLoadingView();
        if (null != mLoadingView) {
            addView(mLoadingView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }
        mErrorView = createErrorView();
        if (null != mErrorView) {
            addView(mErrorView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }
        mEmptyView = createEmptyView();
        if (null != mEmptyView) {
            addView(mEmptyView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }
        mSoleoutView=createSoleoutView();

        if (null!=mSoleoutView){

            addView(mSoleoutView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
        }



        if ( mSucceedView == null) {
            mSucceedView = createSuccessView();
            addView(mSucceedView, new LayoutParams
                    (LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }





        showPagerView(Contans.STATE_UNLOADED);

    }

    /**
     * 创建下架布局
     */
    private View createSoleoutView() {
        View soldOutView = UIUtils.inflate(R.layout.sold_out_layout);
    ImageView ivIcon=    soldOutView.findViewById(R.id.iv_sold_out_layout);
    TextView tvInfo=    soldOutView.findViewById(R.id.tv_info_sold_out_layout);
    TextView  tvOncick=    soldOutView.findViewById(R.id.tv_onclick_sold_out_layout);

//      //   * @param type    下架的类型    1 楼盘  2 店铺  3商品 4 共享家

//        tvOncick.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mtype==Contans.HOUSE_SOLDOUT_TYPE){
//                    EventBus.getDefault().post(new SoldOutEvent(1));
//                    MainActivity.goGoodsShop(mContext,1);
//
//                }else if (mtype==Contans.STORE_SOLDOUT_TYPE){
//                    EventBus.getDefault().post(new SoldOutEvent(2));
//                    MainActivity.goGoodsShop(mContext,2);
//
//                }else if (mtype==Contans.GOODS_SOLDOUT_TYPE){
//                    EventBus.getDefault().post(new SoldOutEvent(3));
//                    MainActivity.goGoodsShop(mContext,2);
//
//                }else if (mtype==Contans.SHARHOUSE_SOLDOUT_TYPE){
//                    EventBus.getDefault().post(new SoldOutEvent(4));
//                    MainActivity.goGoodsShop(mContext,4);
//                }
//            }
//        });



        return soldOutView;


    }


    public void showPagerView(int state) {

        // 這個時候 都不為空 mState默認是STATE_UNLOADED狀態所以只顯示 lodaing 下面的 error
        // 和empty暂时不显示
        if (null != mLoadingView) {
            mLoadingView.setVisibility(state == STATE_UNLOADED
                    || state == STATE_LOADING ? View.VISIBLE :

                    View.INVISIBLE);
        }
        if (null != mErrorView) {
            mErrorView.setVisibility(state == STATE_ERROR ? View.VISIBLE
                    : View.INVISIBLE);
        }
        if (null != mEmptyView) {
            mEmptyView.setVisibility(state == STATE_EMPTY ? View.VISIBLE
                    : View.INVISIBLE);
        }

        if (null != mSucceedView) {
            mSucceedView.setVisibility(state == STATE_SUCCEED ?
                    View.VISIBLE : View.INVISIBLE);
        }
        if (null!=mSoleoutView){
            mSoleoutView.setVisibility(state==STATE_SOLDOUT?View.VISIBLE:View.INVISIBLE);

        }

    }





    /**
     * 空界面
     *
     * @return
     */
    public View createEmptyView() {
        //iv_back_loadpage_empty
        View inflate = UIUtils.inflate(R.layout.loadpage_empty);
        ImageView ivBackImage = inflate.findViewById(R.id.iv_back_loadpage_empty);
        RelativeLayout rlHeadRootEmptyView = inflate.findViewById(R.id.rl_headroot_loadpage_empty);

     FrameLayout frameLayout=   inflate.findViewById(R.id.fl_empty_content_root);
        rlHeadRootEmptyView.setVisibility(GONE);
        ivBackImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        if (mErr_content_layout!=0){
         View  contentView=   UIUtils.inflate(mErr_content_layout);
            frameLayout.addView(contentView);
        }else {
            View inflate1 = UIUtils.inflate(R.layout.layout_default_empy_content);
            frameLayout.addView(inflate1);
        }
        return inflate;
    }

    /**
     * 关闭页面
     */
    protected abstract void close();
    /**
     * 制作界面
     *
     * @return
     */
    protected abstract View createSuccessView();


    /**
     * 重新加载
     */
    protected abstract void reLoading();

    /**
     * 失败的页面
     *
     * @return
     */
    public View createErrorView() {
         View inflate = UIUtils.inflate(R.layout.loadpage_error);
          TextView btn = inflate.findViewById(R.id.  tv_retry_loading_loadingpage_error);
        RelativeLayout headRootErrorView = inflate.findViewById(R.id.rl_headroot_loadpage_error);
        headRootErrorView.setVisibility(INVISIBLE);

        btn.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            showPagerView(Contans.STATE_LOADING);
              reLoading();
          }
          });


        ImageView ivbackError  = inflate.findViewById(R.id.iv_back_loadpage_error);
        ivbackError.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        return inflate;
    }
    /**
     * 正在旋转的页面
     *
     * @return
     */
    public View createLoadingView() {

            return UIUtils.inflate(R.layout.dialog_loading);
    }

}
