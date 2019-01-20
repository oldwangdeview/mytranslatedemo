package translatedemo.com.translatedemo.factory;

import android.support.v4.app.Fragment;

import java.util.HashMap;

import translatedemo.com.translatedemo.fragment.CouponFragment1;
import translatedemo.com.translatedemo.fragment.CouponFragment2;
import translatedemo.com.translatedemo.fragment.CouponFragment3;
import translatedemo.com.translatedemo.fragment.InformationFragment;
import translatedemo.com.translatedemo.fragment.TranslateFragment;
import translatedemo.com.translatedemo.fragment.UserinfoFragment;

/**
 * Created by oldwang on 2019/1/7 0007.
 */

public class CouPonFragmentFactory {
    public static String TAG="MainFragmentFactory";
    private static final int COUPON1 = 0;
    private static final int COUPON2 = 1;
    private static final int COUPON3 = 2;

    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment=null;
        if (mMap.containsKey(index)){
            fragment=   mMap.get(index);
        }else {
            switch (index) {
                case COUPON1:
                    fragment = new CouponFragment1();
                    break;
                case COUPON2:
                    fragment = new CouponFragment2();
                    break;
                case COUPON3:
                    fragment = new CouponFragment3();
                    break;
            }

            mMap.put(index,fragment);

        }
        return fragment;

    }

}
