package com.example.mysendikapp.bildirimler;

public class bildirimModel {
    public String id="0",title,date,content,type,bid="0",readed;

    public bildirimModel(){}

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getReaded() {
        return readed;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public void setReaded(String readed) {
        this.readed = readed;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }




}
