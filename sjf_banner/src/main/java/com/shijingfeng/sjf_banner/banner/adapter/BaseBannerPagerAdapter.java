package com.shijingfeng.sjf_banner.banner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.shijingfeng.sjf_banner.banner.listener.OnItemEventListener;

import java.util.List;

/**
 * Function: 轮播图 ViewPager适配器 基类
 * Date: 2020/1/7 13:35
 * Description:
 *
 * @author ShiJingFeng
 */
public abstract class BaseBannerPagerAdapter<T> extends PagerAdapter {

    protected Context mContext;
    protected List<T> mDataList;
    protected OnItemEventListener mOnItemEventListener;

    public BaseBannerPagerAdapter(@NonNull Context context, List<T> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        if (mDataList == null) {
            return 0;
        } else if (mDataList.size() == 1) {
            return 1;
        }
        return mDataList.size() + 2;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /**
     * 通过 ViewPager 的 Position 计算出真实的 Position (去除左右两个View后的)
     * @param position ViewPager 的 Position
     * @return 真实的 Position
     */
    protected int getRealPositionByPosition(int position) {
        final int startPosition = 0;
        final int endPosition = getCount() - 1;

        //根据轮播图 Position 计算当前实际的 Position
        if (getCount() == 1) {
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
     * 通过 Position 获取 数据
     * @param position Position
     * @return 数据
     */
    protected T getDataByPosition(int position) {
        final T data;

        if (getCount() == 1) {
            data = mDataList.get(0);
        } else {
            if (position == 0) {
                data = mDataList.get(mDataList.size() - 1);
            } else if (position == getCount() - 1) {
                data = mDataList.get(0);
            } else {
                data = mDataList.get(position - 1);
            }
        }

        return data;
    }

    /**
     * 设置事件监听器
     * @param listener 事件监听器
     */
    public void setOnItemEventListener(@Nullable OnItemEventListener listener) {
        this.mOnItemEventListener = listener;
    }

}
