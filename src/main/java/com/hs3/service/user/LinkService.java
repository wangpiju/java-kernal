package com.hs3.service.user;

import com.hs3.dao.user.LinkDao;
import com.hs3.db.Page;
import com.hs3.entity.users.Link;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("linkService")
public class LinkService {
    @Autowired
    private LinkDao linkDao;

    public List<Link> list(String user, String url, Date date_from, Date date_to, String bonus, BigDecimal ratio, String notactive, Page p) {
        return this.linkDao.list(user, url, date_from, date_to, bonus, ratio, notactive, p);
    }

    public boolean setStatus(Integer id, Integer status) {
        return 1 == this.linkDao.setStatus(id, status);
    }
}
