package com.xuehai.test.assertion;

import com.xuehai.test.base.AppiumDriverWait;
import com.xuehai.test.base.AssertionCommand;
import com.xuehai.test.base.AssertionHandler;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.alibaba.fastjson.JSONException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

/**
 * @ClassName InvisibilityOfElementLocated
 * @Description:    控件是否可定位校验
 * @Author Sniper
 * @Date 2019/5/22 10:46
 */
public class InvisibilityOfElementLocated implements AssertionHandler {

	@Override
	public Object assertion(AppiumDriver<MobileElement> appiumDriver, AssertionCommand assertionCommand)
			throws JSONException, WebDriverException {
		return new AppiumDriverWait(appiumDriver).until(ExpectedConditions.invisibilityOfElementLocated(assertionCommand.assertBy()));
	}

}
