package com.xuehai.test.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

/**
 * @ClassName Launcher
 * @Description:    启动器实体类
 * @Author Sniper
 * @Date 2019/5/6 15:26
 */
public class Launcher {

    @JSONField(name = "ip")
    private String ip;
    @JSONField(name = "port")
    private int port;
    @JSONField(name = "bootstrapPort")
    private int bootstrapPort;
    @JSONField(name = "deviceId")
    private String deviceId;
    @JSONField(name = "logName")
    private String logName;
    @JSONField(name = "desiredCapabilities")
    private Map<String, Object> desiredCapabilities;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBootstrapPort() {
        return bootstrapPort;
    }

    public void setBootstrapPort(int bootstrapPort) {
        this.bootstrapPort = bootstrapPort;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Map<String, Object> getDesiredCapabilities() {
        return desiredCapabilities;
    }

    public void setDesiredCapabilities(Map<String, Object> desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
    }

    @Override
    public String toString() {
        return "Launcher{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", bootstrapPort=" + bootstrapPort +
                ", deviceId='" + deviceId + '\'' +
                ", logName='" + logName + '\'' +
                ", desiredCapabilities=" + desiredCapabilities +
                '}';
    }

}
