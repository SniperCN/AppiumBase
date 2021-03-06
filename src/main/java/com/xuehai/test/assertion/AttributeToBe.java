package com.xuehai.test.assertion;

import com.xuehai.test.base.AppiumDriverWait;
import com.xuehai.test.base.AssertionCommand;
import com.xuehai.test.base.AssertionHandler;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

/**
 * @ClassName AttributeToBe
 * @Description:    控件属性ToBe校验
 * @Author Sniper
 * @Date 2019/5/22 10:46
 */
public class AttributeToBe implements AssertionHandler {

	@Override
	public Object assertion(AppiumDriver<MobileElement> appiumDriver, AssertionCommand assertionCommand)
			throws JSONException, WebDriverException {
		JSONObject params = assertionCommand.params();
		String attribute = params.getString("attribute");
		String value = params.getString("value");
		return new AppiumDriverWait(appiumDriver).until(ExpectedConditions.attributeToBe(assertionCommand.assertBy(),
				attribute, value));
	}
}
