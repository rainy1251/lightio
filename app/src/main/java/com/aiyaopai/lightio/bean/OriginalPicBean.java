package com.aiyaopai.lightio.bean;

import java.io.Serializable;
import java.util.List;

public class OriginalPicBean implements Serializable {
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
        private String albumId;
        private Object ave;
        private String categoryId;
        private int createdAt;
        private String createdUserId;
        private boolean downloaded;
        private String hash;
        private int height;
        private String id;
        private String mediaType;
        private String name;
        private int shotAt;
        private int size;
        private String url;
        private int width;

        public String getAlbumId() {
            return albumId;
        }

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        public Object getAve() {
            return ave;
        }

        public void setAve(Object ave) {
            this.ave = ave;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public int getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(int createdAt) {
            this.createdAt = createdAt;
        }

        public String getCreatedUserId() {
            return createdUserId;
        }

        public void setCreatedUserId(String createdUserId) {
            this.createdUserId = createdUserId;
        }

        public boolean isDownloaded() {
            return downloaded;
        }

        public void setDownloaded(boolean downloaded) {
            this.downloaded = downloaded;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getShotAt() {
            return shotAt;
        }

        public void setShotAt(int shotAt) {
            this.shotAt = shotAt;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }
}
