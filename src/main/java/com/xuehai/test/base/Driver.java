package com.xuehai.test.base;

import com.xuehai.test.model.Launcher;
import com.xuehai.test.utils.AppiumUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * @ClassName Driver
 * @Description:    Driver管理
 * @Author Sniper
 * @Date 2019/5/6 16:56
 */
public class Driver{

    private static final String CLASS_NAME = Driver.class.getName();

    public static AppiumDriver<MobileElement> createAppiumDriver(Launcher launcher) {
        AppiumDriver<MobileElement> driver = null;
        try {
            Log.info(CLASS_NAME, "创建AppiumDriver,当前Launcher:{}", launcher);
            if (launcher != null) {
                String ip = launcher.getIp();
                int port = launcher.getPort();
                int bootstrapPort = launcher.getBootstrapPort();
                String deviceId = launcher.getDeviceId();
                String logName = launcher.getLogName();
                Map<String, Object> desiredCapabilities = launcher.getDesiredCapabilities();
                if (AppiumUtil.isDeviceConnect(deviceId)) {
                    while (!AppiumUtil.serverCheck(ip, port)) {
                        AppiumUtil.appiumServerStart(ip, port, bootstrapPort, deviceId, logName);
                        Thread.sleep(10000);
                    }
                    if (!AppiumUtil.driverCheck(ip, port)) {
                        DesiredCapabilities capabilities = new DesiredCapabilities();
                        desiredCapabilities.forEach(capabilities::setCapability);
                        String url = "http://" + ip + ":" + port + "/wd/hub";
                        driver = new AppiumDriver<>(new URL(url), capabilities);
                    }
                } else {
                    throw new NullPointerException("AppiumDriver创建失败,设备未连接电脑");
                }
            } else {
                throw new NullPointerException("AppiumDriver创建失败,Launcher was null");
            }
            Log.info(CLASS_NAME, "AppiumDriver创建成功");
        } catch (MalformedURLException | InterruptedException e) {
            Log.error(CLASS_NAME, "AppiumDriver创建失败", e);
        }
        return driver;
    }

}
