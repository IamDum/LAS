package com.sample.controller;

public class EmbedResponse implements Comparable {

    String word;
    float score;
    String code;
    String text;
    private String url;
    private boolean recommendation;
    private boolean page;


    public boolean isPage() { return page;}

    public void setPage(boolean page) { this.page = page; }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }


    @Override
    public int hashCode() {
        return word.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        EmbedResponse obj1 = (EmbedResponse) obj;
        return obj1.word.equalsIgnoreCase(this.word);
    }

    public boolean isRecommendation() {
        return recommendation;
    }

    public void setRecommendation(boolean recommendation) {
        this.recommendation = recommendation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(Object o) {
        EmbedResponse obj = (EmbedResponse) o;
        if (obj.getScore() > getScore()) {
            return 1;
        } else {
            return -1;
        }
    }
}
