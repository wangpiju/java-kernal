package com.hs3.entity.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LotteryCrawlerConfig {
    private Integer id;
    private String lotteryId;
    private String url;
    private String urlBuilder;
    private String regex;
    private Integer weight;
    private Integer status;
    private Integer craType;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLotteryId() {
        return this.lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlBuilder() {
        return this.urlBuilder;
    }

    public void setUrlBuilder(String urlBuilder) {
        this.urlBuilder = urlBuilder;
    }

    public String getRegex() {
        return this.regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCraType() {
        return this.craType;
    }

    public void setCraType(Integer craType) {
        this.craType = craType;
    }
}
