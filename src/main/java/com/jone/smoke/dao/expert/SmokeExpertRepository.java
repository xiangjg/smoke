package com.jone.smoke.dao.expert;

import com.jone.smoke.entity.expert.SmokeExpert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface SmokeExpertRepository extends JpaRepository<SmokeExpert, Integer>,JpaSpecificationExecutor<SmokeExpert> {

    @Transactional
    @Modifying
    void deleteByIdIn(Collection ids);
    @Transactional
    @Modifying
    @Query("update SmokeExpert s set s.proName = :proName,s.expUnitSkill = :expUnitSkill,s.expNameSkill = :expNameSkill,s.reviewType = :reviewType,s.reviewCost = :reviewCost where s.id = :id")
    int updateSmokeExpertById(@Param("proName") String proName, @Param("expUnitSkill") String expUnitSkill, @Param("expNameSkill") String expNameSkill, @Param("reviewType") Integer reviewType, @Param("reviewCost") BigDecimal reviewCost, @Param("id") Integer id);

}
