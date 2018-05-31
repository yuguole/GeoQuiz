package com.bignerdranch.android.geoquiz.Models;

public class AskBean {

        /**
         * title : first
         * details : 如题
         * askuser : 张三
         * asktime : 2018-05-05 10:03:00.133230+00:00
         */

        private String title;
        private String details;
        private String askuser;
        private String asktime;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getAskuser() {
            return askuser;
        }

        public void setAskuser(String askuser) {
            this.askuser = askuser;
        }

        public String getAsktime() {
            return asktime;
        }

        public void setAsktime(String asktime) {
            this.asktime = asktime;
        }

    @Override
    public String toString() {
        return "AskBean{" +
                "title='" + title + '\'' +
                ", details='" + details + '\'' +
                ", askuser='" + askuser + '\'' +
                ", asktime='" + asktime + '\'' +
                '}';
    }

/**
         * details : 今天在武汉 ，阳关明媚
         * title : 明天天气怎么样
         */


}
