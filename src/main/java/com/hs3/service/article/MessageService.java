package com.hs3.service.article;

import com.hs3.dao.article.MessageContentDao;
import com.hs3.dao.article.MessageDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.article.Message;
import com.hs3.entity.article.MessageContent;
import com.hs3.entity.users.User;
import com.hs3.exceptions.BaseCheckException;
import com.hs3.utils.StrUtils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("messageService")
public class MessageService {
    public static int MESSAGE_LENGTH_TITLE = 50;
    public static int MESSAGE_LENGTH_CONTENT = 500;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private MessageContentDao messageContentDao;
    @Autowired
    private UserDao userDao;

    public List<MessageContent> listContentByCond(MessageContent m, Date startTime, Date endTime, Page page) {
        return this.messageContentDao.listByCond(m, startTime, endTime, page);
    }

    public void saveContent(Message m, MessageContent mc) {
        this.messageContentDao.save(mc);
        if (mc.getRever().equals(m.getRever())) {
            m.setRevStatus(Integer.valueOf(0));
        } else {
            m.setSendStatus(Integer.valueOf(4));
        }
        this.messageDao.updateByCond(m);
    }

    public List<Message> listToShowByUser(String account, Page page) {
        return this.messageDao.listToShowByUser(account, page);
    }

    public int countUnRead(String account) {
        return this.messageDao.countUnRead(account);
    }

    public int countByCond(Message m) {
        return this.messageDao.countByCond(m);
    }

    public List<Message> listByCond(Message m, Page page) {
        return this.messageDao.listByCond(m, page);
    }

    public Message updateByUserRead(Message m)
            throws BaseCheckException {
        Message message = find(m.getId());
        if (message == null) {
            throw new BaseCheckException("消息不存在！");
        }
        boolean isRever = message.getRever().equals(m.getRever());
        boolean isSender = message.getSender().equals(m.getRever());
        if ((!isRever) && (!isSender)) {
            throw new BaseCheckException("用户没有权限读取！");
        }
        if ((isRever) && (message.getRevStatus().intValue() == 0)) {
            message.setRevStatus(Integer.valueOf(1));
            if (this.messageDao.updateByCond(message) != 1) {
                throw new BaseCheckException("更新状态失败！");
            }
        }
        if ((isSender) && (message.getSendStatus().intValue() == 4)) {
            message.setSendStatus(Integer.valueOf(5));
            if (this.messageDao.updateByCond(message) != 1) {
                throw new BaseCheckException("更新状态失败！");
            }
        }
        return message;
    }

    public int deleteByUser(Message m)
            throws BaseCheckException {
        Message message = find(m.getId());
        boolean isRever = message.getRever().equals(m.getRever());
        boolean isSender = message.getSender().equals(m.getRever());
        if ((!isRever) && (!isSender)) {
            throw new BaseCheckException("没有权限删除！");
        }
        if (isRever) {
            message.setRevStatus(Integer.valueOf(3));
        } else if (isSender) {
            message.setSendStatus(Integer.valueOf(3));
        }
        return this.messageDao.updateByCond(message);
    }

    public void saveByUser(Message m)
            throws BaseCheckException {
        User sender = checkUserExists(m.getSender());
        User rever = checkUserExists(m.getRever());
        if ((!rever.getParentAccount().equals(sender.getAccount())) && (!sender.getParentAccount().equals(rever.getAccount()))) {
            throw new BaseCheckException("发送消息失败，只能给上级或下级发送消息！");
        }
        if (sender.getAccount().equals(rever.getAccount())) {
            throw new BaseCheckException("发送消息失败，不能给自己发消息！");
        }
        if (StrUtils.hasEmpty(new Object[]{m.getTitle()})) {
            throw new BaseCheckException("标题不能为空！");
        }
        if (m.getTitle().length() > MESSAGE_LENGTH_TITLE) {
            throw new BaseCheckException("标题长度不能大于" + MESSAGE_LENGTH_TITLE + "字符！");
        }
        if (StrUtils.checkHadSpecialChar(m.getTitle())) {
            throw new BaseCheckException("标题包含特殊字符！");
        }
        if (StrUtils.hasEmpty(new Object[]{m.getSendContent()})) {
            throw new BaseCheckException("发送内容不能为空！");
        }
        if (m.getSendContent().length() > MESSAGE_LENGTH_CONTENT) {
            throw new BaseCheckException("发送内容长度不能大于" + MESSAGE_LENGTH_CONTENT + "字符！");
        }
        if (StrUtils.checkHadSpecialChar(m.getSendContent())) {
            throw new BaseCheckException("发送内容包含特殊字符！");
        }
        Date now = new Date();
        m.setRevStatus(Integer.valueOf(0));
        m.setSendType(Integer.valueOf(1));
        m.setSendStatus(Integer.valueOf(0));
        m.setSendTime(now);


        m.setSender(sender.getAccount());
        m.setRever(rever.getAccount());

        int messageId = this.messageDao.save(m);

        MessageContent mc = new MessageContent();
        mc.setContent(m.getSendContent());
        mc.setCreateTime(now);
        mc.setMessageId(Integer.valueOf(messageId));
        mc.setRever(m.getRever());
        mc.setSender(m.getSender());
        this.messageContentDao.save(mc);
    }

    public int updateCancel(Integer id)
            throws BaseCheckException {
        Message m = new Message();
        m.setId(id);
        m.setSendStatus(Integer.valueOf(1));

        return this.messageDao.updateByCond(m);
    }

    public void saveGroup(Message m)
            throws BaseCheckException {
        List<User> list = this.userDao.listByParent(m.getRever());
        for (User user : list) {
            if (!m.getRever().equals(user.getAccount())) {
                m.setRever(user.getAccount());
                saveSingle(m);
            }
        }
    }

    public void saveList(Message m)
            throws BaseCheckException {
        for (String account : m.getRever().split(",")) {
            m.setRever(account);
            saveSingle(m);
        }
    }

    public void saveSingle(Message m)
            throws BaseCheckException {
        checkUserExists(m.getRever());

        int messageId = this.messageDao.save(m);

        MessageContent mc = new MessageContent();
        mc.setContent(m.getSendContent());
        mc.setCreateTime(new Date());
        mc.setMessageId(Integer.valueOf(messageId));
        mc.setRever(m.getRever());
        mc.setSender(m.getSender());
        this.messageContentDao.save(mc);
    }

    private User checkUserExists(String account)
            throws BaseCheckException {
        User user = this.userDao.findByAccount(account);
        if (user == null) {
            throw new BaseCheckException("[" + account + "]不存在！");
        }
        return user;
    }

    public void save(Message m) {
        this.messageDao.save(m);
    }

    public List<Message> list(Page p) {
        return this.messageDao.list(p);
    }

    public List<Message> listWithOrder(Page p) {
        return this.messageDao.listWithOrder(p);
    }

    public Message find(Integer id) {
        return (Message) this.messageDao.find(id);
    }

    public int update(Message m) {
        return this.messageDao.update(m);
    }

    public int delete(List<Integer> ids) {
        return this.messageDao.delete(ids);
    }
}
