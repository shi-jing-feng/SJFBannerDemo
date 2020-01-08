package com.shijingfeng.library.banner.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.shijingfeng.library.banner.annotation.IndicatorIntervalGravity.FIRST;
import static com.shijingfeng.library.banner.annotation.IndicatorIntervalGravity.SECOND;

/**
 * Function: 指示器内部位置顺序
 * Author: ShiJingFeng
 * Date: 2020/1/5 15:07
 * Description:
 * @author ShiJingFeng
 */
@IntDef(value = {FIRST, SECOND})
@Retention(RetentionPolicy.SOURCE)
public @interface IndicatorIntervalGravity {

    /** 第一位 */
    int FIRST = 0;
    /** 第二位 */
    int SECOND = 1;

}
