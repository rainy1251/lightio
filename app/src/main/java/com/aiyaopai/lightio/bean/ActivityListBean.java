package com.aiyaopai.lightio.bean;

import java.io.Serializable;
import java.util.List;

public class ActivityListBean implements Serializable {
    private int total;
    private List<ResultBean> result;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        private int beginAt;
        private boolean deleted;
        private String description;
        private int endAt;
        private String id;
        private LocationBean location;
        private String logo;
        private String name;
        private boolean published;
        private SharerBean sharer;
        private String teamId;
        private String templatesId;
        private Object userId;
        private int createdAt;
        private List<String> categoryTags;
        private List<WorkersBean> workers;

        public int getBeginAt() {
            return beginAt;
        }

        public void setBeginAt(int beginAt) {
            this.beginAt = beginAt;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getEndAt() {
            return endAt;
        }

        public void setEndAt(int endAt) {
            this.endAt = endAt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isPublished() {
            return published;
        }

        public void setPublished(boolean published) {
            this.published = published;
        }

        public SharerBean getSharer() {
            return sharer;
        }

        public void setSharer(SharerBean sharer) {
            this.sharer = sharer;
        }

        public String getTeamId() {
            return teamId;
        }

        public void setTeamId(String teamId) {
            this.teamId = teamId;
        }

        public String getTemplatesId() {
            return templatesId;
        }

        public void setTemplatesId(String templatesId) {
            this.templatesId = templatesId;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public int getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(int createdAt) {
            this.createdAt = createdAt;
        }

        public List<String> getCategoryTags() {
            return categoryTags;
        }

        public void setCategoryTags(List<String> categoryTags) {
            this.categoryTags = categoryTags;
        }

        public List<WorkersBean> getWorkers() {
            return workers;
        }

        public void setWorkers(List<WorkersBean> workers) {
            this.workers = workers;
        }

        public static class LocationBean implements Serializable {
            private double latitude;
            private double longitude;
            private String address;
            private int cityId;
            private int countryId;
            private int provinceId;

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getCityId() {
                return cityId;
            }

            public void setCityId(int cityId) {
                this.cityId = cityId;
            }

            public int getCountryId() {
                return countryId;
            }

            public void setCountryId(int countryId) {
                this.countryId = countryId;
            }

            public int getProvinceId() {
                return provinceId;
            }

            public void setProvinceId(int provinceId) {
                this.provinceId = provinceId;
            }
        }

        public static class SharerBean implements Serializable {
            private String logo;
            private String description;
            private String title;

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class WorkersBean implements Serializable {
            private String role;
            private String userId;
            private boolean external;

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public boolean isExternal() {
                return external;
            }

            public void setExternal(boolean external) {
                this.external = external;
            }
        }
    }
}
