package com.xuehai.test.base;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.xuehai.test.model.Launcher;
import com.xuehai.test.model.TestCase;
import com.xuehai.test.utils.FileUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

import java.util.*;

/**
 * @ClassName BaseCase
 * @Description:    测试用例基础类
 * @Author Sniper
 * @Date 2019/5/17 15:38
 */
public class BaseCase {

    private static final String CLASS_NAME = BaseCase.class.getName();
    private TestCase testCase;
    private String suiteName;
    private static Map<String, AppiumDriver<MobileElement>> driverMap;

    protected String getSuiteName() {
        return suiteName;
    }

    protected Map<String, AppiumDriver<MobileElement>> getDriverMap() {
        return driverMap;
    }

    @BeforeClass
    protected void beforeClass(ITestContext context) {
        Log.info(CLASS_NAME, "测试类{}开始执行", this.getClass().getName());
        suiteName = context.getSuite().getName();
        //初始化driver
        if (driverMap == null) {
            String launcherPath = (String) Configuration.getConfig().get("launcher-path");
            Object launcherMap = Configuration.getConfig().get("launcher");
            if (launcherMap != null) {
                Object launchers = ((Map) launcherMap).get(suiteName);
                if (launchers != null) {
                    driverMap = new HashMap<>();
                    List launcherList = (List) launchers;
                    launcherList.forEach(launcherName -> {
                        Launcher launcher = parseLauncher(launcherPath, (String) launcherName);
                        AppiumDriver driver = Driver.createAppiumDriver(launcher);
                        driverMap.put((String) launcherName, driver);
                    });
                } else {
                    throw new NullPointerException("Suite对应的Launcher配置不存在,SuiteName:" + suiteName);
                }
            } else {
                throw new NullPointerException("Launcher配置不允许为Null");
            }
        }
        //初始化TestCase
        testCase = parseTestCase(suiteName, this.getClass().getName());
        //把测试用例名称添加到map中,key为className
        Map<String, String> caseMap = CaseTempInfo.getCaseMap();
        caseMap.put(getClass().getName(), testCase.getName());
    }

    @AfterClass
    protected void afterClass() {
        Log.info(CLASS_NAME, "测试类{}执行完毕", this.getClass().getName());
    }

    @AfterSuite
    protected void afterSuite() {
        closeDriver();
    }

    /**
     * @description: 测试数据初始化
     * @return java.util.Iterator<java.lang.Object[]>
     * @throws
     * @author Sniper
     * @date 2019/5/22 14:59
     */
    protected Iterator<Object[]> dataProvider() {
        List<Object[]> dataList = new ArrayList<>();
        testCase.getDataList().forEach(map -> dataList.add(new Object[]{map}));
        return dataList.iterator();
    }

    /**
     * @description: 关闭appiumdriver
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/23 9:37
     */
    private void closeDriver() {
        driverMap.forEach((key, value) -> {
            value.quit();
            Log.info(CLASS_NAME, "AppiumDriver已关闭,当前实例:{}", key);
        });
        driverMap = null;
    }

    /**
     * @description: 解析并加载测试用例
     * @param suiteName     suiteName
     * @param className     className
     * @return com.xuehai.test.model.TestCase
     * @throws
     * @author Sniper
     * @date 2019/5/22 14:45
     */
    private static TestCase parseTestCase(String suiteName, String className) {
        try {
            Log.info(CLASS_NAME, "开始解析测试用例,待加载用例SuiteName:{},ClassName:{}", suiteName, className);
            Object casePaths = Configuration.getConfig().get("case-path");
            if (casePaths != null) {
                String casePath = (String) ((Map) casePaths).get(suiteName);
                String caseJson = FileUtil.read(casePath, "UTF-8");
                JSONObject cases = JSONObject.parseObject(caseJson);
                JSONObject singleCase = cases.getJSONObject(className);
                if (singleCase != null) {
                    TestCase testCase = singleCase.toJavaObject(TestCase.class);
                    Log.info(CLASS_NAME, "{}({})测试用例加载成功", testCase.getName(), className);
                    return testCase;
                } else {
                    Log.error(CLASS_NAME, "测试用例加载失败,key:{}不存在", className);
                    return new TestCase();
                }
            } else {
                Log.error(CLASS_NAME, "case-path未配置");
                return new TestCase();
            }
        } catch (JSONException e) {
            Log.error(CLASS_NAME, "测试用例解析失败", e);
            return new TestCase();
        }
    }

    /**
     * @description: 解析加载launcher
    * @param path      launcher文件路径
     * @param key      launcher key
     * @return com.xuehai.test.model.Launcher
     * @throws
     * @author Sniper
     * @date 2019/5/23 9:15
     */
    private static Launcher parseLauncher(String path, String key) throws JSONException {
        String launcherJson = FileUtil.read(path, "UTF-8");
        JSONObject launcher = JSONObject.parseObject(launcherJson).getJSONObject(key);
        if (launcher != null) {
            return launcher.toJavaObject(Launcher.class);
        } else {
            throw new NullPointerException("Launcher信息为Null,key:" + key);
        }
    }


}
