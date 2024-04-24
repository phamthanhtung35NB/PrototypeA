package com.example.prototypea;

public class ItemList {
    String content;
    String status;
    String key; // Add this line

    public ItemList(String content, String status, String key) { // Modify this line
        this.content = content;
        this.status = status;
        this.key = key; // Add this line
    }
    public String getKey() {
        return key;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
