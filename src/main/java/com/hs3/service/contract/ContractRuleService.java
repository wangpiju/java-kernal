package com.hs3.service.contract;

import com.hs3.dao.article.MessageContentDao;
import com.hs3.dao.article.MessageDao;
import com.hs3.dao.contract.ContractMessageDao;
import com.hs3.dao.contract.ContractRuleDao;
import com.hs3.dao.user.UserDao;
import com.hs3.db.Page;
import com.hs3.entity.article.Message;
import com.hs3.entity.article.MessageContent;
import com.hs3.entity.contract.ContractRule;
import com.hs3.entity.users.User;
import com.hs3.models.contract.ContractRuleModel;
import com.hs3.utils.StrUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("contractRuleService")
public class ContractRuleService {
    @Autowired
    private ContractRuleDao contractRuleDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MessageContentDao messageContentDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private ContractMessageDao contractMessageDao;
    protected static final String MESSAGE_TITLE = "契约通知";
    protected static final String CREATE_MESSAGE = "您的上级希望您接受TA发起的契约分红,请查看-个人中心-我的契约";
    protected static final String UPDATE_MESSAGE = "您的上级更改了你们之间的契约分红,请查看-个人中心-我的契约";
    protected static final String AGREE_MESSAGE = "同意了你们之间的契约分红,请查看-团队管理-会员管理";
    protected static final String UNDO_MESSAGE = "拒绝了你们之间的契约分红,请查看-团队管理-会员管理";
    protected static final Integer USER_CONTRACT_STATUS_0 = Integer.valueOf(0);
    protected static final Integer USER_CONTRACT_STATUS_1 = Integer.valueOf(1);
    protected static final Integer USER_CONTRACT_STATUS_2 = Integer.valueOf(2);
    protected static final Integer USER_CONTRACT_STATUS_3 = Integer.valueOf(3);
    protected static final Integer USER_CONTRACT_STATUS_4 = Integer.valueOf(4);
    protected static final Integer USER_CONTRACT_STATUS_8 = Integer.valueOf(8);
    protected static final Integer USER_CONTRACT_STATUS_9 = Integer.valueOf(9);
    protected static final Integer MESSAGE_STATUS_0 = Integer.valueOf(0);
    protected static final Integer MESSAGE_STATUS_1 = Integer.valueOf(1);

    public void save(ContractRule m) {
        this.contractRuleDao.save(m);
    }

    public List<ContractRule> contractRuleList(Page p, String account) {
        return this.contractRuleDao.contractRuleList(p, account);
    }

    public ContractRuleModel findContractRule(String account, Integer contractStatus) {
        boolean i = false;
        if (contractStatus.intValue() == 2) {
            i = this.contractRuleDao.checkContractRule(account, USER_CONTRACT_STATUS_2);
        } else if (contractStatus.intValue() == 0) {
            i = this.contractRuleDao.checkContractRule(account, USER_CONTRACT_STATUS_0);
        }
        List<ContractRule> contractRuleList = null;
        if (i) {
            if (contractStatus.intValue() == 2) {
                contractRuleList = this.contractRuleDao.findEntityByAccount(account, USER_CONTRACT_STATUS_2);
            } else if (contractStatus.intValue() == 0) {
                contractRuleList = this.contractRuleDao.findEntityByAccount(account, USER_CONTRACT_STATUS_0);
            }
        } else {
            contractRuleList = this.contractRuleDao.findEntityByAccount(account, USER_CONTRACT_STATUS_1);
        }
        ContractRuleModel contractRuleModel = new ContractRuleModel();
        Iterator localIterator = contractRuleList.iterator();
        if (localIterator.hasNext()) {
            ContractRule a = (ContractRule) localIterator.next();
            contractRuleModel.setAccount(account);
            contractRuleModel.setContractStatus(a.getContractStatus());
            contractRuleModel.setContractTime(a.getContractTime());
            if (StrUtils.hasEmpty(new Object[]{a.getParentAccount()})) {
                contractRuleModel.setType(Integer.valueOf(0));
            } else {
                contractRuleModel.setType(Integer.valueOf(1));
            }
        }
        if (contractRuleList.size() > 0) {
            contractRuleModel.setContractRuleList(contractRuleList);
            return contractRuleModel;
        }
        return null;
    }

