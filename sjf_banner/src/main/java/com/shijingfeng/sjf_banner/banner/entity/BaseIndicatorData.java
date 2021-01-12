package com.shijingfeng.sjf_banner.banner.entity;

import android.graphics.drawable.Drawable;
import android.view.Gravity;

import com.shijingfeng.sjf_banner.banner.annotation.IndicatorType;
import com.shijingfeng.sjf_banner.util.CastUtil;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Function: 提示View数据 基类
 * Author: ShiJingFeng
 * Date: 2020/1/4 12:45
 * Description:
 * @author ShiJingFeng
 */
public abstract class BaseIndicatorData<T extends BaseIndicatorData> {

    /** 当前显示的页面位置 (去除左右两个占位View计算后的位置) */
    public int curRealPosition = 0;
    /** 页面总数量 (去除左右两个占位View的总数量) */
    public int totalRealCount = 0;
    /** 指示器宽 */
    public int width = WRAP_CONTENT;
    /** 指示器高 */
    public int height = WRAP_CONTENT;
    /** 指示器 所处的外部位置 */
    public int gravity = Gravity.CENTER;
    /** 顶部 Margin */
    public int marginTop = 0;
    /** 底部 Margin */
    public int marginBottom = 0;
    /** 左部 Margin */
    public int marginStart = 0;
    /** 右部 Margin */
    public int marginEnd = 0;
    /** 顶部 Padding */
    public int paddingTop = 0;
    /** 底部 Padding */
    public int paddingBottom = 0;
    /** 左部 Padding */
    public int paddingStart = 0;
    /** 右部 Padding */
    public int paddingEnd = 0;
    /** 背景 默认透明 */
    public Drawable background;

    public final T setCurRealPosition(int curRealPosition) {
        this.curRealPosition = curRealPosition;
        return CastUtil.cast(this);
    }

    public final T setTotalRealCount(int totalRealCount) {
        this.totalRealCount = totalRealCount;
        return CastUtil.cast(this);
    }

    public final T setWidth(int width) {
        this.width = width;
        return CastUtil.cast(this);
    }

    public final T setHeight(int height) {
        this.height = height;
        return CastUtil.cast(this);
    }

    public final T setGravity(int gravity) {
        this.gravity = gravity;
        return CastUtil.cast(this);
    }

    public final T setMarginTop(int marginTop) {
        this.marginTop = marginTop;
        return CastUtil.cast(this);
    }

    public final T setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
        return CastUtil.cast(this);
    }

    public final T setMarginStart(int marginStart) {
        this.marginStart = marginStart;
        return CastUtil.cast(this);
    }

    public final T setMarginEnd(int marginEnd) {
        this.marginEnd = marginEnd;
        return CastUtil.cast(this);
    }

    public final T setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
        return CastUtil.cast(this);
    }

    public final T setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
        return CastUtil.cast(this);
    }

    public final T setPaddingStart(int paddingStart) {
        this.paddingStart = paddingStart;
        return CastUtil.cast(this);
    }

    public final T setPaddingEnd(int paddingEnd) {
        this.paddingEnd = paddingEnd;
        return CastUtil.cast(this);
    }

    public final T setBackground(Drawable background) {
        this.background = background;
        return CastUtil.cast(this);
    }

    /**
     * 获取指示器类型
     * @return 指示器类型
     */
    public final @IndicatorType int getIndicatorType() {
        if (this instanceof ShapeIndicatorData) {
            return IndicatorType.INDICATOR_SHAPE;
        } else if (this instanceof TextIndicatorData) {
            return IndicatorType.INDICATOR_TEXT;
        } else if (this instanceof TitleIndicatorData) {
            return IndicatorType.INDICATOR_TITLE;
        } else if (this instanceof CombineIndicatorData) {
            return IndicatorType.INDICATOR_COMBINE;
        } else {
            return IndicatorType.INDICATOR_CUSTOM;
        }
    }

}
