package com.shijingfeng.sjfbanner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.shijingfeng.sjfbanner.entity.BannerEntity;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Function:
 * Author: ShiJingFeng
 * Date: 2020/1/5 10:48
 * Description:
 * @author ShiJingFeng
 */
public class BannerPagerAdapter2 extends PagerAdapter {

    private Context mContext;
    private List<View> mViewList;

    public BannerPagerAdapter2(@NonNull Context context, @NonNull List<View> viewList) {
        this.mContext = context;
        this.mViewList = viewList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final View view = mViewList.get(position);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    @Override
    public int getCount() {
        if (mViewList == null) {
            return 0;
        } else {
            return mViewList.size();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
