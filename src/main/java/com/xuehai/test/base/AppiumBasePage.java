package com.xuehai.test.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.xuehai.test.model.Locator;
import com.xuehai.test.utils.FileUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.imagecomparison.OccurrenceMatchingResult;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AppiumBasePage
 * @Description:    提供基础页面公共方法
 * @Author Sniper
 * @Date 2019/5/16 20:35
 */
public class AppiumBasePage {

    private static final String CLASS_NAME = AppiumBasePage.class.getName();
    private AppiumDriver<MobileElement> driver;
    private Map<String, Locator> locators;

    protected AppiumBasePage(AppiumDriver<MobileElement> driver, String suiteName) {
        this.driver = driver;
        locators = getLocators(suiteName);
    }

    /**
     * @description: 加载定位器
     * @param field     字段名称
     * @return com.xuehai.test.model.Locator
     * @throws
     * @author Sniper
     * @date 2019/5/22 13:44
     */
    protected Locator getLocator(String field) {
        Log.info(CLASS_NAME, "开始加载定位器{}", field);
        if (locators != null) {
            if (locators.containsKey(field)) {
                Locator locator = locators.get(field);
                Log.info(CLASS_NAME, "定位器{}({})已加载", field, locator.getLocatorName());
                return locator;
            } else {
                Log.error(CLASS_NAME, "{}定位器加载失败,key不存在", field);
                return null;
            }
        } else {
            Log.error(CLASS_NAME, "{}定位器加载失败,page locators为空", field);
            return null;
        }
    }

    /**
     * @description:   解析页面定位器
     * @param suiteName     suiteName
     * @return java.util.Map<java.lang.String,com.xuehai.test.model.Locator>
     * @throws
     * @author Sniper
     * @date 2019/5/22 13:45
     */
    protected Map<String, Locator> getLocators(String suiteName) {
        try {
            Log.info(CLASS_NAME, "开始解析页面定位器,SuiteName:{},目标页面ClassName:{}", suiteName,
                    this.getClass().getName());
            Map<String, Locator> locatorMap = new HashMap<>();
            Object locatorPathMap = Configuration.getConfig().get("locator-path");
            if (locatorPathMap != null) {
                String locatorPath = (String)((Map) locatorPathMap).get(suiteName);
                Log.info(CLASS_NAME, "当前LocatorPath:{}", locatorPath);
                String pageLocatorJson = FileUtil.read(locatorPath, "UTF-8");
                JSONObject page = JSONObject.parseObject(pageLocatorJson).getJSONObject(this.getClass().getName());
                if (page != null) {
                    JSONArray locators = page.getJSONArray("locators");
                    if (locators != null) {
                        locators.toJavaList(Locator.class).forEach(locator -> locatorMap.put(locator.getFieldName(), locator));
                        Log.info(CLASS_NAME, "{}({})页面定位器解析成功", this.getClass().getName(), page.get("name"));
                        return locatorMap;
                    } else {
                        throw new NullPointerException("Locator解析失败,数据结构中不存在locators字段");
                    }
                } else {
                    throw new NullPointerException("Locator解析失败,页面定位数据不存在,当前ClassName:" + this.getClass().getName());
                }
            } else {
                Log.error(CLASS_NAME, "Locator解析失败,locator-path未配置");
                return null;
            }
        } catch (JSONException e) {
            Log.error(CLASS_NAME,"Locator解析失败", e);
            return null;
        }
    }

