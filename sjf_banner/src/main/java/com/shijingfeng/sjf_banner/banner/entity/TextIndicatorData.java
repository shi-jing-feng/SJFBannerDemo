package com.shijingfeng.sjf_banner.banner.entity;

import android.graphics.Color;

import androidx.annotation.ColorInt;

/**
 * Function: 文本指示器 数据实体类
 * Author: ShiJingFeng
 * Date: 2020/1/4 12:46
 * Description:
 * @author ShiJingFeng
 */
public class TextIndicatorData extends BaseIndicatorData<TextIndicatorData> {

    /** 当前页面位置 文本大小 (sp) */
    public int curTextSize = 14;
    /** 总页面数量 文本大小 (sp) */
    public int totalTextSize = 14;
    /** 当前页面位置 文本颜色 */
    public @ColorInt int curTextColor = Color.parseColor("#FFFFFF");
    /** 总页面数量 文本颜色 */
    public @ColorInt int totalTextColor = Color.parseColor("#FFFFFF");
    /** 分隔符 文本 */
    public String separatorText = "/";
    /** 分隔符 文本大小 (sp) */
    public int separatorTextSize = 14;
    /** 分隔符 文本颜色 */
    public @ColorInt int separatorTextColor = Color.parseColor("#FFFFFF");

    public TextIndicatorData setCurTextSize(int curTextSize) {
        this.curTextSize = curTextSize;
        return this;
    }

    public TextIndicatorData setTotalTextSize(int totalTextSize) {
        this.totalTextSize = totalTextSize;
        return this;
    }

    public TextIndicatorData setCurTextColor(int curTextColor) {
        this.curTextColor = curTextColor;
        return this;
    }

    public TextIndicatorData setTotalTextColor(int totalTextColor) {
        this.totalTextColor = totalTextColor;
        return this;
    }

    public TextIndicatorData setSeparatorText(String separatorText) {
        this.separatorText = separatorText;
        return this;
    }

    public TextIndicatorData setSeparatorTextSize(int separatorTextSize) {
        this.separatorTextSize = separatorTextSize;
        return this;
    }

    public TextIndicatorData setSeparatorTextColor(int separatorTextColor) {
        this.separatorTextColor = separatorTextColor;
        return this;
    }
}