    public ContractRuleModel findMyContractRule(String account, Integer contractStatus) {
        List<ContractRule> contractRuleList = this.contractRuleDao.findEntityByAccount(account, contractStatus);
        ContractRuleModel contractRuleModel = new ContractRuleModel();
        Iterator localIterator = contractRuleList.iterator();
        if (localIterator.hasNext()) {
            ContractRule a = (ContractRule) localIterator.next();
            contractRuleModel.setAccount(account);
            contractRuleModel.setContractStatus(a.getContractStatus());
            contractRuleModel.setContractTime(a.getContractTime());
        }
        if (contractRuleList.size() > 0) {
            contractRuleModel.setContractRuleList(contractRuleList);
            return contractRuleModel;
        }
        return null;
    }

    public int updateToActive(String account) {
        return this.userDao.updateContractStatus(account, USER_CONTRACT_STATUS_3);
    }

    public int updateToclose(String account) {
        return this.userDao.updateContractStatus(account, USER_CONTRACT_STATUS_4);
    }

    public void addAndUpdateContract(ContractRuleModel m, String adminAccount) {
        String account = m.getAccount().trim();
        User user = this.userDao.findByAccount(account);

        List<ContractRule> contractRuleList = m.getContractRuleList();
        for (ContractRule e : contractRuleList) {
            e.setAccount(account);
            e.setRootAccount(user.getRootAccount());
            if ((!account.equals(user.getParentAccount())) && (adminAccount == null)) {
                e.setParentAccount(user.getParentAccount());
            }
            e.setContractStatus(USER_CONTRACT_STATUS_0);
            e.setContractTime(new Date());
            this.contractRuleDao.save(e);
        }
        Message msg = new Message();
        msg.setRevStatus(Integer.valueOf(0));
        msg.setSendType(Integer.valueOf(0));
        msg.setSendStatus(Integer.valueOf(0));
        msg.setSendTime(new Date());
        if (!StrUtils.hasEmpty(new Object[]{adminAccount})) {
            msg.setSender(adminAccount);
        } else {
            msg.setSender(user.getParentAccount());
        }
        msg.setRever(account);
        msg.setTitle("契约通知");
        msg.setSendContent("您的上级希望您接受TA发起的契约分红,请查看-个人中心-我的契约");
        int messageId = this.messageDao.save(msg);

        MessageContent mc = new MessageContent();
        mc.setContent("您的上级希望您接受TA发起的契约分红,请查看-个人中心-我的契约");
        mc.setCreateTime(new Date());
        mc.setMessageId(Integer.valueOf(messageId));
        mc.setRever(msg.getRever());
        mc.setSender(msg.getSender());
        this.messageContentDao.save(mc);

        this.userDao.updateContractStatus(account, USER_CONTRACT_STATUS_0);
    }

    public int delete(String account, Integer contractStatus) {
        return this.contractRuleDao.delete(account, contractStatus);
    }

    public int updateToAgree(String account, String parentAccount) {
        this.contractRuleDao.delete(account, USER_CONTRACT_STATUS_1);

        int i = this.contractRuleDao.updateStatus(account, USER_CONTRACT_STATUS_1, new Date());
        if (!account.equals(parentAccount)) {
            Message msg = new Message();
            msg.setRevStatus(Integer.valueOf(0));
            msg.setSendType(Integer.valueOf(0));
            msg.setSendStatus(Integer.valueOf(0));
            msg.setSendTime(new Date());
            msg.setSender(account);
            msg.setRever(parentAccount);
            msg.setTitle("契约通知");
            msg.setSendContent("您的下级" + account + "同意了你们之间的契约分红,请查看-团队管理-会员管理");
            int messageId = this.messageDao.save(msg);

            MessageContent mc = new MessageContent();
            mc.setContent("您的下级" + account + "同意了你们之间的契约分红,请查看-团队管理-会员管理");
            mc.setCreateTime(new Date());
            mc.setMessageId(Integer.valueOf(messageId));
            mc.setRever(msg.getRever());
            mc.setSender(msg.getSender());
            this.messageContentDao.save(mc);
        }
        this.userDao.updateContractStatus(account, USER_CONTRACT_STATUS_1);
        return i;
    }

