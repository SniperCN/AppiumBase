package com.xuehai.test.base;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.openqa.selenium.By;

/**
 * @ClassName AssertionCommand
 * @Description:    断言命令行
 * @Author Sniper
 * @Date 2019/5/22 10:53
 */
public class AssertionCommand {

    private JSONObject assertionCommand;

    public AssertionCommand(String command) throws JSONException {
        assertionCommand = JSONObject.parseObject(command);
    }

    public String action() {
        return assertionCommand.getString("action");
    }

    public By assertBy() throws JSONException {
        JSONObject locator = assertionCommand.getJSONObject("locator");
        String type = locator.getString("type");
        String value = locator.getString("value");
        switch (type) {
            case "id" :
                return By.id(value);
            case "xpath" :
                return By.xpath(value);
            case "className" :
                return By.className(value);
            default :
                return null;
        }
    }

    public JSONObject params() throws JSONException {
        return assertionCommand.getJSONObject("params");
    }

    @Override
    public String toString() {
        return "AssertionCommand{" +
                "assertionCommand=" + assertionCommand +
                '}';
    }

}
