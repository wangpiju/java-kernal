package com.hs3.service.article;

import com.hs3.dao.article.NoticeDao;
import com.hs3.db.Page;
import com.hs3.entity.article.Notice;
import com.hs3.entity.noticePage.PageModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("noticeService")
public class NoticeService {
    @Autowired
    private NoticeDao noticeDao;

    public List<Notice> listByCond(Notice m, Page page) {
        return this.noticeDao.listByCond(m, page);
    }

    public void save(Notice m) {
        this.noticeDao.save(m);
    }

    public List<Notice> listWithOrder(Page p) {
        return this.noticeDao.listWithOrder(p);
    }

    public Notice find(Integer id) {
        return (Notice) this.noticeDao.find(id);
    }

    public int update(Notice m) {
        return this.noticeDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.noticeDao.delete(ids);
    }

    public List<Notice> getNoticeList(Integer num) {
        return this.noticeDao.getNoticeList(num);
    }

    public int getOpenNum(Integer switching) {
        return this.noticeDao.getOpenNum(switching);
    }

    public int updateOpenOrClose(String id, Integer switching) {
        return this.noticeDao.updateOpenOrClose(id, switching);
    }

    public Notice getNotice(int i) {
        return this.noticeDao.getNotice(i);
    }

    public Notice first() {
        return this.noticeDao.first();
    }

    public PageModel<Notice> listByPage(int pageNo, int pageSize) {
        PageModel<Notice> pageModel = null;
        List<Notice> list = this.noticeDao.listByPage(pageNo, pageSize);
        int total = this.noticeDao.countByPage();
        pageModel = new PageModel();
        pageModel.setPageNo(pageNo);
        pageModel.setPageSize(pageSize);
        pageModel.setTotalRecords(total);
        pageModel.setList(list);
        return pageModel;
    }
}
