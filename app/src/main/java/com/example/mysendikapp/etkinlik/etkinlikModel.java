package com.example.mysendikapp.etkinlik;

public class etkinlikModel {
    public String id,title,url,content,summary,date;

    public etkinlikModel(){ }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary= summary;
    }
}
