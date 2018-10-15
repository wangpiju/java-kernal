package com.pays.common;

import com.hs3.entity.finance.Recharge;
import com.pays.PayApiParam;
import org.codehaus.jackson.node.ObjectNode;

public interface IThirdPayCommon {
    String sendPayReqHtml(PayApiParam param);

    ObjectNode getCheckResult(Recharge recharge, String url);

}
