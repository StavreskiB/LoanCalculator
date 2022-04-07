package com.loancalculator.Repository;

import com.loancalculator.Models.AmortizationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AmortizationScheduleRepository extends JpaRepository<AmortizationSchedule, Integer>, JpaSpecificationExecutor<AmortizationSchedule> {
}
