package com.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class AuthGroupPermissionTag
        extends BodyTagSupport {
    private static final long serialVersionUID = 1L;
    private String element;
    private String elementAttr;
    private boolean hasAuth = false;

    public void setElement(String e) {
        this.element = e;
    }

    public String getElement() {
        return this.element;
    }

    public void setElementAttr(String e) {
        this.elementAttr = e;
    }

    public String getElementAttr() {
        return this.elementAttr;
    }

    public void setHasAuth(boolean hasAuth) {
        this.hasAuth = hasAuth;
    }

    public boolean getHasAuth() {
        return this.hasAuth;
    }

    public int doStartTag()
            throws JspException {
        this.hasAuth = false;
        return 1;
    }

    public int doEndTag()
            throws JspException {
        if (this.hasAuth) {
            String v = "</" + this.element + ">";
            try {
                this.pageContext.getOut().print(v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 6;
    }
}
