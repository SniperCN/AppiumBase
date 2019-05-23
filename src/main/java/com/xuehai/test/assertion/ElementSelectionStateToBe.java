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
 * @ClassName ElementSelectionStateToBe
 * @Description:    下拉框选中状态校验
 * @Author Sniper
 * @Date 2019/5/22 10:46
 */
public class ElementSelectionStateToBe implements AssertionHandler {

	@Override
	public Object assertion(AppiumDriver<MobileElement> appiumDriver, AssertionCommand assertionCommand)
			throws JSONException, WebDriverException {
		JSONObject params = assertionCommand.params();
		boolean selected = params.getBoolean("selected");
		return new AppiumDriverWait(appiumDriver).until(ExpectedConditions.elementSelectionStateToBe(assertionCommand.assertBy(),
				selected));
	}
}
