package com.springcloud.framework.core.context;

public class YContext {
    // TODO 需要考虑hystrix情况
    private static ThreadLocal<YRequestHeader> requestHeaderThreadLocal = new ThreadLocal<>();

    public static YRequestHeader getRequestHeader() {
        return requestHeaderThreadLocal.get();
    }

    public static void setRequestHeader(YRequestHeader requestHeader) {
        requestHeaderThreadLocal.set(requestHeader);
    }
}
