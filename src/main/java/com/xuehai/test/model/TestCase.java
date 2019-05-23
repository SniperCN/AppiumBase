package com.xuehai.test.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName TestCase
 * @Description:    测试用例实体类
 * @Author Sniper
 * @Date 2019/5/17 15:26
 */
public class TestCase {

    @JSONField(name = "name")
    private String name;
    @JSONField(name = "dataList")
    private List<Map<String, JSONObject>> dataList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, JSONObject>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, JSONObject>> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "TestCase{" +
                "name='" + name + '\'' +
                ", dataList=" + dataList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCase testCase = (TestCase) o;
        return Objects.equals(name, testCase.name) &&
                Objects.equals(dataList, testCase.dataList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dataList);
    }

}
