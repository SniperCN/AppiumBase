package com.xuehai.test.base;

import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Sleeper;
import java.time.Clock;
import java.time.Duration;

/**
 * @ClassName AppiumDriverWait
 * @Description:    显式等待
 * @Author Sniper
 * @Date 2019/5/17 14:02
 */
public class AppiumDriverWait<T> extends FluentWait<T> {

    private static final int DEFAULT_POLLINGEVERY_MILLISECONDS = 500;
    private static final int DEFAULT_TIMEOUT_INSECONDS = 10;

    public AppiumDriverWait(T t) {
        this(t, DEFAULT_TIMEOUT_INSECONDS, DEFAULT_POLLINGEVERY_MILLISECONDS);
    }

    public AppiumDriverWait(T t, int timeOutInSeconds, int pollingTime) {
        super(t, Clock.systemDefaultZone(), Sleeper.SYSTEM_SLEEPER);
        withTimeout(Duration.ofSeconds(timeOutInSeconds));
        pollingEvery(Duration.ofMillis(pollingTime));
        ignoring(NotFoundException.class);
    }
}
