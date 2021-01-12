package com.shijingfeng.sjf_banner.banner.indicator;

import androidx.annotation.NonNull;

import com.shijingfeng.sjf_banner.banner.entity.BaseIndicatorData;
import com.shijingfeng.sjf_banner.banner.annotation.IndicatorType;

/**
 * Function: 指示器 基类
 * Author: ShiJingFeng
 * Date: 2020/1/4 12:41
 * Description:
 * @author ShiJingFeng
 */
public interface Indicator<T extends BaseIndicatorData> {

    /**
     * 初始化
     * @param data 初始化需要的数据
     */
    void init(@NonNull T data);

    /**
     * 设置当前可见页面的 Position
     * @param position 当前可见页面的Position
     */
    void setCurrent(final int position);

    /**
     * 获取数据
     * @return T
     */
    T getData();

    /**
     * 获取指示器类型
     * @return 指示器类型
     */
    default @IndicatorType int getIndicatorType() {
        if (this instanceof ShapeIndicator) {
            return IndicatorType.INDICATOR_SHAPE;
        } else if (this instanceof TextIndicator) {
            return IndicatorType.INDICATOR_TEXT;
        } else if (this instanceof TitleIndicator) {
            return IndicatorType.INDICATOR_TITLE;
        } else if (this instanceof CombineIndicator) {
            return IndicatorType.INDICATOR_COMBINE;
        } else {
            return IndicatorType.INDICATOR_CUSTOM;
        }
    }

}
