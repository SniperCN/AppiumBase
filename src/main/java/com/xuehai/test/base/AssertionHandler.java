package com.xuehai.test.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

/**
 * @ClassName AssertionHandler
 * @Description:    断言处理器
 * @Author Sniper
 * @Date 2019/5/22 10:46
 */
public interface AssertionHandler {

    Object assertion(AppiumDriver<MobileElement> appiumDriver, AssertionCommand assertionCommand);

}
