package com.aiyaopai.lightio.bean;

import java.io.Serializable;

public class UploadFileBean implements Serializable {


    private ResultBean result;
    private CallbackResultBean callbackResult;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public CallbackResultBean getCallbackResult() {
        return callbackResult;
    }

    public void setCallbackResult(CallbackResultBean callbackResult) {
        this.callbackResult = callbackResult;
    }

    public static class ResultBean implements Serializable {
        private String Key;
        private String Bucket;
        private String Hash;
        private int Size;
        private String MediaType;

        public String getKey() {
            return Key;
        }

        public void setKey(String Key) {
            this.Key = Key;
        }

        public String getBucket() {
            return Bucket;
        }

        public void setBucket(String Bucket) {
            this.Bucket = Bucket;
        }

        public String getHash() {
            return Hash;
        }

        public void setHash(String Hash) {
            this.Hash = Hash;
        }

        public int getSize() {
            return Size;
        }

        public void setSize(int Size) {
            this.Size = Size;
        }

        public String getMediaType() {
            return MediaType;
        }

        public void setMediaType(String MediaType) {
            this.MediaType = MediaType;
        }
    }

    public static class CallbackResultBean implements Serializable {
        private String originalPictureId;
        private String url;
        private boolean exists;

        public String getOriginalPictureId() {
            return originalPictureId;
        }

        public void setOriginalPictureId(String originalPictureId) {
            this.originalPictureId = originalPictureId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isExists() {
            return exists;
        }

        public void setExists(boolean exists) {
            this.exists = exists;
        }
    }
}
