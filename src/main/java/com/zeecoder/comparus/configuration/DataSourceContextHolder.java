package com.zeecoder.comparus.configuration;

public class DataSourceContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setCurrentDataSource(String dataSource) {
        CONTEXT.set(dataSource);
    }

    public static String getCurrentDataSource() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
