package com.jone.smoke.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

public class SmokeExpertVo implements Serializable {
    private String unit;
    private String name;
    private Integer num;
    private BigDecimal cost;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
