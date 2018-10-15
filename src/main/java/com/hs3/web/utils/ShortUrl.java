package com.hs3.web.utils;

import com.hs3.entity.sys.SysConfig;
import com.hs3.service.sys.SysConfigService;
import com.hs3.utils.StrUtils;

import javax.servlet.http.HttpServletRequest;

public class ShortUrl {
    private static final String SHORT_URL = "SHORT_URL";
    private static SysConfigService sysConfigService = (SysConfigService) SpringContext.getBean(SysConfigService.class);

    public static String getUrl(HttpServletRequest request, String d) {
        SysConfig config = sysConfigService.find("SHORT_URL");
        String url = null;
        if (config != null) {
            if (!StrUtils.hasEmpty(new Object[]{config.getVal()})) {
            }
        } else {
            url = WebUtils.getDomainName(request) + WebUtils.getContextPath(request) + d;
            return url;
        }
        url = config.getVal() + "?code=";

        return url;
    }
}
