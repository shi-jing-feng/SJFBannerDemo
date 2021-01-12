package com.shijingfeng.sjf_banner.banner.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.shijingfeng.sjf_banner.banner.entity.TitleIndicatorData;
import com.shijingfeng.sjf_banner.library.R;

/**
 * Function: 标题文本指示器
 * Author: ShiJingFeng
 * Date: 2020/1/5 15:37
 * Description:
 */
@SuppressLint("ViewConstructor")
public class TitleIndicator extends AppCompatTextView implements Indicator<TitleIndicatorData> {

    private Context mContext;

    /** 配置数据 */
    private TitleIndicatorData mData;
    /** 是否已经初始化 */
    private boolean mHasInited;

    public TitleIndicator(Context context, @NonNull TitleIndicatorData data) {
        super(context);
        this.mContext = context;
        init(data);
    }

    /**
     * 创建 默认 图形指示器
     * @param context Context
     * @param data TextIndicatorData
     * @return TextIndicator
     */
    public static TitleIndicator create(@NonNull Context context, @NonNull TitleIndicatorData data) {
        return new TitleIndicator(context, data);
    }

    @Override
    public void init(@NonNull TitleIndicatorData data) {
        this.mData = data;
        final FrameLayout.LayoutParams externalLayoutParams;

        if (getLayoutParams() != null) {
            externalLayoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        } else {
            externalLayoutParams = new FrameLayout.LayoutParams(data.width, data.height, data.gravity);
        }
        externalLayoutParams.topMargin = data.marginTop;
        externalLayoutParams.bottomMargin = data.marginBottom;
        externalLayoutParams.setMarginStart(data.marginStart);
        externalLayoutParams.setMarginEnd(data.marginEnd);
        setLayoutParams(externalLayoutParams);
        setPadding(data.paddingStart, data.paddingTop, data.paddingEnd, data.paddingBottom);
        setBackground(data.background == null ? ContextCompat.getDrawable(mContext, R.color.banner_transparency) : data.background);

        if (data.titleTextList.size() > data.curRealPosition) {
            final String titleText = data.titleTextList.get(data.curRealPosition);

            if (titleText != null) {
                setText(titleText);
            } else {
                setText("");
            }
        }
        setTextColor(data.titleColor);
        setGravity(data.titleGravity);
        if (!mHasInited) {
            mHasInited = true;
        }
    }

    @Override
    public void setCurrent(int position) {
        if (position >= mData.totalRealCount) {
            return;
        }
        if (mData.curRealPosition == position) {
            return;
        }
        mData.curRealPosition = position;
        if (mData.titleTextList.size() > mData.curRealPosition) {
            final String titleText = mData.titleTextList.get(mData.curRealPosition);

            if (titleText != null) {
                setText(titleText);
            } else {
                setText("");
            }
        }
    }

    @Override
    public TitleIndicatorData getData() {
        return mData;
    }

}
