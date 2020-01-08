package com.shijingfeng.library.banner.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.shijingfeng.library.R;
import com.shijingfeng.library.banner.entity.BaseIndicatorData;
import com.shijingfeng.library.banner.entity.CombineIndicatorData;
import com.shijingfeng.library.banner.entity.ShapeIndicatorData;
import com.shijingfeng.library.banner.entity.TextIndicatorData;
import com.shijingfeng.library.banner.entity.TitleIndicatorData;
import com.shijingfeng.library.util.CastUtil;

import java.util.List;

import static com.shijingfeng.library.banner.annotation.IndicatorType.INDICATOR_COMBINE;
import static com.shijingfeng.library.banner.annotation.IndicatorType.INDICATOR_CUSTOM;
import static com.shijingfeng.library.banner.annotation.IndicatorType.INDICATOR_SHAPE;
import static com.shijingfeng.library.banner.annotation.IndicatorType.INDICATOR_TEXT;
import static com.shijingfeng.library.banner.annotation.IndicatorType.INDICATOR_TITLE;

/**
 * Function:
 * Date: 2020/1/5 15:13
 * Description:
 * @author ShiJingFeng
 */
@SuppressLint("ViewConstructor")
public class CombineIndicator extends FrameLayout implements Indicator<CombineIndicatorData> {

    private Context mContext;

    /** 配置数据 */
    private CombineIndicatorData mData;
    /** 是否已经初始化 */
    private boolean mHasInited;

    public CombineIndicator(@NonNull Context context, @NonNull CombineIndicatorData data) {
        super(context);
        this.mContext = context;
        this.mData = data;
        init(data);
    }

    /**
     * 创建 默认 组合指示器
     * @param context Context
     * @param data TextIndicatorData
     * @return TextIndicator
     */
    public static CombineIndicator create(@NonNull Context context, @NonNull CombineIndicatorData data) {
        return new CombineIndicator(context, data);
    }

    /**
     * 初始化数据
     */
    @SuppressLint("SwitchIntDef")
    @Override
    public void init(@NonNull CombineIndicatorData data) {
        final FrameLayout.LayoutParams externalLayoutParams;

        if (getLayoutParams() != null) {
            externalLayoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        } else {
            externalLayoutParams = new FrameLayout.LayoutParams(mData.width, mData.height, mData.gravity);
        }
        externalLayoutParams.topMargin = data.marginTop;
        externalLayoutParams.bottomMargin = data.marginBottom;
        externalLayoutParams.setMarginStart(data.marginStart);
        externalLayoutParams.setMarginEnd(data.marginEnd);
        setLayoutParams(externalLayoutParams);
        setPadding(data.paddingStart, data.paddingTop, data.paddingEnd, data.paddingBottom);
        setBackground(data.background == null ? ContextCompat.getDrawable(mContext, R.color.transparency) : data.background);
//        setOrientation(HORIZONTAL);

        final List<BaseIndicatorData> indicatorDataList = data.indicatorDataList;
        final int size = indicatorDataList.size();
        final int childCount = getChildCount();

        for (int i = 0; i < size; ++i) {
            final BaseIndicatorData indicatorData = indicatorDataList.get(i);
            Indicator indicator = null;

            if (indicatorData.getIndicatorType() == INDICATOR_CUSTOM) {
                throw new IllegalArgumentException("组合指示器内不支持自定义指示器");
            }

            if (i < childCount) {
                indicator = (Indicator) getChildAt(i);

                if (indicator.getIndicatorType() == indicatorData.getIndicatorType()) {
                    //存在是同一种Indicator, 则复用之
                    switch (indicator.getIndicatorType()) {
                        //图形指示器
                        case INDICATOR_SHAPE:
                            final Indicator<ShapeIndicatorData> shapeIndicator = CastUtil.cast(indicator);

                            shapeIndicator.init((ShapeIndicatorData) indicatorData);
                            break;
                        //文本指示器
                        case INDICATOR_TEXT:
                            final Indicator<TextIndicatorData> textIndicator = CastUtil.cast(indicator);

                            textIndicator.init((TextIndicatorData) indicatorData);
                            break;
                        //标题指示器
                        case INDICATOR_TITLE:
                            final Indicator<TitleIndicatorData> titleIndicator = CastUtil.cast(indicator);

                            titleIndicator.init((TitleIndicatorData) indicatorData);
                            break;
                        //组合指示器
                        case INDICATOR_COMBINE:
                            final Indicator<CombineIndicatorData> combineIndicator = CastUtil.cast(indicator);

                            combineIndicator.init((CombineIndicatorData) indicatorData);
                            break;
                        default:
                            break;
                    }
                    return;
                }
                removeViewAt(i);
            }
            switch (indicatorData.getIndicatorType()) {
                //图形指示器
                case INDICATOR_SHAPE:
                    indicator = ShapeIndicator.create(mContext, (ShapeIndicatorData) indicatorData);
                    break;
                //文本指示器
                case INDICATOR_TEXT:
                    indicator = TextIndicator.create(mContext, (TextIndicatorData) indicatorData);
                    break;
                //标题指示器
                case INDICATOR_TITLE:
                    indicator = TitleIndicator.create(mContext, (TitleIndicatorData) indicatorData);
                    break;
                //组合指示器
                case INDICATOR_COMBINE:
                    indicator = CombineIndicator.create(mContext, (CombineIndicatorData) indicatorData);
                    break;
                default:
                    break;
            }
            if (indicator != null) {
                addView((View) indicator);
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
        final int childCount = getChildCount();

        for (int i = 0; i < childCount; ++i) {
            final Indicator indicator = (Indicator) getChildAt(i);

            indicator.setCurrent(position);
        }
    }

    /**
     * 获取数据
     */
    @Override
    public CombineIndicatorData getData() {
        return mData;
    }
}
