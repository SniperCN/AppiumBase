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
 * @ClassName InvisibilityOfElementWithText
 * @Description:    控件text属性校验
 * @Author Sniper
 * @Date 2019/5/22 10:46
 */
public class InvisibilityOfElementWithText implements AssertionHandler {

	@Override
	public Object assertion(AppiumDriver<MobileElement> appiumDriver, AssertionCommand assertionCommand)
			throws JSONException, WebDriverException {
		JSONObject params = assertionCommand.params();
		String text = params.getString("text");
		return new AppiumDriverWait(appiumDriver).until(ExpectedConditions.invisibilityOfElementWithText(assertionCommand.assertBy(),
				text));
	}

}
