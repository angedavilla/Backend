package com.campana.crm.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campana.crm.Entity.CampanaEntity;

public interface CampanaRepository extends JpaRepository<CampanaEntity, Long> {

	long count();
	
	List<CampanaEntity> findByFechaBetween(LocalDate startDate, LocalDate endDate);
	List<CampanaEntity> findByNombresContainingIgnoreCase(String nombres);
}
