package com.example.sample.persist.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity,Long> {
     List<DividendEntity> findAllByCompanyId(Long id);

     boolean existsByCompanyIdAndDate(Long companyId, LocalDateTime date);


     @Transactional
     void deleteAllByCompanyId(Long id);
}
