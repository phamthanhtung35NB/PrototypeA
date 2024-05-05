package com.example.prototypea.Class;

public class Post {
    private String key;
    private String name;
    private String uid;
    private String status;
    private String content;
    private String image;
    private String date;
    private String time;
    private int likeCount;
    private int tagetCount;
    private int tagetSum;

    public Post() {
    }

    public Post(String key, String name, String uid, String status, String content, String image, String date, String time, int likeCount, int tagetCount, int tagetSum) {
        this.key = key;
        this.name = name;
        this.uid = uid;
        this.status = status;
        this.content = content;
        this.image = image;
        this.date = date;
        this.time = time;
        this.likeCount = likeCount;
        this.tagetCount = tagetCount;
        this.tagetSum = tagetSum;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getTagetCount() {
        return tagetCount;
    }

    public void setTagetCount(int tagetCount) {
        this.tagetCount = tagetCount;
    }

    public int getTagetSum() {
        return tagetSum;
    }

    public void setTagetSum(int tagetSum) {
        this.tagetSum = tagetSum;
    }
}
