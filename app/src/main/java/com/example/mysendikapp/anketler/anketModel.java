package com.example.mysendikapp.anketler;

import java.util.ArrayList;

public class anketModel {
    public String id,title,summary;
    public ArrayList<String> sorular;
    public ArrayList<ArrayList<String>> cevaplar;

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

    public String getSummary() {
        return summary ;
    }
    public void setSummary(String summary ) {
        this.summary = summary ;
    }

    public ArrayList<String> getSorular() {
        return sorular;
    }
    public void setSorular(ArrayList<String> sorular) {
        this.sorular = sorular;
    }

    public ArrayList<ArrayList<String>> getCevaplar() {
        return cevaplar;
    }
    public void setCevaplar(ArrayList<ArrayList<String>> cevaplar) {
        this.cevaplar = cevaplar;
    }
}
