package com.shijingfeng.library.banner.entity;

import com.shijingfeng.library.banner.annotation.IndicatorIntervalGravity;
import com.shijingfeng.library.banner.annotation.IndicatorType;
import com.shijingfeng.library.banner.indicator.Indicator;

import java.util.ArrayList;
import java.util.List;

import static com.shijingfeng.library.banner.annotation.IndicatorIntervalGravity.FIRST;
import static com.shijingfeng.library.banner.annotation.IndicatorIntervalGravity.SECOND;

/**
 * Function: 组合指示器数据
 * Author: ShiJingFeng
 * Date: 2020/1/5 14:47
 * Description:
 * @author ShiJingFeng
 */
public class CombineIndicatorData extends BaseIndicatorData<CombineIndicatorData> {

    /** 指示器列表 */
    public List<BaseIndicatorData> indicatorDataList = new ArrayList<>();

    public CombineIndicatorData setIndicatorDataList(List<BaseIndicatorData> indicatorDataList) {
        this.indicatorDataList = indicatorDataList;
        return this;
    }
}
