package com.shijingfeng.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.shijingfeng.app.R;
import com.shijingfeng.app.adapter.MainAdapter;
import com.shijingfeng.app.entity.BannerEntity;
import com.shijingfeng.app.entity.MainEntity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvContent;
    private MainAdapter mMainAdapter;
    private List<MainEntity> mMainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initAction();
    }

    private void initView() {
        rvContent = findViewById(R.id.rv_content);
    }

    private void initData() {
        mMainList = new ArrayList<>();

        final MainEntity main = new MainEntity();
        final List<BannerEntity> bannerList = new ArrayList<>();

        bannerList.add(new BannerEntity("1", "第1个", getResources().getDrawable(R.drawable.image1)));
        bannerList.add(new BannerEntity("2", "第2个", getResources().getDrawable(R.drawable.image2)));
        bannerList.add(new BannerEntity("3", "第3个", getResources().getDrawable(R.drawable.image3)));
        bannerList.add(new BannerEntity("4", "第4个", getResources().getDrawable(R.drawable.image4)));

        main.bannerList = bannerList;
        mMainList.add(main);

        for (int i = 0; i < 30; ++i) {
            mMainList.add(new MainEntity());
        }

        rvContent.setAdapter(mMainAdapter = new MainAdapter(this, mMainList));
        rvContent.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initAction() {

    }
}
