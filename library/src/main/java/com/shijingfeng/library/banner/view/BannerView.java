package com.shijingfeng.library.banner.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.shijingfeng.library.R;
import com.shijingfeng.library.banner.entity.BaseIndicatorData;
import com.shijingfeng.library.banner.entity.CombineIndicatorData;
import com.shijingfeng.library.banner.entity.ShapeIndicatorData;
import com.shijingfeng.library.banner.entity.TextIndicatorData;
import com.shijingfeng.library.banner.entity.TitleIndicatorData;
import com.shijingfeng.library.banner.indicator.CombineIndicator;
import com.shijingfeng.library.banner.indicator.Indicator;
import com.shijingfeng.library.banner.indicator.ShapeIndicator;
import com.shijingfeng.library.banner.indicator.TextIndicator;
import com.shijingfeng.library.banner.indicator.TitleIndicator;
import com.shijingfeng.library.banner.listener.OnPageChangeListener;
import com.shijingfeng.library.util.CastUtil;
import com.shijingfeng.library.util.DensityUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_SETTLING;
import static com.shijingfeng.library.banner.annotation.IndicatorType.INDICATOR_COMBINE;
import static com.shijingfeng.library.banner.annotation.IndicatorType.INDICATOR_SHAPE;
import static com.shijingfeng.library.banner.annotation.IndicatorType.INDICATOR_TEXT;
import static com.shijingfeng.library.banner.annotation.IndicatorType.INDICATOR_TITLE;

/**
 * Function: 轮播图控件
 * Author: ShiJingFeng
 * Date: 2020/1/3 20:41
 * Description:
 * @author ShiJingFeng
 */
public class BannerView extends FrameLayout {

    /** 默认自动轮播间隔时间纳秒值 */
    private static final long DEFAULT_AUTO_CAROUSE_INTERVAL_NS = 2_000_000_000L;
    /** 默认BannerView宽度 (px) */
    private static final int DEFAULT_BANNER_VIEW_WIDTH = DensityUtil.dp2px(200F);
    /** 默认BannerView高度 (px) */
    private static final int DEFAULT_BANNER_VIEW_HEIGHT = DensityUtil.dp2px(200F);

    /** Context */
    private Context mContext;
    /** 轮播图 ViewPager */
    private BannerViewPager mViewPager;
    /** PagerAdapter */
    private PagerAdapter mPagerAdapter;
    /** 指示器数据 */
    private BaseIndicatorData mIndicatorData;
    /** BannerView内部 ViewPager OnPageChangeListener */
    private ViewPager.OnPageChangeListener mInternalOnPageChangeListener;
    /** ViewPager OnPageChangeListener */
    private OnPageChangeListener mOnPageChangeListener;
    /** ViewPager适配器数据观察者 */
    private DataSetObserver mPagerAdapterObserver;
    /** 参数 */
    private Attributes mAttributes = new Attributes();

    /** 是否暂停自动轮播  true:暂停 false:不暂停 */
    private volatile boolean mPauseAutoCarousel;
    /** 当前轮播图内下标 (包括左右为了循环滚动添加的 View 的 Position) */
    private int mCurrentBannerPosition = 1;
    /** 当前实际的Position (去除掉左右为了循环滚动添加的 View 的 Position) */
    private int mCurRealPosition = 0;
    /** 上一个实际的Position (去除掉左右为了循环滚动添加的 View 的 Position) (-1 代表没有) */
    private int mPreRealPosition = -1;
    /** 实际的页面数量 */
    private int mTotalRealCount = 0;
    /** 被矫正后执行自动切换ViewPager Item的纳秒时间戳 */
    private long mCorrectedExecStamp = 0L;
    /** 自动轮播 ScheduledExecutorService */
    private ScheduledExecutorService mAutoCarouselExecutor;
    /** 自动轮播Timer线程 对象锁 */
    private final Object object = new Object();

    public BannerView(@NonNull Context context) {
        super(context);
        this.mContext = context;
        init(null);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(attrs);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init(attrs);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        init(attrs);
    }

