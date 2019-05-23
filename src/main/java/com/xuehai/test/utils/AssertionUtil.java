package com.xuehai.test.utils;

import org.assertj.core.api.Assertions;

/**
 * @ClassName AssertionUtil
 * @Description:    断言工具类,基于assertJ
 * @Author Sniper
 * @Date 2019/4/18 17:10
 */
public class AssertionUtil {

    /**
     * @description:    对象equals断言
     * @param description   断言描述
     * @param actual        实际结果
     * @param expect        预期结果
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/4/19 10:12
     */
    public static void assertion(String description, Object actual, Object expect) {
        Assertions.assertThat(actual)
                .as(description)
                .withFailMessage("Expect:%s, Actual:%s", expect, actual)
                .isEqualTo(expect);
    }

    /**
     * @description:        条件断言
     * @param description   断言描述
     * @param condition     条件
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/14 19:23
     */
    public static void assertion(String description, boolean condition) {
        Assertions.assertThat(condition)
                .as(description)
                .withFailMessage("False")
                .isTrue();
    }
}
