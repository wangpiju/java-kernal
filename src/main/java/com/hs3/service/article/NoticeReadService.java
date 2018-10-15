package com.hs3.service.article;

import com.hs3.dao.article.NoticeReadDao;
import com.hs3.entity.article.NoticeRead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * program: java-kernal
 * des:
 * author: Terra
 * create: 2018-08-07 14:45
 **/
@Service("noticeReadService")
public class NoticeReadService {

    @Autowired
    private NoticeReadDao noticeReadDao;

    public List<Integer> queryReadIds(String account, Integer type, Integer[] noticeIds) {
        return noticeReadDao.queryReadIds(account, type, noticeIds);
    }

    public boolean save(NoticeRead nr) {
        return noticeReadDao.saveAuto(nr) > 0;
    }

    public NoticeRead getByNoticeId(Integer noticeId, String account) {
        return noticeReadDao.getByNoticeId(noticeId,account);
    }

}
