package com.bangjiat.bjt.module.park.apply.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adorkable.iosdialog.AlertDialog;
import com.bangjiat.bjt.R;
import com.bangjiat.bjt.common.Constants;
import com.bangjiat.bjt.common.DataUtil;
import com.bangjiat.bjt.module.main.ui.activity.BaseWhiteToolBarActivity;
import com.bangjiat.bjt.module.park.apply.adapter.CarDetailAdapter;
import com.bangjiat.bjt.module.park.apply.beans.DealParkApplyInput;
import com.bangjiat.bjt.module.park.apply.beans.ParkApplyDetail;
import com.bangjiat.bjt.module.park.apply.beans.ParkApplyHistoryResult;
import com.bangjiat.bjt.module.park.apply.beans.ParkingResult;
import com.bangjiat.bjt.module.park.apply.contract.ParkApplyContract;
import com.bangjiat.bjt.module.park.apply.presenter.ParkApplyPresenter;
import com.bangjiat.bjt.module.park.car.beans.CarBean;
import com.dou361.dialogui.DialogUIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ApplyDetailActivity extends BaseWhiteToolBarActivity implements ParkApplyContract.View {
    @BindView(R.id.recycler_view_car)
    RecyclerView recyclerViewCar;
    @BindView(R.id.tv_company_name)
    TextView tv_company_name;
    @BindView(R.id.rl_btn)
    RelativeLayout rl_btn;
    ParkApplyHistoryResult.RecordsBean bean;
    private List<ParkApplyDetail> details;
    int applyId;
    int type;
    private CarDetailAdapter mAdapter;
    private Dialog dialog;
    private ParkApplyContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        presenter = new ParkApplyPresenter(this);
        recyclerViewCar.setHasFixedSize(true);
        recyclerViewCar.setLayoutManager(new LinearLayoutManager(mContext));
        bean = (ParkApplyHistoryResult.RecordsBean) getIntent().getSerializableExtra("data");
        if (bean != null) {
            applyId = bean.getApplyId();
            tv_company_name.setText(bean.getCompany());
            details = new Gson().fromJson(bean.getDetail(), new TypeToken<List<ParkApplyDetail>>() {
            }.getType());

            if (details != null && details.size() > 0) {
                setAdapter();
            }
            if (bean.getStatus() == 1) {
                rl_btn.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setAdapter() {
        mAdapter = new CarDetailAdapter(details, mContext);
        recyclerViewCar.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_apply_ditail;
    }

    @Override
    protected String getTitleStr() {
        return "审批详情";
    }

    @OnClick(R.id.btn_refuse)
    public void clickRefuse(View view) {
        type = 2;

        deal();
    }

    private void deal() {
        DealParkApplyInput input = new DealParkApplyInput();
        input.setApplyId(applyId);
        input.setType(type);
        List<DealParkApplyInput.Detail> details = new ArrayList<>();
        List<ParkApplyDetail> lists = mAdapter.getLists();
        for (ParkApplyDetail detail : lists) {
            details.add(new DealParkApplyInput.Detail(detail.getUserId(), detail.getType(), ""));
        }
        input.setDetailList(details);
        presenter.dealParkApply(DataUtil.getToken(mContext), input);
    }

    @OnClick(R.id.btn_agree)
    public void clickAgree(View view) {
        type = 1;
        deal();
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
        Constants.showErrorDialog(this, err);
    }

    @Override
    public void getWorkersCarSuccess(List<CarBean> list) {

    }

    @Override
    public void getParkSpaceSuccess(ParkingResult s) {

    }

    @Override
    public void parkApplySuccess() {

    }

    @Override
    public void dealParkApplySuccess() {
        showSuccessExitDialog();
    }

    public void showSuccessExitDialog() {
        new AlertDialog(mContext).builder().setMsg("审批成功").setCancelable(false).
                setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setResult(RESULT_OK);
                        finish();
                    }
                }).show();
    }

    @Override
    public void getParkApplyHistorySuccess(ParkApplyHistoryResult result) {

    }
}
