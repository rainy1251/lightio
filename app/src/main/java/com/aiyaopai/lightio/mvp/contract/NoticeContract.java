package com.aiyaopai.lightio.mvp.contract;

import com.aiyaopai.lightio.base.BaseView;
import com.aiyaopai.lightio.bean.OriginalPicBean;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;

public interface NoticeContract {
    interface Model {
        Observable<OriginalPicBean> getOriginalPic(int offset, String albumId, String photographerId);
    }

    interface View extends BaseView {

        void getOriginalPic(OriginalPicBean bean);
        void syncComplete();
        void syncSDComplete();
        void syncNum(int syncNum);
        void getError();
    }

    interface Presenter {
        void getOriginalPic(int pageIndex,String albumId, String photographerId);

        void syncData(ArrayList<OriginalPicBean.ResultBean> result);

        void syncSD(String albumId);
    }
}
