package com.hs3.service.article;

import com.hs3.dao.article.MessageContentDao;
import com.hs3.db.Page;
import com.hs3.entity.article.MessageContent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("messageContentService")
public class MessageContentService {
    @Autowired
    private MessageContentDao messageContentDao;

    public void save(MessageContent m) {
        this.messageContentDao.save(m);
    }

    public List<MessageContent> list(Page p) {
        return this.messageContentDao.list(p);
    }

    public List<MessageContent> listWithOrder(Page p) {
        return this.messageContentDao.listWithOrder(p);
    }

    public MessageContent find(Integer id) {
        return (MessageContent) this.messageContentDao.find(id);
    }

    public int update(MessageContent m) {
        return this.messageContentDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.messageContentDao.delete(ids);
    }
}