    /**
     * 初始化
     */
    private void init(@Nullable AttributeSet attrs) {
        //如果在RecyclerView中使用，为了防止数据错乱，最好不要在xml中设置属性
        if (attrs != null) {
            //初始化自定义属性
            final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BannerView);

            mAttributes.mScrollable = typedArray.getBoolean(R.styleable.BannerView_scrollable, true);
            mAttributes.mCarouselInterval = typedArray.getInteger(R.styleable.BannerView_carousel_interval, (int) (DEFAULT_AUTO_CAROUSE_INTERVAL_NS / 1_000_000L)) * 1_000_000L;
            typedArray.recycle();
        }

        //创建ViewPager
        mViewPager = new BannerViewPager(mContext);
        mViewPager.setId(R.id.vp_banner);
        mViewPager.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        mViewPager.setScrollable(mAttributes.mScrollable);
        //把ViewPager添加为第一个子View
        addView(mViewPager);
    }

    /**
     * 通过 ViewPager 的 Position 计算出真实的 Position (去除左右两个View后的)
     * @param position ViewPager 的 Position
     * @return 真实的 Position
     */
    private int getRealPositionByPosition(int position) {
        final int startPosition = 0;
        final int endPosition = mPagerAdapter.getCount() - 1;

        //根据轮播图 Position 计算当前实际的 Position
        if (mPagerAdapter.getCount() == 1) {
            return position;
        } else if (position == startPosition) {
            return endPosition - 2;
        } else if (position == endPosition) {
            return startPosition;
        } else {
            return position - 1;
        }
    }

    /**
     * 通过 真实的 Position (去除左右两个View后的) 计算出 ViewPager 的 Position
     * @param realPosition 真实的 Position
     * @return ViewPager 的 Position
     */
    private int getPositionByRealPosition(int realPosition) {
        if (mPagerAdapter.getCount() == 1) {
            return realPosition;
        } else {
            return realPosition + 1;
        }
    }

    /**
     * 设置 BannerView ViewPager 适配器
     * @param pagerAdapter BannerView ViewPager 适配器
     */
    public BannerView setPagerAdapter(@NonNull PagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
        //注册ViewPager适配器数据监听观察者
        mPagerAdapter.registerDataSetObserver(mPagerAdapterObserver = new DataSetObserver() {

            @Override
            public void onChanged() {
                super.onChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
            }
        });
        if (mPagerAdapter.getCount() <= 1) {
            mTotalRealCount = 1;
        } else {
            mTotalRealCount = mPagerAdapter.getCount() - 2;
        }
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(mInternalOnPageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(final int position) {
                final int preRealPosition;

                if (mPreRealPosition == -1) {
                    mPreRealPosition = mCurRealPosition;
                    preRealPosition = -1;
                } else {
                    mPreRealPosition = mCurRealPosition;
                    preRealPosition = mPreRealPosition;
                }
                mCurRealPosition = getRealPositionByPosition(position);
                mCurrentBannerPosition = position;

                final int childCount = getChildCount();

                for (int i = 0; i < childCount; ++i) {
                    final View child = getChildAt(i);

                    if (child instanceof Indicator) {
                        //更新指示器显示
                        ((Indicator) child).setCurrent(mCurRealPosition);
                    }
                }
                if (mOnPageChangeListener != null && preRealPosition != mCurRealPosition) {
                    mOnPageChangeListener.onPageSelected(preRealPosition, mCurRealPosition);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    //ViewPager Item正在被拖动
                    case SCROLL_STATE_DRAGGING:
                        //暂停自动轮播
                        pause();
                        break;
                    //ViewPager Item正在被安置 (自动滑动到置顶Item)
                    case SCROLL_STATE_SETTLING:
                        break;
                    //ViewPager Item安置完毕，滑动结束
                    case SCROLL_STATE_IDLE:
                        //ViewPager开始下标
                        final int startPosition = 0;
                        //ViewPager结束下标
                        final int endPosition = mPagerAdapter.getCount() - 1;

                        if (mCurrentBannerPosition == startPosition) {
                            //切换，不要动画效果
                            mViewPager.setCurrentItem(endPosition - 1, false);
                        } else if (mCurrentBannerPosition == endPosition) {
                            //切换，不要动画效果
                            mViewPager.setCurrentItem(startPosition + 1, false);
                        }
                        //唤醒自动轮播
                        resume();
                        break;
                    default:
                        break;
                }
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(mCurrentBannerPosition, false);
        return this;
    }

    /**
     * 添加 ViewPager OnPageChangeListener
     * @param onPageChangeListener ViewPager OnPageChangeListener
     * @return BannerView
     */
    public BannerView addOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        if (onPageChangeListener == null) {
            return this;
        }
        mOnPageChangeListener = onPageChangeListener;
        return this;
    }

    /**
     * 设置 ViewPager 当前指定下标的页面为前台显示页面
     * @param position ViewPager 当前选择的页面 Position
     * @return BannerView
     */
    public BannerView setCurrentItem(final int position) {
        mViewPager.setCurrentItem(position);
        return this;
    }

    /**
     * 设置 ViewPager 当前指定下标的页面为前台显示页面
     * @param position ViewPager 当前选择的页面 Position
     * @param smoothScroll 是否顺滑的滑到该项
     * @return BannerView
     */
    public BannerView setCurrentItem(final int position, boolean smoothScroll) {
        mViewPager.setCurrentItem(position, smoothScroll);
        return this;
    }

    /**
     * 设置 ViewPager 当前指定下标的页面为前台显示页面 (实际下标，去除左右两个View后计算后的下标)
     * @param realPosition ViewPager 当前选择的页面实际 Position
     * @return BannerView
     */
    public BannerView setCurrentRealItem(final int realPosition) {
        if (mPagerAdapter == null) {
            throw new IllegalArgumentException("请在 setCurrentRealItem(final int realPosition) 之前添加 PagerAdapter");
        }
        if (realPosition == mCurRealPosition) {
            return this;
        }
        mViewPager.setCurrentItem(getPositionByRealPosition(realPosition));
        return this;
    }

    /**
     * 设置 ViewPager 当前指定下标的页面为前台显示页面 (实际下标，去除左右两个View后计算后的下标)
     * @param realPosition ViewPager 当前选择的页面实际 Position
     * @param smoothScroll 是否顺滑的滑到该项
     * @return BannerView
     */
    public BannerView setCurrentRealItem(int realPosition, boolean smoothScroll) {
        if (mPagerAdapter == null) {
            throw new IllegalArgumentException("请在 setCurrentRealItem(final int realPosition) 之前添加 PagerAdapter");
        }
        if (realPosition == mCurRealPosition) {
            return this;
        }
        mViewPager.setCurrentItem(getPositionByRealPosition(realPosition), smoothScroll);
        return this;
    }

    /**
     * 设置 ViewPager 是否可滑动
     * @param scrollable ViewPager 是否可滑动
     * @return BannerView
     */
    public BannerView setScrollable(final boolean scrollable) {
        mViewPager.setScrollable(mAttributes.mScrollable = scrollable);
        return this;
    }

    /**
     * 设置 自动轮播 间隔 (毫秒)
     * @param carouselIntervalMs 自动轮播 间隔 (毫秒)
     * @return BannerView
     */
    public BannerView setCarouselInterval(final long carouselIntervalMs) {
        this.mAttributes.mCarouselInterval = carouselIntervalMs * 1_000_000L;
        return this;
    }

    /**
     * 设置指示器 (如果指示器为空，则代表没有指示器)
     * @param indicator 指示器
     * @return 自定义指示器
     */
    public BannerView setIndicator(@Nullable Indicator indicator) {
        if (indicator != null && !(indicator instanceof View)) {
            throw new IllegalArgumentException("指示器必须是一个View");
        }
        //移除之前添加的指示器
        for (int i = 0; i < getChildCount(); ++i) {
            final View view = getChildAt(i);

            if (view instanceof Indicator) {
                removeViewAt(i);
                break;
            }
        }
        if (indicator != null) {
            addView(CastUtil.cast(indicator));
        }
        return this;
    }

    @SuppressLint("SwitchIntDef")
    public BannerView setIndicator(@Nullable BaseIndicatorData indicatorData) {
        if (indicatorData == null) {
            //移除之前添加的指示器
            for (int i = 0; i < getChildCount(); ++i) {
                final View view = getChildAt(i);

                if (view instanceof Indicator) {
                    removeViewAt(i);
                    break;
                }
            }
            return this;
        }

        final int childCount = getChildCount();
        int indicatorIndex = 0;
        Indicator indicator = null;

        for (int i = 0; i < childCount; ++i) {
            final View view = getChildAt(i);

            if (view instanceof Indicator) {
                indicatorIndex = i;
                indicator = (Indicator) view;
                break;
            }
        }

        switch (indicatorData.getIndicatorType()) {
            //图形指示器
            case INDICATOR_SHAPE:
                if (indicator == null) {
                    indicator = ShapeIndicator.create(mContext, (ShapeIndicatorData) indicatorData);
                    addView((View) indicator);
                } else if (indicator.getIndicatorType() != INDICATOR_SHAPE) {
                    removeViewAt(indicatorIndex);
                    indicator = ShapeIndicator.create(mContext, (ShapeIndicatorData) indicatorData);
                    addView((View) indicator);
                } else {
                    final Indicator<ShapeIndicatorData> shapeIndicator = CastUtil.cast(indicator);

                    shapeIndicator.init((ShapeIndicatorData) indicatorData);
                }
                break;
            //文本指示器
            case INDICATOR_TEXT:
                if (indicator == null) {
                    indicator = TextIndicator.create(mContext, (TextIndicatorData) indicatorData);
                    addView((View) indicator);
                } else if (indicator.getIndicatorType() != INDICATOR_TEXT) {
                    removeViewAt(indicatorIndex);
                    indicator = TextIndicator.create(mContext, (TextIndicatorData) indicatorData);
                    addView((View) indicator);
                } else {
                    final Indicator<TextIndicatorData> textIndicator = CastUtil.cast(indicator);

                    textIndicator.init((TextIndicatorData) indicatorData);
                }
                break;
            //标题指示器
            case INDICATOR_TITLE:
                if (indicator == null) {
                    indicator = TitleIndicator.create(mContext, (TitleIndicatorData) indicatorData);
                    addView((View) indicator);
                } else if (indicator.getIndicatorType() != INDICATOR_TITLE) {
                    removeViewAt(indicatorIndex);
                    indicator = TitleIndicator.create(mContext, (TitleIndicatorData) indicatorData);
                    addView((View) indicator);
                } else {
                    final Indicator<TitleIndicatorData> titleIndicator = CastUtil.cast(indicator);

                    titleIndicator.init((TitleIndicatorData) indicatorData);
                }
                break;
            //组合指示器
            case INDICATOR_COMBINE:
                if (indicator == null) {
                    indicator = CombineIndicator.create(mContext, (CombineIndicatorData) indicatorData);
                    addView((View) indicator);
                } else if (indicator.getIndicatorType() != INDICATOR_COMBINE) {
                    removeViewAt(indicatorIndex);
                    indicator = CombineIndicator.create(mContext, (CombineIndicatorData) indicatorData);
                    addView((View) indicator);
                } else {
                    final Indicator<CombineIndicatorData> combineIndicator = CastUtil.cast(indicator);

                    combineIndicator.init((CombineIndicatorData) indicatorData);
                }
                break;
            default:
                break;
        }
        return this;
    }

    /**
     * 获取 ViewPager 当前前台显示页面的下标
     * @return ViewPager 当前前台显示页面的下标
     */
    public int getCurrentItem() {
        return mCurrentBannerPosition;
    }

    /**
     * 获取 ViewPager 当前前台显示页面的下标 (实际下标，去除左右两个View后计算后的下标)
     * @return ViewPager 当前前台显示页面的下标 (实际下标，去除左右两个View后计算后的下标)
     */
    public int getCurrentRealItem() {
        return mCurRealPosition;
    }

    /**
     * 获得真实总页面数量
     * @return 真实总页面数量
     */
    public int getTotalRealCount() {
        return mTotalRealCount;
    }

    /**
     * ViewPager 是否可滑动
     * @return true:可滑动  false:不可滑动
     */
    public boolean isScrollable() {
        return mAttributes.mScrollable;
    }

    /**
     * 获取 自动轮播 间隔 (毫秒)
     * @return 自动轮播 间隔 (毫秒)
     */
    public long getCarouseInterval() {
        return mAttributes.mCarouselInterval / 1_000_000L;
    }

    /**
     * 获取 BannerView ViewPager
     * @return BannerView ViewPager
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 开始自动轮播
     */
    public void start() {
        if (mPagerAdapter == null) {
            throw new IllegalArgumentException("请在 start() 之前添加 PagerAdapter");
        }
        if (mAttributes.mCarouselInterval > 0 && mPagerAdapter.getCount() > 1) {
            if (mAutoCarouselExecutor != null) {
                mAutoCarouselExecutor.shutdownNow();
                mAutoCarouselExecutor = null;
            }

            mAutoCarouselExecutor = Executors.newScheduledThreadPool(1);
            mAutoCarouselExecutor.scheduleAtFixedRate(() -> {
                synchronized (object) {
                    if (Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    if (mPauseAutoCarousel) {
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (System.nanoTime() >= mCorrectedExecStamp) {
                            new Handler(Looper.getMainLooper()).post(() -> mViewPager.setCurrentItem(mCurrentBannerPosition + 1, true));
                        }
                    }
                }
            }, mAttributes.mCarouselInterval, mAttributes.mCarouselInterval, TimeUnit.NANOSECONDS);
        }
    }

    /**
     * 暂停自动轮播
     */
    public void pause() {
        if (mAttributes.mCarouselInterval > 0) {
            //暂停自动轮播
            mPauseAutoCarousel = true;
            //自动轮播时间矫正
            mCorrectedExecStamp = System.nanoTime() + mAttributes.mCarouselInterval;
        }
    }

    /**
     * 恢复自动轮播
     */
    public void resume() {
        if (mAttributes.mCarouselInterval > 0) {
            //自动轮播时间矫正
            mCorrectedExecStamp = System.nanoTime() + mAttributes.mCarouselInterval;
            //唤醒自动轮播
            mPauseAutoCarousel = false;
            synchronized (object) {
                object.notify();
            }
        }
    }

    /**
     * 销毁BannerView
     */
    public void destroy() {
        //关闭自动轮播
        if (mAutoCarouselExecutor != null) {
            mAutoCarouselExecutor.shutdownNow();
            mAutoCarouselExecutor = null;
        }
        //移除监听器 和 恢复初始值
        if (mPagerAdapter != null) {
            if (mPagerAdapterObserver != null) {
                mPagerAdapter.unregisterDataSetObserver(mPagerAdapterObserver);
                mPagerAdapterObserver = null;
            }
            mViewPager.setAdapter(null);
            mPagerAdapter = null;
        }
        if (mInternalOnPageChangeListener != null) {
            mViewPager.removeOnPageChangeListener(mInternalOnPageChangeListener);
            mInternalOnPageChangeListener = null;
        }
        mOnPageChangeListener = null;
        mPauseAutoCarousel = false;
        mCurrentBannerPosition = 1;
        mCurRealPosition = 0;
        mPreRealPosition = -1;
        mCorrectedExecStamp = 0L;
    }

    /**
     * 注意: 如果是RecyclerView调用则该View不会销毁 (RecyclerView有复用机制，会把此View保存到Recycler中),
     * 为了防止数据错乱，则需要重置数据
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroy();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        final int width;
        final int height;
        final View childView = getChildAt(0);
        final int cWidth = childView.getMeasuredWidth();
        final int cHeight = childView.getMeasuredHeight();

        //如果是 wrap_content 设置为我们计算的值, 否则直接设置为父容器计算的值
        if (widthMode == MeasureSpec.EXACTLY) {
            width = cWidth;
        } else {
            width = DEFAULT_BANNER_VIEW_WIDTH;
        }
        //如果是 wrap_content 设置为我们计算的值, 否则直接设置为父容器计算的值
        if (heightMode == MeasureSpec.EXACTLY) {
            height = cHeight;
        } else {
            height = DEFAULT_BANNER_VIEW_HEIGHT;
        }
        setMeasuredDimension(width, height);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (mAttributes.mScrollable) {
//            requestParentDisallowInterceptTouchEvent(true);
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (mAttributes.mScrollable) {
//            return false;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    /**
//     * 请求父容器不中断事件 (作用于父容器的 dispatchTouchEvent() 函数)
//     * @param disallowIntercept  true 不允许中断  false 允许中断
//     */
//    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
//        final ViewParent parent = getParent();
//
//        if (parent != null) {
//            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
//        }
//    }

    /**
     * 参数
     */
    private static class Attributes {

        /** 是否可以手动滑动   true:可以手动滑动  false:禁止手动滑动 */
        private boolean mScrollable = true;
        /** 自动轮播间隔(纳秒) 注意：小于等于 0 时禁止自动轮播 */
        private long mCarouselInterval = DEFAULT_AUTO_CAROUSE_INTERVAL_NS;

        /**
         * 重置数据
         */
        private void reset() {
            this.mScrollable = true;
            this.mCarouselInterval = DEFAULT_AUTO_CAROUSE_INTERVAL_NS;
        }

    }

}
