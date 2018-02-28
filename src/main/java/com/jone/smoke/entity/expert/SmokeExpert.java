package com.jone.smoke.entity.expert;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="s_expert",schema="sysdb")
public class SmokeExpert implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    @Column(name="project_name",length = 50)
    private String proName;
    /**
     * 评审类别：1-立项评审 2-检查评价 3-变更评审 4-撤销评审 5-结题评审
     */
    @Column(name="review_type")
    private Integer reviewType;
    @Column(name="review_time")
    private Date reviewTime;
    @Column(name="expert_name_skill")
    private String expNameSkill;
    @Column(name="expert_unit_skill")
    private String expUnitSkill;
    @Column(name="expert_name_manage")
    private String expNameManage;
    @Column(name="expert_unit_manage")
    private String expUnitManage;
    @Column(name="review_cost")
    private BigDecimal reviewCost;
    @Transient//非数据库字段
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public Integer getReviewType() {
        return reviewType;
    }

    public void setReviewType(Integer reviewType) {
        this.reviewType = reviewType;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getExpNameSkill() {
        return expNameSkill;
    }

    public void setExpNameSkill(String expNameSkill) {
        this.expNameSkill = expNameSkill;
    }

    public String getExpUnitSkill() {
        return expUnitSkill;
    }

    public void setExpUnitSkill(String expUnitSkill) {
        this.expUnitSkill = expUnitSkill;
    }

    public String getExpNameManage() {
        return expNameManage;
    }

    public void setExpNameManage(String expNameManage) {
        this.expNameManage = expNameManage;
    }

    public String getExpUnitManage() {
        return expUnitManage;
    }

    public void setExpUnitManage(String expUnitManage) {
        this.expUnitManage = expUnitManage;
    }

    public BigDecimal getReviewCost() {
        return reviewCost;
    }

    public void setReviewCost(BigDecimal reviewCost) {
        this.reviewCost = reviewCost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
