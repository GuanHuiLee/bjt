package com.bangjiat.bjt.module.secretary.communication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bangjiat.bjt.R;
import com.bangjiat.bjt.common.DataUtil;
import com.bangjiat.bjt.common.FullImageActivity;
import com.bangjiat.bjt.common.TimeUtils;
import com.bangjiat.bjt.module.main.ui.activity.BaseToolBarActivity;
import com.bangjiat.bjt.module.me.personaldata.beans.UserInfo;
import com.bangjiat.bjt.module.secretary.communication.beans.EmailBean;
import com.bangjiat.bjt.module.secretary.communication.contract.DealBoxContract;
import com.bangjiat.bjt.module.secretary.communication.presenter.DealBoxPresenter;
import com.bangjiat.bjt.module.secretary.service.adapter.ImageAdapter;
import com.githang.statusbar.StatusBarCompat;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BoxDetailActivity extends BaseToolBarActivity implements DealBoxContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_send_name)
    TextView tv_send_name;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_receive_name)
    TextView tv_receiver_name;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.iv_response)
    ImageView iv_response;
    TextView toolbar_title;


    private List<String> photoList;
    private EmailBean bean;
    private int type;
    private DealBoxContract.Presenter presenter;
    private String token;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
        initData();
    }

    private void initData() {
        userInfo = UserInfo.first(UserInfo.class);
        token = DataUtil.getToken(mContext);
        presenter = new DealBoxPresenter(this);
        bean = (EmailBean) getIntent().getSerializableExtra("data");
        if (bean != null) {
            photoList = new ArrayList<>();
            String resource = bean.getResource();
            if (resource != null && !resource.isEmpty()) {
                String[] split = resource.split("\\|");
                Logger.d(split[0]);
                photoList = Arrays.asList(split);
                setPhotoAdapter();
            }
            tv_content.setText(bean.getContent());
            tv_send_name.setText(bean.getSender());
            tv_time.setText(TimeUtils.changeToTime(bean.getCtime()));
            tv_title.setText(bean.getTitle());

            type = getIntent().getIntExtra("type", 1);
            if (type == 1) {
                tv_title.setText("发件箱");
                tv_receiver_name.setText(bean.getReceiver());
            } else {
                toolbar_title.setText("收件箱");
                iv_response.setVisibility(View.VISIBLE);
                String[] strings = new String[1];
                strings[0] = String.valueOf(bean.getEmailId());
                presenter.markBox(DataUtil.getToken(mContext), strings);
                tv_receiver_name.setText(userInfo.getNickname());
            }
        }
    }

    private void setPhotoAdapter() {
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView.setHasFixedSize(true);
        ImageAdapter adapter = new ImageAdapter(photoList, mContext);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ImageAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, FullImageActivity.class);
                intent.putExtra("path", photoList.get(position));
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.iv_delete)
    public void clickDelete(View view) {
        String[] strings = new String[1];
        strings[0] = String.valueOf(bean.getEmailId());
        presenter.deleteInBox(token, strings);
    }

    @OnClick(R.id.iv_share)
    public void clickShare(View view) {

    }

    @OnClick(R.id.iv_response)
    public void clickResponse(View view) {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_out_box_detail;
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("");
        toolbar_title = toolbar.findViewById(R.id.toolbar_cancel);
        toolbar_title.setVisibility(View.VISIBLE);
        toolbar.setNavigationIcon(R.mipmap.back_black);
        toolbar_title.setTextColor(getResources().getColor(R.color.black));
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void dismissDialog() {

    }

    @Override
    public void fail(String err) {
        Logger.e(err);
    }

    @Override
    public void deleteOutBoxSuccess() {

    }

    @Override
    public void deleteInBoxSuccess() {

    }

    @Override
    public void markBoxSuccess() {

    }

    @Override
    public void getUnReadCountsSuccess(String s) {

    }
}