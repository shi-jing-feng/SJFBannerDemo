package com.shijingfeng.sjf_banner.banner.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.shijingfeng.sjf_banner.banner.annotation.IndicatorType.INDICATOR_COMBINE;
import static com.shijingfeng.sjf_banner.banner.annotation.IndicatorType.INDICATOR_CUSTOM;
import static com.shijingfeng.sjf_banner.banner.annotation.IndicatorType.INDICATOR_SHAPE;
import static com.shijingfeng.sjf_banner.banner.annotation.IndicatorType.INDICATOR_TEXT;
import static com.shijingfeng.sjf_banner.banner.annotation.IndicatorType.INDICATOR_TITLE;

/**
 * Function: 指示器类型
 * Author: ShiJingFeng
 * Date: 2020/1/4 20:35
 * Description:
 * @author ShiJingFeng
 */
@IntDef(value = {INDICATOR_SHAPE, INDICATOR_TEXT, INDICATOR_TITLE, INDICATOR_COMBINE, INDICATOR_CUSTOM})
@Retention(RetentionPolicy.SOURCE)
public @interface IndicatorType {

    /** 指示器类型: 图形 */
    int INDICATOR_SHAPE = 1;
    /** 指示器类型: 文本 */
    int INDICATOR_TEXT = 2;
    /** 指示器类型: 标题 */
    int INDICATOR_TITLE = 3;
    /** 指示器类型: 组合 */
    int INDICATOR_COMBINE = 4;
    /** 指示器类型: 自定义 */
    int INDICATOR_CUSTOM = 5;

}