    @SuppressWarnings("Duplicates")
    private MobileElement getElement(AppiumDriver<MobileElement> driver, Locator locator)
            throws NotFoundException {
        if (driver != null && locator != null) {
            switch (locator.getType()) {
                case "id":
                    return driver.findElement(By.id(locator.getValue()));
                case "xpath":
                    return driver.findElement(By.xpath(locator.getValue()));
                case "className":
                    return driver.findElement(By.className(locator.getValue()));
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    @SuppressWarnings("Duplicates")
    private MobileElement getElement(MobileElement element, Locator locator) throws NotFoundException {
        if (driver != null && locator != null) {
            switch (locator.getType()) {
                case "id":
                    return element.findElement(By.id(locator.getValue()));
                case "xpath":
                    return element.findElement(By.xpath(locator.getValue()));
                case "className":
                    return element.findElement(By.className(locator.getValue()));
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    @SuppressWarnings("Duplicates")
    private List<MobileElement> getElements(AppiumDriver<MobileElement> driver, Locator locator)
            throws NotFoundException {
        if (driver != null && locator != null) {
            switch (locator.getType()) {
                case "id":
                    return driver.findElements(By.id(locator.getValue()));
                case "xpath":
                    return driver.findElements(By.xpath(locator.getValue()));
                case "className":
                    return driver.findElements(By.className(locator.getValue()));
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    @SuppressWarnings("Duplicates")
    private List<MobileElement> getElements(MobileElement element, Locator locator) throws NotFoundException {
        if (driver != null && locator != null) {
            switch (locator.getType()) {
                case "id":
                    return element.findElements(By.id(locator.getValue()));
                case "xpath":
                    return element.findElements(By.xpath(locator.getValue()));
                case "className":
                    return element.findElements(By.className(locator.getValue()));
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @description: 查找控件
     * @param appiumDriver    appiumDriver
     * @param locator         定位器
     * @return io.appium.java_client.MobileElement
     * @throws
     * @author Sniper
     * @date 2019/5/22 13:46
     */
    @SuppressWarnings("Duplicates")
    protected MobileElement findElement(AppiumDriver<MobileElement> appiumDriver, final Locator locator) {
        return new AppiumDriverWait<>(appiumDriver).until(driver -> {
            try {
                return getElement(driver, locator);
            } catch (TimeoutException e) {
                Log.error(CLASS_NAME, locator + "识别超时,当前页面未找到该控件", e);
                return null;
            }
        });
    }

    /**
     * @description: 框架下查找控件
     * @param mobileElement     上层元素
     * @param locator           定位器
     * @return io.appium.java_client.MobileElement
     * @throws
     * @author Sniper
     * @date 2019/5/22 13:47
     */
    @SuppressWarnings("Duplicates")
    protected MobileElement findElement(MobileElement mobileElement, final Locator locator) {
        return new AppiumDriverWait<>(mobileElement).until(element -> {
            try {
                return getElement(element, locator);
            } catch (TimeoutException e) {
                Log.error(CLASS_NAME, locator + "识别超时,当前页面未找到该控件", e);
                return null;
            }
        });
    }

    /**
     * @description: 查找控件集合
     * @param appiumDriver        driver
     * @param locator       定位器
     * @return java.util.List<io.appium.java_client.MobileElement>
     * @throws
     * @author Sniper
     * @date 2019/5/22 13:49
     */
    @SuppressWarnings("Duplicates")
    protected List<MobileElement> findElements(AppiumDriver<MobileElement> appiumDriver, final Locator locator) {
        return new AppiumDriverWait<>(appiumDriver).until(driver -> {
            try {
                return getElements(driver, locator);
            } catch (TimeoutException e) {
                Log.error(CLASS_NAME, locator + "识别超时,当前页面未找到该控件", e);
                return null;
            }
        });
    }

    /**
     * @description: 框架下查找控件
     * @param mobileElement       上层元素
     * @param locator       定位器
     * @return java.util.List<io.appium.java_client.MobileElement>
     * @throw
     * @author Sniper
     * @date 2019/5/22 13:50
     */
    @SuppressWarnings("Duplicates")
    protected List<MobileElement> findElements(MobileElement mobileElement, final Locator locator) {
        return new AppiumDriverWait<>(mobileElement).until(element -> {
            try {
                return getElements(element, locator);
            } catch (TimeoutException e) {
                Log.error(CLASS_NAME, locator + "识别超时,当前页面未找到该控件", e);
                return null;
            }
        });
    }

    /**
     * @description: 点击
     * @param locator   定位器
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/22 13:57
     */
    protected void click(Locator locator) {
        MobileElement mobileElement = findElement(driver, locator);
        if (mobileElement != null) {
            mobileElement.click();
            Log.info(CLASS_NAME, "点击:" + locator.getLocatorName());
        }
    }

    /**
     * @description: 输入
     * @param locator       定位器
     * @param inputValue    输入内容
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/22 13:57
     */
    protected void sendKeys(Locator locator, String inputValue) {
        MobileElement mobileElement = findElement(driver, locator);
        if (mobileElement != null) {
            mobileElement.sendKeys(inputValue);
            Log.info(CLASS_NAME, locator.getLocatorName() + "输入:" + inputValue);
        }
    }

    protected void clear(Locator locator) {
        MobileElement mobileElement = findElement(driver, locator);
        if (mobileElement != null) {
            mobileElement.clear();
            Log.info(CLASS_NAME, "\"" + locator.getLocatorName() + "\" 内容清除成功");
        }
    }

    /**
     * @description: 滑动
     * @param startX        起始x坐标
     * @param startY        起始y坐标
     * @param endX          结束x坐标
     * @param endY          结束y坐标
     * @param duration      滑动持续时间
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/22 13:57
     */
    protected void swipe(int startX, int startY, int endX, int endY, long duration) {
        AndroidTouchAction action = new AndroidTouchAction(driver);
        Duration durations = Duration.ofMillis(duration);
        action.press(PointOption.point(startX, startY))
            .waitAction(WaitOptions.waitOptions(durations))
            .moveTo(PointOption.point(endX, endY))
            .release()
            .perform();
        Log.info(CLASS_NAME, "页面滑动成功,从({},{})滑动到({},{})", startX, startY, endX, endY);
    }

    /**
     * @description: 滑动
     * @param direction     方向
     * @param duration      滑动持续时间
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/22 13:59
     */
    protected void swipe(String direction, long duration) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        if(direction.equalsIgnoreCase("up")){
            swipe(width/2, height/4, width/2, height*3/4, duration);
        }else if(direction.equalsIgnoreCase("down")){
            swipe(width/2, height*3/4, width/2, height/4, duration);
        }else if(direction.equalsIgnoreCase("left")){
            swipe(width/15, height/4, width*14/15, height/4, duration);
        }else if(direction.equalsIgnoreCase("right")){
            swipe(width*14/20, height/4, width/15, height/4, duration);
        }else{
            Log.error(CLASS_NAME, "滑动失败，方向:{}未定义", direction);
        }
    }

    /**
     * @description: 页面截屏
     * @param fileName      文件保存名称
     * @return String
     * @throws
     * @author Sniper
     * @date 2019/5/22 13:59
     */
    protected String captureScreenShot (String fileName) {
        try {
            Log.info(CLASS_NAME, "开始截图,save name:{}", fileName);
            File screenshot = driver.getScreenshotAs(OutputType.FILE);
            String screenshotPath = "screenshot/" + fileName + ".png";
            FileUtils.copyFile(screenshot, new File(screenshotPath));
            Log.info(CLASS_NAME, "截图成功");
            return screenshotPath;
        } catch (IOException e) {
            Log.error(CLASS_NAME, "截图失败", e);
            return null;
        }
    }

    /**
     * @description: 坐标点击
     * @param x     x轴坐标
     * @param y     y轴坐标
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/22 14:05
     */
    protected void tap (int x, int y) {
        new AndroidTouchAction(driver).tap(PointOption.point(x, y)).perform();
    }

    /**
     * @description: 匹配局部图片并点击
     * @param fullImage         全图
     * @param partialImage      局部图片
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/22 14:06
     */
    protected void tap (File fullImage, File partialImage) {
        OccurrenceMatchingResult result = findImageOccurrence(fullImage, partialImage);
        if (result != null) {
            Rectangle rectangle = result.getRect();
            if (rectangle != null) {
                int x = rectangle.getX() + rectangle.getWidth()/2;
                int y = rectangle.getY() + rectangle.getHeight()/2;
                new AndroidTouchAction(driver).tap(PointOption.point(x, y)).perform();
            }
        }
    }

    /**
     * @description: 局部图片匹配
     * @param originalImage         原图
     * @param partialImage      局部图片
     * @return io.appium.java_client.imagecomparison.OccurrenceMatchingResult
     * @throws
     * @author Sniper
     * @date 2019/5/22 14:07
     */
    protected OccurrenceMatchingResult findImageOccurrence(File originalImage, File partialImage) {
        OccurrenceMatchingResult result = null;
        try {
            Log.info(CLASS_NAME, "开始进行图片局部匹配,原图路径:{},局部图片路径:{}", originalImage.getAbsolutePath(),
                    partialImage.getAbsolutePath());
            result = driver.findImageOccurrence(originalImage, partialImage);
            Log.info(CLASS_NAME, "图片局部匹配完成");
        } catch (IOException e) {
            Log.error(CLASS_NAME, "图片局部匹配失败", e);
        }
        return result;
    }

    /**
     * @description: 图片相似度比较
     * @param originalImage        原图
     * @param similarImage         相似图片
     * @return double
     * @throws
     * @author Sniper
     * @date 2019/5/22 14:08
     */
    protected double getImagesSimilarity(File originalImage, File similarImage) {
        double score = 0.0;
        try {
            Log.info(CLASS_NAME, "开始进行图片相似度比较,原图路径:{},相似图片路径:{}", originalImage.getAbsolutePath(),
                    similarImage.getAbsolutePath());
            score = driver.getImagesSimilarity(originalImage, similarImage).getScore();
            Log.info(CLASS_NAME, "图片相似度比较完成,相似度:{}", score);
        } catch (IOException e) {
            Log.error(CLASS_NAME, "图片相似度比较失败", e);
        }
        return score;
    }

}
