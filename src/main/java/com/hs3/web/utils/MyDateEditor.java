package com.hs3.web.utils;

import com.hs3.utils.DateUtils;

import java.beans.PropertyEditorSupport;
import java.util.Date;

public class MyDateEditor
        extends PropertyEditorSupport {
    public void setAsText(String text)
            throws IllegalArgumentException {
        Date d = DateUtils.toDateNull(text, "yyyy-MM-dd HH:mm:ss");
        if (d == null) {
            d = DateUtils.toDateNull(text, "yyyy-MM-dd");
        }
        if (d != null) {
            setValue(d);
        }
    }

    public String getAsText() {
        Date value = (Date) getValue();
        return DateUtils.format(value, "yyyy-MM-dd HH:mm:ss");
    }
}
