package com.bangjiat.bangjiaapp.module.account.contract;

import com.bangjiat.bangjiaapp.common.BaseResult;
import com.bangjiat.bangjiaapp.module.account.beans.LoginInput;

/**
 * Created by Administrator on 2018/4/14 0014.
 */

public interface LoginContract {
    interface Model {
        void login(LoginInput input);
    }

    interface View {
        void showDialog();

        void dismissDialog();

        void showError(String err);

        void loginSuccess(BaseResult<String> result);
    }

    interface Presenter {
        void login(String phone, String password);

        void loginSuccess(BaseResult<String> result);

        void loginFail(String error);
    }
}
