package com.xuehai.test.base;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.xuehai.test.utils.FileUtil;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName ExtentManager
 * @Description:    extentreports对象管理
 * @Author Sniper
 * @Date 2019/4/16 14:27
 */
public class ExtentManager {

    private static final String CLASS_NAME = ExtentManager.class.getName();
    private static ExtentReports extent;

    /**
     * @description:    初始化extentreports实例
     * @return com.aventstack.extentreports.ExtentReports
     * @throws
     * @author Sniper
     * @date 2019/4/19 10:10
     */
    public static ExtentReports getInstance() {
        if(extent == null) {
            try {
                String reportPath = "report/UIAutomationReport.html";
                if (new File("report").exists()) {
                    FileUtil.deleteDirectory(new File("report"));
                }
                ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportPath);
                String configPath = (String) Configuration.getConfig().get("extentreports-config-path");
                htmlReporter.loadXMLConfig(configPath);
                extent = new ExtentReports();
                extent.attachReporter(htmlReporter);
                extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
            } catch (IOException e) {
                Log.error(CLASS_NAME, "历史报告删除异常,ExtentReports创建失败", e);
            }
        }
        return extent;
    }

}
