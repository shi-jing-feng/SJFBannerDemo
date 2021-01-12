package com.shijingfeng.sjf_banner.banner.entity;

import android.graphics.Color;
import android.view.Gravity;

import androidx.annotation.ColorInt;

import java.util.ArrayList;
import java.util.List;

/**
 * Function: 标题指示器数据
 * Author: ShiJingFeng
 * Date: 2020/1/5 15:32
 * Description:
 * @author ShiJingFeng
 */
public class TitleIndicatorData extends BaseIndicatorData<TitleIndicatorData> {

    /** 标题文本列表 */
    public List<String> titleTextList = new ArrayList<>();
    /** 标题大小 (SP) */
    public int titleSize = 18;
    /** 标题颜色 */
    public @ColorInt int titleColor = Color.parseColor("#FFFFFF");
    /** 标题内部位置 */
    public int titleGravity = Gravity.CENTER;

    public final TitleIndicatorData setTitleTextList(List<String> titleTextList) {
        this.titleTextList = titleTextList;
        return this;
    }

    public final TitleIndicatorData setTitleSize(int titleSize) {
        this.titleSize = titleSize;
        return this;
    }

    public final TitleIndicatorData setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public final TitleIndicatorData setTitleGravity(int titleGravity) {
        this.titleGravity = titleGravity;
        return this;
    }
}
