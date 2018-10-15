package com.tag;

import com.hs3.utils.auth.AuthUtils;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

public class AuthPermissionTag
        extends BodyTagSupport {
    private static final long serialVersionUID = 1L;
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public int doStartTag()
            throws JspException {
        HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
        if (AuthUtils.hasAuth(request, this.url)) {
            this.pageContext.setAttribute("url", request.getContextPath() + this.url);

            Tag tag = getParent();
            if ((tag != null) && (tag.getClass().equals(AuthGroupPermissionTag.class))) {
                AuthGroupPermissionTag authTag = (AuthGroupPermissionTag) tag;
                if (!authTag.getHasAuth()) {
                    authTag.setHasAuth(true);
                    String v = "<" + authTag.getElement();
                    if (authTag.getElementAttr() != null) {
                        v = v + " " + authTag.getElementAttr();
                    }
                    v = v + ">";
                    try {
                        this.pageContext.getOut().print(v);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return 1;
        }
        return 0;
    }
}
