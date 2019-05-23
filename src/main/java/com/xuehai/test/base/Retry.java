package com.xuehai.test.base;

import com.xuehai.test.utils.CommonUtil;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * @ClassName Retry
 * @Description:    失败用例重跑
 * @Author Sniper
 * @Date 2019/4/4 16:28
 */
public class Retry implements IRetryAnalyzer {
    private static final String CLASS_NAME = Retry.class.getName();
    private static final int MAX_RETRY_COUNT = (int) Configuration.getConfig().get("max-retry-count");
    private int count = 1;


    @Override
    public boolean retry(ITestResult result) {
        if (count <= MAX_RETRY_COUNT) {
            Log.info(CLASS_NAME, "{}执行失败重跑,当前重跑次数: {}", result.getMethod().getMethodName(), count);
            CommonUtil.cmdExecutor("adb shell am force-stop com.xuehai.response_launcher_teacher");
            CommonUtil.cmdExecutor("adb shell am start "
                    + "com.xuehai.response_launcher_teacher/com.xuehai.launcher.library.welcome.ui.impl.WelcomeActivity");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Log.error(CLASS_NAME, "线程中断", e);
            }
            count ++;
            return true;
        } else {
            count = 1;
            return false;
        }
    }

}