    public int updateTorRefuse(String account, String parentAccount) {
        Message msg = new Message();
        msg.setRevStatus(Integer.valueOf(0));
        msg.setSendType(Integer.valueOf(0));
        msg.setSendStatus(Integer.valueOf(0));
        msg.setSendTime(new Date());
        msg.setSender(account);
        msg.setRever(parentAccount);
        msg.setTitle("契约通知");
        msg.setSendContent("您的下级" + account + "拒绝了你们之间的契约分红,请查看-团队管理-会员管理");
        int messageId = this.messageDao.save(msg);

        MessageContent mc = new MessageContent();
        mc.setContent("您的下级" + account + "拒绝了你们之间的契约分红,请查看-团队管理-会员管理");
        mc.setCreateTime(new Date());
        mc.setMessageId(Integer.valueOf(messageId));
        mc.setRever(msg.getRever());
        mc.setSender(msg.getSender());
        this.messageContentDao.save(mc);


        boolean n = this.contractRuleDao.checkContractRule(account, USER_CONTRACT_STATUS_1);
        if (n) {
            this.userDao.updateContractStatus(account, USER_CONTRACT_STATUS_1);
        } else {
            this.userDao.updateContractStatus(account, USER_CONTRACT_STATUS_2);
        }
        return this.contractRuleDao.updateStatusByBefore(account, USER_CONTRACT_STATUS_0, USER_CONTRACT_STATUS_2, new Date());
    }

    public int updateToUndo(String account, String parentAccount, Integer contractStatus) {
        int i = 0;
        if (contractStatus.intValue() == 0) {
            i = this.contractRuleDao.delete(account, USER_CONTRACT_STATUS_0);
        } else {
            i = this.contractRuleDao.delete(account, USER_CONTRACT_STATUS_2);
        }
        boolean n = this.contractRuleDao.checkContractRule(account, USER_CONTRACT_STATUS_1);
        if (n) {
            this.userDao.updateContractStatus(account, USER_CONTRACT_STATUS_1);
        } else {
            this.userDao.updateContractStatus(account, USER_CONTRACT_STATUS_8);
        }
        return i;
    }

    public int findByAccount(String account) {
        return this.contractRuleDao.findByAccount(account);
    }

    public int updateContractBySys(User u, ContractRuleModel m) {
        String parentAccount = null;
        Integer userContractStatus = Integer.valueOf(8);
        if (1 == m.getType().intValue()) {
            parentAccount = u.getParentAccount();
            userContractStatus = USER_CONTRACT_STATUS_1;
        } else {
            userContractStatus = USER_CONTRACT_STATUS_9;
        }
        this.contractRuleDao.deleteByAccount(u.getAccount());

        List<ContractRule> contractRuleList = m.getContractRuleList();
        Date d = new Date();
        for (ContractRule e : contractRuleList) {
            e.setAccount(u.getAccount());
            e.setRootAccount(u.getRootAccount());
            e.setParentAccount(parentAccount);
            e.setContractStatus(USER_CONTRACT_STATUS_1);
            e.setContractTime(d);
            this.contractRuleDao.save(e);
        }
        this.userDao.updateContractStatus(u.getAccount(), userContractStatus);
        return contractRuleList.size();
    }

    public int deleteContract(String account, Integer contractStatus) {
        int i = this.contractRuleDao.delete(account, contractStatus);

        ContractRule rule = this.contractRuleDao.checkHasSys(account, USER_CONTRACT_STATUS_1);
        if (rule != null) {
            if (StrUtils.hasEmpty(new Object[]{rule.getParentAccount()})) {
                this.userDao.updateContractStatus(account, USER_CONTRACT_STATUS_9);
            } else {
                this.userDao.updateContractStatus(account, USER_CONTRACT_STATUS_1);
            }
        } else {
            this.userDao.updateContractStatus(account, USER_CONTRACT_STATUS_8);
        }
        return i;
    }
}
