package com.aiyaopai.lightio.mvp.contract;

import android.content.Context;

import com.aiyaopai.lightio.adapter.PicAdapter;
import com.aiyaopai.lightio.base.BaseView;
import com.aiyaopai.lightio.bean.PicBean;
import com.aiyaopai.lightio.databinding.FragmentLiveBinding;
import com.aiyaopai.lightio.ptp.Camera;

import java.util.List;
import java.util.Map;


public interface LiveContract {
    interface Model {

        List<PicBean> getDataList(List<PicBean> dataList);
    }

    interface View extends BaseView {

        void getRecycleViewData(List<PicBean> dataList, PicAdapter adapter);
        void getScanProgress(int progress);
        void getScanComplete();
        void getUploadNext(PicBean bean);

        void getUploadSingleNext(PicBean bean);
        void getUploadSingleAdd(PicBean picId);

        void getAllPics(Map<String, List<PicBean>> map);
        void scanIdComplete(List<Integer> picIds);

        void getUploadHandNext(PicBean bean);

        void getUploadHandAdd(PicBean bean);
    }

    interface Presenter {

        void initToolbar(FragmentLiveBinding binding);
        void initRecycleView(Context context, FragmentLiveBinding binding);
        void setModePx(FragmentLiveBinding binding,PicAdapter adapter);
        void scanningPic(List<Integer> picIds, Camera camera,String activity);
        void upLoadPic(List<PicBean> pathList, String token);
        void upLoadSingle(int picId, Camera camera,String activityId,String qiNiuToken);
        void addHandBean(int picId, Camera camera,String activityId,String qiNiuToken,String mode);
        void deleteDB();
        void queryUploadedPics();
        void getScanPicIds(int[] handles);
        void handUploadPic(PicBean bean,String qiNiuToken);
//        void getSdPicNum(String activityId);
    }

}
