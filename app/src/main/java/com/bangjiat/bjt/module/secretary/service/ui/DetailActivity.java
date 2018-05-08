package com.bangjiat.bjt.module.secretary.service.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bangjiat.bjt.R;
import com.bangjiat.bjt.common.FullImageActivity;
import com.bangjiat.bjt.module.main.ui.activity.BaseColorToolBarActivity;
import com.bangjiat.bjt.module.secretary.door.adapter.HandleHistoryAdapter;
import com.bangjiat.bjt.module.secretary.door.beans.HandleHistoryDetail;
import com.bangjiat.bjt.module.secretary.service.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 服务申请 详情
 */
public class DetailActivity extends BaseColorToolBarActivity {
    @BindView(R.id.recycler_view_history)
    RecyclerView recycler_view_history;
    @BindView(R.id.recycler_view_photo)
    RecyclerView recycler_view_photo;

    private List<String> photoList;
    private List<HandleHistoryDetail> histories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        photoList = new ArrayList<>();
        photoList.add("https://img6.bdstatic.com/img/image/public/jingtianshouxie.jpg");
        photoList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524285062940&di=4cdaee551ca0cb2d0d79fab9e6f4f12a&imgtype=0&src=http%3A%2F%2Fimg0.ph.126.net%2FdWGD1ToHPqmML8XYUOnZyg%3D%3D%2F6630522407933417361.jpg");
        photoList.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3763658861,4163786043&fm=27&gp=0.jpg");
        photoList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524285092085&di=bed1257144ccf3f530be1f77191a7edb&imgtype=0&src=http%3A%2F%2Fs9.sinaimg.cn%2Fmiddle%2F5227549bha1025687dd58%26690");


        setHandleAdapter();
        setPhotoAdapter();
    }


    private void setHandleAdapter() {
        recycler_view_history.setLayoutManager(new LinearLayoutManager(mContext));
        recycler_view_history.setHasFixedSize(true);
        HandleHistoryAdapter adapter = new HandleHistoryAdapter(histories, mContext);
        recycler_view_history.setAdapter(adapter);

        adapter.setOnItemClickListener(new HandleHistoryAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
    }

    private void setPhotoAdapter() {
        recycler_view_photo.setLayoutManager(new GridLayoutManager(mContext, 4));
        recycler_view_photo.setHasFixedSize(true);
        ImageAdapter adapter = new ImageAdapter(photoList, mContext);
        recycler_view_photo.setAdapter(adapter);

        adapter.setOnItemClickListener(new ImageAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, FullImageActivity.class);
                intent.putExtra("path", photoList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_datail;
    }

    @Override
    protected String getTitleStr() {
        return "申请详情";
    }
}
