package com.hs3.tasks.sys;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.quartz.Job;

public final class SysClearJobFactory {
    public static final String KEY_SYS_CLEAR = "sys_clear";
    public static final String NAME_GROUP = "系统数据清理任务";
    private static Map<Integer, SysClearJobEnum> jobClassMap = new HashMap<>();
    private static Map<Integer, String> descMap = new TreeMap<>();

    static {
        for (SysClearJobEnum sysClearJobEnum : SysClearJobEnum.values()) {
            jobClassMap.put(sysClearJobEnum.getValue(), sysClearJobEnum);
            descMap.put(sysClearJobEnum.getValue(), sysClearJobEnum.getDesc());
        }
    }

    public static SysClearJobEnum getSysClearJobEnum(Integer key) {
        return jobClassMap.get(key);
    }

    public static Class<? extends Job> getJobClass(Integer key) {
        return ((SysClearJobEnum) jobClassMap.get(key)).getClazz();
    }

    public static String getDesc(Integer key) {
        return descMap.get(key);
    }

    public static Map<Integer, String> getJobs() {
        return descMap;
    }
}
