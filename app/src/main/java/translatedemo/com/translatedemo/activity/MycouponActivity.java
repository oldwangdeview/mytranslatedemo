package translatedemo.com.translatedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import translatedemo.com.translatedemo.R;
import translatedemo.com.translatedemo.adpater.MyCouponPagerAdpater;
import translatedemo.com.translatedemo.base.BaseActivity;
import translatedemo.com.translatedemo.util.UIUtils;
import translatedemo.com.translatedemo.view.ForbidScrollViewpager;

/**
 * Created by oldwang on 2019/1/7 0007.
 * 我的优惠券
 */

public class MycouponActivity extends BaseActivity {
    @BindView(R.id.iv_back_activity_text)
    TextView iv_back_activity_text;
    @BindView(R.id.tv_title_activity_baseperson)
    TextView title_name;
    @BindView(R.id.tv_small_title_layout_head)
    TextView tv_small_title_layout_head;
    @BindArray(R.array.mycoupontitle)
    String[] myOrderTitles;
    @BindView(R.id.tablayout_activity_my_order)
    TabLayout mTablayoutActivityMyOrder;
    @BindView(R.id.forbidScroll_viewpager_activity_my_order)
    ForbidScrollViewpager mForbidScrollViewpagerActivityMyOrder;

    private int mPositon = 0;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_mycoupon);
    }
    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showFullScreen(MycouponActivity.this,false);
        updateactionbar();
    }

    @Override
    protected void initData() {
        super.initData();
        iv_back_activity_text.setVisibility(View.VISIBLE);
        tv_small_title_layout_head.setVisibility(View.VISIBLE);
        tv_small_title_layout_head.setTextColor(this.getResources().getColor(R.color.c_e94950));
        title_name.setText(this.getResources().getString(R.string.mycoupon_text_titlename));
        tv_small_title_layout_head.setText(this.getResources().getString(R.string.mycoupon_text_getcoupon));

        mForbidScrollViewpagerActivityMyOrder.setOffscreenPageLimit(myOrderTitles.length);
        MyCouponPagerAdpater madpater = new MyCouponPagerAdpater(getSupportFragmentManager(),myOrderTitles);
        mForbidScrollViewpagerActivityMyOrder.setAdapter(madpater);
        mTablayoutActivityMyOrder.setupWithViewPager(mForbidScrollViewpagerActivityMyOrder);
        mTablayoutActivityMyOrder.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //切换ViewPager
                mForbidScrollViewpagerActivityMyOrder.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mTablayoutActivityMyOrder.getTabAt(mPositon).select();
    }

    /**
     * 获取优惠券
     */
    @OnClick(R.id.tv_small_title_layout_head)
    public void gotogetcoupon(){
        GetCouponActivty.startactivity(this);
    }

    public static void startactivity(Context mContext){
        Intent mIntent = new Intent(mContext,MycouponActivity.class);
        mContext.startActivity(mIntent);
    }
    @OnClick({R.id.iv_back_activity_text,R.id.iv_back_activity_basepersoninfo})
    public void finishactivity(){
        finish();
    }
}
