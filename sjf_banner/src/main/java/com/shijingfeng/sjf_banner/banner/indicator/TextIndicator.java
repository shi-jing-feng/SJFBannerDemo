package com.shijingfeng.sjf_banner.banner.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.shijingfeng.sjf_banner.banner.entity.TextIndicatorData;
import com.shijingfeng.sjf_banner.library.R;

/**
 * Function: 文本指示器
 * Author: ShiJingFeng
 * Date: 2020/1/4 13:24
 * Description:
 * @author ShiJingFeng
 */
@SuppressLint("ViewConstructor")
public class TextIndicator extends LinearLayout implements Indicator<TextIndicatorData> {

    private Context mContext;
    /** 当前显示的页面的真实位置 TextView */
    private TextView tvCurrent;
    /** 分隔符 TextView */
    private TextView tvSeparator;
    /** 页面总和真实数量 TextView */
    private TextView tvTotal;

    /** 配置数据 */
    private TextIndicatorData mData;
    /** 是否已经初始化 */
    private boolean mHasInited;

    public TextIndicator(Context context) {
        super(context);
        this.mContext = context;
        init(new TextIndicatorData());
    }

    public TextIndicator(@NonNull Context context, @NonNull TextIndicatorData data) {
        super(context);
        this.mContext = context;
        init(data);
    }

    /**
     * 创建 默认 文本指示器
     * @param context Context
     * @param data TextIndicatorData
     * @return TextIndicator
     */
    public static TextIndicator create(@NonNull Context context, @NonNull TextIndicatorData data) {
        return new TextIndicator(context, data);
    }

    /**
     * 初始化
     *
     * @param data 初始化需要的数据
     */
    @Override
    public void init(@NonNull TextIndicatorData data) {
        this.mData = data;
        if (data.totalRealCount <= 1) {
            setVisibility(GONE);
            return;
        }

        initView();
        initData();

        if (!mHasInited) {
            mHasInited = true;
        }
    }

    /**
     * 初始化View
     */
    private void initView() {
        if (!mHasInited) {
            //当前显示的页面的真实位置 TextView
            tvCurrent = new TextView(mContext);
            //分隔符 TextView
            tvSeparator = new TextView(mContext);
            //页面总和真实数量 TextView
            tvTotal = new TextView(mContext);

            addView(tvCurrent);
            addView(tvSeparator);
            addView(tvTotal);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        final FrameLayout.LayoutParams externalLayoutParams;

        if (getLayoutParams() != null) {
            externalLayoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        } else {
            externalLayoutParams = new FrameLayout.LayoutParams(mData.width, mData.height, mData.gravity);
        }
        externalLayoutParams.topMargin = mData.marginTop;
        externalLayoutParams.bottomMargin = mData.marginBottom;
        externalLayoutParams.setMarginStart(mData.marginStart);
        externalLayoutParams.setMarginEnd(mData.marginEnd);
        setLayoutParams(externalLayoutParams);
        setPadding(mData.paddingStart, mData.paddingTop, mData.paddingEnd, mData.paddingBottom);
        setBackground(mData.background == null ? ContextCompat.getDrawable(mContext, R.color.banner_transparency) : mData.background);
        setOrientation(HORIZONTAL);

        //当前显示的页面的真实位置 TextView
        tvCurrent.setText(String.valueOf(mData.curRealPosition + 1));
        tvCurrent.setTextColor(mData.curTextColor);
        tvCurrent.setTextSize(mData.curTextSize);
        //分隔符 TextView
        tvSeparator.setText(mData.separatorText);
        tvSeparator.setTextColor(mData.separatorTextColor);
        tvSeparator.setTextSize(mData.separatorTextSize);
        //页面总和真实数量 TextView
        tvTotal.setText(String.valueOf(mData.totalRealCount));
        tvTotal.setTextColor(mData.totalTextColor);
        tvTotal.setTextSize(mData.totalTextSize);
    }

    /**
     * 设置当前可见页面的 Position
     *
     * @param position 当前可见页面的Position
     */
    @Override
    public void setCurrent(final int position) {
        if (position + 1 > mData.totalRealCount) {
            return;
        }
        mData.curRealPosition = position;
        tvCurrent.setText(String.valueOf(position + 1));
    }

    /**
     * 获取数据
     */
    @Override
    public TextIndicatorData getData() {
        return mData;
    }

}
