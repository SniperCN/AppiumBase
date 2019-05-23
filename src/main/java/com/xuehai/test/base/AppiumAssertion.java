package com.xuehai.test.base;

import com.alibaba.fastjson.JSONException;
import com.xuehai.test.assertion.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import java.util.HashMap;

/**
 * @ClassName AppiumAssertion
 * @Description:    断言处理
 * @Author Sniper
 * @Date 2019/5/23 13:38
 */
public class AppiumAssertion {
    private static final String CLASS_NAME = AppiumAssertion.class.getName();
    private static HashMap<String, AssertionHandler> map = new HashMap<>();

    static {
        map.put("attributeContains", new AttributeContains());
        map.put("attributeToBe", new AttributeToBe());
        map.put("elementSelectionStateToBe", new ElementSelectionStateToBe());
        map.put("invisibilityOfElementLocated", new InvisibilityOfElementLocated());
        map.put("invisibilityOfElementWithText", new InvisibilityOfElementWithText());
        map.put("presenceOfElementLocated", new PresenceOfElementLocated());
    }

    public static Boolean assertion(AppiumDriver<MobileElement> driver, AssertionCommand command)
            throws JSONException, WebDriverException {
        Log.info(CLASS_NAME, "执行断言,断言信息:()", command.toString());
        if (map.containsKey(command.action())) {
            Object obj = map.get(command.action()).assertion(driver, command);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            } else {
                WebElement element = (WebElement) map.get(command.action()).assertion(driver, command);
                if (element != null) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            throw new IllegalArgumentException("无效的断言信息");
        }
    }
}
