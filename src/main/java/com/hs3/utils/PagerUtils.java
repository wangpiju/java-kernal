package com.hs3.utils;

import java.util.Collections;
import java.util.List;

public class PagerUtils<T> {
    private int pageSize;
    private List<T> data;

    private PagerUtils(List<T> data, int pageSize) {
        if ((data == null) || (data.isEmpty())) {
            throw new IllegalArgumentException("data must be not empty!");
        }
        this.data = data;
        this.pageSize = pageSize;
    }

    public static <T> PagerUtils<T> create(List<T> data, int pageSize) {
        return new PagerUtils(data, pageSize);
    }

    public List<T> getPagedList(int pageNum) {
        int fromIndex = (pageNum - 1) * this.pageSize;
        if (fromIndex >= this.data.size()) {
            return Collections.emptyList();
        }
        int toIndex = pageNum * this.pageSize;
        if (toIndex >= this.data.size()) {
            toIndex = this.data.size();
        }
        return this.data.subList(fromIndex, toIndex);
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public List<T> getData() {
        return this.data;
    }
}
