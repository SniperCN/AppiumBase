package com.xuehai.test.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.xuehai.test.base.Log;
import com.xuehai.test.http.HttpClientDispatch;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName AppiumUtil
 * @Description:    Appium工具类
 * @Author Sniper
 * @Date 2019/5/9 10:58
 */
public class AppiumUtil {

    private static final String CLASS_NAME = AppiumUtil.class.getName();

    /**
     * @description: 检测设备连接状态
     * @param deviceId      设备id
     * @return boolean
     * @throws
     * @author Sniper
     * @date 2019/5/23 9:10
     */
    public static boolean isDeviceConnect(String deviceId) {
        boolean flag = false;
        if (deviceId != null) {
            List<String> deviceList = getDeviceList();
            for (String temp : deviceList) {
                if (deviceId.equals(temp)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * @description: 获取设备id列表
     * @return java.util.List<java.lang.String>
     * @throws
     * @author Sniper
     * @date 2019/5/23 9:10
     */
    private static List<String> getDeviceList() {
        List<String> list = new ArrayList<>();
        String devicesInfo = CommonUtil.cmdExecutor("adb", "devices");
        Pattern pattern = Pattern.compile("attached(.*)");
        Matcher match = pattern.matcher(devicesInfo);
        if (match.find()) {
            String deviceInfo=match.group().replace("attached", "");
            pattern = Pattern.compile("(.+?)device");
            match = pattern.matcher(deviceInfo);
            while (match.find()) {
                String temp = match.group().replace("device", "").trim();
                list.add(temp);
            }
        }
        return list;
    }

    /**
     * @description: 检测driver状态
     * @param ip        ip
     * @param port      端口号
     * @return boolean
     * @throws
     * @author Sniper
     * @date 2019/5/23 9:11
     */
    public static boolean driverCheck(String ip, int port) {
        Log.info(CLASS_NAME, "检测AppiumDriver是否已创建");
        try {
            String url = "http://" + ip + ":" + port + "/wd/hub/sessions";
            String responseJson = HttpClientDispatch.getInstance().sendHttpGet(url);
            JSONObject response = JSONObject.parseObject(responseJson);
            String sessionId = (String) JSONPath.eval(response, "$.response.sessionId");
            if (!StringUtils.isEmpty(sessionId)) {
                Log.info(CLASS_NAME, "AppiumDriver已创建");
                return true;
            } else {
                Log.info(CLASS_NAME, "AppiumDriver未创建");
                return false;
            }
        } catch (IOException | JSONException e) {
            Log.error(CLASS_NAME, "AppiumDriver检测失败", e);
            return false;
        }
    }

    /**
     * @description: 检测appiumserver状态
     * @param ip        ip
     * @param port      端口号
     * @return boolean
     * @throws
     * @author Sniper
     * @date 2019/5/23 9:12
     */
    public static boolean serverCheck(String ip, int port) {
        Log.info(CLASS_NAME, "检测AppiumServer状态");
        try {
            String url = "http://" + ip + ":" + port + "/wd/hub/status";
            String responseJson = HttpClientDispatch.getInstance().sendHttpGet(url);
            JSONObject response = JSONObject.parseObject(responseJson);
            int status = (int) JSONPath.eval(response, "$.response.status");
            if (status == 0) {
                Log.info(CLASS_NAME, "AppiumServer Enabled");
                return true;
            } else {
                Log.info(CLASS_NAME, "AppiumServer Disabled");
                return false;
            }
        } catch (IOException | JSONException e) {
            Log.error(CLASS_NAME, "AppiumServer检测失败", e);
            return false;
        }
    }

    /**
     * @description: 启动appiumserver
     * @param ip                ip
     * @param port              端口号
     * @param bootstrapPort     bp port
     * @param deviceId          设备id
     * @param logName           日志名称
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/23 9:13
     */
    public static void appiumServerStart(String ip, int port, int bootstrapPort, String deviceId, String logName) {
        Log.info(CLASS_NAME, "启动AppiumServer");
        StringBuilder command = new StringBuilder("cmd /k start appium");
        if (!StringUtils.isEmpty(ip)) {
            command.append(" -a ").append(ip);
        }
        if (port > 0) {
            command.append(" -p ").append(port);
        }
        if (bootstrapPort > 0) {
            command.append(" -bp ").append(bootstrapPort);
        }
        if (!StringUtils.isEmpty(deviceId)) {
            command.append(" -U ").append(deviceId);
        }
        if (!StringUtils.isEmpty(logName)) {
            command.append(" --local-timezone -g logs/").append(logName).append(".log");
        }
        CommonUtil.cmdExecutor(command.toString());
        Log.info(CLASS_NAME, "AppiumServer启动成功");
    }

    /**
     * @description: 关闭appiumserver
     * @param port      端口号
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/23 9:14
     */
    public static void appiumServerClose(int port) {
        Log.info(CLASS_NAME, "关闭AppiumServer,端口:{}", port);
        String netstat = CommonUtil.cmdExecutor("netstat", "-ano", "|", "findstr", String.valueOf(port));
        Pattern pattern = Pattern.compile("\\d+?$");
        Matcher match = pattern.matcher(netstat);
        if (match.find()) {
            String pid = match.group();
            CommonUtil.killProcess(pid);
        }
        Log.info(CLASS_NAME, "AppiumServer关闭成功");
    }


}
