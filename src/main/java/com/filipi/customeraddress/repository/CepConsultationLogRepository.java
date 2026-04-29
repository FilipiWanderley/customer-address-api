package com.filipi.customeraddress.repository;

import com.filipi.customeraddress.model.CepConsultationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CepConsultationLogRepository extends JpaRepository<CepConsultationLog, Long> {

    List<CepConsultationLog> findByZipCodeOrderByConsultedAtDesc(String zipCode);
}
