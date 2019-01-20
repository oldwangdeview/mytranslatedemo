package translatedemo.com.translatedemo.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author oldwang
 * Time  2017/11/30 14:05
 * Dest  禁止左右滑动
 */

public class ForbidScrollViewpager extends ViewPager {
    private boolean result=false;
    public ForbidScrollViewpager(@NonNull Context context) {
        super(context);
    }

    public ForbidScrollViewpager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (result)
            return super.onInterceptTouchEvent(arg0);
        else
            return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (result)
            return super.onTouchEvent(arg0);
        else
            return false;
    }



}
