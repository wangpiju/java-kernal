package com.hs3.entity.lotts;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class BetTiger {
    private Integer id;
    private String account;
    private Date createTime;
    private String openNum;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOpenNum() {
        return this.openNum;
    }

    public void setOpenNum(String openNum) {
        this.openNum = openNum;
    }

    public int getQ3() {
        return getOpenStatus(0, 1, 2);
    }

    public int getZ3() {
        return getOpenStatus(1, 2, 3);
    }

    public int getH3() {
        return getOpenStatus(2, 3, 4);
    }

    private int getOpenStatus(int i1, int i2, int i3) {
        String[] nums = this.openNum.split(",");
        Set<Integer> list = new HashSet();
        list.add(Integer.valueOf(Integer.parseInt(nums[i1])));
        list.add(Integer.valueOf(Integer.parseInt(nums[i2])));
        list.add(Integer.valueOf(Integer.parseInt(nums[i3])));
        return list.size();
    }
}
