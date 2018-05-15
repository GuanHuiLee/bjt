package com.bangjiat.bjt.module.home.work.leave.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bangjiat.bjt.R;
import com.bangjiat.bjt.common.Constants;
import com.bangjiat.bjt.common.DataUtil;
import com.bangjiat.bjt.module.home.work.leave.adapter.LeaveHistroryAdapter;
import com.bangjiat.bjt.module.home.work.leave.beans.CompanyLeaveResult;
import com.bangjiat.bjt.module.home.work.leave.contract.LeaveContract;
import com.bangjiat.bjt.module.home.work.leave.presenter.LeavePresenter;
import com.bangjiat.bjt.module.main.ui.activity.BaseWhiteToolBarActivity;
import com.dou361.dialogui.DialogUIUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;

public class LeaveHistoryActivity extends BaseWhiteToolBarActivity implements LeaveContract.View {
    private Dialog dialog;
    private LeaveContract.Presenter presenter;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private LeaveHistroryAdapter mAdapter;
    private List<CompanyLeaveResult.RecordsBean> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        presenter = new LeavePresenter(this);
        presenter.getSelfLeave(DataUtil.getToken(mContext), 1, 1, 10);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void setAdapter() {
        mAdapter = new LeaveHistroryAdapter(list, mContext);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new LeaveHistroryAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CompanyLeaveResult.RecordsBean recordsBean = list.get(position);
                Intent intent = new Intent(mContext, LeaveDetailActivity.class);
                intent.putExtra("data", recordsBean);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_leave_history;
    }

    @Override
    protected String getTitleStr() {
        return "请假申请";
    }

    @Override
    public void showDialog() {
        dialog = DialogUIUtils.showLoadingVertical(mContext, "加载中").show();
    }

    @Override
    public void dismissDialog() {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void error(String err) {
        Logger.e(err);
        Constants.showErrorDialog(this, err);
    }

    @Override
    public void addLeaveSuccess() {

    }

    @Override
    public void getCompanyLeaveSuccess(CompanyLeaveResult result) {

    }

    @Override
    public void getSelfLeaveSuccess(CompanyLeaveResult result) {
        if (result != null) {
            List<CompanyLeaveResult.RecordsBean> records = result.getRecords();
            if (records != null && records.size() > 0) {
                list = records;
                setAdapter();
            }
        }
    }

    @Override
    public void dealLeaveSuccess() {

    }
}
