package com.shijingfeng.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.shijingfeng.app.R;
import com.shijingfeng.app.entity.BannerEntity;
import com.shijingfeng.sjf_banner.banner.entity.BaseIndicatorData;
import com.shijingfeng.sjf_banner.banner.entity.CombineIndicatorData;
import com.shijingfeng.sjf_banner.banner.entity.ShapeIndicatorData;
import com.shijingfeng.sjf_banner.banner.entity.TitleIndicatorData;
import com.shijingfeng.sjf_banner.banner.indicator.CombineIndicator;
import com.shijingfeng.sjf_banner.banner.listener.OnPageChangeListener;
import com.shijingfeng.sjf_banner.banner.view.BannerView;
import com.shijingfeng.app.entity.MainEntity;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Function: 轮播图适配器
 * Author: ShiJingFeng
 * Date: 2020/1/5 10:40
 * Description:
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.BannerViewHolder> {

    private static final int TYPE_BANNER = 1;
    private static final int TYPE_ITEM = 2;

    private Context mContext;
    private List<MainEntity> mMainList;
    private int mIndex = 0;

    public MainAdapter(@NonNull Context context, @Nullable List<MainEntity> mainList) {
        this.mContext = context;
        this.mMainList = mainList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view;

        if (viewType == TYPE_BANNER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_banner, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_list, parent, false);
        }
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_BANNER) {
            final MainEntity main = mMainList.get(position);
            final List<BannerEntity> bannerList = main.bannerList;
            final BannerPagerAdapter bannerPagerAdapter = new BannerPagerAdapter(mContext, bannerList);
            final BannerView bannerView = holder.itemView.findViewById(R.id.bv_banner);
            final ShapeIndicatorData shapeIndicatorData = new ShapeIndicatorData();

            shapeIndicatorData.curRealPosition = mIndex;
            shapeIndicatorData.totalRealCount = bannerList.size();
            shapeIndicatorData.gravity = Gravity.END | Gravity.CENTER_VERTICAL;

//        final TextIndicatorData textIndicatorData = new TextIndicatorData();
//
//        textIndicatorData.curRealPosition = 0;
//        textIndicatorData.totalRealCount = bannerList.size();
//        textIndicatorData.gravity = Gravity.END | Gravity.BOTTOM;

            final TitleIndicatorData titleIndicatorData = new TitleIndicatorData();
            final List<String> titleTextList = new ArrayList<>();

            for (BannerEntity entity : bannerList) {
                titleTextList.add(entity.title);
            }

            titleIndicatorData
                    .setCurRealPosition(mIndex)
                    .setTotalRealCount(bannerList.size())
                    .setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM)
                    .setTitleTextList(titleTextList);

            final List<BaseIndicatorData> indicatorDataList = new ArrayList<>();
            final CombineIndicatorData combineIndicatorData = new CombineIndicatorData();

            indicatorDataList.add(titleIndicatorData);
            indicatorDataList.add(shapeIndicatorData);
            combineIndicatorData
                    .setIndicatorDataList(indicatorDataList)
                    .setWidth(MATCH_PARENT)
                    .setBackground(mContext.getResources().getDrawable(R.color.colorAccent))
                    .setGravity(Gravity.BOTTOM);

            bannerView
                    .addOnPageChangeListener(new OnPageChangeListener() {

                        /**
                         * 新页面被选择时执行, 不一定等到动画完成时才执行
                         *
                         * @param prePosition 前一个被选择的页面位置 (小于 0 为没有)
                         * @param curPosition 被选择的新页面位置
                         */
                        @Override
                        public void onPageSelected(int prePosition, int curPosition) {
                            Log.e("测试", "前一个位置: " + prePosition + "  当前位置: " + curPosition);
                        }
                    })
                    .setPagerAdapter(bannerPagerAdapter)
                    .setScrollable(true)
                    .setIndicator(combineIndicatorData)
//                .setIndicator(ShapeIndicator.create(mContext, shapeIndicatorData))
//                .setIndicator(TextIndicator.create(mContext, textIndicatorData))
//                .setIndicator(TitleIndicator.create(mContext, titleIndicatorData))
                    .setCurrentRealItem(mIndex, false)
                    .setIndicator(CombineIndicator.create(mContext, combineIndicatorData))
                    .start();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BannerViewHolder holder) {
        if (holder.getAdapterPosition() == 0) {
            final BannerView bannerView = holder.itemView.findViewById(R.id.bv_banner);

            mIndex = bannerView.getCurrentRealItem();
        }
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (mMainList == null) {
            return 0;
        }
        return mMainList.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
