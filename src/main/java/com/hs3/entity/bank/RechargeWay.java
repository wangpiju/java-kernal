package com.hs3.entity.bank;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 支付渠道
 *
 * @author Stephen Zhou
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RechargeWay implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id; //数据ID
    private String alino;   //支付渠道代码
    private String alias;   //支付渠道名称
    private Integer opentype;   //是否打开 0：打开、1：关闭
    private Integer minmoney;   //最小额度
    private Integer maxmoney;   //最大额度
    private String content; //说明
    private String bankNameIds; //所关联的银行类型ID【暂时用不到】
    private Integer ordernum;   //充值次数
    private Integer waytype;   //渠道类型 0：其他、1：扫码、2：看卡、3：线上
    private String qrcodeurl;   //二维码路径
    private String attfirst;   //属性1
    private String attsecond;   //属性2
    private String attthird;   //属性3
    private String imgData;     //二维码base64位码
    private Integer bnId;   //银行对照 该字段只在扫码类型下需要选择，意义在于匹配现有的等级体系，扫码均是寻找同行卡
    private Integer attempty;   // 属性能否为空
    private String randomNow;   //当下二维码的随机数
    private String randomOld;   //上一次二维码的随机数

    public String getRandomNow() {
        return randomNow;
    }

    public void setRandomNow(String randomNow) {
        this.randomNow = randomNow;
    }

    public String getRandomOld() {
        return randomOld;
    }

    public void setRandomOld(String randomOld) {
        this.randomOld = randomOld;
    }

    public Integer getAttempty() {
        return attempty;
    }

    public void setAttempty(Integer attempty) {
        this.attempty = attempty;
    }

    public Integer getBnId() {
        return bnId;
    }

    public void setBnId(Integer bnId) {
        this.bnId = bnId;
    }

    public String getImgData() {
        return imgData;
    }

    public void setImgData(String imgData) {
        this.imgData = imgData;
    }

    public Integer getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }

    public Integer getWaytype() {
        return waytype;
    }

    public void setWaytype(Integer waytype) {
        this.waytype = waytype;
    }

    public String getQrcodeurl() {
        return qrcodeurl;
    }

    public void setQrcodeurl(String qrcodeurl) {
        this.qrcodeurl = qrcodeurl;
    }

    public String getAttfirst() {
        return attfirst;
    }

    public void setAttfirst(String attfirst) {
        this.attfirst = attfirst;
    }

    public String getAttsecond() {
        return attsecond;
    }

    public void setAttsecond(String attsecond) {
        this.attsecond = attsecond;
    }

    public String getAttthird() {
        return attthird;
    }

    public void setAttthird(String attthird) {
        this.attthird = attthird;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlino() {
        return alino;
    }

    public void setAlino(String alino) {
        this.alino = alino;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getOpentype() {
        return opentype;
    }

    public void setOpentype(Integer opentype) {
        this.opentype = opentype;
    }

    public Integer getMinmoney() {
        return minmoney;
    }

    public void setMinmoney(Integer minmoney) {
        this.minmoney = minmoney;
    }

    public Integer getMaxmoney() {
        return maxmoney;
    }

    public void setMaxmoney(Integer maxmoney) {
        this.maxmoney = maxmoney;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBankNameIds() {
        return bankNameIds;
    }

    public void setBankNameIds(String bankNameIds) {
        this.bankNameIds = bankNameIds;
    }


}
