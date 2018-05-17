package com.springcloud.framework.core.context;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class YFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            YRequestHeader requestHeader = new YRequestHeader();
            requestHeader.setAppId(request.getHeader("appId"));
            requestHeader.setAppVersion(request.getHeader("appVersion"));
            requestHeader.setClientVersion(request.getHeader("clientVersion"));
            requestHeader.setDevId(request.getHeader("devId"));
            requestHeader.setDevName(request.getHeader("devName"));
            requestHeader.setDevType(request.getHeader("devType"));
            requestHeader.setDitchCode(request.getHeader("ditchCode"));
            requestHeader.setIp(request.getHeader("ip"));
            requestHeader.setNet(request.getHeader("net"));
            requestHeader.setSign(request.getHeader("sign"));
            requestHeader.setToken(request.getHeader("token"));
            requestHeader.setUserAgent(request.getHeader("User-Agent"));
            requestHeader.setUserId(request.getHeader("userId"));
            requestHeader.setxForwardedFor(getClientIP(request));

            YContext.setRequestHeader(requestHeader);
        } catch (Exception e) {
            // 不处理 没有request header没关系
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
        return ip;
    }

    @Override
    public void destroy() {

    }
}
