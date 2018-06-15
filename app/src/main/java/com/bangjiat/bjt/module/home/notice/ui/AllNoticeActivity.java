package com.bangjiat.bjt.module.home.notice.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bangjiat.bjt.R;
import com.bangjiat.bjt.common.DataUtil;
import com.bangjiat.bjt.common.ReplaceViewHelper;
import com.bangjiat.bjt.module.home.notice.adapter.NoticeAdapter;
import com.bangjiat.bjt.module.home.notice.beans.NoticeBean;
import com.bangjiat.bjt.module.home.notice.contract.NoticeContract;
import com.bangjiat.bjt.module.home.notice.presenter.NoticePresenter;
import com.bangjiat.bjt.module.main.ui.activity.BaseWhiteToolBarActivity;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

public class AllNoticeActivity extends BaseWhiteToolBarActivity implements NoticeContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    private NoticeContract.Presenter presenter;
    private List<NoticeBean.SysNoticeListBean> list;
    private NoticeAdapter mAdapter;
    private ReplaceViewHelper mReplaceViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        mReplaceViewHelper = new ReplaceViewHelper(this);
        presenter = new NoticePresenter(this);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setHasFixedSize(true);
        list = NoticeBean.SysNoticeListBean.listAll(NoticeBean.SysNoticeListBean.class, "ctime desc");
        if (list == null) {
            list = new ArrayList<>();
            presenter.getAllNotice(DataUtil.getToken(mContext));
        } else {
            if (list.size() == 0) {
                mReplaceViewHelper.toReplaceView(recycler_view, R.layout.no_data_page);
            } else
                setAdapter();
        }

    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_all_notice;
    }

    @Override
    protected String getTitleStr() {
        return "全部公告";
    }


    @Override
    public void showDialog() {

    }

    @Override
    public void dismissDialog() {

    }

    @Override
    public void getAllNoticeResult(NoticeBean noticeBean) {
        if (noticeBean != null) {
            List<NoticeBean.SysNoticeListBean> sysNoticeList = noticeBean.getSysNoticeList();
            if (sysNoticeList != null && sysNoticeList.size() > 0) {
                list.addAll(sysNoticeList);
            }
            List<NoticeBean.SysNoticeListBean> buildNoticeList = noticeBean.getBuildNoticeList();
            if (buildNoticeList != null && buildNoticeList.size() > 0) {
                list.addAll(buildNoticeList);
            }
        }

        if (list.size() > 0) {
            Collections.sort(list, new Comparator<NoticeBean.SysNoticeListBean>() {//按时间排序

                @Override
                public int compare(NoticeBean.SysNoticeListBean o1, NoticeBean.SysNoticeListBean o2) {
                    if (o1.getCtime() < o2.getCtime()) {
                        return 1;
                    } else if (o1.getCtime() == o2.getCtime()) {
                        return 0;
                    }
                    return -1;
                }
            });

            SugarRecord.saveInTx(list);

            mAdapter.setLists(list);
            mReplaceViewHelper.removeView();
            return;
        }
        mReplaceViewHelper.toReplaceView(recycler_view, R.layout.no_data_page);
    }

    private void setAdapter() {
        mAdapter = new NoticeAdapter(list);
        recycler_view.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new NoticeAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NoticeBean.SysNoticeListBean bean = list.get(position);
                Intent intent = new Intent(mContext, NoticeItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", bean);
                intent.putExtras(bundle);
                startActivity(intent);

                bean.setRead(true);
                bean.save();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showError(String err) {
        Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();
    }
}
