package com.xuehai.test.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Objects;

/**
 * @ClassName Locator
 * @Description:    定位器实体类
 * @Author Sniper
 * @Date 2019/5/16 20:06
 */
public class Locator {
    @JSONField(name = "locatorName")
    private String locatorName;
    @JSONField(name = "fieldName")
    private String fieldName;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "value")
    private String value;

    public Locator() {

    }

    public Locator(String value) {
        this.type = "ID";
        this.value = value;
    }

    public Locator(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getLocatorName() {
        return locatorName;
    }

    public void setLocatorName(String locatorName) {
        this.locatorName = locatorName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Locator{" +
                "locatorName='" + locatorName + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locator locator = (Locator) o;
        return Objects.equals(locatorName, locator.locatorName) &&
                Objects.equals(fieldName, locator.fieldName) &&
                Objects.equals(type, locator.type) &&
                Objects.equals(value, locator.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locatorName, fieldName, type, value);
    }
}
