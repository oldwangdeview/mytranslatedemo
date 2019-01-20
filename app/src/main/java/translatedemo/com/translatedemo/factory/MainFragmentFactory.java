package translatedemo.com.translatedemo.factory;

import android.support.v4.app.Fragment;

import java.util.HashMap;

import translatedemo.com.translatedemo.fragment.InformationFragment;
import translatedemo.com.translatedemo.fragment.TranslateFagment2;
import translatedemo.com.translatedemo.fragment.TranslateFragment;
import translatedemo.com.translatedemo.fragment.UserinfoFragment;

/**
 * Created by oldwang on 2018/12/30 0030.
 */

public class MainFragmentFactory {

    public static String TAG="MainFragmentFactory";
    private static final int TRANSLATE_FRAGMENT = 0;
    private static final int INFOMATION_FRAGMENT = 1;
    private static final int USER_HOUSE = 2;
    private static final int TRANSLATE_FAGMENT2 = 3;

    private static HashMap<Integer,Fragment> mMap=new HashMap();

    public static Fragment getFragment(int index) {
        Fragment fragment=null;
        if (mMap.containsKey(index)){
            fragment=   mMap.get(index);
        }else {
            switch (index) {
                case TRANSLATE_FRAGMENT:
                    fragment = new TranslateFragment();
                    break;
                case INFOMATION_FRAGMENT:
                    fragment = new InformationFragment();
                    break;
                case USER_HOUSE:
                    fragment = new UserinfoFragment();
                    break;
                case 3:
                    fragment = new TranslateFagment2();
                    break;
            }

            mMap.put(index,fragment);

        }
        return fragment;

    }



}
