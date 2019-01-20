package translatedemo.com.translatedemo.adpater;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import translatedemo.com.translatedemo.factory.CouPonFragmentFactory;

/**
 * Created by oldwang on 2019/1/7 0007.
 */

public class MyCouponPagerAdpater extends FragmentPagerAdapter {
    private String[] mMyOrderTitles;

    public MyCouponPagerAdpater(FragmentManager fm, String[] myOrderTitles) {
        super(fm);
        mMyOrderTitles = myOrderTitles;
    }
    @Override
    public Fragment getItem(int position) {
        return CouPonFragmentFactory.getFragment(position);
    }

    @Override
    public int getCount() {
        return mMyOrderTitles.length;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mMyOrderTitles[position];
    }


}
