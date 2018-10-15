package com.hs3.models;

import com.hs3.entity.finance.Deposit;
import com.hs3.entity.finance.Recharge;
import com.hs3.utils.DateUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Excel {
    private String fileName;
    private List<ExcelSheet> ems;
    private List<String> countFields = new ArrayList();

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<ExcelSheet> getEms() {
        return this.ems;
    }

    public void setEms(List<ExcelSheet> ems) {
        this.ems = ems;
    }

    public List<String> getCountFields() {
        return this.countFields;
    }

    public void setCountFields(List<String> countFields) {
        this.countFields = countFields;
    }

    private static final Map<String, String> map = new HashMap();

    static {
        map.put(Recharge.class.getName() + ".status.0", "未处理");
        map.put(Recharge.class.getName() + ".status.1", "拒绝");
        map.put(Recharge.class.getName() + ".status.2", "完成");
        map.put(Recharge.class.getName() + ".status.3", "已过期");
        map.put(Recharge.class.getName() + ".status.4", "已撤销");
        map.put(Recharge.class.getName() + ".status.5", "正在处理");
        map.put(Recharge.class.getName() + ".status.6", "审核中");

        map.put(Recharge.class.getName() + ".rechargeType.0", "银行充值");
        map.put(Recharge.class.getName() + ".rechargeType.1", "第三方充值");
        map.put(Recharge.class.getName() + ".rechargeType.2", "现金充值");

        map.put(Recharge.class.getName() + ".test.0", "非测试");
        map.put(Recharge.class.getName() + ".test.1", "测试");

        map.put(Recharge.class.getName() + ".createTime", "yyyy-MM-dd HH:mm:ss");
        map.put(Recharge.class.getName() + ".lastTime", "yyyy-MM-dd HH:mm:ss");

        map.put(Deposit.class.getName() + ".status.0", "未处理");
        map.put(Deposit.class.getName() + ".status.1", "拒绝");
        map.put(Deposit.class.getName() + ".status.2", "完成");
        map.put(Deposit.class.getName() + ".status.3", "已过期");
        map.put(Deposit.class.getName() + ".status.4", "已撤销");
        map.put(Deposit.class.getName() + ".status.5", "正在处理");
        map.put(Deposit.class.getName() + ".status.6", "审核中");
        map.put(Deposit.class.getName() + ".status.7", "审核通过");
        map.put(Deposit.class.getName() + ".status.8", "审核不通过");
        map.put(Deposit.class.getName() + ".status.99", "挂起");

        map.put(Deposit.class.getName() + ".test.0", "非测试");
        map.put(Deposit.class.getName() + ".test.1", "测试");

        map.put(Deposit.class.getName() + ".createTime", "yyyy-MM-dd HH:mm:ss");
        map.put(Deposit.class.getName() + ".lastTime", "yyyy-MM-dd HH:mm:ss");
    }

    private final String getDateFormat(Class<?> clazz, String field) {
        String key = clazz.getName() + "." + field;
        if (map.containsKey(key)) {
            return (String) map.get(key);
        }
        return null;
    }

    private final String getFormatValue(Class<?> clazz, String field, String value) {
        String key = clazz.getName() + "." + field + "." + value;
        if (map.containsKey(key)) {
            return (String) map.get(key);
        }
        return value;
    }

    public void fillValue(List<?> list)
            throws Exception {
        ExcelSheet es = (ExcelSheet) this.ems.get(0);

        Map<String, BigDecimal> countMap = new HashMap() {
            private static final long serialVersionUID = 1L;

            public BigDecimal put(String key, BigDecimal value) {
                BigDecimal v = (BigDecimal) get(key);
                if (v == null) {
                    v = BigDecimal.ZERO;
                }
                if (value == null) {
                    value = BigDecimal.ZERO;
                }
                value = value.add(v);
                return (BigDecimal) super.put(key, value);
            }
        };


        List<List<ExcelData>> eds = new ArrayList(list.size());
        List<ExcelData> ed;
        for (Iterator iterator = list.iterator(); iterator.hasNext(); eds.add(ed)) {
            Object obj = iterator.next();
            ed = new ArrayList();
            for (Iterator iterator2 = ((List) es.getEds().get(0)).iterator(); iterator2.hasNext(); ) {
                ExcelData excelData = (ExcelData) iterator2.next();
                ExcelData e = new ExcelData();
                String value = getObjValue(obj, excelData.getField());
                e.setFormatValue(value);
                ed.add(e);
                if (countFields.contains(excelData.getField()))
                    countMap.put(excelData.getField(), value != null ? ((BigDecimal) (new BigDecimal(value))) : ((BigDecimal) (BigDecimal.ZERO)));
            }

        }

        es.getEds().addAll(eds);
        if (!countFields.isEmpty()) {
            List<ExcelData> ed2 = new ArrayList();
            ExcelData e;
            for (Iterator iterator1 = ((List) es.getEds().get(0)).iterator(); iterator1.hasNext(); ed2.add(e)) {
                ExcelData excelData = (ExcelData) iterator1.next();
                e = new ExcelData();
                BigDecimal value = (BigDecimal) countMap.get(excelData.getField());
                e.setFormatValue(value != null ? value.toString() : "");
            }

            es.getEds().add(ed2);
        }


    }

    private String getObjValue(Object obj, String fieldName) {
        Object value = null;
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            value = field.get(obj);
            if ((value != null) &&
                    (field.getType() == Date.class)) {
                String format = getDateFormat(obj.getClass(), fieldName);
                if (format != null) {
                    value = DateUtils.format((Date) value, format);
                }
            }
        } catch (Exception localException) {
        }
        return value == null ? "" : getFormatValue(obj.getClass(), fieldName, value.toString());
    }
}
