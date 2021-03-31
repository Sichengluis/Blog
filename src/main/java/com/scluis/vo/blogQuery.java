package com.scluis.vo;

/**
 * Created by Sichengluis on 2021/1/25 10:43
 */
public class blogQuery {
    private String title;
    private Long typeId;
    private boolean recommend;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public blogQuery(Long typeId) {
        this.typeId = typeId;
    }

    public blogQuery() {
    }
}
