package com.shijingfeng.sjfbanner.entity;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;

/**
 * Function:
 * Author: ShiJingFeng
 * Date: 2020/1/5 10:38
 * Description:
 */
public class BannerEntity extends BaseEntity {

    public String id = "";
    public String title = "";
    public String imageUrl = "";
    public Drawable drawable;

    public BannerEntity(String id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public BannerEntity(String id, String title, Drawable drawable) {
        this.id = id;
        this.title = title;
        this.drawable = drawable;
    }

}
