package com.hs3.models.user;

import com.hs3.entity.users.User;
import com.hs3.entity.users.UserQuota;

import java.util.List;

public class RootUserAddModel {
    private User user;
    private List<UserQuota> quota;
    private List<Integer> domain;

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserQuota> getQuota() {
        return this.quota;
    }

    public void setQuota(List<UserQuota> quota) {
        this.quota = quota;
    }

    public List<Integer> getDomain() {
        return this.domain;
    }

    public void setDomain(List<Integer> domain) {
        this.domain = domain;
    }
}
