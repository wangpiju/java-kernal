package com.hs3.db;

import com.alibaba.fastjson.JSON;
import com.hs3.utils.NumUtils;

import java.util.*;

public class Page {
    private final int pageSize;
    private int nowPage;
    private int pageCount;
    private int rowCount;
    private int startIndex;
    private Object obj;
    public static final String PAGE_NAME = "page";
    public static final String SIZE_NAME = "rows";
    public static int DEFAULT_SIZE = 25;
    private Map<String, Object> list = new HashMap<>();

    public Page() {
        this.nowPage = 1;
        this.pageSize = DEFAULT_SIZE;
    }

    public Page(Object nowPage, Object pageSize) {
        int p = NumUtils.toInt(nowPage);
        int ps = NumUtils.toInt(pageSize);
        if (p < 1) {
            p = 1;
        }
        if (ps < 1) {
            ps = DEFAULT_SIZE;
        }
        this.nowPage = p;
        this.pageSize = ps;
    }

    public int getNowPage() {
        return this.nowPage;
    }

    public void setNowPage(int nowPage) {
        this.nowPage = nowPage;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int getNext() {
        int next = this.nowPage + 1;
        if (next > this.pageCount) {
            return this.pageCount;
        }
        return next;
    }

    public int getPrev() {
        int prev = this.nowPage - 1;
        if (prev < 1) {
            return 1;
        }
        return prev;
    }

    public int getStartIndex() {
        if (this.rowCount > 0) {
            return this.startIndex;
        } else {
            return (this.nowPage - 1) * this.pageSize;
        }

    }

    public Object getObj() {
        return this.obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        if (this.rowCount > 0) {
            this.pageCount = (rowCount / this.pageSize);
            this.pageCount += (rowCount % this.pageSize == 0 ? 0 : 1);
        } else {
            this.pageCount = 1;
            this.nowPage = 1;
        }
        if (this.nowPage > this.pageCount) {
            this.nowPage = this.pageCount;
        }
        this.startIndex = ((this.nowPage - 1) * this.pageSize);
    }

    public String getParams() {
        Set<String> without = null;
        return getParams(without);
    }

    public String getParams(List<String> without) {
        Set<String> with = new HashSet<>(without);
        return getParams(with);
    }

    public String getParams(String[] without) {
        Set<String> with = new HashSet<>(Arrays.asList(without));
        return getParams(with);
    }

    public String getParams(Set<String> without) {
        StringBuilder params = new StringBuilder();
        for (String k : this.list.keySet()) {
            if ((without == null) || (!without.contains(k))) {
                if (!k.equalsIgnoreCase("rows")) {
                    if (!k.equalsIgnoreCase("page")) {
                        params.append(k).append("=").append(this.list.get(k)).append("&");
                    }
                }
            }
        }
        if ((without == null) || (!without.contains("rows"))) {
            params.append("rows").append("=").append(getPageSize()).append("&");
        }
        if ((without == null) || (!without.contains("page"))) {
            params.append("page").append("=");
        }
        return params.toString();
    }

    public String getParamsOnly(Set<String> with) {
        if (with == null) {
            return null;
        }
        StringBuilder params = new StringBuilder();
        if (with.size() > 0) {
            for (String k : this.list.keySet()) {
                if (with.contains(k)) {
                    params.append("&").append(k).append("=").append(this.list.get(k));
                }
            }
            if (params.length() > 0) {
                params.deleteCharAt(0);
            }
        }
        return params.toString();
    }

    public String getParamsOnly(List<String> without) {
        Set<String> with = new HashSet<>(without);
        return getParamsOnly(with);
    }

    public String getParamsOnly(String[] without) {
        Set<String> with = new HashSet<>(Arrays.asList(without));
        return getParamsOnly(with);
    }

    public void addParams(String key, String val) {
        this.list.put(key, val);
    }

    public List<Integer> getShowPages() {
        int curPage = this.nowPage;
        List<Integer> showPages = new ArrayList<>(11);
        int pageCount = getPageCount();
        if (pageCount < 1) {
            return showPages;
        }
        if (curPage < 6) {
            if (pageCount < 10) {
                for (int i = 1; i <= pageCount; i++) {
                    showPages.add(i);
                }
            } else {
                for (int i = 1; i < 11; i++) {
                    showPages.add(i);
                }
            }
        } else {
            showPages.add(curPage - 4);
            showPages.add(curPage - 3);
            showPages.add(curPage - 2);
            showPages.add(curPage - 1);
            showPages.add(curPage);
            int afterEndIndex = curPage + 5 > pageCount ? pageCount : curPage + 5;
            for (int i = curPage + 1; i <= afterEndIndex; i++) {
                showPages.add(i);
            }
        }
        return showPages;
    }

}
