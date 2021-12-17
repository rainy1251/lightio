package com.aiyaopai.lightio.mvp.contract;

import com.aiyaopai.lightio.base.BaseView;
import com.aiyaopai.lightio.bean.ActivityListBean;
import com.aiyaopai.lightio.bean.BaseBean;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;

public interface NoticeContract {
    interface Model {
        Observable<ActivityListBean> getOriginalPic(String activityId, String photographerId, int pageIndex);
        Observable<BaseBean> getUpLoadToken(String activityId);
    }

    interface View extends BaseView {

        void getOriginalPic(ActivityListBean bean);
        void syncComplete();
        void syncSDComplete();
        void syncNum(int syncNum);
        void getTokenSuccess(BaseBean bean);
        void getError();
    }

    interface Presenter {
        void getOriginalPic(String activityId, String photographerId, int pageIndex);

        void syncData(ArrayList<ActivityListBean.ResultBean> result);

        void syncSD(String activityId);
        void getUpLoadToken(String activityId);
    }
}
