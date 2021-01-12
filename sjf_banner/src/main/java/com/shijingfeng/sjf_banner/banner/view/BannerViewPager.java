package com.shijingfeng.sjf_banner.banner.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Function: 轮播图 ViewPager
 * Author: ShiJingFeng
 * Date: 2020/1/3 20:42
 * Description:
 * @author ShiJingFeng
 */
public class BannerViewPager extends ViewPager {

    private boolean mScrollable = true;

    public BannerViewPager(@NonNull Context context) {
        super(context);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mScrollable) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mScrollable) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

//    /**
//     * 解决手动动态创建 ViewPager wrap_content 和 match_parent 无效问题
//     * @param widthMeasureSpec WidthMeasureSpec
//     * @param heightMeasureSpec HeightMeasureSpec
//     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        int height = 0;
//
//        for(int i = 0; i < getChildCount(); i++) {
//            final View child = getChildAt(i);
//
//            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//
//            final int h = child.getMeasuredHeight();
//
//            if(h > height) {
//                height = h;
//            }
//        }
//
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    /**
     * 设置是否可滑动
     *
     * @param scrollable true:可滑动  false:不可滑动
     */
    public void setScrollable(boolean scrollable) {
        this.mScrollable = scrollable;
    }
}
