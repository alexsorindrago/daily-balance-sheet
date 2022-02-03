package com.fm.daily.balance.sheet.repository;

import com.fm.daily.balance.sheet.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("FROM Invoice i WHERE i.customerId = :customerId")
    List<Invoice> findAllByCustomerId(@Param("customerId") Long customerId);

    @Query("FROM Invoice i WHERE i.customerId = :customerId AND i.dueDate BETWEEN :startDate AND :endDate")
    List<Invoice> findAllByCustomerIdBetween(@Param("customerId") Long customerId,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    @Query("FROM Invoice i WHERE i.supplierId = :supplierId")
    List<Invoice> findAllBySupplierId(@Param("supplierId") Long supplierId);

    @Query("FROM Invoice i WHERE i.salesmanId = :salesmanId")
    List<Invoice> findAllBySalesmanId(@Param("salesmanId") Long salesmanId);
}
