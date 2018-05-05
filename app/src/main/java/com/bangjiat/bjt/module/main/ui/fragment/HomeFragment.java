package com.bangjiat.bjt.module.main.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bangjiat.bjt.R;
import com.bangjiat.bjt.common.BaseFragment;
import com.bangjiat.bjt.module.home.company.ui.AddOrSelectCompanyActivity;
import com.bangjiat.bjt.module.home.notice.beans.NoticeBean;
import com.bangjiat.bjt.module.home.notice.ui.AllNoticeActivity;
import com.bangjiat.bjt.module.home.notice.ui.NoticeItemActivity;
import com.bangjiat.bjt.module.home.scan.beans.QrCodeDataUser;
import com.bangjiat.bjt.module.home.scan.ui.ScanActivity;
import com.bangjiat.bjt.module.home.visitor.ui.VisitorActivity;
import com.bangjiat.bjt.module.home.work.ui.WorkMainActivity;
import com.bangjiat.bjt.module.me.personaldata.beans.UserInfoBean;
import com.bangjiat.bjt.module.secretary.contact.beans.SearchContactResult;
import com.bangjiat.bjt.module.secretary.contact.view.ContactInfoActivity;
import com.google.gson.Gson;
import com.joker.api.Permissions4M;
import com.joker.api.wrapper.Wrapper;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bertsir.zbar.QrConfig;
import cn.bertsir.zbar.QrManager;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;


public class HomeFragment extends BaseFragment {
    @BindView(R.id.banner_guide_content)
    BGABanner mContentBanner;
    private static final int READ_CODE = 2;
    private static final int CAMERA_CODE = 3;
    private boolean isOpenRead, isOpenCamera;

    protected void initView() {
        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
        mContentBanner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.banner1,
                R.drawable.banner2,
                R.drawable.banner3,
                R.drawable.banner4);
        mContentBanner.setDelegate(new BGABanner.Delegate<ImageView, Integer>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, Integer model, int position) {
//                Toast.makeText(banner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @OnClick(R.id.tv_more_notice)
    public void clickMoreNotice(View view) {
        startActivity(new Intent(mContext, AllNoticeActivity.class));
    }

    @OnClick(R.id.tv_content)
    public void clickContent(View view) {
        Intent intent = new Intent(mContext, NoticeItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", new NoticeBean("国庆放假通知：", "国庆七天假期，国际互联网金融特区大厦国庆七天假期，国际互联网金融特区大厦国庆七天假期，国际互联网金融特区大厦国庆七天假期，国际互联网金融特区大厦国庆七天假期，国际互联网金融特国庆七天假期，国际互联网金融特区大厦国庆七天假期，" +
                "国际互联网金融特区大厦国庆七天假期，国际互联网金融特区大厦国庆七天假期，国际互联网金融特区大厦国庆七天假期，国际互联网金融特区大厦。", "2018-03-24 16:54"));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.tv_visitor)
    public void clickVisitor(View view) {
        startActivity(new Intent(mContext, VisitorActivity.class));
    }

    @OnClick(R.id.iv_scan)
    public void clickScan(View view) {
        checkPermission();
    }

    @OnClick(R.id.tv_open_door)
    public void clickOpenDoor(View view) {
        UserInfoBean.CompanyUserBean companyUserBean = UserInfoBean.CompanyUserBean.first(UserInfoBean.CompanyUserBean.class);
        if (companyUserBean != null)
            Logger.d(companyUserBean.toString());
//        startActivity(new Intent(mContext, companyUserBean == null ? AddOrSelectCompanyActivity.class : OpenDoorCodeActivity.class));
        startActivity(new Intent(mContext, AddOrSelectCompanyActivity.class));
    }

    @OnClick(R.id.tv_work)
    public void clickWork(View view) {
        startActivity(new Intent(mContext, WorkMainActivity.class));
    }

    private void checkPermission() {
        Permissions4M.get(this)
                .requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .requestCodes(READ_CODE, CAMERA_CODE)
                .requestListener(new Wrapper.PermissionRequestListener() {
                    @Override
                    public void permissionGranted(int code) {
                        switch (code) {
                            case READ_CODE:
//                                Toast.makeText(mContext, "读权限授权成功", Toast.LENGTH_SHORT).show();
                                isOpenRead = true;
                                startScan();
                                break;
                            case CAMERA_CODE:
                                isOpenCamera = true;
//                                Toast.makeText(mContext, "摄像头授权成功", Toast.LENGTH_SHORT).show();
                                startScan();
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void permissionDenied(int code) {
                        switch (code) {
                            case READ_CODE:
                                isOpenRead = false;
                                Toast.makeText(mContext, "存储权限授权失败", Toast.LENGTH_SHORT).show();
                                break;
                            case CAMERA_CODE:
                                isOpenCamera = false;
                                Toast.makeText(mContext, "摄像头权限授权失败", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void permissionRationale(int code) {
                        switch (code) {
                            case READ_CODE:
//                                Toast.makeText(mContext, "请开启读权限", Toast.LENGTH_SHORT).show();
                                break;
                            case CAMERA_CODE:
//                                Toast.makeText(mContext, "请开启摄像头权限", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .request();
    }

    private void startScan() {
        if (isOpenCamera && isOpenRead) {
            Logger.d("startScan");
            isOpenRead = false;
            isOpenCamera = false;

            QrConfig options = new QrConfig.Builder().create();
            QrManager.getInstance().init(options).startScan(new QrManager.OnScanResultCallback() {
                @Override
                public void onScanSuccess(String result) {
                    QrCodeDataUser user = new Gson().fromJson(result, QrCodeDataUser.class);
                    if (user != null) {
                        Intent intent = new Intent(mContext, ContactInfoActivity.class);
                        SearchContactResult bean = new SearchContactResult(user.getNickname(),
                                user.getUserId(), user.getUsername());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", bean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "二维码解析失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Intent intent = new Intent(mContext, ScanActivity.class);
            intent.putExtra(QrConfig.EXTRA_THIS_CONFIG, options);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permissions4M.onRequestPermissionsResult(this, requestCode, grantResults);
    }


}

