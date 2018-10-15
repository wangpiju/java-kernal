package com.hs3.web.utils;

import com.hs3.entity.contract.ContractRule;
import com.hs3.models.contract.ContractRuleModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContractCheckForm {
    public static String checkForm(ContractRuleModel m) {
        String str = "";
        if (m.getContractRuleList().size() > 0) {
            String a[] = {
                    "规则一", "规则二", "规则三", "规则四", "规则五", "规则六"
            };
            List<ContractRule> contractRuleList = m.getContractRuleList();
            ContractRule contractRule = (ContractRule) contractRuleList.get(0);
            Double gtdBonuses = contractRule.getGtdBonuses();
            if (gtdBonuses == null)
                return str = "请输入分红比例！";
            String regEx = "^[0-9]+(.[0-9]{0,2})?$";
            Pattern pat = Pattern.compile(regEx);
            Matcher mat = pat.matcher(String.valueOf(gtdBonuses));
            if (!mat.find())
                return str = "请输入有效数字，不可为负数，小数点限制2位。";
            if (gtdBonuses.doubleValue() > 100D || gtdBonuses.doubleValue() < 1.0D)
                return str = "请设定保底分红百分比在1.00到100.00之间";
            BigDecimal cumulativeSales = new BigDecimal("0");
            Integer humenNum = null;
            Double dividend = null;
            ContractRule contractRules = null;
            for (int i = 1; i < contractRuleList.size(); i++) {
                contractRules = (ContractRule) contractRuleList.get(i);
                if (i == 1) {
                    if (contractRules.getDividend() == null)
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的分红不能为空！").toString();

                    //jd-gui
					/*String regEx = "^[0-9]+(.[0-9]{0,2})?$";
					Pattern pat = Pattern.compile(regEx);
					Matcher mat = pat.matcher(String.valueOf(contractRules.getDividend()));*/
                    regEx = "^[0-9]+(.[0-9]{0,2})?$";
                    pat = Pattern.compile(regEx);
                    mat = pat.matcher(String.valueOf(contractRules.getDividend()));

                    if (!mat.find())
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的分红，请输入有效数字，不可为负数，小数点限制2位。").toString();
                    if (contractRules.getDividend().doubleValue() <= gtdBonuses.doubleValue())
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的分红请大于保底分红").toString();
                    if (contractRules.getDividend().doubleValue() > 100D || contractRules.getDividend().doubleValue() < 1.0D)
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("请设定分红百分比在1.00到100.00之间").toString();
                    dividend = contractRules.getDividend();
                    if (contractRules.getCumulativeSales() == null || contractRules.getCumulativeSales().compareTo(new BigDecimal("0")) == 0)
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的累积销量不能为空,且要大于0！").toString();
                    regEx = "^[0-9]+(.[0-9]{0,4})?$";
                    pat = Pattern.compile(regEx);
                    mat = pat.matcher(String.valueOf(contractRules.getCumulativeSales()));
                    if (!mat.find())
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的累积销量不能为空,且要大于0。").toString();
                    cumulativeSales = cumulativeSales.add(contractRules.getCumulativeSales());
                    if (contractRules.getHumenNum() == null || contractRules.getHumenNum().intValue() == 0)
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的投注人数，请输入自然整数（1,2,3....）").toString();
                    regEx = "^[1-9][0-9]*$";
                    pat = Pattern.compile(regEx);
                    mat = pat.matcher(String.valueOf(contractRules.getHumenNum()));
                    if (!mat.find())
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的投注人数，请输入自然整数（1,2,3....）").toString();
                    humenNum = contractRules.getHumenNum();
                } else {
                    if (contractRules.getDividend() == null)
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的分红不能为空！").toString();

                    //jd-gui
					/*String regEx = "^[0-9]+(.[0-9]{0,2})?$";
					Pattern pat = Pattern.compile(regEx);
					Matcher mat = pat.matcher(String.valueOf(contractRules.getDividend()));*/
                    regEx = "^[0-9]+(.[0-9]{0,2})?$";
                    pat = Pattern.compile(regEx);
                    mat = pat.matcher(String.valueOf(contractRules.getDividend()));


                    if (!mat.find())
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的分红，请输入有效数字，不可为负数，小数点限制2位。").toString();
                    if (contractRules.getDividend().doubleValue() <= dividend.doubleValue())
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的分红请大于前一规则分红").toString();
                    if (contractRules.getDividend().doubleValue() > 100D || contractRules.getDividend().doubleValue() < 1.0D)
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的分红，请设定分红百分比在1.00到100.00之间").toString();
                    if (contractRules.getCumulativeSales() == null || contractRules.getCumulativeSales().compareTo(new BigDecimal("0")) == 0)
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的累积销量不能为空,且要大于0！").toString();
                    regEx = "^[0-9]+(.[0-9]{0,4})?$";
                    pat = Pattern.compile(regEx);
                    mat = pat.matcher(String.valueOf(contractRules.getCumulativeSales()));
                    if (!mat.find())
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的累积销量不能为空,且要大于0。").toString();
                    if (contractRules.getCumulativeSales().compareTo(cumulativeSales) < 1)
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的累积销量请大于前一规则累积销量。").toString();
                    cumulativeSales = cumulativeSales.multiply(new BigDecimal("0")).add(contractRules.getCumulativeSales());
                    if (contractRules.getHumenNum() == null || contractRules.getHumenNum().intValue() == 0)
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的投注人数，请输入自然整数（1,2,3....）").toString();
                    regEx = "^[1-9][0-9]*$";
                    pat = Pattern.compile(regEx);
                    mat = pat.matcher(String.valueOf(contractRules.getHumenNum()));
                    if (!mat.find())
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的投注人数，请输入自然整数（1,2,3....）").toString();
                    if (contractRules.getHumenNum().intValue() <= humenNum.intValue())
                        return str = (new StringBuilder(String.valueOf(a[i]))).append("的投注人数请大于前一规则投注人数").toString();
                    humenNum = contractRules.getHumenNum();
                }
            }

        }
        return str;

    }
}
