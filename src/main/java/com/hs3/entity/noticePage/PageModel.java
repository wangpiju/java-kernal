package com.hs3.entity.noticePage;

import java.util.List;

public class PageModel<E> {
    private List<E> list;
    private int totalRecords;
    private int pageSize;
    private int pageNo;

    public int getTotalPages() {
        return (this.totalRecords + this.pageSize - 1) / this.pageSize;
    }

    public int getTopPageNo() {
        return 1;
    }

    public int getPreviousPageNo() {
        if (this.pageNo <= 1) {
            return 1;
        }
        return this.pageNo - 1;
    }

    public int getNextPageNo() {
        if (this.pageNo >= getBottomPageNo()) {
            return getBottomPageNo();
        }
        return this.pageNo + 1;
    }

    public int getBottomPageNo() {
        return getTotalPages();
    }

    public List<E> getList() {
        return this.list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public int getTotalRecords() {
        return this.totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
