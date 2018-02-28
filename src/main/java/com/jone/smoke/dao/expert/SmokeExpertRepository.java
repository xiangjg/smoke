package com.jone.smoke.dao.expert;

import com.jone.smoke.entity.expert.SmokeExpert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SmokeExpertRepository extends JpaRepository<SmokeExpert, Integer>,JpaSpecificationExecutor<SmokeExpert> {

}
