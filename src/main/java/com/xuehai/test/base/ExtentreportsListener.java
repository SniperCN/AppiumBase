package com.xuehai.test.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.xuehai.test.utils.CommonUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlClass;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ExtentreportsListener
 * @Description:    extentreports报告监听器
 * @Author Sniper
 * @Date 2019/4/16 15:04
 */
public class ExtentreportsListener extends BaseCase implements ITestListener {

    private static final String CLASS_NAME = ExtentreportsListener.class.getName();
    private static final ExtentReports EXTENT_REPORTS = ExtentManager.getInstance();
    private static ThreadLocal<Map<String, ExtentTest>> parent = new ThreadLocal<>();
    private static ThreadLocal<Map<Integer, ExtentTest>> child = new ThreadLocal<>();
    private static String methodInfo;
    private int id;

    @Override
    public void onFinish(ITestContext context) {
        EXTENT_REPORTS.flush();
        Log.info(CLASS_NAME, "TestSuite: {} 执行完毕", context.getSuite().getName());
    }

    @Override
    public void onStart(ITestContext context) {
        Log.info(CLASS_NAME, "TestSuite: {} 开始执行", context.getSuite().getName());
        List<XmlClass> className = context.getCurrentXmlTest().getClasses();
        String contextName = context.getName();
        Map<String, ExtentTest> map = new HashMap<>();
        for (XmlClass clazz : className) {
            ExtentTest parentTest = EXTENT_REPORTS.createTest(clazz.getName()).assignCategory(contextName);
            map.put(clazz.getName(), parentTest);
            parent.set(map);
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        child.get().get(id).pass(methodInfo + "测试通过");
        Log.info(CLASS_NAME, "{}测试通过", methodInfo);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        child.get().get(id).fail(result.getThrowable());
        Log.error(CLASS_NAME, "{}测试失败", methodInfo);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        child.get().get(id).skip(result.getThrowable());
        Log.error(CLASS_NAME, "测试阻塞", methodInfo);
    }

    @Override
    public void onTestStart(ITestResult result) {
        String className = result.getTestClass().getName();
        String caseName = CaseTempInfo.getCaseMap().get(className);
        caseName = (caseName != null) ? caseName + "(" + className + ")" : className;
        ExtentTest parentTest = parent.get().get(className);
        parentTest.getModel().setName(caseName);
        String methodName = result.getMethod().getMethodName();
        Object[] parameter = result.getParameters();
        methodInfo = getMethodInfo(methodName, parameter);
        id = className.hashCode() + methodInfo.hashCode();
        Map<Integer, ExtentTest> cMap = child.get();
        if (cMap == null || !cMap.containsKey(id)) {
            ExtentTest childTest = parentTest.createNode(methodInfo);
            Map<Integer, ExtentTest> map = new HashMap<>();
            map.put(id, childTest);
            child.set(map);
        }
        Log.info(CLASS_NAME, "{}开始执行测试", methodInfo);
    }

    private String getMethodInfo(String methodName, Object[] parameter) {
        if (parameter.length < 1) {
            return methodName + "()";
        } else if (parameter.length == 1) {
            Object obj = parameter[0];
            return methodName + "(" + obj + ")";
        } else {
            StringBuilder temp = new StringBuilder();
            temp.append(methodName).append("(");
            for (Object obj : parameter) {
                temp.append(obj)
                        .append(",");
            }
            temp.append(")");
            return CommonUtil.format(temp);
        }
    }


}
