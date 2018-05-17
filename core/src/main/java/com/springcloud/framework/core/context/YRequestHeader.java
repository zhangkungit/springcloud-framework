
package com.springcloud.framework.core.context;

import java.io.Serializable;

/**
 * http request header信息对象
 *
 *
 */
public class YRequestHeader implements Serializable {
    /**
     * 应用id
     */
    private String appId;
    /**
     * app版本号
     */
    private String appVersion;
    /**
     * 客户端版本号
     */
    private String clientVersion;
    /**
     * 设备id
     */
    private String devId;
    /**
     * 设备名称
     */
    private String devName;
    /**
     * 设备类型   1IOS, 2Android, 3WEB
     */
    private String devType;
    /**
     * app应用渠道号
     */
    private String ditchCode;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 网络类型
     */
    private String net;
    /**
     * 签名
     */
    private String sign;
    /**
     * 短期令牌
     */
    private String token;
    /**
     * User-Agent
     */
    private String userAgent;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 请求ip
     */
    private String xForwardedFor;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getDitchCode() {
        return ditchCode;
    }

    public void setDitchCode(String ditchCode) {
        this.ditchCode = ditchCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getxForwardedFor() {
        return xForwardedFor;
    }

    public void setxForwardedFor(String xForwardedFor) {
        this.xForwardedFor = xForwardedFor;
    }
}
