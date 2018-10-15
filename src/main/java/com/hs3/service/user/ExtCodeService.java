package com.hs3.service.user;

import com.hs3.dao.extcode.ExtCodeDao;
import com.hs3.db.Page;
import com.hs3.entity.sys.SysClear;
import com.hs3.entity.users.ExtCode;
import com.hs3.utils.DateUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("extService")
public class ExtCodeService {
    @Autowired
    private ExtCodeDao extCodeDao;

    public String saveExtCode(ExtCode code) {
        Date date = new Date();
        code.setCreatetime(date);
        //code.setCode(UUID.randomUUID().toString().replaceAll("-", ""));
        int codeInt = (int) ((Math.random() * 9 + 1) * 10000000);
        String codeStr = String.valueOf(codeInt);
        code.setCode(codeStr);
        code.setStatus("0");
        code.setCanregists("0");
        code.setLastregist(date);
        return this.extCodeDao.save(code);
    }

    public List<Map<String, Object>> listWithRegistNum(Page p) {
        return this.extCodeDao.listWithRegistNum(p);
    }

    public List<Map<String, Object>> listWithRegistNum(String account, String code, Date beginCreateTime, Date endCreateTime, Integer bonusGroupId, BigDecimal minRebateRatio, BigDecimal maxRebateRatio, Integer lastTime, Page p) {
        Date lastRegist = null;
        if (lastTime != null) {
            lastRegist = DateUtils.getToDay(lastTime.intValue() * -1);
        }
        List<Map<String, Object>> rel = this.extCodeDao.listWithRegistNum(account, code,
                beginCreateTime, endCreateTime, bonusGroupId,
                minRebateRatio, maxRebateRatio,
                lastRegist, p);
        return rel;
    }

    public int delete(List<Integer> ids) {
        return this.extCodeDao.delete(ids);
    }

    public Integer setStatus(Integer id, Integer status) {
        return this.extCodeDao.setStatus(id, status);
    }

    public Integer setStatusByAccount(String account, Integer id, Integer status) {
        return this.extCodeDao.setStatusByAccount(account, id, status);
    }

    public ExtCode findByExtCode(String code) {
        return this.extCodeDao.findByExtCode(code);
    }

    public int deleteByClear(SysClear m) {
        return this.extCodeDao.deleteByClear(m);
    }

    public List<Map<String, Object>> listForExtCode(Page p, String code, Date beginCreateTime, Date endCreateTime) {
        return this.extCodeDao.listForExtCode(p, code, beginCreateTime, endCreateTime);
    }
}
