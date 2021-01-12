package com.shijingfeng.app.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.shijingfeng.app.entity.BannerEntity;
import com.shijingfeng.sjf_banner.banner.adapter.BaseBannerPagerAdapter;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Function:
 * Author: ShiJingFeng
 * Date: 2020/1/5 10:48
 * Description:
 * @author ShiJingFeng
 */
public class BannerPagerAdapter extends BaseBannerPagerAdapter<BannerEntity> {

    public BannerPagerAdapter(@NonNull Context context, List<BannerEntity> dataList) {
        super(context, dataList);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final ImageView imageView = new ImageView(mContext);
        final BannerEntity banner = getDataByPosition(position);

        imageView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        imageView.setImageDrawable(banner.drawable);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        Glide.with(mContext)
//                .load(banner.imageUrl)
//                .dontAnimate()
//                .into(imageView);

        container.addView(imageView);

        return imageView;
    }
}
