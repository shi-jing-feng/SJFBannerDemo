package com.shijingfeng.sjf_banner.banner.listener;

import androidx.annotation.Px;
import androidx.viewpager.widget.ViewPager;

/**
 * Function: ViewPager 页面改变监听器
 * Author: ShiJingFeng
 * Date: 2020/1/4 12:57
 * Description:
 */
public interface OnPageChangeListener {

    /**
     * 当页面滑动时执行, 手动滑动和程序滑动都会执行
     * @param position 当前显示的页面中第一个页面的位置, 如果 positionOffset 非零，则 页面位置+1 是可见的
     * @param positionOffset 范围: [0,1), 表示从页面位置开始的偏移量
     * @param positionOffsetPixels 像素值, 表示从 position 位置的偏移量
     */
    default void onPageScrolled(int position, float positionOffset, @Px int positionOffsetPixels) {}

    /**
     * 新页面被选择时执行, 不一定等到动画完成时才执行
     * @param prePosition 前一个被选择的页面位置 (小于 0 为没有)
     * @param curPosition 被选择的新页面位置
     */
    default void onPageSelected(int prePosition, int curPosition) {}

    /**
     * 当滑动状态改变时调用, 对于发现用户何时开始拖动非常有用, 自动安置和安置完成后静止也会调用
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE      静止状态
     * @see ViewPager#SCROLL_STATE_DRAGGING  拖动状态
     * @see ViewPager#SCROLL_STATE_SETTLING  被安置状态
     * 顺序: SCROLL_STATE_DRAGGING  SCROLL_STATE_SETTLING  SCROLL_STATE_IDLE
     */
    default void onPageScrollStateChanged(int state) {}

}
