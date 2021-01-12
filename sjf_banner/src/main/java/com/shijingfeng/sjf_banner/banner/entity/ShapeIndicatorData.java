package com.shijingfeng.sjf_banner.banner.entity;

import android.graphics.drawable.Drawable;

import com.shijingfeng.sjf_banner.util.DensityUtil;

/**
 * Function: 图形指示器 实体类
 * Author: ShiJingFeng
 * Date: 2020/1/4 12:48
 * Description:
 * @author ShiJingFeng
 */
public class ShapeIndicatorData extends BaseIndicatorData<ShapeIndicatorData> {

    /** 高亮 Drawable */
    public Drawable focusDrawable;
    /** 普通 Drawable */
    public Drawable normalDrawable;
    /** 间隔距离 (px) */
    public int interval = DensityUtil.dp2px(5);

    public final ShapeIndicatorData setFocusDrawable(Drawable focusDrawable) {
        this.focusDrawable = focusDrawable;
        return this;
    }

    public final ShapeIndicatorData setNormalDrawable(Drawable normalDrawable) {
        this.normalDrawable = normalDrawable;
        return this;
    }

    public final ShapeIndicatorData setInterval(int interval) {
        this.interval = interval;
        return this;
    }
}
