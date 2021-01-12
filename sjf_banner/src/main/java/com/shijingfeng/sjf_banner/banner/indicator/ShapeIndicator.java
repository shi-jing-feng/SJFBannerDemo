package com.shijingfeng.sjf_banner.banner.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.shijingfeng.sjf_banner.banner.entity.ShapeIndicatorData;
import com.shijingfeng.sjf_banner.library.R;

/**
 * Function: 图形指示器
 * Author: ShiJingFeng
 * Date: 2020/1/4 15:16
 * Description:
 * @author ShiJingFeng
 */
@SuppressLint("ViewConstructor")
public class ShapeIndicator extends LinearLayout implements Indicator<ShapeIndicatorData> {

    private Context mContext;

    /** 配置数据 */
    private ShapeIndicatorData mData;
    /** 前一个可见页面位置 */
    private int mPrePosition = -1;
    /** 是否已经初始化 */
    private boolean mHasInited;

    public ShapeIndicator(@NonNull Context context, @NonNull ShapeIndicatorData data) {
        super(context);
        this.mContext = context;
        init(data);
    }

    /**
     * 创建 图形指示器
     * @param context Context
     * @param data TextIndicatorData
     * @return TextIndicator
     */
    public static ShapeIndicator create(@NonNull Context context, @NonNull ShapeIndicatorData data) {
        return new ShapeIndicator(context, data);
    }

    /**
     * 初始化
     *
     * @param data 初始化需要的数据
     */
    @Override
    public void init(@NonNull ShapeIndicatorData data) {
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
    private void initView() {}

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

        final int childCount = getChildCount();

        for (int i = 0; i < mData.totalRealCount; ++i) {
            final ImageView imageView;

            if (i < childCount) {
                //LinearLayout子View复用
                imageView = (ImageView) getChildAt(i);
            } else {
                imageView = new ImageView(mContext);
                addView(imageView);
            }

            final LinearLayout.LayoutParams internalLayoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();

            if (i == 0) {
                internalLayoutParams.setMarginStart(0);
            } else {
                internalLayoutParams.setMarginStart(mData.interval);
            }
            imageView.setLayoutParams(internalLayoutParams);
            if (i == mData.curRealPosition) {
                imageView.setImageDrawable(mData.focusDrawable == null ? ContextCompat.getDrawable(mContext, R.drawable.ic_dot_selected) : mData.focusDrawable);
            } else {
                imageView.setImageDrawable(mData.normalDrawable == null ? ContextCompat.getDrawable(mContext, R.drawable.ic_dot_unselected) : mData.normalDrawable);
            }
        }
    }

    /**
     * 设置当前可见页面的 Position
     *
     * @param position 当前可见页面的Position
     */
    @Override
    public void setCurrent(int position) {
        if (position >= mData.totalRealCount || position >= getChildCount()) {
            return;
        }
        if (mData.curRealPosition == position) {
            return;
        }
        mPrePosition = mData.curRealPosition;
        mData.curRealPosition = position;

        final ImageView ivPrevious = (ImageView) getChildAt(mPrePosition);
        final ImageView ivCurrent = (ImageView) getChildAt(mData.curRealPosition);

        ivPrevious.setImageDrawable(mData.normalDrawable == null ? ContextCompat.getDrawable(mContext, R.drawable.ic_dot_unselected) : mData.normalDrawable);
        ivCurrent.setImageDrawable(mData.focusDrawable == null ? ContextCompat.getDrawable(mContext, R.drawable.ic_dot_selected) : mData.focusDrawable);
    }

    /**
     * 获取数据
     */
    @Override
    public ShapeIndicatorData getData() {
        return mData;
    }

}
