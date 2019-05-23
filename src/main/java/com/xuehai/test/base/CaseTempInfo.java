package com.xuehai.test.base;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CaseTempInfo
 * @Description:    测试用例临时信息
 * @Author Sniper
 * @Date 2019/4/16 16:13
 */
public class CaseTempInfo {

    private static Map<String, String> caseMap;

    /**
     * @description:    获取用例<类名, 用例名称> map
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @throws
     * @author Sniper
     * @date 2019/4/17 11:15
     */
    public static Map<String, String> getCaseMap() {
        if (caseMap == null) {
            caseMap = new HashMap<>();
        }
        return caseMap;
    }
}
