package com.aiyaopai.lightio.bean;

import java.util.ArrayList;

public class ActivityListBean {
    public ArrayList<ResultBean> getResult() {
        return Result;
    }

    public void setResult(ArrayList<ResultBean> result) {
        Result = result;
    }

    public ArrayList<ResultBean> Result;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public int Total;

    public class ResultBean {
        public String Id;
        public String Title;
        public String LocalKey;
        public int Size;
        public String Address;
        public String BeginAt;
        public String EndAt;
        public String OriginalAt;
        public String Url;
        private String BannerImage;

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getLocalKey() {
            return LocalKey;
        }

        public void setLocalKey(String localKey) {
            LocalKey = localKey;
        }

        public int getSize() {
            return Size;
        }

        public void setSize(int size) {
            Size = size;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getBeginAt() {
            return BeginAt;
        }

        public void setBeginAt(String beginAt) {
            BeginAt = beginAt;
        }

        public String getEndAt() {
            return EndAt;
        }

        public void setEndAt(String endAt) {
            EndAt = endAt;
        }

        public String getOriginalAt() {
            return OriginalAt;
        }

        public void setOriginalAt(String originalAt) {
            OriginalAt = originalAt;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String url) {
            Url = url;
        }

        public String getBannerImage() {
            return BannerImage;
        }

        public void setBannerImage(String bannerImage) {
            BannerImage = bannerImage;
        }

        public FranchiseeBean getFranchisee() {
            return Franchisee;
        }

        public void setFranchisee(FranchiseeBean franchisee) {
            Franchisee = franchisee;
        }

        private FranchiseeBean Franchisee;
        public class FranchiseeBean{

            private String CityName;

            public String getCityName() {
                return CityName;
            }

            public void setCityName(String cityName) {
                CityName = cityName;
            }
        }
    }
}
